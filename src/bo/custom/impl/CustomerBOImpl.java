package bo.custom.impl;

import bo.custom.CustomerBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dto.CustomerDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {

    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);/*Property injection has been injected*/

    @Override
    public List<String> getCustomerIds() throws SQLException, ClassNotFoundException {
        return customerDAO.getCustomerIds();
    }

    @Override
    public CustomerDTO searchForCustomer(String customerId) throws SQLException, ClassNotFoundException {
        return customerDAO.search(customerId);
    }

    @Override
    public ResultSet searchByName(String text) throws SQLException, ClassNotFoundException {
        return customerDAO.searchByName(text);
    }

    @Override
    public boolean saveCustomer(CustomerDTO c1) throws SQLException, ClassNotFoundException {
        return customerDAO.add(c1);
    }
}
