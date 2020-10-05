package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(1);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        log.debug("save meal {}", meal);
        if (meal.isNew()) {
            int id = counter.getAndIncrement();
            meal.setId(id);
            meals.put(meal.getId(), meal);
            return meal;
        }
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(Meal meal) {
        log.debug("delete meal {}", meal);
        return meals.remove(meal.getId()) != null;
    }

    @Override
    public Meal get(int id) {
        log.debug("get meal by id {}", id);
        return meals.get(id);
    }

    @Override
    public List<Meal> getAll() {
        log.debug("get all meals");
        return new ArrayList<>(meals.values());
    }
}
