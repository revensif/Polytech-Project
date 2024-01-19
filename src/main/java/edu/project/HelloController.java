package edu.project;

import edu.project.FirstTab.Point;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;

import static edu.project.FirstTab.FirstTabUtils.*;
import static edu.project.SecondTab.SecondTabUtils.*;

public class HelloController {
    private static final Map<Point, Integer> buttonClicks = new HashMap<>();

    private static final Map<Point, Integer> parameterClicks = new HashMap<>();
    public static final int LAYOUT_X = 40;
    public static final int LAYOUT_Y = 80;
    public static final double SPACING = 1.0;
    private int counterForCircle;
    private int counterForY = 1;
    private static final String PARAMETER = "Параметр ";

    @FXML
    private TabPane mainTabPane;

    @FXML
    private AnchorPane firstTabPane;

    @FXML
    private HBox firstTabHBox;

    @FXML
    private Button subLevelButton;

    @FXML
    private Button entityButton;

    @FXML
    private TextField firstTextField;

    @FXML
    private TextField secondTextField;

    @FXML
    private TextField thirdTextField;

    @FXML
    private TextField fourthTextField;

    @FXML
    private TextField circleTextField;

    @FXML
    private Tab parametrizationTab;

    @FXML
    private HBox secondTabHBox;

    @FXML
    private Button parameterButton;

    @FXML
    private AnchorPane secondTabPane;

    @FXML
    private TextField parameterField;

    @FXML
    private TextField measurementField;

    @FXML
    private TextField valueField;

    @FXML
    protected void onSubLevelClicked(ActionEvent event) {
        int[] position = getButtonPosition(event, subLevelButton);
        Point newPoint = new Point(position[0], position[1]);
        int clicks = buttonClicks.getOrDefault(newPoint, 0);
        counterForCircle = 2 + position[0];
        HBox hBox = getHbox(circleTextField, firstTextField, secondTextField, thirdTextField, fourthTextField, counterForCircle);
        if (clicks > 0) {
            hBox.getChildren().removeFirst();
            changeHBoxSettings(firstTabHBox, hBox, position[0] + 1, counterForY, 16.0);
            removeUselessButtons(event, firstTabPane);
        } else {
            changeHBoxSettings(firstTabHBox, hBox, position[0] + 1, counterForY, 0.0);
        }
        buttonClicks.put(newPoint, buttonClicks.getOrDefault(newPoint, 0) + 1);
        Button button = getButton(position[0] + 1, counterForY, subLevelButton);
        counterForY++;
        firstTabPane.getChildren().add(hBox);
        firstTabPane.getChildren().add(button);
        entityButton.setLayoutY(entityButton.getLayoutY() + LAYOUT_Y);
    }

    @FXML
    private void onEntityClicked() {
        counterForCircle = 1;
        HBox hBox = getHbox(circleTextField, firstTextField, secondTextField, thirdTextField, fourthTextField, counterForCircle);
        Button button = getButton(0, counterForY, subLevelButton);
        changeHBoxSettings(firstTabHBox, hBox, 0, counterForY, 0.0);
        deleteAllButtons(firstTabPane, entityButton);
        entityButton.setLayoutY(entityButton.getLayoutY() + LAYOUT_Y);
        firstTabPane.getChildren().add(hBox);
        firstTabPane.getChildren().add(button);
        counterForY++;
    }

    @FXML
    private void onParametrizationTabClicked() {
        if (parametrizationTab.isSelected()) {
            if ((buttonClicks.isEmpty()) && (firstTextField.getText().isEmpty())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Невозможно перейти на эту вкладку!");
                alert.setContentText("Должна быть создана хотя бы одна сущность!");
                alert.showAndWait();
                mainTabPane.getSelectionModel().select(0);
            } else {
                TextField textField = (TextField) secondTabHBox.getChildren().get(1);
                textField.setText(firstTextField.getText());
                textField.setMouseTransparent(true);
                textField.setEditable(false);
                getAllHBoxToParametrizationTab(
                        firstTabPane,
                        firstTabHBox,
                        secondTabPane,
                        secondTabHBox,
                        parameterButton,
                        parameterField,
                        measurementField,
                        valueField);
            }
        }
    }

    @FXML
    private void onParameterClicked(ActionEvent event) throws IllegalAccessException {
        Button pressedButton = (Button) event.getSource();
        int[] buttonPosition = getButtonPosition(event, parameterButton);
        Point newPoint = new Point(buttonPosition[0], buttonPosition[1]);
        int clicks = parameterClicks.getOrDefault(newPoint, 2);
        AnchorPane anchorPane = (AnchorPane) pressedButton.getParent();
        parameterClicks.put(newPoint, parameterClicks.getOrDefault(newPoint, 2) + 1);
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof HBox) {
                int[] hBoxPosition = getHBoxPosition((HBox) node, secondTabHBox);
                if ((hBoxPosition[0] == buttonPosition[0]) && (hBoxPosition[1] == buttonPosition[1])) {
                    AnchorPane pressedPane = getHBoxInAnchorPaneThatIsInScrollPane((HBox) node);
                    TextField[] fields = copyTextFieldsFromHBox(parameterField, measurementField, valueField);
                    fields[0].setPromptText(PARAMETER + clicks);
                    HBox newHBox = new HBox();
                    newHBox.getChildren().addAll(fields);
                    newHBox.setAlignment(Pos.CENTER);
                    newHBox.setPrefHeight(25.0);
                    newHBox.setPrefWidth(380.0);
                    newHBox.setSpacing(SPACING);
                    newHBox.setLayoutY(pressedPane.getChildren().getFirst().getLayoutY() + (25 * (clicks - 1)));
                    pressedPane.getChildren().addAll(newHBox);
                }
            }
        }
    }

    public static void changeTextField(TextField result, TextField textfield) {
        result.setAlignment(textfield.getAlignment());
        result.setEditable(textfield.isEditable());
        result.setLayoutX(textfield.getLayoutX());
        result.setLayoutY(textfield.getLayoutY());
        result.setMouseTransparent(textfield.isMouseTransparent());
        result.setPrefHeight(textfield.getPrefHeight());
        result.setPrefWidth(textfield.getPrefWidth());
        result.getStyleClass().add(textfield.getStyleClass().getFirst());
        result.setPromptText(textfield.getPromptText());
    }

    public static int[] getInts(double firstLayoutX, double firstLayoutY, double secondLayoutX, double secondLayoutY) {
        double layoutX = firstLayoutX;
        double layoutY = firstLayoutY;
        int i = 0;
        int j = 0;
        while (layoutX != secondLayoutX) {
            layoutX -= LAYOUT_X;
            i++;
        }
        while (layoutY != secondLayoutY) {
            layoutY -= LAYOUT_Y;
            j++;
        }
        return new int[]{i, j};
    }
}