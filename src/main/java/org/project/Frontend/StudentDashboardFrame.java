package org.project.Frontend;

import org.project.model.Course;
import org.project.model.Lesson;
import org.project.model.Student;
import org.project.storage.CourseJsonDb;
import org.project.storage.JsonDatabaseManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class StudentDashboardFrame extends JFrame {
    private JsonDatabaseManager db;
    private CourseJsonDb db2;
    private Student student;

    private JPanel mainPanel;
    private JTable coursesTable;
    private JTable lessonsTable;
    private JTextArea lessonContentArea;

    private final String[] COURSE_COLUMNS = {"CourseID", "Title", "InstructorID", "Enrollment", "Progress"};
    private final String[] LESSON_COLUMNS = {"LessonID", "Title"};

    private int selectedCourseId = -1;
    private int selectedLessonId = -1;

    public StudentDashboardFrame(Student student) {
        this.student = student;
        db = new JsonDatabaseManager();
        db2 = new CourseJsonDb();
        setupUI();
    }

    private void setupUI() {
        setTitle("Student Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Border border = BorderFactory.createLineBorder(new Color(35, 65, 204), 2, true);

        // Main layout
        JPanel panel = new JPanel(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Student Dashboard – Welcome, " + student.getUsername());
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.add(lblTitle, BorderLayout.WEST);
        header.setBackground(new Color(30, 80, 160));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> handleLogout());
        header.add(logoutBtn, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        // SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(10, 1, 5, 5));
        sidebar.setPreferredSize(new Dimension(200, 500));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        sidebar.setBackground(new Color(235, 235, 235));

        JButton viewCoursesBtn = new JButton("View Courses");
        JButton viewLessonsBtn = new JButton("View Lessons");
        sidebar.add(viewCoursesBtn);
        sidebar.add(viewLessonsBtn);
        panel.add(sidebar, BorderLayout.WEST);

        // MAIN PANEL
        mainPanel = new JPanel(new CardLayout());
        panel.add(mainPanel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);

        // Setup views
        setupCoursesView();
        setupLessonsView();

        // Sidebar actions
        viewCoursesBtn.addActionListener(e -> switchView("courses"));
        viewLessonsBtn.addActionListener(e -> {
            if (selectedCourseId != -1) {
                loadLessonsForCourse(selectedCourseId);
                switchView("lessons");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a course from Courses view first.");
            }
        });

        switchView("courses");
    }

    private void setupCoursesView() {
        JPanel coursesPanel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(COURSE_COLUMNS, 0);
        coursesTable = new JTable(model);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadCourses(model);

        coursesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = coursesTable.getSelectedRow();
                if (row == -1) return;

                selectedCourseId = (int) coursesTable.getValueAt(row, 0);
            }
        });

        coursesPanel.add(new JScrollPane(coursesTable), BorderLayout.CENTER);

        JButton enrollBtn = new JButton("Enroll in Selected Course");
        enrollBtn.addActionListener(e -> enrollSelectedCourse());
        coursesPanel.add(enrollBtn, BorderLayout.SOUTH);

        mainPanel.add(coursesPanel, "courses");
    }

    private void setupLessonsView() {
        JPanel lessonsPanel = new JPanel(new BorderLayout());

        // Table of lessons
        DefaultTableModel lessonModel = new DefaultTableModel(LESSON_COLUMNS, 0);
        lessonsTable = new JTable(lessonModel);
        lessonsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lessonsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = lessonsTable.getSelectedRow();
                if (row == -1) return;

                selectedLessonId = (int) lessonsTable.getValueAt(row, 0);
                Course c = db2.getCourseById(selectedCourseId);
                if (c != null) {
                    Lesson l = c.getLessonById(selectedLessonId);
                    if (l != null) lessonContentArea.setText(l.getContent());
                }
            }
        });

        lessonsPanel.add(new JScrollPane(lessonsTable), BorderLayout.WEST);

        // Lesson content
        lessonContentArea = new JTextArea();
        lessonContentArea.setLineWrap(true);
        lessonContentArea.setWrapStyleWord(true);
        lessonContentArea.setEditable(false);
        lessonsPanel.add(new JScrollPane(lessonContentArea), BorderLayout.CENTER);

        JButton markCompletedBtn = new JButton("Mark Lesson Completed");
        markCompletedBtn.addActionListener(e -> markLessonCompleted());
        lessonsPanel.add(markCompletedBtn, BorderLayout.SOUTH);

        mainPanel.add(lessonsPanel, "lessons");
    }

    private void switchView(String viewName) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, viewName);
    }

    private void loadCourses(DefaultTableModel model) {
        model.setRowCount(0);
        ArrayList<Course> allCourses = db2.getAllCourses();

        for (Course c : allCourses) {
            boolean enrolled = student.getEnrolledCourses().contains(c.getCourseId());
            int progress = student.calculateProgress(c.getCourseId());

            model.addRow(new Object[]{
                    c.getCourseId(),
                    c.getTitle(),
                    c.getInstructorId(),
                    enrolled ? "Enrolled" : "Not Enrolled",
                    progress + "%"
            });
        }
    }

    private void loadLessonsForCourse(int courseId) {
        Course course = db2.getCourseById(courseId);
        if (course == null) return;

        // Get lesson IDs from student data (lessonsPerCourse) to ensure only assigned lessons
        ArrayList<Integer> lessonIds = student.getLessonsPerCourse().get(courseId);
        if (lessonIds == null) {
            JOptionPane.showMessageDialog(this, "You are not enrolled in this course.");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) lessonsTable.getModel();
        model.setRowCount(0);

        for (int id : lessonIds) {
            Lesson lesson = course.getLessonById(id);
            if (lesson != null) {
                model.addRow(new Object[]{lesson.getLessonId(), lesson.getTitle()});
            }
        }

        lessonContentArea.setText("");
    }


    private void enrollSelectedCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }

        if (student.getEnrolledCourses().contains(selectedCourseId)) {
            JOptionPane.showMessageDialog(this, "Already enrolled!");
            return;
        }

        boolean addedToCourse = db2.addStudentToCourse(selectedCourseId, Integer.parseInt(student.getUserId()));
        boolean addedToStudent = db.enrollStudentInCourse(student.getUserId(), selectedCourseId);

        if (addedToCourse && addedToStudent) {
            student = (Student) db.findUserByEmail(student.getEmail());

            Course c = db2.getCourseById(selectedCourseId);
            ArrayList<Integer> lessonIds = new ArrayList<>();
            for (Lesson l : c.getLessons()) lessonIds.add(l.getLessonId());
            student.setLessonsForCourse(selectedCourseId, lessonIds);
            db.updateUser(student);

            loadCourses((DefaultTableModel) coursesTable.getModel());
            JOptionPane.showMessageDialog(this, "Successfully enrolled!");
        } else {
            JOptionPane.showMessageDialog(this, "Enrollment failed.");
        }
    }

    private void markLessonCompleted() {
        if (selectedLessonId == -1) {
            JOptionPane.showMessageDialog(this, "Select a lesson first.");
            return;
        }

        student.markLessonCompleted(selectedCourseId, selectedLessonId);
        db.updateUser(student);

        loadCourses((DefaultTableModel) coursesTable.getModel());

        JOptionPane.showMessageDialog(this, "Lesson marked as completed!");
    }


    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            new LoginFrame();
            dispose();
        }
    }
}
