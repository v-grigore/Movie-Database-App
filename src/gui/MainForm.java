package gui;

import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainForm extends JPanel {
    private JFrame appFrame;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JButton menuButton;
    private JTextField searchTextField;
    private JButton searchButton;
    private JPanel searchPanel;
    private JButton favoritesButton;
    private JButton profileButton;
    private JButton logo;
    private JPanel displayPanel;
    private JScrollBar verticalScrollBar;
    private JScrollBar horizontalScrollBar;
    private JPanel view;
    private JButton switchOrderButton;
    private JLabel sortByLabel;
    private JButton sortMenuButton;
    private JButton productionsButton;
    private JButton actorsButton;
    private JPopupMenu sortPopup;
    private JPopupMenu menuPopup;
    private final MouseAdapter productionsHover = GUI.createMouseHover(productionsButton, Color.WHITE, Color.LIGHT_GRAY);
    private final MouseAdapter actorsHover = GUI.createMouseHover(actorsButton, Color.WHITE, Color.LIGHT_GRAY);
    private final MouseAdapter sortHover = GUI.createMouseHover(sortMenuButton, Color.WHITE, new Color(0x87CEFA));
    private final ActionListener searchActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<ArtisticEntity> searchResults = IMDB.getInstance().searchResults(searchTextField.getText());
            setProductions(new ArrayList<>());
            setActors(new ArrayList<>());
            for (ArtisticEntity entity : searchResults) {
                if (entity instanceof Production)
                    productions.add((Production) entity);
                else
                    actors.add((Actor) entity);
            }
            actorsButton.doClick();
            productionsButton.doClick();
        }
    };
    private ArrayList<Production> productions;
    private ArrayList<Production> sortedProductions;
    private ArrayList<Actor> actors;

    public JPanel getPanel() {
        return panel;
    }

    private void internDisplayProductions(ArrayList<Production> productions) {
        scrollPane.getViewport().removeAll();
        view.removeAll();
        view.setBorder(new EmptyBorder(12, 12, 12, 12));
        view.setLayout(new BoxLayout(view, BoxLayout.PAGE_AXIS));
        int index = 0;
        for (Production production : productions) {
            JSeparator separator = new JSeparator();
            separator.setForeground(Color.BLACK);
            ProductionForm productionForm = new ProductionForm(production, appFrame, this);
            if (IMDB.getInstance().getActiveUser().getAccountType() != AccountType.Regular) {
                productionForm.disableRating();
            }
            view.add(productionForm);
            if (++index < productions.size()) {
                view.add(Box.createRigidArea(new Dimension(0, 5)));
                view.add(separator);
                view.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        scrollPane.setViewportView(view);
        scrollPane.revalidate();
        scrollPane.repaint();

        System.out.println("ynysebi\n" + IMDB.getInstance().getMyContributions());
    }

    public void displayProductions(ArrayList<Production> productions) {
        this.productions = productions;
        this.sortedProductions = new ArrayList<>(productions);
        internDisplayProductions(productions);
    }

    public void displayActors() {
        scrollPane.getViewport().removeAll();
        view.removeAll();
        view.setBorder(new EmptyBorder(12, 12, 12, 12));
        view.setLayout(new BoxLayout(view, BoxLayout.PAGE_AXIS));
        int index = 0;
        for (Actor actor : actors) {
            JSeparator separator = new JSeparator();
            separator.setForeground(Color.BLACK);
            ActorForm actorForm = new ActorForm(actor, appFrame, this);
            view.add(actorForm);
            if (++index < actors.size()) {
                view.add(Box.createRigidArea(new Dimension(0, 5)));
                view.add(separator);
                view.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        scrollPane.setViewportView(view);
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    public void refresh() {
        if (actorsButton.isEnabled()) {
            internDisplayProductions(sortedProductions);
        }
        else {
            displayActors();
        }
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = new ArrayList<>(actors);
        this.actors.sort(Comparator.naturalOrder());
    }

    public void removeProduction(Production production) {
        sortedProductions.remove(production);
    }

    public void removeActor(Actor actor) {
        actors.remove(actor);
    }

    public void replaceProduction(Production oldProduction, Production newProduction) {
        int index = sortedProductions.indexOf(oldProduction);
        sortedProductions.set(index, newProduction);
    }

    public void replaceActor(Actor oldActor, Actor newActor) {
        int index = actors.indexOf(oldActor);
        actors.set(index, newActor);
    }

    public void setProductions(ArrayList<Production> productions) {
        this.productions = productions;
    }

    public MainForm(JFrame appFrame) {
        this.appFrame = appFrame;

        menuButton.addMouseListener(GUI.createMouseHover(menuButton, Color.BLACK, Color.DARK_GRAY));
        searchTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchTextField.getText().equals("Search IMDb")) {
                    searchTextField.setText("");
                    searchTextField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setText("Search IMDb");
                    searchTextField.setForeground(Color.GRAY);
                }
            }
        });
        favoritesButton.addMouseListener(GUI.createMouseHover(favoritesButton, Color.BLACK, Color.DARK_GRAY));
        profileButton.addMouseListener(GUI.createMouseHover(profileButton, Color.BLACK, Color.DARK_GRAY));
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPopup.show(menuButton, 0, menuButton.getHeight());
            }
        });
        sortMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortPopup.show(sortMenuButton, 0, sortMenuButton.getHeight());
            }
        });
        sortMenuButton.addMouseListener(sortHover);
        switchOrderButton.addMouseListener(GUI.createMouseHover(switchOrderButton, Color.WHITE, Color.LIGHT_GRAY));
        switchOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (actorsButton.isEnabled()) {
                    Collections.reverse(sortedProductions);
                    internDisplayProductions(sortedProductions);
                }
                else {
                    Collections.reverse(actors);
                    displayActors();
                }
            }
        });
        actorsButton.addMouseListener(actorsHover);
        productionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productionsButton.setEnabled(false);
                productionsButton.removeMouseListener(productionsHover);
                productionsButton.setBackground(Color.WHITE);
                productionsButton.setBorderPainted(true);
                actorsButton.setEnabled(true);
                actorsButton.addMouseListener(actorsHover);
                actorsButton.setBorderPainted(false);
                sortMenuButton.setEnabled(true);
                sortMenuButton.addMouseListener(sortHover);
                sortMenuButton.setText("Default");
                displayProductions(productions);
                JViewport viewport = scrollPane.getViewport();
                viewport.setViewPosition(new Point(0, 0));
            }
        });
        actorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productionsButton.setEnabled(true);
                productionsButton.addMouseListener(productionsHover);
                productionsButton.setBorderPainted(false);
                actorsButton.setEnabled(false);
                actorsButton.removeMouseListener(actorsHover);
                actorsButton.setBackground(Color.WHITE);
                actorsButton.setBorderPainted(true);
                sortMenuButton.setEnabled(false);
                sortMenuButton.removeMouseListener(sortHover);
                sortMenuButton.setText("Alphabetical");
                displayActors();
                JViewport viewport = scrollPane.getViewport();
                viewport.setViewPosition(new Point(0, 0));
            }
        });
        favoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActors(new ArrayList<>());
                setProductions(new ArrayList<>());
                for (ArtisticEntity entity : IMDB.getInstance().getActiveUser().getFavorites()) {
                    if (entity instanceof Production)
                        productions.add((Production) entity);
                    else
                        actors.add((Actor) entity);
                }
                actorsButton.doClick();
                productionsButton.doClick();
            }
        });
        logo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setProductions(IMDB.getInstance().getProductions());
                setActors(IMDB.getInstance().getActors());
                actorsButton.doClick();
                productionsButton.doClick();
            }
        });
        GUI.fixFont(menuButton);
        GUI.fixFont(searchTextField);
        GUI.fixFont(favoritesButton);
        GUI.fixFont(profileButton);
        GUI.fixFont(productionsButton);
        GUI.fixFont(actorsButton);
        GUI.fixFont(sortByLabel);
        GUI.fixFont(sortMenuButton);
        GUI.fixFont(switchOrderButton);
        GUI.fixFont(sortPopup);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        searchButton.addActionListener(searchActionListener);
        searchTextField.addActionListener(searchActionListener);
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfileForm profileForm = new ProfileForm(appFrame, IMDB.getInstance().getActiveUser());
                profileForm.setVisible(true);
            }
        });
    }

    private void createUIComponents() {
        displayPanel = new JPanel();
        scrollPane = new JScrollPane();
        view = new JPanel();
        searchTextField = new JTextField();
        searchTextField.setBorder(new EmptyBorder(0, 0, 4, 0));
        searchPanel = new JPanel();
        searchButton = new JButton();
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuButton = new JButton();
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        favoritesButton = new JButton();
        favoritesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileButton = new JButton();
        profileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logo = new JButton();
        logo.setMargin(new Insets(0, 0, 0, 0));
        logo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        verticalScrollBar = new JScrollBar();
        verticalScrollBar.setUI(new NoArrowScrollBarUI());
        horizontalScrollBar = new JScrollBar();
        horizontalScrollBar.setUI(new NoArrowScrollBarUI());
        setupSortPopup();
        sortByLabel = new JLabel();
        sortMenuButton = new JButton();
        sortMenuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchOrderButton = new JButton();
        switchOrderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        productionsButton = new JButton();
        productionsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actorsButton = new JButton();
        actorsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupSortPopup() {
        sortPopup = new JPopupMenu();
        sortPopup.setBackground(Color.WHITE);
        JMenuItem menuItem0 = new JMenuItem("Default");
        JMenuItem menuItem1 = new JMenuItem("IMDb rating");
        JMenuItem menuItem2 = new JMenuItem("Release date");
        JMenuItem menuItem3 = new JMenuItem("Number of ratings");
        JMenuItem menuItem4 = new JMenuItem("Alphabetical");
        menuItem0.setBackground(Color.WHITE);
        menuItem1.setBackground(Color.WHITE);
        menuItem2.setBackground(Color.WHITE);
        menuItem3.setBackground(Color.WHITE);
        menuItem4.setBackground(Color.WHITE);
        GUI.fixFont(menuItem0);
        GUI.fixFont(menuItem1);
        GUI.fixFont(menuItem2);
        GUI.fixFont(menuItem3);
        GUI.fixFont(menuItem4);
        sortPopup.add(menuItem0);
        sortPopup.add(menuItem1);
        sortPopup.add(menuItem2);
        sortPopup.add(menuItem3);
        sortPopup.add(menuItem4);
        menuItem0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortMenuButton.setText("Default");
                sortedProductions = new ArrayList<>(productions);
                internDisplayProductions(sortedProductions);
            }
        });
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortMenuButton.setText("IMDb rating");
                sortedProductions.sort(Comparator.comparingDouble(Production::getIMDBRating).reversed());
                internDisplayProductions(sortedProductions);
            }
        });
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortMenuButton.setText("Release date");
                sortedProductions.sort(Comparator.comparingInt((Production production) -> production instanceof Movie ?
                        ((Movie) production).getReleaseYear() : ((Series) production).getReleaseYear()).reversed());
                internDisplayProductions(sortedProductions);
            }
        });
        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortMenuButton.setText("Number of ratings");
                sortedProductions.sort(Comparator.comparingInt((Production production) -> production.getRatings().size()).reversed());
                internDisplayProductions(sortedProductions);
            }
        });
        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortMenuButton.setText("Alphabetical");
                sortedProductions.sort(Comparator.naturalOrder());
                internDisplayProductions(sortedProductions);
            }
        });
    }

    public void setupMenuPopup() {
        menuPopup = new JPopupMenu();
        menuPopup.setBackground(Color.BLACK);
        JMenuItem jMenuItem1 = new JMenuItem("Notifications");
        JMenuItem jMenuItem2 = new JMenuItem("Requests");
        JMenuItem jMenuItem3 = new JMenuItem("Solve requests");
        JMenuItem jMenuItem4 = new JMenuItem("Add user");
        JMenuItem jMenuItem5 = new JMenuItem("Remove user");
        jMenuItem1.setIcon(new ImageIcon(getClass().getResource("/notifications_icon_24x.png")));
        jMenuItem2.setIcon(new ImageIcon(getClass().getResource("/requests_icon_24x.png")));
        jMenuItem3.setIcon(new ImageIcon(getClass().getResource("/solve_icon_24x.png")));
        jMenuItem4.setIcon(new ImageIcon(getClass().getResource("/add_user_icon_24x.png")));
        jMenuItem5.setIcon(new ImageIcon(getClass().getResource("/remove_user_icon_24x.png")));
        jMenuItem1.setBackground(Color.BLACK);
        jMenuItem2.setBackground(Color.BLACK);
        jMenuItem3.setBackground(Color.BLACK);
        jMenuItem4.setBackground(Color.BLACK);
        jMenuItem5.setBackground(Color.BLACK);
        jMenuItem1.setForeground(Color.WHITE);
        jMenuItem2.setForeground(Color.WHITE);
        jMenuItem3.setForeground(Color.WHITE);
        jMenuItem4.setForeground(Color.WHITE);
        jMenuItem5.setForeground(Color.WHITE);
        GUI.fixFont(jMenuItem1);
        GUI.fixFont(jMenuItem2);
        GUI.fixFont(jMenuItem3);
        GUI.fixFont(jMenuItem4);
        GUI.fixFont(jMenuItem5);
        jMenuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NotificationsForm notificationsForm = new NotificationsForm(appFrame, IMDB.getInstance().getActiveUser());
                notificationsForm.setVisible(true);
            }
        });
        jMenuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RequestsForm requestsForm = new RequestsForm(appFrame, IMDB.getInstance().getActiveUser());
                requestsForm.setVisible(true);
            }
        });
        jMenuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        jMenuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        jMenuItem5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        menuPopup.add(jMenuItem1);
        if (IMDB.getInstance().getActiveUser().getAccountType() != AccountType.Admin)
            menuPopup.add(jMenuItem2);
        if (IMDB.getInstance().getActiveUser().getAccountType() != AccountType.Regular)
            menuPopup.add(jMenuItem3);
        if (IMDB.getInstance().getActiveUser() instanceof Admin) {
            menuPopup.add(jMenuItem4);
            menuPopup.add(jMenuItem5);
        }
    }

    static class NoArrowScrollBarUI extends BasicScrollBarUI {
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension();
                }
            };
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension();
                }
            };
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(Color.WHITE);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            g.setColor(Color.GRAY);
            g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
        }
    }
}
