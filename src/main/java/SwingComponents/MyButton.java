package SwingComponents;

import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {

    private String text;
    private boolean primary;
    private Image image;

    public MyButton(String text, boolean primary, boolean putImage) {
        this.primary = primary;
        this.text = text;

        if (putImage)
            image = new ImageIcon(getClass().getResource("/Images/cart.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (primary) {
            if (isEnabled()) {
                g2d.setColor(getModel().isRollover() ? Color.WHITE : new Color(50, 50, 50));
            } else {
                g2d.setColor(new Color(200, 200, 200));
            }
            g2d.fillRoundRect(3, 4, getWidth() - 4, getHeight() - 5, 20, 20);

            g2d.setColor(getModel().isRollover() ? new Color(50, 50, 50) : Color.WHITE);
            g2d.drawRoundRect(3, 4, getWidth() - 4, getHeight() - 5, 20, 20);
        } else {
            if (isEnabled()) {
                g2d.setColor(getModel().isRollover() ? new Color(80, 80, 80) : new Color(220, 220, 220));
            } else {
                g2d.setColor(new Color(200, 200, 200)); // Light gray for disabled state
            }
            g2d.fillRoundRect(3, 4, getWidth() - 4, getHeight() - 5, 20, 20);
            g2d.setColor(getModel().isRollover() ? Color.WHITE : new Color(50, 50, 50));


            g2d.drawRoundRect(3, 4, getWidth() - 4, getHeight() - 5, 20, 20);
        }

        if (image == null) {
            FontMetrics fm = g2d.getFontMetrics();
            g2d.setColor(isEnabled() ? Color.WHITE : Color.GRAY);
            g2d.setColor(primary ? Color.WHITE : Color.BLACK);
            if (primary) {
                g2d.setColor(getModel().isRollover() ? Color.BLACK : Color.WHITE);
            } else {
                g2d.setColor(getModel().isRollover() ? Color.WHITE : Color.BLACK);
            }
            g2d.drawString(text, (getWidth() - fm.stringWidth(text)) / 2 + 2, (getHeight() - fm.getAscent()) / 2 + fm.getAscent());
        } else {
            g2d.drawImage(image, (getWidth() - image.getWidth(null)) / 2, (getHeight() - image.getHeight(null)) / 2, null);
            repaint();
        }

        g2d.dispose();
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public void setPrimary(boolean bln) {
        this.primary = bln;
        repaint();
    }
}

