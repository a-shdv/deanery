package org.university.deanery.exceptions;

public class TimetableNotFoundException extends Exception {
    public TimetableNotFoundException() {
    }

    public TimetableNotFoundException(String message) {
        super(message);
    }
}
