package dao;

import javafx.collections.ObservableList;
import model.Item;
import model.tm.FullDetailedItemTm;
import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<Item,String>{
//     boolean saveItem(Item i) throws SQLException, ClassNotFoundException ;
     List<String> getItemIds() throws SQLException, ClassNotFoundException ;
//     Item getItem(String id) throws SQLException, ClassNotFoundException;
//     void updateItem(Item item) throws SQLException, ClassNotFoundException;
     void deleteItem(FullDetailedItemTm item, ObservableList<FullDetailedItemTm> items) throws SQLException, ClassNotFoundException;
}
