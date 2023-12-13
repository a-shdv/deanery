package org.university.deanery.exceptions;

public class TimetableAlreadyExistsException extends Exception {
    public TimetableAlreadyExistsException() {
    }

    public TimetableAlreadyExistsException(String message) {
        super(message);
    }
}
