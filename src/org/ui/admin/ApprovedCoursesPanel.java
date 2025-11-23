package org.ui.admin;

import com.formdev.flatlaf.FlatClientProperties;
import org.model.Course;
import org.service.AdminService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ApprovedCoursesPanel extends JPanel {

    private final AdminService adminService;

    public ApprovedCoursesPanel(AdminService adminService) {
        this.adminService = adminService;

        setLayout(new BorderLayout());
        setOpaque(false);

        add(createHeader(), BorderLayout.NORTH);
        add(createTableCard(), BorderLayout.CENTER);
    }

    // -------------------------
    // HEADER TITLE
    // -------------------------
    private Component createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 25, 10, 25));

        JLabel title = new JLabel("Approved Courses");
        title.putClientProperty(FlatClientProperties.STYLE, ""
                + "font: 28pt;"
                + "foreground: white;"
        );

        header.add(title, BorderLayout.WEST);
        return header;
    }

    // -------------------------
    // MAIN TABLE IN GLASS CARD
    // -------------------------
    private Component createTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 22));   // semi-transparent white glass
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        card.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 18;"
        );

        // Table model
        String[] cols = {"ID", "Title", "Instructor"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        JTable table = new JTable(model);
        table.setRowHeight(36);

        table.putClientProperty(FlatClientProperties.STYLE, ""
                + "font: 14pt;"
                + "foreground: white;"
                + "background: #1F1F1F;"
                + "selectionBackground: #3A3A3A;"
                + "selectionForeground: white;"
                + "cellPadding: 8,8,8,8;"
        );

        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);

        // Load approved courses
        ArrayList<Course> list = adminService.getApprovedCourses();
        for (Course c : list) {
            model.addRow(new Object[]{
                    c.getCourseId(),
                    c.getTitle(),
                    c.getInstructorId()
            });
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(31, 31, 31));

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }
}
