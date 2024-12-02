package BuyProduct;

import javax.swing.*;

public class BuyProductService {

    public String computeTotal(JTextField qtyField, JTextField priceField, int qty) {
        try {
            double quantity = Double.parseDouble(qtyField.getText().trim());
            if (quantity > qty) {
                return "INSUFFICIENT STOCK";
            }
            if (quantity <= 0) {
                return "INVALID QUANTITY";
            }
            double price = Double.parseDouble(priceField.getText().trim());
            double subtotal = price * quantity;
            return String.format("%.2f", subtotal);
        } catch (NumberFormatException e) {
            if (!qtyField.getText().isEmpty()) {
                return "INVALID INPUT";
            }else {
                return "";
            }
        }
    }


    public boolean isValidQuantity(String quantityText, int availableQty) {
        try {
            double quantity = Double.parseDouble(quantityText.trim());
            return quantity > 0 && quantity <= availableQty;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
