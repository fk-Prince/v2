package Product.Entity;

public class Payment extends Product {
    private double cash;
    private double total;
    private int invoiceId;

    private static final double tax = 0.12;

    public int getInvoiceId() {
        return invoiceId;
    }

    public double getCash() {
        return cash;
    }

    public double getTotal() {
        return total;
    }

    public Payment(String productType, int productId, String brandName, int productQty, double productPrice) {
        super(productType, productId, brandName, productQty, productPrice);
    }

    public Payment(String productType, int productId, String brandName, int productQty, double productPrice, double cash, double total, int invoiceid) {
        super(productType, productId, brandName, productQty, productPrice);
        this.cash = cash;
        this.total = total;
        this.invoiceId = invoiceid;
    }


    public static double computeTax(double total) {
        return total * tax;
    }

    public double computeTax() {
        return total * tax;
    }

    public static double computeTotalDue(double subtotal) {
        return subtotal + computeTax(subtotal);
    }

    public double computeWithoutTax() {
        return getProductQty() * getProductPrice();
    }


    public String toString() {
        return invoiceId + "," + super.toString() + "," + cash + "," + total;
    }

}
