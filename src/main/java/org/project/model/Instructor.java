package org.project;
import java.util.ArrayList;
import java.util.List;

public class Instructor extends User {
    ArrayList<Course> courses;

    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "Instructor");
        courses = new ArrayList<>();
    }

}