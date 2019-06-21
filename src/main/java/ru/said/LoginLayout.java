package ru.said;

import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import ru.said.view.UserView;
import ru.said.service.UserService;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginLayout extends VerticalLayout {
    private Button inter = new Button(" войти в систему");
    private TextField userLogin = new TextField("Логин");
    private PasswordField userPassword = new PasswordField("Пароль");
    private UserService userService = new UserService();
    private static final Logger LOGGER = Logger.getLogger(LoginLayout.class);

    LoginLayout() {

        inter.addClickListener(clickEvent -> {
            String login = userLogin.getValue();
            String password = userPassword.getValue();
            try {
                if (userService.authentication(login, password)) {
                    Notification.show("",
                            "Вход выполнен",
                            Notification.Type.HUMANIZED_MESSAGE);
                    CurrentUser.set(login);
                    new UserView();
                } else {
                    Notification.show("Ошибка",
                            "Вход не выполнен",
                            Notification.Type.HUMANIZED_MESSAGE);
                }
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(),e);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(e.getMessage(),e);
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
