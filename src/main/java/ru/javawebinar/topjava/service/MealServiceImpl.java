package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.List;

public class MealServiceImpl implements MealService {
    private MealDao mealDao;

    public MealServiceImpl(MealDao mealDao) {
        this.mealDao = mealDao;
    }

    @Override
    public List<MealWithExceed> getByCalories(int caloriesPerDay) {
        return MealsUtil.getFilteredWithExceeded(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
    }

    @Override
    public void removeById(int id) {
        mealDao.removeById(id);
    }

    @Override
    public Meal getById(int id) {
        return mealDao.getById(id);
    }

}
