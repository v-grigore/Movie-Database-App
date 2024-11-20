package gui;

import model.IMDB;
import model.Production;
import model.Rating;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RateForm extends JDialog {
    public RateForm(JFrame owner, Production production) {
        this(owner);
        this.production = production;

        title.setText(production.getName());

        activeUser = IMDB.getInstance().getActiveUser();

        oldRating = production.findRating(activeUser.getUsername());

        if (oldRating != null) {
            score = oldRating.getScore();
            ratingLabel.setText(String.valueOf(score));
            comment.setVisible(true);
            comment.setText(oldRating.getComments());
            removeRatingButton.setVisible(true);

            for (int i = 0; i < score; i++)
                stars[i].setIcon(fullStarIcon);
        }
    }

    private JPanel panel;
    private JButton exitButton;
    private JPanel displayPanel;
    private JPanel starPanel;
    private JButton starExample;
    private JButton rateButton;
    private JButton removeRatingButton;
    private JButton[] stars = new JButton[10];
    private JTextArea comment;
    private JLabel title;
    private JLabel rateThisLabel;
    private JLabel ratingLabel;
    private User activeUser;
    private Rating oldRating;
    private int score = 0;
    private Production production;
    private ImageIcon emptyStarIcon = new ImageIcon(getClass().getResource("/star_icon_blue_32x.png"));
    private ImageIcon fullStarIcon = new ImageIcon(getClass().getResource("/star_icon_full_blue_32x.png"));
    private Color gold = new Color(255, 193, 7);
    private Color dimBlue = new Color(29, 80, 155);

    private MouseAdapter createStarHover(int index) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                for (int i = 0; i < 10; i++) {
                    if (i > index)
                        stars[i].setIcon(emptyStarIcon);
                    else
                        stars[i].setIcon(fullStarIcon);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                for (int i = 0; i < 10; i++) {
                    if (i < score)
                        stars[i].setIcon(fullStarIcon);
                    else
                        stars[i].setIcon(emptyStarIcon);
                }
            }
        };
    }

    private ActionListener createStarAction(int index) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                score = index + 1;
                ratingLabel.setText(String.valueOf(score));
                comment.setVisible(true);
                if (!rateButton.isEnabled()) {
                    rateButton.addMouseListener(GUI.createMouseHover(rateButton, gold, new Color(155, 106, 7)));
                    rateButton.setEnabled(true);
                    rateButton.setBackground(gold);
                    rateButton.setForeground(Color.BLACK);
                }
            }
        };
    }

    public RateForm(JFrame owner) {
        super(owner, true);
        setUndecorated(true);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        rateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Rating rating = new Rating(activeUser.getUsername(), score, comment.getText());
                IMDB.getInstance().addRating(rating, production);
            }
        });
        removeRatingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                IMDB.getInstance().removeRating(oldRating, production);
            }
        });
        exitButton.addMouseListener(GUI.createMouseHover(exitButton, Color.BLACK, Color.DARK_GRAY));
        removeRatingButton.addMouseListener(GUI.createMouseHover(removeRatingButton, Color.BLACK, dimBlue));

        for (int i = 0; i < 10; i++) {
            stars[i].addMouseListener(createStarHover(i));
            stars[i].addActionListener(createStarAction(i));
            starPanel.add(stars[i]);
        }

        GUI.fixFont(ratingLabel);
        GUI.fixFont(rateButton);
        GUI.fixFont(removeRatingButton);
        GUI.fixFont(title);
        GUI.fixFont(rateThisLabel);
        GUI.fixFont(comment);

        GUI.setupCharacterLimit(comment, 100);

        panel.setBackground(new Color(0, 0, 0, 150));
        panel.setPreferredSize(new Dimension(owner.getWidth(), owner.getHeight()));
        panel.revalidate();
        panel.repaint();

        getContentPane().add(panel);
        setBackground(new Color(0, 0, 0, 150));
        pack();
        setLocationRelativeTo(owner);
    }

    private void createUIComponents() {
        panel = new JPanel();
        displayPanel = new JPanel();
        starPanel = new JPanel();
        starExample = new JButton();
        exitButton = new JButton();
        rateButton = new JButton();
        removeRatingButton = new JButton();
        comment = new JTextArea();
        title = new JLabel();
        rateThisLabel = new JLabel();
        ratingLabel = new JLabel();
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeRatingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        for (int i = 0; i < 10; i++) {
            JButton jButton = new JButton(new ImageIcon(getClass().getResource("/star_icon_blue_32x.png")));
            jButton.setMaximumSize(new Dimension(40, 40));
            jButton.setPreferredSize(new Dimension(40, 40));
            jButton.setContentAreaFilled(false);
            jButton.setBorderPainted(false);
            jButton.setFocusPainted(false);
            jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            stars[i] = jButton;
        }
    }
}
