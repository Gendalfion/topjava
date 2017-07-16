package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;

public interface MealService {
    List<MealWithExceed> getByCalories (int caloriesPerDay);
}
