package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepository.class);

    private static final InMemoryMealRepository instance = new InMemoryMealRepository();

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(1);

    {
        create(new Meal(LocalDateTime.of(2022, Month.OCTOBER, 10, 7, 0), "Завтрак", 500));
        create(new Meal(LocalDateTime.of(2022, Month.OCTOBER, 10, 13, 0), "Обед", 500));
        create(new Meal(LocalDateTime.of(2022, Month.OCTOBER, 10, 19, 0), "Ужин", 1000));
        create(new Meal(LocalDateTime.of(2022, Month.OCTOBER, 9, 9, 0), "Завтрак", 600));
        create(new Meal(LocalDateTime.of(2022, Month.OCTOBER, 9, 14, 0), "Обед", 500));
        create(new Meal(LocalDateTime.of(2022, Month.OCTOBER, 9, 18, 0), "Ужин", 1000));
    }

    private InMemoryMealRepository() {

    }

    public static InMemoryMealRepository getInstance() {
        return instance;
    }

    @Override
    public Meal create(Meal meal) {
        int id = counter.getAndIncrement();
        meal.setId(id);
        meals.put(id, meal);
        log.info("Create meal: \n {} \n and set id = {}", meal, id);
        return meal;
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
    public void deleteById(int id) {
        log.info("Delete meal by id={}", id);
        meals.remove(id);
    }

    @Override
    public Meal updateOrCreate(Meal meal) {
        Integer id = meal.getId();
        return id == null ? create(meal) : meals.put(id, meal);
    }
}
