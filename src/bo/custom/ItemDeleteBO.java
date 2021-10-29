package bo.custom;

import bo.SuperBO;
import javafx.collections.ObservableList;
import dto.tm.ItemTm;

import java.sql.SQLException;

public interface ItemDeleteBO extends SuperBO {
    void deleteItem(ItemTm x, ObservableList<ItemTm> items) throws SQLException, ClassNotFoundException;
}
