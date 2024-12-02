package SwingComponents.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class FieldPanel extends JPanel {

    private final InputField input;

    public FieldPanel(String text, String image) {
        setBackground(Color.WHITE);
        setOpaque(false);
        setBorder(new EmptyBorder(4, 0, 3, 4));
        setLayout(new BorderLayout());
        input = new InputField(text);
        input.setBackground(Color.WHITE);
        add(input, BorderLayout.CENTER);

        JLabel label = new JLabel();
        label.setBorder(new EmptyBorder(0, 10, 0, 10));
        Image s = new ImageIcon(getClass().getResource(image)).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(s));
        add(label, BorderLayout.WEST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(getBackground());
        g2d.fillRoundRect(2, 3, getWidth() - 3, getHeight() - 4, 10, 10);

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawRoundRect(2, 3, getWidth() - 3, getHeight() - 4, 10, 10);

        g2d.dispose();
    }


    public String getText() {
        return input.getText().trim();
    }

    public void setText(String text) {
        input.setText(text);
    }

    public void setEnabled(boolean bln){
        input.setEnabled(bln);
    }
}
