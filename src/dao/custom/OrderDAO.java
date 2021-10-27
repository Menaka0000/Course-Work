package dao.custom;

import dao.CrudDAO;
import javafx.collections.ObservableList;
import model.ItemDetails;
import model.Order;
import model.tm.CartTm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDAO extends CrudDAO<Order,String> {
     String getOrderId() throws SQLException, ClassNotFoundException;
     boolean saveOrderDetail(Order order) throws SQLException, ClassNotFoundException;
     boolean updateItemTable(Order order) throws SQLException, ClassNotFoundException;
     void modifyOrderDetail(String orderId, ArrayList<ItemDetails> items) throws SQLException, ClassNotFoundException;
     void deleteOrder(ObservableList<CartTm> obList1, String orderId) throws SQLException, ClassNotFoundException;
     ResultSet searchByCustomerId(String id)throws SQLException, ClassNotFoundException;
}
