package ru.javawebinar.topjava.repository;

import org.springframework.jdbc.core.ResultSetExtractor;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

public class JdbcUtil {
    public static ResultSetExtractor<List<User>> usersExtractor = rs -> {
        Map<Integer, User> users = new LinkedHashMap<>();
        while (rs.next()) {
            User user;
            int id = rs.getInt(1);
            String roleString = rs.getString("role");
            if (users.containsKey(id)) {
                user = users.get(id);
                if (roleString != null) {
                    user.getRoles().add(Role.valueOf(roleString));
                }
            } else {
                user = new User();
                user.setId(id);
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRegistered(rs.getDate("registered"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                user.setRoles(roleString == null ? null : EnumSet.of(Role.valueOf(roleString)));
                users.put(id, user);
            }
        }
        return new ArrayList<>(users.values());
    };

    public static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
}
