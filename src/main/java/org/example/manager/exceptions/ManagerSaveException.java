package org.example.manager.exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message) {
        super(message);
    }

    public ManagerSaveException(String message, Exception exception) {
        super(message, exception);
    }
}
