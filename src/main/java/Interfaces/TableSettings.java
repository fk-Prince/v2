package Interfaces;

import Product.Entity.Product;

public interface TableSettings {
    void refreshComboBOx();

    void setEnabled(boolean bln);

    void reduceQtyInTable(Product product);

    void refreshTable();

    void refreshTableWhenTypeChoosen();
}
