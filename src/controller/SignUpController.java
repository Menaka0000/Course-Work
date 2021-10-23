package controller;

import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import util.LoadFxml;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpController {
    public TextField txtUserId;
    public TextField txtUserName;
    public TextField txtPosition;
    public TextField txtPassword;
    public TextField txtAddress;
    public TextField txtSalary;

    public void createOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        PreparedStatement pst1 = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `user`");
        ResultSet rst = pst1.executeQuery();
        while(rst.next()){
            if (rst.getString(1).equals(txtUserId.getText())){
                new Alert(Alert.AlertType.WARNING,"Duplicate User Id \nPlease try with another").show();
                return;
            }
        }
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `user` VALUES (?,?,?,?,?,?)");
            pst.setObject(1,txtUserId.getText());
            pst.setObject(2,txtUserName.getText());
            pst.setObject(3,txtPosition.getText());
            pst.setObject(4,txtPassword.getText());
            pst.setObject(5,txtAddress.getText());
            pst.setObject(6,txtSalary.getText());
            pst.executeUpdate();
            LoadFxml.chooseFxml1("../view/LoginForm.fxml",actionEvent);
    }

    public void exitOnAction(ActionEvent actionEvent) {
        LoadFxml.exit(actionEvent);
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        LoadFxml.chooseFxml1("../view/LoginForm.fxml",actionEvent);
    }
}
