package com.gradesystem.model;

public class Student {

    private final String rollNumber;
    private String name;
    private String section;

    public Student(String rollNumber, String name, String section){
        this.rollNumber = rollNumber;
        this.name = name;
        this.section = section;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String toFileFormat() {
        return rollNumber + "|" + name + "|" + section;
    }

    public static Student fromFileFormat(String line) {
        String[] parts = line.split("\\|", -1);
        return new Student(parts[0], parts[1], parts[2]);
    }

    @Override
    public String toString() {
        return String.format("%-10s %-20s %-10s", rollNumber, name, section);
    }

    public static class Assignment {

        private final String name;
        private final double maxMarks;
        private final double weightPercent;

        public Assignment(String name, double maxMarks, double weightPercent) {
            this.name = name;
            this.maxMarks = maxMarks;
            this.weightPercent = weightPercent;
        }

        public String getName() {
            return name;
        }

        public double getMaxMarks() {
            return maxMarks;
        }

        public double getWeightPercent() {
            return weightPercent;
        }

        public String toFileFormat() {
            return name + "|" + maxMarks + "|" + weightPercent;
        }

        public static Assignment fromFileFormat(String line) {
            String[] parts = line.split("\\|", -1);
            return new Assignment(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
        }

        @Override
        public String toString() {
            return String.format("%-20s max=%-6.1f weight=%-5.1f%%", name, maxMarks, weightPercent);
        }
    }

    public static class Grade {

        private final String studentRollNumber;
        private final String assignmentName;
        private final double marksObtained;

        public Grade(String studentRollNumber, String assignmentName, double marksObtained) {
            this.studentRollNumber = studentRollNumber;
            this.assignmentName = assignmentName;
            this.marksObtained = marksObtained;
        }

        public String getStudentRollNumber() {
            return studentRollNumber;
        }

        public String getAssignmentName() {
            return assignmentName;
        }

        public double getMarksObtained() {
            return marksObtained;
        }

        public String toFileFormat() {
            return studentRollNumber + "|" + assignmentName + "|" + marksObtained;
        }

        public static Grade fromFileFormat(String line) {
            String[] parts = line.split("\\|", -1);
            return new Grade(parts[0], parts[1], Double.parseDouble(parts[2]));
        }
    }

    public enum LetterGrade {
        A(90, 100, "Excellent"),
        B(80, 89.999, "Good"),
        C(70, 79.999, "Satisfactory"),
        D(60, 69.999, "Passing"),
        F(0, 59.999, "Fail");

        private final double lowerBound;
        private final double upperBound;
        private final String remark;

        LetterGrade(double lowerBound, double upperBound, String remark) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }

        public boolean isPassing() {
            return this != F;
        }

        public static LetterGrade fromPercentage(double percentage) {
            for (LetterGrade grade : values()) {
                if (percentage >= grade.lowerBound && percentage <= grade.upperBound) {
                    return grade;
                }
            }
            return percentage > 100 ? A : F;
        }
    }
}
