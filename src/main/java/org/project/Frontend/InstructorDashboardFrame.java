package org.project.Frontend;

import org.project.model.Course;
import org.project.model.Instructor;
import org.project.service.InstructorRole;
import org.project.model.Lesson;
import org.project.model.Student;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class InstructorDashboardFrame extends JFrame{
    private JPanel mainPanel;
    private final Instructor instructor;
    private final InstructorRole instructorService;

    public InstructorDashboardFrame(Instructor instructor) {
        this.instructor = instructor;
        this.instructorService = new InstructorRole();
        setupUI();
    }

    private void setupUI() {
        setTitle("Instructor Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addHeader();
        addSidebar();
        addMainPanel();
        setVisible(true);
    }

    private void addHeader() {
        JPanel displayNamePanel = new JPanel(new BorderLayout());
        displayNamePanel.setBackground(new Color(30, 80, 160));
        displayNamePanel.setPreferredSize(new Dimension(100, 60));

        JLabel title = new JLabel("Instructor Dashboard – Welcome, " + instructor.getUsername());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> handleLogout());

        displayNamePanel.add(title, BorderLayout.WEST);
        displayNamePanel.add(logoutBtn, BorderLayout.EAST);

        add(displayNamePanel, BorderLayout.NORTH);
    }


    private void addSidebar() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(200, 500));
        buttonPanel.setBackground(new Color(235, 235, 235));
        buttonPanel.setLayout(new GridLayout(10, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JButton createCourseButton = new JButton("Create Course");
        JButton manageCourseButton = new JButton("Manage My Courses");
        JButton viewEnrolledStudentsButton = new JButton("View Enrolled Students");

        createCourseButton.addActionListener(e -> openCreateCourseView());
        manageCourseButton.addActionListener(e -> openManageCoursesView());
        viewEnrolledStudentsButton.addActionListener(e -> openViewStudentsView());

        buttonPanel.add(createCourseButton);
        buttonPanel.add(manageCourseButton);
        buttonPanel.add(viewEnrolledStudentsButton);

        add(buttonPanel, BorderLayout.WEST);
    }

    private void addMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        add(mainPanel, BorderLayout.CENTER);
        openCreateCourseView();
    }

    private void setMainPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }



    private void openCreateCourseView() {

        JPanel panel = new JPanel(null);

        JLabel lblTitle = new JLabel("Create Course");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBounds(20, 10, 300, 40);

        JLabel lblCourseTitle = new JLabel("Course Title:");
        lblCourseTitle.setBounds(50, 80, 120, 25);

        JTextField txtCourseTitle = new JTextField();
        txtCourseTitle.setBounds(180, 80, 350, 30);

        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setBounds(50, 140, 120, 25);

        JTextArea txtDescription = new JTextArea();
        txtDescription.setBounds(180, 140, 350, 120);

        JButton btnCreate = new JButton("Create");
        btnCreate.setBounds(180, 300, 120, 35);

        JLabel lblMessage = new JLabel();
        lblMessage.setBounds(180, 350, 400, 30);

        btnCreate.addActionListener(e -> {
            String title = txtCourseTitle.getText().trim();
            String desc = txtDescription.getText().trim();

            if (title.isEmpty()) {
                lblMessage.setForeground(Color.RED);
                lblMessage.setText("Title cannot be empty.");
                return;
            }

            instructorService.createCourse(instructor,title, desc);

            lblMessage.setForeground(new Color(0, 140, 0));
            lblMessage.setText("✔ Course created successfully!");
        });

        panel.add(lblTitle);
        panel.add(lblCourseTitle);
        panel.add(txtCourseTitle);
        panel.add(lblDescription);
        panel.add(txtDescription);
        panel.add(btnCreate);
        panel.add(lblMessage);

        setMainPanel(panel);
    }


    private void openManageCoursesView() {
        JPanel panel = new JPanel(new BorderLayout());



        List<Course> courses = instructorService.getCdb().loadCourses().stream()
                .filter(c -> c.getInstructorId()==Integer.parseInt(instructor.getUserId()))
                .collect(Collectors.toList());

        String[] cols = {"Course ID", "Title", "Description"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Course c : courses) {
            model.addRow(new Object[]{c.getCourseId(), c.getTitle(), c.getDescription()});
        }

        JTable table = new JTable(model);

//

        table.addMouseListener(new MouseAdapter() {
                                   @Override
                                   public void mouseClicked(MouseEvent e){
                                       int  row = table.rowAtPoint(e.getPoint());
                                       if (row == -1) return;

                                       String courseId = table.getValueAt(row,0).toString();

                                       if(SwingUtilities.isLeftMouseButton(e)){
                                           openManageLessonsView(courseId);
                                       } else if (SwingUtilities.isRightMouseButton(e)) {
                                           int option = JOptionPane.showOptionDialog(
                                                   table,
                                                   "CHOOSE AN ACTION FOT THIS COURSE",
                                                   "Course Options",
                                                   JOptionPane.YES_NO_CANCEL_OPTION,
                                                   JOptionPane.PLAIN_MESSAGE,
                                                   null,
                                                   new Object[]{"Edit", "Delete", "Cancel"},
                                                   "Edit"
                                           );

                                           if (option == 0) { // Edit
                                               String newTitle = JOptionPane.showInputDialog("New Course Title:", table.getValueAt(row, 1));
                                               String newDesc = JOptionPane.showInputDialog("New Course Description:", table.getValueAt(row, 2));
                                               if (newTitle != null && !newTitle.trim().isEmpty()) {
                                                   instructorService.editCourse(Integer.parseInt(courseId), newTitle,newDesc);
                                                   openManageCoursesView();
                                               }
                                           }
                                           else if (option == 1) { // Delete
                                               int confirm = JOptionPane.showConfirmDialog(table, "Are you sure?", "Delete Course", JOptionPane.YES_NO_OPTION);
                                               if (confirm == JOptionPane.YES_OPTION) {
                                                   instructorService.deleteCourse(Integer.parseInt(courseId),instructor);
                                                   openManageCoursesView();
                                               }
                                           }

                                       }
                                   }
                               }
        );


        panel.add(new JScrollPane(table), BorderLayout.CENTER);


        setMainPanel(panel);
    }



    private void openManageLessonsView(String courseId) {
        JPanel panel = new JPanel(new BorderLayout());

        Course course = instructorService.getCdb().loadCourses().stream()
                .filter(c -> c.getCourseId()==Integer.parseInt(courseId))
                .findFirst().orElse(null);

        if (course == null) return;

        JLabel lblTitle = new JLabel("Manage Lessons – " + course.getTitle());
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"Lesson ID", "Title","Content", "Has Quiz?"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Lesson l : course.getLessons()) {
            model.addRow(new Object[]{l.getLessonId(), l.getTitle(),  l.getContent(), (l.getQuiz() != null ? "Yes" : "No")});
        }

        JTable table = new JTable(model);

        JButton btnAddLesson = new JButton("Add New Lesson");
        btnAddLesson.addActionListener(e -> {
            String lessonTitle = JOptionPane.showInputDialog("Lesson Title:");
            String lessonContent = JOptionPane.showInputDialog("Content:");

            if (lessonTitle != null && !lessonTitle.trim().isEmpty()) {
                instructorService.addLesson(courseId, lessonTitle, lessonContent);
                openManageLessonsView(courseId);
            }
        });

        JButton btnAddQuiz = new JButton("Add / Edit Quiz");
        btnAddQuiz.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a lesson first.");
                return;
            }

            int lessonId = (int) table.getValueAt(row, 0);
            AddQuizFrame frame = new AddQuizFrame(Integer.parseInt(courseId), lessonId);
            frame.setVisible(true);
        });

        JButton btnManageLesson = new JButton("Delete / Edit Lesson");
        btnManageLesson.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int option = JOptionPane.showOptionDialog(
                        table,
                        "Do you want to delete or edit this lesson?",
                        "LESSON Options",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        new Object[]{"Edit", "Delete", "Cancel"},
                        "Edit"
                );

                if (option == 0) { // Edit
                    String newTitle = JOptionPane.showInputDialog("New Lesson Title:", table.getValueAt(row, 1));
                    String newCon = JOptionPane.showInputDialog("New Lesson Content:", table.getValueAt(row, 2));
                    if (newTitle != null && !newTitle.trim().isEmpty()) {
                        instructorService.editLesson(Integer.parseInt(courseId),(int) table.getValueAt(row,0), newTitle,newCon);
                        openManageLessonsView(courseId);
                    }
                }
                else if (option == 1) { // Delete
                    int confirm = JOptionPane.showConfirmDialog(table, "Are you sure?", "Delete Course", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        instructorService.deleteLesson(Integer.parseInt(courseId),(int) table.getValueAt(row,0));
                        openManageLessonsView(courseId);
                    }
                }
            }
        });

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.add(btnAddLesson);
        southPanel.add(btnAddQuiz);
        southPanel.add(btnManageLesson);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        setMainPanel(panel);
    }



    private void openViewStudentsView() {

        JPanel panel = new JPanel(new BorderLayout());

        JLabel lblTitle = new JLabel("View Enrolled Students");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        java.util.List<Course> instructorCourses = instructorService.getCdb().loadCourses().stream()
                .filter(c -> c.getInstructorId()==Integer.parseInt(instructor.getUserId()))
                .collect(Collectors.toList());

        String[] courseTitles = instructorCourses.stream().map(Course::getTitle).toArray(String[]::new);

        JComboBox<String> courseDropdown = new JComboBox<>(courseTitles);
        String[] columns = {"Student ID", "Username", "Email"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable studentTable = new JTable(tableModel);

        courseDropdown.addActionListener(e -> {
            int index = courseDropdown.getSelectedIndex();
            if (index == -1) return;

            Course selected = instructorCourses.get(index);
            List<Student> students = instructorService.viewEnrollment(selected.getCourseId());

            tableModel.setRowCount(0);
            for (Student s : students) {
                tableModel.addRow(new Object[]{s.getUserId(), s.getUsername(), s.getEmail()});
            }
        });

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(courseDropdown, BorderLayout.WEST);
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        setMainPanel(panel);
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
