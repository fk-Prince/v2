package AddProduct;

import Interfaces.TableSettings;
import PriceAdjustment.PriceAdjustmentFrame;
import Product.Entity.Product;
import Product.Repository.ProductRepository;
import SwingComponents.AnimateMessage;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class AddProductService {
    private final ProductRepository productRepository;
    private final TableSettings tableSettings;
    private final PriceAdjustmentFrame priceAdjustment;
    private final JPanel parentPanel;

    public AddProductService(ProductRepository productRepository, TableSettings tableSettings, PriceAdjustmentFrame priceAdjustment, JPanel parentPanel) {
        this.productRepository = productRepository;
        this.tableSettings = tableSettings;
        this.priceAdjustment = priceAdjustment;
        this.parentPanel = parentPanel;
    }

    public boolean registerProduct(String productType, int productId, String productName, double productPrice, int productQty) {
        try {
            if (productQty <= 0 || productPrice <= 0) {
                AnimateMessage.showMessage("INVALID INPUT", true, (MigLayout) parentPanel.getLayout(), parentPanel, 25, 10, false);
                return false;
            }

            boolean isIdDuplicate = productRepository.isIdDuplicate(productId);
            if (isIdDuplicate) {
                Product existingProduct = productRepository.getProductById(productId);
                double formatPrice = Double.parseDouble(String.format("%.2f", productPrice));
                if (existingProduct.getBrandName().equalsIgnoreCase(productName)
                        && formatPrice == Double.parseDouble(String.format("%.2f", existingProduct.getProductPrice()))
                        && existingProduct.getProductType().equalsIgnoreCase(productType)) {
                    AnimateMessage.showMessage("Product Updated", false, (MigLayout) parentPanel.getLayout(), parentPanel, 25, 15, false);
                    productRepository.updateProduct(new Product(productType, productId, productName, productQty, formatPrice));
                } else {
                    AnimateMessage.showMessage("Duplicate ID", true, (MigLayout) parentPanel.getLayout(), parentPanel, 25, 15, false);
                    return false;
                }
            } else {
                Product newProduct = new Product(productType, productId, productName, productQty, productPrice);
                productRepository.addNewProduct(newProduct);
                AnimateMessage.showMessage("Product Added", false, (MigLayout) parentPanel.getLayout(), parentPanel, 25, 15, false);
            }

            tableSettings.refreshComboBOx();
            tableSettings.refreshTable();
            priceAdjustment.resetFields();
            return true;
        } catch (Exception e) {
            AnimateMessage.showMessage("ERROR", true, (MigLayout) parentPanel.getLayout(), parentPanel, 25, 15, false);
            return false;
        }
    }

    public boolean isDigit(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
