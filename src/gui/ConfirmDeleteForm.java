package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Function;

public class ConfirmDeleteForm extends JDialog {
    public ConfirmDeleteForm(JFrame owner, String text) {
        super(owner, true);
        setUndecorated(true);

        this.text.setText(text);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                confirmed = true;
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                confirmed = false;
            }
        });
        cancelButton.addMouseListener(GUI.createMouseHover(cancelButton, new Color(211, 211, 211), Color.GRAY));
        confirmButton.addMouseListener(GUI.createMouseHover(confirmButton, new Color(220, 20, 60), new Color(155, 20, 48)));

        GUI.fixFont(cancelButton);
        GUI.fixFont(confirmButton);
        GUI.fixFont(this.text);

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
    private JLabel text;
    private JButton confirmButton;
    private JButton cancelButton;
    private boolean confirmed;

    private void createUIComponents() {
        panel = new JPanel();
        displayPanel = new JPanel();
        text = new JLabel();
        confirmButton = new JButton();
        cancelButton = new JButton();
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
