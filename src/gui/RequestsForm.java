package gui;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RequestsForm extends JDialog {
    public RequestsForm(JFrame owner, User user) {
        super(owner, true);
        setUndecorated(true);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        exitButton.addMouseListener(GUI.createMouseHover(exitButton, Color.WHITE, Color.LIGHT_GRAY));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RequestCreateForm requestCreateForm = new RequestCreateForm(owner);
                requestCreateForm.setVisible(true);
                displayNotifications(user, owner);
                revalidate();
                repaint();
            }
        });
        addButton.addMouseListener(GUI.createMouseHover(addButton, Color.WHITE, Color.LIGHT_GRAY));

        GUI.fixFont(nothingLabel);

        displayNotifications(user, owner);

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
    private JScrollPane scrollPane;
    private JButton exitButton;
    private JScrollBar scrollBar;
    private JPanel requestsPanel;
    private JLabel nothingLabel;
    private JButton addButton;

    private void createUIComponents() {
        panel = new JPanel();
        displayPanel = new JPanel();
        scrollPane = new JScrollPane();
        exitButton = new JButton();
        scrollBar = new JScrollBar();
        requestsPanel = new JPanel();
        nothingLabel = new JLabel();
        addButton = new JButton();

        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        scrollBar.setUI(new MainForm.NoArrowScrollBarUI());
    }

    public void displayNotifications(User user, JFrame owner) {
        ArrayList<Request> requests = IMDB.getInstance().getMySentRequests();

        scrollPane.getViewport().removeAll();
        requestsPanel.removeAll();
        requestsPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.PAGE_AXIS));
        int index = 0;
        for (Request request : requests) {
            JSeparator separator = new JSeparator();
            separator.setForeground(Color.BLACK);
            RequestViewForm requestViewForm = new RequestViewForm(request, this, index, owner, user);
            requestsPanel.add(requestViewForm);
            if (++index < requests.size()) {
                requestsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                requestsPanel.add(separator);
                requestsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        if (requests.isEmpty()) {
            requestsPanel.add(nothingLabel);
            nothingLabel.setVisible(true);
        }
        scrollPane.setViewportView(requestsPanel);
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}
