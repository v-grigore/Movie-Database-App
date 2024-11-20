package gui;

import javax.swing.*;
import java.awt.*;

public class UserForm {
    public UserForm(JList favorites, JLabel username, JLabel xp, JLabel name, JLabel country, JLabel age, JLabel gender,
                    JLabel birthdate, JLabel accountType) {
        this.favorites = favorites;
        this.username = username;
        this.xp = xp;
        this.name = name;
        this.country = country;
        this.age = age;
        this.gender = gender;
        this.birthdate = birthdate;
        this.accountType = accountType;
    }

    private JList favorites;
    private JLabel username;
    private JLabel xp;
    private JLabel name;
    private JLabel country;
    private JLabel age;
    private JLabel gender;
    private JLabel birthdate;
    private JLabel accountType;
    private JPanel panel;

    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}
