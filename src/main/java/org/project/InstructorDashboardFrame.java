import javax.swing.*;
import java.awt.*;

public class InstructorDashboardFrame extends JFrame {
    private Instructor instructor;
    private JButton logoutButton;

    public InstructorDashboardFrame(Instructor instructor) {
        this.instructor = instructor;
        setupUI();
    }

    private void setupUI() {
        setTitle("Instructor Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());


        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.addActionListener(e -> handleLogout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

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
}