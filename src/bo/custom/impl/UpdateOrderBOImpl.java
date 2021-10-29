package bo.custom.impl;

import bo.custom.UpdateOrderBO;
import dao.DAOFactory;
import dao.custom.OrderDAO;
import dto.OrderDTO;
import java.sql.SQLException;

public class UpdateOrderBOImpl implements UpdateOrderBO {

    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER);/*Property injection has been injected*/

    @Override
    public boolean updateOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
       return orderDAO.update(orderDTO);
    }
}
