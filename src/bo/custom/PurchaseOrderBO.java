package bo.custom;

import bo.SuperBO;
import javafx.collections.ObservableList;
import dto.OrderDTO;
import dto.tm.CartTm;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface PurchaseOrderBO extends SuperBO {
    boolean purchaseOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException;

    String getOrderId() throws SQLException, ClassNotFoundException;

    ResultSet searchByCustomerId(String x) throws SQLException, ClassNotFoundException;

    void deleteOrder(ObservableList<CartTm> obList1, String text) throws SQLException, ClassNotFoundException;
}
