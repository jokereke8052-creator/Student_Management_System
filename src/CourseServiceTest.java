public class CourseServiceTest {

    public static void main(String[] args) {

        CourseService service = new CourseService();

        try {

            // Load existing courses.
            int loaded = service.loadCoursesFromFile();

            System.out.println("Loaded courses: " + loaded);

            // Display all courses.
            for (Course course : service.getAllCourses()) {

                System.out.println(
                        course.getCourseCode() + " | "
                        + course.getCourseTitle() + " | "
                        + course.getUnits()
                );
            }

            // Calculate total units.
            System.out.println(
                    "Total units: " + service.getTotalUnits()
            );

            // Recursive count.
            System.out.println(
                    "Recursive count: "
                    + service.countCoursesRecursively()
            );

            // Test searching.
            Course found = service.findCourseByCode("COS 201");

            System.out.println(
                    "Search test: "
                    + (found != null ? "PASSED" : "FAILED")
            );

        } catch (Exception e) {

            System.out.println("Test failed: " + e.getMessage());
        }
    }
}