package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.CustomerDAO;
import model.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public boolean add(Customer c) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `customer`");
        while(rst.next()){
            if(rst.getString(1).equals(c.getId())){
                return false;
            }
        }
        return CrudUtil.executeUpdate("INSERT INTO `customer` VALUES(?,?,?,?,?,?)",c.getId(),c.getName(),
                c.getAddress(),c.getCity(),c.getProvince(),c.getPostalCode());
    }


    @Override
    public List<String> getCustomerIds() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.executeQuery("SELECT * FROM `customer`");
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(
                    rst.getString(1)
            );
        }
        return ids;
    }

    @Override
    public ResultSet searchByName(String name) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeQuery("SELECT * FROM `customer` WHERE CustName=?",name);
    }

    @Override
    public Customer search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customer WHERE CustId=?",id);
        if (rst.next()) {
            return new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Customer> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }
    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Customer customer) throws SQLException, ClassNotFoundException {
        return false;
    }


}
