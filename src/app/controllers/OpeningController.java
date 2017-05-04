package app.controllers;

import app.ActionsHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
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
        new ActionsHandler().changeScene(btnCreate, "/fxml/Questionaire.fxml");
    }

    @FXML
    public void goToProperties(ActionEvent e) throws IOException {
        new ActionsHandler().changeScene(btnProperties, "/fxml/Properties.fxml");
    }

    @FXML
    public void exit(ActionEvent e){
        ActionsHandler.onClose(e, btnExit);
    }

    ///////////////////////////////////////



}
