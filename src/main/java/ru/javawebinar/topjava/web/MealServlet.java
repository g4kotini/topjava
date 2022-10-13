package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository repository;

    @Override
    public void init() {
        log.info("Servlet is created");
        repository = new InMemoryMealRepository();
    }

    @Override
    protected void
    doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        action = action == null ? "" : action;
        switch (action) {
            case "delete": {
                int id = Integer.parseInt(req.getParameter("id"));
                log.debug("Delete GET request params: \n Id: {}", id);
                repository.deleteById(id);
                resp.sendRedirect(req.getContextPath() + "/meals");
                break;
            }
            case "update": {
                int id = Integer.parseInt(req.getParameter("id"));
                log.debug("Update GET request params: \n Id: {}", id);
                Meal meal = repository.getById(id);
                req.setAttribute("meal", meal);
                req.getRequestDispatcher("createUpdateMeal.jsp").forward(req, resp);
                break;
            }
            case "create": {
                log.trace("Create GET request");
                req.setAttribute("meal", new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
                req.getRequestDispatcher("createUpdateMeal.jsp").forward(req, resp);
                break;
            }
            default: {
                log.trace("GET request");
                List<MealTo> mealTos = MealsUtil.filteredByStreams(
                        repository.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
                req.setAttribute("mealTos", mealTos);
                req.getRequestDispatcher("meals.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String stringId = req.getParameter("id");
        Integer id = stringId.isEmpty() ? null : Integer.parseInt(req.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        log.debug(
                "Update or create POST request params: \n Id: {} \n Description: {} \n Date & Time: {} \n Calories: {}",
                id, description, dateTime, calories);
        Meal meal = new Meal(id, dateTime, description, calories);
        repository.createOrUpdate(meal);
        resp.sendRedirect(req.getContextPath() + "/meals");
    }
}
