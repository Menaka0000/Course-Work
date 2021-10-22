package controller;

import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;

public class LoadFxml {
    public static void chooseFxml(String address, ActionEvent actionEvent ) throws IOException {
        Stage stage =(Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();
        Stage stage1=new Stage();
        stage1.setScene(new Scene(FXMLLoader.load(LoadFxml.class.getResource(address))));
        stage1.initStyle(StageStyle.UNDECORATED);
        stage1.show();
    } public static void chooseFxml1(String address, ActionEvent actionEvent ) throws IOException {
        Stage stage =(Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(LoadFxml.class.getResource(address))));
        stage.show();
    }
    public static void loadChildFxml(String address, ActionEvent actionEvent, Pane context1) throws IOException {
        System.out.println("test");
        URL resource = LoadFxml.class.getResource(address);
        Parent load = FXMLLoader.load(resource);
        context1.getChildren().clear();
        context1.getChildren().add(load);
    }
    public static void exit(ActionEvent actionEvent){
        Alert alert  = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        ButtonType yesButtonType = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(cancelButtonType, yesButtonType );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButtonType) {
            try {
                DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `tempItem`").executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }
}
