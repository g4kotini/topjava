package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
       return DateTimeUtil.parseLocalDate(text);
    }

    @Override
    public String print(LocalDate date, Locale locale) {
        if (date == null) {
            return "";
        }
        return date.toString();
    }
}
