package Product.Entity;

public class Product {
    private final int productId, productQty;
    private String productType;
    private final String brandName;
    private final double productPrice;

    public double getProductPrice() {
        return productPrice;
    }

    public String getBrandName() {
        return brandName;
    }

    public int getProductQty() {
        return productQty;
    }

    public int getProductId() {
        return productId;
    }

    public Product(int productId, String brandName, int productQty, double productPrice) {
        this.productId = productId;
        this.productQty = productQty;
        this.brandName = brandName;
        this.productPrice = productPrice;
    }

    public Product(String productType, int productId, String brandName, int productQty, double productPrice) {
        this.productType = productType;
        this.productId = productId;
        this.productQty = productQty;
        this.brandName = brandName;
        this.productPrice = productPrice;
    }

    public String toString() {
        return productType + "," + productId + "," + brandName + "," + productQty + "," + productPrice;
    }

    public String getProductType() {
        return productType;
    }


}
