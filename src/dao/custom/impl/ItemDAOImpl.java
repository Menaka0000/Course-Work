package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.ItemDAO;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import dto.ItemDTO;
import dto.ItemDetails;
import dto.tm.ItemTm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean add(ItemDTO i) throws SQLException, ClassNotFoundException {
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
    public ItemDTO search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst =  CrudUtil.executeQuery("SELECT * FROM `item` WHERE ItemCode=?",id);
        if (rst.next()) {
            return new ItemDTO(
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
    public boolean update(ItemDTO itemDTO) throws SQLException, ClassNotFoundException {
        if (CrudUtil.executeUpdate("UPDATE `item` SET Description=?,qtyOnHand=?,unitPrice=? WHERE ItemCode=?",
                itemDTO.getDescription(), itemDTO.getQtyOnHand(), itemDTO.getUnitPrice(), itemDTO.getCode())){
            new Alert (Alert.AlertType.CONFIRMATION, itemDTO.getCode()+" Item was Updated Successfully").show();
            return true;
        }else {new Alert (Alert.AlertType.WARNING," Try Again").show();}
        return false;
    }

    @Override
    public boolean updateWhenOrderIsPurchasing(ItemDetails temp) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("UPDATE `item` SET qtyOnHand=? WHERE ItemCode=?", (search(temp.getItemCode()).getQtyOnHand() - temp.getQtyForSell())
                , temp.getItemCode());
    }

    @Override
    public boolean updateWhenOrderIsBeingUpdating(String id,int updatedValue) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate("UPDATE `item` SET qtyOnHand=? WHERE ItemCode=?",updatedValue,id);
    }

    @Override
    public void deleteItem(ItemTm item, ObservableList<ItemTm> items) throws SQLException, ClassNotFoundException {
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
    public ArrayList<ItemDTO> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO> allItemDTOS =new ArrayList<>();
        ResultSet rst =  CrudUtil.executeQuery("SELECT * FROM `Item`");
        while (rst.next()) {
            allItemDTOS.add(new ItemDTO(rst.getString(1),rst.getString(2),rst.getInt(3),rst.getInt(4)));
        }
        return allItemDTOS;
    }
}
