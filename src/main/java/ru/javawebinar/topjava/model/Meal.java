package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal {
    private Integer id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public Meal(Integer id, LocalDate date, LocalTime time, String description, int calories) {
        this.id = id;
        this.dateTime = LocalDateTime.of(date, time);
        this.description = description;
        this.calories = calories;
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.id = null;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
