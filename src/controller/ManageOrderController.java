package controller;

import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import util.LoadFxml;

import java.io.IOException;
import java.sql.SQLException;

public class ManageOrderController  {
    public Label lblTime;
    public Label lblDate;
    public Label lblStatus;
    public Label lblLoginTime;
    public Pane Context1;


    public void initialize(){
        SetDateAndTime.setDateAndTime(lblDate,lblTime,lblStatus,lblLoginTime);
    }

    public void exitOnAction(ActionEvent actionEvent) {
        LoadFxml.exit(actionEvent);
    }

    public void logOutOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `tempItem`").executeUpdate();
        LoadFxml.chooseFxml("../view/LoginForm.fxml",actionEvent);
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `tempItem`").executeUpdate();
        LoadFxml.chooseFxml1("../view/DashBoardForCashier.fxml",actionEvent);
    }

    public void modifyOrderOnAction(ActionEvent actionEvent) throws IOException {
        LoadFxml.loadChildFxml("../view/ModifyOrderDetail.fxml",actionEvent,Context1);
    }
}
