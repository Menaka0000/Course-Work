package controller;

import dao.custom.impl.ItemDAOImpl;
import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.tm.ItemTm;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteItemController {
    public TableView<ItemTm> tblItem;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;
    public TableColumn colUnitPrice;
    ObservableList<ItemTm> items= FXCollections.observableArrayList();
    ItemTm x=null;


    public void initialize(){
        try {
            loadPreValues();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        colCode.setStyle("-fx-alignment: CENTER;");
        colDescription.setStyle("-fx-alignment: CENTER;");
        colQtyOnHand.setStyle("-fx-alignment: CENTER;");
        colUnitPrice.setStyle("-fx-alignment: CENTER;");
        colCode.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        tblItem.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    x= tblItem.getSelectionModel().getSelectedItem();
                });
    }

    public void deleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try{
            new ItemDAOImpl().deleteItem(x,items);
            tblItem.refresh();
        }catch (NullPointerException e){
            new Alert(Alert.AlertType.WARNING,"Please select a item from `Item` table..").show();
        }
    }

    public void loadPreValues() throws SQLException, ClassNotFoundException {
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT  * FROM `item`");
        ResultSet rst = pst.executeQuery();
        while(rst.next()){
           ItemTm x= new ItemTm(rst.getString(1),rst.getString(2),rst.getInt(3),rst.getDouble(4));
            items.add(x);
        }
        tblItem.setItems(items);
    }
}
