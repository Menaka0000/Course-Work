package controller;

import com.jfoenix.controls.JFXTextField;
import dao.custom.impl.CustomerDAOImpl;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import model.Customer;

import java.sql.SQLException;

public class NewCustomerController {
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtCity;
    public JFXTextField txtProvince;
    public JFXTextField txtPostalCode;

    public void addOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(txtId.getText().equals("")||txtName.getText().equals("")
                ||txtAddress.getText().equals("")||txtCity.getText().equals("")||txtProvince.getText().equals("")||txtPostalCode.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Try again...\nSome fields may be Empty..").show();return;
        }
        Customer c1=new Customer(txtId.getText(),txtName.getText(),txtAddress.getText(),
                txtCity.getText(),txtProvince.getText(),txtPostalCode.getText());
        if(new CustomerDAOImpl().add(c1)){
            new Alert(Alert.AlertType.CONFIRMATION,"Saved Successfully...").show();
        }else{
            new Alert(Alert.AlertType.WARNING,"Try again...\nId may be duplicate..").show();
        }
    }
}
