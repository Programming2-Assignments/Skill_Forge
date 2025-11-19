package org.project;import java.util.ArrayList;

public class Instructor extends User {

    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "Instructor");
    }

}