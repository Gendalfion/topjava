package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext appCtx;
    private MealRestController mealController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealController = appCtx.getBean(MealRestController.class);
        this.getServletContext().setAttribute("SpringContext", appCtx);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories"))
        );

        String id = request.getParameter("id");
        if (id.isEmpty()) {
            mealController.create(meal);
            log.info("Create {}", meal);
        } else {
            mealController.update(meal, Integer.valueOf(id));
            log.info("Update {}", meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                break;
            case "clear_filter":
                request.getSession().setAttribute("startDate", null);
                request.getSession().setAttribute("startTime", null);
                request.getSession().setAttribute("endDate", null);
                request.getSession().setAttribute("endTime", null);
            case "all":
            default:
                log.info("getAll");

                request.setAttribute("meals",
                        mealController.getFilteredWithExceed(
                                getSessionParam(request, "startDate"),
                                getSessionParam(request, "startTime"),
                                getSessionParam(request, "endDate"),
                                getSessionParam(request, "endTime"))
                );
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    private String getSessionParam(HttpServletRequest request, String param) {
        String paramVal = request.getParameter(param);
        if (paramVal == null || paramVal.isEmpty()) {
            return (String) request.getSession().getAttribute(param);
        }
        request.getSession().setAttribute(param, paramVal);
        return paramVal;
    }
}
