package bo.custom.impl;

import bo.custom.PurchaseOrderBO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dao.custom.impl.ItemDAOImpl;
import dao.custom.impl.OrderDAOImpl;
import dao.custom.impl.OrderDetailDAOImpl;
import db.DbConnection;
import model.Order;
import java.sql.Connection;
import java.sql.SQLException;

public class PurchaseOrderBOImpl implements PurchaseOrderBO {
    private final ItemDAO itemDAO = new ItemDAOImpl();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    public boolean purchaseOrder(Order order) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        con.setAutoCommit(false);
        try {
            if( orderDAO.saveOrderDetail(order) && orderDAO.updateItemTable(order) && orderDAO.add(order)){
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
}
