package bo.custom.impl;

import bo.custom.ItemDeleteBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import javafx.collections.ObservableList;
import dto.tm.ItemTm;

import java.sql.SQLException;

public class ItemDeleteBOImpl implements ItemDeleteBO {

    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);/*property injection has been injected*/

    @Override
    public void deleteItem(ItemTm x, ObservableList<ItemTm> items) throws SQLException, ClassNotFoundException {
        itemDAO.deleteItem(x,items);
    }
}
