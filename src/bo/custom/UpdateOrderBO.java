package bo.custom;

import bo.SuperBO;
import dto.OrderDTO;

import java.sql.SQLException;

public interface UpdateOrderBO extends SuperBO {
    boolean updateOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException;
}
