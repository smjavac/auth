package ru.said;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import ru.said.view.UserView;

public class MainLayou2 extends VerticalLayout  {
    Button inter = new Button(" войти в систему");
    TextField user_login = new TextField("Имя пользователя");
    TextField user_password = new TextField("Пароль");

    MainLayou2() {
        String login = user_login.getValue();
        String password = user_password.getValue();
        String logFromBase;
        String passFromBase;
        inter.addClickListener(clickEvent -> {
            if (true) {
                new UserView();
            }
        });
        addComponents(user_login, user_password, inter);

    }
}
