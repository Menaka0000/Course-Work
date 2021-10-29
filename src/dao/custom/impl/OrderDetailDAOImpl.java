package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.OrderDetailDAO;
import dto.ItemDetails;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    @Override
    public boolean add(ItemDetails itemDetails) throws SQLException, ClassNotFoundException {
        if (!CrudUtil.executeUpdate("INSERT INTO `Order Detail` VALUES (?,?,?,?)",itemDetails.getItemCode(),itemDetails.getOrderId(),itemDetails.getQtyForSell(),itemDetails.getUnitPrice())){
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("DELETE FROM `order detail` WHERE orderId=?",id);
    }

    @Override
    public boolean updateWhenOrderUpdating(String orderId,String itemId,int getQtyForSell) throws SQLException, ClassNotFoundException {
       return CrudUtil.executeUpdate("UPDATE `order detail` SET qty=? WHERE orderId=? AND itemCode=?",getQtyForSell,orderId,itemId);
    }

    @Override
    public ItemDetails search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `order Detail` WHERE  orderId=?", id);
        rst.next();
        return new ItemDetails(rst.getString(2),rst.getString(1),rst.getDouble(4),rst.getInt(3));
    }

    @Override
    public ItemDetails searchByItemId(String itemId,String orderId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `order Detail` WHERE  itemCode=? AND orderID=?", itemId,orderId);
        rst.next();
        return new ItemDetails(rst.getString(2),rst.getString(1),rst.getDouble(4),rst.getInt(3));
    }

    @Override
    public ArrayList<ItemDetails> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDetails> itemDetails=new ArrayList<>();
        ResultSet rst = CrudUtil.executeQuery("SELECT * From `order Detail`");
        while(rst.next()){
            itemDetails.add(new ItemDetails(rst.getString(2),rst.getString(1),rst.getDouble(4),rst.getInt(3)));
        }
        return itemDetails;
    }

    @Override
    public boolean update(ItemDetails itemDetails) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This method has not been implemented");
    }

}
