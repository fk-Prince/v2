package PointOfSale;

import AddProduct.AddProductFrame;
import BuyProduct.BuyProductFrame;
import Interfaces.SwitchPanel;
import Interfaces.TransitionPanel;
import Numpad.Numpad;
import PriceAdjustment.PriceAdjustmentFrame;
import Product.Repository.PaymentRepository;
import Product.Repository.ProductRepository;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PointOfSaleFrame extends JFrame implements TransitionPanel, SwitchPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel, mainPanel;
    private MigLayout migLayout;

    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private AddProductFrame addProduct;
    private BuyProductFrame buyProduct;
    private ProductListTable productListTable;
    private PriceAdjustmentFrame priceAdjustment;
    private SideBar sideBar;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PointOfSaleFrame::new);
    }

    public PointOfSaleFrame() {
        productRepository = new ProductRepository();
        paymentRepository = new PaymentRepository();
        init();
        initComponent();
        setVisible(true);
    }

    private void init() {
        String TITLE = "Point Of Sale";
        int WIDTH = 1300;
        int HEIGHT = 700;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setTitle(TITLE);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponent() {
        migLayout = new MigLayout("fill, insets 0 0 0 0");
        mainPanel = new JPanel(migLayout);
        mainPanel.setBackground(Color.WHITE);
        cardPanel = new JPanel(new CardLayout());
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        cardLayout = (CardLayout) cardPanel.getLayout();


        Numpad numpad = new Numpad();
        numpad.setBorder(new EmptyBorder(0, 10, 10, 10));

        productListTable = new ProductListTable(numpad);
        sideBar = new SideBar(this, this, productListTable);

        priceAdjustment = new PriceAdjustmentFrame(productListTable, productRepository, paymentRepository, numpad);
        addProduct = new AddProductFrame(productListTable, productRepository, numpad, priceAdjustment);
        buyProduct = new BuyProductFrame(productListTable, numpad);

        productListTable.setListener2(sideBar);
        sideBar.setListener(buyProduct);
        productListTable.setListener(buyProduct);

        cardPanel.add(addProduct, "ADD_PRODUCT");
        cardPanel.add(buyProduct, "BUY_PRODUCT");
        cardPanel.add(priceAdjustment, "PRICE_ADJUSTMENT");
        cardPanel.add(buyProduct, "BUY_PRODUCT");
        cardLayout.show(cardPanel, "BUY_PRODUCT");

        mainPanel.add(sideBar, "w 50px, pos 0 0, h 100%");
        mainPanel.add(productListTable, "w 56%, pos 4% 0, h 100%");
        mainPanel.add(cardPanel, "w 40%, pos 60% 0, h 70%");
        mainPanel.add(numpad, "w 40%, pos 60% 70%, h 30%");
        add(mainPanel);
    }

    @Override
    public void animatePanel(JPanel panel, String constraints) {
        migLayout.setComponentConstraints(panel, constraints);
        mainPanel.revalidate();
    }

    @Override
    public void switchPanelTo(String panelName) {
        cardLayout.show(cardPanel, panelName);

        if (panelName.equalsIgnoreCase("BUY_PRODUCT")) {
            productListTable.setListener(buyProduct);
            sideBar.setListener(buyProduct);
        } else if (panelName.equalsIgnoreCase("ADD_PRODUCT")) {
            productListTable.setListener(addProduct);
            sideBar.setListener(addProduct);
        } else if (panelName.equalsIgnoreCase("PRICE_ADJUSTMENT")) {
            productListTable.setListener(priceAdjustment);
            sideBar.setListener(priceAdjustment);
        }
        mainPanel.revalidate();
    }


}
