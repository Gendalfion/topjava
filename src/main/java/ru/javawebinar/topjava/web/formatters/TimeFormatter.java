package ru.javawebinar.topjava.web.formatters;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class TimeFormatter implements Formatter<LocalTime> {
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        try {
            return LocalTime.parse(text, timeFormatter);
        } catch (DateTimeParseException e) {
            try {
                throw new ParseException(e.getParsedString(), e.getErrorIndex()).initCause(e);
            } catch (Throwable throwable) {
                throw new IllegalStateException(throwable);
            }
        }
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object.format(timeFormatter);
    }
}
