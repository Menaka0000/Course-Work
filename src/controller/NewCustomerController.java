package controller;

import bo.BoFactory;
import bo.custom.CustomerBO;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import dto.CustomerDTO;

import java.sql.SQLException;

public class NewCustomerController {
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtCity;
    public JFXTextField txtProvince;
    public JFXTextField txtPostalCode;

    /*Property injection has been injected*/
    private final CustomerBO customerBO= (CustomerBO) BoFactory.getBoFactory().getBO(BoFactory.BoTypes.CUSTOMER);

    public void addOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(txtId.getText().equals("")||txtName.getText().equals("")
                ||txtAddress.getText().equals("")||txtCity.getText().equals("")||txtProvince.getText().equals("")||txtPostalCode.getText().equals("")){            new Alert(Alert.AlertType.WARNING,"Try again...\nSome fields may be Empty..").show();return;
        }
        CustomerDTO c1=new CustomerDTO(txtId.getText(),txtName.getText(),txtAddress.getText(),
                txtCity.getText(),txtProvince.getText(),txtPostalCode.getText());
        if(customerBO.saveCustomer(c1)){
            new Alert(Alert.AlertType.CONFIRMATION,"Saved Successfully...").show();
        }else{
            new Alert(Alert.AlertType.WARNING,"Try again...\nId may be duplicate..").show();
        }
    }
}
