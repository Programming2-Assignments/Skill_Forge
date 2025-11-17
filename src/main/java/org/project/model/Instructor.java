package org.project.model;

import java.util.ArrayList;

public class Instructor extends User {
    ArrayList<Course> courses;

    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "Instructor");
        courses = new ArrayList<>();
    }

    public void addCourse(Course course){
        courses.add(course);
    }

    public void removeCourse(Course course){
        courses.remove(course);
    }
}