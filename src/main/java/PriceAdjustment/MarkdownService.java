package PriceAdjustment;

import Product.Entity.Product;
import Product.Repository.ProductRepository;
import SwingComponents.MyTextField2;

import java.util.List;

public class MarkdownService {
    private final ProductRepository productRepository;

    public MarkdownService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean markdown(double markdownPercentage, MyTextField2[] fields) {
        try {
            String selectedProductType = fields[0].getText().trim();
            int selectedProductId = Integer.parseInt(fields[2].getText().trim());
            List<Product> productList = productRepository.getAllProducts();

            int i = 0;
            for (Product product : productList) {
                if (selectedProductType.equalsIgnoreCase(product.getProductType()) && selectedProductId == product.getProductId()) {
                    double newSellingPrice = product.getProductPrice() * (1 - (markdownPercentage / 100.0));
                    double formatPrice = Double.parseDouble(String.format("%.2f",newSellingPrice));
                    if (formatPrice < 1) {
                        return false;
                    }
                    productList.set(i, new Product(
                            product.getProductType(),
                            product.getProductId(),
                            product.getBrandName(),
                            product.getProductQty(),
                            formatPrice
                    ));
                }
                i++;
            }
            productRepository.updateProducts(productList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
