package edu.project.SecondTab;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import static edu.project.FirstTab.FirstTabUtils.getButton;
import static edu.project.HelloController.*;

public final class SecondTabUtils {

    private static final int FIRST_TAB_SIZE = 5;
    private static final double HBOX_HEIGHT = 25.0;
    private static final double HBOX_WIDTH = 380.0;
    private static final double SCROLL_PANE_HEIGHT = 51.0;
    private static final double SCROLL_PANE_WIDTH = 400.0;

    private SecondTabUtils() {
    }

    public static void getAllHBoxToParametrizationTab(
            AnchorPane firstTabPane,
            HBox firstTabHBox,
            AnchorPane secondTabPane,
            HBox secondTabHBox,
            Button parameterButton,
            TextField parameterField,
            TextField measurementField,
            TextField valueField) {
        for (Node node : firstTabPane.getChildren()) {
            if (node instanceof HBox) {
                if (node.equals(firstTabHBox)) {
                    continue;
                }
                HBox hbox = makeCopyOfHBox((HBox) node, parameterField, measurementField, valueField);
                secondTabPane.getChildren().add(hbox);
                int[] position = getHBoxPosition(hbox, secondTabHBox);
                Button button = getButton(position[0], position[1], parameterButton);
                secondTabPane.getChildren().add(button);
            }
        }
    }

    public static int[] getHBoxPosition(HBox hBoxFromFirstTab, HBox secondTabHBox) {
        return getInts(hBoxFromFirstTab.getLayoutX(), hBoxFromFirstTab.getLayoutY(), secondTabHBox.getLayoutX(), secondTabHBox.getLayoutY());
    }

    private static HBox makeCopyOfHBox(HBox hBox, TextField parameterField, TextField measurementField, TextField valueField) {
        HBox result = new HBox();
        TextField circleField = (TextField) hBox.getChildren().getFirst();
        TextField firstCopy = makeCopyOfTextFieldForSecondTab(circleField);
        result.getChildren().add(firstCopy);
        if (hBox.getChildren().size() == FIRST_TAB_SIZE) {
            TextField nameField = (TextField) hBox.getChildren().get(1);
            TextField secondCopy = makeCopyOfTextFieldForSecondTab(nameField);
            result.getChildren().add(secondCopy);
        }
        result.setAlignment(hBox.getAlignment());
        result.setLayoutX(hBox.getLayoutX());
        result.setLayoutY(hBox.getLayoutY());
        result.setAlignment(Pos.CENTER);
        result.setSpacing(SPACING);
        result.setPrefHeight(hBox.getPrefHeight());
        result.setPrefWidth(hBox.getPrefWidth());
        result.getChildren().add(makeCopyOfScrollPane(parameterField, measurementField, valueField));
        return result;
    }

    private static ScrollPane makeCopyOfScrollPane(TextField parameterField, TextField measurementField, TextField valueField) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefHeight(HBOX_HEIGHT);
        hBox.setPrefWidth(HBOX_WIDTH);
        hBox.setSpacing(SPACING);
        TextField[] fields = copyTextFieldsFromHBox(parameterField, measurementField, valueField);
        hBox.getChildren().addAll(fields);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(hBox);
        ScrollPane result = new ScrollPane(anchorPane);
        result.setPannable(true);
        result.setPrefHeight(SCROLL_PANE_HEIGHT);
        result.setPrefWidth(SCROLL_PANE_WIDTH);
        return result;
    }

    public static TextField[] copyTextFieldsFromHBox(TextField parameterField, TextField measurementField, TextField valueField) {
        TextField parameter = makeCopyOfTextFieldForSecondTab(parameterField);
        TextField measurement = makeCopyOfTextFieldForSecondTab(measurementField);
        TextField value = makeCopyOfTextFieldForSecondTab(valueField);
        parameter.setMouseTransparent(false);
        measurement.setMouseTransparent(false);
        value.setMouseTransparent(false);
        parameter.setEditable(true);
        measurement.setEditable(true);
        value.setEditable(true);
        parameter.setText("");
        measurement.setText("");
        value.setText("");
        return new TextField[]{parameter, measurement, value};
    }

    private static TextField makeCopyOfTextFieldForSecondTab(TextField textfield) {
        TextField result = new TextField();
        changeTextField(result, textfield);
        result.getStyleClass().add(textfield.getStyleClass().getLast());
        result.setText(String.valueOf(textfield.getText()));
        result.setEditable(false);
        result.setMouseTransparent(true);
        return result;
    }

    public static AnchorPane getHBoxInAnchorPaneThatIsInScrollPane(HBox hBox) throws IllegalAccessException {
        for (Node node : hBox.getChildren()) {
            if (node instanceof ScrollPane) {
                return (AnchorPane) ((ScrollPane) node).getContent();
            }
        }
        throw new IllegalAccessException("Произошла ошибка!");
    }
}