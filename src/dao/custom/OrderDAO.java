package dao.custom;

import dao.CrudDAO;
import javafx.collections.ObservableList;
import dto.ItemDetails;
import dto.OrderDTO;
import dto.tm.CartTm;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDAO extends CrudDAO<OrderDTO,String> {
     String getOrderId() throws SQLException, ClassNotFoundException;
     boolean saveOrderDetail(OrderDTO orderDTO) throws SQLException, ClassNotFoundException;
     boolean updateItemTable(OrderDTO orderDTO) throws SQLException, ClassNotFoundException;
     boolean modifyOrderDetail(String orderId, ArrayList<ItemDetails> items) throws SQLException, ClassNotFoundException;
     void deleteOrder(ObservableList<CartTm> obList1, String orderId) throws SQLException, ClassNotFoundException;
     ResultSet searchByCustomerId(String id)throws SQLException, ClassNotFoundException;
}
