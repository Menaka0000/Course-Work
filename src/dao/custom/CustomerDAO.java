package dao.custom;
import dao.CrudDAO;
import model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO<Customer,String> {
     List<String> getCustomerIds() throws SQLException, ClassNotFoundException;
     ResultSet searchByName(String name)throws SQLException, ClassNotFoundException;
}
