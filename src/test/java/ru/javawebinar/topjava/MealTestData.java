package ru.javawebinar.topjava;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public final static RowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    public static final List<Meal> userMeals = Arrays.asList(
            new Meal(START_SEQ + 3, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(START_SEQ + 4, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(START_SEQ + 7, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(START_SEQ + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(START_SEQ + 9, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
            new Meal(START_SEQ + 10, LocalDateTime.of(2020, Month.FEBRUARY, 1, 8, 0), "Завтрак", 1000)
    );

    public static LocalDate START_DATE = LocalDate.of(2020, Month.JANUARY, 30);

    public static LocalDate END_DATE = LocalDate.of(2020, Month.JANUARY, 31);

    public static List<Meal> getBetweenDates() {
        return userMeals.subList(0, userMeals.size() - 1);
    }

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2022, Month.OCTOBER, 25, 21, 45), "Новая еда", 1000);
    }

    public static Meal getExistUserMeal() {
        return new Meal(START_SEQ + 3, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    }
}
