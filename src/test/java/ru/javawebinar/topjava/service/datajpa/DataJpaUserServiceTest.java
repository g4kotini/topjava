package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.Arrays;
import java.util.List;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @Test
    public void getWithMeals() {
        List<Meal> adminMeals = Arrays.asList(MealTestData.adminMeal2, MealTestData.adminMeal1);
        User admin = service.getWithMeals(UserTestData.ADMIN_ID);
        UserTestData.USER_MATCHER.assertMatch(admin, UserTestData.admin);
        MealTestData.MEAL_MATCHER.assertMatch(admin.getMeals(), adminMeals);
    }
}
