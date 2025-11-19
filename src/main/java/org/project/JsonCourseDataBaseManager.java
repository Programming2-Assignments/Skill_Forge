package org.project;
import com.google.gson.*;
import java.io.*;
import java.util.ArrayList;

public class JsonCourseDataBaseManager {
    private static final String COURSES_FILE = "courses.json";
    private Gson gson;

    public JsonCourseDataBaseManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // LOAD COURSES
    public ArrayList<course> loadCourses() {
        ArrayList<course> courses = new ArrayList<>();
        File file = new File(COURSES_FILE);
        if (!file.exists()) {
            return courses;
        }

        try (FileReader reader = new FileReader(COURSES_FILE)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject courseObj = element.getAsJsonObject();

                // Deserialize course with Gson
                course c = gson.fromJson(courseObj, course.class);
                courses.add(c);
            }

        } catch (Exception e) { e.printStackTrace(); }

        return courses;
    }

    // SAVE COURSES
    public void saveCourses(ArrayList<course> courses) {
        try (FileWriter writer = new FileWriter(COURSES_FILE)) {
            gson.toJson(courses, writer);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ADD COURSE
    public course addCourse(course course) {
        ArrayList<course> courses = loadCourses();
        int nextId = generateNextCourseId(courses);
        course.setCourseId(nextId);
        courses.add(course);
        saveCourses(courses);
        return course;
    }

    private int generateNextCourseId(ArrayList<course> courses) {
        int max = 0;
        for (course c : courses)
            if (c.getCourseId() > max) max = c.getCourseId();
        return max + 1;
    }

    // FIND COURSE
    public course getCourseById(int id) {
        ArrayList<course> courses = loadCourses();
        for (course c : courses)
            if (c.getCourseId() == id) return c;
        return null;
    }

    // UPDATE COURSE
    public boolean updateCourse(course updatedCourse) {
        ArrayList<course> courses = loadCourses();

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
        ArrayList<course> courses = loadCourses();

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
        ArrayList<course> courses = loadCourses();

        for (course c : courses)
            if (c.getCourseId() == courseId) {
                int nextLessonId = generateNextLessonId(c);
                lesson.setLessonId(nextLessonId);
                c.getLessons().add(lesson);
                saveCourses(courses);
                return lesson;
            }

        return null;
    }

    private int generateNextLessonId(course c) {
        int max = 0;
        for (Lesson l : c.getLessons())
            if (l.getLessonId() > max)
                max = l.getLessonId();
        return max + 1;
    }

    public boolean deleteLesson(int courseId, int lessonId) {
        ArrayList<course> courses = loadCourses();

        for (course c : courses)
            if (c.getCourseId() == courseId) {
                boolean removed = c.removeLessonById(lessonId);
                if (removed) saveCourses(courses);
                return removed;
            }

        return false;
    }

    public ArrayList<course> getAllCourses() {
        return new ArrayList<>(loadCourses());
    }


    public boolean addStudentToCourse(int courseId, int studentId) {
        ArrayList<course> courses = loadCourses();

        for (int i = 0; i < courses.size(); i++) {
            course c = courses.get(i);

            if (c.getCourseId() == courseId) {
                if (!c.getStudents().contains(studentId)) {
                    c.getStudents().add(studentId);
                    saveCourses(courses);
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}

