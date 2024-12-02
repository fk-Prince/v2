package AddProduct;

import Interfaces.ProductSelected;
import Interfaces.ResetPanel;
import Interfaces.TableSettings;
import Numpad.Numpad;
import PriceAdjustment.PriceAdjustmentFrame;
import Product.Entity.Product;
import Product.Repository.ProductRepository;
import SwingComponents.AnimateMessage;
import SwingComponents.MyButton;
import SwingComponents.MyTextField2;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class AddProductFrame extends JPanel implements ResetPanel, ProductSelected {

    private final MyTextField2[] fields;
    private final JPanel inputHolder;
    private final AddProductService productService;

    public AddProductFrame(TableSettings tableSettings, ProductRepository productRepository, Numpad numpad, PriceAdjustmentFrame priceAdjustment) {
        setLayout(new MigLayout("fill, insets 0"));
        title();
        inputHolder = new JPanel(new MigLayout("insets 0, fill,center"));
        this.productService = new AddProductService(productRepository, tableSettings, priceAdjustment, this);
        String[] labels = {"Product Type", "Product ID", "Product BrandName", "Product Price", "Product Quantity"};
        fields = new MyTextField2[labels.length];

        int y = 25;
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            inputHolder.add(label, "pos 10% " + (y + 2) + "%,wrap");
            fields[i] = new MyTextField2();
            fields[i].setFocusListener(numpad);
            inputHolder.add(fields[i], "pos 40% " + y + "%,wrap,w 50%,h 8%!");
            y += 10;
        }

        MyButton registerButton = new MyButton("ADD PRODUCT", true, false);
        registerButton.addActionListener(_ -> registerProduct());
        inputHolder.add(registerButton, "pos 35% " + (y + 5) + "%, w 30%, h 60!");
        add(inputHolder, "dock center");
    }

    private void registerProduct() {
        try {
            for (JTextField field : fields) {
                if (field != null && field.getText().trim().isEmpty()) {
                    AnimateMessage.showMessage("Please Fill all Fields", true, (MigLayout) getLayout(), inputHolder, 25, 15,false);
                    return;
                }
            }

            String productType = fields[0].getText().trim();
            if (!productService.isDigit(fields[1].getText().trim())) {
                AnimateMessage.showMessage("PRODUCT ID SHOULD BE A NUMBER", true, (MigLayout) getLayout(), inputHolder, 25, 15,false);
                return;
            }
            int productId = Integer.parseInt(fields[1].getText().trim());
            String productName = fields[2].getText().trim();
            double productPrice = Double.parseDouble(fields[3].getText().trim());
            int productQty = Integer.parseInt(fields[4].getText().trim());

            boolean success = productService.registerProduct(productType, productId, productName, productPrice, productQty);

            if (success) {
                resetFields();
            }
        } catch (Exception e) {
            AnimateMessage.showMessage("INVALID INPUT", true, (MigLayout) getLayout(), inputHolder, 25, 15,false);
        }
    }


    private void title() {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                FontMetrics fm = g2d.getFontMetrics();
                String title = "Register Product";
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
    public void resetFields() {
        if (fields != null) {
            for (JTextField field : fields) {
                field.setText("");
            }
        }

    }

    @Override
    public void productSelected(Product product) {
        resetFields();
        if (product != null) {
            fields[0].setText(product.getProductType());
            fields[1].setText(String.valueOf(product.getProductId()));
            fields[2].setText(product.getBrandName());
            fields[3].setText(String.format("%.2f", product.getProductPrice()));
        }
    }
}
