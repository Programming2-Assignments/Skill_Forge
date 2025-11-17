package org.project.Frontend;
import org.project.model.Student;
import org.project.model.Course;
import org.project.storage.CourseJsonDb;
import org.project.storage.JsonDatabaseManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class StudentDashboardFrame extends JFrame {
    private JsonDatabaseManager db;
    private CourseJsonDb db2;
    private Student student;
    private JButton logoutButton;
    private JButton enrollButton;
    private JButton viewlessonButton;
    private JTable coursesTable;
    private JTextField tfCourseId;
    private JTextField tfTitle;
    private JTextField tfInstructor;
    private JTextField tfEnrolled;
    private JTextField tfProgress;
    private final String[] COLUMN_NAMES = {"CourseID", "Title", "instructorId" , "enrollment" , "progress"};

    public StudentDashboardFrame(Student student) {
        this.student = student;
        db2 = new CourseJsonDb();
        setupUI();

    }

    private void setupUI() {
        setTitle("Student Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Border border = BorderFactory.createLineBorder(new Color(35, 65, 204),2,true);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel Header = new JPanel(new BorderLayout());
        JLabel label1 = new JLabel("Student DashBoard");
        label1.setFont(new Font("Arial", Font.BOLD,23));
        label1.setForeground(new Color(51, 51, 51));
        Header.add(label1);
        label1.setHorizontalAlignment(JLabel.CENTER);
        label1.setVerticalAlignment(JLabel.NORTH);
        panel.add(Header, BorderLayout.NORTH);
        Header.setBorder(border);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0);
        coursesTable = new JTable(model);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(coursesTable);
        loadCoursesIntoTable(model);
        addTableClickEvent();

        centerPanel.add(tableScroll);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(10, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tfCourseId = new JTextField();
        tfTitle = new JTextField();
        tfInstructor = new JTextField();
        tfEnrolled = new JTextField();
        tfProgress = new JTextField();

        infoPanel.add(new JLabel("Course ID:"));
        infoPanel.add(tfCourseId);

        infoPanel.add(new JLabel("Title:"));
        infoPanel.add(tfTitle);

        infoPanel.add(new JLabel("Instructor ID:"));
        infoPanel.add(tfInstructor);

        infoPanel.add(new JLabel("Enrollment:"));
        infoPanel.add(tfEnrolled);

        infoPanel.add(new JLabel("Progress:"));
        infoPanel.add(tfProgress);

        centerPanel.add(infoPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.setForeground(new Color(255,255,255));
        logoutButton.addActionListener(e -> handleLogout());
        logoutButton.setBackground(new Color(35, 65, 204));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);

        enrollButton = new JButton("Enroll course");
        enrollButton.setFont(new Font("Arial", Font.PLAIN, 16));
        enrollButton.addActionListener(e -> EnrollCourse());
        enrollButton.setForeground(new Color(255,255,255));
        enrollButton.setBackground(new Color(35, 65, 204));
        enrollButton.setFocusPainted(false);
        enrollButton.setBorderPainted(false);

        viewlessonButton = new JButton(" view lessons");
        viewlessonButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewlessonButton.addActionListener(e ->  viewlesson());
        viewlessonButton.setForeground(new Color(255,255,255));
        viewlessonButton.setBackground(new Color(35, 65, 204));
        viewlessonButton.setFocusPainted(false);
        viewlessonButton.setBorderPainted(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255,255,255));
        buttonPanel.add(logoutButton);
        buttonPanel.add(enrollButton);
        buttonPanel.add(viewlessonButton);
        logoutButton.setHorizontalAlignment(JButton.RIGHT);
        enrollButton.setHorizontalAlignment(JButton.CENTER);
        viewlessonButton.setHorizontalAlignment(JButton.LEFT);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setBorder(border);
        add(panel);
        setVisible(true);
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
    private void addTableClickEvent() {
        coursesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = coursesTable.getSelectedRow();
                if (row == -1) return;

                tfCourseId.setText(coursesTable.getValueAt(row, 0).toString());
                tfTitle.setText(coursesTable.getValueAt(row, 1).toString());
                tfInstructor.setText(coursesTable.getValueAt(row, 2).toString());
                tfEnrolled.setText(coursesTable.getValueAt(row, 3).toString());
                tfProgress.setText(coursesTable.getValueAt(row, 4).toString());
            }
        });
    }
    private void loadCoursesIntoTable(DefaultTableModel model) {
        model.setRowCount(0);

        System.out.println("DEBUG: Loading courses...");
        ArrayList<Course> all = db2.loadCourses();
        System.out.println("DEBUG: Number of courses = " + all.size());

        for (Course c : all) {
            System.out.println("DEBUG: Loaded course: " + c.getCourseId() + " - " + c.getTitle());
        }

        for (Course c : all) {
            boolean enrolled = student.getEnrolledCourses().contains(c.getCourseId());
            int progress;
            progress = student.getProgress().getOrDefault(c.getCourseId(), 0);

            model.addRow(new Object[]{
                    c.getCourseId(),
                    c.getTitle(),
                    c.getInstructorId(),
                    enrolled ? "Enrolled" : "Not Enrolled",
                    progress + "%"
            });
        }
    }


    private void EnrollCourse() {
        DefaultTableModel model = (DefaultTableModel) coursesTable.getModel();

        // Get course ID as String
        int courseId = Integer.parseInt(tfCourseId.getText().trim());
        String enrollment = tfEnrolled.getText().trim();

        // Check if student is already enrolled
        if (!"Enrolled".equals(enrollment)) {
            // Get the course
            Course temp = db2.getCourseById(courseId);

            if (temp != null) {
                int studentId = Integer.parseInt(student.getUserId()); // keep as String
                boolean success = db2.enrollStudent(courseId, studentId); // update db2 method to accept String IDs

                if (success) {
                    loadCoursesIntoTable(model);
                    JOptionPane.showMessageDialog(this, "Successfully enrolled!", "Enrollment Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Enrollment failed!", "Enrollment Message", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Course not found!", "Enrollment Message", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Already enrolled!", "Enrollment Message", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void  viewlesson(){}

}