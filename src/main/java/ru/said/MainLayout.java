package ru.said;


import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import ru.said.bean.User;
import ru.said.service.UserService;

import static ru.said.DatabaseUtils.getConnection;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainLayout extends HorizontalLayout {

    private Button add = new Button("Добавить");
    private Button delete = new Button("Удалить");
    private Button edit = new Button("Изменить");
    private TextField search = new TextField();
    private ListDataProvider<User> dataProvider;
    private List<User> usersList = new ArrayList<>();
    private Grid<User> grid = new Grid<>();
    private Binder<User> binder = new Binder<>();
    private MenuBar logoutMenu = new MenuBar();
    private VerticalLayout verticalLayout = new VerticalLayout();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private static final Logger LOGGER = Logger.getLogger(MainLayout.class);


    public MainLayout() {

        try {
            usersList = UserService.getAll(getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logoutMenu.addItem("Logout", FontAwesome.SIGN_OUT, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                VaadinSession.getCurrent().getSession().invalidate();
                Page.getCurrent().reload();
            }
        });

        LOGGER.debug("SELECT * FROM ddt_users");
        dataProvider = new ListDataProvider<User>(usersList);
        grid.setDataProvider(dataProvider);
        grid.setWidth("1000");
        add.addClickListener(clickEvent -> {
            addUser();
        });
        delete.addClickListener(clickEvent -> deleteUser());
        edit.addClickListener(clickEvent -> editUser());
        horizontalLayout.addComponents(add, delete, edit, search);
        verticalLayout.addComponents(horizontalLayout, grid, logoutMenu);
        addComponent(verticalLayout);

        grid.addColumn(User::getUserName).setCaption("Имя пользователя");
        grid.addColumn(User::getLogin).setCaption("Логин");
        grid.addColumn(User::getPassword).setCaption("Пароль");
    }

    void initData2() {
        dataProvider = new ListDataProvider<>(usersList);
        grid.setDataProvider(dataProvider);
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
                    String password2 = UserService.hash(password);
                    usersList = UserService.addRow(getConnection(), userName, login, password2);
                    LOGGER.debug("INSERT INTO ddt_users (user_name, login, password)" +
                            " VALUES ('" + userName + "', '" + login + "', '" + password2 + "')");
                    //     close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
            }
            Page.getCurrent().reload();
            initData2();
        });
        layout2.addComponents(userNameTxt, userLoginTxt, userPasswordTxt, save);

        addComponent(layout2);

    }

    void deleteUser() {
        String userName;
        try {
            userName = grid.getSelectionModel().getFirstSelectedItem().get().getUserName();
            usersList = UserService.deleteRow(getConnection(), userName);
            LOGGER.debug("DELETE from ddt_users where user_name ='" + userName + "'");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
        }
        Page.getCurrent().reload();
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
                String password2 = UserService.hash(password);
                usersList = UserService.editRow(getConnection(), user_name, login, password2);
                LOGGER.debug("UPDATE ddt_users SET login ='" +
                        "" + login + "', password = '" + password + "" +
                        "' where user_name = '" + user_name + "'");
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
            }
            initData2();
            Page.getCurrent().reload();
        });
        layout2.addComponents(userLoginTxt, userPassordTxt, save);
        addComponent(layout2);
    }


}
