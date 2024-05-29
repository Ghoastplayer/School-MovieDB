package net.tim.gui.panel;

import net.tim.gui.Frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class MenuPanel extends JPanel {
    Frame frame;
    JButton quizButton, searchMoviesButton, searchPersonsButton, exitButton;
    JButton[] buttons;
    int selectedButtonIndex = 0;

    public MenuPanel(Frame frame) {
        this.frame = frame;
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new GridBagLayout()); // Use GridBagLayout
        setPreferredSize(new Dimension(400, 400)); // do not use a fixed size

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        quizButton = createButton("Quiz", getClass().getResource("/icon/menu/quiz.png"), e -> {
            frame.switchPanel("QuizPanel");
        });
        searchMoviesButton = createButton("Search Movies", getClass().getResource("/icon/menu/search.png"), e -> {
            frame.switchPanel("MovieSearchPanel");
        });
        searchPersonsButton = createButton("Search Persons", getClass().getResource("/icon/menu/search.png"), e -> {
            frame.switchPanel("PersonSearchPanel");
        });
        exitButton = createButton("Exit", getClass().getResource("/icon/menu/exit.png"), e -> {
            System.exit(0);
        });

        buttons = new JButton[]{quizButton, searchMoviesButton, searchPersonsButton, exitButton};

        add(quizButton, gbc);
        add(searchMoviesButton, gbc);
        add(searchPersonsButton, gbc);
        add(exitButton, gbc);

        setupKeyboardListener();
    }

    private JButton createButton(String text, URL icon, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 20)); // Set font to plain
        button.setBackground(Color.LIGHT_GRAY); // Set background color
        button.setForeground(Color.BLACK); // Set text color
        button.setFocusPainted(false); // Remove focus border
        button.addActionListener(actionListener);

        // Set icon
        Image img = null;
        try {
            img = ImageIO.read(icon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image scaledImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImg));

        return button;
    }

    private void setupKeyboardListener() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down");
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "enter");

        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedButtonIndex = (selectedButtonIndex - 1 + buttons.length) % buttons.length;
                buttons[selectedButtonIndex].requestFocus();
            }
        });

        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedButtonIndex = (selectedButtonIndex + 1) % buttons.length;
                buttons[selectedButtonIndex].requestFocus();
            }
        });

        actionMap.put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttons[selectedButtonIndex].doClick();
            }
        });
    }
}