package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDaoMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/meals")
public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealService mealService;

    private final int userCaloriesMock = 2000;

    private final DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void init() throws ServletException {
        super.init();

        log.debug ("meals servlet initialization with memory mapped MealDaoMemoryImpl");
        mealService = new MealServiceImpl(new MealDaoMemoryImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        log.debug("doGet: {}?{}", req.getRequestURI(), req.getQueryString());

        if ("delete".equals(action)) {
            int idToDelete = Integer.parseInt(req.getParameter("mealId"));
            mealService.removeById(idToDelete);
            resp.sendRedirect("meals");
        } else if ("update_add".equals(action)) {
            int idToUpdate = Integer.parseInt(req.getParameter("id"));
            LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"), dtf);
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));

            mealService.update(new Meal(idToUpdate, dateTime, description, calories));
            resp.sendRedirect("meals");
        } else {
            if ("edit".equals(action)) {
                int idToEdit = Integer.parseInt(req.getParameter("mealId"));
                req.setAttribute("mealToEdit", mealService.getById(idToEdit));
            }
            req.setAttribute("mealsWithExceed", mealService.getByCalories(userCaloriesMock));
            RequestDispatcher view = req.getRequestDispatcher("meals.jsp");
            view.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
