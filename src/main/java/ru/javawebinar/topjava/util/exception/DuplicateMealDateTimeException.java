package ru.javawebinar.topjava.util.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateMealDateTimeException extends DuplicateKeyException {
    public DuplicateMealDateTimeException(String msg) {
        super(msg);
    }

    public DuplicateMealDateTimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
