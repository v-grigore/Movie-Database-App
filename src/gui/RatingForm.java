package gui;

import model.Rating;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class RatingForm extends JPanel {
    public RatingForm(Rating rating) {
        starsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        GUI.fixFont(username);
        GUI.fixFont(score);
        GUI.fixFont(comment);

        username.setText(rating.getUsername());
        score.setText(rating.getScore() + "/10");
        comment.setText(rating.getComments());

        ImageIcon star = new ImageIcon(getClass().getResource("/star_icon_yellow_24x.png"));

        for (int i = 0; i < rating.getScore(); i++)
            starsPanel.add(new JLabel(star));

        starsPanel.add(score);
        panel.remove(score);
        score.setVisible(true);

        this.setLayout(new BorderLayout());
        this.add(panel);
    }
    private JPanel panel;
    private JLabel username;
    private JPanel starsPanel;
    private JTextArea comment;
    private JLabel score;

    private void createUIComponents() {
        panel = new JPanel();
        username = new JLabel();
        starsPanel = new JPanel();
        comment = new JTextArea();
        score = new JLabel();
    }
}
