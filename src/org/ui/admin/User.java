package org.model;

public abstract class User {

    private final String userId;
    private String username;
    private String email;
    private String passwordHash;
    private UserRole role;

    public User(String userId, String username, String email, String passwordHash, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public UserRole getRole() { return role; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(UserRole role) { this.role = role; }

    // Helper: check if user is admin
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }
}
