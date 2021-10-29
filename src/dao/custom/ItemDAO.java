package dao.custom;

import dao.CrudDAO;
import javafx.collections.ObservableList;
import dto.ItemDTO;
import dto.ItemDetails;
import dto.tm.ItemTm;
import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<ItemDTO,String> {
     List<String> getItemIds() throws SQLException, ClassNotFoundException ;
     void deleteItem(ItemTm item, ObservableList<ItemTm> items) throws SQLException, ClassNotFoundException;
     boolean updateWhenOrderIsPurchasing(ItemDetails item)throws SQLException, ClassNotFoundException;
     boolean updateWhenOrderIsBeingUpdating(String id,int updatedValue)throws SQLException, ClassNotFoundException;
}
