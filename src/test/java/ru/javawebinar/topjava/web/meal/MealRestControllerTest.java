package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + '/';

    private static final BeanMatcher<MealWithExceed> EXCEED_BEAN_MATCHER = BeanMatcher.of(MealWithExceed.class);

    @Autowired
    private MealService mealService;

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(EXCEED_BEAN_MATCHER.contentListMatcher(MealsUtil.getWithExceeded(MEALS, UserTestData.USER.getCaloriesPerDay())));
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(MEAL1));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print());

        MATCHER.assertListEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), mealService.getAll(USER_ID));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = new Meal(MEAL1);
        updated.setDateTime(LocalDateTime.now());
        updated.setDescription("Новое описание");
        updated.setCalories(777);
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk())
                .andDo(print());

        MATCHER.assertListEquals(Arrays.asList(updated, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), mealService.getAll(USER_ID));
    }

    @Test
    public void testGetBetween() throws Exception {
        final LocalDateTime startTime = LocalDateTime.of(LocalDate.of(2015, Month.MAY, 30), LocalTime.of(10, 0));
        final LocalDateTime endTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(13, 0));
        final List<MealWithExceed> meals = MealsUtil.getWithExceeded(MEALS, UserTestData.USER.getCaloriesPerDay());
        mockMvc.perform(get(REST_URL + "between/spring?startDateTime=" + startTime.toString()
                + "&endDateTime=" + endTime.toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(EXCEED_BEAN_MATCHER.contentListMatcher(meals.get(4), meals.get(5)));
    }

    @Test
    public void testCreate() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.now(), "Новая еда", 777);
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated())
                .andDo(print());

        Meal returned = MATCHER.fromJsonAction(action);
        newMeal.setId(returned.getId());

        MATCHER.assertEquals(newMeal, returned);
        MATCHER.assertListEquals(Arrays.asList(newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), mealService.getAll(USER_ID));
    }
}
