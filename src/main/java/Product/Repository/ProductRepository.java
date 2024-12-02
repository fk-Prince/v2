package Product.Repository;

import Product.Entity.Product;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductRepository {

    private final File productFile = new File("products.txt");

    public ProductRepository() {
        try {
            if (!productFile.exists()) productFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProductByType(String productType) throws IOException {
        return new BufferedReader(new FileReader(productFile))
                .lines()
                .map(line -> line.split(","))
                .map(lines -> new Product(
                        lines[0],
                        Integer.parseInt(lines[1]),
                        lines[2],
                        Integer.parseInt(lines[3]),
                        Double.parseDouble(lines[4])))
                .filter(product -> productType.equalsIgnoreCase("ALL") || product.getProductType().equalsIgnoreCase(productType))
                .sorted(Comparator.comparingInt(Product::getProductId))
                .collect(Collectors.toList());

    }


    public boolean isIdDuplicate(int productId) throws IOException {
        return new BufferedReader(new FileReader(productFile))
                .lines()
                .map(lines -> lines.split(","))
                .anyMatch(lines -> productId == Integer.parseInt(lines[1]));
    }

    public void addNewProduct(Product newProduct) throws IOException {
        boolean duplicate = false;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(productFile));
        String line;
        while ((line = br.readLine()) != null) {
            String[] lines = line.split(",");
            if (newProduct.getProductType().equalsIgnoreCase(lines[0])
                    && newProduct.getProductId() == Integer.parseInt(lines[1])
                    && newProduct.getBrandName().equalsIgnoreCase(lines[2])
                    && newProduct.getProductPrice() == Double.parseDouble(lines[4])) {
                sb.append(newProduct.getProductType()).append(",")
                        .append(newProduct.getProductId()).append(",")
                        .append(newProduct.getBrandName()).append(",")
                        .append(Integer.parseInt(lines[3]) + newProduct.getProductQty()).append(",")
                        .append(newProduct.getProductPrice()).append("\n");
                duplicate = true;
            } else {
                sb.append(line).append("\n");
            }
        }
        if (!duplicate) {
            sb.append(newProduct.getProductType()).append(",")
                    .append(newProduct.getProductId()).append(",")
                    .append(newProduct.getBrandName()).append(",")
                    .append(newProduct.getProductQty()).append(",")
                    .append(newProduct.getProductPrice()).append("\n");
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(productFile));
        bw.write(sb.toString());
        bw.close();
    }

    public void updateProduct(Product newProduct) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(productFile));
        String line;
        System.out.println(newProduct.getProductQty() + " new new");
        while ((line = br.readLine()) != null) {
            String[] lines = line.split(",");
            if (newProduct.getProductType().equalsIgnoreCase(lines[0])
                    && newProduct.getProductId() == Integer.parseInt(lines[1])
                    && newProduct.getBrandName().equalsIgnoreCase(lines[2])
                    && newProduct.getProductPrice() == Double.parseDouble(lines[4])) {

                sb.append(newProduct.getProductType()).append(",")
                        .append(newProduct.getProductId()).append(",")
                        .append(newProduct.getBrandName()).append(",")
                        .append(Integer.parseInt(lines[3]) + newProduct.getProductQty()).append(",")
                        .append(newProduct.getProductPrice()).append("\n");
            } else {
                sb.append(line).append("\n");
            }
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(productFile));
        bw.write(sb.toString());
        bw.close();
    }


    public void updateProducts(List<Product> newProductPrice) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(productFile));
        StringBuilder sb = new StringBuilder();
        for (Product product : newProductPrice) {
            sb.append(product.toString()).append("\n");
        }
        bw.write(sb.toString());
        bw.close();
    }

    public Product getProductById(int id) throws IOException {
        return new BufferedReader(new FileReader(productFile))
                .lines()
                .map(lines -> lines.split(","))
                .filter(lines -> id == Integer.parseInt(lines[1]))
                .map(lines -> new Product(
                        lines[0],
                        Integer.parseInt(lines[1]),
                        lines[2],
                        Integer.parseInt(lines[3]),
                        Double.parseDouble(lines[4])
                ))
                .findFirst()
                .orElse(null);
    }

    public List<Product> getAllProducts() throws IOException {
        return new BufferedReader(new FileReader(productFile))
                .lines().map(lines -> lines.split(","))
                .map(lines ->
                        new Product(
                                lines[0],
                                Integer.parseInt(lines[1]),
                                lines[2],
                                Integer.parseInt(lines[3]),
                                Double.parseDouble(lines[4])
                        ))
                .collect(Collectors.toList());
    }
}
