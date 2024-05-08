package com.example.authorization.exceptions;

public class UserNameExsist extends RuntimeException {
    public UserNameExsist(String message) {
        super(message);
    }

    public UserNameExsist(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNameExsist(Throwable cause) {
        super(cause);
    }
}
