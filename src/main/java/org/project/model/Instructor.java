package org.project.model;

import java.util.ArrayList;

public class Instructor extends User {
    ArrayList<Integer> coursesID;

    public Instructor(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "Instructor");
        coursesID = new ArrayList<>();
    }

    public void addCourse(Integer courseid){
        Integer cID = courseid;
        coursesID.add(cID);
    }

    public void removeCourse(Integer courseid){
        Integer cID = courseid;
        coursesID.remove(cID);
    }
}