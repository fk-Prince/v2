package SwingComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;


public class MyTableModel extends DefaultTableCellRenderer {


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel x = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        x.setOpaque(true);
        x.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        if (isSelected) {
            x.setBackground(new Color(56, 161, 107));
        } else {
            x.setBackground(Color.WHITE);
        }
        table.setBorder(new EmptyBorder(5,5,5,5));

        x.setForeground(Color.BLACK);
        return x;
    }



}
