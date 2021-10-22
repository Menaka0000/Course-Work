package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ManageItemController {
    public Label lblTime;
    public Label lblDate;
    public Label lblStatus;
    public Label lblLoginTime;
    public Pane Context1;

    public void initialize(){
        SetDateAndTime.setDateAndTime(lblDate,lblTime,lblStatus,lblLoginTime);
    }

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        LoadFxml.chooseFxml("../view/LoginForm.fxml",actionEvent);
    }

    public void exitOnAction(ActionEvent actionEvent) {
        LoadFxml.exit(actionEvent);
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        LoadFxml.chooseFxml1("../view/DashBordForAdmin.fxml",actionEvent);
    }

    public void newItemOnAction(ActionEvent actionEvent) throws IOException {
        LoadFxml.loadChildFxml("../view/NewItem.fxml",actionEvent,Context1);
    }

    public void updateItemOnAction(ActionEvent actionEvent) throws IOException {
        LoadFxml.loadChildFxml("../view/UpdateItem.fxml",actionEvent,Context1);
    }

    public void removeItemOnAction(ActionEvent actionEvent) throws IOException {
        LoadFxml.loadChildFxml("../view/DeleteItem.fxml",actionEvent,Context1);
    }
}
