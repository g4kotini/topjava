package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final Meal userMeal1 = new Meal(START_SEQ + 3, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal userMeal2 = new Meal(START_SEQ + 4, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal userMeal3 = new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal userMeal4 = new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal userMeal5 = new Meal(START_SEQ + 7, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal userMeal6 = new Meal(START_SEQ + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal userMeal7 = new Meal(START_SEQ + 9, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    public static final Meal userMeal8 = new Meal(START_SEQ + 10, LocalDateTime.of(2020, Month.FEBRUARY, 1, 8, 0), "Завтрак", 1000);

    public static final List<Meal> userMeals = Arrays.asList(userMeal1, userMeal2, userMeal3, userMeal4, userMeal5, userMeal6, userMeal7, userMeal8);

    public static LocalDate START_DATE = LocalDate.of(2020, Month.JANUARY, 30);

    public static LocalDate END_DATE = LocalDate.of(2020, Month.JANUARY, 31);

    public static List<Meal> userMealBetweenDates = Arrays.asList(userMeal1, userMeal2, userMeal3, userMeal4, userMeal5, userMeal6, userMeal7);

    public static final int NOT_EXISTENT_ID = START_SEQ + 11;

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2022, Month.OCTOBER, 25, 21, 45), "Новая еда", 1000);
    }

    public static Meal getExistUserMeal() {
        return new Meal(userMeal1);
    }

    public static Meal getUpdatedUserMeal() {
        Meal updated = new Meal(userMeal1);
        updated.setCalories(1000);
        updated.setDescription("Завтрак");
        updated.setDateTime(LocalDateTime.of(2022, Month.FEBRUARY, 01, 7, 10));
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    public static void isSorted(List<Meal> actual, Comparator<Meal> comparator) {
        assertThat(actual).isSortedAccordingTo(comparator);
    }
}
