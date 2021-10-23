package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import util.LoadFxml;

import java.io.IOException;

public class DashBordForAdminController {
    public AnchorPane context;
    public JFXButton btnManageItems;
    public JFXButton btnReports;
    public Label lblTime;
    public Label lblDate;
    public Label lblStatus;
    public Label lblLoginTime;

    public void initialize(){
        SetDateAndTime.setDateAndTime(lblDate,lblTime,lblStatus,lblLoginTime);
    }

    public void manageItemsOnAction(ActionEvent actionEvent) throws IOException {
        LoadFxml.chooseFxml1("../view/ManageItem.fxml",actionEvent);
    }

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        LoadFxml.chooseFxml("../view/LoginForm.fxml",actionEvent);
    }

    public void exitOnAction(ActionEvent actionEvent) {
        LoadFxml.exit(actionEvent);
    }


}
