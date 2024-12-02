package PointOfSale;

import Interfaces.TableSettings;
import Product.Entity.Payment;
import Product.Entity.Product;
import Product.Repository.PaymentRepository;
import SwingComponents.MyTableModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SalesPanel extends JPanel implements TableSettings {

    private final PaymentRepository paymentRepository;

    private static JFrame frame;
    private JComboBox<String> productType;
    private List<Payment> paymentList;
    private DefaultTableModel model;
    private JTextField totalWithTax, totalWithoutTax, taxField;

    public SalesPanel(PaymentRepository paymentRepository) {
        setLayout(new MigLayout("fill,insets 0"));
        this.paymentRepository = paymentRepository;
        paymentList = new ArrayList<>();
        init();
        choices();
        table();
    }



    private void choices() {
        productType = new JComboBox<>();
        productType.setPreferredSize(new Dimension(150, 25));
        productType.addItem("All");
        Set<String> p = new HashSet<>();
        p.add("All");
        try {
            paymentList = paymentRepository.getPayments();
        } catch (Exception _) {

        }
        for (Payment payment : paymentList) {
            if (!p.contains(payment.getProductType())) {
                productType.addItem(payment.getProductType());
                p.add(payment.getProductType());
            }
        }
        productType.addActionListener(_ -> refreshTable());

        JTextField searchInvoice = new JTextField();
        searchInvoice.setPreferredSize(new Dimension(150, 30));
        searchInvoice.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                getInvoice();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                getInvoice();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            public void getInvoice() {


                try {
                    if (!searchInvoice.getText().trim().isEmpty()) {
                        int id = Integer.parseInt(searchInvoice.getText().trim());
                        if (productType != null) {
                            List<Payment> payments = paymentRepository.getByIdAndType(id, productType.getSelectedItem().toString());
                            System.out.println(payments.size());
                            model.setRowCount(0);
                            for (Payment payment : payments) {
                                model.addRow(new Object[]{
                                        payment.getInvoiceId(),
                                        payment.getProductType(),
                                        payment.getBrandName() + " / " + payment.getProductId(),
                                        payment.getProductQty(),
                                        String.format("%.2f",payment.getProductPrice()),
                                        String.format("%.2f",payment.getTotal())
                                });
                            }
                            displayTotal(payments);
                        }
                    } else {
                        refreshTable();
                    }
                } catch (Exception _) {

                }
            }
        });
        searchInvoice.setBorder(new EmptyBorder(5, 5, 5, 5));
        searchInvoice.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        displayGeneralSales();
        add(searchInvoice, "pos 40% 1%");
        add(new JLabel("Search By Invoice ID:"), "pos 22% 3%");
        add(productType, "pos 1% 2%");
    }

    private void displayGeneralSales() {
        totalWithoutTax = new JTextField(String.format("%.2f", paymentList.stream().mapToDouble(Payment::computeWithoutTax).sum()));
        totalWithoutTax.setHorizontalAlignment(SwingConstants.RIGHT);
        totalWithoutTax.setEditable(false);
        totalWithoutTax.setFocusable(false);
        totalWithoutTax.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(new JLabel("Generated Sales Without Tax: "), "pos 55% 81%");
        add(totalWithoutTax, "pos 77% 79%, w 150!,h 30!");

        taxField = new JTextField(String.format("%.2f", paymentList.stream().mapToDouble(Payment::computeTax).sum()));
        taxField.setHorizontalAlignment(SwingConstants.RIGHT);
        taxField.setEditable(false);
        taxField.setFocusable(false);
        taxField.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(new JLabel("Value Added Tax: "), "pos 64% 88%");
        add(taxField, "pos 77% 86%, w 150!,h 30!");

        totalWithTax = new JTextField(String.format("%.2f", Payment.computeTotalDue(paymentList.stream()
                .mapToDouble(Payment::computeWithoutTax).sum())));
        totalWithTax.setHorizontalAlignment(SwingConstants.RIGHT);
        totalWithTax.setEditable(false);
        totalWithTax.setFocusable(false);
        totalWithTax.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(new JLabel(" Generated Sales With Tax: "), "pos 57% 95%");
        add(totalWithTax, "pos 77% 93%, w 150!,h 30!");
    }

    private void init() {
        if (frame != null) frame.dispose();
        frame = new JFrame();
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.add(this);
    }

    private void table() {
        model = new DefaultTableModel();
        model.addColumn("INVOICE ID");
        model.addColumn("TYPE");
        model.addColumn("PRODUCT NAME/ID");
        model.addColumn("QTY");
        model.addColumn("PRICE");
        model.addColumn("SUB TOTAL");


        for (Payment payment : paymentList) {
            model.addRow(new Object[]{
                    payment.getInvoiceId(),
                    payment.getProductType(),
                    payment.getBrandName() + " / " + payment.getProductId(),
                    payment.getProductQty(),
                    String.format("%.2f",payment.getProductPrice()),
                    String.format("%.2f", payment.getTotal())
            });
        }

        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                MyTableModel renderer = new MyTableModel();
                if (column == 0) {
                    renderer.setHorizontalAlignment(SwingConstants.CENTER);
                    getColumnModel().getColumn(column).setPreferredWidth(50);
                } else if (column == 1) {
                    renderer.setHorizontalAlignment(SwingConstants.LEADING);
                    getColumnModel().getColumn(column).setPreferredWidth(120);
                } else if (column == 2) {
                    renderer.setHorizontalAlignment(SwingConstants.LEFT);
                    getColumnModel().getColumn(column).setPreferredWidth(180);
                } else if (column == 3) {
                    renderer.setHorizontalAlignment(SwingConstants.RIGHT);
                    getColumnModel().getColumn(column).setPreferredWidth(70);
                } else if (column == 4) {
                    renderer.setHorizontalAlignment(SwingConstants.RIGHT);
                    getColumnModel().getColumn(column).setPreferredWidth(80);
                } else if (column == 5) {
                    renderer.setHorizontalAlignment(SwingConstants.RIGHT);
                    getColumnModel().getColumn(column).setPreferredWidth(80);
                }

                return renderer;
            }


        };

        table.setIntercellSpacing(new Dimension(0, 0));
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setFocusable(false);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setBorder(null);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(table.getWidth(), 40));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        add(scroll, "pos 0 9%,h 70%, w 100%");
    }

    @Override
    public void refreshComboBOx() {

    }

    @Override
    public void reduceQtyInTable(Product product) {

    }

    @Override
    public void refreshTable() {

        model.setRowCount(0);

        for (Payment payment : paymentList) {
            if (productType.getSelectedItem().toString().equalsIgnoreCase("ALL")
                    || payment.getProductType().equalsIgnoreCase(productType.getSelectedItem().toString())) {
                model.addRow(new Object[]{
                        payment.getInvoiceId(),
                        payment.getProductType(),
                        payment.getBrandName() + " / " + payment.getProductId(),
                        payment.getProductQty(),
                        String.format("%.2f",payment.getProductPrice()),
                        String.format("%.2f",payment.getTotal())
                });
            }
        }

        displayTotal(paymentList);
    }

    private void displayTotal(List<Payment> paymentList) {
        double subtotal = paymentList.stream()
                .filter(payment -> productType.getSelectedItem().toString().equalsIgnoreCase("ALL")
                        || payment.getProductType().equalsIgnoreCase(productType.getSelectedItem().toString()))
                .mapToDouble(Payment::computeWithoutTax)
                .sum();
        double tax = paymentList.stream()
                .filter(payment -> productType.getSelectedItem().toString().equalsIgnoreCase("ALL")
                        || payment.getProductType().equalsIgnoreCase(productType.getSelectedItem().toString()))
                .mapToDouble(Payment::computeTax)
                .sum();
        double overallTotal = Payment.computeTotalDue(subtotal);


        totalWithoutTax.setText(String.format("%.2f", subtotal));
        taxField.setText(String.format("%.2f", tax));
        totalWithTax.setText(String.format("%.2f", overallTotal));
    }

    @Override
    public void refreshTableWhenTypeChoosen() {

    }

}
