package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal get (int id) {
        log.info("get id={}, userId={}", id, AuthorizedUser.id());
        return service.get(id, AuthorizedUser.id());
    }

    public void delete(int id) {
        log.info("delete id={}, userId={}", id, AuthorizedUser.id());
        service.delete(id, AuthorizedUser.id());
    }

    public Meal create(Meal meal) {
        log.info("create {}, userId={}", meal, AuthorizedUser.id());
        checkNew(meal);
        return service.save(meal, AuthorizedUser.id());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}, userId={}", meal, id, AuthorizedUser.id());
        checkIdConsistent(meal, id);
        service.save(meal, AuthorizedUser.id());
    }

    public List<MealWithExceed> getWithExceeded() {
        log.info("getWithExceeded, userId={}, caloriesPerDay={}", AuthorizedUser.id(), AuthorizedUser.getCaloriesPerDay());
        return service.getWithExceeded(AuthorizedUser.id(), AuthorizedUser.getCaloriesPerDay());
    }
}