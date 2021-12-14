package com.mastery.simplewebapp.error.exception;

public class SQLConstraintException extends RuntimeException {

    public SQLConstraintException() {
    }

    public SQLConstraintException(String message) {
        super(message);
    }

    public SQLConstraintException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLConstraintException(Throwable cause) {
        super(cause);
    }

}
