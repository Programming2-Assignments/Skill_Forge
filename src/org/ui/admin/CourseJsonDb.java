package org.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.model.Course;
import org.model.ApprovalStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class CourseJsonDb {

    private static final String COURSES_FILE = "data/courses.json";
    private final Gson gson;

    public CourseJsonDb() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    // LOAD COURSES
    public ArrayList<Course> loadCourses() {
        ArrayList<Course> courses = new ArrayList<>();

        File file = new File(COURSES_FILE);
        if (!file.exists()) {
            return courses;
        }

        try (FileReader reader = new FileReader(file)) {
            Course[] courseArray = gson.fromJson(reader, Course[].class);
            if (courseArray != null) {
                Collections.addAll(courses, courseArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return courses;
    }

    // SAVE COURSES
    public void saveCourses(ArrayList<Course> courses) {
        try (FileWriter writer = new FileWriter(COURSES_FILE)) {
            gson.toJson(courses, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GET COURSE BY ID
    public Course getCourseById(int id) {
        ArrayList<Course> courses = loadCourses();
        for (Course c : courses) {
            if (c.getCourseId() == id) {
                return c;
            }
        }
        return null;
    }

    // UPDATE COURSE
    public void updateCourse(Course updatedCourse) {
        ArrayList<Course> courses = loadCourses();

        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseId() == updatedCourse.getCourseId()) {
                courses.set(i, updatedCourse);
                saveCourses(courses);
                return;
            }
        }
    }

    // GET PENDING COURSES
    public ArrayList<Course> getPendingCourses() {
        ArrayList<Course> courses = loadCourses();
        ArrayList<Course> pending = new ArrayList<>();

        for (Course c : courses) {
            if (c.getStatus() == ApprovalStatus.PENDING) {
                pending.add(c);
            }
        }
        return pending;
    }

    // APPROVE COURSE
    public void approveCourse(int courseId) {
        Course c = getCourseById(courseId);
        if (c == null) return;
        c.setStatus(ApprovalStatus.APPROVED);
        updateCourse(c);
    }

    // REJECT COURSE
    public void rejectCourse(int courseId) {
        Course c = getCourseById(courseId);
        if (c == null) return;
        c.setStatus(ApprovalStatus.REJECTED);
        updateCourse(c);
    }

    // GET APPROVED COURSES
    public ArrayList<Course> getApprovedCourses() {
        ArrayList<Course> courses = loadCourses();
        ArrayList<Course> approved = new ArrayList<>();

        for (Course c : courses) {
            if (c.getStatus() == ApprovalStatus.APPROVED) {
                approved.add(c);
            }
        }
        return approved;
    }

    // ⭐ THE MISSING METHOD ⭐
    public ArrayList<Course> getRejectedCourses() {
        ArrayList<Course> courses = loadCourses();
        ArrayList<Course> rejected = new ArrayList<>();

        for (Course c : courses) {
            if (c.getStatus() == ApprovalStatus.REJECTED) {
                rejected.add(c);
            }
        }
        return rejected;
    }

    // ENROLL STUDENT (ONLY IF APPROVED)
    public boolean enrollStudent(int courseId, int studentId) {
        Course c = getCourseById(courseId);
        if (c == null) return false;

        if (c.getStatus() != ApprovalStatus.APPROVED) {
            return false;
        }

        boolean added = c.enrollStudent(studentId);
        updateCourse(c);
        return added;
    }
}
