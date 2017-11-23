package it.algos.springvaadin.servlet;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.server.SpringVaadinServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 29-ago-2017
 * Time: 14:46
 */
@WebServlet(value = "/*", asyncSupported = true)
public class MySpringServlet extends SpringVaadinServlet {
    public MySpringServlet() {
        super();
    }

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.service(request, response);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
