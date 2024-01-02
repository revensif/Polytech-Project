package edu.project.FirstTab;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

import static edu.project.HelloController.*;

public final class FirstTabUtils {

    private FirstTabUtils() {
    }

    public static void deleteAllButtons(AnchorPane firstTabPane, Button entityButton) {
        firstTabPane.getChildren().removeIf((node) -> ((node instanceof Button) && (!node.equals(entityButton))));
    }

    private static TextField makeCopyOfTextField(TextField textfield, TextField circleTextField, int counterForCircle) {
        TextField result = new TextField();
        result.setAlignment(textfield.getAlignment());
        result.setEditable(textfield.isEditable());
        result.setLayoutX(textfield.getLayoutX());
        result.setLayoutY(textfield.getLayoutY());
        result.setMouseTransparent(textfield.isMouseTransparent());
        result.setPrefHeight(textfield.getPrefHeight());
        result.setPrefWidth(textfield.getPrefWidth());
        result.getStyleClass().add(textfield.getStyleClass().getFirst());
        result.setPromptText(textfield.getPromptText());
        if (textfield.equals(circleTextField)) {
            result.getStyleClass().add(textfield.getStyleClass().getLast());
            result.setText(String.valueOf(counterForCircle));
        }
        return result;
    }

    public static void changeHBoxSettings(HBox firstHBox, HBox hBox, int i, int j, double correction) {
        hBox.setLayoutX(firstHBox.getLayoutX() + correction + (LAYOUT_X * i));
        hBox.setLayoutY(firstHBox.getLayoutY() + (LAYOUT_Y * j));
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(SPACING);
        hBox.setPrefHeight(firstHBox.getPrefHeight());
        hBox.setPrefWidth(firstHBox.getPrefWidth());
    }

    public static HBox getHbox(TextField circleTextField,
                               TextField firstTextField,
                               TextField secondTextField,
                               TextField thirdTextField,
                               TextField fourthTextField,
                               int counterForCircle) {
        HBox result = new HBox();
        List<TextField> textFields = List.of(circleTextField, firstTextField, secondTextField, thirdTextField, fourthTextField);
        for (TextField textField : textFields) {
            TextField copy = makeCopyOfTextField(textField, circleTextField, counterForCircle);
            result.getChildren().add(copy);
        }
        return result;
    }

    public static Button getButton(int i, int j, Button subLevelButton) {
        Button result = new Button();
        result.setLayoutX(subLevelButton.getLayoutX() + (LAYOUT_X * i));
        result.setLayoutY(subLevelButton.getLayoutY() + (LAYOUT_Y * j));
        result.setMnemonicParsing(subLevelButton.isMnemonicParsing());
        result.setPrefHeight(subLevelButton.getPrefHeight());
        result.setPrefWidth(subLevelButton.getPrefWidth());
        result.getStyleClass().add(subLevelButton.getStyleClass().getLast());
        result.setOnAction(subLevelButton.getOnAction());
        result.setText(subLevelButton.getText());
        result.setFont(subLevelButton.getFont());
        result.setEffect(subLevelButton.getEffect());
        return result;
    }

    public static void removeUselessButtons(ActionEvent event, AnchorPane firstTabPane) {
        Button pressedButton = (Button) event.getSource();
        List<Node> list = new ArrayList<>();
        for (Node node : firstTabPane.getChildren()) {
            if (node instanceof Button) {
                if ((node.getLayoutX() > pressedButton.getLayoutX()) && (node.getLayoutY() > pressedButton.getLayoutY())) {
                    list.add(node);
                }
            }
        }
        for (Node node : list) {
            firstTabPane.getChildren().remove(node);
        }
    }

    public static int[] getButtonPosition(ActionEvent event, Button subLevelButton) {
        Button pressedButton = (Button) event.getSource();
        double layoutX = pressedButton.getLayoutX();
        double layoutY = pressedButton.getLayoutY();
        int i = 0;
        int j = 0;
        while (layoutX != subLevelButton.getLayoutX()) {
            layoutX -= LAYOUT_X;
            i++;
        }
        while (layoutY != subLevelButton.getLayoutY()) {
            layoutY -= LAYOUT_Y;
            j++;
        }
        return new int[]{i, j};
    }


}
