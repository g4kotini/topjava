package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, User> repository = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(1);

    {
        MealsUtil.users.forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save: user= \n {}", user);
        if (user.isNew()) {
            int id = counter.getAndIncrement();
            user.setId(id);
            repository.put(id, user);
            log.info("User with id={} has been created", id);
            return user;
        } else {
            User updatedUser = repository.computeIfPresent(user.getId(), (id, oldUserValue) -> user);
            if (updatedUser == null) {
                log.info("There is no users with id={}", user.getId());
            } else {
                log.info("User with id={} has been updated", user.getId());
            }
            return updatedUser;
        }
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return new ArrayList<>(repository.values()).stream()
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .get();
    }
}
