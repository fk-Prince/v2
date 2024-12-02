package Cart;

import Product.Entity.Payment;
import Product.Repository.PaymentRepository;

import java.io.IOException;
import java.util.Queue;
import java.util.Random;

public class CartService {
    private static String invoiceId = null;
    private final PaymentRepository paymentRepository;

    public CartService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public String getInvoiceId() {
        if (invoiceId == null) {
            generateId();
        }
        return invoiceId;
    }

    private void generateId() {
        Random random = new Random();
        while (true) {
            StringBuilder randomid = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                randomid.append(random.nextInt(10));
            }
            try {
                if (!paymentRepository.isInvoiceIdDuplicate(randomid.toString())) {
                    invoiceId = randomid.toString();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public double computeSubtotal(Queue<Payment> payments) {
        return payments.stream().mapToDouble(Payment::computeWithoutTax).sum();
    }

    public double computeTax(double subtotal) {
        return Payment.computeTax(subtotal);
    }

    public double computeTotalDue(double subtotal) {
        return Payment.computeTotalDue(subtotal);
    }

    public boolean processPayment(Queue<Payment> payments, double cash) {
        for (Payment payment : payments) {
            try {
                double totalDue = Payment.computeTotalDue(payment.computeWithoutTax());
                paymentRepository.doPayment(new Payment(
                        payment.getProductType(),
                        payment.getProductId(),
                        payment.getBrandName(),
                        payment.getProductQty(),
                        payment.getProductPrice(),
                        cash,
                        totalDue,
                        Integer.parseInt(invoiceId)
                ));
            } catch (Exception _) {
                 return false;
            }
        }
        return true;
    }

    public void resetInvoiceId() {
        invoiceId = null;
    }
}
