# Project Statement: Student Grade Management System

## Problem Statement

Instructors handling small to mid-sized classes without institutional software often resort to spreadsheets or paper records to track student marks across multiple assignments, each carrying a different weight toward a final grade. This approach is error-prone: it offers no validation against duplicate entries, no protection against marks exceeding an assignment's maximum, no centralised authentication, and no consistent way to compute a weighted overall percentage and letter grade.



The **Student Grade Management System** addresses this by providing a terminal-based Java application that handles the full workflow of grade management — from instructor authentication through student/assignment setup to weighted grade calculation and report generation — using only core Java, runnable from the command line on any machine with a JDK installed.

## Scope of the Project

* Instructor login/authentication to restrict access to grade-management functions.
* Creation and management of student records.
* Creation and management of assignments, including weighting.
* Recording and updating of student grades per assignment.
* Automatic calculation of weighted percentage scores.
* Generation of formatted, human-readable report cards per student.
* Full operation via a command-line interface (CLI) — no GUI required.
* Buildable and runnable via both Maven and plain `javac`/`java`.

## Target Users

* **Instructors / course handlers** — the primary users, who log in to add students and assignments, record and update grades, and generate report cards.
* **Course evaluators / graders** — secondary users who run and review the application as part of a course submission, verifying functionality and code quality from the command line.
* **Students** (indirectly) — the subjects of the records managed by the system, represented through the report cards it produces.

## High-Level Features

1. **Instructor Authentication** — Secure login gate before any management functionality is accessible.
2. **Student Management** — Add, view, and manage student records.
3. **Assignment Management** — Define assignments and assign weights toward the final grade.
4. **Grade Recording** — Enter and update grades for students against specific assignments.
5. **Weighted Percentage Calculation** — Automatically compute a student's overall weighted score based on assignment weights.
6. **Report Card Generation** — Produce clearly formatted, per-student report cards summarizing assignments, grades, and overall performance.
7. **Robust Error Handling** — Graceful handling of invalid input and edge cases across all menu paths.
8. **CLI-First Design** — Entirely operable from a terminal, with no GUI dependency, satisfying course executability requirements.
9. **Dual Build Support** — Compilable and runnable through both Maven and direct `javac`/`java` commands.

