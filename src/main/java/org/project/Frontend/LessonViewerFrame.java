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

public class LessonViewerFrame extends JFrame {

    private JsonDatabaseManager db;
    private CourseJsonDb db2;
    private Course course;
    private Student student;

    private JButton logoutButton;
    private JButton checkLessonButton;
    private JTable lessonTable;
    private JTextArea LessonContent;

    private final String[] COLUMN_NAMES = {"LessonID", "Title"};

    private int selectedLessonId = -1;

    public LessonViewerFrame(Course course, Student student) {
        this.course = course;
        this.student = student;
        this.db = new JsonDatabaseManager();
        this.db2 = new CourseJsonDb();
        ui();
        loadLessonsIntoTable();
        addTableClickEvent();
    }

    private void ui() {
        setTitle("Lessons view");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Border border = BorderFactory.createLineBorder(new Color(35, 65, 204), 2, true);

        JPanel panel = new JPanel(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        JLabel label1 = new JLabel("Lesson Dashboard");
        label1.setFont(new Font("Arial", Font.BOLD, 23));
        label1.setHorizontalAlignment(JLabel.CENTER);
        header.add(label1);
        header.setBorder(border);
        panel.add(header, BorderLayout.NORTH);

        // CENTER PANEL
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // TABLE
        DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0);
        lessonTable = new JTable(model);
        lessonTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(lessonTable);
        centerPanel.add(tableScroll);

        // RIGHT PANEL (Lesson content)
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lbl = new JLabel("Lesson Content:");
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(lbl, BorderLayout.NORTH);

        LessonContent = new JTextArea();
        LessonContent.setLineWrap(true);
        LessonContent.setWrapStyleWord(true);
        LessonContent.setFont(new Font("Arial", Font.PLAIN, 14));
        LessonContent.setEditable(false);
        LessonContent.setBorder(border);

        JScrollPane contentScroll = new JScrollPane(LessonContent);
        infoPanel.add(contentScroll, BorderLayout.CENTER);

        centerPanel.add(infoPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        // BUTTON PANEL
        logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        logoutButton.addActionListener(e -> handleLogout());

        checkLessonButton = new JButton("Check Lesson");
        styleButton(checkLessonButton);
        checkLessonButton.addActionListener(e -> checkLesson());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(logoutButton);
        buttonPanel.add(checkLessonButton);
        buttonPanel.setBorder(border);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);

        setVisible(true);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(35, 65, 204));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
    }

    private void loadLessonsIntoTable() {
        DefaultTableModel model = (DefaultTableModel) lessonTable.getModel();
        model.setRowCount(0);

        for (Lesson l : course.getLessons()) {
            model.addRow(new Object[]{
                    l.getLessonId(),
                    l.getTitle()
            });
        }
    }

    private void addTableClickEvent() {
        lessonTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = lessonTable.getSelectedRow();
                if (row == -1) return;

                selectedLessonId = (int) lessonTable.getValueAt(row, 0);

                Lesson lesson = course.getLessonById(selectedLessonId);
                if (lesson != null) {
                    LessonContent.setText(lesson.getContent());
                }
            }
        });
    }

    private void checkLesson() {
        if (selectedLessonId == -1) {
            JOptionPane.showMessageDialog(this, "Select a lesson first.");
            return;
        }

        student.markLessonCompleted(course.getCourseId(), selectedLessonId);
        db.updateUser(student);

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
