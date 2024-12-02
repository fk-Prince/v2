package PriceAdjustment;

import Interfaces.ProductSelected;
import Interfaces.ResetPanel;
import Interfaces.TableSettings;
import Numpad.Numpad;
import PointOfSale.SalesPanel;
import Product.Entity.Product;
import Product.Repository.PaymentRepository;
import Product.Repository.ProductRepository;
import SwingComponents.AnimateMessage;
import SwingComponents.MyButton;
import SwingComponents.MyTextField2;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PriceAdjustmentFrame extends JPanel implements ProductSelected, ResetPanel {

    private final PaymentRepository paymentRepository;
    private final TableSettings productListTable;
    private final Numpad numpad;
    private final MarkupService markupService;
    private final MarkdownService markdownService;

    private JPanel inputHolder;
    private MyTextField2[] fields;
    private List<Product> productList;
    private MyTextField2 field;
    private Product productSelected;

    public PriceAdjustmentFrame(TableSettings productListTable, ProductRepository productRepository, PaymentRepository paymentRepository, Numpad numpad) {
        this.productListTable = productListTable;
        this.numpad = numpad;
        this.paymentRepository = paymentRepository;
        markdownService = new MarkdownService(productRepository);
        markupService = new MarkupService(productRepository);
        setLayout(new MigLayout("fill,insets 0"));
        title();
        init();
    }

    private void title() {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                FontMetrics fm = g2d.getFontMetrics();
                String title = "Price Adjustment";
                g2d.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, 50);
                g2d.dispose();
            }
        };

        label.setFocusable(false);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.BLACK);

        add(label, "dock north, h 20%!");
    }

    private void inputs() {
        String[] labels = {"Product Type", "Product BrandName", "Product ID", "Product Price"};
        fields = new MyTextField2[labels.length];
        inputHolder = new JPanel(new MigLayout("insets 0, fill,center"));
        int y = 10;
        for (int i = 0; i < fields.length; i++) {
            inputHolder.add(new JLabel(labels[i]), "pos 10% " + (y + 2) + "%,wrap");

            fields[i] = new MyTextField2();
            fields[i].setFocusable(false);
            fields[i].setEditable(false);
            inputHolder.add(fields[i], "pos 40% " + y + "%,w 50%,h 8%!");
            y += 10;
        }
        inputHolder.add(new JLabel("Markup / Markdown"), "pos 10% 60%");
        inputHolder.add(new JLabel("Percentage"), "pos 13% 63%");

        field = new MyTextField2();
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        field.setFocusListener(numpad);
        field.setEditable(false);
        field.setFocusable(false);
        inputHolder.add(field, "pos 40% 60%, w 50%!, h 8%!");

        JLabel label = new JLabel("%");
        label.setFont(new Font("Arial", Font.BOLD, 15));
        inputHolder.add(label, "pos 92% 62%");
        add(inputHolder, "dock center");
    }

    private void init() {
        inputs();

        MyButton myButton = new MyButton("View Sales", false, false);
        myButton.addActionListener(_ -> new SalesPanel(paymentRepository));
        inputHolder.add(myButton, "pos 20% 80%, w 150!, h 70!");

        MyButton markup = new MyButton("Markup", true, false);
        markup.addActionListener(_ -> markup());
        inputHolder.add(markup, "pos 60% 80%, w 15%!, h 70!");

        MyButton markdown = new MyButton("Markdown", true, false);
        markdown.addActionListener(_ -> markdown());
        inputHolder.add(markdown, "pos 75% 80%, w 15%!, h 70!");
    }

    private void markdown() {
        if (!valid()) {
            return;
        }
        double markdownPercentage = Double.parseDouble(field.getText().trim());
        boolean success = markdownService.markdown(markdownPercentage, fields);
        if (success) {
            AnimateMessage.showMessage("Operation Successful", false, (MigLayout) getLayout(), this, 25, 10, false);
            resetFields();
            productListTable.refreshComboBOx();
            productListTable.refreshTable();
        } else {
            AnimateMessage.showMessage("Markdown Percentage is to high.", true, (MigLayout) getLayout(), this, 25, 10, false);
        }
    }


    private void markup() {

        if (!valid()) {
            return;
        }
        double markupPercentage = Double.parseDouble(field.getText().trim());
        markupService.markup(markupPercentage, fields);
        AnimateMessage.showMessage("Operation Successful", false, (MigLayout) getLayout(), this, 25, 10, false);
        resetFields();
        productListTable.refreshComboBOx();
        productListTable.refreshTable();

    }

    private boolean isDigit(String x) {
        try {
            Double.parseDouble(x);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean valid() {
        if (productSelected == null) {
            AnimateMessage.showMessage("PLEASE SELECT A PRODUCT", true, (MigLayout) getLayout(), this, 25, 10, false);
            return false;
        }
        String inputPercentage = field.getText().trim();
        if (inputPercentage.isEmpty() || !isDigit(inputPercentage)) {
            AnimateMessage.showMessage("INVALID INPUT", true, (MigLayout) getLayout(), this, 25, 10, false);
            return false;
        }

        double markPercentage = Double.parseDouble(field.getText().trim());
        if (markPercentage < 1) {
            AnimateMessage.showMessage("INVALID INPUT", true, (MigLayout) getLayout(), this, 25, 10, false);
            return false;
        }
        return true;
    }


    @Override
    public void productSelected(Product product) {
        if (product != null) {
            this.productSelected = product;
            fields[0].setText(product.getProductType());
            fields[1].setText(product.getBrandName());
            fields[2].setText(String.valueOf(product.getProductId()));
            fields[3].setText(String.format("%.2f", product.getProductPrice()));
            field.setEditable(true);
            field.setFocusable(true);
        }
    }

    @Override
    public void resetFields() {
        field.setText("");
        field.setEditable(false);
        field.setFocusable(false);
        for (MyTextField2 field : fields) {
            field.setText("");
        }
    }
}
