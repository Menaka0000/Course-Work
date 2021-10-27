package util;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public  class TitleBarController {
    public Pane pane;
    private Stage stage;
    private double x, y;
    public JFXButton btnmini;
    public JFXButton btnfull;

    @FXML
    void dragged(MouseEvent event) {
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);/**/
    }

    @FXML
    void pressed(MouseEvent event) {
        stage = (Stage) pane.getScene().getWindow();
        x = event.getSceneX();
        y = event.getSceneY();
    }

    public void fullScreen(ActionEvent actionEvent) {
        Stage stage=(Stage) btnfull.getScene().getWindow();
        stage.setFullScreenExitHint(" ");
        stage.setFullScreen(true);
    }

    public void minimize(ActionEvent actionEvent) {
        Stage stage=(Stage) btnmini.getScene().getWindow();
        stage.setIconified(true);
    }
}
