package gui;

import model.Actor;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ActorSimpleForm extends JPanel {
    public ActorSimpleForm(Actor actor) {
        GUI.fixFont(name);

        URL photoURL = getClass().getResource("/actors/" + actor.getId() + ".png");
        if (photoURL != null)
            photo.setIcon(new ImageIcon(photoURL));
        name.setText(actor.getName());

        this.setLayout(new BorderLayout());
        this.add(panel);
    }
    private JPanel panel;
    private JLabel photo;
    private JLabel name;

    private void createUIComponents() {
        panel = new JPanel();
        photo = new JLabel();
        name = new JLabel();
    }
}
