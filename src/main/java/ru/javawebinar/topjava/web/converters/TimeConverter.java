package ru.javawebinar.topjava.web.converters;

import org.springframework.core.convert.converter.Converter;
import ru.javawebinar.topjava.web.formatters.TimeFormatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeConverter implements Converter<String, LocalTime> {
    private static DateTimeFormatter timeFormatter = TimeFormatter.timeFormatter;

    @Override
    public LocalTime convert(String source) {
        try {
            return LocalTime.parse(source, timeFormatter);
        } catch (DateTimeParseException ignored) {}
        return null;
    }
}
