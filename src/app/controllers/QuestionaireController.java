package app.controllers;

import app.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.apache.commons.lang3.StringUtils;
import org.mariuszgromada.math.mxparser.Function;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created on 29.04.2017.
 */
//TODO: sprawdzanie ograniczen gornych i dolnych dla wszystkich zmiennych decyzyjnych
//TODO: sprawdzanie przypadku dowolnej funkcji, nie tylko wielomianu
//TODO: reset wszystkiego przy przejściu do menu/tworzeniu nowego problemu

public class QuestionaireController implements Initializable {

    static boolean nonPolyEnabled;
    private ArrayList<String> signsArr = new ArrayList<>(Arrays.asList("<", "<=", ">", ">="));
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
    @FXML public Button btnSubmitAllLims;
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
        setTfFunctionListener();
        setLabelsOnStart();
        btnSubmitAllLims.setDisable(true);
        btnSubmitFcn.setDisable(true);
        tfFunction.setDisable(true);
        Algo.epsilon = Double.parseDouble(lblEpsVal.getText());
    }

    ////////////////////////////////////

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

    private void setTfFunctionListener(){
        tfFunction.textProperty().addListener((observable, oldValue, newValue) -> {
            isTfFcnOk = checkExpressionField(newValue);
            if(isTfFcnOk) btnSubmitFcn.setDisable(false);
            else btnSubmitFcn.setDisable(true);
        });
    }

    private void setLabelsOnStart() {
        lblErrors.setText("");
        lblEpsVal.setText("0.1");
        lblFcn.setText("");
        lblResult.setText("Click Compute button to see result.");
    }

    ////////////////////////////////////

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

    ////////////////////////////////////

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
        tfLim.textProperty().addListener((observable, oldValue, newValue) -> {
            isTfLimOk = checkExpressionField(newValue);
            System.out.println(isTfValOk && isTfLimOk);
            if(isTfValOk && isTfLimOk) {
                submit.setDisable(false);
            }
            else submit.setDisable(true);
            System.out.println(isTfLimOk&&isTfValOk);
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
            else submit.setDisable(true);
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

            Function f = createFunctionFromString(tfString, tmpDecisionVars);

            if(f.checkSyntax()) {
                LimitField limit = new LimitField(
                        f, signsArr.get(bf.getCounter()), Double.parseDouble(tfVal.getText()), tmpDecisionVars
                );
                System.out.println(limit.toString());
                Algo.limits.add(limit);
                for (String tmpDecisionVar : tmpDecisionVars) {
                    if (!Algo.containsDecVar(tmpDecisionVar)) {
                        Algo.decisionVars.add(new DecisionVar(tmpDecisionVar));
                    }
                }
                lblErrors.setText(((Double) f.calculate(8.5, 2, 1)).toString());
                Label lbl = new Label(tfString + " " + bf.getText() + " " + tfVal.getText());
                limitsVBox.getChildren().remove(limitsCounter - 1);
                limitsVBox.getChildren().add(limitsCounter - 1, lbl);
                btnAddLimit.setDisable(isAddReleased);
                isAddReleased = true;
                if(limitsCounter > 0) {
                    btnSubmitAllLims.setDisable(false);
                }
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

    @FXML
    public void submitAllLimits(ActionEvent e) {
        lblErrors.setText("Submit all limits not fully implemented...");
        //if(nieWszystkieZmienneMajaOgraniczenieGorneIDolne){
        //    komunikat ze musi dodać wszystkie ogarniczenia
        //}else{
        btnAddLimit.setDisable(true);
        btnSubmitAllLims.setDisable(true);
        tfFunction.setDisable(false);
    }

    ////////////////////////////////////////

    //TODO: dodać przypadek gdy dowolna funkcja, nie tylko wielomiany
    private boolean checkExpressionField(String tfText){
        if(!QuestionaireController.nonPolyEnabled) {
            if(tfText.matches(
                    "^[-]?([0-9]+\\.?[0-9]*[*])?[x][0-9]*(\\^[0-9]+)?" +
                            "([+-]([0-9]+\\.?[0-9]*[*])?[x][0-9]*(\\^[0-9]+)?)*$")
                    ) {
                return true;
            } else {
                return  false;
            }
        }
        else {
            return true;
        }
    }

    private Function createFunctionFromString(String tfString, ArrayList<String> tmpDecisionVars) {
        //znajdowanie zmiennych decyzyjnych postaci xLICZBA
        for(int i = 0; i < tfString.length(); i++){
            System.out.println(tfString.charAt(i));
            if(tfString.charAt(i) == 'x'){
                String num = "";
                while ((++i < tfString.length()) && Character.isDigit(tfString.charAt(i)) ) {
                    System.out.println(tfString.charAt(i));
                    num += tfString.charAt(i);
                }

                String decVar = "x" + num;
                System.out.println(decVar);
                if (!tmpDecisionVars.contains(decVar)) {
                    tmpDecisionVars.add(decVar);
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
        lblErrors.setText("Action submit function not fully implemented...");

        btnAddLimit.setDisable(true);
        ArrayList<String> targetDecisionVars = new ArrayList<>();
        Function targetFcn = createFunctionFromString(tfFunction.getText(), targetDecisionVars);
        isTfFcnOk = targetFcn.checkSyntax();

        for(DecisionVar var: Algo.decisionVars){
            if(!targetDecisionVars.contains(var.name)){
                lblErrors.setText("Target function doesn't contain all limited decision variables!");
                btnAddLimit.setDisable(false);
                return;
            }
        }
        Algo.targetFcn = targetFcn;
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
        lblResult.setText("Action compute not implemented...");
    }

    /////////////////////////////////////

    private void printDialogNodes(Pane pane){
        List<Node> nodes = pane.getChildren();
        for(int i = 0; i < nodes.size(); i++){
            System.out.print("\t");
            System.out.println(nodes.get(i).getClass());
        }
        System.out.println();
    }

    ////////////////////////////////

}
