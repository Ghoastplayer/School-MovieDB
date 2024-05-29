package net.tim.gui.panel;

import net.tim.gui.Frame;
import net.tim.quiz.QuizQuestion;
import net.tim.quiz.QuizQuestionGenerator;
import net.tim.db.DB_Manager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuizPanel extends JPanel {
    private JButton startButton;
    private final JButton backButton;
    private JTextField numQuestionsField;
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;
    private List<QuizQuestion> questions;
    private final QuizQuestionGenerator quizQuestionGenerator;

    public QuizPanel(DB_Manager dbManager) {
        setLayout(new BorderLayout());
        quizQuestionGenerator = new QuizQuestionGenerator(dbManager);
        backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            frame.switchPanel("MenuPanel");
        });
        add(backButton, BorderLayout.SOUTH);
        displayStartScreen();
    }

    public void displayStartScreen() {
        removeAll();
        add(backButton, BorderLayout.SOUTH);
        JPanel startPanel = new JPanel();
        startButton = new JButton("Start Quiz");
        numQuestionsField = new JTextField(5);

        startButton.addActionListener(e -> {
            startPanel.setVisible(false);
            int numQuestions = Integer.parseInt(numQuestionsField.getText());
            try {
                questions = quizQuestionGenerator.getQuestions(numQuestions);
                displayQuestion(0);
            }catch (IllegalStateException ise){
                startPanel.setVisible(true);
                JOptionPane.showMessageDialog(this, "Pleas hang on a moment, the database is still loading.");
            }catch (IllegalArgumentException iae) {
                startPanel.setVisible(true);
                JOptionPane.showMessageDialog(this, "Please hang on a moment, the database is still loading " + numQuestions + " questions.");
                quizQuestionGenerator.generateXQuestions(numQuestions);
            }

        });

        startPanel.add(startButton);
        startPanel.add(numQuestionsField);

        add(startPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void displayQuestion(int questionIndex) {
        if (questionIndex < questions.size()) {
            removeAll();
            add(backButton, BorderLayout.SOUTH);
            QuizQuestion question = questions.get(questionIndex);
            add(question, BorderLayout.CENTER);
            revalidate();
            repaint();
            currentQuestionIndex = questionIndex;
        } else {
            JOptionPane.showMessageDialog(this, "Quiz finished! You got " + correctAnswersCount + " out of " + questions.size() + " correct.");
            displayStartScreen();
        }
    }

    public void incrementCorrectAnswersCount() {
        correctAnswersCount++;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
}