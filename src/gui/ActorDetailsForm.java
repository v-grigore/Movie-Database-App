package gui;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

public class ActorDetailsForm extends JDialog {
    public ActorDetailsForm(JFrame owner, Actor actor) {
        this(owner);

        URL photoURL = getClass().getResource("/actors/" + actor.getId() + ".png");
        if (photoURL != null)
            photo.setIcon(new ImageIcon(photoURL));

        name.setText(actor.getName());
        biography.setText(actor.getBiography());

        displayProductions(actor);

        panel.revalidate();
        panel.repaint();
    }
    private JPanel panel;
    private JLabel photo;
    private JLabel name;
    private JTextArea biography;
    private JPanel displayPanel;
    private JScrollBar movieScrollBar;
    private JScrollPane movieScrollPane;
    private JPanel movieDisplayPanel;
    private JButton exitButton;

    public ActorDetailsForm(JFrame owner) {
        super(owner, true);
        setUndecorated(true);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        exitButton.addMouseListener(GUI.createMouseHover(exitButton, Color.WHITE, Color.LIGHT_GRAY));

        GUI.fixFont(name);
        GUI.fixFont(biography);

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
        photo = new JLabel();
        name = new JLabel();
        biography = new JTextArea();
        displayPanel = new JPanel();
        movieScrollPane = new JScrollPane();
        movieDisplayPanel = new JPanel();
        movieScrollBar = new JScrollBar();
        exitButton = new JButton();

        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        movieScrollBar.setUI(new MainForm.NoArrowScrollBarUI());
    }

    public void displayProductions(Actor actor) {
        movieScrollPane.getViewport().removeAll();
        movieDisplayPanel.removeAll();
        movieDisplayPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        movieDisplayPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (Pair<String, ProductionType> pair : actor.getFilmography()) {
            Production production = IMDB.getInstance().findProduction(pair.first);
            if (production != null) {
                ProductionSimpleForm productionSimpleForm = new ProductionSimpleForm(production);
                movieDisplayPanel.add(productionSimpleForm);
            }
            else {
                production = new Movie(pair.first);
                ProductionSimpleForm productionSimpleForm = new ProductionSimpleForm(production);
                movieDisplayPanel.add(productionSimpleForm);
            }
        }
        movieScrollPane.setViewportView(movieDisplayPanel);
        movieScrollPane.revalidate();
        movieScrollPane.repaint();
    }
}
