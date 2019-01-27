package com.validation;

public class DateValidationException extends RuntimeException {

    public DateValidationException(String message) {
        super(message);
    }

    public DateValidationException(Throwable cause) {
        super(cause);
    }

    public DateValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
