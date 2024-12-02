package SwingComponents.PasswordField;

import javax.swing.*;
import java.awt.*;

public class PasswordField extends JPasswordField {
    private final String text;

    public PasswordField(String text) {
        this.text = text;
        setBorder(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        FontMetrics fm = g2d.getFontMetrics();
        if (!isFocusOwner() && getPassword().length == 0) {
            g2d.setColor(Color.BLACK);
            g2d.drawString(text, getInsets().left + 20, (getHeight() - fm.getAscent()) / 2 + fm.getAscent());
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 0.2f));
        } else {
            repaint();
        }

        g2d.dispose();

    }


}
