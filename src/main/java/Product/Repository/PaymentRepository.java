package Product.Repository;

import Product.Entity.Payment;
import Product.Entity.Product;

import java.io.*;
import java.util.Comparator;
import java.util.List;

public class PaymentRepository {
    private final File paymentFile = new File("payment.txt");

    public boolean isInvoiceIdDuplicate(String id) throws IOException {
        if (!paymentFile.exists()) paymentFile.createNewFile();
        return new BufferedReader(new FileReader(paymentFile))
                .lines().map(lines -> lines.split(","))
                .anyMatch(lines -> id.equalsIgnoreCase(lines[0]));
    }

    public void doPayment(Payment payment) throws IOException {
        if (!paymentFile.exists()) paymentFile.createNewFile();
        inventory(payment);
        System.out.println(payment.getTotal());
        BufferedWriter bw = new BufferedWriter(new FileWriter(paymentFile, true));
        bw.write(payment.toString() + "\n");
        bw.close();
    }

    private void inventory(Payment payment) throws IOException {
        File productFile = new File("products.txt");
        if (!productFile.exists()) productFile.createNewFile();
        BufferedReader br = new BufferedReader(new FileReader(productFile));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            String[] lines = line.split(",");
            if (payment.getProductType().equalsIgnoreCase(lines[0]) && payment.getProductId() == Integer.parseInt(lines[1])) {
                int newQty = Integer.parseInt(lines[3]) - payment.getProductQty();
                if (newQty < 0) newQty = 0;
                sb.append(lines[0]).append(",")
                        .append(Integer.parseInt(lines[1])).append(",")
                        .append(lines[2]).append(",")
                        .append(newQty).append(",")
                        .append(Double.parseDouble(lines[4])).append("\n");
            } else {
                sb.append(line).append("\n");
            }
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(productFile));
        bw.write(sb.toString());
        bw.close();
    }


    public List<Payment> getPayments() throws IOException {
        if (!paymentFile.exists()) paymentFile.createNewFile();
        return new BufferedReader(new FileReader(paymentFile))
                .lines()
                .map(line -> line.split(","))
                .map(lines -> new Payment(
                        lines[1], Integer.parseInt(lines[2]),
                        lines[3], Integer.parseInt(lines[4]),
                        Double.parseDouble(lines[5]),
                        Double.parseDouble(lines[6]),
                        Double.parseDouble(lines[7]),
                        Integer.parseInt(lines[0])))
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();

    }

    public List<Payment> getByIdAndType(int id, String type) throws IOException {
        if (!paymentFile.exists()) paymentFile.createNewFile();
        return new BufferedReader(new FileReader(paymentFile))
                .lines()
                .map(lines -> lines.split(","))
                .filter(lines -> lines[0].startsWith(String.valueOf(id)) &&
                        (type.equalsIgnoreCase(lines[1]) || type.equalsIgnoreCase("ALL")))
                .map(lines -> new Payment(
                        lines[1], Integer.parseInt(lines[2]),
                        lines[3], Integer.parseInt(lines[4]),
                        Double.parseDouble(lines[5]), Double.parseDouble(lines[6]),
                        Double.parseDouble(lines[7]), Integer.parseInt(lines[0])))
                .sorted(Comparator.comparingInt(Product::getProductId))
                .toList();
    }

}

