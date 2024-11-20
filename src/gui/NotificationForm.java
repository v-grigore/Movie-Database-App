package gui;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotificationForm extends JPanel {
    public NotificationForm(String string, User user, NotificationsForm notificationsForm) {
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user.deleteNotification(string);
                notificationsForm.displayNotifications(user);
            }
        });
        exitButton.addMouseListener(GUI.createMouseHover(exitButton, Color.WHITE, Color.LIGHT_GRAY));

        GUI.fixFont(notification);

        notification.setText(string);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }
    private JPanel panel;
    private JButton exitButton;
    private JLabel notification;

    private void createUIComponents() {
        panel = new JPanel();
        exitButton = new JButton();
        notification = new JLabel();

        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
