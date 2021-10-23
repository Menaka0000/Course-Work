package controller;

import com.jfoenix.controls.JFXTextField;
import dao.custom.impl.ItemDAOImpl;
import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Item;
import model.tm.ItemTm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateItemController {


    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public Label lblPreUnitPrice;
    public Label lblPreQtyOnHand;
    public Label lblPreDes;
    public Label lblPreId;
    public TableView<ItemTm> tblItem;
    public TableColumn colCode;
    public TableColumn colDescription;
    ObservableList<ItemTm> items= FXCollections.observableArrayList();

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
        colCode.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));

        tblItem.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> {
                    ItemTm x=tblItem.getSelectionModel().getSelectedItem();
                    try {
                        setPreValues(x);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });

    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (lblPreId.getText().equals("") ){new Alert(Alert.AlertType.WARNING,"Please select a row from `Item` table..").show();}
        else {
            if (txtDescription.getText().equals("")){new Alert(Alert.AlertType.WARNING,"Some fields may be empty..").show();}
            else{   new ItemDAOImpl().update(new Item(lblPreId.getText(),txtDescription.getText(),Integer.parseInt(txtQtyOnHand.getText())
                    ,Integer.parseInt(txtUnitPrice.getText())));
            }
        }
    }

    public void loadPreValues() throws SQLException, ClassNotFoundException {
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT  * FROM `item`");
        ResultSet rst = pst.executeQuery();
        while(rst.next()){
            ItemTm x= new ItemTm(rst.getString(1),rst.getString(2));
            items.add(x);
        }
        tblItem.setItems(items);
    }

    public void setPreValues(ItemTm x) throws SQLException, ClassNotFoundException {
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `item` WHERE ItemCode=?");
        pst.setObject(1,x.getId());
        ResultSet rst = pst.executeQuery();
        rst.next();
        lblPreId.setText(rst.getString(1));
        lblPreDes.setText(rst.getString(2));
        lblPreQtyOnHand.setText(rst.getString(3));
        lblPreUnitPrice.setText(rst.getString(4));
    }
}
