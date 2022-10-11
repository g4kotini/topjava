package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    List<Meal> getAll();

    Meal create(Meal meal);

    void deleteById(int id);

    Meal getById(int id);

    Meal updateOrCreate(Meal meal);
}
