
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Course> courses = new ArrayList<>();
    private static final Path FILE_PATH = Paths.get("courses.txt");

    public static void main(String[] args) {
        boolean running = true;

        System.out.println("Welcome to the Student Course Management System!");

        while (running) {
            displayMenu();
            String input = scanner.nextLine().trim();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number from 1 to 7.");
                continue;
            }

            switch (choice) {
                case 1:
                    addCourse();
                    break;
                case 2:
                    viewAllCourses();
                    break;
                case 3:
                    searchCourse();
                    break;
                case 4:
                    computeTotalUnits();
                    break;
                case 5:
                    saveCoursesToFile();
                    break;
                case 6:
                    loadCoursesFromFile();
                    break;
                case 7:
                    running = false;
                    System.out.println("Thank you for using the system!");
                    break;
                default:
                    System.out.println("Invalid choice. Select a number from 1 to 7.");
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n=== STUDENT COURSE MANAGEMENT SYSTEM ===");
        System.out.println("1. Add Course");
        System.out.println("2. View All Courses");
        System.out.println("3. Search Course by Code");
        System.out.println("4. Compute Total Units");
        System.out.println("5. Save to File");
        System.out.println("6. Load from File");
        System.out.println("7. Exit Program");
        System.out.print("Enter your choice: ");
    }

    private static void addCourse() {
        System.out.print("Enter course code: ");
        String code = scanner.nextLine().trim().toUpperCase();

        if (code.isEmpty() || code.contains("|")) {
            System.out.println("Course code cannot be empty or contain the | character.");
            return;
        }

        if (findCourseByCode(code) != null) {
            System.out.println("A course with this code already exists.");
            return;
        }

        System.out.print("Enter course title: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty() || title.contains("|")) {
            System.out.println("Course title cannot be empty or contain the | character.");
            return;
        }

        System.out.print("Enter course units: ");
        String unitsInput = scanner.nextLine().trim();

        try {
            int units = Integer.parseInt(unitsInput);

            if (units <= 0) {
                System.out.println("Course units must be greater than zero.");
                return;
            }

            Course course = new Course(code, title, units);
            courses.add(course);
            System.out.println("Course added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Invalid units. Please enter a whole number, such as 2 or 3.");
        }
    }

    private static void viewAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses are currently recorded.");
            return;
        }

        System.out.println("\n--- ALL RECORDED COURSES ---");
        System.out.printf("%-15s %-30s %s%n",
                "COURSE CODE", "COURSE TITLE", "UNITS");
        System.out.println("---------------------------------------------------------------");

        for (Course course : courses) {
            System.out.printf("%-15s %-30s %d%n",
                    course.getCourseCode(),
                    course.getCourseTitle(),
                    course.getUnits());
        }

        System.out.println("Number of courses (recursive count): "
                + countCoursesRecursively(0));
    }

    private static void searchCourse() {
        System.out.print("Enter course code to search: ");
        String code = scanner.nextLine().trim();

        if (code.isEmpty()) {
            System.out.println("Course code cannot be empty.");
            return;
        }

        Course course = findCourseByCode(code);

        if (course != null) {
            System.out.println("\nCourse found:");
            System.out.println("Course Code: " + course.getCourseCode());
            System.out.println("Course Title: " + course.getCourseTitle());
            System.out.println("Units: " + course.getUnits());
        } else {
            System.out.println("Course not found.");
        }
    }

    private static Course findCourseByCode(String code) {
        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(code.trim())) {
                return course;
            }
        }
        return null;
    }

    private static void computeTotalUnits() {
        if (courses.isEmpty()) {
            System.out.println("No courses available. Add or load courses first.");
            return;
        }

        int totalUnits = 0;

        for (Course course : courses) {
            totalUnits += course.getUnits();
        }

        System.out.println("Total credit units: " + totalUnits);
    }

    private static void saveCoursesToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(
                FILE_PATH,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {

            for (Course course : courses) {
                writer.write(course.toFileLine());
                writer.newLine();
            }

            System.out.println("Courses saved successfully to: "
                    + FILE_PATH.toAbsolutePath());

        } catch (IOException e) {
            System.out.println("Could not save courses: " + e.getMessage());
        }
    }

    private static void loadCoursesFromFile() {
        if (!Files.exists(FILE_PATH)) {
            System.out.println("No saved course file was found yet. Save courses first.");
            return;
        }

        ArrayList<Course> loadedCourses = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(FILE_PATH)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Course course = Course.fromFileLine(line);

                    if (findCourseInList(loadedCourses,
                            course.getCourseCode()) == null) {
                        loadedCourses.add(course);
                    } else {
                        System.out.println(
                                "Skipped duplicate course code on file line "
                                        + lineNumber + ".");
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println(
                            "Skipped invalid data on file line " + lineNumber + ".");
                }
            }

            courses.clear();
            courses.addAll(loadedCourses);

            System.out.println("Loaded " + courses.size()
                    + " course(s) from file.");

        } catch (IOException e) {
            System.out.println("Could not load courses: " + e.getMessage());
        }
    }

    private static Course findCourseInList(
            ArrayList<Course> list, String code) {

        for (Course course : list) {
            if (course.getCourseCode().equalsIgnoreCase(code)) {
                return course;
            }
        }

        return null;
    }

    // Recursive feature required by the assessment.
    private static int countCoursesRecursively(int index) {
        if (index >= courses.size()) {
            return 0;
        }

        return 1 + countCoursesRecursively(index + 1);
    }
}
