package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDaoMemoryImpl;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/meals")
public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealService mealService;

    private final int userCaloriesMock = 2000;

    @Override
    public void init() throws ServletException {
        super.init();

        log.debug ("meals servlet initialization with memory mapped MealDaoMemoryImpl");
        mealService = new MealServiceImpl(new MealDaoMemoryImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward = "meals.jsp";

        req.setAttribute("mealsWithExceed", mealService.getByCalories(userCaloriesMock));

        RequestDispatcher view = req.getRequestDispatcher(forward);
        view.forward(req, resp);
    }
}
