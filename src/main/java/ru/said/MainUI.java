package ru.said;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import ru.said.view.UserView;

import javax.servlet.annotation.WebServlet;

public class MainUI extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        if (CurrentUser.get().isEmpty()) {
            setContent(new LoginLayout());
        } else {
            new UserView();
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
