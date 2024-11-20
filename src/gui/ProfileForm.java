package gui;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileForm extends JDialog {
    public ProfileForm(JFrame owner, User user) {
        super(owner, true);
        setUndecorated(true);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        exitButton.addMouseListener(GUI.createMouseHover(exitButton, Color.WHITE, Color.LIGHT_GRAY));

        GUI.fixFont(nameLabel);
        GUI.fixFont(nameText);
        GUI.fixFont(xpLabel);
        GUI.fixFont(xpText);
        GUI.fixFont(typeLabel);
        GUI.fixFont(typeText);

        nameText.setText(user.getUsername());
        xpText.setText(user.getXp() != -1 ? user.getXp() + "" : "-");
        typeText.setText(user.getAccountType() + "");

        displayPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        nameText.setBorder(new EmptyBorder(0, 10, 0 ,0));
        xpText.setBorder(new EmptyBorder(0, 10, 0 ,0));
        typeText.setBorder(new EmptyBorder(0, 10, 0 ,0));

        panel.setBackground(new Color(0, 0, 0, 150));
        panel.setPreferredSize(new Dimension(owner.getWidth(), owner.getHeight()));
        panel.revalidate();
        panel.repaint();

        getContentPane().add(panel);
        setBackground(new Color(0, 0, 0, 150));
        pack();
        setLocationRelativeTo(owner);
    }
    private JPanel panel;
    private JLabel nameLabel;
    private JLabel nameText;
    private JLabel xpLabel;
    private JLabel xpText;
    private JLabel typeLabel;
    private JLabel typeText;
    private JPanel displayPanel;
    private JButton exitButton;

    private void createUIComponents() {
        panel = new JPanel();
        displayPanel = new JPanel();
        nameLabel = new JLabel();
        nameText = new JLabel();
        xpLabel = new JLabel();
        xpText = new JLabel();
        typeLabel = new JLabel();
        typeText = new JLabel();
        exitButton = new JButton();

        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
