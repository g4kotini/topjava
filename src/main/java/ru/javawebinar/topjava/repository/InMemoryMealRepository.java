package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(1);

    {
        Arrays.asList(
                new Meal(LocalDateTime.of(2022, Month.OCTOBER, 10, 7, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2022, Month.OCTOBER, 10, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2022, Month.OCTOBER, 10, 19, 0), "Ужин", 1000),
                new Meal(LocalDateTime.of(2022, Month.OCTOBER, 9, 9, 0), "Завтрак", 600),
                new Meal(LocalDateTime.of(2022, Month.OCTOBER, 9, 14, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2022, Month.OCTOBER, 9, 18, 0), "Ужин", 1000)
        ).forEach(this::createOrUpdate);
    }

    @Override
    public List<Meal> getAll() {
        log.info("Get all meals");
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getById(int id) {
        log.info("Get meal by id={}", id);
        return meals.get(id);
    }

    @Override
    public boolean deleteById(int id) {
        log.info("Delete meal by id={}", id);
        return meals.remove(id) != null;
    }

    @Override
    public Meal createOrUpdate(Meal meal) {
        Integer currentId = meal.getId();
        if (currentId == null) {
            int newId = counter.getAndIncrement();
            log.debug("Create new meal with id={}", newId);
            meal.setId(newId);
            meals.put(newId, meal);
            return meal;
        } else {
            if (meals.get(currentId) == null) {
                log.debug("There is no meals with id={}", currentId);
                return null;
            }
            log.debug("Meal with id={} has been updated", currentId);
            Meal newMeal = new Meal(meal);
            meals.put(currentId, newMeal);
            return newMeal;
        }
    }
}
