package app.controllers;

import app.ActionsHandler;
import app.Algo;
import app.ButtonFields;
import app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created on 29.04.2017.
 */
public class QuestionaireController implements Initializable {

    static boolean nonPolyEnabled;
    private ArrayList<String> signsArr = new ArrayList<>(Arrays.asList("<", "<=", ">", ">="));
    private ArrayList<ButtonFields> limitsButtons= new ArrayList<>();
    private ArrayList<Function> functions = new ArrayList<>();
    private ArrayList<String> decisionVars = new ArrayList<>();
    private boolean isAddReleased = true;
    private boolean isTfLimOk;
    private boolean isTfValOk;
    private boolean isTfFcnOk;
    private int limitsCounter;

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
        isTfValOk = false;
        isTfLimOk = false;
        lblErrors.setText("");
        isAddReleased = false;
        btnAddLimit.setDisable(!isAddReleased);
        limitsCounter = limitsCounter + 1;

        //utworz hbox
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        hBox.setPrefHeight(Region.USE_COMPUTED_SIZE);

        //utworz komponenty potrzebne do hboxa
        TextField tfLim = new TextField();
        ButtonFields bf = new ButtonFields();
        bf.setText(signsArr.get(bf.getCounter()));
        TextField tfVal = new TextField();
        tfVal.setPrefWidth(60);
        Button submit = new Button("Submit"); //TODO: ustawić margines lewy
        submit.setDisable(true);

        setTfLimListener(tfLim, submit);
        setTfValListener(tfVal, submit);
        setButtonFieldListener(bf);
        setSubmitLimitListener(submit, tfLim, bf, tfVal);
        addLimitToHBox(hBox, tfLim, bf, tfVal, submit);
        limitsVBox.getChildren().add(hBox);
    }

    ////////////////////////////////////////
    private void setTfLimListener(TextField tfLim, Button submit) {
        //TODO: ustaw listenera dla pola tekstowego z funkcja
        tfLim.textProperty().addListener((observable, oldValue, newValue) -> {
            String tfLimText = newValue;
            Expression ex = new Expression(tfLimText);
            isTfLimOk = true;
            if(isTfValOk && isTfLimOk) submit.setDisable(false);
        });
    }

    private void setTfValListener(TextField tfVal, Button submit) {
        tfVal.textProperty().addListener((observable, oldValue, newValue)  -> {
            String text = tfVal.getText();
            double value;
            if(text.matches("^[0-9]+\\.?[0-9]*$")) {
                value = Double.parseDouble(tfVal.getText());
                isTfValOk = true;
            } else {
                isTfValOk = false;
            }
            if(isTfValOk && isTfLimOk) submit.setDisable(false);
        });
    }

    private void setButtonFieldListener(ButtonFields bf) {
        bf.setOnAction(event -> {
            bf.setCounter(bf.getCounter() + 1);
            bf.setText(signsArr.get(bf.getCounter()));
        });
    }

    private void setSubmitLimitListener(Button submit, TextField tfLim, ButtonFields bf, TextField tfVal) {
        submit.setOnAction(event -> {
            String tfString = tfLim.getText(); System.out.println(tfString);
            ArrayList<String> tmpDecisionVars = new ArrayList<>();

            Function f = createFunctionFromString(submit, tfString, tmpDecisionVars);

            if(f.checkSyntax()) {
                functions.add(f);
                lblErrors.setText(((Double) f.calculate(8.5, 2, 1)).toString());
                Label lbl = new Label(tfString + " " + bf.getText() + " " + tfVal.getText());
                limitsVBox.getChildren().remove(limitsCounter - 1);
                limitsVBox.getChildren().add(limitsCounter - 1, lbl);
                isAddReleased = true;
                btnAddLimit.setDisable(!isAddReleased);
            }
            else {
                lblErrors.setText("Syntax error while parsing function..."); //never
            }

        });
    }

    private void addLimitToHBox(HBox hBox, TextField tfLim, ButtonFields bf, TextField tfVal, Button submit){
        hBox.getChildren().add(tfLim);
        hBox.getChildren().add(bf);
        hBox.getChildren().add(tfVal);
        hBox.getChildren().add(submit);
    }
    ////////////////////////////////////////
    private Function createFunctionFromString(Button btn, String tfString, ArrayList<String> tmpDecisionVars) {
        char[] tfArr = tfString.toCharArray();
        //znajdowanie zmiennych decyzyjnych postaci xLICZBA
        for(int i = 0; i < tfArr.length; i++){
            System.out.println(tfArr[i]);
            if(tfArr[i] == 'x'){
                String num = "";
                if(i < tfArr.length - 1) {
                    Character ch = tfArr[++i];
                    System.out.print(ch);
                    if (Character.isDigit(ch)) {
                        num += ch;
                        if(i < tfArr.length - 1) {
                            while (Character.isDigit(tfArr[i+1])) {
                                ch = tfArr[++i];
                                System.out.print(ch);
                                num += ch;
                                if(i == tfArr.length - 1) break;
                            }
                        }
                    }
                    String decVar = "x" + num;
                    System.out.println(decVar);
                    if (!tmpDecisionVars.contains(decVar)) {
                        tmpDecisionVars.add(decVar);
                    }
                    if (!decisionVars.contains(decVar)) {
                        decisionVars.add(decVar);
                    }
                }
            }
        }
        int decisionVarCounter = tmpDecisionVars.size();
        String fcnString = "f(";
        //odtworzenie argumentow funkcji
        for(int i = 0; i < decisionVarCounter; i++){
            fcnString = fcnString + tmpDecisionVars.get(i);
            if(i != decisionVarCounter - 1) {
                fcnString += ", ";
            }
        }
        fcnString += ") = " + tfString;
        System.out.println(fcnString);
        Function f = new Function(fcnString);
        return f;
    }
    ////////////////////////////////////////

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
