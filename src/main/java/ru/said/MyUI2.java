package ru.said;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import ru.said.view.UserView;

import javax.servlet.annotation.WebServlet;

public class MyUI2 extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        if (CurrentUser.get().isEmpty()) {
            setContent(new MainLayou2());
        } else {
            new UserView();
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI2.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
