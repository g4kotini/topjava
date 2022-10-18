package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.userMeals.forEach((meal -> save(meal, 1)));
        MealsUtil.adminMeals.forEach((meal -> save(meal, 2)));
    }

    @Override
    public Meal save(Meal meal, int authUserId) {
        log.info("save: authUserid={}, meal= \n {}", authUserId, meal);
        Map<Integer, Meal> meals = repository.computeIfAbsent(authUserId, newKey -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            int newMealId = counter.incrementAndGet();
            meal.setId(newMealId);
            meals.put(newMealId, meal);
            log.info("Meal with id={} has been created", newMealId);
            return meal;
        } else {
            int currentMealId = meal.getId();
            Meal updatedMeal = meals.computeIfPresent(currentMealId, (mealId, oldMealValue) -> meal);
            if (updatedMeal == null) {
                log.info("There is no meal with id={} or this food does not belong to user with id={}",
                        currentMealId, authUserId);
            } else {
                log.info("Meal with id={} has been updated", currentMealId);
            }
            return updatedMeal;
        }
    }

    @Override
    public boolean delete(int id, int authUserId) {
        log.info("delete: meal id={}, user id={}", id, authUserId);
        Map<Integer, Meal> meals = repository.get(authUserId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int authUserId) {
        log.info("get: meal id={}, user id={}", id, authUserId);
        Map<Integer, Meal> meals = repository.get(authUserId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getAll(int authUserId) {
        log.info("get all: user id={}", authUserId);
        Map<Integer, Meal> meals = repository.get(authUserId);
        return meals == null ?
                Collections.emptyList() :
                meals.values().stream()
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFiltered(Predicate<Meal> predicate, int authUserId) {
        log.info("get filtered: user id={}", authUserId);
        return getAll(authUserId).stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}

