package bo.custom.impl;

import bo.custom.UpdateItemBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dto.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class UpdateItemBOImpl implements UpdateItemBO {
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        return itemDAO.getAll();
    }

    @Override
    public ItemDTO searchForItem(String id) throws SQLException, ClassNotFoundException {
        return itemDAO.search(id);
    }
}
