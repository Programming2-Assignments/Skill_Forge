package org.project.model;

import java.util.List;

public class Quiz {
    private String quizId;
    private String lessonId;
    private List<Question> questions;
    private int passScore = 60;
    private int maxAttempts = -1;

    public Quiz() {}

    public Quiz(String quizId, String lessonId, List<Question> questions, int passScore, int maxAttempts) {
        this.quizId = quizId;
        this.lessonId = lessonId;
        this.questions = questions;
        this.passScore = passScore;
        this.maxAttempts = maxAttempts;
    }

    public int calculateScore(List<Integer> chosenAnswers) {
        int correct = 0;
        for (int i = 0; i < questions.size() && i < chosenAnswers.size(); i++) {
            if (questions.get(i).isCorrect(chosenAnswers.get(i))) correct++;
        }
        return (int) ((correct * 100.0) / (questions.size() == 0 ? 1 : questions.size()));
    }

    public boolean isPassed(int score) {
        return score >= passScore;
    }
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getPassScore() {
        return passScore;
    }

    public void setPassScore(int passScore) {
        this.passScore = passScore;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

}
