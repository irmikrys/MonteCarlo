package app.controllers;

import app.ActionsHandler;
import app.Algo;
import app.ButtonFields;
import app.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created on 29.04.2017.
 */
public class QuestionaireController implements Initializable {

    public static boolean nonPolyEnabled;
    public ArrayList<String> signsArr = new ArrayList<>(Arrays.asList("<", "<=", ">", ">="));
    public List<ButtonFields> limitsButtons= new ArrayList<>();

    @FXML public BorderPane borderPaneFirst;
    @FXML public AnchorPane splitLeftAnchor;
    @FXML public SplitPane splitPane;
    @FXML public VBox limitsVBox;

    @FXML public Button btnAddLimit;
    @FXML public Button btnSubmitFcn;
    @FXML public Button btnSetEps;
    @FXML public Button btnCompute;

    @FXML public TextField tfFunction;

    @FXML public Label lblFcn;
    @FXML public Label lblEpsVal;
    @FXML public Label lblResult;
    @FXML public Label lblErrors;

    @FXML public CheckBox minimizeCheck;
    @FXML public CheckBox maximizeCheck;

    @FXML public MenuItem miClose;
    @FXML public MenuItem miNew;
    @FXML public CheckMenuItem cmiEnPoly;

    ////////////////////////////////////

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCmiEnPolyListener();
        if(QuestionaireController.nonPolyEnabled) {
            cmiEnPoly.setSelected(true);
        }
        setMaxCheckListener();
        setMinCheckListener();
        setLabelsOnStart();
        Algo.epsilon = Double.parseDouble(lblEpsVal.getText());
    }

    @FXML
    public void closeAction(ActionEvent e) {
        ActionsHandler.onClose(e, btnCompute);
    }

    @FXML
    public void newAction(ActionEvent e) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(Main.currentCss);
        alert.setHeaderText("");
        alert.setTitle("New");
        alert.setContentText("Are you sure you want to create new problem? All changes will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            new ActionsHandler().changeScene(btnCompute, "/fxml/Questionaire.fxml");
        } else {
            e.consume();
        }

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

    @FXML
    public void addLimit(ActionEvent e) {
        //utworz hbox
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        hBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        //utworz komponenty potrzebne do hboxa
        TextField tfLim = new TextField();
        ButtonFields bf = new ButtonFields();
        bf.setText(signsArr.get(bf.getCounter()).toString());
        TextField tfVal = new TextField();
        tfVal.setPrefWidth(60);
        Button submit = new Button("Submit"); //TODO: ustawić margines lewy
        //ustaw listenera dla buttona ButtonFields ze zmiana znaku
        bf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bf.setCounter(bf.getCounter() + 1);
                bf.setText(signsArr.get(bf.getCounter()));
            }
        });
        //dodaj button do tablicy
        limitsButtons.add(bf);
        //ustaw listenera dla buttona zatwierdzajacego
        //dodaj wszystkie komponenty do hboxa
        hBox.getChildren().add(tfLim);
        hBox.getChildren().add(bf);
        hBox.getChildren().add(tfVal);
        hBox.getChildren().add(submit);
        //dodaj hboxa do vboxa
        limitsVBox.getChildren().add(hBox);
    }

    @FXML
    public void submitFcn(ActionEvent e) {
        lblErrors.setText("Action submit function not implemented...");
    }

    @FXML
    public void setEpsilon(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog(lblEpsVal.getText());
        dialog.setTitle("Epsilon");
        dialog.setHeaderText("");
        dialog.setContentText("New epsilon value: ");
        dialog.getDialogPane().getStylesheets().add(Main.currentCss);
        lblErrors.setText("");

        Pane first = dialog.getDialogPane();
        //printDialogNodes(first);
        Pane second = (Pane) first.getChildren().get(3);
        //printDialogNodes(second);
        TextField tf = (TextField) second.getChildren().get(1);
        System.out.println(tf.getText());

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(eps -> {
            String text = tf.getText();
            double value;
            if(text.matches("^[0-9]+\\.?[0-9]*$")) {
                value = Double.parseDouble(tf.getText());
            }
            else {
                value = Algo.epsilon;
            }
            Algo.epsilon = value;
            lblEpsVal.setText(((Double) value).toString());
            System.out.println("New epsilon value: " + Algo.epsilon);
        });
    }

    @FXML
    public void computeResult(ActionEvent e) {
        lblErrors.setText("Action compute not implemented...");
    }

    /////////////////////////////////////

    private void setCmiEnPolyListener(){
        cmiEnPoly.selectedProperty().addListener((ov, old_val, new_val) -> {
            if(cmiEnPoly.isSelected()){
                QuestionaireController.nonPolyEnabled = true;
                System.out.println("Non-poly enabled cmi");
            } else {
                QuestionaireController.nonPolyEnabled = false;
                System.out.println("Non-poly disabled cmi");
            }
        });
    }

    private void setMaxCheckListener() {
        excludeDoubleCheck(maximizeCheck, minimizeCheck);
    }

    private void setMinCheckListener() {
        excludeDoubleCheck(minimizeCheck, maximizeCheck);
    }

    private void setLabelsOnStart() {
        lblErrors.setText("");
        lblEpsVal.setText("0.1");
        lblFcn.setText("");
        lblResult.setText("Click Compute button to see result.");
    }

    ////////////////////////////////

    private void excludeDoubleCheck(CheckBox first, CheckBox second){
        first.selectedProperty().addListener((ov, old_val, new_val) -> {
            if(second.isSelected()){
                if(first.isSelected()){
                    second.setSelected(false);
                }
                else{
                    second.setSelected(true);
                }
            }
        });
    }

    private void printDialogNodes(Pane pane){
        List<Node> nodes = pane.getChildren();
        for(int i = 0; i < nodes.size(); i++){
            System.out.print("\t");
            System.out.println(nodes.get(i).getClass());
        }
        System.out.println();
    }
}
