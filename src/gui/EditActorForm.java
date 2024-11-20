package gui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class EditActorForm extends JDialog {
    public EditActorForm(JFrame owner, Actor actor) {
        this(owner);
        this.actor = actor;

        URL photoURL = getClass().getResource("/actors/" + actor.getId() + ".png");
        if (photoURL != null)
            photoButton.setIcon(new ImageIcon(photoURL));

        nameTextField.setText(actor.getName());
        biographyTextArea.setText(actor.getBiography());

        StringBuilder string = new StringBuilder();
        for (Pair<String, ProductionType> pair : actor.getFilmography()) {
            string.append(pair.first).append("\n");
        }
        moviesTextArea.setText(string.toString());
    }
    private JPanel panel;
    private JPanel displayPanel;
    private JTextField nameTextField;
    private JTextArea biographyTextArea;
    private JButton photoButton;
    private JLabel nameLabel;
    private JLabel biographyLabel;
    private JTextArea moviesTextArea;
    private JLabel moviesLabel;
    private JButton saveButton;
    private JButton cancelButton;
    private JScrollBar biographyScrollBar;
    private JScrollBar moviesScrollBar;
    private JScrollPane biographyScrollPane;
    private JScrollPane moviesScrollPane;
    private Actor actor;

    public EditActorForm(JFrame owner) {
        super(owner, true);
        setUndecorated(true);

        saveButton.addMouseListener(GUI.createMouseHover(saveButton, new Color(50, 205, 50), new Color(39, 155,39)));
        cancelButton.addMouseListener(GUI.createMouseHover(cancelButton, new Color(211, 211, 211), Color.LIGHT_GRAY));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                boolean hasContributor = !actor.getContributor().equals("ADMIN");

                String name = nameTextField.getText();
                String bio = biographyTextArea.getText();
                ArrayList<String> movieTitles = new ArrayList<>(Arrays.asList(moviesTextArea.getText().split("\n")));
                ArrayList<Pair<String, ProductionType>> filmography = new ArrayList<>();
                for (String title : movieTitles) {
                    Production production = IMDB.getInstance().findProduction(title);
                    if (production != null) {
                        if (production instanceof Movie)
                            filmography.add(new Pair<>(title, ProductionType.Movie));
                        else
                            filmography.add(new Pair<>(title, ProductionType.Series));
                    }
                }

                actor = new Actor(name, bio, filmography);

                if (hasContributor)
                    actor.setContributor(IMDB.getInstance().getActiveUser().getUsername());
            }
        });

        GUI.fixFont(nameLabel);
        GUI.fixFont(nameTextField);
        GUI.fixFont(biographyLabel);
        GUI.fixFont(biographyTextArea);
        GUI.fixFont(moviesLabel);
        GUI.fixFont(moviesTextArea);
        GUI.fixFont(saveButton);
        GUI.fixFont(cancelButton);

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
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        biographyLabel = new JLabel();
        biographyTextArea = new JTextArea();
        photoButton = new JButton();
        moviesLabel = new JLabel();
        moviesTextArea = new JTextArea();
        saveButton = new JButton();
        cancelButton = new JButton();
        biographyScrollPane = new JScrollPane();
        biographyScrollBar = new JScrollBar();
        moviesScrollPane = new JScrollPane();
        moviesScrollBar = new JScrollBar();

        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        biographyScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        moviesScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
    }

    public Actor getActor() {
        return actor;
    }
}
