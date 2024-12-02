package SwingComponents;

import javax.swing.*;
import java.awt.*;

public class MessageDialog extends JPanel {

    private final boolean flag;
    private final String message;
    private static JPanel parent;

    public MessageDialog(String message, boolean flag) {
        this.flag = flag;
        this.message = message;
        setOpaque(true);

        Timer timer = new Timer(3000, e -> {
            if (getParent() != null) {
                parent = (JPanel) getParent();
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            }
        });
        timer.start();
        timer.setRepeats(false);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(0, 0, getWidth(), getHeight());

        g2d.setColor(new Color(47, 55, 62));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setFont(new Font("Arial", Font.PLAIN, 15));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.drawString(message, (getWidth() - fm.stringWidth(message)) / 2, (getHeight() - fm.getAscent()) / 2 + fm.getAscent());

        g2d.setColor(flag ? Color.RED : Color.GREEN);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(1, 0, 1, getHeight());
        g2d.drawLine(getWidth() - 2, 0, getWidth() - 2, getHeight());
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(250, 50);
    }
}
