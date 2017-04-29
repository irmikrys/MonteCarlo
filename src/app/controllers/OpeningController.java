package app.controllers;

import app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created on 29.04.2017.
 */
public class OpeningController implements Initializable {
    @FXML public Button btnCreate;
    @FXML public Button btnProperties;
    @FXML public Button btnExit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void goToCreator(ActionEvent e) throws IOException {
        changeScene(btnCreate, "/fxml/Questionaire.fxml");
    }

    @FXML
    public void goToProperties(ActionEvent e) throws IOException {
        changeScene(btnProperties, "/fxml/Properties.fxml");
    }

    @FXML
    public void exit(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(Main.currentCss);
        alert.setHeaderText("");
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.close();
        } else {
            e.consume();
        }
    }

    ///////////////////////////////////////

    public void changeScene(Button btn, String fxmlFile) throws IOException {

        Parent root2 = FXMLLoader.load(getClass().getResource(fxmlFile));
        if(btn == btnCreate) {
            Stage stage = (Stage) btn.getScene().getWindow();
            stage.setMinWidth(750);
            stage.setMinHeight(500);
            stage.centerOnScreen();
        }
        btn.getScene().setRoot(root2);
    }

}
