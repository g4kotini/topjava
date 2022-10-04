package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsToFilteredByCycle =
                filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToFilteredByCycle.forEach(System.out::println);
        System.out.println("---");
        List<UserMealWithExcess> mealsToFilteredByStream =
                filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToFilteredByStream.forEach(System.out::println);
        System.out.println("---");
        List<UserMealWithExcess> mealsToFilteredByCycleInOnePass =
                filteredByCyclesInOnePass(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToFilteredByCycleInOnePass.forEach(System.out::println);
        System.out.println("---");
        List<UserMealWithExcess> mealsToFilteredByStreamInOnePass =
                filteredByStreamsInOnePass(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToFilteredByStreamInOnePass.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        /*
        1. Accumulating info about calories by days
        2. Filter meals based on start/end time
        3. Set excess true/false based on caloriesByDate and caloriesPerDay info
        4. Transform UserMeal obj to UserMealWithExcess obj
         */
        Map<LocalDate, Integer> caloriesByDate = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                boolean excess = caloriesByDate.get(meal.getDate()) > caloriesPerDay;
                result.add(new UserMealWithExcess(
                        meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), excess));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        // step by step based on cycles solution
        Map<LocalDate, Integer> caloriesByDate = meals.stream()
                .collect(Collectors.toMap(UserMeal::getDate, UserMeal::getCalories, Integer::sum));
        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(),
                        caloriesByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCyclesInOnePass(List<UserMeal> meals, LocalTime startTime,
                                                                     LocalTime endTime, int caloriesPerDay) {
        /*
        The main idea is to delay the decision of assign a value to
        UserMealWithExcess.excess until caloriesByDate is full of values.

        This can be done by reflection, or you can use mutable wrapper for UserMealWithExcess.excess,
        Then in the process of recalculating map values, it will be possible to
        change the value of the excess field.

        Or you can delay the process of creating objects by placing it in the chain of consumers.
        This solution is good because it does not require any changes to the UserMealWithExcess class.
         */
        Map<LocalDate, Integer> caloriesByDate = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();
        Consumer<Void> consumer = v -> {
        };
        for (UserMeal meal : meals) {
            caloriesByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
            if (isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                consumer = consumer.andThen(v -> result.add(new UserMealWithExcess(
                        meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(),
                        caloriesByDate.get(meal.getDate()) > caloriesPerDay)));
            }
        }
        consumer.accept(null);
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreamsInOnePass(List<UserMeal> meals, LocalTime startTime,
                                                                      LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(getUserMealToUserMealWithExcessCollector(startTime, endTime, caloriesPerDay));
    }

    private static
    Collector<UserMeal, ?, List<UserMealWithExcess>> getUserMealToUserMealWithExcessCollector(
            LocalTime startTime,
            LocalTime endTime, int caloriesPerDay) {
        // UserMeal -> Map<LocalDate, Map.Entry<Integer, List<UserMeal>>> -> List<UserMealWithExcess>>
        return Collector.<UserMeal, Map<LocalDate,
                Map.Entry<Integer, List<UserMeal>>>, List<UserMealWithExcess>>
                of(
                // SUPPLIER: create a container Map<LocalDate, Map.Entry<Integer, List<UserMeal>>>
                HashMap::new,
                // ACCUMULATOR: fills container
                (container, meal) -> container.merge(
                        meal.getDate(),
                        new AbstractMap.SimpleEntry<>(meal.getCalories(), Collections.singletonList(meal)),
                        (oldV, newV) -> {
                            List<UserMeal> meals = new ArrayList<>();
                            meals.addAll(oldV.getValue());
                            meals.addAll(newV.getValue());
                            return new AbstractMap.SimpleEntry<>(oldV.getKey() + newV.getKey(), meals);
                        }
                ),
                // COMBINER: DUMMY
                (c1, c2) -> {
                    throw new UnsupportedOperationException("This collector doesn't support multithreading.");
                },
                // FINISHER: filter -> set excess -> transform
                (container) -> {
                    List<UserMealWithExcess> result = new ArrayList<>();
                    container.values()
                            .forEach(entry -> entry.getValue().stream()
                                    .filter(meal -> (isBetweenHalfOpen(meal.getTime(), startTime, endTime)))
                                    .forEach(meal -> result.add(new UserMealWithExcess(
                                            meal.getDateTime(), meal.getDescription(),
                                            meal.getCalories(), entry.getKey() > caloriesPerDay))));
                    return result;
                },
                // non concurrent collector
                Collector.Characteristics.UNORDERED
        );
    }
}
