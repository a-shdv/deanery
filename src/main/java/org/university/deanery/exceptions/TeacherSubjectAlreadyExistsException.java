package org.university.deanery.exceptions;

public class TeacherSubjectAlreadyExistsException extends Exception {
    public TeacherSubjectAlreadyExistsException() {
    }

    public TeacherSubjectAlreadyExistsException(String message) {
        super(message);
    }
}
