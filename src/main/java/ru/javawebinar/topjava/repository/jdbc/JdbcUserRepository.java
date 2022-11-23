package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.*;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

//    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    public  Validator validator;

    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    ResultSetExtractor<List<User>> usersExtractor = rs -> {
        Map<Integer, User> users = new HashMap<>();
        while (rs.next()) {
            User user;
            int id = rs.getInt(1);
            if (users.containsKey(id)) {
                user = users.get(id);
                String roleString = rs.getString(9);
                if (roleString != null) {
                    user.getRoles().add(Role.valueOf(rs.getString(9)));
                }
            } else {
                user = new User();
                user.setId(id);
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setRegistered(rs.getDate(5));
                user.setEnabled(rs.getBoolean(6));
                user.setCaloriesPerDay(rs.getInt(7));
                String roleString = rs.getString(9);
                user.setRoles(roleString == null ? null : EnumSet.of(Role.valueOf(roleString)));
                users.put(id, user);
            }
        }
        return new ArrayList<>(users.values());
    };

    ResultSetExtractor<Set<Role>> rolesExtractor = rs -> {
        Set<Role> roles = new HashSet<>();
        while (rs.next()) {
            Role role = Role.valueOf(rs.getString(2));
            roles.add(role);
        }
        return roles;
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        // No idea why validation is not working
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", user.getRoles(), 2,
                    (ps, role) -> {
                ps.setInt(1, newKey.intValue());
                ps.setString(2, role.name());
            });
            user.setId(newKey.intValue());
        } else {
            int updated = namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource);
            Set<Role> dbRoles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id = ?", rolesExtractor, user.getId());
            Set<Role> actualRoles = user.getRoles();
            // Should be in batch but maybe there is an easier way
            actualRoles.forEach(role -> {
                if (!dbRoles.contains(role)) {
                    // INSERT
                    jdbcTemplate.update("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", user.getId(), role.name());
                }
            });
            dbRoles.forEach(role -> {
                if (!actualRoles.contains(role)) {
                    // DELETE
                    jdbcTemplate.update("DELETE FROM user_roles WHERE user_id = ? AND role = ?", user.getId(), role.name());
                }
            });
            if (updated == 0) return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ur ON users.id = ur.user_id WHERE id=?", usersExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ur ON users.id = ur.user_id WHERE email=?", usersExtractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ur ON users.id = ur.user_id ORDER BY name, email", usersExtractor);
    }
}
