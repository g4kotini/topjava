package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal) {
        return service.save(meal, SecurityUtil.authUserId());
    }

    public boolean delete(int mealId) {
        return service.delete(mealId, SecurityUtil.authUserId());
    }

    public Meal get(int mealId) {
        return service.get(mealId, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll() {
        return service.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalDate endDate,
                                    LocalTime startTime, LocalTime endTime) {
        return service.getFiltered(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay(),
                startDate, endDate, startTime, endTime);
    }
}
