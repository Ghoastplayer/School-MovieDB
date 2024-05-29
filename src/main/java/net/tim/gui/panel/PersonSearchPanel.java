package net.tim.gui.panel;

import net.tim.db.DB_Manager;
import net.tim.db.entity.Person;
import net.tim.gui.Frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

public class PersonSearchPanel extends JPanel {

    private final DB_Manager dbManager;
    private final JTable resultsTable;

    public PersonSearchPanel(DB_Manager dbManager) {
        this.dbManager = dbManager;
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30)); // Increase the size of the text field
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30)); // Increase the size of the search button
        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(100, 30)); // Increase the size of the reset button

        // Add a combo box for search type
        JComboBox<String> searchTypeComboBox = new JComboBox<>(new String[]{"Name", "Birthdate", "Country", "Gender"});
        searchTypeComboBox.setPreferredSize(new Dimension(100, 30)); // Increase the size of the combo box

        // Add a combo box for operator type
        JComboBox<String> operatorComboBox = new JComboBox<>(new String[]{"=", "<", ">", "<=", ">="});
        operatorComboBox.setPreferredSize(new Dimension(50, 30)); // Increase the size of the combo box
        operatorComboBox.setEnabled(false); // Initially disabled

        ActionListener searchActionListener = e -> {
            String searchType = (String) searchTypeComboBox.getSelectedItem();
            String operator = (String) operatorComboBox.getSelectedItem();
            String query = searchField.getText();

            List<Person> persons = new ArrayList<>();

            switch (searchType) {
                case "Name" -> persons = dbManager.getPersonsByName(query);
                case "Birthdate" -> persons = dbManager.getPersonsByBirthdate(operator, query);
                case "Country" -> persons = dbManager.getPersonsByCountry(query);

                case "Gender" -> persons = dbManager.getPersonsByGender(query);
                default -> persons = dbManager.getAllPersons();
            }

            fillTableWithPersons(persons);
        };

        ActionListener resetActionListener = e -> {
            searchField.setText("");
            fillTableWithPersons(dbManager.getAllPersons());
        };

        searchTypeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String searchType = (String) e.getItem();
                if (searchType.equals("Birthdate")) {
                    operatorComboBox.setEnabled(true);
                } else {
                    operatorComboBox.setSelectedIndex(0);
                    operatorComboBox.setEnabled(false);
                }
            }
        });

        searchField.addActionListener(searchActionListener);
        searchButton.addActionListener(searchActionListener);
        resetButton.addActionListener(resetActionListener);

        searchPanel.add(searchField);
        searchPanel.add(searchTypeComboBox);
        searchPanel.add(operatorComboBox);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        resultsTable = new JTable();
        resultsTable.getTableHeader().setReorderingAllowed(false);
        resultsTable.setDragEnabled(false);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

        JPanel backButtonPanel = new JPanel();
        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 14)); // Change the font to Arial, bold, size 14
        backButton.setPreferredSize(new Dimension(150, 30)); // Change the size of the button
        backButton.addActionListener(e -> {
            // Switch to the menu panel
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            frame.switchPanel("MenuPanel");
        });
        backButtonPanel.add(backButton);

        northPanel.add(backButtonPanel);
        northPanel.add(searchPanel);

        add(northPanel, BorderLayout.NORTH);

        add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        fillTableWithPersons(dbManager.getAllPersons());
    }

    private void fillTableWithPersons(List<Person> persons) {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Firstname", "Lastname", "Birthdate", "Country", "Gender"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Person person : persons) {
            Object[] rowData = new Object[]{
                    person.getId(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getDateOfBirth(),
                    person.getCountry(),
                    person.getGender()
            };
            tableModel.addRow(rowData);
        }

        resultsTable.setModel(tableModel);
    }
}