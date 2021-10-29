package bo.custom.impl;

import bo.custom.OrderDetailBO;
import dao.DAOFactory;
import dao.custom.OrderDetailDAO;
import dto.ItemDetails;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailBOImpl implements OrderDetailBO {
    /*property injection has been injected*/
    private final OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);

    @Override
    public ArrayList<ItemDetails> getAllItemDetails() throws SQLException, ClassNotFoundException {
        return orderDetailDAO.getAll();
    }
}
