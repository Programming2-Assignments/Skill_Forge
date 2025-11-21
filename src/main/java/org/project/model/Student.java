package org.project.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student extends User {
    private ArrayList<Integer> enrolledCourses;
    private Map<Integer, Integer> progress; // courseId -> progress %
    private Map<Integer, ArrayList<Integer>> lessonsPerCourse; // courseId -> all lesson IDs
    private Map<Integer, ArrayList<Integer>> completedLessons; // courseId -> completed lesson IDs

    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "Student");
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
        this.lessonsPerCourse = new HashMap<>();
        this.completedLessons = new HashMap<>();
    }

    // ------------------- Enrolled Courses -------------------
    public ArrayList<Integer> getEnrolledCourses() {
        if (enrolledCourses == null) enrolledCourses = new ArrayList<>();
        return enrolledCourses;
    }

    public void setEnrolledCourses(ArrayList<Integer> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    // ------------------- Progress -------------------
    public Map<Integer, Integer> getProgress() {
        if (progress == null) progress = new HashMap<>();
        return progress;
    }

    public void setProgress(Map<Integer, Integer> progress) {
        this.progress = progress;
    }

    // ------------------- Lessons per Course -------------------
    public Map<Integer, ArrayList<Integer>> getLessonsPerCourse() {
        if (lessonsPerCourse == null) lessonsPerCourse = new HashMap<>();
        return lessonsPerCourse;
    }

    public void setLessonsForCourse(int courseId, ArrayList<Integer> lessons) {
        lessonsPerCourse.put(courseId, lessons);
        completedLessons.putIfAbsent(courseId, new ArrayList<>());
    }

    // ------------------- Completed Lessons -------------------
    public Map<Integer, ArrayList<Integer>> getCompletedLessons() {
        if (completedLessons == null) completedLessons = new HashMap<>();
        return completedLessons;
    }

    public void setCompletedLessons(Map<Integer, ArrayList<Integer>> completedLessons) {
        this.completedLessons = completedLessons;
    }

    public void markLessonCompleted(int courseId, int lessonId) {
        completedLessons.putIfAbsent(courseId, new ArrayList<>());
        ArrayList<Integer> list = completedLessons.get(courseId);
        if (!list.contains(lessonId)) {
            list.add(lessonId);
        }
        progress.put(courseId, calculateProgress(courseId));
    }

    public boolean isLessonCompleted(int courseId, int lessonId) {
        return completedLessons.getOrDefault(courseId, new ArrayList<>()).contains(lessonId);
    }

    public int calculateProgress(int courseId) {
        ArrayList<Integer> completed = completedLessons.getOrDefault(courseId, new ArrayList<>());
        ArrayList<Integer> allLessons = lessonsPerCourse.getOrDefault(courseId, new ArrayList<>());
        if (allLessons.isEmpty()) return 0;
        return (int) ((completed.size() * 100.0) / allLessons.size());
    }

    // ------------------- Utility -------------------
    public boolean hasEnrolledInCourse(int courseId) {
        return enrolledCourses != null && enrolledCourses.contains(courseId);
    }
}
