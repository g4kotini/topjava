package ru.javawebinar.topjava.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static LocalDate parseLocalDate(@Nullable String date) {
        return StringUtils.hasLength(date) ? LocalDate.parse(date) : null;
    }

    public static LocalTime parseLocalTime(@Nullable String time) {
        return StringUtils.hasLength(time) ? LocalTime.parse(time) : null;
    }

    public static LocalDate atCurrentDateOrMin(@Nullable LocalDate startDate) {
        return startDate == null ? LocalDate.MIN : startDate;
    }

    public static LocalDate atStartOfNextDayOrMax(@Nullable LocalDate endDate) {
        return endDate == null ? LocalDate.MAX : endDate.plusDays(1);
    }

    public static LocalTime atCurrentTimeOrMin(@Nullable LocalTime startTime) {
        return startTime == null ? LocalTime.MIN : startTime;
    }

    public static LocalTime atCurrentTimeOrMax(@Nullable LocalTime endTime) {
        return endTime == null ? LocalTime.MAX : endTime;
    }

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T lt, @NonNull T start, @NonNull T end) {
        return lt.compareTo(start) >= 0 && lt.compareTo(end) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}
