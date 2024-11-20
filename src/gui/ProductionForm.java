package gui;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Locale;

public class ProductionForm extends JPanel {
    public ProductionForm(Production production, JFrame appFrame, MainForm mainForm) {
        this();
        this.appFrame = appFrame;
        this.production = production;
        this.mainForm = mainForm;
        updatePanel();
    }

    public void updatePanel() {
        User activeUser = IMDB.getInstance().getActiveUser();

        URL productionCard = getClass().getResource("/movies/" + production.getId() + ".jpg");
        if (productionCard != null)
            card.setIcon(new ImageIcon(productionCard));

        title.setText(production.getName());
        String information = getInformation(production);
        info.setText(information);
        rating.setText(GUI.formatDouble(production.getIMDBRating()));
        ratingCount.setText(" (" + production.getRatings().size() + ")");

        if (activeUser.getFavorites().contains(production)) {
            favoriteButton.setVisible(false);
            favoriteRemoveButton.setVisible(true);
        }

        if (IMDB.getInstance().getMyContributions().contains(production)) {
            editButton.setVisible(true);
            deleteButton.setVisible(true);
        }

        FontMetrics fontMetrics = title.getFontMetrics(title.getFont());
        int textWidth = fontMetrics.stringWidth(title.getText());
        int textHeight = fontMetrics.getHeight();
        Dimension preferredSize = new Dimension(textWidth + 10, textHeight + 10);
        title.setPreferredSize(preferredSize);
        panel.revalidate();
        panel.repaint();
    }

    public void enableRating() {
        rateButton.setEnabled(true);
        rateButton.addMouseListener(rateMouseAdapter);
    }

    public void disableRating() {
        rateButton.setEnabled(false);
        rateButton.removeMouseListener(rateMouseAdapter);
    }

    private JFrame appFrame;
    private JLabel card;
    private JTextField info;
    private JButton rateButton;
    private JButton favoriteButton;
    private JLabel rating;
    private JTextField ratingCount;
    private JButton title;
    private JButton favoriteRemoveButton;
    private JPanel panel;
    private JButton editButton;
    private JButton deleteButton;
    private JPanel innerPanel;
    private MainForm mainForm;
    private Production production;
    private final MouseAdapter rateMouseAdapter = GUI.createMouseHover(rateButton, Color.WHITE, Color.LIGHT_GRAY);

    public JPanel getPanel() {
        return panel;
    }

    public ProductionForm() {
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                title.setForeground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                title.setForeground(Color.BLACK);
            }
        });
        rateButton.addMouseListener(rateMouseAdapter);
        favoriteButton.addMouseListener(GUI.createMouseHover(favoriteButton, Color.WHITE, Color.LIGHT_GRAY));
        favoriteRemoveButton.addMouseListener(GUI.createMouseHover(favoriteRemoveButton, Color.WHITE, Color.LIGHT_GRAY));
        editButton.addMouseListener(GUI.createMouseHover(editButton, Color.WHITE, Color.LIGHT_GRAY));
        deleteButton.addMouseListener(GUI.createMouseHover(deleteButton, Color.WHITE, Color.LIGHT_GRAY));
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        favoriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IMDB.getInstance().getActiveUser().addToFavorites(production);
                favoriteButton.setVisible(false);
                favoriteRemoveButton.setVisible(true);
            }
        });
        favoriteRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IMDB.getInstance().getActiveUser().removeFromFavorites(production);
                favoriteRemoveButton.setVisible(false);
                favoriteButton.setVisible(true);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = "Are you sure you want to delete " + production.getName() + "?";
                ConfirmDeleteForm confirmDeleteForm = new ConfirmDeleteForm(appFrame, text);
                confirmDeleteForm.setVisible(true);

                if (confirmDeleteForm.isConfirmed()) {
                    ((Staff) IMDB.getInstance().getActiveUser()).removeProductionSystem(production);
                    mainForm.removeProduction(production);
                    mainForm.refresh();
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditProductionForm editProductionForm = new EditProductionForm(appFrame, production);
                editProductionForm.setVisible(true);
                handleEdit(editProductionForm.getProduction());
            }
        });
        GUI.fixFont(title);
        GUI.fixFont(editButton);
        GUI.fixFont(deleteButton);
        GUI.fixFont(rateButton);
        GUI.fixFont(favoriteButton);
        GUI.fixFont(favoriteRemoveButton);
        GUI.fixFont(info);
        GUI.fixFont(rating);
        GUI.fixFont(ratingCount);
        rateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RateForm rateForm = new RateForm(appFrame, production);
                rateForm.setVisible(true);
                rating.setText(GUI.formatDouble(production.getIMDBRating()));
                ratingCount.setText(" (" + production.getRatings().size() + ")");
            }
        });
        title.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductionDetailsForm productionDetailsForm = new ProductionDetailsForm(appFrame, production);
                productionDetailsForm.setVisible(true);
            }
        });
    }

    public static String getInformation(Production production) {
        String information = "";
        if (production instanceof Movie) {
            Movie movie = (Movie) production;
            int length = movie.getLength() != -1 ? movie.getLength() : 0;
            int hours = length / 60;
            int minutes = length % 60;
            if (movie.getReleaseYear() != -1)
                information += movie.getReleaseYear() + "    ";
            if (hours != 0)
                information += hours + "h ";
            if (minutes != 0)
                information += minutes + "m";
        } else {
            Series series = (Series) production;
            if (series.getReleaseYear() != -1)
                information += series.getReleaseYear() + "    ";
            information += series.getEpisodeCount() + " eps";
        }
        return information;
    }

    private void createUIComponents() {
        panel = new JPanel();
        innerPanel = new JPanel();
        title = new JButton();
        card = new JLabel();
        info = new JTextField();
        rateButton = new JButton();
        favoriteButton = new JButton();
        favoriteRemoveButton = new JButton();
        editButton = new JButton();
        deleteButton = new JButton();
        rating = new JLabel();
        ratingCount = new JTextField();
        ratingCount.setBorder(new EmptyBorder(0, 0, 0, 0));
        info.setBorder(new EmptyBorder(0, 0, 0, 0));
        title.setMargin(new Insets(0, 5, 0, 0));
        title.setCursor(new Cursor(Cursor.HAND_CURSOR));
        favoriteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        favoriteRemoveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void handleEdit(Production newProduction) {
        ((Staff) IMDB.getInstance().getActiveUser()).updateProduction(this.production, newProduction);
        mainForm.replaceProduction(this.production, newProduction);
        mainForm.refresh();
    }
}
