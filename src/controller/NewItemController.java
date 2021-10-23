package controller;

import com.jfoenix.controls.JFXTextField;
import dao.custom.impl.ItemDAOImpl;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import model.Item;

import java.sql.SQLException;

public class NewItemController {
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtId;

    public void addOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(txtId.getText().equals("")||txtDescription.getText().equals("")||txtUnitPrice.getText().equals("")||txtQtyOnHand.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Try again...\nSome fields may be Empty..").show();return;
        }
        Item i1=new Item(txtId.getText(),txtDescription.getText(), Integer.parseInt(txtQtyOnHand.getText()),
                Integer.parseInt(txtUnitPrice.getText()));
        if(new ItemDAOImpl().add(i1)){
            new Alert(Alert.AlertType.CONFIRMATION,"Saved Successfully...").show();
        }else{
            new Alert(Alert.AlertType.WARNING,"Try again...\nId may be duplicate..").show();
        }
    }
}
