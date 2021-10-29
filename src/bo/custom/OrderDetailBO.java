package bo.custom;

import bo.SuperBO;
import dto.ItemDetails;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDetailBO extends SuperBO {
    ArrayList<ItemDetails> getAllItemDetails() throws SQLException, ClassNotFoundException;
}
