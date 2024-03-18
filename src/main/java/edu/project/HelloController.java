package edu.project;

import edu.project.FirstTab.Point;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.util.*;

import static edu.project.FirstTab.FirstTabUtils.*;
import static edu.project.SecondTab.SecondTabUtils.*;

public class HelloController {
    private final Map<Point, Integer> BUTTON_CLICKS = new HashMap<>();

    private final Map<Point, Integer> PARAMETER_CLICKS = new HashMap<>();
    private final List<Entity> ENTITIES = new ArrayList<>();
    private final Set<Entity> ENTITIES_FOR_MATRIX = new HashSet<>();
    private final Set<Entity> DISCLOSURE_BUTTONS_FOR_MATRIX = new HashSet<>();
    private final Set<Entity> CLOSURE_BUTTONS_FOR_MATRIX = new HashSet<>();
    public static final int LAYOUT_X = 40;
    public static final int LAYOUT_Y = 80;
    public static final double SPACING = 1.0;
    public static final double SQUARE_SIZE = 300.0;
    private int counterForCircle;
    private int counterForY = 1;
    private static final String PARAMETER = "Параметр ";
    private int mainEntitiesCounter = 1;
    private Button prevButton;

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
    private Tab matrixOfConnectionsTab;

    @FXML
    private AnchorPane thirdTabPane;

    @FXML
    private TextField emptySquareField;

    @FXML
    protected void onSubLevelClicked(ActionEvent event) {
        if (ENTITIES.isEmpty()) {
            ENTITIES.add(new Entity("1", firstTabHBox.getChildren().get(1)));
        }
        int[] position = getButtonPosition(event, subLevelButton);
        Point newPoint = new Point(position[0], position[1]);
        int clicks = BUTTON_CLICKS.getOrDefault(newPoint, 0);
        counterForCircle = 2 + position[0];
        HBox hBox = getHbox(circleTextField, firstTextField, secondTextField, thirdTextField, fourthTextField, counterForCircle);
        if (clicks > 0) {
            hBox.getChildren().removeFirst();
            changeHBoxSettings(firstTabHBox, hBox, position[0] + 1, counterForY, 16.0);
            removeUselessButtons(event, firstTabPane);
            int[] prevButtonPosition = getInts(
                    prevButton.getLayoutX(),
                    prevButton.getLayoutY(),
                    subLevelButton.getLayoutX(),
                    subLevelButton.getLayoutY());
            int diff = prevButtonPosition[0] - position[0];
            String level = ENTITIES.getLast().level().substring(0, ENTITIES.getLast().level().length() - (diff * 2));
            ENTITIES.add(new Entity(level + "." + (clicks + 1), hBox.getChildren().getFirst()));
        } else {
            changeHBoxSettings(firstTabHBox, hBox, position[0] + 1, counterForY, 0.0);
            ENTITIES.add(new Entity(ENTITIES.getLast().level() + ".1", hBox.getChildren().get(1)));
        }
        BUTTON_CLICKS.put(newPoint, BUTTON_CLICKS.getOrDefault(newPoint, 0) + 1);
        Button button = getButton(position[0] + 1, counterForY, subLevelButton);
        prevButton = button;
        counterForY++;
        firstTabPane.getChildren().add(hBox);
        firstTabPane.getChildren().add(button);
        entityButton.setLayoutY(entityButton.getLayoutY() + LAYOUT_Y);
    }

    @FXML
    private void onEntityClicked() {
        if (ENTITIES.isEmpty()) {
            ENTITIES.add(new Entity("1", firstTabHBox.getChildren().get(1)));
        }
        counterForCircle = 1;
        HBox hBox = getHbox(circleTextField, firstTextField, secondTextField, thirdTextField, fourthTextField, counterForCircle);
        Button button = getButton(0, counterForY, subLevelButton);
        changeHBoxSettings(firstTabHBox, hBox, 0, counterForY, 0.0);
        deleteAllButtons(firstTabPane, entityButton);
        entityButton.setLayoutY(entityButton.getLayoutY() + LAYOUT_Y);
        firstTabPane.getChildren().add(hBox);
        firstTabPane.getChildren().add(button);
        counterForY++;
        ENTITIES.add(new Entity(String.valueOf(++mainEntitiesCounter), hBox.getChildren().get(1)));
    }

