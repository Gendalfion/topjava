package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithAuthorization;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal save(Meal meal, int userId) throws NotFoundException {
        return checkNotFoundWithAuthorization(repository.save(meal, userId), userId);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        checkNotFoundWithAuthorization(repository.delete(id, userId), id, userId);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithAuthorization(repository.get(id, userId), userId);
    }

    @Override
    public List<MealWithExceed> getWithExceeded(int userId, int caloriesPerDay) {
        return MealsUtil.getWithExceeded(repository.getAll(userId), caloriesPerDay);
    }

    @Override
    public List<MealWithExceed> getFilteredWithExceeded(int userId, LocalDate startDate, LocalTime startTime
            , LocalDate endDate, LocalTime endTime, int caloriesPerDay) {
        return MealsUtil.getFilteredWithExceeded(
                repository.getFiltered(startDate, endDate, userId),
                startTime,
                endTime,
                caloriesPerDay
        );
    }
}