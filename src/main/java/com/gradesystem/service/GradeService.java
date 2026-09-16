package com.gradesystem.service;

import com.gradesystem.exception.GradeSystemException.AssignmentNotFoundException;
import com.gradesystem.exception.GradeSystemException.DuplicateStudentException;
import com.gradesystem.exception.GradeSystemException.InvalidGradeException;
import com.gradesystem.exception.GradeSystemException.StudentNotFoundException;
import com.gradesystem.model.Student;
import com.gradesystem.model.Student.Assignment;
import com.gradesystem.model.Student.Grade;
import com.gradesystem.util.FileManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GradeService {
    private static final String STUDENTS_FILE = "students.txt";
    private static final String ASSIGNMENTS_FILE = "assignments.txt";
    private static final String GRADES_FILE = "grades.txt";
    private final FileManager fileManager = new FileManager();
    private final Map<String, Student> students = new LinkedHashMap<>();
    private final Map<String, Assignment> assignments = new LinkedHashMap<>();
    private final List<Grade> grades = new ArrayList<>();

    public GradeService() {
        loadAll();
    }
    private void loadAll() {
        for (String line : fileManager.readLines(STUDENTS_FILE)) {
            Student s = Student.fromFileFormat(line);
            students.put(s.getRollNumber(), s);
        }
        for (String line : fileManager.readLines(ASSIGNMENTS_FILE)) {
            Assignment a = Assignment.fromFileFormat(line);
            assignments.put(a.getName(), a);
        }
        for (String line : fileManager.readLines(GRADES_FILE)) {
            grades.add(Grade.fromFileFormat(line));
        }
    }
    private void persistStudents() {
        List<String> lines = new ArrayList<>();
        for (Student s : students.values()) lines.add(s.toFileFormat());
        fileManager.writeLines(STUDENTS_FILE, lines);
    }
    private void persistAssignments() {
        List<String> lines = new ArrayList<>();
        for (Assignment a : assignments.values()) lines.add(a.toFileFormat());
        fileManager.writeLines(ASSIGNMENTS_FILE, lines);
    }
    private void persistGrades() {
        List<String> lines = new ArrayList<>();
        for (Grade g : grades) lines.add(g.toFileFormat());
        fileManager.writeLines(GRADES_FILE, lines);
    }
    public void addStudent(String rollNumber, String name, String section) throws DuplicateStudentException {
        if (students.containsKey(rollNumber)) {
            throw new DuplicateStudentException(rollNumber);
        }
        students.put(rollNumber, new Student(rollNumber, name, section));
        persistStudents();
    }

    public Student getStudent(String rollNumber) throws StudentNotFoundException {
        Student s = students.get(rollNumber);
        if (s == null) {
            throw new StudentNotFoundException(rollNumber);
        }
        return s;
    }
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    public boolean removeStudent(String rollNumber) throws StudentNotFoundException {
        if (!students.containsKey(rollNumber)) {
            throw new StudentNotFoundException(rollNumber);
        }
        students.remove(rollNumber);
        grades.removeIf(g -> g.getStudentRollNumber().equals(rollNumber));
        persistStudents();
        persistGrades();
        return true;
    }
    public void addAssignment(String name, double maxMarks, double weightPercent) throws InvalidGradeException {
        if (maxMarks <= 0) {
            throw new InvalidGradeException("Maximum marks must be greater than zero.");
        }
        if (weightPercent < 0 || weightPercent > 100) {
            throw new InvalidGradeException("Weight percentage must be between 0 and 100.");
        }
        assignments.put(name, new Assignment(name, maxMarks, weightPercent));
        persistAssignments();
    }

    public Assignment getAssignment(String name) throws AssignmentNotFoundException {
        Assignment a = assignments.get(name);
        if (a == null) {
            throw new AssignmentNotFoundException(name);
        }
        return a;
    }
    public List<Assignment> getAllAssignments() {
        return new ArrayList<>(assignments.values());
    }
    public double getTotalWeight() {
        double total = 0;
        for (Assignment a : assignments.values()) {
            total += a.getWeightPercent();
        }
        return total;
    }
    public void recordGrade(String rollNumber, String assignmentName, double marks)
            throws StudentNotFoundException, AssignmentNotFoundException, InvalidGradeException {
        getStudent(rollNumber); 
        Assignment assignment = getAssignment(assignmentName); 

        if (marks < 0) {
            throw new InvalidGradeException("Marks cannot be negative.");
        }
        if (marks > assignment.getMaxMarks()) {
            throw new InvalidGradeException(
                    String.format("Marks (%.2f) cannot exceed the maximum for '%s' (%.2f).",
                            marks, assignmentName, assignment.getMaxMarks()));
        }

        grades.removeIf(g -> g.getStudentRollNumber().equals(rollNumber) && g.getAssignmentName().equals(assignmentName));
        grades.add(new Grade(rollNumber, assignmentName, marks));
        persistGrades();
    }
    public List<Grade> getGradesForStudent(String rollNumber) {
        List<Grade> result = new ArrayList<>();
        for (Grade g : grades) {
            if (g.getStudentRollNumber().equals(rollNumber)) {
                result.add(g);
            }
        }
        return result;
    }
    public double computeWeightedPercentage(String rollNumber) {
        List<Grade> studentGrades = getGradesForStudent(rollNumber);
        double weightedSum = 0;
        double weightUsed = 0;
        for (Grade g : studentGrades) {
            Assignment a = assignments.get(g.getAssignmentName());
            if (a == null) continue; 
            double scorePercent = (g.getMarksObtained() / a.getMaxMarks()) * 100.0;
            weightedSum += scorePercent * a.getWeightPercent();
            weightUsed += a.getWeightPercent();
        }
        if (weightUsed == 0) {
            return 0.0;
        }
        return weightedSum / weightUsed;
    }
}
