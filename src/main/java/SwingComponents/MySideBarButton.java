package SwingComponents;

import javax.swing.*;
import java.awt.*;

public class MySideBarButton extends JButton {

    private final String text;
    private final Image image;
    private boolean isActive = false;
    private final int WIDTH = 250;
    private final int HEIGHT = 60;


    public MySideBarButton(String text, String image) {
        this.text = text;
        this.image = new ImageIcon(getClass().getResource(image)).getImage();
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
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        FontMetrics fm = g2d.getFontMetrics();


        g2d.setColor(getModel().isRollover() ? new Color(97, 121, 145) : new Color(97, 133, 96));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(0, 0, getWidth(), 0);
        g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        if (isActive) {
            g2d.setColor(Color.WHITE);
            g2d.drawString(text, (WIDTH / 2) - (fm.stringWidth(text) / 2), (HEIGHT - fm.getHeight()) / 2 + fm.getAscent());
        }
        g2d.drawImage(image, 5, (HEIGHT - image.getHeight(null) + 17) / 2, 40, 40, null);


        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    public void setActive(boolean bln) {
        this.isActive = bln;
        setEnabled(true);
        repaint();
    }
}
