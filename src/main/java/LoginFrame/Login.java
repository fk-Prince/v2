package LoginFrame;

import PointOfSale.PointOfSaleFrame;
import SwingComponents.AnimateMessage;
import SwingComponents.MyButton;
import SwingComponents.PasswordField.PasswordPanel;
import SwingComponents.TextField.FieldPanel;
import User.User;
import User.UserRepository;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;

public class Login extends JPanel {

    private final UserRepository userRepository;
    private final MigLayout migLayout;

    public Login() {
        userRepository = new UserRepository();
        migLayout = new MigLayout("fill,insets 0");
        setLayout(migLayout);
        initComponents();
    }

    private void initComponents() {
        JPanel fields = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(50, 10, 10, 10);
        gbc.fill = GridBagConstraints.CENTER;


        FieldPanel username = new FieldPanel("Username", "/Images/user.png");
        username.setFocusable(true);
        username.setPreferredSize(new Dimension(300, 50));
        gbc.gridy = 0;
        fields.add(username, gbc);
        gbc.insets = new Insets(10, 10, 10, 10);
        PasswordPanel password = new PasswordPanel("Password", "/Images/password.png");
        password.setPreferredSize(new Dimension(300, 50));
        gbc.gridy = 1;
        fields.add(password, gbc);

        MyButton button = new MyButton("Login", true, false);
        button.addActionListener(e -> {
            if (isInputsValid(username, password)) {
                username.setText("");//reset
                password.setText("");
            }
        });
        button.setPreferredSize(new Dimension(150, 50));
        gbc.gridy = 2;
        gbc.insets = new Insets(50, 10, 10, 10);
        fields.add(button, gbc);
        add(fields, "al center");

        add(logoutButton(), "pos 2% 90%");
    }

    private JButton logoutButton() {
        JButton logout = new JButton() {
            private final Image image = new ImageIcon(getClass().getResource("/Images/logout.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(image, getWidth() - image.getWidth(null), (getHeight() - image.getHeight(null)) / 2, null);
                repaint();
                g2d.dispose();

            }
        };

        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logout.setPreferredSize(new Dimension(0, 50));
        logout.setContentAreaFilled(false);
        logout.setBorderPainted(false);
        logout.setFocusPainted(false);
        logout.setOpaque(false);
        logout.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            parentFrame.dispose();
        });
        return logout;
    }

    private boolean isInputsValid(FieldPanel username, PasswordPanel password) {
        if (password.getPassword().isEmpty() || username.getText().isEmpty()) {
            AnimateMessage.showMessage("Invalid Input", true, migLayout, this, 25, 0,true);
            return false;
        }


        try {
            Optional<User> user = userRepository.checkAccount(new User(username.getText(), password.getPassword()));
            if (user.isPresent()) {
                AnimateMessage.showMessage("Successfully Logged in", false, migLayout, this, 25, 0,true);
                Timer timer = new Timer(2000, e -> {
                    SwingUtilities.getWindowAncestor(this).dispose();
                    SwingUtilities.invokeLater(PointOfSaleFrame::new);
                });
                timer.setRepeats(false);
                timer.start();
                password.setEnabled(false);
                username.setEnabled(false);
            } else {
                AnimateMessage.showMessage("Incorrect Password", true, migLayout, this, 25, 0,true);
            }
        } catch (IOException e) {
            System.out.println("Error ");
        }

        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(new Font("Arial", Font.PLAIN, 30));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(Color.BLACK);
        g2d.drawString("Login", (getWidth() - fm.stringWidth("Login")) / 2, 75);
        g2d.dispose();
    }
}
