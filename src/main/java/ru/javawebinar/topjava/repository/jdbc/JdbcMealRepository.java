package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class JdbcMealRepository implements MealRepository {
    private static final Logger log = getLogger(JdbcMealRepository.class);

    private final SimpleJdbcInsert simpleInsert;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final static RowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        simpleInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");

        namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("user with id={} saving meal: \n {}", userId, meal);
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("dateTime", meal.getDateTime())
                .addValue("user_id", userId);
        if (meal.isNew()) {
            int newId = simpleInsert.executeAndReturnKey(parameters).intValue();
            meal.setId(newId);
            return meal;
        } else if (namedJdbcTemplate.update("UPDATE meals SET description=:description, " +
                "calories=:calories, date_time=:dateTime WHERE id=:id AND user_id=:user_id", parameters) == 0) {
            return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("user with id={} deleting meal with id={}", userId, id);
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) == 1;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("user with id={} getting meal with id={}", userId, id);
        return DataAccessUtils
                .singleResult(jdbcTemplate
                        .query("SELECT * FROM meals WHERE id=? AND user_id=?", ROW_MAPPER, id, userId));
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("user with id={} getting all meals", userId);
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=? ORDER BY date_time DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        log.info("user with id={} getting all meals between {} and {}", userId, startDateTime, endDateTime);
        return jdbcTemplate.query("SELECT * FROM meals WHERE date_time>=? AND date_time<? " +
                "AND user_id=? ORDER BY date_time DESC", ROW_MAPPER, startDateTime, endDateTime, userId);
    }
}
