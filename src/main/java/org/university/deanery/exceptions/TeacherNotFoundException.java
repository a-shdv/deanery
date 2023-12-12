package org.university.deanery.exceptions;

public class TeacherNotFoundException extends Exception {
    public TeacherNotFoundException() {
    }

    public TeacherNotFoundException(String message) {
        super(message);
    }
}
