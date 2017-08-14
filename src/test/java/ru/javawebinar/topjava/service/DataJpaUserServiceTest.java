package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Override
    public void testGetWithMeals() throws Exception {
        Pair<User, List<Meal>> userWithMeals = service.getWithMeals(USER_ID);
        MATCHER.assertEquals(USER, userWithMeals.getFirst());
        MealTestData.MATCHER.assertCollectionEquals(MealTestData.MEALS, userWithMeals.getSecond());
    }

    @Override
    public void testGetWithoutMeals() throws Exception {
        Pair<User, List<Meal>> userWithoutMeals = service.getWithMeals(USER_WO_MEAL_ID);
        MATCHER.assertEquals(USER_WO_MEAL, userWithoutMeals.getFirst());
        Assert.assertTrue(userWithoutMeals.getSecond().isEmpty());
    }

    @Override
    public void testGetWithMealsNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.getWithMeals(1);
    }
}