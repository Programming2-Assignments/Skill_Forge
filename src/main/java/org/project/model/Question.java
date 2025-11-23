package org.project.model;

import java.util.List;

public class Question {
    private String questionId;
    private String text;
    private List<String> options;
    private int correctOptionIndex;

    public Question() {}

    public Question(String questionId, String text, List<String> options, int correctOptionIndex) {
        this.questionId = questionId;
        this.text = text;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public boolean isCorrect(int chosenIndex) {
        return chosenIndex == correctOptionIndex;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }

}
