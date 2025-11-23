package org.project.model;

public class QuizAttempt {
    private String attemptId;
    private String quizId;
    private String studentId;
    private int score;
    private boolean passed;
    private String attemptDate;

    public QuizAttempt() {}

    public QuizAttempt(String attemptId, String quizId, String studentId, int score, boolean passed, String attemptDate) {
        this.attemptId = attemptId;
        this.quizId = quizId;
        this.studentId = studentId;
        this.score = score;
        this.passed = passed;
        this.attemptDate = attemptDate;
    }

    public String getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(String attemptId) {
        this.attemptId = attemptId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(String attemptDate) {
        this.attemptDate = attemptDate;
    }

}
