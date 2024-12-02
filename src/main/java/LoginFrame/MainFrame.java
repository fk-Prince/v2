package LoginFrame;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }

    public MainFrame() {
        init();
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new MigLayout("fill,insets 0"));
        mainPanel.setBackground(Color.BLACK);

        JPanel login = new Login();

        mainPanel.add(login, "pos 0 0, w 100%, h 100%");
        add(mainPanel);
    }

    private void init() {
        setSize(700, 550);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
       // setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

}