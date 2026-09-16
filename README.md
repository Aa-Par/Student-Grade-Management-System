# Student Grade Management System

A command-line Java application that lets an instructor log in, register students,
define graded assignments, record marks, and generate formatted report cards and
class-wide statistics directly in the terminal.

Built entirely with core Java (no external libraries/dependencies) and simple
file-based persistence, so it runs anywhere a JDK is installed.

---

## Features

- **Instructor login** with a default account provided out of the box.
- **Student management** — add, list, and remove students (roll number, name, section).
- **Assignment management** — define assignments with a max mark and a weight
  (percentage contribution to the final grade).
- **Grade entry** — record or update a student's marks for any assignment, with
  validation (no negative marks, no marks above the assignment maximum).
- **Report cards** — a per-student breakdown of every assignment score plus a
  weighted overall percentage, letter grade (A–F), and pass/fail status, printed
  with aligned `printf`-formatted tables.
- **Class summary** — every student's result in one table, plus class average and
  pass rate statistics.
- **Persistent storage** — all data is saved to plain pipe-delimited text files in
  a `data/` folder, so your data survives between runs.
- **Custom exception hierarchy** for clear, specific error messages (duplicate
  student, student/assignment not found, invalid grade, authentication failure).

---

## Requirements

- **Java Development Kit (JDK) 17 or later.**
- **No other dependencies.** The project uses only the Java standard library.
- Maven is optional, but the project can be built and run with plain `javac`/`java` as well.

---

## Project Structure

```
StudentGradeManagementSystem/
├── pom.xml
├── README.md
├── data/                          # created automatically at first run
└── src/
    └── main/
        └── java/
            └── com/gradesystem/
                ├── Main.java                     # CLI entry point / menu loop
                ├── model/
                │   └── Student.java               # Student, + nested Assignment,
                │                                   # Grade, and LetterGrade enum
                ├── exception/
                │   └── GradeSystemException.java  # base + 5 nested exception types
                ├── service/
                │   ├── GradeService.java          # core business logic
                │   └── AuthService.java           # login handling
                ├── ui/
                │   └── ReportPrinter.java         # formatted terminal output
                └── util/
                    └── FileManager.java           # file read/write helper
```

**A note on file layout:** `Assignment`, `Grade`, and `LetterGrade` are nested
inside `Student.java` (as `Student.Assignment`, `Student.Grade`, `Student.LetterGrade`),
and all five custom exceptions are nested inside `GradeSystemException.java` (as
`GradeSystemException.DuplicateStudentException`, etc.). This keeps related types
in one file instead of scattering many small classes across the tree, while each
nested type remains fully public and is imported normally, e.g.:
```java
import com.gradesystem.model.Student.Assignment;
import com.gradesystem.exception.GradeSystemException.StudentNotFoundException;
```

---

## Setup and Run Instructions

These steps assume a fresh machine with no prior context about this project.

### Option A — Using plain `javac` / `java` (no Maven required)

1. **Clone the repository:**
   ```bash
   git clone https://github.com/<your-username>/<your-repo-name>.git
   cd <your-repo-name>
   ```

2. **Compile all source files** into an `out/` directory:
   ```bash
   javac -d out $(find src -name "*.java")
   ```
   On Windows (PowerShell), use:
   ```powershell
   javac -d out (Get-ChildItem -Recurse -Filter *.java -Path src).FullName
   ```

3. **Run the application:**
   ```bash
   java -cp out com.gradesystem.Main
   ```

### Option B — Using Maven

1. **Clone the repository** (same as above).

2. **Compile the project:**
   ```bash
   mvn compile
   ```

3. **Run the application:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.gradesystem.Main"
   ```
   *(If you don't have the `exec` plugin configured, you can instead package and
   run the jar — see below.)*

   **Or build a runnable jar:**
   ```bash
   mvn package
   java -jar target/student-grade-management-system.jar
   ```

---

## First-Time Login

The application ships with a default instructor account so you can log in
immediately:

```
Username: admin
Password: admin123
```

Credentials are stored in `data/credentials.txt` after the first run. You have
3 login attempts per session before the program exits.

---

## Using the Application

After logging in, you'll see a numbered menu:

```
1. Add Student
2. Add Assignment
3. Record Grade
4. View Student Report Card
5. View Class Summary
6. List All Students
7. List All Assignments
8. Remove Student
0. Exit
```
---

## Data Files

| File                    | Contents                                      |
|-------------------------|------------------------------------------------|
| `data/students.txt`     | `rollNumber\|name\|section`                    |
| `data/assignments.txt`  | `name\|maxMarks\|weightPercent`                |
| `data/grades.txt`       | `rollNumber\|assignmentName\|marksObtained`    |
| `data/credentials.txt`  | `username\|password`                           |

These files are plain text and human-readable; you can inspect them directly
to verify what has been saved.

---

## Grading Scale

| Percentage | Letter | Status |
|-----------|--------|--------|
| 90 – 100  | A      | Pass   |
| 80 – 89.9 | B      | Pass   |
| 70 – 79.9 | C      | Pass   |
| 60 – 69.9 | D      | Pass   |
| 0 – 59.9  | F      | Fail   |

---

## Error Handling

The system uses a custom checked-exception hierarchy so every failure path
gives a specific, actionable message instead of a stack trace:

- Adding a student with a roll number that already exists → `DuplicateStudentException`
- Looking up a roll number / assignment name that doesn't exist → `StudentNotFoundException` / `AssignmentNotFoundException`
- Entering negative marks or marks above an assignment's maximum → `InvalidGradeException`
- Failed login attempts → `AuthenticationException`

The application also exits gracefully (no stack trace) if input is piped in
and the stream closes unexpectedly, which makes it safe to script or
auto-test from the command line.

---
