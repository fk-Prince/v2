package PointOfSale;

import Cart.CartFrame;
import Interfaces.ProductSelected;
import Interfaces.ShoppingCart;
import Interfaces.SwitchPanel;
import Interfaces.TableSettings;
import Numpad.Numpad;
import Product.Entity.Payment;
import Product.Entity.Product;
import Product.Repository.PaymentRepository;
import Product.Repository.ProductRepository;
import SwingComponents.AnimateMessage;
import SwingComponents.MyButton;
import SwingComponents.MyTableModel;
import SwingComponents.MyTextField2;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ProductListTable extends JPanel implements TableSettings, ShoppingCart {

    private PaymentRepository paymentRepository;
    private ProductSelected productSelected;
    private final ProductRepository productRepository;
    private final Numpad numpad;
    private SwitchPanel switchPanel;

    private MyTextField2 field;
    private Set<String> productTypeList;
    private JTable table;
    private DefaultTableModel model;
    private List<Product> productList;
    private JComboBox<String> productType;
    private Queue<Payment> paymentQueue;
    private CartFrame cart;
    private static MyButton currentButton;

    public ProductListTable(Numpad numpad) {
        this.numpad = numpad;
        paymentRepository = new PaymentRepository();
        productRepository = new ProductRepository();
        setLayout(new MigLayout("fill,insets 0"));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false);

        buttons();
        productChoice();
        displayData();
    }

    private void buttons() {
        JPanel buttonPanel = new JPanel(new GridLayout());
        buttonPanel.setBackground(Color.WHITE);

        MyButton addButton = new MyButton("Add", false, false);
        addButton.setPreferredSize(new Dimension(100, 50));
        addButton.addActionListener(_ -> {
            switchPanel.switchPanelTo("ADD_PRODUCT");
            highlightButton(addButton);
        });
        buttonPanel.add(addButton);

        MyButton buyButton = new MyButton("Buy", true, false);
        currentButton = buyButton;
        buyButton.setPreferredSize(new Dimension(100, 50));
        buyButton.addActionListener(_ -> {
            switchPanel.switchPanelTo("BUY_PRODUCT");
            highlightButton(buyButton);
        });
        buttonPanel.add(buyButton);

        MyButton markButton = new MyButton("Update", false, false);
        markButton.setPreferredSize(new Dimension(100, 50));
        markButton.addActionListener(_ -> {
            switchPanel.switchPanelTo("PRICE_ADJUSTMENT");
            highlightButton(markButton);
        });
        buttonPanel.add(markButton);


        MyButton salesButton = new MyButton("Sales", false, false);
        salesButton.setPreferredSize(new Dimension(100, 50));
        salesButton.addActionListener(_ -> new SalesPanel(paymentRepository));
        buttonPanel.add(salesButton);

        MyButton cartButton = new MyButton("Cart", false, false);
        cartButton.setPreferredSize(new Dimension(100, 50));
        cartButton.addActionListener(_ -> cart = new CartFrame(paymentQueue, paymentRepository));
        buttonPanel.add(cartButton);

        MyButton resetButton = new MyButton("Clear", false, false);
        resetButton.setPreferredSize(new Dimension(100, 50));
        resetButton.addActionListener(_ -> {
            if (paymentQueue != null && !paymentQueue.isEmpty()) {
                AnimateMessage.showMessage("Cart Reset", true, (MigLayout) getLayout(), this, 28, 0, false);
                paymentQueue.clear();
            }
            refreshComboBOx();
            refreshTable();
            if (cart != null) {
                cart.resetFields();
            }
        });
        buttonPanel.add(resetButton);
        add(buttonPanel, "pos 50% 2%, w 50%, h 50!");
    }

    private void highlightButton(MyButton button) {
        if (currentButton != null) {
            currentButton.setPrimary(false);
        }
        currentButton = button;
        currentButton.setPrimary(true);
    }

    private void productChoice() {
        JPanel panel = new JPanel(new MigLayout("insets 0, gap 0, fillx", "[]", "[]2[]"));
        panel.setOpaque(false);
        JLabel label = new JLabel("Select Product Type: ");
        panel.add(label, "split 2");
        productType = new JComboBox<>();
        productType.addItem("ALL");
        refreshComboBOx();
        productType.setPreferredSize(new Dimension(150, 25));
        productType.addActionListener(_ -> refreshTable());
        panel.add(productType, "wrap");

        JLabel label2 = new JLabel("Search by Product Id:");
        panel.add(label2, "split 2");

        field = new MyTextField2();
        field.setFocusListener(numpad);
        field.setPreferredSize(new Dimension(150, 30));
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                refreshTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refreshTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }


        });
        panel.add(field);

        add(panel, "h 2%, w 100%, wrap");
    }


    private void displayData() {
        model = new DefaultTableModel();
        model.addColumn("PRODUCT TYPE");
        model.addColumn("PRODUCT ID");
        model.addColumn("BRAND NAME");
        model.addColumn("STOCK");
        model.addColumn("PRICE");

        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                DefaultTableCellRenderer renderer;

                if (table.isEnabled()) {
                    renderer = new MyTableModel();
                } else {
                    renderer = new DefaultTableCellRenderer();
                }
                if (column == 0) {
                    getColumnModel().getColumn(column).setPreferredWidth(150);
                } else if (column == 1) {
                    getColumnModel().getColumn(column).setPreferredWidth(100);
                } else if (column == 2) {
                    getColumnModel().getColumn(column).setPreferredWidth(200);
                } else if (column == 3 || column == 4) {
                    getColumnModel().getColumn(column).setPreferredWidth(80);
                }
                renderer.setHorizontalAlignment(SwingConstants.CENTER);
                return renderer;
            }

        };

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                if (productSelected == null) return;

                int id = (int) model.getValueAt(row, 1);
                String type = model.getValueAt(row, 0).toString();
                for (Product product : productList) {
                    if (product.getProductId() == id && product.getProductType().equalsIgnoreCase(type)) {
                        productSelected.productSelected(product);
                        break;
                    }
                }
            }
        });
        table.setRowHeight(35);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setBackground(Color.WHITE);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        displayProductType();

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setFont(new Font("Arial", Font.PLAIN, 14));
        header.setForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        add(scroll, "h 78%, w 100%");
    }

    public void displayProductType() {
        try {
            productList = productRepository.getProductByType((String) productType.getSelectedItem());
        } catch (IOException _) {

        }
        model.setRowCount(0);
        productList.forEach(product -> model.addRow(
                new Object[]{product.getProductType(),
                        product.getProductId(),
                        product.getBrandName(),
                        product.getProductQty(),
                        String.format("%.2f", product.getProductPrice())
                }));
    }

    @Override
    public void refreshComboBOx() {
        if (productTypeList == null) productTypeList = new HashSet<>();
        productTypeList.clear();
        productType.removeAllItems();
        productType.addItem("ALL");

        try {
            productList = productRepository.getProductByType("ALL");
            for (Product product : productList) {
                if (!productTypeList.contains(product.getProductType())) {
                    productType.addItem(product.getProductType());
                    productTypeList.add(product.getProductType());
                }
            }
        } catch (IOException _) {

        }

        if (!productType.getSelectedItem().toString().equalsIgnoreCase("ALL")) {
            refreshTableWhenTypeChoosen();
        }

    }

    @Override
    public void refreshTableWhenTypeChoosen() {
        model.setRowCount(0);
        String selectedType = (String) productType.getSelectedItem();
        try {
            productList = productRepository.getProductByType(selectedType);
        } catch (IOException _) {
            return;
        }

        for (Product product : productList) {
            model.addRow(new Object[]{
                    product.getProductType(),
                    product.getProductId(),
                    product.getBrandName(),
                    product.getProductQty(),
                    String.format("%.2f", product.getProductPrice())
            });
        }
    }

    @Override
    public void setEnabled(boolean bln) {
        table.setEnabled(bln);
        table.setFocusable(bln);
        productType.setEnabled(bln);
        field.setEnabled(bln);
    }

    @Override
    public void reduceQtyInTable(Product product) {
        int i = 0;
        for (Product p : productList) {
            if (product.getProductId() == p.getProductId() && product.getProductType().equalsIgnoreCase(p.getProductType())) {
                int stock = p.getProductQty() - product.getProductQty();
                productList.set(i, new Product(p.getProductType(), p.getProductId(), p.getBrandName(), stock, p.getProductPrice()));
                refreshTable();
                break;
            }
            i++;
        }
    }

    @Override
    public void refreshTable() {
        model.setRowCount(0);
        if (productType == null) return;

        String selectedType = (productType.getSelectedItem() != null)
                ? productType.getSelectedItem().toString()
                : "ALL";

        if (productList == null) return;
        for (Product product : productList) {

            String searchText = field.getText().trim();


            if ((selectedType.equalsIgnoreCase("ALL") || product.getProductType().equalsIgnoreCase(selectedType))
                    && (searchText.isEmpty() || Integer.toString(product.getProductId()).startsWith(searchText))) {
                model.addRow(new Object[]{
                        product.getProductType(),
                        product.getProductId(),
                        product.getBrandName(),
                        product.getProductQty(),
                        String.format("%.2f", product.getProductPrice())
                });
            }
        }
    }

    public void setListener(ProductSelected productSelected) {
        this.productSelected = productSelected;
    }


    @Override
    public void addToCart(Queue<Payment> paymentQueue) {
        this.paymentQueue = paymentQueue;
    }

    public void setListener2(SwitchPanel switchPanel) {
        this.switchPanel = switchPanel;
    }
}
