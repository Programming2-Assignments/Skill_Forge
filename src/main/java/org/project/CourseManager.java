package org.skillforge.storage;

import org.skillforge.model.Course;
import org.skillforge.model.Lesson;
import org.skillforge.service.CourseJsonDb;

import java.util.ArrayList;


public class CourseManager {

    private final CourseJsonDb courseDb;

    // Constructor
    public CourseManager() {
        this.courseDb = new CourseJsonDb();
    }


    // COURSE OPERATIONS
    public Course createCourse(String title, String description, int instructorId) {
        Course c = new Course();
        c.setTitle(title);
        c.setDescription(description);
        c.setInstructorId(instructorId);
        return courseDb.addCourse(c);
    }

    public boolean editCourse(int courseId, String newTitle, String newDescription) {
        Course c = courseDb.getCourseById(courseId);
        if (c == null) return false;
        c.setTitle(newTitle);
        c.setDescription(newDescription);
        return courseDb.updateCourse(c);
    }

    public boolean deleteCourse(int courseId) {
        return courseDb.deleteCourse(courseId);
    }


    // LESSON OPERATIONS
    public Lesson addLesson(int courseId, String title, String content) {
        Lesson l = new Lesson();
        l.setTitle(title);
        l.setContent(content);
        return courseDb.addLesson(courseId, l);
    }

    public boolean editLesson(int courseId, int lessonId, String newTitle, String newContent) {
        Course c = courseDb.getCourseById(courseId);
        if (c == null) return false;
        Lesson l = c.getLessonById(lessonId);
        if (l == null) return false;
        l.setTitle(newTitle);
        l.setContent(newContent);
        return courseDb.updateCourse(c);
    }

    public boolean deleteLesson(int courseId, int lessonId) {
        Course c = courseDb.getCourseById(courseId);
        if (c == null) return false;
        return courseDb.deleteLesson(courseId, lessonId);
    }


    // STUDENT ENROLLMENT
    // ENROLL
    public boolean enrollStudent(int courseId, int studentId) {
        return courseDb.enrollStudent(courseId, studentId);
    }

    // UNENROLL
    public boolean unenrollStudent(int courseId, int studentId) {
        return courseDb.unenrollStudent(courseId, studentId);
    }


    // COURSE RETRIEVAL
    public Course getCourseById(int id) {
        return courseDb.getCourseById(id);
    }

    public ArrayList<Course> getAllCourses() {
        return courseDb.loadCourses();
    }


}
