package ru.said.view;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.Window;
import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;

import org.apache.log4j.Logger;
import ru.said.SecurityUtils;
import ru.said.bean.User;
import ru.said.service.UserService;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class UserView {

    private Button add = new Button("Добавить");
    private Button delete = new Button("Удалить");
    private Button edit = new Button("Изменить");
    private Binder<User> binder = new Binder<>();
    private TextField search = new TextField();
    private ListDataProvider<User> dataProvider;
    private List<User> usersList = new ArrayList<>();
    private Grid<User> grid = new Grid<>();
    VerticalLayout verticalLayout = new VerticalLayout();
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    UserService userService = new UserService();
    MenuBar logoutMenu = new MenuBar();

    public static final String CURRENT_USER_SESSION_ATTRIBUTE_KEY = UserView.class
            .getCanonicalName();

    /*
     * добавляем логирование
     * */

    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(UserView.class);

    private Boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
    }

    private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
        dataProvider.setFilter(User::getUserName, s -> caseInsensitiveContains(s, event.getValue()));
    }

    public UserView() {
        MySub mySub1 = new MySub();
        try {
            mySub1.initDataProvider();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySub1.addColums();
        UI.getCurrent().addWindow(mySub1);
        MySub mySub2 = new MySub();
        add.addClickListener(clickEvent -> {
            mySub2.addUser();
            UI.getCurrent().addWindow(mySub2);
        });
        delete.addClickListener(clickEvent -> mySub2.deleteUser());
        edit.addClickListener(clickEvent -> {
            mySub2.editUser();
            UI.getCurrent().addWindow(mySub2);
        });
        logoutMenu.addItem("Logout", FontAwesome.SIGN_OUT, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                VaadinSession.getCurrent().getSession().invalidate();
                Page.getCurrent().reload();
            }
        });
        search.setWidth("157");
        search.addValueChangeListener(this::onNameFilterTextChange);
    }

    public static String get() {
        String currentUser = (String) getCurrentHttpSession().getAttribute(
                CURRENT_USER_SESSION_ATTRIBUTE_KEY);
        if (currentUser == null) {
            return "";
        } else {
            return currentUser;
        }
    }

    private static WrappedSession getCurrentHttpSession() {
        VaadinSession s = VaadinSession.getCurrent();
        if (s == null) {
            throw new IllegalStateException(
                    "No session found for current thread");
        }
        return s.getSession();
    }

    public static void set(String currentUser) {
        if (currentUser == null) {
            getCurrentHttpSession().removeAttribute(
                    CURRENT_USER_SESSION_ATTRIBUTE_KEY);
        } else {
            getCurrentHttpSession().setAttribute(
                    CURRENT_USER_SESSION_ATTRIBUTE_KEY, currentUser);
        }
    }

    class MySub extends Window {

        void initData2() {
            dataProvider = new ListDataProvider<>(usersList)
//                    @Override
//                    public Object getId(User item) {
//                        return item.getUser_name();
//                    }
            ;
            grid.setDataProvider(dataProvider);
        }

        void initDataProvider() throws SQLException {
            usersList = userService.getAll();
            LOGGER.debug("SELECT * FROM ddt_users");
            dataProvider = new ListDataProvider<User>(usersList);
            grid.setDataProvider(dataProvider);
            grid.setWidth("1000");
//                horizontalLayout.setComponentAlignment(add, Alignment.MIDDLE_CENTER);
            horizontalLayout.addComponents(add, delete, edit, search);
            verticalLayout.addComponents(horizontalLayout, grid, logoutMenu);
//                verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER); // для отображения содержимого в центре окна
//                verticalLayout.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);            // для отображения содержимого в центре окна
            setContent(verticalLayout);
        }

        void addColums() {
            grid.addColumn(User::getUserName).setCaption("Имя пользователя");
            grid.addColumn(User::getLogin).setCaption("Логин");
            grid.addColumn(User::getPassword).setCaption("Пароль");
        }

        void addUser() {
            Button save = new Button("Сохранить");
            final VerticalLayout layout2 = new VerticalLayout();
            final TextField userNameTxt = new TextField("Имя пользователя");
            final TextField userLoginTxt = new TextField("Логин");
            final PasswordField userPasswordTxt = new PasswordField("Пароль");
            save.addClickListener(clickEvent -> {
                binder.forField(userNameTxt)
                        .withValidator(value -> value.length() > 0, "Поле не должно быть пустым")
                        .bind(User::getUserName, User::setUserName);
                binder.forField(userLoginTxt)
                        .withValidator(value -> value.length() > 0, "Поле не должно быть пустым")
                        .bind(User::getLogin, User::setLogin);
                binder.forField(userPasswordTxt)
                        .withValidator(value -> value.length() > 7, "Количество символов без пробелов меньше 8 ")
                        .bind(User::getPassword, User::setPassword);
                String userName = userNameTxt.getValue();
                String login = userLoginTxt.getValue();
                String password = userPasswordTxt.getValue();
                BinderValidationStatus<User> status = binder.validate();
                try {
                    if (!status.hasErrors()) {
                        String password2 = SecurityUtils.hash(password);
                        usersList = userService.addRow(userName, login, password2);
                        LOGGER.debug("INSERT INTO ddt_users (user_name, login, password)" +
                                " VALUES ('" + userName + "', '" + login + "', '" + password2 + "')");
                        close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    LOGGER.error(e);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    LOGGER.error(e);
                }
                initData2();
            });
            layout2.addComponents(userNameTxt, userLoginTxt, userPasswordTxt, save);
            setContent(layout2);
        }

        void deleteUser() {
            String userName;
            try {
                userName = grid.getSelectionModel().getFirstSelectedItem().get().getUserName();
                usersList = userService.deleteRow(userName);
                LOGGER.debug("DELETE from ddt_users where user_name ='" + userName + "'");
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.error(e);
            }
            initData2();
        }

        void editUser() {
            Button save = new Button("Сохранить");
            final VerticalLayout layout2 = new VerticalLayout();
            final TextField userLoginTxt = new TextField("Логин");
            final TextField userPassordTxt = new TextField("Пароль");
            userLoginTxt.setValue(grid.getSelectedItems().iterator().next().getLogin());
            userPassordTxt.setValue(grid.getSelectedItems().iterator().next().getPassword());
            save.addClickListener(clickEvent -> {
                String user_name = grid.getSelectionModel().getFirstSelectedItem().get().getUserName();
                String login = userLoginTxt.getValue();
                String password = userPassordTxt.getValue();
                try {
                    String password2 = SecurityUtils.hash(password);
                    usersList = userService.editRow(user_name, login, password2);
                    LOGGER.debug("UPDATE ddt_users SET login ='" +
                            "" + login + "', password = '" + password + "" +
                            "' where user_name = '" + user_name + "'");
                } catch (SQLException e) {
                    e.printStackTrace();
                    LOGGER.error(e);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    LOGGER.error(e);
                }
                initData2();
                close();
            });
            layout2.addComponents(userLoginTxt, userPassordTxt, save);
            setContent(layout2);
        }

        MySub() {
            super("Пользователи");
            center();
            setSizeFull();
        }
    }
}