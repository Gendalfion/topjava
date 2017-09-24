package ru.javawebinar.topjava.util.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateUserEmailException extends DuplicateKeyException {
    public DuplicateUserEmailException(String msg) {
        super(msg);
    }

    public DuplicateUserEmailException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
