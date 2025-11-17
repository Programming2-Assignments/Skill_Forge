package org.project.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student extends User {
    private ArrayList<Course> enrolledCourses;
    private Map<String, Integer> progress;

    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "Student");
    }

    public Student(String userId, String username, String email, String passwordHash, ArrayList<Course> enrolledCourses, Map<String, Integer> progress) {
        super(userId, username, email, passwordHash, "Student");
        enrolledCourses = new ArrayList<>();
        progress = new HashMap<>();
    }

    public ArrayList<Course> getEnrolledCourses() {
        if (enrolledCourses == null) enrolledCourses = new ArrayList<>();
        return enrolledCourses;
    }

    public Map<String, Integer> getProgress() {
        if (progress == null) progress = new HashMap<>();
        return progress;
    }
    public void setEnrolledCourses(ArrayList<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void setProgress(Map<String, Integer> progress) {
        this.progress = progress;
    }

    public int getProgressForCourse(String courseId) {
        if (progress.containsKey(courseId)) {
            return progress.get(courseId);
        }
        return 0;
    }
}