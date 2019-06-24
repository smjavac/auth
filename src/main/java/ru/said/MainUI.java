package ru.said;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;

public class MainUI extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        if (CurrentUser.get().isEmpty()) {
            setContent(new LoginLayout((LoginLayout.LoginListener) () -> showMainView()));
        } else {
        //    new UserView();
        // setContent(new MainLayout());
            showMainView();
        }
    }

    protected void showMainView() {

        setContent(new MainLayout());

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
