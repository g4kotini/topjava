package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class MealService {
    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal save(Meal meal, int authUserId) {
        if (meal.isNew()) {
            ValidationUtil.checkNew(meal);
        }
        Meal savedMeal = repository.save(meal, authUserId);
        ValidationUtil.checkNotFoundWithId(savedMeal, savedMeal.getId());
        return repository.save(meal, authUserId);
    }

    public boolean delete(int mealId, int authUserId) {
        return repository.delete(mealId, authUserId);
    }

    public Meal get(int mealId, int authUserId) {
        Meal meal = repository.get(mealId, authUserId);
        ValidationUtil.checkNotFoundWithId(meal, meal.getId());
        return meal;
    }

    public List<MealTo> getAll(int authUserId, int caloriesPerDay) {
        List<Meal> userMeals = repository.getAll(authUserId);
        return MealsUtil.getTos(userMeals, caloriesPerDay);
    }
    
    public List<MealTo> getFiltered(int authUserId, int caloriesPerDay, 
                                    LocalDate startDate, LocalDate endDate, 
                                    LocalTime startTime, LocalTime endTime) {
        List<Meal> userMeals = repository.getFiltered(
                meal -> meal.getDate().isAfter(startDate) && meal.getDate().isBefore(endDate),
                authUserId);
        return MealsUtil.getFilteredTos(userMeals, caloriesPerDay, startTime, endTime);
    }
}
