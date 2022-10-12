package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    List<Meal> getAll();

    boolean deleteById(int id);

    Meal getById(int id);

    Meal createOrUpdate(Meal meal);
}
