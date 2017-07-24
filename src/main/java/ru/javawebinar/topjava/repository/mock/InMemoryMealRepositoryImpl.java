package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
//        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
        int startDate = 1;
        int endDate = 15;
        Month month = Month.MAY;
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        for (int date = startDate; date <= endDate; date++) {
            int userId = rnd.nextInt(1, 4);
            save(new Meal(
                    LocalDateTime.of(2017, month, date, rnd.nextInt(7, 12), rnd.nextInt(60))
                    , "Завтрак"
                    , rnd.nextInt(300, 900)), userId);

            save(new Meal(
                    LocalDateTime.of(2017, month, date, rnd.nextInt(12, 16), rnd.nextInt(60))
                    , "Обед"
                    , rnd.nextInt(500, 1500)), userId);

            save(new Meal(
                    LocalDateTime.of(2017, month, date, rnd.nextInt(17, 23), rnd.nextInt(60))
                    , "Ужин"
                    , rnd.nextInt(400, 1000)), userId);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setAuthorizationId(userId);
        } else if (userId != meal.getAuthorizationId()) {
            return null;
        }
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = repository.get(id);
        if (meal == null || meal.getAuthorizationId() != userId) {
            return false;
        }
        repository.remove(id);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        if (meal == null || meal.getAuthorizationId() != userId) {
            return null;
        }
        return meal;
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return getFiltered(LocalDate.MIN, LocalDate.MAX, userId);
    }

    @Override
    public Collection<Meal> getFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getAuthorizationId() == userId && DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

