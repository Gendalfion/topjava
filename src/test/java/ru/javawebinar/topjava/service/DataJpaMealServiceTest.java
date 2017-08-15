package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_WO_MEAL_ID;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Override
    public void testGetWithUser() throws Exception {
        Meal mealWithUser = service.getWithUser(MEAL1_ID, USER_ID);
        MATCHER.assertEquals(mealWithUser, MEAL1);
    }

    @Override
    public void testGetWithUserNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.getWithUser(MEAL1_ID, USER_WO_MEAL_ID);
    }
}