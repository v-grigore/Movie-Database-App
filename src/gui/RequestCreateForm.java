package gui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;

public class RequestCreateForm extends JDialog {
    public RequestCreateForm(JFrame owner) {
        super(owner, true);
        setUndecorated(true);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        exitButton.addMouseListener(GUI.createMouseHover(exitButton, Color.WHITE, Color.LIGHT_GRAY));
        sendButton.addMouseListener(GUI.createMouseHover(sendButton, new Color(50, 205, 50), new Color(39, 155,39)));
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (type == RequestTypes.MOVIE_ISSUE && IMDB.getInstance().findProduction(nameTextField.getText()) == null)
                    return;

                if (type == RequestTypes.ACTOR_ISSUE && IMDB.getInstance().findActor(nameTextField.getText()) == null)
                    return;

                dispose();
                request = new Request(RequestTypes.valueOf(typeButton.getText()), nameTextField.getText(), descriptionTextArea.getText());
                ((RequestsManager) IMDB.getInstance().getActiveUser()).createRequest(request);
            }
        });
        typeButton.addMouseListener(GUI.createMouseHover(typeButton, Color.WHITE, Color.LIGHT_GRAY));
        typeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typePopup.show(typeButton, 0, typeButton.getHeight());
            }
        });

        GUI.fixFont(typeLabel);
        GUI.fixFont(typeButton);
        GUI.fixFont(typePopup);
        GUI.fixFont(nameLabel);
        GUI.fixFont(nameTextField);
        GUI.fixFont(descriptionLabel);
        GUI.fixFont(descriptionTextArea);
        GUI.fixFont(sendButton);

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
    private JLabel typeLabel;
    private JTextField nameTextField;
    private JScrollBar scrollBar;
    private JButton exitButton;
    private JLabel nameLabel;
    private JLabel descriptionLabel;
    private JTextArea descriptionTextArea;
    private JScrollPane scrollPane;
    private JButton sendButton;
    private JButton typeButton;
    private JPopupMenu typePopup;
    private Request request;
    private RequestTypes type = RequestTypes.DELETE_ACCOUNT;

    private void createUIComponents() {
        panel = new JPanel();
        displayPanel = new JPanel();
        typeButton = new JButton();
        typeLabel = new JLabel();
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        scrollPane = new JScrollPane();
        scrollBar = new JScrollBar();
        descriptionLabel = new JLabel();
        descriptionTextArea = new JTextArea();
        sendButton = new JButton();
        exitButton = new JButton();

        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        typeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        scrollBar.setUI(new MainForm.NoArrowScrollBarUI());
        setupTypePopup();
    }

    private void setupTypePopup() {
        typePopup = new JPopupMenu();
        typePopup.setBackground(Color.WHITE);
        JMenuItem menuItem0 = new JMenuItem("DELETE_ACCOUNT");
        JMenuItem menuItem1 = new JMenuItem("ACTOR_ISSUE");
        JMenuItem menuItem2 = new JMenuItem("MOVIE_ISSUE");
        JMenuItem menuItem3 = new JMenuItem("OTHERS");
        menuItem0.setBackground(Color.WHITE);
        menuItem1.setBackground(Color.WHITE);
        menuItem2.setBackground(Color.WHITE);
        menuItem3.setBackground(Color.WHITE);
        GUI.fixFont(menuItem0);
        GUI.fixFont(menuItem1);
        GUI.fixFont(menuItem2);
        GUI.fixFont(menuItem3);
        typePopup.add(menuItem0);
        typePopup.add(menuItem1);
        typePopup.add(menuItem2);
        typePopup.add(menuItem3);
        menuItem0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeButton.setText("DELETE_ACCOUNT");
                type = RequestTypes.DELETE_ACCOUNT;
                nameLabel.setVisible(false);
                nameTextField.setVisible(false);
            }
        });
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeButton.setText("ACTOR_ISSUE");
                type = RequestTypes.ACTOR_ISSUE;
                nameLabel.setVisible(true);
                nameTextField.setVisible(true);
            }
        });
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeButton.setText("MOVIE_ISSUE");
                type = RequestTypes.MOVIE_ISSUE;
                nameLabel.setVisible(true);
                nameTextField.setVisible(true);
            }
        });
        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeButton.setText("OTHERS");
                type = RequestTypes.OTHERS;
                nameLabel.setVisible(false);
                nameTextField.setVisible(false);
            }
        });
    }

    public Request getRequest() {
        return request;
    }
}
