package org.project;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class AuthenticationManager {
    private JsonDatabaseManager dbManager;
    private User currentUser;

    public AuthenticationManager() {
        this.dbManager = new JsonDatabaseManager();
        this.currentUser = null;
    }


    public boolean signup(String username, String email, String password, String role) {
        if (username == null || username.isEmpty()) {
            System.out.println("Username is required");
            return false;
        }

        if (email == null || email.isEmpty() || !isValidEmail(email)) {
            System.out.println("Valid email is required");
            return false;
        }

        if (password == null || password.length() < 6) {
            System.out.println("Password must be at least 6 characters");
            return false;
        }


        ArrayList<User> users = dbManager.loadUsers();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                System.out.println("Email already exists");
                return false;
            }
        }

        Integer n = new Random().nextInt(10000);
        String userId = String.valueOf(n);
        String passwordHash = PasswordHasher.hashPassword(password);

        User newUser;

        if (role.equals("Student")) {
            newUser = new Student(userId, username, email, passwordHash);
        } else {
            newUser = new Instructor(userId, username, email, passwordHash);
        }


        users.add(newUser);
        dbManager.saveUsers(users);
        System.out.println("User created successfully");
        return true;
    }


    public User login(String email, String password) {
        ArrayList<User> users = dbManager.loadUsers();

        for (User user : users) {
            if (user.getEmail().equals(email)) {
                if (PasswordHasher.checkPassword(password, user.getPasswordHash())) {
                    currentUser = user;
                    System.out.println("Login successful");
                    return user;
                } else {
                    System.out.println("Incorrect password");
                    return null;
                }
            }
        }

        System.out.println("User not found");
        return null;
    }


    public void logout() {
        currentUser = null;
        System.out.println("Logged out successfully");
    }


    public User getCurrentUser() {
        return currentUser;
    }


    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}