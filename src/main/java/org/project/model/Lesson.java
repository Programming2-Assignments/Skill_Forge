package org.project.model;

public class Lesson {
    private int lessonId;
    private String title;
    private String content;
    private boolean check;
    private Quiz quiz;

    // Constructors
    public Lesson() {
    }

    public Lesson(int lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }


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

    public Quiz getQuiz() { return quiz; }

    public void setQuiz(Quiz quiz) { this.quiz = quiz; }


    @Override
    public String toString() {
        return "Lesson{" +
                "lessonId=" + lessonId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
