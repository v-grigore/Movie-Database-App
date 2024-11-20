package gui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestViewForm extends JPanel {
    public RequestViewForm(Request request, RequestsForm requestsForm, int index, JFrame appFrame, User user) {
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = "Are you sure you want to remove request " + (index + 1) + "# ?";
                ConfirmDeleteForm confirmDeleteForm = new ConfirmDeleteForm(appFrame, text);
                confirmDeleteForm.setVisible(true);

                if (confirmDeleteForm.isConfirmed()) {
                    ((RequestsManager) IMDB.getInstance().getActiveUser()).removeRequest(request);
                    requestsForm.displayNotifications(user, appFrame);
                }
            }
        });
        deleteButton.addMouseListener(GUI.createMouseHover(deleteButton, Color.WHITE, Color.LIGHT_GRAY));

        GUI.fixFont(typeLabel);
        GUI.fixFont(nameLabel);
        GUI.fixFont(descriptionTextArea);
        GUI.fixFont(dateLabel);

        typeLabel.setText((index + 1) + "# " + request.getRequestType());
        descriptionTextArea.setText(request.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = request.getSubmissionDate().format(formatter);
        dateLabel.setText("Sent on " + formattedDateTime);

        if (request.getRequestType() == RequestTypes.ACTOR_ISSUE || request.getRequestType() == RequestTypes.MOVIE_ISSUE) {
            nameLabel.setText(request.getName());
            nameLabel.setVisible(true);
        }

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }
    private JPanel panel;
    private JButton deleteButton;
    private JLabel typeLabel;
    private JLabel nameLabel;
    private JTextArea descriptionTextArea;
    private JLabel dateLabel;

    private void createUIComponents() {
        panel = new JPanel();
        deleteButton = new JButton();
        typeLabel = new JLabel();
        nameLabel = new JLabel();
        descriptionTextArea = new JTextArea();
        dateLabel = new JLabel();

        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
