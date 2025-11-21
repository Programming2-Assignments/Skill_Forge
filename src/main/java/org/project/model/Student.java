package org.project.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student extends User {
    private ArrayList<Integer> enrolledCourses;
    private Map<Integer, Integer> progress;
    private Map<Integer, ArrayList<Integer>> lessonsPerCourse;
    private Map<Integer, ArrayList<Boolean>> checkedPerCourse;

    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, "Student");
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
        this.lessonsPerCourse = new HashMap<>();
        this.checkedPerCourse = new HashMap<>();
    }

    public ArrayList<Integer> getEnrolledCourses() {
        if (enrolledCourses == null) enrolledCourses = new ArrayList<>();
        return enrolledCourses;
    }

    public Map<Integer, Integer> getProgress() {
        if (progress == null) progress = new HashMap<>();
        return progress;
    }
    public void setEnrolledCourses(ArrayList<Integer> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void setProgress(Map<Integer, Integer> progress) {
        this.progress = progress;
    }

    public int getProgressForCourse(String courseId) {
        if (progress.containsKey(courseId)) {
            return progress.get(courseId);
        }
        return 0;
    }

    public Map<Integer, ArrayList<Integer>> getLessonsPerCourse() {
        return lessonsPerCourse;
    }

    public Map<Integer, ArrayList<Boolean>> getCheckedPerCourse() {
        return checkedPerCourse;
    }

    public void setLessonsForCourse(int courseId, ArrayList<Integer> lessons) {
        lessonsPerCourse.put(courseId, lessons);

        ArrayList<Boolean> checked = new ArrayList<>();
        for (int i = 0; i < lessons.size(); i++)
            checked.add(false); // default: not checked

        checkedPerCourse.put(courseId, checked);
    }

    public void checkLesson(int courseId, int lessonIndex) {
        if (!checkedPerCourse.containsKey(courseId)) return;

        checkedPerCourse.get(courseId).set(lessonIndex, true);
    }

    public int calculateProgress(int courseId) {
        if (!checkedPerCourse.containsKey(courseId)) return 0;

        ArrayList<Boolean> checked = checkedPerCourse.get(courseId);
        int total = checked.size();
        int done = 0;

        for (boolean b : checked) if (b) done++;

        return (int) ((done * 100.0) / total);
    }

    public void markLessonCompleted(int courseId, int lessonIndex) {
        if (!checkedPerCourse.containsKey(courseId)) return;

        ArrayList<Boolean> list = checkedPerCourse.get(courseId);

        // Prevent out of range
        if (lessonIndex < 0 || lessonIndex >= list.size()) return;

        list.set(lessonIndex, true);
    }

}