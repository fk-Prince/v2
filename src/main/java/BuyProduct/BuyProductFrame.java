package BuyProduct;

import Cart.CartFrame;
import Interfaces.ProductSelected;
import Interfaces.ResetPanel;
import Interfaces.ShoppingCart;
import Numpad.Numpad;
import PointOfSale.ProductListTable;
import Product.Entity.Payment;
import Product.Entity.Product;
import Product.Repository.PaymentRepository;
import SwingComponents.AnimateMessage;
import SwingComponents.MyButton;
import SwingComponents.MyTextField2;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;


public class BuyProductFrame extends JPanel implements ProductSelected, ResetPanel {

    private final ProductListTable productListTable;
    private final Numpad numpad;
    private Product productSelected;

    private MyTextField2[] fields;
    private MyButton buyButton;
    private final Queue<Payment> paymentQueue;
    private final ShoppingCart shoppingCart;
    private final BuyProductService buyProductService;

    public BuyProductFrame(ProductListTable productListTable, Numpad numpad) {
        shoppingCart = productListTable;
        paymentQueue = new LinkedList<>();
        this.productListTable = productListTable;
        this.numpad = numpad;
        this.buyProductService = new BuyProductService();
        setLayout(new MigLayout("fill,insets 0"));
        title();
        inputs();
    }

    public void inputs() {
        JPanel inputHolder = new JPanel(new MigLayout("insets 0, fill,center"));
        String[] labels = {"Product Type", "Product ID", "Product BrandName", "Product Price"
                , "Product Quantity", "Subtotal"};
        fields = new MyTextField2[labels.length];

        int y = 20;
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            fields[i] = new MyTextField2();
            fields[i].setFocusListener(numpad);
            fields[i].setEditable(false);
            fields[i].setFocusable(false);
            if (i == 4) {
                displayTotal(fields[i]);
            }
            inputHolder.add(label, "pos 10% " + (y + 2) + "%,wrap");
            inputHolder.add(fields[i], "pos 45% " + y + "%,wrap,w 50%,h 8%!");

            y += 10;
        }

        buyButton = new MyButton("ADD TO CART", true, false);
        buyButton.addActionListener(_ -> {
            Payment payment = new Payment(
                    fields[0].getText().substring(0, 1).toUpperCase() + fields[0].getText().substring(1).toLowerCase(),
                    Integer.parseInt(fields[1].getText()),
                    fields[2].getText(),
                    Integer.parseInt(fields[4].getText()),
                    Double.parseDouble(fields[3].getText())
            );
            productListTable.reduceQtyInTable(payment);
            paymentQueue.offer(payment);
            shoppingCart.addToCart(paymentQueue);
            resetFields();
            AnimateMessage.showMessage("Added to Cart", false, (MigLayout) getLayout(), inputHolder, 25, 0, false);
        });
        buyButton.setEnabled(false);
        inputHolder.add(buyButton, "pos 35% " + y + "%,w 30%, h 60!");
        add(inputHolder, "dock center");
    }

    private void displayTotal(MyTextField2 field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void updateTotal() {
                fields[4].setEditable(!fields[0].getText().trim().isEmpty());
                fields[4].setFocusable(!fields[0].getText().trim().isEmpty());
                buyButton.setEnabled(buyProductService.isValidQuantity(fields[4].getText(), productSelected.getProductQty()));

                if (!fields[4].getText().isEmpty()) {
                    String total = buyProductService.computeTotal(fields[4], fields[3], productSelected.getProductQty());
                    fields[5].setText(total);
                } else {
                    fields[5].setText("");
                }
            }
        });
    }

    private void title() {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                FontMetrics fm = g2d.getFontMetrics();
                String title = "Purchase Product";
                g2d.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, 35);
                g2d.dispose();
            }
        };

        label.setFocusable(false);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.BLACK);
        label.setPreferredSize(new Dimension(1, 50));

        add(label, "dock north");
    }

    @Override
    public void productSelected(Product product) {
        resetFields();
        if (product != null) {
            productSelected = product;
            fields[0].setText(product.getProductType());
            fields[1].setText(String.valueOf(product.getProductId()));
            fields[2].setText(product.getBrandName());
            fields[3].setText(String.format("%.2f", product.getProductPrice()));
            if (product.getProductQty() == 0) {
                fields[4].setText("OUT OF STOCK");
                fields[5].setText("");
                buyButton.setEnabled(false);
                setField(false);
            } else {
                fields[4].setText("");
                setField(true);
            }
        }
    }

    private void setField(boolean isEditable) {
        fields[4].setEditable(isEditable);
        fields[4].setFocusable(isEditable);
    }

    @Override
    public void resetFields() {
        for (JTextField field : fields) {
            field.setText("");
        }
        fields[4].setEditable(false);
        fields[4].setFocusable(false);
    }
}
