package ru.said;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import ru.said.view.UserView;
import ru.said.service.UserService;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class MainLayou2 extends VerticalLayout  {
    Button inter = new Button(" войти в систему");
    TextField user_login = new TextField("Имя пользователя");
    TextField user_password = new TextField("Пароль");
    UserService userService = new UserService();
    MainLayou2() {

        inter.addClickListener(clickEvent -> {
            String login = user_login.getValue();
            String password = user_password.getValue();
            try {
                if (userService.authentication(login, password)) {
                    new UserView();
                }
            } catch (SQLException e) {e.printStackTrace();} catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });
        addComponents(user_login, user_password, inter);
    }
}
