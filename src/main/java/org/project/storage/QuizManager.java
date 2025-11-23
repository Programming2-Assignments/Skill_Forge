package org.project.storage;

import org.project.model.QuizAttempt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuizManager {
    private final JsonDatabaseManager db;

    public QuizManager() {
        this.db = new JsonDatabaseManager();
    }

    public void saveAttempt(QuizAttempt attempt) {
        ArrayList<QuizAttempt> attempts = db.loadQuizAttempts();
        attempts.add(attempt);
        db.saveQuizAttempts(attempts);
    }

    public List<QuizAttempt> getAttemptsForStudentAndQuiz(String studentId, String quizId) {
        return db.loadQuizAttempts().stream()
                .filter(a -> a.getStudentId().equals(studentId) && a.getQuizId().equals(quizId))
                .collect(Collectors.toList());
    }

    public int countAttemptsForStudentQuiz(String studentId, String quizId) {
        return getAttemptsForStudentAndQuiz(studentId, quizId).size();
    }

    public List<QuizAttempt> getAllAttempts() {
        return db.loadQuizAttempts();
    }
}
