package com.gradesystem.exception;

public class GradeSystemException extends Exception {

    public GradeSystemException(String message) {
        super(message);
    }

    public static class DuplicateStudentException extends GradeSystemException {
        public DuplicateStudentException(String rollNumber) {
            super("A student with roll number '" + rollNumber + "' already exists.");
        }
    }

    public static class StudentNotFoundException extends GradeSystemException {
        public StudentNotFoundException(String rollNumber) {
            super("No student found with roll number '" + rollNumber + "'.");
        }
    }

    public static class AssignmentNotFoundException extends GradeSystemException {
        public AssignmentNotFoundException(String name) {
            super("No assignment found with name '" + name + "'.");
        }
    }

    public static class InvalidGradeException extends GradeSystemException {
        public InvalidGradeException(String message) {
            super(message);
        }
    }

    public static class AuthenticationException extends GradeSystemException {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}
