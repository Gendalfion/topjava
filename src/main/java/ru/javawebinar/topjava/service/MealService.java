package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;

public interface MealService {
    List<MealWithExceed> getByCalories (int caloriesPerDay);

    void removeById(int id);

    Meal getById(int id);

    void add(Meal meal);

    void update(Meal meal);
}
