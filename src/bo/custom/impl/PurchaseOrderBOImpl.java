package bo.custom.impl;

import bo.custom.PurchaseOrderBO;
import dao.DAOFactory;
import dao.custom.OrderDAO;
import db.DbConnection;
import javafx.collections.ObservableList;
import dto.OrderDTO;
import dto.tm.CartTm;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseOrderBOImpl implements PurchaseOrderBO {

    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDAOFactory().getDAO(DAOFactory.DAOTypes.ORDER);/*Property injection has been injected*/

    @Override
    public boolean purchaseOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        con.setAutoCommit(false);
        try {
            if( orderDAO.add(orderDTO) && orderDAO.saveOrderDetail(orderDTO) && orderDAO.updateItemTable(orderDTO)  ){
                con.commit();
                return true;
            } else {
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
    public String getOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.getOrderId();
    }

    @Override
    public ResultSet searchByCustomerId(String x) throws SQLException, ClassNotFoundException {
      return orderDAO.searchByCustomerId(x);
    }

    @Override
    public void deleteOrder(ObservableList<CartTm> obList1, String text) throws SQLException, ClassNotFoundException {
        orderDAO.deleteOrder(obList1,text);
    }
}
