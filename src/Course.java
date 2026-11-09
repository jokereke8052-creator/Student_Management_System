
public class Course {
    private String courseCode;
    private String courseTitle;
    private int units;

    public Course(String courseCode, String courseTitle, int units) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.units = units;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public int getUnits() {
        return units;
    }

    public String toFileLine() {
        return courseCode + "|" + courseTitle + "|" + units;
    }

    public static Course fromFileLine(String line) {
        String[] parts = line.split("\\|", -1);

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid course record.");
        }

        String code = parts[0].trim();
        String title = parts[1].trim();
        int units = Integer.parseInt(parts[2].trim());

        if (code.isEmpty() || title.isEmpty() || units <= 0) {
            throw new IllegalArgumentException("Invalid course details.");
        }

        return new Course(code, title, units);
    }
}
