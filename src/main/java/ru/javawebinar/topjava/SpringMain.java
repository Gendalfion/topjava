package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
//            adminUserController.create(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));
            /*
            UserRepository userRepository = appCtx.getBean(InMemoryUserRepositoryImpl.class);
            userRepository.getAll().forEach(System.out::println);

            MealRepository mealRepository = appCtx.getBean(MealRepository.class);
            mealRepository.getAll().forEach(System.out::println);

            MealService mealService = appCtx.getBean(MealService.class);
            mealService.getWithExceeded(1, 2000).forEach(System.out::println);
            */

            MealRestController mealController = appCtx.getBean(MealRestController.class);
            mealController.getWithExceeded().forEach(System.out::println);
        }
    }
}
