package dao.custom.impl;

import controller.ModifyOrderDetailController;
import dao.CrudUtil;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
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
    private final ItemDAO itemDAO = new ItemDAOImpl();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAOImpl();

    @Override
    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `order` ORDER BY orderId DESC LIMIT 1").executeQuery();
        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if(tempId<=9){return "O-00"+tempId;}
            else if(tempId<=99){return "O-0"+tempId;}
            else {return "O-"+tempId;}
        }else{return "O-001";}
    }


    @Override
    public boolean add(Order order) throws SQLException, ClassNotFoundException {
        Connection con=DbConnection.getInstance().getConnection();
        con.setAutoCommit(false);
        try {
            if(CrudUtil.executeUpdate("INSERT INTO `Order` VALUES (?,?,?,?,?)", order.getOrderId(), order.getCustomerId()
                    , order.getOrderDate(), order.getOrderTime(), order.getCost())){
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
            orderDetailDAO.add(temp);
        }
        return true;
    }

    @Override
    public boolean updateItemTable(Order order) throws SQLException, ClassNotFoundException {
        for (ItemDetails temp:order.getItems()
             ) {
                 return itemDAO.updateWhenOrderIsPurchasing(temp);
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
        Connection con = DbConnection.getInstance().getConnection();
        con.setAutoCommit(false);
        try {
            ItemDetails itemDetail = orderDetailDAO.search(orderId);
            for (ItemDetails x:items
            ) {
                if (itemDetail.getItemCode().equals(x.getItemCode())){
                    System.out.println("test");
                    if(itemDetail.getQtyForSell()>x.getQtyForSell()){
                        int difference=itemDetail.getQtyForSell()-x.getQtyForSell();
                        System.out.println("test1");
                        int updatedValue= itemDAO.search(itemDetail.getItemCode()).getQtyOnHand()+difference;
                        System.out.println("test2");

                        System.out.println(itemDAO.updateWhenOrderIsBeingUpdating(itemDetail.getItemCode(),updatedValue) );
                        System.out.println(orderDetailDAO.updateWhenOrderUpdating(itemDetail.getOrderId(),x.getQtyForSell()) );

                        if (itemDAO.updateWhenOrderIsBeingUpdating(itemDetail.getItemCode(),updatedValue) &&
                                orderDetailDAO.updateWhenOrderUpdating(itemDetail.getOrderId(),x.getQtyForSell())){
                            con.commit();new Alert(Alert.AlertType.CONFIRMATION,"Order Modified successfully").show();
                        }
                        else {new Alert(Alert.AlertType.WARNING,"Try Againhahaha..").show();}
                    }else{
                        int difference=x.getQtyForSell()-itemDetail.getQtyForSell();
                        int updatedValue=itemDAO.search(itemDetail.getItemCode()).getQtyOnHand()-difference;

                        System.out.println(itemDAO.updateWhenOrderIsBeingUpdating(itemDetail.getItemCode(),updatedValue) );
                        System.out.println(orderDetailDAO.updateWhenOrderUpdating(itemDetail.getOrderId(),x.getQtyForSell()) );

                        if (itemDAO.updateWhenOrderIsBeingUpdating(itemDetail.getItemCode(),updatedValue) &&
                                orderDetailDAO.updateWhenOrderUpdating(itemDetail.getOrderId(),x.getQtyForSell())){
                            con.commit();new Alert(Alert.AlertType.CONFIRMATION,"Order Modified successfully").show();}
                        else {new Alert(Alert.AlertType.WARNING,"Try Againlalala..").show();}
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }  finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `tempItem`").executeUpdate();
    }

    @Override
    public void deleteOrder(ObservableList<CartTm> obList1,String orderId) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        for (CartTm temp:obList1
             ) {
            if(itemDAO.updateWhenOrderIsBeingUpdating(temp.getCode(),itemDAO.search(temp.getCode()).getQtyOnHand()+temp.getQty())){
                System.out.println("Item Updated");
            }
        }

        PreparedStatement pst3 = con.prepareStatement("DELETE FROM `order detail` WHERE orderId=?");
        pst3.setObject(1,orderId);
        if (pst3.executeUpdate()>0 && CrudUtil.executeUpdate("DELETE FROM `order` WHERE orderId=?",orderId)){
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
