package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MealTestData {
    public final static int START_SEQ = 100_002;

    public final static int USER_ID = 100_000;

    public final static int TESTED_MEAL_ID = START_SEQ;

    public final static int NOT_FOUND = 10;

    public final static Meal userMeal1 = new Meal(START_SEQ, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public final static Meal userMeal2 = new Meal(START_SEQ + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public final static Meal userMeal3 = new Meal(START_SEQ + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public final static Meal userMeal4 = new Meal(START_SEQ + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public final static Meal userMeal5 = new Meal(START_SEQ + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public final static Meal userMeal6 = new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public final static Meal userMeal7 = new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public final static Meal testedMeal = userMeal1;

    public final static Meal duplicateDateTimeMeal = new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 800);

    public final static List<Meal> userMeals = Stream.of(userMeal1, userMeal2, userMeal3, userMeal4, userMeal5, userMeal6, userMeal7).
            sorted(Comparator.comparing(Meal::getDateTime).reversed()).
            collect(Collectors.toList());

    public static Meal getUpdated() {
        Meal meal = new Meal(testedMeal);
        meal.setDateTime(LocalDateTime.of(2020, 2, 1, 12, 30));
        meal.setDescription("Обновленный завтрак");
        meal.setCalories(1000);
        return meal;
    }

    public static Meal getNew() {
        return new Meal(
                LocalDateTime.of(2020, 2, 1, 20, 30),
                "Ужин",
                1200
        );
    }
}
