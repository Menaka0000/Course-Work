package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import util.LoadFxml;
import util.SetDateAndTime;
import util.TitleBarController;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginFormController extends TitleBarController {
    public JFXTextField txtUserName;
    public JFXTextField txtPassword;
    public Pane pane;
    public JFXButton btnmini;
    public JFXButton btnfull;

    public void signUpOnAction(ActionEvent actionEvent) throws IOException {
        LoadFxml.chooseFxml1("../view/SignUp.fxml",actionEvent);
    }

    public void signInOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {

        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `user` WHERE user_name=?");
        preparedStatement.setObject(1,txtUserName.getText());
        ResultSet rst = preparedStatement.executeQuery();
        if(rst.next()){
            if(rst.getString(4).equals(txtPassword.getText())){
                SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss a");
                String timeNow=sdf.format(new Date());
                SetDateAndTime.loginTime=timeNow;
                SetDateAndTime.name=rst.getString(2);
                if (rst.getString(3).equals("Cashier")){
                LoadFxml.chooseFxml("../view/DashBoardForCashier.fxml",actionEvent);
                }else {
                    LoadFxml.chooseFxml("../view/DashBordForAdmin.fxml",actionEvent);}
            }else {
                new Alert(Alert.AlertType.WARNING,"Invalid Password...").show();
            }
        }else { new Alert(Alert.AlertType.WARNING,"Invalid User Name...").show();}

    }

    public void exitOnAction(ActionEvent actionEvent) {
        LoadFxml.exit(actionEvent);
    }



    //------------------------------------------------------


}
