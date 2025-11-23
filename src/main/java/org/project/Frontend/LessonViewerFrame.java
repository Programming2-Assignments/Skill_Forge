package org.project.Frontend;

import org.project.model.Course;
import org.project.model.Lesson;
import org.project.model.Student;
import org.project.storage.CourseJsonDb;
import org.project.storage.JsonDatabaseManager;
import org.project.storage.QuizManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LessonViewerFrame extends JFrame {

    private JsonDatabaseManager db;
    private CourseJsonDb db2;
    private Course course;
    private Student student;
    private JButton startQuizButton;
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


        JPanel header = new JPanel(new BorderLayout());
        JLabel label1 = new JLabel("Lesson Dashboard");
        label1.setFont(new Font("Arial", Font.BOLD, 23));
        label1.setHorizontalAlignment(JLabel.CENTER);
        header.add(label1);
        header.setBorder(border);
        panel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));


        DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0);
        lessonTable = new JTable(model);
        lessonTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(lessonTable);
        centerPanel.add(tableScroll);

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


        logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        logoutButton.addActionListener(e -> handleLogout());

        checkLessonButton = new JButton("Check Lesson");
        styleButton(checkLessonButton);
        checkLessonButton.addActionListener(e -> checkLesson());

        startQuizButton = new JButton("Start Quiz");
        styleButton(startQuizButton);
        startQuizButton.addActionListener(e -> startQuizForSelectedLesson());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        buttonPanel.add(logoutButton);
        buttonPanel.add(checkLessonButton);
        buttonPanel.add(startQuizButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(35, 65, 204));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
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

        loadLessonsIntoTable();
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

    private void startQuizForSelectedLesson() {
        if (selectedLessonId == -1) {
            JOptionPane.showMessageDialog(this, "Select a lesson first.");
            return;
        }
        Lesson lesson = course.getLessonById(selectedLessonId);
        if (lesson == null || lesson.getQuiz() == null) {
            JOptionPane.showMessageDialog(this, "No quiz for this lesson.");
            return;
        }

        String currentStudentId = student.getUserId();

        QuizManager qm = new QuizManager();
        int attempts = qm.countAttemptsForStudentQuiz(currentStudentId, lesson.getQuiz().getQuizId());
        int max = lesson.getQuiz().getMaxAttempts();
        if (max > -1 && attempts >= max) {
            JOptionPane.showMessageDialog(this, "You reached max attempts for this quiz.");
            return;
        }

        QuizFrame qf = new QuizFrame(lesson.getQuiz(), student.getUserId());
        qf.setVisible(true);

        qf.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                List<org.project.model.QuizAttempt> attemptsList = qm.getAttemptsForStudentAndQuiz(student.getUserId(), lesson.getQuiz().getQuizId());
                if (!attemptsList.isEmpty()) {
                    org.project.model.QuizAttempt last = attemptsList.get(attemptsList.size()-1);
                    if (last.isPassed()) {
                        student.markLessonCompleted(course.getCourseId(), selectedLessonId);
                        db.updateUser(student);
                        loadLessonsIntoTable();
                        JOptionPane.showMessageDialog(null, "Lesson marked as completed (passed quiz).");
                    }
                }
            }
        });
    }
}
