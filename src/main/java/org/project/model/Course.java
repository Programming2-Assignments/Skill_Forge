package org.project.model;

import java.util.*;

public class Course {
    private int courseId;
    private String title;
    private String description;
    private int instructorId;
    private List<Lesson> lessons;
    private List<Integer> students;

    // Default constructor required for Gson
    public Course() {
        this.lessons = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public Course(int courseId, String title, String description, int instructorId) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    // Getters
    public int getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<Integer> getStudents() {
        return students;
    }

    // Setters
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons != null ? lessons : new ArrayList<>();
    }

    public void setStudents(List<Integer> students) {
        this.students = students != null ? students : new ArrayList<>();
    }

    // Methods
    public boolean addLesson(Lesson lesson) {
        if (lessons.stream().anyMatch(l -> l.getLessonId() == lesson.getLessonId())) return false;
        lessons.add(lesson);
        return true;
    }

    public boolean removeLessonById(int lessonId) {
        return lessons.removeIf(l -> l.getLessonId() == lessonId);
    }

    public Lesson getLessonById(int lessonId) {
        return lessons.stream().filter(l -> l.getLessonId() == lessonId).findFirst().orElse(null);
    }

    public boolean enrollStudent(int studentId) {
        if (students.contains(studentId)) return false;
        students.add(studentId);
        return true;
    }

    public boolean unenrollStudent(int studentId) {
        return students.remove(Integer.valueOf(studentId));
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", instructorId=" + instructorId +
                ", lessons=" + lessons +
                ", students=" + students +
                '}';
    }

}
