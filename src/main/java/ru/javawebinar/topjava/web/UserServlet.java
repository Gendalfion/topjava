package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int userId = Integer.valueOf(Objects.requireNonNull(request.getParameter("userId")));
        AuthorizedUser.setId(userId);

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        ApplicationContext appCtx = (ApplicationContext) getServletContext().getAttribute("SpringContext");
        request.setAttribute("loggedUser", appCtx.getBean(ProfileRestController.class).get());
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
