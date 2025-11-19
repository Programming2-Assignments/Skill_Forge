package org.project;

public class Lesson {
    private int lessonId;
    private String title;
    private String content;
    private boolean check;

    // Constructors
    public Lesson() {}

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Lesson(int lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
    }

    // Getters and Setters
    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

