package ru.javawebinar.topjava.repository.datajpa;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository mealRepository;

    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository mealRepository, CrudUserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUser(userRepository.getReferenceById(userId));
        return mealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.delete(id, userId) != 0;
    }

    @Transactional(readOnly = true)
    @Override
    public Meal get(int id, int userId) {
//        Meal meal = mealRepository.findById(id).orElse(null);
//        return meal != null && meal.getUser().id() != userId ? null : meal;
        return mealRepository.findById(id).filter(m -> m != null && m.getUser().id() == userId).orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return mealRepository.getBetween(startDateTime, endDateTime, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Meal getWithUser(int id, int userId) {
        Meal meal = get(id, userId);
//        Hibernate.initialize(meal.getUser().getName());
        Hibernate.unproxy(meal);
        User user = Hibernate.unproxy(meal.getUser(), User.class);
        meal.setUser(user);
//        return mealRepository.getWithUser(id, userId);
        return meal;
    }
}
