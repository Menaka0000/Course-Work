package dao;
import model.Customer;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO<Customer,String> {
//     boolean saveCustomer(Customer c) throws SQLException, ClassNotFoundException;
     List<String> getCustomerIds() throws SQLException, ClassNotFoundException;
//     Customer getCustomer(String id) throws SQLException, ClassNotFoundException;
}
