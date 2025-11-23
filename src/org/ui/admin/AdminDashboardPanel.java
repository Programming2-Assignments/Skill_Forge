package org.ui.admin;

import com.formdev.flatlaf.FlatClientProperties;
import org.service.AdminService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminDashboardPanel extends JPanel {

    private final MainAppWindow parent;
    private final AdminService adminService;

    public AdminDashboardPanel(MainAppWindow parent, AdminService adminService) {
        this.parent = parent;
        this.adminService = adminService;

        setLayout(new BorderLayout());
        setOpaque(false);

        // ---------- TITLE ----------
        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));   // FIXED FONT
        title.setForeground(Color.WHITE);

        JPanel titleWrapper = new JPanel(new BorderLayout());
        titleWrapper.setOpaque(false);
        titleWrapper.setBorder(new EmptyBorder(20, 30, 20, 30));
        titleWrapper.add(title, BorderLayout.WEST);

        add(titleWrapper, BorderLayout.NORTH);

        // ---------- CENTER ----------
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(10, 10, 40, 10));
        add(center, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 20, 0);

        JButton pendingBtn  = createGlassButton("⏳   Pending Courses");
        JButton approvedBtn = createGlassButton("✔   Approved Courses");
        JButton rejectedBtn = createGlassButton("✖   Rejected Courses");

        pendingBtn.addActionListener(e -> parent.showPendingCourses());
        approvedBtn.addActionListener(e -> parent.showApprovedCourses());
        rejectedBtn.addActionListener(e -> parent.showRejectedCourses());

        center.add(pendingBtn, gbc);
        gbc.gridy++;
        center.add(approvedBtn, gbc);
        gbc.gridy++;
        center.add(rejectedBtn, gbc);
    }

    private JButton createGlassButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(300, 55));
        btn.setFocusPainted(false);

        btn.putClientProperty(FlatClientProperties.STYLE, ""
                + "background: #FFFFFF;"
                + "foreground: #000000;"
                + "hoverBackground: #EDEDED;"
                + "pressedBackground: #DCDCDC;"
                + "arc: 18;"
                + "borderWidth: 0;"
        );

        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));

        return btn;
    }

    // ⭐ IMPORTANT FIX: guarantees buttons never disappear
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(900, 650);
    }
}
