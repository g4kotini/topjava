package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public void create(Meal meal) {
        int userId = authUserId();
        log.info("create {} for user {}", meal, userId);
        checkNew(meal);
        service.save(meal, userId);
    }

    public void update(Meal meal, int id) {
        int userId = authUserId();
        log.info("update {} with id = {} for user {}", meal, id, userId);
        assureIdConsistent(meal, id);
        service.save(meal, userId);
    }

    public void delete(int id) {
        int userId = authUserId();
        log.info("delete {} for user {}", id, userId);
        service.delete(id, userId);
    }

    public Meal get(int id) {
        int userId = authUserId();
        log.info("get {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public List<MealTo> getAll() {
        int userId = authUserId();
        log.info("getAll for user {}", userId);
        return getTos(service.getAll(userId), authUserCaloriesPerDay());
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, @Nullable LocalTime startTime, @Nullable LocalTime endTime) {
        int userId = authUserId();
        log.info("getBetween ({} - {}) and ({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Meal> meals = service.getBetween(atCurrentDateOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
        return getFilteredTos(meals, authUserCaloriesPerDay(), atCurrentTimeOrMin(startTime), atCurrentTimeOrMax(endTime));
    }
}
