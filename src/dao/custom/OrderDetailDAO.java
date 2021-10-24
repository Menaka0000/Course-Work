package dao.custom;

import dao.CrudDAO;
import model.ItemDetails;

import java.sql.SQLException;

public interface OrderDetailDAO extends CrudDAO<ItemDetails,String> {
    boolean updateWhenOrderUpdating(String id,int getQtyForSell)throws SQLException, ClassNotFoundException;
}
