package org.project;
import org.project.model.*;
import org.project.storage.CourseJsonDb;
import org.project.storage.JsonDatabaseManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InstructorRole {

    private final JsonDatabaseManager Udb=new JsonDatabaseManager();
    private final CourseJsonDb Cdb=new CourseJsonDb();

    //COURSES
    public Course createCourse(Instructor instructor, String title, String description) {
        ArrayList<Course> courses = Cdb.loadCourses();
        int courseID = new Random().nextInt(10000);
        Course course=new Course(courseID,title,description,Integer.parseInt(instructor.getUserId()));
        courses.add(course);
        Cdb.saveCourses(courses);
        instructor.addCourse(course);
        saveInstructor(instructor);
        return course;
    }

    public void deleteCourse(int courseId, Instructor instructor){
        ArrayList<Course> courses = Cdb.loadCourses();
        for (Course c : courses) {
            if (c.getCourseId()==courseId) {
                courses.remove(c);
                instructor.removeCourse(c);
                break;
            }
        }
        Cdb.saveCourses(courses);
        saveInstructor(instructor);
    }

    public void editCourse(int courseId,String newCourseTitle, String newCourseDescription){
        ArrayList<Course> courses = Cdb.loadCourses();
        for (Course c : courses) {
            if (c.getCourseId()==courseId) {
                c.setTitle(newCourseTitle);
                c.setDescription(newCourseDescription);
            }
        }
        Cdb.saveCourses(courses);
    }
    // LESSONS
    public void addLesson(String courseId,String lessonName,String lessonContent){
        ArrayList<Course> courses = Cdb.loadCourses();
        int lessonId = new Random().nextInt(10000);
        Lesson lesson = new Lesson(lessonId,lessonName,lessonContent);
        for (Course course : courses) {
            if (courseId.equals(String.valueOf(course.getCourseId()))) {
                course.addLesson(lesson);
                break;
            }
        }
        Cdb.saveCourses(courses);

    }

    public void editLesson(int courseId,int lessonId,String newLessonName,String newLessonContent){
        ArrayList<Course> courses = Cdb.loadCourses();
        for (Course c : courses) {
            if (c.getCourseId()==(courseId)) {
                for (Lesson l : c.getLessons()) {
                    if (l.getLessonId()==lessonId) {
                        l.setTitle(newLessonName);
                        l.setContent(newLessonContent);
                        break;
                    }
                }
            }
        }
        Cdb.saveCourses(courses);

    }

    public void deleteLesson(int courseId,int lessonId){
        ArrayList<Course> courses = Cdb.loadCourses();
        for (Course course : courses) {
            if (courseId==course.getCourseId()) {
                course.removeLessonById(lessonId);
                break;
            }
        }
        Cdb.saveCourses(courses);
    }

    public List<Student> viewEnrollment(int courseId) {
        ArrayList<User> users = Udb.loadUsers();
        ArrayList<Course> courses = Cdb.loadCourses();
        ArrayList<Student> students = new ArrayList<>();
        List<Integer> studentIds = new ArrayList<>();
        for (Course course : courses) {
            if (courseId == course.getCourseId()) {
                studentIds = course.getStudents();
            }
        }
        for (Integer studentId : studentIds) {
            for (User user : users) {
                if (user instanceof Student && user.getUserId().equals(String.valueOf(studentId))) {
                    students.add((Student) user);
                }
            }
        }
        return students;
    }

    private void saveInstructor(Instructor instructor) {
        ArrayList<User> users = Udb.loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(instructor.getUserId())) {
                users.set(i, instructor);
                break;
            }
        }
        Udb.saveUsers(users);
    }

    public JsonDatabaseManager getUdb() {
        return Udb;
    }

    public CourseJsonDb getCdb() {
        return Cdb;
    }
}
