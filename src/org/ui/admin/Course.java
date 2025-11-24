package org.model;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private int courseId;
    private String title;
    private String description;
    private int instructorId;

    private final List<Lesson> lessons;
    private final List<Integer> students;

    private ApprovalStatus status;

    // Default constructor for Gson
    public Course() {
        this.lessons = new ArrayList<>();
        this.students = new ArrayList<>();
        this.status = ApprovalStatus.PENDING;
    }

    // Full constructor (useful for instructors creating courses)
    public Course(int courseId, String title, String description, int instructorId) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = new ArrayList<>();
        this.students = new ArrayList<>();
        this.status = ApprovalStatus.PENDING;
    }

    // Getters
    public int getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getInstructorId() { return instructorId; }
    public List<Lesson> getLessons() { return lessons; }
    public List<Integer> getStudents() { return students; }
    public ApprovalStatus getStatus() { return status; }

    // Setters
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    public void setStatus(ApprovalStatus status) { this.status = status; }

    // Lesson methods
    public boolean addLesson(Lesson lesson) {
        return lessons.add(lesson);
    }

    public boolean removeLessonById(int lessonId) {
        return lessons.removeIf(l -> l.getLessonId() == lessonId);
    }

    public Lesson getLessonById(int lessonId) {
        return lessons.stream().filter(l -> l.getLessonId() == lessonId).findFirst().orElse(null);
    }

    // Student methods
    public boolean enrollStudent(int studentId) {
        if (!students.contains(studentId)) {
            students.add(studentId);
            return true;
        }
        return false;
    }

    public boolean unenrollStudent(int studentId) {
        return students.remove(Integer.valueOf(studentId));
    }
}
