package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.ItemDAO;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import model.Item;
import model.tm.FullDetailedItemTm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean add(Item i) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM `Item`");
        while(rst.next()){
            if(rst.getString(1).equals(i.getCode())){
                return false;
            }
        }
        return CrudUtil.executeUpdate("INSERT INTO `Item` VALUES(?,?,?,?)",i.getCode(),i.getDescription(),
                i.getQtyOnHand(), i.getUnitPrice());
    }

    @Override
    public List<String> getItemIds() throws SQLException, ClassNotFoundException {
        ResultSet rst =  CrudUtil.executeQuery("SELECT * FROM `Item`");
        List<String> ids = new ArrayList<>();
        while (rst.next()){
            ids.add(
                    rst.getString(1)
            );
        }
        return ids;
    }

    @Override
    public Item search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst =  CrudUtil.executeQuery("SELECT * FROM `Item` WHERE ItemCode=?",id);
        if (rst.next()) {
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4)
            );

        } else {
            return null;
        }
    }

    @Override
    public boolean update(Item item) throws SQLException, ClassNotFoundException {
        if (CrudUtil.executeUpdate("UPDATE `item` SET Description=?,qtyOnHand=?,unitPrice=? WHERE ItemCode=?",
                item.getDescription(),item.getQtyOnHand(),item.getUnitPrice(),item.getCode())){
            new Alert (Alert.AlertType.CONFIRMATION,item.getCode()+" Item was Updated Successfully").show();
            return true;
        }else {new Alert (Alert.AlertType.WARNING," Try Again").show();}
        return false;
    }

    @Override
    public void deleteItem(FullDetailedItemTm item, ObservableList<FullDetailedItemTm> items) throws SQLException, ClassNotFoundException {
        Alert alert  = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure,\n you want to Delete ( "+item.getId()+" ) Item" );
        ButtonType yesButtonType = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(cancelButtonType, yesButtonType );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButtonType) {
            CrudUtil.executeUpdate("DELETE FROM `item`  WHERE ItemCode=?",item.getId());
            items.removeIf(x -> x.getId().equals(item.getId()));
        }
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ArrayList<Item> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }
}
