package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.OrderDetailDAO;
import model.ItemDetails;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public boolean add(ItemDetails itemDetails) throws SQLException, ClassNotFoundException {
        if (CrudUtil.executeUpdate("INSERT INTO `Order Detail` VALUES (?,?,?,?)",itemDetails.getItemCode(),itemDetails.getOrderId(),itemDetails.getQtyForSell(),itemDetails.getUnitPrice())){}else{return false;}
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("DELETE FROM `order detail` WHERE orderId=?",id);
    }

    @Override
    public boolean update(ItemDetails itemDetails) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean updateWhenOrderUpdating(String id,int getQtyForSell) throws SQLException, ClassNotFoundException {
       return CrudUtil.executeUpdate("UPDATE `order detail` SET qty=? WHERE orderId=?",getQtyForSell,id);
    }

    @Override
    public ItemDetails search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `order Detail` WHERE  orderId=?", id);
        rst.next();
        return new ItemDetails(rst.getString(2),rst.getString(1),rst.getDouble(4),rst.getInt(3));
    }

    @Override
    public ArrayList<ItemDetails> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDetails> itemDetails=new ArrayList<>();
        ResultSet rst = CrudUtil.executeQuery("SELECT * From `order Detail`");
        while(rst.next()){
            itemDetails.add(new ItemDetails(rst.getString(1),rst.getString(2),rst.getInt(3),rst.getInt(4)));
        }
        return itemDetails;
    }
}
