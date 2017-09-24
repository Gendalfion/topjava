package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.DuplicateMealDateTimeException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {
    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public List<Meal> getBetweenDateTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Assert.notNull(startDateTime, "startDateTime must not be null");
        Assert.notNull(endDateTime, "endDateTime  must not be null");
        return repository.getBetween(startDateTime, endDateTime, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    @Transactional
    public Meal update(Meal meal, int userId) {
        assureMealDateTimeUnique(meal, userId);
        return checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    @Override
    @Transactional
    public Meal create(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        assureMealDateTimeUnique(meal, userId);
        return repository.save(meal, userId);
    }

    private void assureMealDateTimeUnique(Meal meal, int userId) {
        if (meal.getDateTime() == null) {
            return;
        }

        LocalDateTime dateTime = meal.getDateTime().truncatedTo(ChronoUnit.MINUTES);
        List<Meal> foundMeal = getBetweenDateTimes(dateTime, dateTime, userId);
        Assert.isTrue(foundMeal.size() <= 1, "must find not more than 1 meal with specific date-time");
        if (!foundMeal.isEmpty()) {
            if (meal.isNew() || !foundMeal.get(0).getId().equals(meal.getId())) {
                throw new DuplicateMealDateTimeException(DUPLICATE_MEAL_DATE_TIME);
            }
        }
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return checkNotFoundWithId(repository.getWithUser(id, userId), id);
    }
}