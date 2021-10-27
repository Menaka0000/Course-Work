package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.LoadFxml;
import util.SetDateAndTime;

import java.io.IOException;

public class DashBoardForCashierController  {
    public AnchorPane context;
    public Label lblTime;
    public Label lblDate;
    public JFXButton btnMakeOrder;
    public JFXButton btnManageOrder;
    public Label lblStatus;
    public Label lblLoginTime;

    public void initialize(){
        SetDateAndTime.setDateAndTime(lblDate,lblTime,lblStatus,lblLoginTime);
    }

    public  void logOutOnAction(ActionEvent actionEvent) throws IOException {
        LoadFxml.chooseFxml("../view/LoginForm.fxml",actionEvent);
    }

    public void makeOrderOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene( FXMLLoader.load(getClass().getResource("../view/MakeOrder.fxml"))));
        stage.show();
    }

    public void manageOrderOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene( FXMLLoader.load(getClass().getResource("../view/ManageOrder.fxml"))));
        stage.show();
    }


    public void exitOnAction(ActionEvent actionEvent) {
        LoadFxml.exit(actionEvent);
    }
//------------------------------------------------


}
