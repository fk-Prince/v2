package SwingComponents.PasswordField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class PasswordPanel extends JPanel {

    private boolean isPasswordToggled = false;
    private final PasswordField input;
    private final JButton button;
    private boolean isInputValid = true;

    public PasswordPanel(String text, String image) {
        setBackground(Color.WHITE);
        setOpaque(false);
        setBorder(new EmptyBorder(4, 0, 3, 4));
        setLayout(new BorderLayout());
        input = new PasswordField(text);
        input.setBackground(Color.WHITE);
        add(input, BorderLayout.CENTER);

        JLabel label = new JLabel();
        label.setBorder(new EmptyBorder(0, 10, 0, 10));
        Image s = new ImageIcon(getClass().getResource(image)).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(s));
        add(label, BorderLayout.WEST);


        button = new JButton();
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusable(false);
        button.addActionListener(e -> {
            isPasswordToggled = !isPasswordToggled;
            if (isPasswordToggled) {
                input.setText(new String(input.getPassword()));
                input.setEchoChar('\u0000');
                Image x1 = new ImageIcon(getClass().getResource("/Images/pshow.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(x1));
            } else {
                input.setEchoChar('•');
                Image x1 = new ImageIcon(getClass().getResource("/Images/phide.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(x1));
            }
        });
        button.setBorder(new EmptyBorder(0, 10, 0, 10));
        Image x = new ImageIcon(getClass().getResource("/Images/phide.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(x));
        add(button, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(getBackground());
        g2d.fillRoundRect(2, 3, getWidth() - 3, getHeight() - 4, 10, 10);
        g2d.setColor(isInputValid ? Color.LIGHT_GRAY : Color.RED);
        g2d.drawRoundRect(2, 3, getWidth() - 3, getHeight() - 4, 10, 10);


        g2d.dispose();

    }

    public String getPassword() {
        return new String(input.getPassword()).trim();
    }

    public void setInput(boolean bo) {
        this.isInputValid = bo;
        repaint();
    }

    public void isInputValid() {
        setInput(!new String(input.getPassword()).trim().isEmpty());

        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setInput(!new String(input.getPassword()).trim().isEmpty());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setInput(!new String(input.getPassword()).trim().isEmpty());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setInput(!new String(input.getPassword()).trim().isEmpty());
            }
        });

    }


    public void setText(String text) {
        Image x = new ImageIcon(getClass().getResource("/Images/phide.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(x));
        input.setText(text);
    }

    public void setEnabled(boolean bln) {
        input.setEnabled(bln);
    }
}
