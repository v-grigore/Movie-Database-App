package gui;

import model.IMDB;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame {
    private final LoginForm loginForm = new LoginForm(this);
    private final MainForm mainForm = new MainForm(this);
    private boolean loggedIn = false;

    public GUI() {
        setIconImage(new ImageIcon(getClass().getResource("/app_icon.png")).getImage());
        setTitle("IMDB");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setContentPane(loginForm.getPanel());
        pack();
        loginForm.setupInvalidPopup(false);
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (loggedIn) {
                    setExtendedState(NORMAL);
                    switchToLoginForm();
                }
                else
                    System.exit(0);
            }
        });
    }

    public void run() {
        loggedIn = true;
        switchToMainForm();
    }

    private void switchToLoginForm() {
        loggedIn = false;
        loginForm.setupInvalidPopup(true);
        setContentPane(loginForm.getPanel());
        pack();
        loginForm.setupInvalidPopup(false);
        setResizable(false);
        setLocationRelativeTo(null);
        loginForm.getPanel().setVisible(true);
        mainForm.setVisible(false);
    }

    private void switchToMainForm() {
        setContentPane(mainForm);
        pack();
        setResizable(true);
        setExtendedState(MAXIMIZED_BOTH);
        mainForm.setActors(IMDB.getInstance().getActors());
        mainForm.displayProductions(IMDB.getInstance().getProductions());
        mainForm.setupMenuPopup();
        loginForm.getPanel().setVisible(false);
        mainForm.setVisible(true);
    }

    public static MouseAdapter createMouseHover(JButton jButton, Color bg, Color hover) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                jButton.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jButton.setBackground(bg);
            }
        };
    }

    public static void fixFont(Component component) {
        Font currentFont = component.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize() * 4 / 3);
        component.setFont(newFont);
    }

    public static void setupCharacterLimit(JTextArea textArea, int limit) {
        AbstractDocument document = (AbstractDocument) textArea.getDocument();
        document.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                int length = fb.getDocument().getLength() + string.length();
                if (length <= limit) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                int currentLength = fb.getDocument().getLength() + text.length() - length;
                if (currentLength <= limit) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    public static String formatDouble(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            return String.format("%.1f", value);
        }
    }
}
