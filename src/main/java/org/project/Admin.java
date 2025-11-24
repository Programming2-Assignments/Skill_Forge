package org.project;

import org.project.model.User;

public class Admin extends User {

    public Admin(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "Admin");
    }
}
