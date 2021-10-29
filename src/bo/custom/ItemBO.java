package bo.custom;

import bo.SuperBO;
import dto.ItemDTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ItemBO extends SuperBO {
    List<String> getItemIds() throws SQLException, ClassNotFoundException;

    ItemDTO searchForItemId(String id) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException;

    boolean saveItem(ItemDTO itemDTO) throws SQLException, ClassNotFoundException;
}
