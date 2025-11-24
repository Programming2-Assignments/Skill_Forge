package org.project;


import org.project.model.Course;
import org.project.storage.CourseJsonDb;

import java.util.ArrayList;

public class AdminService {

    private final CourseJsonDb courseDb = new CourseJsonDb();

    // ----- PENDING -----
    public ArrayList<Course> getPendingCourses() {
        return courseDb.getPendingCourses();
    }

    // ----- APPROVED -----
    public ArrayList<Course> getApprovedCourses() {
        return courseDb.getApprovedCourses();
    }

    // ----- REJECTED -----
    public ArrayList<Course> getRejectedCourses() {
        return courseDb.getRejectedCourses();
    }

    // ----- ACTIONS -----
    public void approveCourse(int courseId) {
        courseDb.approveCourse(courseId);
    }

    public void rejectCourse(int courseId) {
        courseDb.rejectCourse(courseId);
    }

    // ----- UTIL -----
    public Course getCourseById(int courseId) {
        return courseDb.getCourseById(courseId);
    }
}
