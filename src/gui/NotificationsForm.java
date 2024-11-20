package gui;

import model.Actor;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotificationsForm extends JDialog {
    public NotificationsForm(JFrame owner, User user) {
        super(owner, true);
        setUndecorated(true);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        exitButton.addMouseListener(GUI.createMouseHover(exitButton, Color.WHITE, Color.LIGHT_GRAY));

        GUI.fixFont(nothingLabel);

        displayNotifications(user);

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
    private JPanel displayPanel;
    private JScrollBar scrollBar;
    private JScrollPane scrollPane;
    private JButton exitButton;
    private JPanel notificationsPanel;
    private JLabel nothingLabel;

    private void createUIComponents() {
        panel = new JPanel();
        displayPanel = new JPanel();
        scrollBar = new JScrollBar();
        scrollPane = new JScrollPane();
        exitButton = new JButton();
        notificationsPanel = new JPanel();
        nothingLabel = new JLabel();

        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        scrollBar.setUI(new MainForm.NoArrowScrollBarUI());
    }

    public void displayNotifications(User user) {
        scrollPane.getViewport().removeAll();
        notificationsPanel.removeAll();
        notificationsPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.PAGE_AXIS));
        int index = 0;
        for (String notification : user.getNotifications()) {
            JSeparator separator = new JSeparator();
            separator.setForeground(Color.BLACK);
            NotificationForm notificationForm = new NotificationForm(notification, user, this);
            notificationsPanel.add(notificationForm);
            if (++index < user.getNotifications().size()) {
                notificationsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                notificationsPanel.add(separator);
                notificationsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        if (user.getNotifications().isEmpty()) {
            notificationsPanel.add(nothingLabel);
            nothingLabel.setVisible(true);
        }
        scrollPane.setViewportView(notificationsPanel);
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}
