package net.tim.quiz;

import net.tim.gui.panel.QuizPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizQuestion extends JPanel {
    private final String question;
    private final String[] answers;
    private final String correctAnswer;

    public QuizQuestion(String question, String[] answers, String correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel questionLabel = new JLabel("<html>" + question + "</html>");
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionLabel.setHorizontalAlignment(JLabel.CENTER);
        add(questionLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2));


        for (String answer : answers) {
            JButton answerButton = new JButton(answer);
            answerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (Component component : buttonPanel.getComponents()) {
                        if (component instanceof JButton button) {
                            if (button.getText().equals(correctAnswer)) {
                                button.setBackground(Color.GREEN);
                                if (e.getSource() == button) {
                                    ((QuizPanel) getParent()).incrementCorrectAnswersCount();
                                }
                            }
                        }
                    }

                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            QuizPanel quizPanel = (QuizPanel) getParent();
                            quizPanel.displayQuestion(quizPanel.getCurrentQuestionIndex() + 1);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            });
            buttonPanel.add(answerButton);
        }

        add(buttonPanel);
    }
}