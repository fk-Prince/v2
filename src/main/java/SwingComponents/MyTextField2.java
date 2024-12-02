package SwingComponents;

import Numpad.Numpad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MyTextField2 extends JTextField {

    public MyTextField2() {
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }


    public void setFocusListener(Numpad numpad) {
        MyTextField2 field = this;
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                numpad.setFocusedField(field);
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
    }
}
