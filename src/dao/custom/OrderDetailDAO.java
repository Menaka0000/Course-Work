package dao.custom;

import dao.CrudDAO;
import dto.ItemDetails;
import java.sql.SQLException;

public interface OrderDetailDAO extends CrudDAO<ItemDetails,String> {
    boolean updateWhenOrderUpdating(String orderId,String itemId,int getQtyForSell)throws SQLException, ClassNotFoundException;
    ItemDetails searchByItemId(String itemId,String orderId)throws SQLException, ClassNotFoundException;
}
