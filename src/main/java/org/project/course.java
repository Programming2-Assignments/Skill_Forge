package org.project;

import java.util.*;


public class course {
    private int courseId;
    private String title;
    private String description;
    private int instructorId;
    private List<Lesson> lessons;
    private List<Integer> students;
    private int NoOfChecked;
    private int NoOfLesson;

    // Constructors
    public course() {
        this.lessons = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public course(int courseId, String title, String description, int instructorId) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
    }

    public int getNoOfLesson() {
        return NoOfLesson;
    }

    public void setNoOfLesson(int noOfLesson) {
        NoOfLesson = noOfLesson;
    }

    // Getters and Setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId (int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public int getInstructorId () {
        return instructorId;
    }

    public void setInstructorId (int instructorId) {
        this.instructorId = instructorId;
    }


    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public ArrayList<Integer> getStudents() {
        return (ArrayList<Integer>) students;
    }

    public void setStudents(List<Integer> students) {
        this.students = students;
    }

    // Methods
    public boolean addLesson (Lesson lesson) {
        for (Lesson l : lessons)
            if (l.getLessonId() == lesson.getLessonId())
                return false;
        lessons.add(lesson);
        return true;
    }

    public boolean removeLessonById(int lessonId) {
        return lessons.removeIf(l -> l.getLessonId() == lessonId);
    }

    public Lesson getLessonById (int lessonId) {
        for (Lesson l : lessons)
            if (l.getLessonId() == lessonId)
                return l;
        return null;
    }

    public boolean enrollStudent(int studentId) {
        if (students.contains(studentId))
            return false;
        students.add(studentId);
        return true;
    }

    public boolean unenrollStudent(int studentId) {
        return students.removeIf(id -> id == studentId);
    }
    public void addlesson(){
        this.NoOfLesson = this.NoOfLesson + 1 ;
    }

    public void addchecked(){
        this.NoOfChecked = this.NoOfChecked + 1 ;
    }
    public double calculateprogress(){
        return (1.0*(this.NoOfChecked)/1.0*(this.NoOfLesson));
    }
}
