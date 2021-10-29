package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.CustomerDAO;
import dto.CustomerDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public boolean add(CustomerDTO c) throws SQLException, ClassNotFoundException {
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
            ids.add(rst.getString(1));
        }
        return ids;
    }

    @Override
    public ResultSet searchByName(String name) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeQuery("SELECT * FROM `customer` WHERE CustName=?",name);
    }

    @Override
    public CustomerDTO search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customer WHERE CustId=?",id);
        if (rst.next()) {
            return new CustomerDTO(
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
    public ArrayList<CustomerDTO> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This method has not been implemented");
    }
    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This method has not been implemented");
    }

    @Override
    public boolean update(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This method has not been implemented");
    }


}
