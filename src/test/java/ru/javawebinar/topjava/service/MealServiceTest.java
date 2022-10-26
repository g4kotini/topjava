package ru.javawebinar.topjava.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Comparator;
import java.util.List;

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
    MealService service;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void createSuccess() {
        Meal createdMeal = service.create(getNew(), UserTestData.USER_ID);
        Integer createdMealId = createdMeal.getId();
        Meal expectedMeal = getNew();
        expectedMeal.setId(createdMealId);
        Assertions.assertThat(createdMeal).usingRecursiveComparison().isEqualTo(expectedMeal);
    }

    @Test
    public void createDuplicateDateTimeThrownDataAccessExceptionException() {
        Assertions.assertThatExceptionOfType(DataAccessException.class).isThrownBy(() -> {
            service.create(getNew(), UserTestData.USER_ID);
            service.create(getNew(), UserTestData.USER_ID);
        });
    }

    @Test
    public void updateUserMealSuccess() {
        Meal existedAndUpdatedUserMeal = getExistUserMeal();
        int existUserMealId = existedAndUpdatedUserMeal.getId();
        existedAndUpdatedUserMeal.setCalories(1100);
        service.update(existedAndUpdatedUserMeal, UserTestData.USER_ID);
        Meal updatedMealFromDb = DataAccessUtils
                .singleResult(jdbcTemplate
                        .query("SELECT * FROM meals WHERE id=?", MealTestData.ROW_MAPPER, existUserMealId));
        Assertions.assertThat(updatedMealFromDb).usingRecursiveComparison().isEqualTo(existedAndUpdatedUserMeal);
    }

    @Test
    public void updateUserMealByGuestThrownNotFoundException() {
        Meal existedAndUpdatedUserMeal = getExistUserMeal();
        existedAndUpdatedUserMeal.setCalories(1100);
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> service.update(existedAndUpdatedUserMeal, UserTestData.GUEST_ID));
    }

    @Test
    public void deleteSuccess() {
        int existedUserMealId = getExistUserMeal().getId();
        service.delete(existedUserMealId, UserTestData.USER_ID);
        Assertions.assertThat(jdbcTemplate
                .query("SELECT FROM meals WHERE id=?", ROW_MAPPER, existedUserMealId)).isEmpty();
    }

    @Test
    public void deleteUserMealByGuestThrownNotFoundException() {
        int existedUserMealId = getExistUserMeal().getId();
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> service.delete(existedUserMealId, UserTestData.GUEST_ID));
    }

    @Test
    public void getSuccess() {
        Meal existedUserMeal = getExistUserMeal();
        int existedUserMealId = existedUserMeal.getId();
        Meal userMealFromService = service.get(existedUserMealId, UserTestData.USER_ID);
        Assertions.assertThat(userMealFromService).usingRecursiveComparison().isEqualTo(existedUserMeal);
    }

    @Test
    public void getUserMealByGuestThrownNotFoundException() {
        int existedUserMealId = getExistUserMeal().getId();
        Assertions.assertThatExceptionOfType(NotFoundException.class).isThrownBy(
                () -> service.get(existedUserMealId, UserTestData.GUEST_ID));
    }

    @Test
    public void getAllUserMealsSortedByDateTimeSuccess() {
        List<Meal> userMealsFromService = service.getAll(UserTestData.USER_ID);
        Assertions.assertThat(userMealsFromService)
                .usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(userMeals);
        Assertions.assertThat(userMealsFromService)
                .isSortedAccordingTo(Comparator.comparing(Meal::getDateTime).reversed());
    }

    @Test
    public void getBetweenHalfOpenUserMealsFilteredAndSortedByDateSuccess() {
        List<Meal> userMealsFromService = service
                .getBetweenInclusive(START_DATE, END_DATE, UserTestData.USER_ID);
        Assertions.assertThat(userMealsFromService)
                .usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(getBetweenDates());
        Assertions.assertThat(userMealsFromService)
                .isSortedAccordingTo(Comparator.comparing(Meal::getDateTime).reversed());
    }
}
