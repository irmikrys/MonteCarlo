package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Created on 04.05.2017.
 */
public class ActionsHandler {

    public void changeScene(Button btn, String fxmlFile) throws IOException {

        Parent root2 = FXMLLoader.load(getClass().getResource(fxmlFile));
        btn.getScene().setRoot(root2);
    }

    public static void onClose(ActionEvent e, Button btn){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(Main.currentCss);
        alert.setHeaderText("");
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Stage stage = (Stage) btn.getScene().getWindow();
            stage.close();
        } else {
            e.consume();
        }
    }
}
