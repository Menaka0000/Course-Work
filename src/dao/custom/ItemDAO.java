package dao.custom;

import dao.CrudDAO;
import javafx.collections.ObservableList;
import model.Item;
import model.tm.FullDetailedItemTm;
import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<Item,String> {
     List<String> getItemIds() throws SQLException, ClassNotFoundException ;
     void deleteItem(FullDetailedItemTm item, ObservableList<FullDetailedItemTm> items) throws SQLException, ClassNotFoundException;
}