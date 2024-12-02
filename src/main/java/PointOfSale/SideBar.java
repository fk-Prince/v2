package PointOfSale;

import Interfaces.ResetPanel;
import Interfaces.SwitchPanel;
import Interfaces.TableSettings;
import Interfaces.TransitionPanel;
import LoginFrame.MainFrame;
import Product.Repository.PaymentRepository;
import SwingComponents.MySideBarButton;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SideBar extends JPanel implements SwitchPanel {
    protected boolean isSideBarActive;
    private Timer timer;
    private int SIDEBAR_POSITION = 50;
    private ResetPanel reset;

    private final List<MySideBarButton> buttonlist;
    private final SwitchPanel panelSwitching;
    private final TransitionPanel transitionPanel;
    private final TableSettings tableSettings;
    private JButton toggleSideBarButton;


    public SideBar(TransitionPanel transitionPanel, SwitchPanel panelSwitching, TableSettings tableSettings) {
        this.transitionPanel = transitionPanel;
        this.tableSettings = tableSettings;
        this.panelSwitching = panelSwitching;
        buttonlist = new ArrayList<>();
        init();
    }

    public void init() {
        setLayout(new MigLayout("fillx,insets 0 0 0 0", "[]0[]", "[]2[]"));
        setBackground(new Color(92, 110, 129));

        MySideBarButton b1 = new MySideBarButton("BUY PRODUCT", "/Images/buy.png");
        MySideBarButton b2 = new MySideBarButton("ADD PRODUCT", "/Images/buy.png");
        MySideBarButton b3 = new MySideBarButton("GENERATE SALES", "/Images/buy.png");
        MySideBarButton b4 = new MySideBarButton("PRICE ADJUSTMENT", "/Images/buy.png");


        add(addSlideButton(), "ay 20%, ax 95%, wrap");
        add(b1, "wrap,gaptop 15%");
        add(b2, "wrap");
        add(b3, "wrap");
        add(b4, "wrap");
        add(logoutButton(), "pos 8% 90%, wrap");
        PaymentRepository paymentRepository = new PaymentRepository();
        b1.addActionListener(_ -> switchPanelTo("BUY_PRODUCT"));
        b2.addActionListener(_ -> switchPanelTo("ADD_PRODUCT"));
        b3.addActionListener(_ -> new SalesPanel(paymentRepository));
        b4.addActionListener(_ -> switchPanelTo("PRICE_ADJUSTMENT"));


        buttonlist.add(b1);
        buttonlist.add(b2);
        buttonlist.add(b3);
        buttonlist.add(b4);
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
        logout.addActionListener(_ -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            parentFrame.dispose();
            new MainFrame();
        });
        return logout;
    }

    private JButton addSlideButton() {
        toggleSideBarButton = new JButton() {
            private final Image hide = new ImageIcon(getClass().getResource("/Images/exit.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            private final Image show = new ImageIcon(getClass().getResource("/Images/show.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                Image image = isSideBarActive ? hide : show;
                if (isSideBarActive) {
                    g2d.drawImage(image, getWidth() - image.getWidth(null) + 10, (getHeight() - image.getHeight(null)) / 2, null);
                } else {
                    g2d.drawImage(image, getWidth() - image.getWidth(null) + 4, (getHeight() - image.getHeight(null)) / 2, null);
                }
                repaint();
                g2d.dispose();

            }
        };
        toggleSideBarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleSideBarButton.setPreferredSize(new Dimension(0, 50));
        toggleSideBarButton.setContentAreaFilled(false);
        toggleSideBarButton.setBorderPainted(false);
        toggleSideBarButton.setFocusPainted(false);
        toggleSideBarButton.setOpaque(false);
        toggleSideBarButton.addActionListener(_ -> animationSideBar());

        return toggleSideBarButton;
    }

    private void animationSideBar() {
        toggleSideBarButton.setEnabled(false);
        tableSettings.setEnabled(false);
        buttonlist.forEach(e -> e.setEnabled(false));


        timer = new Timer(0, _ -> {
            SIDEBAR_POSITION = isSideBarActive ? SIDEBAR_POSITION - 25 : SIDEBAR_POSITION + 25;
            transitionPanel.animatePanel(this, "pos 0 0, h 100%, w " + SIDEBAR_POSITION + "px");
            if (SIDEBAR_POSITION >= 250 || SIDEBAR_POSITION <= 50) {
                SIDEBAR_POSITION = isSideBarActive ? 50 : 250;
                tableSettings.setEnabled(isSideBarActive);
            }


            if (SIDEBAR_POSITION == 250 || SIDEBAR_POSITION == 50) {
                timer.stop();
                isSideBarActive = !isSideBarActive;
                buttonlist.forEach(e -> e.setActive(isSideBarActive));
                toggleSideBarButton.setEnabled(true);
            }


            if (SIDEBAR_POSITION == 125) buttonlist.forEach(e -> e.setActive(true));
            revalidate();
        });
        timer.setDelay(10);
        timer.start();
    }

    public void setListener(ResetPanel reset) {
        this.reset = reset;
    }

    @Override
    public void switchPanelTo(String panelName) {
        panelSwitching.switchPanelTo(panelName);
        tableSettings.refreshComboBOx();
        if (isSideBarActive) animationSideBar();
        reset.resetFields();
    }
}
