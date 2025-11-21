package org.project.model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private int courseId;
    private String title;
    private String description;
    private int instructorId;
    private List<Lesson> lessons;
    private List<Integer> students;
    private int NoOfLesson; // old
    private int NoOfChecked; // old

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

    // Getters & Setters
    public int getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getInstructorId() { return instructorId; }
    public List<Lesson> getLessons() { return lessons; }
    public List<Integer> getStudents() { return students; }

    public void setCourseId(int courseId) { this.courseId = courseId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons != null ? lessons : new ArrayList<>(); }
    public void setStudents(List<Integer> students) { this.students = students != null ? students : new ArrayList<>(); }

    // Old methods
    public int getNoOfLesson() { return NoOfLesson; }
    public void setNoOfLesson(int noOfLesson) { NoOfLesson = noOfLesson; }
    public void addlesson() { NoOfLesson++; }

    public void addchecked() { NoOfChecked++; }
    public double calculateprogress() {
        if (NoOfLesson == 0) return 0;
        return (NoOfChecked * 100.0) / NoOfLesson;
    }

    // New / existing methods
    public boolean addLesson(Lesson lesson) {
        if (lessons.stream().anyMatch(l -> l.getLessonId() == lesson.getLessonId())) return false;
        lessons.add(lesson);
        addlesson();
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
}
