package org.project.Frontend;

import org.project.model.Course;
import org.project.model.Lesson;
import org.project.model.Question;
import org.project.model.Quiz;
import org.project.storage.CourseJsonDb;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddQuizFrame extends JFrame {

    private final int courseId;
    private final int lessonId;
    private final CourseJsonDb cdb = new CourseJsonDb();

    private final JPanel questionsContainer = new JPanel();
    private final List<QuestionPanel> questionPanels = new ArrayList<>();

    private final JTextField passScoreField = new JTextField("60");
    private final JTextField maxAttemptsField = new JTextField("-1");

    public AddQuizFrame(int courseId, int lessonId) {
        this.courseId = courseId;
        this.lessonId = lessonId;
        initUI();
        loadExistingQuizIfAny();
    }

    private void initUI() {
        setTitle("Add / Edit Quiz - Course " + courseId + " Lesson " + lessonId);
        setSize(820, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        top.add(new JLabel("Passing Score (%)"));
        passScoreField.setColumns(4);
        top.add(passScoreField);

        top.add(new JLabel("Max Attempts (-1 = unlimited)"));
        maxAttemptsField.setColumns(4);
        top.add(maxAttemptsField);

        root.add(top, BorderLayout.NORTH);

        questionsContainer.setLayout(new BoxLayout(questionsContainer, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(questionsContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        root.add(scroll, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton addQBtn = new JButton("Add Question");
        JButton saveBtn = new JButton("Save Quiz");
        JButton cancelBtn = new JButton("Cancel");

        addQBtn.addActionListener(e -> addQuestionPanel(null));
        saveBtn.addActionListener(e -> saveQuiz());
        cancelBtn.addActionListener(e -> dispose());

        footer.add(addQBtn);
        footer.add(saveBtn);
        footer.add(cancelBtn);

        root.add(footer, BorderLayout.SOUTH);

        add(root);

        if (questionPanels.isEmpty()) addQuestionPanel(null);
    }

    private void loadExistingQuizIfAny() {
        Course c = cdb.getCourseById(courseId);
        if (c == null) return;
        Lesson l = c.getLessonById(lessonId);
        if (l == null) return;
        Quiz q = l.getQuiz();
        if (q == null) return;

        questionsContainer.removeAll();
        questionPanels.clear();
        passScoreField.setText(String.valueOf(q.getPassScore()));
        maxAttemptsField.setText(String.valueOf(q.getMaxAttempts()));
        if (q.getQuestions() != null) {
            for (Question qq : q.getQuestions()) addQuestionPanel(qq);
        }
        SwingUtilities.invokeLater(() -> {
            questionsContainer.revalidate();
            questionsContainer.repaint();
        });
    }

    private void addQuestionPanel(Question prefill) {
        QuestionPanel qp = new QuestionPanel(prefill);
        questionPanels.add(qp);
        questionsContainer.add(qp);
        questionsContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        SwingUtilities.invokeLater(() -> {
            questionsContainer.revalidate();
            questionsContainer.repaint();
        });

        SwingUtilities.invokeLater(() -> {
            qp.requestFocusForFirstField();
        });
    }

    private void saveQuiz() {

        int passScore;
        int maxAttempts;
        try {
            passScore = Integer.parseInt(passScoreField.getText().trim());
            maxAttempts = Integer.parseInt(maxAttemptsField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pass score and max attempts must be integers.", "Validation", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (passScore < 0 || passScore > 100) {
            JOptionPane.showMessageDialog(this, "Pass score must be between 0 and 100.", "Validation", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Question> qlist = new ArrayList<>();
        for (QuestionPanel qp : questionPanels) {
            Question q = qp.toQuestion();
            if (q == null) {
                return;
            }
            qlist.add(q);
        }
        if (qlist.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least one question.", "Validation", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String quizId = UUID.randomUUID().toString();
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        quiz.setLessonId(String.valueOf(lessonId));
        quiz.setQuestions(qlist);
        quiz.setPassScore(passScore);
        quiz.setMaxAttempts(maxAttempts);

        Course c = cdb.getCourseById(courseId);
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Course not found. Cannot save quiz.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Lesson l = c.getLessonById(lessonId);
        if (l == null) {
            JOptionPane.showMessageDialog(this, "Lesson not found. Cannot save quiz.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        l.setQuiz(quiz);
        ArrayList<Course> courses = cdb.loadCourses();

        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseId() == c.getCourseId()) {
                courses.set(i, c);
                break;
            }
        }
        cdb.saveCourses(courses);

        JOptionPane.showMessageDialog(this, "Quiz saved and attached to lesson.", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private class QuestionPanel extends JPanel {
        private final JTextArea questionText = new JTextArea();
        private final JTextField[] optionFields = new JTextField[4];
        private final JComboBox<String> correctIndexCombo;
        private final JButton removeBtn = new JButton("Remove");

        public QuestionPanel(Question prefill) {
            setLayout(new BorderLayout(6, 6));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                    new EmptyBorder(8, 8, 8, 8)
            ));
            setBackground(Color.WHITE);
            setFocusable(true);

            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            JLabel lbl = new JLabel("Question");
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 13f));
            top.add(lbl, BorderLayout.WEST);

            removeBtn.addActionListener(e -> {
                questionPanels.remove(this);
                questionsContainer.remove(this);
                SwingUtilities.invokeLater(() -> {
                    questionsContainer.revalidate();
                    questionsContainer.repaint();
                });
            });
            top.add(removeBtn, BorderLayout.EAST);
            add(top, BorderLayout.NORTH);

            questionText.setLineWrap(true);
            questionText.setWrapStyleWord(true);
            questionText.setRows(3);
            questionText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            questionText.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    new EmptyBorder(6, 6, 6, 6)
            ));
            questionText.setEditable(true);
            questionText.setFocusable(true);
            add(new JScrollPane(questionText), BorderLayout.CENTER);

            JPanel optionsWrapper = new JPanel(new BorderLayout());
            optionsWrapper.setOpaque(false);

            JPanel optionsGrid = new JPanel();
            optionsGrid.setLayout(new GridBagLayout());
            optionsGrid.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            for (int i = 0; i < 4; i++) {
                gbc.gridy = i;
                gbc.gridx = 0;
                gbc.weightx = 0;
                JLabel idxLabel = new JLabel((i + 1) + ")");
                optionsGrid.add(idxLabel, gbc);

                gbc.gridx = 1;
                gbc.weightx = 1.0;
                JTextField tf = new JTextField();
                tf.setColumns(30);
                tf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        new EmptyBorder(4, 6, 4, 6)
                ));
                tf.setEditable(true);
                tf.setEnabled(true);
                tf.setFocusable(true);
                // ensure clicks set focus reliably
                tf.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        tf.requestFocusInWindow();
                    }
                });

                optionFields[i] = tf;
                optionsGrid.add(tf, gbc);
            }

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
            bottom.setOpaque(false);
            bottom.add(optionsGrid);

            correctIndexCombo = new JComboBox<>(new String[]{"0", "1", "2", "3"});
            bottom.add(new JLabel("Correct Option Index"));
            bottom.add(correctIndexCombo);

            optionsWrapper.add(bottom, BorderLayout.CENTER);
            add(optionsWrapper, BorderLayout.SOUTH);

            if (prefill != null) {
                questionText.setText(prefill.getText());
                List<String> opts = prefill.getOptions();
                for (int i = 0; i < 4 && i < opts.size(); i++) optionFields[i].setText(opts.get(i));
                int idx = prefill.getCorrectOptionIndex();
                if (idx >= 0 && idx < 4) correctIndexCombo.setSelectedIndex(idx);
            }

            SwingUtilities.invokeLater(() -> {
                this.revalidate();
                this.repaint();
            });
        }

        public Question toQuestion() {
            String qText = questionText.getText().trim();
            if (qText.isEmpty()) {
                JOptionPane.showMessageDialog(AddQuizFrame.this, "Question text cannot be empty.", "Validation", JOptionPane.ERROR_MESSAGE);
                questionText.requestFocusInWindow();
                return null;
            }
            List<String> opts = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                String o = optionFields[i].getText().trim();
                if (o.isEmpty()) {
                    JOptionPane.showMessageDialog(AddQuizFrame.this, "All option fields must be filled.", "Validation", JOptionPane.ERROR_MESSAGE);
                    optionFields[i].requestFocusInWindow();
                    return null;
                }
                opts.add(o);
            }
            int correctIdx = correctIndexCombo.getSelectedIndex();
            Question q = new Question();
            q.setQuestionId(UUID.randomUUID().toString());
            q.setText(qText);
            q.setOptions(opts);
            q.setCorrectOptionIndex(correctIdx);
            return q;
        }

        public void requestFocusForFirstField() {
            SwingUtilities.invokeLater(() -> {
                if (optionFields[0] != null) optionFields[0].requestFocusInWindow();
                else questionText.requestFocusInWindow();
            });
        }
    }
}
