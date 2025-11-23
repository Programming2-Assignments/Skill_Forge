package org.project.Frontend;

import org.project.model.Question;
import org.project.model.Quiz;
import org.project.model.QuizAttempt;
import org.project.storage.QuizManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuizResultFrame extends JFrame {

    public QuizResultFrame(Quiz quiz, List<Integer> answers, int score, boolean passed, QuizAttempt attempt) {
        setTitle("Quiz Result - " + (quiz != null && quiz.getQuizId() != null ? quiz.getQuizId() : ""));
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel header = new JPanel(new BorderLayout());
        JLabel lblScore = new JLabel("Score: " + score + "%");
        lblScore.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(lblScore, BorderLayout.WEST);

        JLabel lblStatus = new JLabel(passed ? "Passed " : "Failed ");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 18));
        lblStatus.setHorizontalAlignment(SwingConstants.RIGHT);
        lblStatus.setForeground(passed ? new Color(0, 120, 0) : Color.RED);
        header.add(lblStatus, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        String[] cols = {"#", "Question", "Your Answer", "Correct Answer"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (quiz != null && quiz.getQuestions() != null) {
            List<Question> qs = quiz.getQuestions();
            for (int i = 0; i < qs.size(); i++) {
                Question q = qs.get(i);
                int userIdx = (answers != null && i < answers.size()) ? answers.get(i) : -1;
                String yourAns = (userIdx >= 0 && userIdx < q.getOptions().size()) ? q.getOptions().get(userIdx) : "No answer";
                String correctAns = q.getOptions().get(q.getCorrectOptionIndex());
                model.addRow(new Object[]{i + 1, q.getText(), yourAns, correctAns});
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(380);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);

        root.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        JButton btnSaveClose = new JButton("Save & Close");
        btnSaveClose.addActionListener(e -> {
            if (attempt != null) {
                // ensure attempt saved (idempotent)
                QuizManager qm = new QuizManager();
                qm.saveAttempt(attempt);
            }
            dispose();
        });

        footer.add(btnSaveClose);
        footer.add(btnClose);

        root.add(footer, BorderLayout.SOUTH);

        add(root);
    }
}
