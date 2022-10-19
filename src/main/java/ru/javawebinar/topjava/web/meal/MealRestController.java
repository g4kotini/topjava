package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} create {}", userId, meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} update {}", userId, meal);
        service.update(meal, id, userId);
    }

    public boolean delete(int mealId) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} delete meal with id={}", userId, mealId);
        return service.delete(mealId, userId);
    }

    public Meal get(int mealId) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} get meal with id={}", userId, mealId);
        return service.get(mealId, userId);
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("user {} getAll", userId);
        return service.getAll(userId, SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getFilteredByDateTime(LocalDate startDate, LocalDate endDate,
                                              LocalTime startTime, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("user {} getFiltered by startDate={} endDate={} startTime={} endTime={}",
                userId, startDate, endDate, startTime, endTime);
        return service.getFilteredByDateTime(userId, SecurityUtil.authUserCaloriesPerDay(),
                startDate, endDate, startTime, endTime);
    }
}
