package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import db.DbConnection;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import dto.ItemDetails;
import dto.OrderDTO;
import dto.tm.CartTm;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {

    /* Property injection has been injected */
    private final ItemDAO itemDAO = new ItemDAOImpl();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAOImpl();

    @Override
    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `order` ORDER BY orderId DESC LIMIT 1");
        if (rst.next()){
            int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if(tempId<=9){return "O-00"+tempId;}
            else if(tempId<=99){return "O-0"+tempId;}
            else {return "O-"+tempId;}
        }else{return "O-001";}
    }

    @Override
    public boolean add(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
       return CrudUtil.executeUpdate("INSERT INTO `Order` VALUES (?,?,?,?,?)", orderDTO.getOrderId(), orderDTO.getCustomerId()
                , orderDTO.getOrderDate(), orderDTO.getOrderTime(), orderDTO.getCost());
    }

    @Override
    public  boolean saveOrderDetail(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        for (ItemDetails temp : orderDTO.getItems()
        ) {
            System.out.println("test2222");
            if (!orderDetailDAO.add(temp)){return false;}
            System.out.println("test3333");
        }
        return true;
    }

    @Override
    public boolean updateItemTable(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        for (ItemDetails temp: orderDTO.getItems()
             ) {
                 if (!itemDAO.updateWhenOrderIsPurchasing(temp)){return false;}
        }
        return true;
    }

    @Override
    public boolean update(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        if (CrudUtil.executeUpdate("UPDATE `order` SET cost=? WHERE orderId=?", orderDTO.getCost(), orderDTO.getOrderId())){
            modifyOrderDetail(orderDTO.getOrderId(), orderDTO.getItems());
            return true;
        }
        return false;
    }

    @Override
    public boolean modifyOrderDetail(String orderId,ArrayList<ItemDetails> items) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        con.setAutoCommit(false);
        try {
            for (ItemDetails x:items
            ) {
                if (x.getUpdateStatus()==0) {
                    ItemDetails search = orderDetailDAO.searchByItemId(x.getItemCode(),x.getOrderId());
                    int difference = x.getQtyForSell()-search.getQtyForSell();
                    System.out.println("qtyforsale "+x.getQtyForSell()+" previ "+search.getQtyForSell());
                    System.out.println(difference);
                    int updatedValue = itemDAO.search(search.getItemCode()).getQtyOnHand() + (-difference);
                    System.out.println(updatedValue);
                    if (itemDAO.updateWhenOrderIsBeingUpdating(search.getItemCode(), updatedValue) && orderDetailDAO.updateWhenOrderUpdating(search.getOrderId(),search.getItemCode(), x.getQtyForSell())) {
                        con.commit();
                    } else {
                        con.rollback();
                        return false;
                    }
                        /*else {
                            int difference = x.getQtyForSell() - search.getQtyForSell();
                            int updatedValue = itemDAO.search(search.getItemCode()).getQtyOnHand() - difference;
                            if (itemDAO.updateWhenOrderIsBeingUpdating(search.getItemCode(), updatedValue) &&
                                    orderDetailDAO.updateWhenOrderUpdating(search.getOrderId(), x.getQtyForSell())) {
                                con.commit();
                            } else {
                                con.rollback();
                                return false;
                            }
                        }*/
                }else {
                    if (orderDetailDAO.add(x) && itemDAO.updateWhenOrderIsBeingUpdating(x.getItemCode(),itemDAO.search(x.getItemCode()).getQtyOnHand()- x.getQtyForSell())){
                        con.commit();
                    }else {
                        con.rollback();
                        return false;
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
        }return true;
    }

    @Override
    public void deleteOrder(ObservableList<CartTm> obList1,String orderId) throws SQLException, ClassNotFoundException {
        for (CartTm temp:obList1
             ) {
            int updatedValue=(itemDAO.search(temp.getCode()).getQtyOnHand()+temp.getQty());
            if(itemDAO.updateWhenOrderIsBeingUpdating(temp.getCode(),updatedValue)){
                System.out.println("Item Updated when order was being deleting");
            }
        }
        if (CrudUtil.executeUpdate("DELETE FROM `order` WHERE orderId=?",orderId)){
            new  Alert (Alert.AlertType.CONFIRMATION,"Order Delete Successfully").show();
        }else{new  Alert (Alert.AlertType.WARNING,"Try again").show();}
    }

    @Override
    public ResultSet searchByCustomerId(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeQuery("SELECT * FROM `order` WHERE cId=?",id);
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This method has not been implemented");
    }
    @Override
    public OrderDTO search(String s) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This method has not been implemented");
    }
    @Override
    public ArrayList<OrderDTO> getAll() throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("This method has not been implemented");
    }
}
