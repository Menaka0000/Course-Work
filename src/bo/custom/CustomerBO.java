package bo.custom;

import bo.SuperBO;
import dto.CustomerDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface CustomerBO extends SuperBO {
    List<String> getCustomerIds() throws SQLException, ClassNotFoundException;

    CustomerDTO searchForCustomer(String customerId) throws SQLException, ClassNotFoundException;

    ResultSet searchByName(String text) throws SQLException, ClassNotFoundException;

    boolean saveCustomer(CustomerDTO c1) throws SQLException, ClassNotFoundException;
}
