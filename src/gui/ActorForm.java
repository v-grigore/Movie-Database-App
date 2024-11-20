package gui;

import model.Actor;
import model.IMDB;
import model.Staff;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class ActorForm extends JPanel {
    public ActorForm(Actor actor, JFrame appFrame, MainForm mainForm) {
        this();
        this.appFrame = appFrame;
        this.actor = actor;
        this.mainForm = mainForm;
        updatePanel();
    }

    private JFrame appFrame;
    private JLabel photo;
    private JTextPane biography;
    private JButton nameButton;
    private JPanel panel;
    private JButton favoriteButton;
    private JButton favoriteRemoveButton;
    private JPanel innerPanel;
    private JButton editButton;
    private JButton deleteButton;
    private MainForm mainForm;
    private Actor actor;

    public void updatePanel() {
        User activeUser = IMDB.getInstance().getActiveUser();

        URL actorPhoto = getClass().getResource("/actors/" + actor.getId() + ".png");
        if (actorPhoto != null)
            photo.setIcon(new ImageIcon(actorPhoto));

        nameButton.setText(actor.getName());
        biography.setText(actor.getBiography());

        biography.setCaret(null);

        if (activeUser.getFavorites().contains(actor)) {
            favoriteButton.setVisible(false);
            favoriteRemoveButton.setVisible(true);
        }

        if (IMDB.getInstance().getMyContributions().contains(actor)) {
            editButton.setVisible(true);
            deleteButton.setVisible(true);
        }

        FontMetrics fontMetrics = nameButton.getFontMetrics(nameButton.getFont());
        int textWidth = fontMetrics.stringWidth(nameButton.getText());
        int textHeight = fontMetrics.getHeight();
        Dimension preferredSize = new Dimension(textWidth + 10, textHeight + 10);
        nameButton.setPreferredSize(preferredSize);
        panel.revalidate();
        panel.repaint();
    }

    public ActorForm() {
        nameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                nameButton.setForeground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nameButton.setForeground(Color.BLACK);
            }
        });
        favoriteButton.addMouseListener(GUI.createMouseHover(favoriteButton, Color.WHITE, Color.LIGHT_GRAY));
        favoriteRemoveButton.addMouseListener(GUI.createMouseHover(favoriteRemoveButton, Color.WHITE, Color.LIGHT_GRAY));
        editButton.addMouseListener(GUI.createMouseHover(editButton, Color.WHITE, Color.LIGHT_GRAY));
        deleteButton.addMouseListener(GUI.createMouseHover(deleteButton, Color.WHITE, Color.LIGHT_GRAY));
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        favoriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IMDB.getInstance().getActiveUser().addToFavorites(actor);
                favoriteButton.setVisible(false);
                favoriteRemoveButton.setVisible(true);
            }
        });
        favoriteRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IMDB.getInstance().getActiveUser().removeFromFavorites(actor);
                favoriteRemoveButton.setVisible(false);
                favoriteButton.setVisible(true);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = "Are you sure you want to delete " + actor.getName() + "?";
                ConfirmDeleteForm confirmDeleteForm = new ConfirmDeleteForm(appFrame, text);
                confirmDeleteForm.setVisible(true);

                if (confirmDeleteForm.isConfirmed()) {
                    ((Staff) IMDB.getInstance().getActiveUser()).removeActorSystem(actor);
                    mainForm.removeActor(actor);
                    mainForm.refresh();
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditActorForm editActorForm = new EditActorForm(appFrame, actor);
                editActorForm.setVisible(true);
                handleEdit(editActorForm.getActor());
            }
        });
        nameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActorDetailsForm actorDetailsForm = new ActorDetailsForm(appFrame, actor);
                actorDetailsForm.setVisible(true);
            }
        });
        GUI.fixFont(biography);
        GUI.fixFont(nameButton);
        GUI.fixFont(favoriteButton);
        GUI.fixFont(favoriteRemoveButton);
        GUI.fixFont(editButton);
        GUI.fixFont(deleteButton);
    }

    private void createUIComponents() {
        innerPanel = new JPanel();
        panel = new JPanel();
        photo = new JLabel();
        biography = new JTextPane();
        nameButton = new JButton();
        favoriteButton = new JButton();
        favoriteRemoveButton = new JButton();
        editButton = new JButton();
        deleteButton = new JButton();
        favoriteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        favoriteRemoveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nameButton.setMargin(new Insets(0, 15, 0, 0));
        nameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void handleEdit(Actor newActor) {
        ((Staff) IMDB.getInstance().getActiveUser()).updateActor(this.actor, newActor);
        mainForm.replaceActor(this.actor, newActor);
        mainForm.refresh();
    }
}
