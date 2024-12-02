package Numpad;

import SwingComponents.MyTextField2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

public class Numpad extends JPanel implements ActionListener{

    private JTextField focusedField;


    public Numpad() {
        setLayout(new BorderLayout());


        JPanel buttonHolder = new JPanel(new GridLayout(4, 3, 5, 5));
        buttonHolder.setBorder(new EmptyBorder(20, 0, 0, 0));
        JButton[] numButtons = new JButton[9];
        for (int i = 0; i < 9; i++) {
            numButtons[i] = new JButton(String.valueOf(i + 1));
            numButtons[i].setFont(new Font("Arial", Font.PLAIN, 20));
            numButtons[i].addActionListener(this);
            numButtons[i].setFocusable(false);
        }

        for (int i = 0; i < 9; i++) {
            buttonHolder.add(numButtons[i]);
        }
        JButton b0 = new JButton("0");
        b0.setFont(new Font("Arial", Font.PLAIN, 20));
        b0.addActionListener(this);
        buttonHolder.add(b0);
        add(buttonHolder, BorderLayout.CENTER);

        JButton clearButton = new JButton(".");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 20));
        clearButton.setFocusable(false);
        clearButton.addActionListener(this);
        buttonHolder.add(clearButton);


        JButton backSpace = new JButton("");
        backSpace.setFocusable(false);
        Image image = new ImageIcon(getClass().getResource("/Images/back.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        backSpace.setIcon(new ImageIcon(image));
        backSpace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedField != null && !focusedField.getText().isEmpty()) {
                    focusedField.setText(focusedField.getText().substring(0, focusedField.getText().length() - 1));
                }
            }
        });
        buttonHolder.add(backSpace);
    }

    public void setFocusedField(MyTextField2 field) {
        this.focusedField = field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        if (focusedField != null) {
            focusedField.setText(focusedField.getText() + source.getText());
        }
    }


}
