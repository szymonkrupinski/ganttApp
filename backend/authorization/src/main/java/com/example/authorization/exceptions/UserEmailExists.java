package com.example.authorization.exceptions;

public class UserEmailExists extends RuntimeException {
    public UserEmailExists(String message) {
        super(message);
    }

    public UserEmailExists(String message, Throwable cause) {
        super(message, cause);
    }

    public UserEmailExists(Throwable cause) {
        super(cause);
    }
}

