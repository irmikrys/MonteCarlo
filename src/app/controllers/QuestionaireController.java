package app.controllers;

import app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created on 29.04.2017.
 */
public class QuestionaireController implements Initializable {

    public static boolean nonPolyEnabled;

    @FXML public Button btnAddLimit;
    @FXML public Button btnCompute;

    @FXML public TextField tfFunction;

    @FXML public Label lblResult;
    @FXML public Label lblFcn;
    @FXML public Label lblErrors;

    @FXML public CheckBox minimizeCheck;
    @FXML public CheckBox maximizeCheck;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void popupTheme(ActionEvent e) throws IOException {

        String css0 = Main.class.getResource("/css/Green.css").toExternalForm(); //default
        String css1 = Main.class.getResource("/css/BevelGrey.css").toExternalForm();
        String css7 = Main.class.getResource("/css/Royal.css").toExternalForm();

        List<String> listCss = new ArrayList<>(
                Arrays.asList(css0, css1, css7)
        );

        String name0 = StringUtils.substringBetween(css0,"/css/",".css");
        String name1 = StringUtils.substringBetween(css1,"/css/",".css");
        String name7 = StringUtils.substringBetween(css7,"/css/",".css");

        List<String> choices = new ArrayList<>();
        choices.add(name0); choices.add(name1); choices.add(name7);

        ChoiceDialog<String> dialog = new ChoiceDialog<>(StringUtils.substringBetween(Main.currentCss,"/css/",".css"), choices);
        dialog.setTitle("Change Theme");
        dialog.setHeaderText("");
        dialog.setContentText("Choose Theme:");
        dialog.getDialogPane().getStylesheets().add(Main.currentCss);

        GridPane grid = (GridPane) dialog.getDialogPane().getChildren().get(3);
        ComboBox<String> cb = (ComboBox) grid.getChildren().get(1);

        cb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String selectedCss = cb.getSelectionModel().getSelectedItem();
            for(int i = 0; i < listCss.size(); i++){
                if(listCss.get(i).contains(selectedCss)){
                    dialog.getDialogPane().getStylesheets().clear();
                    dialog.getDialogPane().getStylesheets().add(listCss.get(i));
                    break;
                }
            }
        });

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String curCss;
            for(int i = 0; i < listCss.size(); i++){
                if(listCss.get(i).contains(result.get())){
                    curCss = listCss.get(i);
                    Scene scene = btnCompute.getScene();
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(curCss);
                    Main.currentCss = curCss;
                    break;
                }
            }

            System.out.println("Your choice: " + result.get());
        }

    }

}
