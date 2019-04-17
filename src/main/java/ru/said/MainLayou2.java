package ru.said;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class MainLayou2 extends VerticalLayout  {
    Button inter = new Button(" войти в систему");
    TextField login = new TextField("Имя пользователя");
    TextField password = new TextField("Пароль");

    MainLayou2() {
        addComponents(login, password, inter);
    }
}
