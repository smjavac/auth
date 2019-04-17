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
    List<User> branchesList = new ArrayList<>();
    private Grid<User> grid = new Grid<>();
    VerticalLayout verticalLayout = new VerticalLayout();
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    UserService userService = new UserService();

    private static final org.apache.log4j.Logger logger =  Logger.getLogger(UserView.class);
    private Boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
        private void onNameFilterTextChange ((HasValue.ValueChangeEvent < String) > event) {
            dataProvider.setFilter(User::getUser_name, s -> caseInsensitiveContains(s, event.getValue()));

        }
    }
}
