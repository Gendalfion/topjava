package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    // null if create/update failed
    Meal save(Meal meal, int userId);

    // false if not found, or user access failed
    boolean delete(int id, int userId);

    // null if not found, or user access failed
    Meal get(int id, int userId);

    Collection<Meal> getAll();
    Collection<Meal> getAll(int userId);
}
