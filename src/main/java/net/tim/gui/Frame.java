package net.tim.gui;

import net.tim.db.DB_Manager;
import net.tim.gui.panel.MenuPanel;
import net.tim.gui.panel.MovieSearchPanel;
import net.tim.gui.panel.PersonSearchPanel;
import net.tim.gui.panel.QuizPanel;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Frame extends JFrame {

    DB_Manager dbManager;

    {
        Properties props = new Properties();
        try {
            FileInputStream in = new FileInputStream("src/main/resources/db.properties");
            props.load(in);
            in.close();
        } catch (IOException e) {
            System.out.println("Error reading from properties file");
            e.printStackTrace();
        }

        dbManager = new DB_Manager(
                props.getProperty("db.host"),
                Integer.parseInt(props.getProperty("db.port")),
                props.getProperty("db.name"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }

    MenuPanel menuPanel = new MenuPanel(this);
    MovieSearchPanel movieSearchPanel = new MovieSearchPanel(dbManager);
    PersonSearchPanel personSearchPanel = new PersonSearchPanel(dbManager);
    QuizPanel quizPanel = new QuizPanel(dbManager);


    public Frame() {
        super("Movie Explorer");
        setLayout(new CardLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(menuPanel, "MenuPanel");
        add(movieSearchPanel, "MovieSearchPanel");
        add(personSearchPanel, "PersonSearchPanel");
        add(quizPanel, "QuizPanel");

        switchPanel("MenuPanel");
        pack();
        setVisible(true);
    }

    public void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) this.getContentPane().getLayout();
        cardLayout.show(this.getContentPane(), panelName);
    }
}
