package ru.said.view;

import com.vaadin.ui.Window;
import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;

import org.apache.log4j.Logger;
import ru.said.bean.User;
import ru.said.service.UserService;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserView  {

    Button add = new Button("Добавить");
    Button delete = new Button("Удалить");
    Button edit = new Button("Изменить");
    private Binder<User> binder = new Binder<>();
    private TextField search = new TextField();
    private ListDataProvider<User> dataProvider;
    List<User> usersList = new ArrayList<>();
    private Grid<User> grid = new Grid<>();
    VerticalLayout verticalLayout = new VerticalLayout();
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    UserService userService = new UserService();

    private static final org.apache.log4j.Logger logger =  Logger.getLogger(UserView.class);

    private Boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
    }
    private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
        dataProvider.setFilter(User::getUser_name, s -> caseInsensitiveContains(s, event.getValue()));
    }

    public UserView() {
        MySub mySub1 = new MySub();
        try {
            mySub1.initDataProvider();
        } catch (SQLException e){
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
        search.setWidth("157");
        search.addValueChangeListener(this::onNameFilterTextChange);
    }

        class MySub extends Window {

            void initData2() {

                dataProvider = new ListDataProvider<User>(usersList){
//                    @Override
//                    public Object getId(User item) {
//                        return item.getUser_name();
//                    }
                };
                grid.setDataProvider(dataProvider);
            }

            void initDataProvider() throws SQLException {
                usersList = userService.getall();
                logger.debug("SELECT * FROM ddt_users");
                dataProvider = new ListDataProvider<User>(usersList){
//                    @Override
//                    public Object getId(User item) {
//                        return item.getUser_name();
//                    }
                };
                grid.setDataProvider(dataProvider);
                horizontalLayout.addComponents(add,delete,edit,search);
                verticalLayout.addComponents(horizontalLayout,grid);
                setContent(verticalLayout);
            }

            void addColums(){
                grid.addColumn(User::getUser_name).setCaption("User_name");
                grid.addColumn(User::getLogin).setCaption("Login");
                grid.addColumn(User::getPassword).setCaption("Password");
            }

            void addUser() {
                Button save = new Button("Save");
                final VerticalLayout layout2 = new VerticalLayout();
                final TextField user_nameTxt = new TextField("user_name");
                final TextField user_loginTxt = new TextField("user_login");
                final TextField user_PasswordTxt = new TextField("user_password");
                save.addClickListener(clickEvent -> {
                    binder.forField(user_nameTxt)
                      .withValidator(value -> value.length() > 0, "Поле не должно быть пустым")
                      .bind(User::getUser_name,User::setUser_name);
                    binder.forField(user_loginTxt)
                      .withValidator(value -> value.length() > 0, "Поле не должно быть пустым")
                      .bind(User::getLogin,User::setLogin);
                    binder.forField(user_PasswordTxt)
                      .withValidator(value -> value.length() > 7, "Количество символов без пробелов меньше 8 ")
                      .bind(User::getPassword,User::setPassword);
                    String userName = user_nameTxt.getValue();
                    String login = user_loginTxt.getValue();
                    String password = user_PasswordTxt.getValue();
                    BinderValidationStatus<User> status = binder.validate();
                    try {
                        if(!status.hasErrors()) {
                            usersList = userService.addRow(userName,login,password);
                            logger.debug("INSERT INTO ddt_users (user_name, login, password) VALUES ('"+ userName +"', '"+ login +"', '"+ password +"')");
                            close();
                        }
                    } catch (SQLException e){
                        e.printStackTrace();
                        logger.error(e);
                    }
                    initData2();
                });
                layout2.addComponents(user_nameTxt,user_loginTxt,user_PasswordTxt,save);
                setContent(layout2);
            }

            void deleteUser(){
                String user_name;
                try {
                    user_name = grid.getSelectionModel().getFirstSelectedItem().get().getUser_name();
                    usersList = userService.deleteRow(user_name);
                    logger.debug("DELETE from ddt_users where user_name ='"+ user_name+"'");
                } catch (SQLException e){
                    e.printStackTrace();
                    logger.error(e);
                }
                initData2();
            }

            void editUser() {
                Button save = new Button("Save");
                final VerticalLayout layout2 = new VerticalLayout();
           //     final TextField user_nameTxt = new TextField("user_name");
                final TextField user_loginTxt = new TextField("user_Login");
                final TextField user_passordTxt = new TextField("user_Password");
            //    user_nameTxt.setValue(grid.getSelectedItems().iterator().next().getUser_name());
                user_loginTxt.setValue(grid.getSelectedItems().iterator().next().getLogin());
                user_passordTxt.setValue(grid.getSelectedItems().iterator().next().getPassword());
                save.addClickListener(clickEvent ->{
                    String user_name = grid.getSelectionModel().getFirstSelectedItem().get().getUser_name();
                    String login = user_loginTxt.getValue();
                    String password = user_passordTxt.getValue();
                    try {
                        usersList = userService.editRow(user_name,login,password);
                        logger.debug("UPDATE ddt_users SET login ='"+ login +"', password = '"+ password +"' where user_name = '" + user_name +"'");
                    } catch (SQLException e){
                        e.printStackTrace();
                        logger.error(e);
                    }
                    initData2();
                    close();
                });
                layout2.addComponents( user_loginTxt,user_passordTxt,save);
                setContent(layout2);
            }

            MySub() {
                super("Users");
                center();
                setSizeFull();
            }
        }
}