    @FXML
    private void onParametrizationTabClicked() {
        if (ENTITIES.isEmpty()) {
            ENTITIES.add(new Entity("1", firstTabHBox.getChildren().get(1)));
        }
        if (parametrizationTab.isSelected()) {
            for (Entity entity1 : ENTITIES) {
                for (Entity entity2 : ENTITIES) {
                    TextField cell = new TextField();
                    cell.setPrefWidth(SQUARE_SIZE);
                    cell.setPrefHeight(SQUARE_SIZE);
                    cell.setEditable(true);
                    cell.setAlignment(Pos.CENTER);
                    String level = entity1.level() + "x" + entity2.level();
                    cell.setPromptText(level);
                    ENTITIES_FOR_MATRIX.add(new Entity(level, cell));
                }
            }
            if ((BUTTON_CLICKS.isEmpty()) && (firstTextField.getText().isEmpty())) {
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
        int clicks = PARAMETER_CLICKS.getOrDefault(newPoint, 2);
        AnchorPane anchorPane = (AnchorPane) pressedButton.getParent();
        PARAMETER_CLICKS.put(newPoint, PARAMETER_CLICKS.getOrDefault(newPoint, 2) + 1);
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
                    newHBox.setPrefHeight(HBOX_HEIGHT);
                    newHBox.setPrefWidth(HBOX_WIDTH);
                    newHBox.setSpacing(SPACING);
                    newHBox.setLayoutY(pressedPane.getChildren().getFirst().getLayoutY() + (25 * (clicks - 1)));
                    pressedPane.getChildren().addAll(newHBox);
                }
            }
        }
    }

    @FXML
    private void onMatrixOfConnectionsClicked() {
        List<Entity> firstLevel = new ArrayList<>();
        for (Entity entity : ENTITIES) {
            if (entity.level().length() == 1) {
                firstLevel.add(entity);
            }
        }
        firstLevel = firstLevel.stream()
                .sorted(Comparator.comparingInt(entity -> Integer.parseInt(entity.level())))
                .toList();
        double x = emptySquareField.getLayoutX();
        double y = emptySquareField.getLayoutY();
        createFirstLevelFieldsFromFirstTab(firstLevel, x, y);
        createFirstLevelButtonsClosure(firstLevel, x, y);
        createFirstLevelButtonsDisclosure(firstLevel, x, y);
        firstLevel = ENTITIES_FOR_MATRIX.stream()
                .filter(entity -> entity.level().length() == 3)
                .toList();
        createFirstLevelFieldForMatrix(firstLevel);
    }

    private Button createCircleButtonForLevelDisclosure(double x, double y) {
        Button circleButton = new Button();
        circleButton.setMnemonicParsing(false);
        circleButton.setLayoutX(x);
        circleButton.setLayoutY(y);
        circleButton.setPrefHeight(10);
        circleButton.setPrefWidth(10);
        circleButton.setText("+");
        circleButton.getStyleClass().addAll(circleTextField.getStyleClass());
        circleButton.setFont(new Font("System Bold", 10));
        circleButton.setAlignment(Pos.CENTER);
        circleButton.setOnAction(this::onLevelDisclosureClicked);
        return circleButton;
    }

    private Button createCircleButtonForLevelClosure(double x, double y) {
        Button circleButton = createCircleButtonForLevelDisclosure(x, y);
        circleButton.setOnAction(this::onLevelClosureClicked);
        circleButton.setText("-");
        circleButton.setPrefWidth(20);
        return circleButton;
    }

    private void onLevelDisclosureClicked(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();
        String buttonName = "";
        for (Entity entity : DISCLOSURE_BUTTONS_FOR_MATRIX) {
            Button button = (Button) entity.node();
            if (button.equals(pressedButton)) {
                buttonName = entity.level();
                break;
            }
        }
        String[] nameParts = buttonName.split(":");
        TextField entityFromList = (TextField) ENTITIES.stream()
                .filter(entity -> entity.level().equals(nameParts[0]))
                .toList()
                .getFirst().node();
        double x = pressedButton.getLayoutX() - 5;
        double y = pressedButton.getLayoutY() - 5;
        for (Node node : thirdTabPane.getChildren()) {
            if (node instanceof Button) {
                continue;
            }
            TextField textField = (TextField) node;
            if (textField.getLayoutX() == x
                    && textField.getLayoutY() == y) {
                thirdTabPane.getChildren().remove(node);
                thirdTabPane.getChildren().remove(pressedButton);
                entityFromList = textField;
                break;
            }
        }
        List<Entity> subLevels = ENTITIES.stream()
                .filter(entity -> ((entity.level().startsWith(nameParts[0]))
                        && (entity.level().length() == nameParts[0].length() + 2)))
                .toList();
        if (nameParts[1].equals("above")) {
            changeMatrixIfPressedAbove(subLevels, entityFromList, nameParts, x, y);
        } else {
            changeMatrixIfPressedLeft(subLevels, entityFromList, nameParts, x, y);
        }
    }

    private void onLevelClosureClicked(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();
        String buttonName = "";
        for (Entity entity : CLOSURE_BUTTONS_FOR_MATRIX) {
            Button button = (Button) entity.node();
            if (button.equals(pressedButton)) {
                buttonName = entity.level();
                break;
            }
        }
        String[] nameParts = buttonName.split(":");
        List<Node> nodesForRemove = new ArrayList<>();
        List<String> levels = new ArrayList<>();
        List<Node> nodesWithDifferentLevels = new ArrayList<>();
        if (nameParts[1].equals("above")) {
            double x = pressedButton.getLayoutX() - 140.0;
            double y = emptySquareField.getLayoutY();
            if (getNeededNodesToRemoveAbove(nodesForRemove, nameParts, levels, x)) {
                return;
            }
            thirdTabPane.getChildren().removeAll(nodesForRemove);
            nodesForRemove.clear();
            getNeededTextFieldToMatrix(x, y, nameParts);
            getNeededButtonToMatrix(buttonName);
            Set<String> secondParts = new HashSet<>();
            getNeededTextFieldsToRemoveInMatrixAbove(levels, nodesForRemove, secondParts);
            for (Node node : nodesForRemove) {
                if (node.getLayoutX() == x) {
                    nodesWithDifferentLevels.add(node);
                }
            }
            thirdTabPane.getChildren().removeAll(nodesForRemove);
            getNewTextFieldsToMatrixAbove(nodesWithDifferentLevels, nameParts, x);
        } else {
            double x = emptySquareField.getLayoutX();
            double y = pressedButton.getLayoutY() - 140.0;
            if (getNeededNodesToRemoveLeft(nodesForRemove, nameParts, levels, y)) {
                return;
            }
            thirdTabPane.getChildren().removeAll(nodesForRemove);
            nodesForRemove.clear();
            getNeededTextFieldToMatrix(x, y, nameParts);
            getNeededButtonToMatrix(buttonName);
            Set<String> firstParts = new HashSet<>();
            getNeededTextFieldsToRemoveInMatrixLeft(levels, nodesForRemove, firstParts);
            for (Node node : nodesForRemove) {
                if (node.getLayoutY() == y) {
                    nodesWithDifferentLevels.add(node);
                }
            }
            thirdTabPane.getChildren().removeAll(nodesForRemove);
            getNewTextFieldsToMatrixLeft(nodesWithDifferentLevels, nameParts, y);
        }
    }

    private void getNewTextFieldsToMatrixAbove(List<Node> nodesWithDifferentLevels, String[] nameParts, double x) {
        for (Node node : nodesWithDifferentLevels) {
            TextField currentTextField = (TextField) node;
            String[] parts = currentTextField.getPromptText().split("x");
            for (Entity entity : ENTITIES_FOR_MATRIX) {
                if (entity.level().equals(nameParts[0] + "x" + parts[1])) {
                    TextField newTextField = (TextField) entity.node();
                    if (parts[1].length() > 1) {
                        newTextField.setLayoutX(x);
                        newTextField.setLayoutY(currentTextField.getLayoutY());
                        newTextField.setPrefWidth(SQUARE_SIZE);
                        newTextField.setPrefHeight(currentTextField.getPrefHeight());
                    } else {
                        newTextField.setLayoutY(x);
                        newTextField.setLayoutY(currentTextField.getLayoutY());
                        newTextField.setPrefHeight(SQUARE_SIZE);
                        newTextField.setPrefWidth(SQUARE_SIZE);
                    }
                    thirdTabPane.getChildren().add(newTextField);
                }
            }
        }
    }

    private void getNewTextFieldsToMatrixLeft(List<Node> nodesWithDifferentLevels, String[] nameParts, double y) {
        for (Node node : nodesWithDifferentLevels) {
            TextField currentTextField = (TextField) node;
            String[] parts = currentTextField.getPromptText().split("x");
            for (Entity entity : ENTITIES_FOR_MATRIX) {
                if (entity.level().equals(parts[0] + "x" + nameParts[0])) {
                    TextField newTextField = (TextField) entity.node();
                    if (parts[0].length() > 1) {
                        newTextField.setLayoutX(currentTextField.getLayoutX());
                        newTextField.setLayoutY(y);
                        newTextField.setPrefWidth(currentTextField.getPrefWidth());
                        newTextField.setPrefHeight(SQUARE_SIZE);
                    } else {
                        newTextField.setLayoutY(currentTextField.getLayoutX());
                        newTextField.setLayoutY(y);
                        newTextField.setPrefHeight(SQUARE_SIZE);
                        newTextField.setPrefWidth(SQUARE_SIZE);
                    }
                    thirdTabPane.getChildren().add(newTextField);
                }
            }
        }
    }

    private boolean getNeededNodesToRemoveAbove(List<Node> nodesForRemove, String[] nameParts, List<String> levels, double x) {
        for (Node node : thirdTabPane.getChildren()) {
            if (node instanceof Button) {
                continue;
            }
            TextField textField = (TextField) node;
            for (Entity entity : ENTITIES) {
                TextField textFieldInMap = (TextField) entity.node();
                if ((textFieldInMap.getText().equals(textField.getText()))
                        && (textField.getLayoutY() == emptySquareField.getLayoutY())
                        && (entity.level().startsWith(nameParts[0]))) {
                    if (entity.level().length() == 1) {
                        return true;
                    }
                    nodesForRemove.add(node);
                    levels.add(entity.level());
                }
            }
        }
        for (Node node : thirdTabPane.getChildren()) {
            if (node instanceof TextField) {
                continue;
            }
            Button buttonInMatrix = (Button) node;
            if ((buttonInMatrix.getLayoutX() >= x) && (buttonInMatrix.getLayoutX() < x + SQUARE_SIZE)
                    && (buttonInMatrix.getText().equals("+"))) {
                nodesForRemove.add(node);
            }
        }
        return false;
    }

    private boolean getNeededNodesToRemoveLeft(List<Node> nodesForRemove, String[] nameParts, List<String> levels, double y) {
        for (Node node : thirdTabPane.getChildren()) {
            if (node instanceof Button) {
                continue;
            }
            TextField textField = (TextField) node;
            for (Entity entity : ENTITIES) {
                TextField textFieldInMap = (TextField) entity.node();
                if ((textFieldInMap.getText().equals(textField.getText()))
                        && (textField.getLayoutX() == emptySquareField.getLayoutX())
                        && (entity.level().startsWith(nameParts[0]))) {
                    if (entity.level().length() == 1) {
                        return true;
                    }
                    nodesForRemove.add(node);
                    levels.add(entity.level());
                }
            }
        }
        for (Node node : thirdTabPane.getChildren()) {
            if (node instanceof TextField) {
                continue;
            }
            Button buttonInMatrix = (Button) node;
            if ((buttonInMatrix.getLayoutY() >= y) && (buttonInMatrix.getLayoutY() < y + SQUARE_SIZE)
                    && (buttonInMatrix.getText().equals("+"))) {
                nodesForRemove.add(node);
            }
        }
        return false;
    }

    private void getNeededTextFieldsToRemoveInMatrixAbove(List<String> levels,
                                                          List<Node> nodesForRemove,
                                                          Set<String> secondParts) {
        for (String level : levels) {
            for (Node node : thirdTabPane.getChildren()) {
                if (node instanceof Button) {
                    continue;
                }
                TextField textField = (TextField) node;
                if (textField.getPromptText().startsWith(level + "x")) {
                    nodesForRemove.add(node);
                    for (Entity entity : ENTITIES_FOR_MATRIX) {
                        if (textField.getPromptText().equals(entity.level())) {
                            TextField textFieldInMap = (TextField) entity.node();
                            textFieldInMap.setText(textField.getText());
                        }
                    }
                    String secondPart = textField.getPromptText().split("x")[1];
                    secondParts.add(secondPart);
                }
            }
        }
    }

    private void getNeededTextFieldsToRemoveInMatrixLeft(List<String> levels,
                                                         List<Node> nodesForRemove,
                                                         Set<String> firstParts) {
        for (String level : levels) {
            for (Node node : thirdTabPane.getChildren()) {
                if (node instanceof Button) {
                    continue;
                }
                TextField textField = (TextField) node;
                if (textField.getPromptText().endsWith("x" + level)) {
                    nodesForRemove.add(node);
                    for (Entity entity : ENTITIES_FOR_MATRIX) {
                        if (textField.getPromptText().equals(entity.level())) {
                            TextField textFieldInMap = (TextField) entity.node();
                            textFieldInMap.setText(textField.getText());
                        }
                    }
                    String firstPart = textField.getPromptText().split("x")[0];
                    firstParts.add(firstPart);
                }
            }
        }
    }

    private void getNeededTextFieldToMatrix(double x, double y, String[] nameParts) {
        TextField firstLevelField = (TextField) ENTITIES.stream()
                .filter(entity -> entity.level().equals(nameParts[0]))
                .toList()
                .getFirst().node();
        TextField newTextField = new TextField();
        newTextField.setLayoutX(x);
        newTextField.setLayoutY(y);
        newTextField.setPrefWidth(SQUARE_SIZE);
        newTextField.setPrefHeight(SQUARE_SIZE);
        newTextField.setText(firstLevelField.getText());
        newTextField.setEditable(false);
        newTextField.setMouseTransparent(true);
        newTextField.setAlignment(Pos.CENTER);
        thirdTabPane.getChildren().add(newTextField);
    }

    private void getNeededButtonToMatrix(String buttonName) {
        for (Entity entity : DISCLOSURE_BUTTONS_FOR_MATRIX) {
            if (entity.level().equals(buttonName)) {
                Button newButton = (Button) entity.node();
                thirdTabPane.getChildren().add(newButton);
                break;
            }
        }
    }

    private void changeMatrixIfPressedAbove(List<Entity> subLevels,
                                            TextField entityFromList,
                                            String[] nameParts,
                                            double x,
                                            double y) {
        addNewSubLevelEntitiesAbove(subLevels, entityFromList, x, y);
        List<TextField> currentFieldsInMatrix = new ArrayList<>();
        for (Node node : thirdTabPane.getChildren()) {
            if (node instanceof Button) {
                continue;
            }
            TextField textField = (TextField) node;
            if (textField.getPromptText().startsWith(nameParts[0] + "x")) {
                currentFieldsInMatrix.add(textField);
            }
        }
        thirdTabPane.getChildren().removeAll(currentFieldsInMatrix);
        addNewSubLevelEntitiesToMatrixAbove(currentFieldsInMatrix, subLevels, entityFromList);
        changeTextFieldInMatrix(currentFieldsInMatrix);
    }

    private void changeMatrixIfPressedLeft(List<Entity> subLevels,
                                           TextField entityFromList,
                                           String[] nameParts,
                                           double x,
                                           double y) {
        addNewSubLevelEntitiesLeft(subLevels, entityFromList, x, y);
        List<TextField> currentFieldsInMatrix = new ArrayList<>();
        for (Node node : thirdTabPane.getChildren()) {
            if (node instanceof Button) {
                continue;
            }
            TextField textField = (TextField) node;
            if (textField.getPromptText().endsWith("x" + nameParts[0])) {
                currentFieldsInMatrix.add(textField);
            }
        }
        thirdTabPane.getChildren().removeAll(currentFieldsInMatrix);
        addNewSubLevelEntitiesToMatrixLeft(currentFieldsInMatrix, subLevels, entityFromList);
        changeTextFieldInMatrix(currentFieldsInMatrix);
    }

    private void addNewSubLevelEntitiesAbove(List<Entity> subLevels, TextField entityFromList, double x, double y) {
        for (Entity subLevel : subLevels) {
            TextField textField = (TextField) subLevel.node();
            TextField newField = makeCopyOfTextField(textField, circleTextField, 0);
            String[] numbers = subLevel.level().split("\\.");
            int lastNumber = Integer.parseInt(numbers[numbers.length - 1]) - 1;
            newField.setLayoutX(x + ((entityFromList.getPrefWidth() / subLevels.size()) * lastNumber));
            newField.setLayoutY(y);
            newField.setPrefHeight(SQUARE_SIZE);
            newField.setPrefWidth(entityFromList.getPrefWidth() / subLevels.size());
            newField.setText(textField.getText());
            newField.setAlignment(Pos.CENTER);
            newField.setEditable(false);
            newField.setMouseTransparent(true);
            thirdTabPane.getChildren().add(newField);
            List<Entity> subLevelsOfSubLevel = getSubLevelsOfSubLevel(ENTITIES, subLevel);
            if (!subLevelsOfSubLevel.isEmpty()) {
                Button subLevelButton = createCircleButtonForLevelDisclosure(newField.getLayoutX() + 5,
                        newField.getLayoutY() + 5);
                DISCLOSURE_BUTTONS_FOR_MATRIX.add(new Entity(subLevel.level() + ":above", subLevelButton));
                thirdTabPane.getChildren().add(subLevelButton);
            }
        }
    }

    private void changeTextFieldInMatrix(List<TextField> currentFieldsInMatrix) {
        for (TextField textField : currentFieldsInMatrix) {
            for (Entity entityInMap : ENTITIES_FOR_MATRIX) {
                TextField textFieldInMap = (TextField) entityInMap.node();
                if (textFieldInMap.getPromptText().equals(textField.getPromptText())) {
                    textFieldInMap.setText(textField.getText());
                    textFieldInMap.setLayoutX(textField.getLayoutX());
                    textFieldInMap.setLayoutY(textField.getLayoutY());
                    textFieldInMap.setPrefHeight(textField.getPrefHeight());
                    textFieldInMap.setPrefWidth(textField.getPrefWidth());
                }
            }
        }
    }

    private void addNewSubLevelEntitiesToMatrixAbove(List<TextField> currentFieldsInMatrix,
                                                     List<Entity> subLevels,
                                                     TextField entityFromList) {
        for (TextField textField : currentFieldsInMatrix) {
            String secondPart = textField.getPromptText().split("x")[1];
            for (Entity entity : subLevels) {
                String level = entity.level();
                for (Entity entityInMap : ENTITIES_FOR_MATRIX) {
                    TextField textFieldInMap = (TextField) entityInMap.node();
                    if (textFieldInMap.getPromptText().equals(level + "x" + secondPart)) {
                        String[] numbers = level.split("\\.");
                        int lastNumber = Integer.parseInt(numbers[numbers.length - 1]) - 1;
                        textFieldInMap.setLayoutX(textField.getLayoutX()
                                + (entityFromList.getPrefWidth() / subLevels.size() * lastNumber));
                        textFieldInMap.setLayoutY(textField.getLayoutY());
                        textFieldInMap.setPrefHeight(textField.getPrefHeight());
                        textFieldInMap.setPrefWidth(entityFromList.getPrefWidth() / subLevels.size());
                        textFieldInMap.setAlignment(Pos.CENTER);
                        textFieldInMap.setEditable(true);
                        textFieldInMap.setMouseTransparent(false);
                        thirdTabPane.getChildren().add(textFieldInMap);
                    }
                }
            }
        }
    }

    private void addNewSubLevelEntitiesLeft(List<Entity> subLevels, TextField entityFromList, double x, double y) {
        for (Entity subLevel : subLevels) {
            TextField textField = (TextField) subLevel.node();
            TextField newField = makeCopyOfTextField(textField, circleTextField, 0);
            String[] numbers = subLevel.level().split("\\.");
            int lastNumber = Integer.parseInt(numbers[numbers.length - 1]) - 1;
            newField.setLayoutX(x);
            newField.setLayoutY(y + ((entityFromList.getPrefWidth() / subLevels.size()) * lastNumber));
            newField.setPrefWidth(SQUARE_SIZE);
            newField.setPrefHeight(entityFromList.getPrefHeight() / subLevels.size());
            newField.setText(textField.getText());
            newField.setAlignment(Pos.CENTER);
            newField.setEditable(false);
            newField.setMouseTransparent(true);
            thirdTabPane.getChildren().add(newField);
            List<Entity> subLevelsOfSubLevel = getSubLevelsOfSubLevel(ENTITIES, subLevel);
            if (!subLevelsOfSubLevel.isEmpty()) {
                Button subLevelButton = createCircleButtonForLevelDisclosure(newField.getLayoutX() + 5,
                        newField.getLayoutY() + 5);
                DISCLOSURE_BUTTONS_FOR_MATRIX.add(new Entity(subLevel.level() + ":left", subLevelButton));
                thirdTabPane.getChildren().add(subLevelButton);
            }
        }
    }

    private void addNewSubLevelEntitiesToMatrixLeft(List<TextField> currentFieldsInMatrix,
                                                    List<Entity> subLevels,
                                                    TextField entityFromList) {
        for (TextField textField : currentFieldsInMatrix) {
            String firstPart = textField.getPromptText().split("x")[0];
            for (Entity entity : subLevels) {
                String level = entity.level();
                for (Entity entityInMap : ENTITIES_FOR_MATRIX) {
                    TextField textFieldInMap = (TextField) entityInMap.node();
                    if (textFieldInMap.getPromptText().equals(firstPart + "x" + level)) {
                        String[] numbers = level.split("\\.");
                        int lastNumber = Integer.parseInt(numbers[numbers.length - 1]) - 1;
                        textFieldInMap.setLayoutX(textField.getLayoutX());
                        textFieldInMap.setLayoutY(textField.getLayoutY()
                                + (entityFromList.getPrefWidth() / subLevels.size() * lastNumber));
                        textFieldInMap.setPrefHeight(textField.getPrefHeight() / subLevels.size());
                        textFieldInMap.setPrefWidth(textField.getPrefWidth());
                        textFieldInMap.setAlignment(Pos.CENTER);
                        textFieldInMap.setEditable(true);
                        textFieldInMap.setMouseTransparent(false);
                        thirdTabPane.getChildren().add(textFieldInMap);
                    }
                }
            }
        }
    }

    private static List<Entity> getSubLevelsOfSubLevel(List<Entity> ENTITIES, Entity subLevel) {
        return ENTITIES.stream()
                .filter(entity -> ((entity.level().startsWith(subLevel.level()))
                        && (entity.level().length() == subLevel.level().length() + 2)))
                .toList();
    }

    private void createFirstLevelButtonsDisclosure(List<Entity> firstLevel, double x, double y) {
        for (int i = 1; i <= firstLevel.size(); i++) {
            boolean flag = true;
            Entity firstLevelEntity = firstLevel.get(i - 1);
            long size = ENTITIES.stream()
                    .filter(entity -> ((entity.level().startsWith(firstLevelEntity.level()))
                            && (entity.level().length() > firstLevelEntity.level().length())))
                    .count();
            if (size != 0) {
                Button horizontalButton = createCircleButtonForLevelDisclosure(x + 5 + (i * SQUARE_SIZE), y + 5);
                Button verticalButton = createCircleButtonForLevelDisclosure(x + 5, y + 5 + (i * SQUARE_SIZE));
                for (Entity entity : DISCLOSURE_BUTTONS_FOR_MATRIX) {
                    Button button = (Button) entity.node();
                    if (button.getLayoutX() == horizontalButton.getLayoutX()
                            && button.getLayoutY() == horizontalButton.getLayoutY()) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    DISCLOSURE_BUTTONS_FOR_MATRIX.addAll(List.of(
                            new Entity(firstLevelEntity.level() + ":above", horizontalButton),
                            new Entity(firstLevelEntity.level() + ":left", verticalButton)));
                    thirdTabPane.getChildren().addAll(horizontalButton, verticalButton);
                }
            }
        }
    }

    private void createFirstLevelButtonsClosure(List<Entity> firstLevel, double x, double y) {
        for (int i = 1; i <= firstLevel.size(); i++) {
            boolean flag = true;
            Entity firstLevelEntity = firstLevel.get(i - 1);
            long size = ENTITIES.stream()
                    .filter(entity -> ((entity.level().startsWith(firstLevelEntity.level()))
                            && (entity.level().length() > firstLevelEntity.level().length())))
                    .count();
            if (size != 0) {
                Button horizontalButton = createCircleButtonForLevelClosure(x + 140.0 + (i * SQUARE_SIZE), y - 30);
                Button verticalButton = createCircleButtonForLevelClosure(x - 30, y + 140.0 + (i * SQUARE_SIZE));
                for (Entity entity : DISCLOSURE_BUTTONS_FOR_MATRIX) {
                    Button button = (Button) entity.node();
                    if (button.getLayoutX() == horizontalButton.getLayoutX()
                            && button.getLayoutY() == horizontalButton.getLayoutY()) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    CLOSURE_BUTTONS_FOR_MATRIX.addAll(List.of(
                            new Entity(i + ":above", horizontalButton),
                            new Entity(i + ":left", verticalButton)));
                    thirdTabPane.getChildren().addAll(horizontalButton, verticalButton);
                }
            }
        }
    }

    private void createFirstLevelFieldForMatrix(List<Entity> firstLevel) {
        for (Entity entity : firstLevel) {
            boolean flag = false;
            TextField textField = (TextField) entity.node();
            String[] levels = entity.level().split("x");
            int levelLeft = Integer.parseInt(levels[0]);
            int levelRight = Integer.parseInt(levels[1]);
            textField.setLayoutX(emptySquareField.getLayoutX() + (levelLeft * SQUARE_SIZE));
            textField.setLayoutY(emptySquareField.getLayoutY() + (levelRight * SQUARE_SIZE));
            for (Node node : thirdTabPane.getChildren()) {
                if (node instanceof Button) {
                    continue;
                }
                TextField field = (TextField) node;
                if (field.getPromptText().equals(textField.getPromptText())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                continue;
            }
            thirdTabPane.getChildren().add(textField);
        }
    }

    private void createFirstLevelFieldsFromFirstTab(List<Entity> firstLevel, double x, double y) {
        for (int i = 1; i <= firstLevel.size(); i++) {
            boolean flag = false;
            TextField textFieldFromFirstTab = (TextField) firstLevel.get(i - 1).node();
            TextField[] textFields = new TextField[2];
            for (int j = 0; j < 2; j++) {
                textFields[j] = new TextField();
                textFields[j].setPrefWidth(SQUARE_SIZE);
                textFields[j].setPrefHeight(SQUARE_SIZE);
                textFields[j].setText(textFieldFromFirstTab.getText());
                textFields[j].setEditable(false);
                textFields[j].setMouseTransparent(true);
                textFields[j].setAlignment(Pos.CENTER);
            }
            textFields[0].setLayoutX(x + (SQUARE_SIZE * i));
            textFields[0].setLayoutY(y);
            textFields[1].setLayoutX(x);
            textFields[1].setLayoutY(y + (SQUARE_SIZE * i));
            for (int j = 0; j < 2; j++) {
                for (Node node : thirdTabPane.getChildren()) {
                    if (node instanceof Button) {
                        continue;
                    }
                    TextField textField = (TextField) node;
                    if (textField.getLayoutX() == textFields[j].getLayoutX()
                            && textField.getLayoutY() == textFields[j].getLayoutY()) {
                        if (!textField.getText().equals(textFields[j].getText())
                                && textField.getPrefWidth() == textFields[j].getPrefWidth()) {
                            textField.setText(textFieldFromFirstTab.getText());
                        }
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                continue;
            }
            thirdTabPane.getChildren().addAll(textFields);
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

    public record Entity(String level, Node node) {
    }

}