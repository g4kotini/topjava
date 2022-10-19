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
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        ValidationUtil.checkNew(meal);
        return repository.save(meal, userId);
    }

    public void update(Meal meal, int id, int userId) {
        ValidationUtil.assureIdConsistent(meal, id);
        ValidationUtil.checkNotFoundWithId(repository.save(meal, userId), id);
    }

    public boolean delete(int mealId, int userId) {
        return repository.delete(mealId, userId);
    }

    public Meal get(int mealId, int userId) {
        Meal meal = repository.get(mealId, userId);
        ValidationUtil.checkNotFoundWithId(meal, meal.getId());
        return meal;
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        List<Meal> userMeals = repository.getAll(userId);
        return MealsUtil.getTos(userMeals, caloriesPerDay);
    }
    
    public List<MealTo> getFilteredByDateTime(int userId, int caloriesPerDay,
                                              LocalDate startDate, LocalDate endDate,
                                              LocalTime startTime, LocalTime endTime) {
        startDate = startDate == null ? LocalDate.MIN : startDate;
        endDate = endDate == null ? LocalDate.MAX : endDate;
        startTime = startTime == null ? LocalTime.MIN : startTime;
        endTime = endTime == null ? LocalTime.MAX : endTime;
        List<Meal> userMeals = repository.getFilteredByDate(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(userMeals, caloriesPerDay, startTime, endTime);
    }
}
