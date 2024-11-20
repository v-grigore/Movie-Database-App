package gui;

import model.Production;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ProductionSimpleForm extends JPanel {
    public ProductionSimpleForm(Production production) {
        GUI.fixFont(name);

        URL photoURL = getClass().getResource("/movies/" + production.getId() + ".jpg");
        if (photoURL != null)
            photo.setIcon(new ImageIcon(photoURL));
        name.setText(production.getName());

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
