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
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@WebServlet("/meals")
public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealService mealService;

    private final DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void init() throws ServletException {
        super.init();

        log.debug("meals servlet initialization with memory mapped MealDaoMemoryImpl");
        mealService = new MealServiceImpl(new MealDaoMemoryImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String ENCODING = "UTF-8";

        req.setCharacterEncoding(ENCODING);

        String action = req.getParameter("action");
        log.debug("doGet: {}?{}", req.getRequestURI(), req.getQueryString());

        if ("delete".equals(action)) {
            String message = "";
            String errorMessage = "";

            try {
                int idToDelete = Integer.parseInt(req.getParameter("mealId"));
                mealService.removeById(idToDelete);
                message = "Данные из базы успешно удалены...";
            } catch (Exception e) {
                errorMessage = "Ошибка удаления данных из базы: " + e.getMessage();
            }

            message = URLEncoder.encode(message, ENCODING);
            errorMessage = URLEncoder.encode(errorMessage, ENCODING);
            resp.sendRedirect("meals?message=" + message + "&error=" + errorMessage);
        } else if ("update_add".equals(action)) {
            String message = "";
            String errorMessage = "";

            try {
                int idToUpdate = Integer.parseInt(req.getParameter("id"));
                LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"), dtf);
                String description = req.getParameter("description");
                int calories = Integer.parseInt(req.getParameter("calories"));

                if (idToUpdate < 0) {
                    mealService.add(new Meal(dateTime, description, calories));
                } else {
                    mealService.update(new Meal(idToUpdate, dateTime, description, calories));
                }
                message = "Данные успешно занесены в базу...";
            } catch (Exception e) {
                errorMessage = "Ошибка занасения данных в базу: " + e.getMessage();
            }

            message = URLEncoder.encode(message, ENCODING);
            errorMessage = URLEncoder.encode(errorMessage, ENCODING);
            resp.sendRedirect("meals?message=" + message + "&error=" + errorMessage);
        } else {
            if ("edit".equals(action)) {
                int idToEdit = Integer.parseInt(req.getParameter("mealId"));
                req.setAttribute("mealToEdit", mealService.getById(idToEdit));
            } else if ("add".equals(action)) {
                req.setAttribute("mealToEdit", new Meal(-1, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0));
            }
            req.setAttribute("mealsWithExceed", mealService.getByCalories(2000));
            RequestDispatcher view = req.getRequestDispatcher("meals.jsp");
            view.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
