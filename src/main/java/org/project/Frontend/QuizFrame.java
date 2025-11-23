package org.project.Frontend;

import org.project.model.Question;
import org.project.model.Quiz;
import org.project.model.QuizAttempt;
import org.project.storage.QuizManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class QuizFrame extends JFrame {
    private final Quiz quiz;
    private final String studentId;

    private final List<Integer> answers;
    private int currentIndex = 0;

    private final JLabel lblQuestionNumber = new JLabel();
    private final JTextArea taQuestion = new JTextArea();
    private final JPanel optionsPanel = new JPanel();
    private final JProgressBar progressBar = new JProgressBar();
    private final JButton btnPrev = new JButton("Prev");
    private final JButton btnNext = new JButton("Next");
    private final JButton btnSubmit = new JButton("Submit");

    public QuizFrame(Quiz quiz, String studentId) {
        this.quiz = quiz;
        this.studentId = studentId;
        this.answers = new ArrayList<>();
        int qSize = (quiz != null && quiz.getQuestions() != null) ? quiz.getQuestions().size() : 0;
        for (int i = 0; i < qSize; i++) answers.add(-1);

        initUI();
        if (qSize > 0) renderQuestion(0);
    }

    private void initUI() {
        setTitle("Quiz: " + (quiz.getQuizId() == null ? "" : quiz.getQuizId()));
        setSize(820, 640);
        setMinimumSize(new Dimension(300, 260));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setBackground(Color.WHITE);
        JLabel title = new JLabel("Quiz");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(25, 40, 80));
        header.add(title, BorderLayout.WEST);

        progressBar.setMinimum(0);
        progressBar.setMaximum(Math.max(1, answers.size()));
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(220, 24));
        progressBar.setBackground(Color.WHITE);
        header.add(progressBar, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);


        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1, true),
                new EmptyBorder(14, 14, 14, 14)
        ));


        lblQuestionNumber.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblQuestionNumber.setForeground(new Color(35, 55, 120));
        card.add(lblQuestionNumber, BorderLayout.NORTH);

        taQuestion.setLineWrap(true);
        taQuestion.setWrapStyleWord(true);
        taQuestion.setEditable(false);
        taQuestion.setFocusable(false);
        taQuestion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        taQuestion.setBackground(Color.WHITE);
        taQuestion.setBorder(null);
        taQuestion.setOpaque(false);

        JScrollPane questionScroll = new JScrollPane(taQuestion,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        questionScroll.setBorder(null);
        questionScroll.setOpaque(false);
        questionScroll.getViewport().setOpaque(false);

        card.add(questionScroll, BorderLayout.CENTER);

        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setBorder(new EmptyBorder(8, 6, 8, 6));
        card.add(optionsPanel, BorderLayout.SOUTH);

        root.add(card, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        nav.setBackground(Color.WHITE);

        styleNavButton(btnPrev);
        styleNavButton(btnNext);
        styleNavButton(btnSubmit);

        btnPrev.addActionListener(e -> gotoIndex(currentIndex - 1));
        btnNext.addActionListener(e -> gotoIndex(currentIndex + 1));
        btnSubmit.addActionListener(e -> confirmAndSubmit());

        btnSubmit.setVisible(false);
        btnPrev.setEnabled(false);

        nav.add(btnPrev);
        nav.add(btnNext);
        nav.add(btnSubmit);

        footer.add(nav, BorderLayout.SOUTH);
        root.add(footer, BorderLayout.SOUTH);

        add(root);
    }

    private void styleNavButton(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(35, 65, 204));
        b.setForeground(Color.BLUE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void renderQuestion(int index) {
        if (quiz == null || quiz.getQuestions() == null || index < 0 || index >= quiz.getQuestions().size()) return;

        currentIndex = index;
        Question q = quiz.getQuestions().get(index);


        lblQuestionNumber.setText(String.format("Question %d of %d", index + 1, quiz.getQuestions().size()));


        taQuestion.setText(q.getText());


        optionsPanel.removeAll();
        ButtonGroup bg = new ButtonGroup();
        int optionIndex = 0;
        for (String opt : q.getOptions()) {
            JRadioButton rb = new JRadioButton(opt);
            rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            rb.setBorder(new EmptyBorder(8, 6, 8, 6));
            rb.setBackground(Color.WHITE);
            rb.setActionCommand(String.valueOf(optionIndex));

            int selected = answers.get(index);
            if (selected == optionIndex) rb.setSelected(true);

            rb.addActionListener(e -> {

                answers.set(currentIndex, Integer.parseInt(rb.getActionCommand()));
                updateProgressBar();
            });

            bg.add(rb);


            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(Color.WHITE);
            row.add(rb, BorderLayout.WEST);
            optionsPanel.add(row);
            optionsPanel.add(Box.createRigidArea(new Dimension(0, 6)));
            optionIndex++;
        }


        btnPrev.setEnabled(currentIndex > 0);
        btnNext.setVisible(currentIndex < quiz.getQuestions().size() - 1);
        btnSubmit.setVisible(currentIndex == quiz.getQuestions().size() - 1);

        updateProgressBar();

        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void updateProgressBar() {
        int answered = 0;
        for (Integer a : answers) if (a != null && a >= 0) answered++;
        progressBar.setValue(answered);
        progressBar.setString(String.format("Answered %d / %d", answered, answers.size()));
    }

    private void gotoIndex(int idx) {
        if (idx < 0 || idx >= quiz.getQuestions().size()) return;
        renderQuestion(idx);
    }

    private void confirmAndSubmit() {

        int answered = 0;
        for (Integer a : answers) if (a != null && a >= 0) answered++;

        int missing = answers.size() - answered;
        String message = String.format("You're about to submit the quiz.\nAnswered: %d\nUnanswered: %d\n\nDo you want to continue?", answered, missing);
        int res = JOptionPane.showConfirmDialog(this, message, "Confirm Submit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (res != JOptionPane.YES_OPTION) return;


        int score = calculateScore();
        boolean passed = quiz.isPassed(score);


        QuizManager qm = new QuizManager();
        Integer n = new Random().nextInt(10000);
        String id = String.valueOf(n);
        QuizAttempt attempt = new QuizAttempt(id, quiz.getQuizId(), studentId, score, passed, LocalDateTime.now().toString());
        qm.saveAttempt(attempt);


        QuizResultFrame resultFrame = new QuizResultFrame(quiz, answers, score, passed, attempt);
        resultFrame.setVisible(true);
        dispose();
    }


    private int calculateScore() {

        try {
            return quiz.calculateScore(answers);
        } catch (Exception ex) {

            int correct = 0;
            List<Question> qs = quiz.getQuestions();
            for (int i = 0; i < qs.size(); i++) {
                Integer sel = answers.get(i);
                if (sel != null && sel >= 0 && qs.get(i).isCorrect(sel)) correct++;
            }
            return (int) ((correct * 100.0) / (qs.size() == 0 ? 1 : qs.size()));
        }
    }
}
