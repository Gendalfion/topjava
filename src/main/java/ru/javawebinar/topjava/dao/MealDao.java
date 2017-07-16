package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {

    List<Meal> getAll ();

    void add(Meal meal);

    void removeById(int id);

    Meal getById(int id);
}
