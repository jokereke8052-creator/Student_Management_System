
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class CourseDashboard extends JFrame {

    // Application service
    private final CourseService courseService;

    // Table components
    private JTable courseTable;
    private DefaultTableModel tableModel;

    // Dashboard statistics
    private JLabel totalCoursesLabel;
    private JLabel totalUnitsLabel;
    private JLabel recursiveCountLabel;

    // Search field
    private JTextField searchField;

    // Application colours
    private final Color SIDEBAR_COLOR = new Color(30, 41, 59);
    private final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private final Color ACCENT_COLOR = new Color(37, 99, 235);
    private final Color TEXT_COLOR = new Color(31, 41, 55);

    public CourseDashboard() {

        courseService = new CourseService();

        // Load existing course records.
        try {
            courseService.loadCoursesFromFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load courses: " + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        // Configure application window.
        setTitle("Student Course Management System");
        setSize(1100, 720);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Build the interface.
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainContent(), BorderLayout.CENTER);

        refreshDashboard();
    }

    // SIDEBAR
    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(
                BorderFactory.createEmptyBorder(25, 18, 20, 18)
        );

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel appName = new JLabel("COURSE MANAGER");
        appName.setForeground(Color.WHITE);
        appName.setFont(new Font("SansSerif", Font.BOLD, 17));

        JLabel subtitle = new JLabel("Student Management");
        subtitle.setForeground(new Color(203, 213, 225));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));

        topPanel.add(appName);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(subtitle);
        topPanel.add(Box.createVerticalStrut(45));

        JLabel section = new JLabel("MENU");
        section.setForeground(new Color(148, 163, 184));
        section.setFont(new Font("SansSerif", Font.BOLD, 11));

        topPanel.add(section);
        topPanel.add(Box.createVerticalStrut(15));

        JLabel overview = new JLabel("  Dashboard");
        overview.setForeground(Color.WHITE);
        overview.setFont(new Font("SansSerif", Font.BOLD, 14));

        topPanel.add(overview);

        JButton exitButton = new JButton("Exit Application");
        styleButton(exitButton, new Color(71, 85, 105), Color.WHITE);
        exitButton.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(exitButton, BorderLayout.CENTER);

        sidebar.add(topPanel, BorderLayout.NORTH);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    // MAIN CONTENT
    private JPanel createMainContent() {

        JPanel content = new JPanel(new BorderLayout(0, 22));
        content.setBackground(BACKGROUND_COLOR);
        content.setBorder(
                BorderFactory.createEmptyBorder(28, 30, 25, 30)
        );

        content.add(createHeader(), BorderLayout.NORTH);
        content.add(createCenterPanel(), BorderLayout.CENTER);

        return content;
    }

    // HEADER
    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        title.setForeground(TEXT_COLOR);

        JLabel description = new JLabel(
                "Manage your courses and academic credit units."
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 13));
        description.setForeground(new Color(107, 114, 128));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(6));
        titlePanel.add(description);

        JButton addButton = new JButton("+ Add Course");
        styleButton(addButton, ACCENT_COLOR, Color.WHITE);
        addButton.addActionListener(e -> showAddCourseDialog());

        header.add(titlePanel, BorderLayout.WEST);
        header.add(addButton, BorderLayout.EAST);

        return header;
    }

    // CENTER PANEL
    private JPanel createCenterPanel() {

        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setOpaque(false);

        center.add(createStatisticsPanel(), BorderLayout.NORTH);
        center.add(createCoursePanel(), BorderLayout.CENTER);

        return center;
    }

    // STATISTICS CARDS
    private JPanel createStatisticsPanel() {

        JPanel statistics = new JPanel(new GridLayout(1, 3, 15, 0));
        statistics.setOpaque(false);

        JPanel coursesCard = createStatCard("Total Courses");
        JPanel unitsCard = createStatCard("Total Credit Units");
        JPanel countCard = createStatCard("Recursive Count");

        totalCoursesLabel = (JLabel) coursesCard.getClientProperty("valueLabel");
        totalUnitsLabel = (JLabel) unitsCard.getClientProperty("valueLabel");
        recursiveCountLabel = (JLabel) countCard.getClientProperty("valueLabel");

        statistics.add(coursesCard);
        statistics.add(unitsCard);
        statistics.add(countCard);

        return statistics;
    }

    private JPanel createStatCard(String heading) {

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(229, 231, 235)
                ),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel title = new JLabel(heading);
        title.setFont(new Font("SansSerif", Font.PLAIN, 13));
        title.setForeground(new Color(107, 114, 128));

        JLabel value = new JLabel("0");
        value.setFont(new Font("SansSerif", Font.BOLD, 28));
        value.setForeground(TEXT_COLOR);

        card.add(title);
        card.add(Box.createVerticalStrut(12));
        card.add(value);

        card.putClientProperty("valueLabel", value);

        return card;
    }

    // COURSE MANAGEMENT PANEL
    private JPanel createCoursePanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(229, 231, 235)
                ),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel heading = new JLabel("Course Records");
        heading.setFont(new Font("SansSerif", Font.BOLD, 17));
        heading.setForeground(TEXT_COLOR);

        // Search and file action buttons.
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);

        searchField = new JTextField(15);
        searchField.setPreferredSize(new Dimension(180, 34));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JButton searchButton = new JButton("Search");
        styleButton(searchButton, ACCENT_COLOR, Color.WHITE);
        searchButton.addActionListener(e -> searchCourse());

        JButton clearButton = new JButton("Clear");
        styleButton(clearButton, new Color(229, 231, 235), TEXT_COLOR);
        clearButton.addActionListener(e -> {
            searchField.setText("");
            courseTable.clearSelection();
        });

        JButton loadButton = new JButton("Load");
        styleButton(loadButton, new Color(229, 231, 235), TEXT_COLOR);
        loadButton.addActionListener(e -> loadCourses());

        JButton saveButton = new JButton("Save");
        styleButton(saveButton, new Color(229, 231, 235), TEXT_COLOR);
        saveButton.addActionListener(e -> saveCourses());

        toolbar.add(searchField);
        toolbar.add(searchButton);
        toolbar.add(clearButton);
        toolbar.add(loadButton);
        toolbar.add(saveButton);

        JPanel top = new JPanel(new BorderLayout(0, 15));
        top.setOpaque(false);
        top.add(heading, BorderLayout.NORTH);
        top.add(toolbar, BorderLayout.CENTER);

        // Course table.
        tableModel = new DefaultTableModel(
                new Object[]{"COURSE CODE", "COURSE TITLE", "UNITS"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(35);
        courseTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        courseTable.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );
        courseTable.setShowGrid(false);
        courseTable.setIntercellSpacing(new Dimension(0, 0));

        courseTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        courseTable.getTableHeader().setBackground(
                new Color(243, 244, 246)
        );

        courseTable.getTableHeader().setForeground(TEXT_COLOR);
        courseTable.getTableHeader().setPreferredSize(
                new Dimension(0, 38)
        );

        courseTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        courseTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        courseTable.getColumnModel().getColumn(2).setPreferredWidth(70);

        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(top, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ADD COURSE
    private void showAddCourseDialog() {

        JTextField codeField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField unitsField = new JTextField();

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 12));

        form.add(new JLabel("Course Code:"));
        form.add(codeField);

        form.add(new JLabel("Course Title:"));
        form.add(titleField);

        form.add(new JLabel("Credit Units:"));
        form.add(unitsField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Add New Course",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            int units = Integer.parseInt(
                    unitsField.getText().trim()
            );

            courseService.addCourse(
                    codeField.getText(),
                    titleField.getText(),
                    units
            );

            refreshDashboard();

            JOptionPane.showMessageDialog(
                    this,
                    "Course added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Credit units must be a whole number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Course",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // SEARCH COURSE
    private void searchCourse() {

        String code = searchField.getText().trim();

        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a course code to search."
            );
            return;
        }

        Course course = courseService.findCourseByCode(code);

        if (course == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Course not found.",
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // Select the matching course in the table.
        for (int i = 0; i < tableModel.getRowCount(); i++) {

            if (tableModel.getValueAt(i, 0)
                    .toString()
                    .equalsIgnoreCase(course.getCourseCode())) {

                courseTable.setRowSelectionInterval(i, i);

                courseTable.scrollRectToVisible(
                        courseTable.getCellRect(i, 0, true)
                );

                break;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Course found!\n\n"
                + "Code: " + course.getCourseCode() + "\n"
                + "Title: " + course.getCourseTitle() + "\n"
                + "Units: " + course.getUnits(),
                "Search Result",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // SAVE COURSES
    private void saveCourses() {

        try {

            courseService.saveCoursesToFile();

            JOptionPane.showMessageDialog(
                    this,
                    "Courses saved successfully to data/courses.txt",
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not save courses: " + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // LOAD COURSES
    private void loadCourses() {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Loading will replace the current course list.\n"
                + "Continue?",
                "Confirm Load",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            int loaded = courseService.loadCoursesFromFile();

            refreshDashboard();

            JOptionPane.showMessageDialog(
                    this,
                    loaded + " course(s) loaded successfully.",
                    "Load Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load courses: " + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // REFRESH TABLE AND DASHBOARD
    private void refreshDashboard() {

        tableModel.setRowCount(0);

        for (Course course : courseService.getAllCourses()) {

            tableModel.addRow(new Object[]{
                    course.getCourseCode(),
                    course.getCourseTitle(),
                    course.getUnits()
            });
        }

        totalCoursesLabel.setText(
                String.valueOf(courseService.getAllCourses().size())
        );

        totalUnitsLabel.setText(
                String.valueOf(courseService.getTotalUnits())
        );

        recursiveCountLabel.setText(
                String.valueOf(courseService.countCoursesRecursively())
        );
    }

    // BUTTON STYLING
    private void styleButton(
            JButton button, Color background, Color foreground) {

        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(
                BorderFactory.createEmptyBorder(9, 14, 9, 14)
        );
    }
}
