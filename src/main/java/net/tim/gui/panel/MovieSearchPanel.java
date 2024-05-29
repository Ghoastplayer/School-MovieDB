package net.tim.gui.panel;

import net.tim.db.DB_Manager;
import net.tim.db.entity.Movie;
import net.tim.gui.Frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class MovieSearchPanel extends JPanel {
    private final JTextField searchField;
    private final JButton searchButton;
    private final JButton resetButton;
    private final JTable resultsTable;
    private final JComboBox<String> searchTypeComboBox;
    private final JComboBox<String> operatorComboBox;
    private final DB_Manager dbManager;

    public MovieSearchPanel(DB_Manager dbManager) {
        this.dbManager = dbManager;

        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30)); // Increase the size of the text field
        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30)); // Increase the size of the search button
        resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(100, 30)); // Increase the size of the reset button

        // Add a combo box for search type
        searchTypeComboBox = new JComboBox<>(new String[]{"Title", "Actor", "Genre", "Director", "Year", "Length", "FSK"});
        searchTypeComboBox.setPreferredSize(new Dimension(100, 30)); // Increase the size of the combo box

        // Add a combo box for operator type
        operatorComboBox = new JComboBox<>(new String[]{"=", "<", ">"});
        operatorComboBox.setPreferredSize(new Dimension(50, 30)); // Increase the size of the combo box
        operatorComboBox.setEnabled(false); // Initially disabled

        ActionListener searchActionListener = e -> {
            // Handle search action
            String query = searchField.getText();
            String searchType = (String) searchTypeComboBox.getSelectedItem();
            String operator = (String) operatorComboBox.getSelectedItem();
            List<Movie> movies = switch (searchType) {
                case "Title" -> dbManager.getMoviesByTitle(query);
                case "Actor" -> dbManager.getMoviesByActor(query);
                case "Genre" -> dbManager.getMoviesByGenre(query);
                case "Director" -> dbManager.getMoviesByRegisseur(query);
                case "Year" -> dbManager.getMoviesByYear(operator, query);
                case "Length" -> dbManager.getMoviesByLength(operator, query);
                case "FSK" -> dbManager.getMoviesByFSK(operator, query);
                default -> dbManager.getAllMovies();
            };

            // Update the resultsTable with the search results
            if (movies != null) {
                fillTableWithMovies(movies);
            }
        };

        ActionListener resetActionListener = e -> {
            // Handle reset action
            searchField.setText("");
            fillTableWithMovies(dbManager.getAllMovies());
        };

        searchTypeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String searchType = (String) e.getItem();
                // Enable the operator combo box only for "Year", "Length", and "FSK"
                if (searchType.equals("Year") || searchType.equals("Length") || searchType.equals("FSK")) {
                    operatorComboBox.setEnabled(true);
                }
                else {
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

        resultsTable = new JTable(); // Initialize the table with a suitable table model
        resultsTable.getTableHeader().setReorderingAllowed(false); // Disable column reordering
        resultsTable.setDragEnabled(false); // Disable drag and drop

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


        fillTableWithMovies(dbManager.getAllMovies());
    }

    private void fillTableWithMovies(List<Movie> movies) {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Publication Date", "Title", "Length", "FSK"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This causes all cells to be not editable
            }
        };

        for (Movie movie : movies) {
            Object[] rowData = new Object[]{
                    movie.getId(),
                    movie.getPublicationDate(),
                    movie.getTitel(),
                    movie.getLength(),
                    movie.getFsk()
            };
            tableModel.addRow(rowData);
        }

        resultsTable.setModel(tableModel);

        TableColumnModel columnModel = resultsTable.getColumnModel();
        TableColumn column = columnModel.getColumn(2);
        column.setPreferredWidth(300); // Increase the width of the title column

    }
}