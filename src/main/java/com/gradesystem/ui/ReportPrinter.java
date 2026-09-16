package com.gradesystem.ui;

import com.gradesystem.exception.GradeSystemException.StudentNotFoundException;
import com.gradesystem.model.Student;
import com.gradesystem.model.Student.Assignment;
import com.gradesystem.model.Student.Grade;
import com.gradesystem.model.Student.LetterGrade;
import com.gradesystem.service.GradeService;
import java.util.List;

public class ReportPrinter {
    private static final String LINE = "=".repeat(60);
    private static final String THIN_LINE = "-".repeat(60);
    private final GradeService gradeService;
    public ReportPrinter(GradeService gradeService) {
        this.gradeService = gradeService;
    }
    public void printReportCard(String rollNumber) throws StudentNotFoundException {
        Student student = gradeService.getStudent(rollNumber);
        List<Grade> grades = gradeService.getGradesForStudent(rollNumber);
        System.out.println(LINE);
        System.out.printf("%-15s %s%n", "REPORT CARD", "");
        System.out.println(LINE);
        System.out.printf("%-15s: %s%n", "Roll Number", student.getRollNumber());
        System.out.printf("%-15s: %s%n", "Name", student.getName());
        System.out.printf("%-15s: %s%n", "Section", student.getSection());
        System.out.println(THIN_LINE);
        if (grades.isEmpty()) {
            System.out.println("No grades recorded yet for this student.");
            System.out.println(LINE);
            return;
        }
        System.out.printf("%-22s %10s %10s %10s%n", "Assignment", "Marks", "Max", "Percent");
        System.out.println(THIN_LINE);
        for (Grade g : grades) {
            try {
                Assignment a = gradeService.getAssignment(g.getAssignmentName());
                double percent = (g.getMarksObtained() / a.getMaxMarks()) * 100.0;
                System.out.printf("%-22s %10.2f %10.2f %9.2f%%%n",
                        g.getAssignmentName(), g.getMarksObtained(), a.getMaxMarks(), percent);
            } catch (Exception ignored) {
                
            }
        }
        double overallPercent = gradeService.computeWeightedPercentage(rollNumber);
        LetterGrade letterGrade = LetterGrade.fromPercentage(overallPercent);

        System.out.println(THIN_LINE);
        System.out.printf("%-22s %10s %10s %9.2f%%%n", "OVERALL (weighted)", "", "", overallPercent);
        System.out.printf("%-22s %10s %10s %10s%n", "Letter Grade", "", "", letterGrade.name());
        System.out.printf("%-22s %10s %10s %10s%n", "Status", "", "", letterGrade.isPassing() ? "PASS" : "FAIL");
        System.out.printf("%-22s %10s %10s %10s%n", "Remark", "", "", letterGrade.getRemark());
        System.out.println(LINE);
    }
    public void printClassSummary() {
        List<Student> students = gradeService.getAllStudents();
        System.out.println(LINE);
        System.out.println("CLASS SUMMARY");
        System.out.println(LINE);
        if (students.isEmpty()) {
            System.out.println("No students registered yet.");
            System.out.println(LINE);
            return;
        }
        System.out.printf("%-10s %-20s %-8s %10s %8s %6s%n", "Roll No", "Name", "Section", "Percent", "Grade", "Status");
        System.out.println(THIN_LINE);
        double sumPercent = 0;
        int passCount = 0;
        for (Student s : students) {
            double percent = gradeService.computeWeightedPercentage(s.getRollNumber());
            LetterGrade letterGrade = LetterGrade.fromPercentage(percent);
            sumPercent += percent;
            if (letterGrade.isPassing()) passCount++;

            System.out.printf("%-10s %-20s %-8s %9.2f%% %8s %6s%n",
                    s.getRollNumber(), s.getName(), s.getSection(),
                    percent, letterGrade.name(), letterGrade.isPassing() ? "PASS" : "FAIL");
        }
        double classAverage = sumPercent / students.size();
        double passRate = (passCount * 100.0) / students.size();
        System.out.println(THIN_LINE);
        System.out.printf("%-20s: %.2f%%\n", "Class Average", classAverage);
        System.out.printf("%-20s: %d / %d\n", "Students Passing", passCount, students.size());
        System.out.printf("%-20s: %.2f%%\n", "Pass Rate", passRate);
        System.out.println(LINE);
    }
    public void printStudentList() {
        List<Student> students = gradeService.getAllStudents();
        System.out.println(THIN_LINE);
        System.out.printf("%-10s %-20s %-10s%n", "Roll No", "Name", "Section");
        System.out.println(THIN_LINE);
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.println(THIN_LINE);
        System.out.println("Total students: " + students.size());
    }
    public void printAssignmentList() {
        List<Assignment> assignments = gradeService.getAllAssignments();
        System.out.println(THIN_LINE);
        System.out.printf("%-20s %10s %10s%n", "Assignment", "Max Marks", "Weight %");
        System.out.println(THIN_LINE);
        for (Assignment a : assignments) {
            System.out.printf("%-20s %10.2f %9.2f%%%n", a.getName(), a.getMaxMarks(), a.getWeightPercent());
        }
        System.out.println(THIN_LINE);
        System.out.printf("Total weight assigned: %.2f%%%n", gradeService.getTotalWeight());
    }
}
