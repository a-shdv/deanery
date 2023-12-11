package org.university.deanery.exceptions;

public class TeacherAlreadyExistsException extends Exception {
    public TeacherAlreadyExistsException() {
    }

    public TeacherAlreadyExistsException(String message) {
        super(message);
    }
}
