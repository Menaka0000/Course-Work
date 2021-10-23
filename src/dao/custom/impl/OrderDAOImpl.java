package dao.custom.impl;

import controller.ModifyOrderDetailController;
import dao.custom.OrderDAO;
import db.DbConnection;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.ItemDetails;
import model.Order;
import model.tm.CartTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `order` ORDER BY orderId DESC LIMIT 1").executeQuery();
        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if(tempId<9){return "O-00"+tempId;}
            else if(tempId<99){return "O-0"+tempId;}
            else {return "O-"+tempId;}
        }else{return "O-001";}
    }


    @Override
    public boolean add(Order order){
        Connection con=null;
        try {
            con=DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement stm = con.prepareStatement("INSERT INTO `Order` VALUES (?,?,?,?,?)");
            stm.setObject(1,order.getOrderId());
            stm.setObject(2,order.getCustomerId());
            stm.setObject(3,order.getOrderDate());
            stm.setObject(4,order.getOrderTime());
            stm.setObject(5,order.getCost());

            if(stm.executeUpdate()>0){
                if( saveOrderDetail(order) && updateItemTable(order)){
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            }else {
                con.rollback();
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public  boolean saveOrderDetail(Order order) throws SQLException, ClassNotFoundException {
        for (ItemDetails temp :order.getItems()
             ) {
            PreparedStatement stm = DbConnection.getInstance().getConnection().
                    prepareStatement("INSERT INTO `Order Detail` VALUES (?,?,?,?)");
            stm.setObject(1,temp.getItemCode());
            stm.setObject(2,order.getOrderId());
            stm.setObject(3,temp.getQtyForSell());
            stm.setObject(4,temp.getUnitPrice());
            if (stm.executeUpdate()>0){}else{return false;}
        }
        return true;
    }

    @Override
    public boolean updateItemTable(Order order) throws SQLException, ClassNotFoundException {
        int qty;
        for (ItemDetails temp:order.getItems()
             ) {
            PreparedStatement stm1 = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `item` WHERE ItemCode=?");
            stm1.setObject(1,temp.getItemCode());
            ResultSet rst = stm1.executeQuery();
            rst.next();
            qty=Integer.parseInt(rst.getString(3));
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE `item` SET qtyOnHand=? WHERE ItemCode=?");
            stm.setObject(1,(qty-temp.getQtyForSell()));
            stm.setObject(2,temp.getItemCode());
            if (stm.executeUpdate()>0){}else {return false;}
        }return true;
    }

    @Override
    public boolean update(Order order) throws SQLException, ClassNotFoundException {
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("UPDATE `order` SET cost=? WHERE orderId=?");
        pst.setObject(1,order.getCost());
        pst.setObject(2,order.getOrderId());
        pst.executeUpdate();
        modifyOrderDetail(order.getOrderId(),order.getItems());
        return false;
    }


    @Override
    public void modifyOrderDetail(String orderId,ArrayList<ItemDetails> items) throws SQLException, ClassNotFoundException {
        Connection con=DbConnection.getInstance().getConnection();
        con.setAutoCommit(false);
        PreparedStatement pst = con.prepareStatement("SELECT * FROM `order Detail` WHERE  orderId=?");
        pst.setObject(1,orderId);
        ResultSet rst = pst.executeQuery();
        while(rst.next()){
            for (ItemDetails x:items
            ) {
                if (rst.getString(1).equals(x.getItemCode())){
                    if(rst.getInt(3)>x.getQtyForSell()){
                        int difference=rst.getInt(3)-x.getQtyForSell();
                        PreparedStatement pst1 = con.prepareStatement("Select * FROM `item` WHERE ItemCode=?");
                        pst1.setObject(1,rst.getString(1));
                        ResultSet rst1 = pst1.executeQuery();rst1.next();
                        int updatedValue=rst1.getInt(3)+difference;
                        PreparedStatement pst2 = con.prepareStatement("UPDATE  `item`SET qtyOnHand=? WHERE ItemCode=?");
                        pst2.setObject(1,updatedValue);
                        pst2.setObject(2,rst.getString(1));
                        PreparedStatement pst3 = con.prepareStatement("UPDATE `order detail` SET qty=? WHERE ItemCode=?");
                        pst3.setObject(1,x.getQtyForSell());
                        pst3.setObject(2,rst.getString(1));
                        if (pst2.executeUpdate()>0 &&  pst3.executeUpdate()>0){con.commit();new Alert(Alert.AlertType.CONFIRMATION,"Order Modified successfully").show();}
                        else {new Alert(Alert.AlertType.WARNING,"Try Again..").show();}
                    }else{
                        int difference=x.getQtyForSell()-rst.getInt(3);
                        PreparedStatement pst1 = con.prepareStatement("Select * FROM `item` WHERE ItemCode=?");
                        pst1.setObject(1,rst.getString(1));
                        ResultSet rst1 = pst1.executeQuery();rst1.next();
                        int updatedValue=rst1.getInt(3)-difference;
                        PreparedStatement pst2 = con.prepareStatement("UPDATE  `item`SET qtyOnHand=? WHERE ItemCode=?");
                        pst2.setObject(1,updatedValue);
                        pst2.setObject(2,rst.getString(1));
                        PreparedStatement pst3 = con.prepareStatement("UPDATE `order detail` SET qty=? WHERE ItemCode =?");
                        pst3.setObject(1,x.getQtyForSell());
                        pst3.setObject(2,rst.getString(1));
                        if (pst2.executeUpdate()>0 &&  pst3.executeUpdate()>0){con.commit();new Alert(Alert.AlertType.CONFIRMATION,"Order Modified successfully").show();}
                        else {new Alert(Alert.AlertType.WARNING,"Try Again..").show();}
                    }
                }
            }
        }
        con.setAutoCommit(true);
        DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `tempItem`").executeUpdate();
    }

    @Override
    public void deleteOrder(ObservableList<CartTm> obList1,String orderId) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        for (CartTm temp:obList1
             ) {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM `item` WHERE ItemCode=?");
            pst.setObject(1,temp.getCode());
            ResultSet rst = pst.executeQuery();
            rst.next();
                int newValue=rst.getInt(3)+temp.getQty();
            PreparedStatement pst1 = con.prepareStatement("UPDATE `item` SET qtyOnHand=? WHERE ItemCode=?");
            pst1.setObject(1,newValue);
            pst1.setObject(2,rst.getString(1));
            if(pst1.executeUpdate()>0){
                System.out.println("Item Updated");
            }

        }
        PreparedStatement pst2 = con.prepareStatement("DELETE FROM `order` WHERE orderId=?");
        pst2.setObject(1,orderId);

        PreparedStatement pst3 = con.prepareStatement("DELETE FROM `order detail` WHERE orderId=?");
        pst3.setObject(1,orderId);
        if (pst3.executeUpdate()>0 && pst2.executeUpdate()>0 ){
            new  Alert (Alert.AlertType.CONFIRMATION,"Order Delete Successfully").show();
        }else{new  Alert (Alert.AlertType.WARNING,"Try again").show();}
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }
    @Override
    public Order search(String s) throws SQLException, ClassNotFoundException {
        return null;
    }
    @Override
    public ArrayList<Order> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }
}
