package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal actual = service.create(getNew(), UserTestData.USER_ID);
        Integer id = actual.getId();
        Meal expected = service.get(id, UserTestData.USER_ID);
        assertMatch(actual, expected);
    }

    @Test
    public void createDuplicateDateTime() {
        service.create(getNew(), UserTestData.USER_ID);
        assertThrows(DataAccessException.class, () -> service.create(getNew(), UserTestData.USER_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdatedUserMeal();
        int id = updated.getId();
        service.update(updated, UserTestData.USER_ID);
        Meal actual = service.get(id, UserTestData.USER_ID);
        assertMatch(actual, getUpdatedUserMeal());
    }

    @Test
    public void updateNotYoursMeal() {
        Meal userMeal = getUpdatedUserMeal();
        assertThrows(NotFoundException.class, () -> service.update(userMeal, UserTestData.GUEST_ID));
    }

    @Test
    public void delete() {
        int userMealId = getExistUserMeal().getId();
        service.delete(userMealId, UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(userMealId, UserTestData.USER_ID));
    }

    @Test
    public void deleteNotYoursMeal() {
        int userMealId = getExistUserMeal().getId();
        assertThrows(NotFoundException.class, () -> service.delete(userMealId, UserTestData.GUEST_ID));
    }

    @Test
    public void deleteNotExistentMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_EXISTENT_ID, UserTestData.USER_ID));
    }

    @Test
    public void get() {
        Meal expected = getExistUserMeal();
        int id = expected.getId();
        Meal actual = service.get(id, UserTestData.USER_ID);
        assertMatch(actual, expected);
    }

    @Test
    public void getNotYoursMeal() {
        int id = getExistUserMeal().getId();
        assertThrows(NotFoundException.class, () -> service.get(id, UserTestData.GUEST_ID));
    }

    @Test
    public void getNotExistentMeal() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_EXISTENT_ID, UserTestData.USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(UserTestData.USER_ID);
        assertMatch(actual, userMeals);
        isSorted(actual, Comparator.comparing(Meal::getDateTime).reversed());
    }

    @Test
    public void getBetweenDates() {
        List<Meal> actual = service
                .getBetweenInclusive(START_DATE, END_DATE, UserTestData.USER_ID);
        assertMatch(actual, userMealBetweenDates);
        isSorted(actual, Comparator.comparing(Meal::getDateTime).reversed());
    }
}
