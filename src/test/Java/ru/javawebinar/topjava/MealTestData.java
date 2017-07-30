package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.BaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

public class MealTestData {
    public static final int START_SEQ = BaseEntity.START_SEQ + UserTestData.USER_COUNT;

    public static final Meal[] DATA = {
            new Meal(START_SEQ + 5, LocalDateTime.parse("2015-05-31T20:00:00"), "Ужин", 510),
            new Meal(START_SEQ + 4, LocalDateTime.parse("2015-05-31T13:00:00"), "Обед", 500),
            new Meal(START_SEQ + 3, LocalDateTime.parse("2015-05-31T10:00:00"), "Завтрак", 1000),
            new Meal(START_SEQ + 2, LocalDateTime.parse("2015-05-30T20:00:00"), "Ужин", 500),
            new Meal(START_SEQ + 1, LocalDateTime.parse("2015-05-30T13:00:00"), "Обед", 1000),
            new Meal(START_SEQ, LocalDateTime.parse("2015-05-30T10:00:00"), "Завтрак", 500),

            new Meal(START_SEQ + 7, LocalDateTime.parse("2015-06-01T21:00:00"), "Админ ужин", 1500),
            new Meal(START_SEQ + 6, LocalDateTime.parse("2015-06-01T14:00:00"), "Админ ланч", 510)
    };

    public static final BeanMatcher<Meal> MATCHER = new BeanMatcher<>();
}
