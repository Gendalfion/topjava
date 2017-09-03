package ru.javawebinar.topjava.web.formatters;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateFormatter implements Formatter<LocalDate> {
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        try {
            return LocalDate.parse(text, dateFormatter);
        } catch (DateTimeParseException e) {
            try {
                throw new ParseException(e.getParsedString(), e.getErrorIndex()).initCause(e);
            } catch (Throwable throwable) {
                throw new IllegalStateException(throwable);
            }
        }
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object.format(dateFormatter);
    }
}
