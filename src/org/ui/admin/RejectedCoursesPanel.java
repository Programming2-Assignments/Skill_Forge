package org.ui.admin;

import org.model.Course;
import org.service.AdminService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class RejectedCoursesPanel extends JPanel {

    private final AdminService adminService;

    public RejectedCoursesPanel(AdminService adminService) {
        this.adminService = adminService;

        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createGlassTable(), BorderLayout.CENTER);
    }

    // ===============================
    //          HEADER
    // ===============================
    private Component createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Rejected Courses");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        header.add(title, BorderLayout.WEST);
        return header;
    }

    // ===============================
    //       GLASS PANEL + TABLE
    // ===============================
    private Component createGlassTable() {
        GlassPanel glass = new GlassPanel(22);
        glass.setLayout(new BorderLayout());
        glass.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] cols = {"ID", "Title", "Instructor"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setOpaque(false);
        table.setFillsViewportHeight(true);

        // Table header styling
        JTableHeader header = table.getTableHeader();
        header.setOpaque(true);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(45, 45, 52));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Table body styling
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(25, 25, 30));
        table.setSelectionBackground(new Color(70, 70, 90));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false);

        // Load rejected courses
        ArrayList<Course> list = adminService.getRejectedCourses();
        for (Course c : list) {
            model.addRow(new Object[]{
                    c.getCourseId(),
                    c.getTitle(),
                    c.getInstructorId()
            });
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        glass.add(scroll, BorderLayout.CENTER);
        return glass;
    }
}
