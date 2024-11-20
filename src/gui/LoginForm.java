package gui;

import model.IMDB;
import model.User;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class LoginForm {
    public LoginForm(GUI gui) throws HeadlessException {
        emailField.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());
        Font font = new Font(logo.getFont().getName(), Font.PLAIN, 144);
        logo.setFont(font);
        this.gui = gui;
    }

    private JPanel panel;
    private JLabel logo;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel invalidTextPopup;
    private GUI gui;

    public JPanel getPanel() {
        return panel;
    }

    public void setupInvalidPopup(boolean value) {
        invalidTextPopup.setVisible(value);
    }

    private void login() {
        String email = emailField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        User user = IMDB.getInstance().findUser(email, password);

        if (user != null) {
            IMDB.getInstance().setActiveUser(user);
            invalidTextPopup.setVisible(false);
            gui.run();
        } else {
            invalidTextPopup.setVisible(true);
        }

        emailField.setText("");
        passwordField.setText("");
    }

    private void createUIComponents() {
        logo = new JLabel();
    }
}
