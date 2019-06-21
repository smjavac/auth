package ru.said;

import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import ru.said.view.UserView;
import ru.said.service.UserService;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class MainLayou2 extends VerticalLayout {
    private Button inter = new Button(" войти в систему");
    private TextField userLogin = new TextField("Логин");
    private PasswordField userPassword = new PasswordField("Пароль");
    private UserService userService = new UserService();
    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(MainLayou2.class);

    MainLayou2() {

        inter.addClickListener(clickEvent -> {
            String login = userLogin.getValue();
            String password = userPassword.getValue();
            try {
                if (userService.authentication(login, password)) {
                    Notification.show("",
                            "Вход выполнен",
                            Notification.Type.HUMANIZED_MESSAGE);
                    UserView.set(login);
                    new UserView();
                } else {
                    Notification.show("Ошибка",
                            "Вход не выполнен",
                            Notification.Type.HUMANIZED_MESSAGE);
                }
            } catch (SQLException e) {
                LOGGER.error(e);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(e);
            }
            userLogin.clear();
            userPassword.clear();

        });

        addComponents(userLogin, userPassword, inter);
        setComponentAlignment(userLogin, Alignment.MIDDLE_CENTER);
        setComponentAlignment(userPassword, Alignment.MIDDLE_CENTER);
        setComponentAlignment(inter, Alignment.MIDDLE_CENTER);
    }
}
