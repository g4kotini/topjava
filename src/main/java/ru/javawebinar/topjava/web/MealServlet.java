package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    MealRepository repository;

    @Override
    public void init() throws ServletException {
        log.info("initialize servlet");
        repository = new InMemoryMealRepository();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("mealId");
        LocalDate date = LocalDate.parse(req.getParameter("date"));
        LocalTime time = LocalTime.parse(req.getParameter("time"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        Meal meal = new Meal(id.isEmpty() ? null : Integer.parseInt(id), date, time, description, calories);
        log.debug(meal.isNew() ? "create {}" : "update {}", meal);
        repository.save(meal);
        resp.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action != null ? action : "") {
            case "update":
            case "create":
                log.debug("update/create action");
                Meal meal = action.equals("update") ? repository.get(getId(req)) :
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
                req.setAttribute("meal", meal);
                req.getRequestDispatcher("/mealForm.jsp").forward(req, resp);
                break;
            case "delete":
                log.debug("delete action");
                repository.delete(repository.get(getId(req)));
                resp.sendRedirect("meals");
                break;
            default:
                log.debug("redirect to meals");
                List<MealTo> mealTos = getTos(repository.getAll(), DEFAULT_CALORIES_PER_DAY);
                req.setAttribute("meals", mealTos);
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        }
    }

    private int getId(HttpServletRequest req) {
        String paramId = Objects.requireNonNull(req.getParameter("mealId"), "request parameter can't be null");
        return Integer.parseInt(paramId);
    }
}
