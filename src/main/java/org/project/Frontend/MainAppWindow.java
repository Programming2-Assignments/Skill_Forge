package org.project.Frontend;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatClientProperties;
import org.project.AdminService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainAppWindow extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private final AdminService adminService = new AdminService();

    public MainAppWindow() {
        setTitle("SkillForge Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Background of whole frame (dark gradient)
        getContentPane().setBackground(new Color(10, 10, 14));

        // ========== SIDEBAR ==========
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);
        sidebar.setBorder(new EmptyBorder(20, 18, 20, 18));
        sidebar.setPreferredSize(new Dimension(230, getHeight()));

        sidebar.putClientProperty(FlatClientProperties.STYLE, ""
                + "background: rgba(20,20,25,180);"
                + "arc: 18;"
        );

        JLabel appTitle = new JLabel("SkillForge Admin");
        appTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font: 19pt;"
                + "foreground: white;"
        );
        appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        appTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        sidebar.add(appTitle);

        sidebar.add(new JSeparator());
        sidebar.add(Box.createVerticalStrut(18));

        JButton dashboardBtn = createSidebarButton("🏠  Dashboard");
        JButton pendingBtn   = createSidebarButton("⏳  Pending Courses");
        JButton approvedBtn  = createSidebarButton("✔  Approved Courses");
        JButton rejectedBtn  = createSidebarButton("✖  Rejected Courses");
        JButton logoutBtn    = createSidebarButton("🚪  Logout");

        dashboardBtn.addActionListener(e -> showDashboard());
        pendingBtn.addActionListener(e -> showPendingCourses());
        approvedBtn.addActionListener(e -> showApprovedCourses());
        rejectedBtn.addActionListener(e -> showRejectedCourses());

        logoutBtn.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );
            if (res == JOptionPane.YES_OPTION)
                dispose();
        });

        sidebar.add(dashboardBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(pendingBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(approvedBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(rejectedBtn);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);

        // ========== CONTENT AREA ==========
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Wrap content inside a modern GlassPanel
        GlassPanel glassBackground = new GlassPanel(24);
        glassBackground.setLayout(new BorderLayout());
        glassBackground.setBorder(new EmptyBorder(16, 16, 16, 16));
        glassBackground.setOpaque(false);

        // ⭐ FIX: prevent layout collapse (dashboard buttons disappearing)
        glassBackground.setPreferredSize(new Dimension(900, 650));

        glassBackground.add(contentPanel, BorderLayout.CENTER);

        // Add all panels
        contentPanel.add(new AdminDashboardPanel(this, adminService), "dashboard");
        contentPanel.add(new PendingCoursesPanel(this, adminService), "pending");
        contentPanel.add(new ApprovedCoursesPanel(adminService), "approved");
        contentPanel.add(new RejectedCoursesPanel(adminService), "rejected");

        add(glassBackground, BorderLayout.CENTER);

        showDashboard();
    }

    // Sidebar button template
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        btn.putClientProperty(FlatClientProperties.STYLE, ""
                + "background: #1E1E1E;"
                + "hoverBackground: #2D2D2D;"
                + "foreground: white;"
                + "arc: 18;"
                + "borderWidth: 0;"
                + "font: 14pt;"
        );

        btn.setBorder(new EmptyBorder(6, 16, 6, 16));
        return btn;
    }

    // Navigation
    public void showDashboard() { cardLayout.show(contentPanel, "dashboard"); }
    public void showPendingCourses() { cardLayout.show(contentPanel, "pending"); }
    public void showApprovedCourses() { cardLayout.show(contentPanel, "approved"); }
    public void showRejectedCourses() { cardLayout.show(contentPanel, "rejected"); }

    // Main
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            MainAppWindow window = new MainAppWindow();
            window.setVisible(true);
        });
    }
}
