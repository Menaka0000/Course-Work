package bo.custom;

import model.Order;

import java.sql.SQLException;

public interface PurchaseOrderBO {
    boolean purchaseOrder(Order order) throws SQLException, ClassNotFoundException;
}
