package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
public class MealServiceTest {

    @Autowired
    private DbPopulator dbPopulator;

    @Autowired
    private MealService mealService;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        Meal userMeal = mealService.get(START_SEQ, UserTestData.USER_ID);
        MATCHER.assertEquals(DATA[5], userMeal);
        Meal adminMeal = mealService.get(START_SEQ + 6, UserTestData.ADMIN_ID);
        MATCHER.assertEquals(DATA[7], adminMeal);
    }

    @Test(expected = NotFoundException.class)
    public void testGetOthersPeopleMeal() throws Exception {
        mealService.get(START_SEQ + 6, UserTestData.USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotExistingMeal() throws Exception {
        mealService.get(1, UserTestData.USER_ID);
    }

    @Test
    public void testDelete() throws Exception {
        mealService.delete(START_SEQ + 1, UserTestData.USER_ID);
        mealService.delete(START_SEQ + 6, UserTestData.ADMIN_ID);
        List<Meal> allMeals = mealService.getAll(UserTestData.USER_ID);
        allMeals.addAll(mealService.getAll(UserTestData.ADMIN_ID));
        List<Meal> expectedMeals = new ArrayList<>(Arrays.asList(DATA));
        expectedMeals.remove(7);
        expectedMeals.remove(4);
        MATCHER.assertCollectionEquals(expectedMeals, allMeals);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteOthersPeopleMeal() throws Exception {
        mealService.delete(START_SEQ + 1, UserTestData.ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotExistingMeal() throws Exception {
        mealService.delete(1, UserTestData.USER_ID);
    }

    @Test
    public void testGetBetweenDates() throws Exception {
        List<Meal> actualData = mealService.getBetweenDates(LocalDate.parse("2015-05-30"), LocalDate.parse("2015-05-30"), UserTestData.USER_ID);
        List<Meal> expectedData = Arrays.asList(DATA[3], DATA[4], DATA[5]);
        MATCHER.assertCollectionEquals(expectedData, actualData);

        actualData = mealService.getBetweenDates(LocalDate.parse("2015-06-01"), LocalDate.parse("2015-06-01"), UserTestData.ADMIN_ID);
        expectedData = Arrays.asList(DATA[6], DATA[7]);
        MATCHER.assertCollectionEquals(expectedData, actualData);
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        List<Meal> actualData = mealService.getBetweenDateTimes(
                LocalDateTime.parse("2015-05-30T13:00:00"),
                LocalDateTime.parse("2015-05-31T13:00:00"),
                UserTestData.USER_ID);
        List<Meal> expectedData = Arrays.asList(DATA[1], DATA[2], DATA[3], DATA[4]);
        MATCHER.assertCollectionEquals(expectedData, actualData);

        actualData = mealService.getBetweenDateTimes(
                LocalDateTime.parse("2015-06-01T13:59:59"),
                LocalDateTime.parse("2015-06-01T14:00:01"),
                UserTestData.ADMIN_ID);
        expectedData = Collections.singletonList(DATA[7]);
        MATCHER.assertCollectionEquals(expectedData, actualData);
    }

    private void compareWithDB(List<Meal> expectedMeals) {
        List<Meal> actualMeals = new ArrayList<>(mealService.getAll(UserTestData.USER_ID));
        actualMeals.addAll(mealService.getAll(UserTestData.ADMIN_ID));
        MATCHER.assertCollectionEquals(expectedMeals, actualMeals);
    }

    @Test
    public void testGetAll() throws Exception {
        compareWithDB(Arrays.asList(DATA));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal userMeal = new Meal(START_SEQ + 3, LocalDateTime.parse("2015-05-29T10:22:00"), "newUserMeal", 1200);
        mealService.update(userMeal, UserTestData.USER_ID);

        Meal adminMeal = new Meal(START_SEQ + 6, LocalDateTime.parse("2015-06-02T00:00:00"), "newAdminMeal", 500);
        mealService.update(adminMeal, UserTestData.ADMIN_ID);

        List<Meal> expectedMeals = Arrays.asList(DATA[0], DATA[1], DATA[3], DATA[4], DATA[5], userMeal, adminMeal, DATA[6]);
        compareWithDB(expectedMeals);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateOthersPeopleMeal() throws Exception {
        Meal userMeal = new Meal(START_SEQ, LocalDateTime.parse("2015-05-29T10:22:00"), "newUserMeal", 1200);
        mealService.update(userMeal, UserTestData.ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNotExistingMeal() throws Exception {
        Meal notExistingMeal = new Meal(1, LocalDateTime.parse("2015-05-29T10:22:00"), "newUserMeal", 1200);
        mealService.update(notExistingMeal, UserTestData.USER_ID);
    }

    @Test
    public void testSave() throws Exception {
        Meal userMeal = new Meal(LocalDateTime.parse("2015-05-29T10:22:00"), "newUserMeal", 1200);
        userMeal.setId(mealService.save(userMeal, UserTestData.USER_ID).getId());
        List<Meal> expectedMeals = new ArrayList<>(Arrays.asList(DATA).subList(0, 6));
        expectedMeals.add(userMeal);

        Meal adminMeal = new Meal(LocalDateTime.parse("2015-06-02T00:00:00"), "newAdminMeal", 500);
        adminMeal.setId(mealService.save(adminMeal, UserTestData.ADMIN_ID).getId());
        expectedMeals.add(adminMeal);
        expectedMeals.addAll(Arrays.asList(DATA).subList(6, 8));

        compareWithDB(expectedMeals);
    }

}