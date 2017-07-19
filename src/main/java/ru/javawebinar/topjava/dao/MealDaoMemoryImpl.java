package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoMemoryImpl implements MealDao {
    private static final Logger LOG = LoggerFactory.getLogger(MealDaoMemoryImpl.class);

    private Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private AtomicInteger curID = new AtomicInteger(0);

    public MealDaoMemoryImpl() {
        addTestDataToMap();
    }

    public void addTestDataToMap () {
        /*add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));*/
        int startDate = 1;
        int endDate = 15;
        Month month = Month.MAY;
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        for (int date = startDate; date <= endDate; date++) {
            add(new Meal(
                    LocalDateTime.of(2017, month, date, rnd.nextInt(7, 12), rnd.nextInt(60))
                    , "Завтрак"
                    , rnd.nextInt(300, 900)));

            add(new Meal(
                    LocalDateTime.of(2017, month, date, rnd.nextInt(12, 16), rnd.nextInt(60))
                    , "Обед"
                    , rnd.nextInt(500, 1500)));

            add(new Meal(
                    LocalDateTime.of(2017, month, date, rnd.nextInt(17, 23), rnd.nextInt(60))
                    , "Ужин"
                    , rnd.nextInt(400, 1000)));
        }
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public void add(Meal meal) {
        int id = curID.getAndIncrement();
        mealMap.put(id, new Meal(id, meal.getDateTime(), meal.getDescription(), meal.getCalories()));
        LOG.debug("add: {}", meal);
    }

    @Override
    public void removeById(int id) {
        Meal meal = mealMap.remove(id);
        LOG.debug("remove: id = {}, meal = {}", id, meal);
    }

    @Override
    public Meal getById(int id) {
        Meal meal = mealMap.get(id);
        LOG.debug("getById: id = {}, meal = {}", id, meal);
        return meal;
    }

    @Override
    public void update(Meal meal) {
        int curId = curID.get();
        if (meal.getId() < 0 || meal.getId() >= curId) {
            LOG.warn("update: invalid id for update (id = {}, curId = {})", meal.getId(), curId);
        } else {
            Meal oldMeal = mealMap.put(meal.getId(), meal);
            LOG.debug("update: oldMeal = {}, newMeal = {}", oldMeal, meal);
        }
    }
}
