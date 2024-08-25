package com.example.authorization.exceptions;

public class UserDontExists extends RuntimeException {
        public UserDontExists(String message) {
            super(message);
        }

        public UserDontExists(String message, Throwable cause) {
            super(message, cause);
        }

        public UserDontExists(Throwable cause) {
            super(cause);
        }
    }

