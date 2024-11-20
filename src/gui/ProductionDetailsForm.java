package gui;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

public class ProductionDetailsForm extends JDialog {
    public ProductionDetailsForm(JFrame owner, Production production) {
        this(owner);

        URL productionCard = getClass().getResource("/movies/" + production.getId() + ".jpg");
        if (productionCard != null)
            card.setIcon(new ImageIcon(productionCard));

        title.setText(production.getName());
        info.setText(ProductionForm.getInformation(production));
        plot.setText(production.getDescription());

        if (!production.getDirectors().isEmpty()) {
            StringBuilder directors = new StringBuilder(production.getDirectors().get(0));
            for (int i = 1; i < production.getDirectors().size(); i++) {
                directors.append(", ").append(production.getDirectors().get(i));
            }
            directorsTextField.setText(directors.toString());
        }

        if (!production.getGenres().isEmpty()) {
            StringBuilder genres = new StringBuilder(production.getGenres().get(0).toString());
            for (int i = 1; i < production.getGenres().size(); i++) {
                genres.append(", ").append(production.getGenres().get(i).toString());
            }
            genreTextField.setText(genres.toString());
        }

        displayActors(production);
        displayRatings(production);

        if (production instanceof Series)
            displayEpisodes((Series) production);
        else
            episodeScrollPane.setVisible(false);

        panel.revalidate();
        panel.repaint();
    }
    private JPanel panel;
    private JPanel displayPanel;
    private JLabel title;
    private JLabel card;
    private JTextArea plot;
    private JLabel directorsLabel;
    private JTextField info;
    private JTextField directorsTextField;
    private JLabel castLabel;
    private JScrollPane actorsScrollPane;
    private JPanel actorsDisplayPanel;
    private JScrollBar actorsScrollBar;
    private JTextField genreTextField;
    private JScrollPane ratingScrollPane;
    private JPanel ratingPanel;
    private JScrollBar ratingScrollBar;
    private JScrollPane episodeScrollPane;
    private JPanel episodePanel;
    private JScrollBar episodeScrollBar;
    private JPanel innerPanel;
    private JButton exitButton;
    private JLabel ratingLabel;
    private JTextArea episodeTextArea;

    public ProductionDetailsForm(JFrame owner) {
        super(owner, true);
        setUndecorated(true);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        exitButton.addMouseListener(GUI.createMouseHover(exitButton, Color.WHITE, Color.LIGHT_GRAY));

        GUI.fixFont(title);
        GUI.fixFont(info);
        GUI.fixFont(genreTextField);
        GUI.fixFont(plot);
        GUI.fixFont(directorsLabel);
        GUI.fixFont(directorsTextField);
        GUI.fixFont(castLabel);
        GUI.fixFont(ratingLabel);
        GUI.fixFont(episodeTextArea);

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
        innerPanel = new JPanel();
        displayPanel = new JPanel();
        ratingPanel = new JPanel();
        ratingScrollBar = new JScrollBar();
        ratingScrollPane = new JScrollPane();
        episodePanel = new JPanel();
        episodeScrollBar = new JScrollBar();
        episodeScrollPane = new JScrollPane();
        actorsDisplayPanel = new JPanel();
        actorsScrollBar = new JScrollBar();
        actorsScrollPane = new JScrollPane();
        title = new JLabel();
        info = new JTextField();
        card = new JLabel();
        plot = new JTextArea();
        directorsLabel = new JLabel();
        directorsTextField = new JTextField();
        castLabel = new JLabel();
        genreTextField = new JTextField();
        exitButton = new JButton();
        ratingLabel = new JLabel();
        episodeTextArea = new JTextArea();

        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actorsScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        episodeScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        ratingScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        info.setBorder(new EmptyBorder(0, 0, 0, 0));
        directorsTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
        genreTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    public void displayActors(Production production) {
        ArrayList<Actor> actors = new ArrayList<>();
        for (String string : production.getActors()) {
            Actor actor = IMDB.getInstance().findActor(string);
            if (actor != null) {
                actors.add(actor);
            }
        }

        actorsScrollPane.getViewport().removeAll();
        actorsDisplayPanel.removeAll();
        actorsDisplayPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        actorsDisplayPanel.setLayout(new BoxLayout(actorsDisplayPanel, BoxLayout.PAGE_AXIS));
        int index = 0;
        for (Actor actor : actors) {
            JSeparator separator = new JSeparator();
            separator.setForeground(Color.BLACK);
            ActorSimpleForm actorForm = new ActorSimpleForm(actor);
            actorsDisplayPanel.add(actorForm);
            if (++index < actors.size()) {
                actorsDisplayPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                actorsDisplayPanel.add(separator);
                actorsDisplayPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        actorsScrollPane.setViewportView(actorsDisplayPanel);
        actorsScrollPane.revalidate();
        actorsScrollPane.repaint();
    }

    public void displayRatings(Production production) {
        ArrayList<Rating> ratings = new ArrayList<>(production.getRatings());

        ratingScrollPane.getViewport().removeAll();
        ratingPanel.removeAll();
        ratingPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.PAGE_AXIS));
        int index = 0;
        for (Rating rating : ratings) {
            JSeparator separator = new JSeparator();
            separator.setForeground(Color.BLACK);
            RatingForm ratingForm = new RatingForm(rating);
            ratingPanel.add(ratingForm);
            if (++index < ratings.size()) {
                ratingPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                ratingPanel.add(separator);
                ratingPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        ratingScrollPane.setViewportView(ratingPanel);
        ratingScrollPane.revalidate();
        ratingScrollPane.repaint();
    }

    public void displayEpisodes(Series series) {
        StringBuilder string = new StringBuilder();
        for (String season : series.getEpisodes().keySet()) {
            string.append(season);
            for (Episode episode : series.getEpisodes().get(season)) {
                string.append("\n    '").append(episode.getName()).append("' : ").append(episode.getLength());
            }
            string.append("\n");
        }
        episodeTextArea.setText(string.toString());
    }
}
