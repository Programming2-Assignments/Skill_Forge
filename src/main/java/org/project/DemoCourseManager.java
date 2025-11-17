package org.skillforge.storage;

public class DemoCourseManager {
    public static void main(String[] args) {
        CourseManager manager = new CourseManager();

        // Create course
        var course = manager.createCourse("Java OOP", "Intro to OOP", 2);

        // Add lessons
        manager.addLesson(course.getCourseId(), "Lesson 1", "Content");
        manager.addLesson(course.getCourseId(), "Lesson 2", "Content 2");

        // Enroll students
        manager.enrollStudent(course.getCourseId(), 5);
        manager.enrollStudent(course.getCourseId(), 6);

        // Edit course
        manager.editCourse(course.getCourseId(), "Advanced Java OOP", "OOP in depth");

        // Display all courses
        manager.getAllCourses().forEach(System.out::println);
    }
}
