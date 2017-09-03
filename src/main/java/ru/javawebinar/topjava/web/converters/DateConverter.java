package ru.javawebinar.topjava.web.converters;

import org.springframework.core.convert.converter.Converter;
import ru.javawebinar.topjava.web.formatters.DateFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateConverter implements Converter<String, LocalDate> {
    private static DateTimeFormatter dateFormatter = DateFormatter.dateFormatter;

    @Override
    public LocalDate convert(String source) {
        try {
            return LocalDate.parse(source, dateFormatter);
        } catch (DateTimeParseException ignored) {}
        return null;
    }
}
