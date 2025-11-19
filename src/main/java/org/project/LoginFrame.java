package org.project;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private AuthenticationManager authManager;

    public LoginFrame() {
        authManager = new AuthenticationManager();
        UI();
    }

    private void UI() {
        setTitle("Skill Forge - Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(240, 242, 245));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Skill Forge");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(35, 65, 204), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        emailLabel.setForeground(new Color(51, 51, 51));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(300, 40));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(35, 65, 204), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passwordLabel.setForeground(new Color(51, 51, 51));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(35, 65, 204), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(35, 65, 204));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> Login());

        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signupPanel.setBackground(Color.WHITE);

        JLabel signupLabel = new JLabel("Don't have an account?");
        signupLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        signupLabel.setForeground(new Color(120, 120, 120));

        signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Arial", Font.BOLD, 13));
        signupButton.setForeground(new Color(35, 65, 204));
        signupButton.setContentAreaFilled(false);
        signupButton.setBorderPainted(false);
        signupButton.setFocusPainted(false);
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.addActionListener(e -> openSignupFrame());

        signupPanel.add(signupLabel);
        signupPanel.add(signupButton);

        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(signupPanel);

        mainPanel.add(titlePanel);
        mainPanel.add(Box.createVerticalStrut(40));
        mainPanel.add(formPanel);

        add(mainPanel);
        setVisible(true);
    }

    private void Login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = authManager.login(email, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    "Login Successful",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            if (user.getRole().equals("Student")) {
                new StudentDashboardFrame((Student) user);
            } else {
                new InstructorDashboardFrame((Instructor) user);
            }

            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid email or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSignupFrame() {
        new SignupFrame();
        dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
