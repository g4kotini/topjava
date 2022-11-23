package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private static final Logger log = getLogger(JspMealController.class);

    private final MealRestController mealController;

    @Autowired
    public JspMealController(MealRestController mealController) {
        this.mealController = mealController;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(Model model) {
        log.info("get all");
        model.addAttribute("meals", mealController.getAll());
        return "meals";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        log.info("delete");
        int id = Integer.parseInt(request.getParameter("id"));
        mealController.delete(id);
        return "redirect:/meals";
    }

    @RequestMapping(value = {"/update", "/create"}, method = RequestMethod.GET)
    public String createOrUpdate(HttpServletRequest request, Model model) {
        String idAsString = request.getParameter("id");
        if (idAsString != null) {
            log.info("update");
            int id = Integer.parseInt(idAsString);
            Meal meal = mealController.get(id);
            model.addAttribute("meal", meal);
        } else {
            log.info("create");
            model.addAttribute("meal",
                    new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        }
        return "mealForm";
    }

    @RequestMapping(value = "/filter")
    public String filter(HttpServletRequest request, Model model) {
        log.info("filter");
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", mealController.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postCreateOrUpdate(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {
            log.info("post data update");
            mealController.update(meal, getId(request));
        } else {
            log.info("post data create");
            mealController.create(meal);
        }
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
