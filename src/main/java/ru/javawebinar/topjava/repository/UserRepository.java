package ru.javawebinar.topjava.repository;

import org.springframework.data.util.Pair;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    // false if not found
    boolean delete(int id);

    // null if not found
    User get(int id);

    // null if not found
    User getByEmail(String email);

    List<User> getAll();

    default Pair<User, List<Meal>> getWithMeals(int id) {
        throw new UnsupportedOperationException("This service does not support getWithMeal operation");
    }
}