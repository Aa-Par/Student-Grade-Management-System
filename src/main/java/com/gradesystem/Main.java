package com.gradesystem;

import com.gradesystem.exception.GradeSystemException;
import com.gradesystem.exception.GradeSystemException.AuthenticationException;
import com.gradesystem.service.AuthService;
import com.gradesystem.service.GradeService;
import com.gradesystem.ui.ReportPrinter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final GradeService gradeService = new GradeService();
    private static final ReportPrinter reportPrinter = new ReportPrinter(gradeService);
    public static void main(String[] args) {
        System.out.println(" STUDENT GRADE MANAGEMENT SYSTEM :");
        if (!login()) {
            System.out.println("Too many failed login attempts. Exiting.");
            return;
        }
        runMenu();
        System.out.println("Goodbye.");
        scanner.close();
    }
    private static boolean login() {
        System.out.println("(Default credentials: username: admin, password: admin123)");
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Username: ");
            String username = readLine();
            if (username == null) return false;
            System.out.print("Password: ");
            String password = readLine();
            if (password == null) return false;

            try {
                authService.login(username, password);
                System.out.println("Login successful. Welcome, " + username + ".\n");
                return true;
            } catch (AuthenticationException e) {
                attempts++;
                System.out.println(e.getMessage() + " (" + (3 - attempts) + " attempts remaining)");
            }
        }
        return false;
    }
    private static void runMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine();
            if (choice == null) break; // stdin closed

            switch (choice.trim()) {
                case "1" -> addStudent();
                case "2" -> addAssignment();
                case "3" -> recordGrade();
                case "4" -> viewReportCard();
                case "5" -> reportPrinter.printClassSummary();
                case "6" -> reportPrinter.printStudentList();
                case "7" -> reportPrinter.printAssignmentList();
                case "8" -> removeStudent();
                case "0" -> running = false;
                default -> System.out.println("Invalid option. Please choose a number from the menu.");
            }
            System.out.println();
        }
    }
    private static void printMenu() {
        System.out.println("MAIN MENU :");
        System.out.println("1. Add a Student");
        System.out.println("2. Add an Assignment");
        System.out.println("3. Record Grade");
        System.out.println("4. View Student Report Card");
        System.out.println("5. View Class Summary");
        System.out.println("6. List All Students");
        System.out.println("7. List All Assignments");
        System.out.println("8. Remove Student");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
    private static void addStudent() {
        System.out.print("Roll Number: ");
        String roll = readLine();
        if (roll == null || roll.isBlank()) {
            System.out.println("Roll number cannot be empty.");
            return;
        }
        System.out.print("Name: ");
        String name = readLine();
        System.out.print("Section: ");
        String section = readLine();
        try {
            gradeService.addStudent(roll.trim(), name == null ? "" : name.trim(),
                    section == null ? "" : section.trim());
            System.out.println("Student added successfully.");
        } catch (GradeSystemException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void addAssignment() {
        System.out.print("Assignment Name: ");
        String name = readLine();
        if (name == null || name.isBlank()) {
            System.out.println("Assignment name cannot be empty.");
            return;
        }
        Double maxMarks = readDouble("Max Marks: ");
        if (maxMarks == null) return;
        Double weight = readDouble("Weight (% of final grade): ");
        if (weight == null) return;

        try {
            gradeService.addAssignment(name.trim(), maxMarks, weight);
            System.out.println("Assignment added successfully.");
            double totalWeight = gradeService.getTotalWeight();
            if (totalWeight > 100.0001) {
                System.out.printf("Warning: total assignment weight is now %.2f%%, which exceeds 100%%.%n",
                        totalWeight);
            }
        } catch (GradeSystemException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void recordGrade() {
        System.out.print("Student Roll Number: ");
        String roll = readLine();
        if (roll == null) return;
        System.out.print("Assignment Name: ");
        String assignment = readLine();
        if (assignment == null) return;
        Double marks = readDouble("Marks Obtained: ");
        if (marks == null) return;

        try {
            gradeService.recordGrade(roll.trim(), assignment.trim(), marks);
            System.out.println("Grade recorded successfully.");
        } catch (GradeSystemException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void viewReportCard() {
        System.out.print("Student Roll Number: ");
        String roll = readLine();
        if (roll == null) return;
        try {
            reportPrinter.printReportCard(roll.trim());
        } catch (GradeSystemException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void removeStudent() {
        System.out.print("Student Roll Number to remove: ");
        String roll = readLine();
        if (roll == null) return;
        try {
            gradeService.removeStudent(roll.trim());
            System.out.println("Student removed successfully.");
        } catch (GradeSystemException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static String readLine() {
        try {
            if (!scanner.hasNextLine()) return null;
            return scanner.nextLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            return null;
        }
    }
    private static Double readDouble(String prompt) {
        for (int i = 0; i < 2; i++) {
            System.out.print(prompt);
            String line = readLine();
            if (line == null) return null;
            try {
                return Double.valueOf(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        System.out.println("Too many invalid attempts; cancelling operation.");
        return null;
    }
}
