package org.ui.admin;

import org.model.Course;
import org.service.AdminService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;   // <-- ADD THIS
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


/**
 * Ultra-modern pending courses panel using glass style + soft white buttons.
 */
public class PendingCoursesPanel extends JPanel {

    private final MainAppWindow parent;
    private final AdminService adminService;

    private JTable table;
    private DefaultTableModel model;

    public PendingCoursesPanel(MainAppWindow parent, AdminService adminService) {
        this.parent = parent;
        this.adminService = adminService;

        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ============================
        //         TOP BAR
        // ============================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(0, 0, 20, 0));

        ModernWhiteButton backBtn = new ModernWhiteButton("⬅  Back");
        backBtn.setPreferredSize(new Dimension(120, 42));
        backBtn.addActionListener(e -> parent.showDashboard());
        topBar.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Pending Courses");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        topBar.add(title, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);

        // ============================
        //     GLASS WRAPPED TABLE
        // ============================
        GlassPanel glass = new GlassPanel(22);
        glass.setLayout(new BorderLayout());
        glass.setBorder(new EmptyBorder(20, 20, 20, 20));

        setupTable();
        loadPendingCourses();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        glass.add(scroll, BorderLayout.CENTER);

        add(glass, BorderLayout.CENTER);
    }

    // ============================
    //        TABLE DESIGN
    // ============================
    private void setupTable() {
        model = new DefaultTableModel(
                new Object[]{"ID", "Title", "Instructor", "", ""}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 3 || col == 4; // only Approve/Reject buttons
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        table.setOpaque(false);
        table.setFillsViewportHeight(true);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(40, 40, 48));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setForeground(Color.WHITE);
        table.setBackground(new Color(25, 25, 30));
        table.setSelectionBackground(new Color(70, 70, 90));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false);

        // APPROVE button
        table.getColumnModel().getColumn(3).setCellRenderer((t, v, sel, focus, row, col) -> {
            ModernWhiteButton b = new ModernWhiteButton("Approve");
            b.setFont(b.getFont().deriveFont(12f));
            return b;
        });

        // REJECT button
        table.getColumnModel().getColumn(4).setCellRenderer((t, v, sel, focus, row, col) -> {
            ModernWhiteButton b = new ModernWhiteButton("Reject");
            b.setFont(b.getFont().deriveFont(12f));
            return b;
        });

        table.getColumnModel().getColumn(3).setCellEditor(new ActionEditor(true));
        table.getColumnModel().getColumn(4).setCellEditor(new ActionEditor(false));

        // Double click to review
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int id = (int) model.getValueAt(row, 0);
                        Course c = adminService.getCourseById(id);
                        if (c != null) {
                            new CourseReviewDialog(parent, c).setVisible(true);
                            reload();
                        }
                    }
                }
            }
        });
    }

    // ============================
    //   BUTTON CELL EDITORS
    // ============================
    private class ActionEditor extends DefaultCellEditor {

        private final boolean approve;
        private ModernWhiteButton btn;

        public ActionEditor(boolean approve) {
            super(new JCheckBox());
            this.approve = approve;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean selected, int row, int col) {

            btn = new ModernWhiteButton(approve ? "Approve" : "Reject");
            btn.setFont(btn.getFont().deriveFont(12f));

            btn.addActionListener(e -> {
                int id = (int) model.getValueAt(row, 0);

                if (approve) {
                    adminService.approveCourse(id);
                    JOptionPane.showMessageDialog(PendingCoursesPanel.this, "Course Approved!");
                } else {
                    adminService.rejectCourse(id);
                    JOptionPane.showMessageDialog(PendingCoursesPanel.this, "Course Rejected!");
                }

                stopCellEditing();
                reload();
            });

            return btn;
        }

        @Override
        public Object getCellEditorValue() { return ""; }
    }

    // ============================
    //   LOAD / REFRESH TABLE DATA
    // ============================
    private void loadPendingCourses() {
        model.setRowCount(0);
        ArrayList<Course> list = adminService.getPendingCourses();

        for (Course c : list) {
            model.addRow(new Object[]{
                    c.getCourseId(),
                    c.getTitle(),
                    c.getInstructorId(),
                    "Approve",
                    "Reject"
            });
        }
    }

    public void reload() {
        loadPendingCourses();
    }
}
