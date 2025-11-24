package org.ui.admin;

import com.formdev.flatlaf.FlatClientProperties;
import org.model.Course;
import org.model.ApprovalStatus;
import org.service.AdminService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CourseReviewDialog extends JDialog {

    public CourseReviewDialog(MainAppWindow parent, Course course) {
        super(parent, "Review Course", true);

        setSize(540, 460);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        // GLASS BACKGROUND CARD
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 20));
        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        card.putClientProperty(FlatClientProperties.STYLE, "arc: 22;"
        );

        // TITLE
        JLabel title = new JLabel("Course Review", SwingConstants.CENTER);
        title.putClientProperty(FlatClientProperties.STYLE, "font: 26;"
                + "foreground: #FFFFFF;"
        );
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        card.add(title, BorderLayout.NORTH);

        // DETAILS SECTION
        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);

        addDetail(details, "Course ID", String.valueOf(course.getCourseId()));
        addDetail(details, "Title", course.getTitle());
        addDetail(details, "Instructor ID", String.valueOf(course.getInstructorId()));
        addDetail(details, "Description", course.getDescription());
        addDetail(details, "Status", course.getStatus().name());

        JScrollPane detailsScroll = new JScrollPane(details);
        detailsScroll.setOpaque(false);
        detailsScroll.getViewport().setOpaque(false);
        detailsScroll.setBorder(null);
        detailsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        card.add(detailsScroll, BorderLayout.CENTER);

        // ACTION BUTTONS
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        buttons.setOpaque(false);

        JButton approveBtn = createActionButton("Approve", "#2ECC71", "#27AE60");
        JButton rejectBtn = createActionButton("Reject", "#E74C3C", "#C0392B");

        approveBtn.addActionListener(e -> {
            new AdminService().approveCourse(course.getCourseId());
            JOptionPane.showMessageDialog(this, "Course Approved!");
            dispose();
        });

        rejectBtn.addActionListener(e -> {
            new AdminService().rejectCourse(course.getCourseId());
            JOptionPane.showMessageDialog(this, "Course Rejected!");
            dispose();
        });

        buttons.add(approveBtn);
        buttons.add(rejectBtn);

        card.add(buttons, BorderLayout.SOUTH);

        add(card);
    }


    // DETAIL ROW BUILDER
    private void addDetail(JPanel parent, String label, String value) {
        JLabel lbl = new JLabel(label + ":");
        lbl.putClientProperty(FlatClientProperties.STYLE, "font: 15;"
                + "foreground: #DDDDDD;"
        );

        JLabel val = new JLabel(value);
        val.putClientProperty(FlatClientProperties.STYLE, "font: 15;"
                + "foreground: #FFFFFF;"
        );

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 8, 0));

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);

        parent.add(row);
    }


    // MODERN BUTTON MAKER
    private JButton createActionButton(String text, String bg, String hover) {
        JButton btn = new JButton(text);

        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 18;"
                + "borderWidth: 0;"
                + "background: " + bg + ";"
                + "hoverBackground: " + hover + ";"
                + "foreground: #FFFFFF;"
                + "font: 14;"
        );

        btn.setBorder(new EmptyBorder(10, 25, 10, 25));
        return btn;
    }
}
