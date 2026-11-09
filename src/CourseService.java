
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CourseService {

    // Stores all courses currently in memory.
    private final ArrayList<Course> courses = new ArrayList<>();

    // Location of the course records.
    private final Path filePath =
            Paths.get("data", "courses.txt");

    // 1. ADD COURSE
    public Course addCourse(String code, String title, int units) {

        if (code == null || code.trim().isEmpty()
                || code.contains("|")) {
            throw new IllegalArgumentException(
                    "Course code cannot be empty or contain |.");
        }

        if (title == null || title.trim().isEmpty()
                || title.contains("|")) {
            throw new IllegalArgumentException(
                    "Course title cannot be empty or contain |.");
        }

        if (units <= 0) {
            throw new IllegalArgumentException(
                    "Course units must be greater than zero.");
        }

        code = code.trim().toUpperCase(Locale.ROOT);
        title = title.trim();

        if (findCourseByCode(code) != null) {
            throw new IllegalArgumentException(
                    "A course with this code already exists.");
        }

        Course course = new Course(code, title, units);

        courses.add(course);

        return course;
    }

    // 2. GET ALL COURSES
    public List<Course> getAllCourses() {

        return Collections.unmodifiableList(
                new ArrayList<>(courses));
    }

    // 3. SEARCH COURSE BY CODE
    public Course findCourseByCode(String code) {

        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        for (Course course : courses) {

            if (course.getCourseCode()
                    .equalsIgnoreCase(code.trim())) {

                return course;
            }
        }

        return null;
    }

    // 4. COMPUTE TOTAL CREDIT UNITS
    public int getTotalUnits() {

        int totalUnits = 0;

        for (Course course : courses) {
            totalUnits += course.getUnits();
        }

        return totalUnits;
    }

    // 5. SAVE COURSES TO FILE
    public void saveCoursesToFile() throws IOException {

        Path parent = filePath.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {

            for (Course course : courses) {

                writer.write(course.toFileLine());
                writer.newLine();
            }
        }
    }

    // 6. LOAD COURSES FROM FILE
    public int loadCoursesFromFile() throws IOException {

        if (!Files.exists(filePath)) {
            return 0;
        }

        ArrayList<Course> loadedCourses = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {

                    Course course = Course.fromFileLine(line);

                    if (findCourseInList(
                            loadedCourses,
                            course.getCourseCode()) == null) {

                        loadedCourses.add(course);
                    } else {

                        System.out.println(
                                "Skipped duplicate course code on line "
                                + lineNumber);
                    }

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Skipped invalid course data on line "
                            + lineNumber);
                }
            }
        }

        courses.clear();
        courses.addAll(loadedCourses);

        return courses.size();
    }

    // Helper method for checking duplicate records while loading.
    private Course findCourseInList(
            List<Course> list, String code) {

        for (Course course : list) {

            if (course.getCourseCode()
                    .equalsIgnoreCase(code)) {

                return course;
            }
        }

        return null;
    }

    // 7. RECURSIVE COURSE COUNTING
    public int countCoursesRecursively() {

        return countCoursesRecursively(0);
    }

    private int countCoursesRecursively(int index) {

        // Base case: stop when we reach the end.
        if (index >= courses.size()) {
            return 0;
        }

        // Recursive case: count one and move to the next.
        return 1 + countCoursesRecursively(index + 1);
    }
}
