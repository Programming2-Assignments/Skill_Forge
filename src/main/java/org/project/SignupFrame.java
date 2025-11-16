import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SignupFrame extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private JButton signupButton;
    private JButton backButton;
    private AuthenticationManager authManager;

    public SignupFrame() {
        authManager = new AuthenticationManager();
        UI();
    }

    private void UI() {
        setTitle("Skill Forge - Sign Up");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 242, 245));

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(new EmptyBorder(40, 60, 50, 60));

        // Logo/Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(240, 242, 245));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Skill Forge");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(35, 65, 204), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        // Username Field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        usernameLabel.setForeground(new Color(51, 51, 51));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameSubtitle = new JLabel("Choose a unique username");
        usernameSubtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        usernameSubtitle.setForeground(new Color(120, 120, 120));
        usernameSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(35, 65, 204), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        // Email Field
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

        // Password Field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passwordLabel.setForeground(new Color(51, 51, 51));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordSubtitle = new JLabel("Must be 6 characters or more");
        passwordSubtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        passwordSubtitle.setForeground(new Color(120, 120, 120));
        passwordSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(35, 65, 204), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        // Confirm Password Field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 13));
        confirmPasswordLabel.setForeground(new Color(51, 51, 51));
        confirmPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel confirmPasswordSubtitle = new JLabel("Re-enter your password");
        confirmPasswordSubtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        confirmPasswordSubtitle.setForeground(new Color(120, 120, 120));
        confirmPasswordSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(300, 40));
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(35, 65, 204), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        // Role Field
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        roleLabel.setForeground(new Color(51, 51, 51));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);



        String[] roles = {"Student", "Instructor"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        roleComboBox.setPreferredSize(new Dimension(300, 40));
        roleComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        roleComboBox.setBackground(Color.WHITE);
        roleComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(35, 65, 204), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        // Signup Button
        signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Arial", Font.BOLD, 15));
        signupButton.setForeground(Color.WHITE);
        signupButton.setBackground(new Color(35, 65, 204));
        signupButton.setFocusPainted(false);
        signupButton.setBorderPainted(false);
        signupButton.setPreferredSize(new Dimension(300, 45));
        signupButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.addActionListener(e -> handleSignup());

        // Back to login link panel
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(Color.WHITE);

        JLabel loginLabel = new JLabel("Already have an account?");
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        loginLabel.setForeground(new Color(120, 120, 120));

        backButton = new JButton("Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setForeground(new Color(35, 65, 204));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> backToLogin());

        loginPanel.add(loginLabel);
        loginPanel.add(backButton);

        // Add components to form panel
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameSubtitle);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordSubtitle);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(confirmPasswordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(confirmPasswordSubtitle);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(roleLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(roleComboBox);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(signupButton);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(loginPanel);

        // Add to main panel
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createVerticalStrut(40));
        mainPanel.add(formPanel);

        add(mainPanel);
        setVisible(true);
    }

    private void handleSignup() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 6 characters",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = authManager.signup(username, email, password, role);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Account created successfully!\nYou can now login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            backToLogin();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Signup failed. Email may already exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToLogin() {
        new LoginFrame();
        dispose();
    }
}