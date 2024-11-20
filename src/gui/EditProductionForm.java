package gui;

import model.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class EditProductionForm extends JDialog {
    public EditProductionForm(JFrame owner, Production production) {
        this(owner);
        this.production = production;

        titleTextField.setText(production.getName());
        actorsTextArea.setText(String.join("\n", production.getActors()));
        directorsTextArea.setText(String.join("\n", production.getDirectors()));
        genresTextArea.setText(production.getGenres().stream().map(Genre::name).collect(Collectors.joining("\n")));
        plotTextArea.setText(production.getDescription());

        URL productionCard = getClass().getResource("/movies/" + production.getId() + ".jpg");
        if (productionCard != null)
            cardButton.setIcon(new ImageIcon(productionCard));

        if (production instanceof Movie) {
            movieButton.doClick();
            releaseYearSpinner.setValue(((Movie) production).getReleaseYear());
            durationSpinner.setValue(((Movie) production).getLength());
        }
        else {
            episodeMap = new TreeMap<>(((Series) production).getEpisodes());

            seriesButton.doClick();
            releaseYearSpinner.setValue(((Series) production).getReleaseYear());
            durationSpinner.setValue(((Series) production).getSeasonCount());

            DefaultListModel<String> listModel = new DefaultListModel<>();
            seasonList.setModel(listModel);

            System.out.println(((Series) production).getEpisodes().keySet());

            for (String season : ((Series) production).getEpisodes().keySet())
                listModel.addElement(season);

//            seasonList.revalidate();
//            seasonList.repaint();
        }
    }
    private JPanel panel;
    private JPanel displayPanel;
    private JTextField titleTextField;
    private JButton cardButton;
    private JLabel titleLabel;
    private JLabel typeLabel;
    private JLabel directorsLabel;
    private JLabel actorsLabel;
    private JLabel genresLabel;
    private JTextArea directorsTextArea;
    private JTextArea actorsTextArea;
    private JTextArea genresTextArea;
    private JScrollBar genresScrollBar;
    private JScrollBar actorsScrollBar;
    private JScrollBar directorsScrollBar;
    private JScrollPane directorsScrollPane;
    private JScrollPane genresScrollPane;
    private JScrollPane actorsScrollPane;
    private JLabel plotLabel;
    private JScrollPane plotScrollPane;
    private JTextArea plotTextArea;
    private JScrollBar plotScrollBar;
    private JLabel releaseYearLabel;
    private JSpinner releaseYearSpinner;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel durationLabel;
    private JSpinner durationSpinner;
    private JLabel episodesLabel;
    private JScrollPane seasonScrollPane;
    private JList<String> seasonList;
    private JTextArea episodesTextArea;
    private JScrollPane episodesScrollPane;
    private JScrollBar episodesScrollBar;
    private JScrollBar seasonScrollBar;
    private JRadioButton movieButton;
    private JRadioButton seriesButton;
    private JLabel seasonLabel;
    private JTextField seasonTextField;
    private JButton addButton;
    private JButton removeButton;
    private JScrollPane durationScrollPane;
    private JTextArea durationTextArea;
    private JScrollBar durationScrollBar;
    private Production production;
    private TreeMap<String, ArrayList<Episode>> episodeMap = new TreeMap<>();

    public EditProductionForm(JFrame owner) {
        super(owner, true);
        setUndecorated(true);

        saveButton.addMouseListener(GUI.createMouseHover(saveButton, new Color(50, 205, 50), new Color(39, 155,39)));
        cancelButton.addMouseListener(GUI.createMouseHover(cancelButton, new Color(211, 211, 211), Color.LIGHT_GRAY));
        addButton.addMouseListener(GUI.createMouseHover(addButton, Color.WHITE, Color.LIGHT_GRAY));
        removeButton.addMouseListener(GUI.createMouseHover(removeButton, Color.WHITE, Color.LIGHT_GRAY));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.out.println(getProduction().getContributor() + "mihaitapiticu");

                String title = titleTextField.getText();
                ArrayList<String> directors = new ArrayList<>(Arrays.asList(directorsTextArea.getText().split("\n")));
                ArrayList<String> actors = new ArrayList<>(Arrays.asList(actorsTextArea.getText().split("\n")));
                ArrayList<Genre> genres = Arrays.stream(genresTextArea.getText().split("\n"))
                        .map(Genre::valueOf)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                String plot = plotTextArea.getText();
                int releaseYear = (int) releaseYearSpinner.getValue();

                boolean hasContributor = !production.getContributor().equals("ADMIN");

                if (movieButton.isSelected()) {
                    int duration = (int) durationSpinner.getValue();

                    production = new Movie(title, directors, actors, genres, production.getRatings(), plot,
                            production.getIMDBRating(), duration, releaseYear);
                    if (hasContributor)
                        production.setContributor(IMDB.getInstance().getActiveUser().getUsername());
                    return;
                }

                production = new Series(title, directors, actors, genres, production.getRatings(), plot,
                        production.getIMDBRating(), releaseYear, episodeMap.size(), episodeMap);
                if (hasContributor)
                    production.setContributor(IMDB.getInstance().getActiveUser().getUsername());
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        seasonList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String value = seasonList.getSelectedValue();
                if (episodeMap.containsKey(value)) {
                    StringBuilder string1 = new StringBuilder();
                    StringBuilder string2 = new StringBuilder();
                    for (Episode episode : episodeMap.get(value)) {
                        string1.append(episode.getName()).append("\n");
                        string2.append(episode.getLength()).append("\n");
                    }
                    episodesTextArea.setText(string1.toString());
                    durationTextArea.setText(string2.toString());
                }
                else {
                    episodesTextArea.setText("");
                    durationTextArea.setText("\n");
                }
                seasonTextField.setText(value);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<String> listModel = (DefaultListModel<String>) seasonList.getModel();
                listModel.addElement("Season " + (listModel.size() + 1));
                seasonList.setSelectedIndex(listModel.getSize() - 1);
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<String> listModel = (DefaultListModel<String>) seasonList.getModel();
                int selectedIndex = seasonList.getSelectedIndex();

                if (listModel.size() > 1 && selectedIndex != -1) {
                    episodeMap.remove(listModel.get(selectedIndex));
                    if (selectedIndex > 0)
                        seasonList.setSelectedIndex(selectedIndex - 1);
                    else
                        seasonList.setSelectedIndex(selectedIndex + 1);
                    listModel.removeElementAt(selectedIndex);
                }
            }
        });
        seasonTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<String> listModel = (DefaultListModel<String>) seasonList.getModel();
                int selectedIndex = seasonList.getSelectedIndex();
                String string = seasonTextField.getText();

                if (selectedIndex != -1 && !string.isEmpty()) {
                    episodeMap.remove(listModel.get(selectedIndex));
                    listModel.set(selectedIndex, string);

                    ArrayList<String> names = new ArrayList<>(Arrays.asList(episodesTextArea.getText().split("\n")));
                    ArrayList<Integer> numbersArrayList = Arrays.stream(durationTextArea.getText().split("\n"))
                            .map(Integer::parseInt)
                            .collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<Episode> episodesList = new ArrayList<>();

                    if (names.size() == numbersArrayList.size()) {
                        for (int i = 0; i < names.size(); i++) {
                            String name = names.get(i);
                            int number = numbersArrayList.get(i);
                            Episode episode = new Episode(name, number);
                            episodesList.add(episode);
                        }
                    }

                    episodeMap.put(string, episodesList);
                }
            }
        });
        movieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                durationLabel.setEnabled(true);
                durationSpinner.setEnabled(true);

                episodesLabel.setEnabled(false);
                episodesTextArea.setEnabled(false);
                episodesScrollPane.setEnabled(false);
                seasonList.setEnabled(false);
                seasonLabel.setEnabled(false);
                seasonTextField.setEnabled(false);
                seasonScrollPane.setEnabled(false);
                durationTextArea.setEnabled(false);
                durationScrollPane.setEnabled(false);
                addButton.setEnabled(false);
                removeButton.setEnabled(false);
            }
        });
        seriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                durationLabel.setEnabled(false);
                durationSpinner.setEnabled(false);

                episodesLabel.setEnabled(true);
                episodesTextArea.setEnabled(true);
                episodesScrollPane.setEnabled(true);
                seasonList.setEnabled(true);
                seasonLabel.setEnabled(true);
                seasonTextField.setEnabled(true);
                seasonScrollPane.setEnabled(true);
                durationTextArea.setEnabled(true);
                durationScrollPane.setEnabled(true);
                addButton.setEnabled(true);
                removeButton.setEnabled(true);
            }
        });

        GUI.fixFont(titleLabel);
        GUI.fixFont(titleTextField);
        GUI.fixFont(directorsLabel);
        GUI.fixFont(directorsTextArea);
        GUI.fixFont(actorsLabel);
        GUI.fixFont(actorsTextArea);
        GUI.fixFont(genresLabel);
        GUI.fixFont(genresTextArea);
        GUI.fixFont(typeLabel);
        GUI.fixFont(movieButton);
        GUI.fixFont(seriesButton);
        GUI.fixFont(plotLabel);
        GUI.fixFont(plotTextArea);
        GUI.fixFont(releaseYearLabel);
        GUI.fixFont(releaseYearSpinner);
        GUI.fixFont(durationLabel);
        GUI.fixFont(durationSpinner);
        GUI.fixFont(episodesLabel);
        GUI.fixFont(seasonList);
        GUI.fixFont(episodesTextArea);
        GUI.fixFont(saveButton);
        GUI.fixFont(cancelButton);
        GUI.fixFont(seasonLabel);
        GUI.fixFont(seasonTextField);
        GUI.fixFont(durationTextArea);

        ((AbstractDocument) durationTextArea.getDocument()).setDocumentFilter(new NumericWithNewlineFilter());

        episodeMap.put("Season 1", new ArrayList<>());
        episodeMap.get("Season 1").add(new Episode("Name", 1));

        panel.setBackground(new Color(0, 0, 0, 150));
        panel.setPreferredSize(new Dimension(owner.getWidth(), owner.getHeight()));
        panel.revalidate();
        panel.repaint();

        getContentPane().add(panel);
        setBackground(new Color(0, 0, 0, 150));
        pack();
        setLocationRelativeTo(owner);
    }

    private void createUIComponents() {
        panel = new JPanel();
        displayPanel = new JPanel();
        cardButton = new JButton();
        titleLabel = new JLabel();
        titleTextField = new JTextField();
        typeLabel = new JLabel();
        movieButton = new JRadioButton();
        seriesButton = new JRadioButton();
        directorsLabel = new JLabel();
        directorsScrollBar = new JScrollBar();
        directorsScrollPane = new JScrollPane();
        directorsTextArea = new JTextArea();
        actorsLabel = new JLabel();
        actorsScrollBar = new JScrollBar();
        actorsScrollPane = new JScrollPane();
        actorsTextArea = new JTextArea();
        genresLabel = new JLabel();
        genresScrollBar = new JScrollBar();
        genresScrollPane = new JScrollPane();
        genresTextArea = new JTextArea();
        plotLabel = new JLabel();
        plotScrollBar = new JScrollBar();
        plotScrollPane = new JScrollPane();
        plotTextArea = new JTextArea();
        SpinnerNumberModel numberModel = new SpinnerNumberModel(0, -1, 2024, 1);
        releaseYearLabel = new JLabel();
        releaseYearSpinner = new JSpinner(numberModel);
        SpinnerNumberModel numberModel2 = new SpinnerNumberModel(0, -1, 9999, 1);
        durationLabel = new JLabel();
        durationSpinner = new JSpinner(numberModel2);
        episodesLabel = new JLabel();
        episodesScrollBar = new JScrollBar();
        episodesScrollPane = new JScrollPane();
        episodesTextArea = new JTextArea();
        seasonList = new JList<>();
        seasonScrollBar = new JScrollBar();
        seasonScrollPane = new JScrollPane();
        saveButton = new JButton();
        cancelButton = new JButton();
        seasonLabel = new JLabel();
        seasonTextField = new JTextField();
        addButton = new JButton();
        removeButton = new JButton();
        durationScrollPane = new JScrollPane();
        durationTextArea = new JTextArea();
        durationScrollBar = new JScrollBar();

        cardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        movieButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        seriesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        seasonScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        episodesScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        plotScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        actorsScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        directorsScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        genresScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        durationScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(movieButton);
        buttonGroup.add(seriesButton);
    }

    private static class NumericWithNewlineFilter extends DocumentFilter {
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string.matches("[\\d\\n]+")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text.matches("[\\d\\n]+")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    public Production getProduction() {
        return production;
    }
}
