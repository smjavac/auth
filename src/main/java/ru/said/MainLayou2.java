package ru.said;

import com.vaadin.ui.*;
import ru.said.view.UserView;
import ru.said.service.UserService;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class MainLayou2 extends VerticalLayout  {
    Button inter = new Button(" войти в систему");
    TextField user_login = new TextField("Логин");
    PasswordField user_password = new PasswordField("Пароль");
    UserService userService = new UserService();
    MainLayou2() {

        inter.addClickListener(clickEvent -> {
            String login = user_login.getValue();
            String password = user_password.getValue();
            try {
                if (userService.authentication(login, password)) {
                    Notification.show("",
                            "Вход выполнен",
                            Notification.Type.HUMANIZED_MESSAGE);
                    new UserView().set(login);
                    new UserView("go");
                }
                else Notification.show("Ошибка",
                        "Вход не выполнен",
                        Notification.Type.HUMANIZED_MESSAGE);
            } catch (SQLException e) {e.printStackTrace();} catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            user_login.clear();
            user_password.clear();

        });

        addComponents(user_login, user_password, inter);
        setComponentAlignment(user_login, Alignment.MIDDLE_CENTER);
        setComponentAlignment(user_password, Alignment.MIDDLE_CENTER);
        setComponentAlignment(inter, Alignment.MIDDLE_CENTER);
    }
}
