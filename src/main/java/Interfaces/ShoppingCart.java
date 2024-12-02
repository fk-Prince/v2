package Interfaces;

import Product.Entity.Payment;

import java.util.Queue;

public interface ShoppingCart {
    void addToCart(Queue<Payment> paymentQueue);
}
