package org.project.storage;

import com.google.gson.Gson;
import com.google.gson.*;
import org.project.model.Course;
import org.project.model.Lesson;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class CourseJsonDb {
    private static final String COURSES_FILE = "courses.json";
    private Gson gson;

    public CourseJsonDb() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // LOAD COURSES
    public ArrayList<Course> loadCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        File file = new File(COURSES_FILE);
        if (!file.exists()) {
            return courses;
        }

        try (FileReader reader = new FileReader(COURSES_FILE)) {
            Course[] courseArray = gson.fromJson(reader, Course[].class);
            if (courseArray != null) {
                Collections.addAll(courses, courseArray);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return courses;
    }

    // SAVE COURSES
    public void saveCourses(ArrayList<Course> courses) {
        try (FileWriter writer = new FileWriter(COURSES_FILE)) {
            gson.toJson(courses, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ADD COURSE
    public Course addCourse(Course course) {
        ArrayList<Course> courses = loadCourses();
        int nextId = generateNextCourseId(courses);
        course.setCourseId(nextId);
        courses.add(course);
        saveCourses(courses);
        return course;
    }

    private int generateNextCourseId(ArrayList<Course> courses) {
        int max = 0;
        for (Course c : courses)
            if (c.getCourseId() > max) max = c.getCourseId();
        return max + 1;
    }

    // FIND COURSE
    public Course getCourseById(int id) {
        ArrayList<Course> courses = loadCourses();
        for (Course c : courses)
            if (c.getCourseId() == id) return c;
        return null;
    }

    // UPDATE COURSE
    public boolean updateCourse(Course updatedCourse) {
        ArrayList<Course> courses = loadCourses();

        for (int i = 0; i < courses.size(); i++)
            if (courses.get(i).getCourseId() == updatedCourse.getCourseId()) {
                courses.set(i, updatedCourse);
                saveCourses(courses);
                return true;
            }

        return false;
    }


    // DELETE COURSE
    public boolean deleteCourse(int id) {
        ArrayList<Course> courses = loadCourses();

        for (int i = 0; i < courses.size(); i++)
            if (courses.get(i).getCourseId() == id) {
                courses.remove(i);
                saveCourses(courses);
                return true;
            }

        return false;
    }


    // LESSON MANAGEMENT
    public Lesson addLesson(int courseId, Lesson lesson) {
        ArrayList<Course> courses = loadCourses();

        for (Course c : courses)
            if (c.getCourseId() == courseId) {
                int nextLessonId = generateNextLessonId(c);
                lesson.setLessonId(nextLessonId);
                c.getLessons().add(lesson);
                saveCourses(courses);
                return lesson;
            }

        return null;
    }

    private int generateNextLessonId(Course c) {
        int max = 0;
        for (Lesson l : c.getLessons())
            if (l.getLessonId() > max)
                max = l.getLessonId();
        return max + 1;
    }

    public boolean deleteLesson(int courseId, int lessonId) {
        ArrayList<Course> courses = loadCourses();

        for (Course c : courses)
            if (c.getCourseId() == courseId) {
                boolean removed = c.removeLessonById(lessonId);
                if (removed) saveCourses(courses);
                return removed;
            }

        return false;
    }


    // ENROLL STUDENT
    public boolean enrollStudent(int courseId, int studentId) {
        ArrayList<Course> courses = loadCourses();

        for (Course c : courses)
            if (c.getCourseId() == courseId) {
                boolean added = c.enrollStudent(studentId);
                if (added) {
                    saveCourses(courses);
                }
                return added;
            }

        return false;
    }

    // UNENROLL STUDENT
    public boolean unenrollStudent(int courseId, int studentId) {
        ArrayList<Course> courses = loadCourses();

        for (Course c : courses)
            if (c.getCourseId() == courseId) {
                boolean removed = c.unenrollStudent(studentId);
                if (removed) saveCourses(courses);
                return removed;
            }

        return false;
    }

}
