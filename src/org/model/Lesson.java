package org.model;

public class Lesson {
    private int lessonId;
    private String title;
    private String content;

    public Lesson() {}

    public Lesson(int lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
    }

    public int getLessonId() { return lessonId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }

    public void setLessonId(int lessonId) { this.lessonId = lessonId; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
}
