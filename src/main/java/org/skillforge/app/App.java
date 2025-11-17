package org.skillforge.app;

import org.skillforge.model.Course;
import org.skillforge.model.Lesson;
import org.skillforge.service.CourseJsonDb;  // or your exact name

public class App {
    public static void main(String[] args) {

        CourseJsonDb mgr = new CourseJsonDb();

        Course c = new Course();
        c.setTitle("Java OOP");
        c.setDescription("Intro to OOP");
        c.setInstructorId(2);

        mgr.addCourse(c);

        Lesson l = new Lesson(0, "Lesson 1", "Content");
        mgr.addLesson(c.getCourseId(), l);

        mgr.enrollStudent(c.getCourseId(), 5);

        System.out.println(mgr.loadCourses());
    }
}
