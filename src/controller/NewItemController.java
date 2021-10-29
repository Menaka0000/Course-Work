package controller;

import bo.BoFactory;
import bo.custom.ItemBO;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import dto.ItemDTO;

import java.sql.SQLException;

public class NewItemController {
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtId;

    private final ItemBO itemBO = (ItemBO) BoFactory.getBoFactory().getBO(BoFactory.BoTypes.ITEM);/*Property injection has been injected*/

    public void addOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(txtId.getText().equals("")||txtDescription.getText().equals("")||txtUnitPrice.getText().equals("")||txtQtyOnHand.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Try again...\nSome fields may be Empty..").show();return;
        }
        ItemDTO itemDTO =new ItemDTO(txtId.getText(),txtDescription.getText(), Integer.parseInt(txtQtyOnHand.getText()),
                Integer.parseInt(txtUnitPrice.getText()));
        if(itemBO.saveItem(itemDTO)){
            new Alert(Alert.AlertType.CONFIRMATION,"Saved Successfully...").show();
        }else{
            new Alert(Alert.AlertType.WARNING,"Try again...\nId may be duplicate..").show();
        }
    }
}
