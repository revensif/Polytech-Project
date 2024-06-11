package edu.project;

import com.udojava.evalex.Expression;
import edu.project.client.GigaChatClient;
import edu.project.first_tab.Point;
import edu.project.repository.JdbcConnectionRepository;
import edu.project.repository.JdbcEntityRepository;
import edu.project.repository.JdbcMathModelRepository;
import edu.project.repository.JdbcParameterRepository;
import edu.project.repository.entity.Connection;
import edu.project.repository.entity.MathModel;
import impl.org.controlsfx.spreadsheet.CellView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static edu.project.first_tab.FirstTabUtils.*;
import static edu.project.second_tab.SecondTabUtils.*;

@Component
@RequiredArgsConstructor
public class PolytechController {
    private final Map<Point, Integer> BUTTON_CLICKS = new HashMap<>();
    private final Map<Point, Integer> PARAMETER_CLICKS = new HashMap<>();
    private final List<Entity> ENTITIES = new ArrayList<>();
    private final Map<String, String> MATH_MODELS = new HashMap<>();
    private final Map<String, Integer> ENTITIES_ID = new HashMap<>();
    public static final int LAYOUT_X = 40;
    public static final int LAYOUT_Y = 80;
    public static final double SPACING = 1.0;
    private int counterForCircle;
    private int counterForY = 1;
    private int counterForModel = 2;
    private int counterForIteration = 0;
    private static final String PARAMETER = "Параметр ";
    private int mainEntitiesCounter = 1;
    private Button prevButton;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab systemTab;

    @FXML
    private Tab parametrizationTab;

    @FXML
    private Tab matrixOfConnectionsTab;

    @FXML
    private Tab propertiesTab;

    @FXML
    private Tab modelTab;

    @FXML
    private Tab resultTab;

    @FXML
    private AnchorPane firstTabPane;

    @FXML
    private HBox firstTabHBox;

    @FXML
    private Button subLevelButton;

    @FXML
    private Button entityButton;

    @FXML
    private Button entityDatabaseButton;

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
    private HBox secondTabHBox;

    @FXML
    private Button parameterButton;

    @FXML
    private Button parameterDatabaseButton;

    @FXML
    private AnchorPane secondTabPane;

    @FXML
    private TextField parameterField;

    @FXML
    private TextField measurementField;

    @FXML
    private TextField valueField;

    @FXML
    private AnchorPane thirdTabPane;

    @FXML
    private TextField verticalEntity;

    @FXML
    private TextField horizontalEntity;

    @FXML
    private TextArea characteristics;

    @FXML
    private TextArea formula;

    @FXML
    private AnchorPane modelPane;

    @FXML
    private Button modelCreation;

    @FXML
    private HBox modelHBox;

    @FXML
    private TextField modelCircle;

    @FXML
    private TextField modelName;

    @FXML
    private TextField formulaField;

    @FXML
    private TextArea parametersField;

    @FXML
    private TextArea connectionChoice;

    @FXML
    private AnchorPane lastTabPane;

    @FXML
    private Button resultDatabaseButton;

    private final GigaChatClient client;

    private final JdbcEntityRepository entityRepository;

    private final JdbcParameterRepository parameterRepository;

    private final JdbcConnectionRepository connectionRepository;

    private final JdbcMathModelRepository mathModelRepository;
    private Tab prevTab = systemTab;

    @FXML
    private void onSystemTabClicked() {
        if (systemTab.isSelected()) {
            prevTab = systemTab;
        }
    }

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
        entityDatabaseButton.setLayoutY(entityDatabaseButton.getLayoutY() + LAYOUT_Y);
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
        deleteAllButtons(firstTabPane, entityButton, entityDatabaseButton);
        entityButton.setLayoutY(entityButton.getLayoutY() + LAYOUT_Y);
        firstTabPane.getChildren().add(hBox);
        firstTabPane.getChildren().add(button);
        counterForY++;
        ENTITIES.add(new Entity(String.valueOf(++mainEntitiesCounter), hBox.getChildren().get(1)));
        entityDatabaseButton.setLayoutY(entityDatabaseButton.getLayoutY() + LAYOUT_Y);
    }

    @FXML
    private void onParametrizationTabClicked() {
        if (ENTITIES.isEmpty()) {
            ENTITIES.add(new Entity("1", firstTabHBox.getChildren().get(1)));
        }
        if (parametrizationTab.isSelected()) {
            if (prevTab.equals(systemTab)) {
                if (secondTabPane.getChildren().size() != 3) {
                    counterForIteration = 0;
                    List<HBox> firstTabHBoxNumber = new ArrayList<>();
                    for (Node node : firstTabPane.getChildren()) {
                        if (node instanceof HBox hBox) {
                            firstTabHBoxNumber.add(hBox);
                        }
                    }
                    List<HBox> secondTabHBoxNumber = new ArrayList<>();
                    for (Node node : secondTabPane.getChildren()) {
                        if (node instanceof HBox hBox) {
                            secondTabHBoxNumber.add(hBox);
                        }
                    }
                    for (int i = 0; i < secondTabHBoxNumber.size(); i++) {
                        List<Node> firstNodes = firstTabHBoxNumber.get(i).getChildren();
                        String firstName = ((TextField) firstNodes.get(firstNodes.size() - 4)).getText();
                        List<Node> secondNodes = secondTabHBoxNumber.get(i).getChildren();
                        TextField secondName = (TextField) secondNodes.get(firstNodes.size() - 4);
                        if (!firstName.equals(secondName.getText())) {
                            secondName.setText(firstName);
                        }
                    }
                    for (int i = secondTabHBoxNumber.size(); i < firstTabHBoxNumber.size(); i++) {
                        boolean flag = false;
                        HBox firstHBox = firstTabHBoxNumber.get(i);
                        HBox hbox = makeCopyOfHBox(firstHBox, parameterField, measurementField, valueField);
                        secondTabPane.getChildren().add(hbox);
                        if (hbox.getChildren().size() == 2) {
                            hbox.setLayoutX(hbox.getLayoutX() - 16.0);
                            flag = true;
                        }
                        int[] position = getHBoxPosition(hbox, secondTabHBox);
                        if (flag) {
                            hbox.setLayoutX(hbox.getLayoutX() + 16.0);
                        }
                        Button button = getButton(position[0], position[1], parameterButton);
                        secondTabPane.getChildren().add(button);
                        parameterDatabaseButton.setLayoutY(parameterDatabaseButton.getLayoutY() + LAYOUT_Y);
                    }
                    return;
                }
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
                        valueField,
                        parameterDatabaseButton);
            }
            prevTab = parametrizationTab;
        }
    }

    @FXML
    private void onEntityDatabaseClicked() {
        OffsetDateTime createdAt = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        for (Node node : firstTabPane.getChildren()) {
            if (node instanceof HBox hBox) {
                List<Node> nodes = hBox.getChildren();
                String attribute = ((TextField) nodes.getLast()).getText();
                String type = ((TextField) nodes.get(nodes.size() - 2)).getText();
                String ontology = ((TextField) nodes.get(nodes.size() - 3)).getText();
                String name = ((TextField) nodes.get(nodes.size() - 4)).getText();
                for (String field : List.of(attribute, type, ontology, name)) {
                    if ((field == null) || (field.isEmpty())) {
                        return;
                    }
                }
                var entities = entityRepository.findByAllFieldsExpectIdAndCreatedAt(name, ontology, type, attribute);
                if (entities.isEmpty()) {
                    int entityId = ThreadLocalRandom.current().nextInt();
                    ENTITIES_ID.put(name, entityId);
                    entityRepository.addEntity(entityId, name, ontology, type, attribute, createdAt);
                } else {
                    ENTITIES_ID.put(name, entities.getFirst().getEntityId());
                }
            }
        }
    }

    @FXML
    private void onParameterDatabaseClicked() {
        OffsetDateTime createdAt = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        for (Node node : secondTabPane.getChildren()) {
            if (node instanceof HBox hBox) {
                List<Node> nodes = hBox.getChildren();
                String entityName = ((TextField) nodes.get(nodes.size() - 2)).getText();
                List<Node> parametersHBox = getParametersHBox(hBox);
                for (Node nodeInPane : parametersHBox) {
                    if (nodeInPane instanceof HBox hBoxInPane) {
                        List<Node> parameterNodes = hBoxInPane.getChildren();
                        Integer entityId = ENTITIES_ID.get(entityName);
                        String parameterName = ((TextField) parameterNodes.getFirst()).getText();
                        String measureUnit = ((TextField) parameterNodes.get(1)).getText();
                        for (String field : List.of(parameterName, measureUnit)) {
                            if ((field == null) || (field.isEmpty())) {
                                return;
                            }
                        }
                        int value = Integer.parseInt(((TextField) parameterNodes.getLast()).getText());
                        if (parameterRepository.findParameterByAllFieldsExceptParameterId(
                                entityId, parameterName, measureUnit, value, 0).isEmpty()) {
                            parameterRepository.addParameter(
                                    entityId, parameterName, measureUnit, value, 0, createdAt);
                        }
                    }
                }
            }
        }
    }

    private List<Node> getParametersHBox(HBox hBox) {
        ScrollPane scrollPaneInsideHBox = (ScrollPane) hBox.getChildren().getLast();
        AnchorPane anchorPaneInsideScrollPane = (AnchorPane) scrollPaneInsideHBox.getContent();
        return anchorPaneInsideScrollPane.getChildren();
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
            if (node instanceof HBox hbox) {
                boolean flag = false;
                if (hbox.getChildren().size() == 2) {
                    hbox.setLayoutX(hbox.getLayoutX() - 16.0);
                    flag = true;
                }
                int[] hBoxPosition = getHBoxPosition(hbox, secondTabHBox);
                if (flag) {
                    hbox.setLayoutX(hbox.getLayoutX() + 16.0);
                }
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
        if (matrixOfConnectionsTab.isSelected()) {
            if (prevTab.equals(parametrizationTab)) {
                if (thirdTabPane.getChildren().size() != 1) {
                    thirdTabPane.getChildren().remove(thirdTabPane.getChildren().getLast());
                }
                int tableSize = ENTITIES.size() + 1;
                GridBase gridBase = new GridBase(tableSize, tableSize);
                ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
                getRows(gridBase, rows);
                gridBase.setRows(rows);
                gridBase.setRowHeightCallback(integer -> 100.0);
                SpreadsheetView spreadsheetView = new SpreadsheetView(gridBase);
                spreadsheetView.setLayoutY(50);
                spreadsheetView.setPrefHeight(tableSize * 400);
                spreadsheetView.setPrefWidth(tableSize * 400);
                spreadsheetView.getColumns().forEach(spreadsheetColumn -> spreadsheetColumn.setPrefWidth(100));
                spreadsheetView.addEventFilter(MouseEvent.MOUSE_CLICKED, new MouseHandler());
                thirdTabPane.getChildren().add(spreadsheetView);
                if (lastTabPane.getChildren().size() != 1) {
                    lastTabPane.getChildren().removeIf((node) -> !(node instanceof Button));
                    resultDatabaseButton.setLayoutY(LAYOUT_X);
                }
                for (Node node : secondTabPane.getChildren()) {
                    if (node instanceof HBox hBox) {
                        HBox newHBox = getHBoxForLastTab(hBox);
                        lastTabPane.getChildren().add(newHBox);
                        resultDatabaseButton.setLayoutY(resultDatabaseButton.getLayoutY() + LAYOUT_Y);
                    }
                }
            }
            prevTab = matrixOfConnectionsTab;
        }
    }

    private HBox getHBoxForLastTab(HBox hBox) {
        List<Node> nodesInHBox = hBox.getChildren();
        HBox newHBox = new HBox();
        changeHBoxForLastTab(newHBox, hBox);
        TextField circle = new TextField();
        TextField name = new TextField();
        changeFieldsForLastTab(nodesInHBox, circle, name);
        ScrollPane scrollPane = new ScrollPane();
        changeScrollPaneForLastTab(nodesInHBox, scrollPane);
        newHBox.getChildren().addAll(circle, name, scrollPane);
        return newHBox;
    }

    private void changeScrollPaneForLastTab(List<Node> nodesInHBox, ScrollPane scrollPane) {
        AnchorPane anchorPane = new AnchorPane();
        ScrollPane scrollInHBox = (ScrollPane) nodesInHBox.getLast();
        AnchorPane anchorInHBox = (AnchorPane) scrollInHBox.getContent();
        scrollPane.setPannable(scrollInHBox.isPannable());
        scrollPane.setPrefWidth(scrollInHBox.getPrefWidth());
        scrollPane.setPrefHeight(scrollInHBox.getPrefHeight());
        changeAnchorPaneForLastTab(anchorPane, anchorInHBox);
        scrollPane.setContent(anchorPane);
    }

    private void changeAnchorPaneForLastTab(AnchorPane anchorPane, AnchorPane anchorInHBox) {
        for (Node node : anchorInHBox.getChildren()) {
            if (node instanceof HBox hBox) {
                HBox newHBox = new HBox();
                List<Node> nodes = hBox.getChildren();
                TextField parameterName = new TextField();
                TextField parameterMeasureUnit = new TextField();
                TextField parameterValue = new TextField();
                changeParameterFields(nodes, parameterName, parameterMeasureUnit, parameterValue);
                changeHBoxForLastTab(newHBox, hBox);
                newHBox.getChildren().addAll(parameterName, parameterMeasureUnit, parameterValue);
                anchorPane.getChildren().add(newHBox);
            }
        }
    }

    private void changeParameterFields(
            List<Node> nodes, TextField parameterName, TextField parameterMeasureUnit, TextField parameterValue) {
        TextField nameInHBox = (TextField) nodes.getFirst();
        TextField measureUnitInHBox = (TextField) nodes.get(1);
        TextField valueInHBox = (TextField) nodes.getLast();
        changeTextFieldForLastTab(parameterName, nameInHBox);
        changeTextFieldForLastTab(parameterMeasureUnit, measureUnitInHBox);
        changeTextFieldForLastTab(parameterValue, valueInHBox);
    }

    private void changeHBoxForLastTab(HBox newHBox, HBox hBox) {
        newHBox.setAlignment(hBox.getAlignment());
        newHBox.setPrefHeight(hBox.getPrefHeight());
        newHBox.setPrefWidth(hBox.getPrefWidth());
        newHBox.setSpacing(hBox.getSpacing());
        newHBox.setLayoutX(hBox.getLayoutX());
        newHBox.setLayoutY(hBox.getLayoutY());
    }

    private void changeFieldsForLastTab(List<Node> nodesInHBox, TextField circle, TextField name) {
        TextField circleInHBox = (TextField) nodesInHBox.getFirst();
        TextField nameInHBox = (TextField) nodesInHBox.get(1);
        changeTextFieldForLastTab(circle, circleInHBox);
        changeTextFieldForLastTab(name, nameInHBox);
        circle.getStyleClass().add(circleInHBox.getStyleClass().getLast());
    }

    private void changeTextFieldForLastTab(TextField result, TextField textField) {
        changeTextField(result, textField);
        result.setText(textField.getText());
    }

    @FXML
    private void onConnectionDatabaseClicked() {
        SpreadsheetView spreadsheetView = (SpreadsheetView) thirdTabPane.getChildren().getLast();
        ObservableList<ObservableList<SpreadsheetCell>> rows = spreadsheetView.getGrid().getRows();
        OffsetDateTime createdAt = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        for (int row = 1; row < rows.size(); row++) {
            for (int column = 1; column < rows.getFirst().size(); column++) {
                TextField verticalField = (TextField) ENTITIES.get(column - 1).node;
                TextField horizontalField = (TextField) ENTITIES.get(row - 1).node;
                String firstEntityName = verticalField.getText();
                String secondEntityName = horizontalField.getText();
                int firstEntityId = ENTITIES_ID.getOrDefault(firstEntityName, 0);
                int secondEntityId = ENTITIES_ID.getOrDefault(secondEntityName, 0);
                Connection connectionByIds = connectionRepository.findConnectionByEntitiesId(firstEntityId, secondEntityId);
                Connection connectionByNames = connectionRepository.findConnectionByEntitiesNames(firstEntityName, secondEntityName);
                String description = rows.get(row).get(column).getText();
                boolean flag = false;
                for (String field : List.of(firstEntityName, secondEntityName, description)) {
                    if ((field == null) || (field.isEmpty())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
                if ((connectionByIds == null) && (connectionByNames == null)) {
                    connectionRepository.addConnection(
                            firstEntityId,
                            secondEntityId,
                            firstEntityName,
                            secondEntityName,
                            description,
                            createdAt);
                }
            }
        }
    }

    @FXML
    private void onMathModelDatabaseClicked() {
        OffsetDateTime createdAt = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        for (Node node : modelPane.getChildren()) {
            if (node instanceof HBox hBox) {
                List<Node> nodes = hBox.getChildren();
                String name = ((TextField) nodes.get(1)).getText();
                String formula = ((TextField) nodes.get(2)).getText();
                String choices = ((TextArea) nodes.getLast()).getText();
                if ((choices == null) || (choices.isEmpty())) {
                    continue;
                }
                String[] choice = choices.split(",");
                TextField verticalField = (TextField) ENTITIES.get(Integer.parseInt(choice[0]) - 1).node;
                TextField horizontalField = (TextField) ENTITIES.get(Integer.parseInt(choice[1]) - 1).node;
                String firstEntityName = verticalField.getText();
                String secondEntityName = horizontalField.getText();
                int firstEntityId = ENTITIES_ID.getOrDefault(firstEntityName, 0);
                int secondEntityId = ENTITIES_ID.getOrDefault(secondEntityName, 0);
                MathModel mathModelByIds = mathModelRepository.findMathModelByEntitiesId(firstEntityId, secondEntityId);
                MathModel mathModelByNames = mathModelRepository.findMathModelByEntitiesNames(firstEntityName, secondEntityName);
                boolean flag = false;
                for (String field : List.of(name, formula, firstEntityName, secondEntityName)) {
                    if ((field == null) || (field.isEmpty())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
                if ((mathModelByIds == null) && (mathModelByNames == null)) {
                    mathModelRepository.addMathModel(
                            firstEntityId,
                            secondEntityId,
                            firstEntityName,
                            secondEntityName,
                            name,
                            formula,
                            createdAt);
                }
            }
        }
    }

    @FXML
    private void onMathModelProcessClicked() {
        for (Node node : modelPane.getChildren()) {
            if (node instanceof HBox hBox) {
                List<Node> nodes = hBox.getChildren();
                String formula = ((TextField) nodes.get(2)).getText();
                String[] choice = ((TextArea) nodes.getLast()).getText().split(",");
                String[] parameters = ((TextArea) nodes.get(nodes.size() - 2)).getText().split(",");
                if ((formula == null) || (formula.isEmpty())) {
                    continue;
                }
                String[] formulaParts = formula.split("=");
                if (formulaParts.length != 2) {
                    continue;
                }
                Expression expression = new Expression(formulaParts[1]);
                processParameters(parameters, expression);
                TextField verticalField = (TextField) ENTITIES.get(Integer.parseInt(choice[0]) - 1).node;
                TextField horizontalField = (TextField) ENTITIES.get(Integer.parseInt(choice[1]) - 1).node;
                String firstEntityName = verticalField.getText();
                String secondEntityName = horizontalField.getText();
                BigDecimal result = expression.eval();
                String[] resultParts = parameters[0].split("\\.");
                TextField valueField = getTextFieldForResult(resultParts);
                if (valueField == null) {
                    continue;
                }
                boolean flag = false;
                for (String field : List.of(firstEntityName, secondEntityName, String.valueOf(result))) {
                    if ((field == null) || (field.isEmpty())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
                valueField.setText(String.valueOf(result));
                MATH_MODELS.put(firstEntityName + " " + secondEntityName, formula);
            }
        }
        counterForIteration++;
    }

    private TextField getTextFieldForResult(String[] resultParts) {
        String nameInParts = ((TextField) ENTITIES.get(Integer.parseInt(resultParts[1]) - 1).node).getText();
        for (Node node : lastTabPane.getChildren()) {
            if (node instanceof HBox hBox) {
                List<Node> nodes = hBox.getChildren();
                String nameInPane = ((TextField) nodes.get(nodes.size() - 2)).getText();
                if (nameInParts.equals(nameInPane)) {
                    List<Node> parametersHBox = getParametersHBox(hBox);
                    for (Node nodeInPane : parametersHBox) {
                        if (nodeInPane instanceof HBox hBoxInPane) {
                            List<Node> parameterNodes = hBoxInPane.getChildren();
                            String parameterName = ((TextField) parameterNodes.getFirst()).getText();
                            if (parameterName.equals(resultParts[0])) {
                                return (TextField) parameterNodes.getLast();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void processParameters(String[] parameters, Expression expression) {
        for (int i = 1; i < parameters.length; i++) {
            String[] parts = parameters[i].split("\\.");
            String nameInParts = ((TextField) ENTITIES.get(Integer.parseInt(parts[1]) - 1).node).getText();
            for (Node node : lastTabPane.getChildren()) {
                if (node instanceof HBox hBox) {
                    List<Node> nodes = hBox.getChildren();
                    String nameInPane = ((TextField) nodes.get(nodes.size() - 2)).getText();
                    if (nameInParts.equals(nameInPane)) {
                        List<Node> parametersHBox = getParametersHBox(hBox);
                        for (Node nodeInPane : parametersHBox) {
                            if (nodeInPane instanceof HBox hBoxInPane) {
                                List<Node> parameterNodes = hBoxInPane.getChildren();
                                String parameterName = ((TextField) parameterNodes.getFirst()).getText();
                                if (parameterName.equals(parts[0])) {
                                    expression.setVariable(parameterName, ((TextField) parameterNodes.getLast()).getText());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void onModelClicked() {
        HBox hBox = getHBoxForModelTab(modelHBox);
        modelCreation.setLayoutY(modelCreation.getLayoutY() + 77);
        modelPane.getChildren().add(hBox);
        counterForModel++;
    }

    @FXML
    private void onResultDatabaseClicked() {
        OffsetDateTime createdAt = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        for (Node node : lastTabPane.getChildren()) {
            if (node instanceof HBox hBox) {
                List<Node> nodes = hBox.getChildren();
                String entityName = ((TextField) nodes.get(nodes.size() - 2)).getText();
                if ((entityName == null || (entityName.isEmpty()))) {
                    continue;
                }
                List<Node> parametersHBox = getParametersHBox(hBox);
                for (Node nodeInPane : parametersHBox) {
                    if (nodeInPane instanceof HBox hBoxInPane) {
                        List<Node> parameterNodes = hBoxInPane.getChildren();
                        int entityId = ENTITIES_ID.get(entityName);
                        String parameterName = ((TextField) parameterNodes.getFirst()).getText();
                        String measureUnit = ((TextField) parameterNodes.get(1)).getText();
                        String stringValue = ((TextField) parameterNodes.getLast()).getText();
                        int value = Integer.parseInt(stringValue);
                        if (parameterRepository.findParameterByAllFieldsExceptParameterId(
                                entityId, parameterName, measureUnit, value, counterForIteration).isEmpty()) {
                            parameterRepository.addParameter(
                                    entityId, parameterName, measureUnit, value, counterForIteration, createdAt);
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void onPropertiesTabClicked() {
        if (propertiesTab.isSelected()) {
            prevTab = propertiesTab;
        }
    }

    @FXML
    private void onModelTabClicked() {
        if (modelTab.isSelected()) {
            prevTab = modelTab;
        }
    }

    @FXML
    private void onResultTabClicked() {
        if (resultTab.isSelected()) {
            prevTab = resultTab;
        }
    }

    private HBox getHBoxForModelTab(HBox modelHBox) {
        HBox hBox = new HBox();
        List<TextField> fields = List.of(modelCircle, modelName, formulaField);
        List<TextArea> areas = List.of(parametersField, connectionChoice);
        for (TextField textField : fields) {
            TextField copy = makeCopyOfTextField(textField, modelCircle, counterForModel);
            hBox.getChildren().add(copy);
        }
        for (TextArea area : areas) {
            TextArea copy = makeCopyOfTextArea(area);
            hBox.getChildren().add(copy);
        }
        hBox.setAlignment(Pos.CENTER);
        hBox.setLayoutX(modelHBox.getLayoutX());
        hBox.setLayoutY(modelHBox.getLayoutY() + ((counterForModel - 1) * 77));
        hBox.setPrefWidth(modelHBox.getPrefWidth());
        hBox.setPrefHeight(modelHBox.getPrefHeight());
        return hBox;
    }

    private TextArea makeCopyOfTextArea(TextArea textArea) {
        TextArea result = new TextArea();
        changeTextArea(result, textArea);
        return result;
    }

    private void changeTextArea(TextArea result, TextArea textArea) {
        result.setPrefHeight(textArea.getPrefHeight());
        result.setPrefWidth(textArea.getPrefWidth());
        result.setPromptText(textArea.getPromptText());
    }

    @SneakyThrows
    private void handleMouseEvent(MouseEvent event, SpreadsheetCell cell, CellView view) {
        if (event.getButton().equals(MouseButton.MIDDLE)) {
            TextField verticalEntity = (TextField) ENTITIES.get(cell.getColumn() - 1).node;
            TextField horizontalEntity = (TextField) ENTITIES.get(cell.getRow() - 1).node;
            this.verticalEntity.setText(verticalEntity.getText());
            this.horizontalEntity.setText(horizontalEntity.getText());
            this.characteristics.setText(view.getText());
            mainTabPane.getSelectionModel().select(3);
            String text = MATH_MODELS.get(verticalEntity.getText() + " " + horizontalEntity.getText());
            formula.setText(Objects.requireNonNullElse(text, "Математическая модель не найдена"));
        }
    }

    private CellView getCorrectView(MouseEvent event) {
        if (event.getTarget() instanceof Text text) {
            if (!(text.getParent() instanceof CellView)) {
                return null;
            }
            return (CellView) text.getParent();
        } else {
            return (CellView) event.getTarget();
        }
    }

    private void getRows(GridBase gridBase, ObservableList<ObservableList<SpreadsheetCell>> rows) {
        for (int row = 0; row < gridBase.getRowCount(); ++row) {
            ObservableList<SpreadsheetCell> currentRow = FXCollections.observableArrayList();
            for (int column = 0; column < gridBase.getColumnCount(); ++column) {
                if ((row == 0) && (column == 0)) {
                    createCell(row, column, currentRow, "", false);
                    continue;
                }
                if ((column != 0) && (row == 0)) {
                    TextField textField = (TextField) ENTITIES.get(column - 1).node;
                    createCell(row, column, currentRow, textField.getText(), false);
                } else if (column == 0) {
                    TextField textField = (TextField) ENTITIES.get(row - 1).node;
                    createCell(row, column, currentRow, textField.getText(), false);
                } else {
                    TextField verticalField = (TextField) ENTITIES.get(column - 1).node;
                    TextField horizontalField = (TextField) ENTITIES.get(row - 1).node;
                    String firstEntityName = verticalField.getText();
                    String secondEntityName = horizontalField.getText();
                    int firstEntityId = ENTITIES_ID.getOrDefault(firstEntityName, 0);
                    int secondEntityId = ENTITIES_ID.getOrDefault(secondEntityName, 0);
                    Connection connectionByIds = connectionRepository.findConnectionByEntitiesId(firstEntityId, secondEntityId);
                    Connection connectionByNames = connectionRepository.findConnectionByEntitiesNames(firstEntityName, secondEntityName);
                    if ((connectionByIds == null) && (connectionByNames == null)) {
                        String description = client.getGigaChatToken().getAccessToken();
                        createCell(row, column, currentRow, description, true);
                    } else if (connectionByIds == null) {
                        createCell(row, column, currentRow, connectionByNames.getDescription(), true);
                    } else if (connectionByNames == null) {
                        createCell(row, column, currentRow, connectionByIds.getDescription(), true);
                    } else {
                        createCell(row, column, currentRow, connectionByIds.getDescription(), true);
                    }
                }
            }
            rows.add(currentRow);
        }
    }

    private static void createCell(
            int row,
            int column,
            ObservableList<SpreadsheetCell> currentRow,
            String text,
            boolean editable) {
        SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, text);
        cell.setEditable(editable);
        currentRow.add(cell);
    }

    private static String constructMessageForDescription(String firstEntity, String secondEntity) {
        String construct = "Задача определить связь между двумя элементами. "
                + "Связь определяется как первый элемент влияет на второй элемент. "
                + "Эти элементы принадлежат одной технической системе или системе управления. "
                + "Описать нужно конкретно без вводных и заключительных фраз. "
                + "Описать нужно с учетом методологии системной инженерии. "
                + "Описание должно полностью учитывать описание и свойства каждого из связываемых элементов. "
                + "По описанию должна быть возможность определить математическую модель их связи."
                + "Первый элемент: %s; Второй элемент: %s.";
        return String.format(construct, firstEntity, secondEntity);
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

    private record Entity(String level, Node node) {
    }

    private class MouseHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (!(event.getTarget() instanceof Text) && !(event.getTarget() instanceof CellView)) {
                return;
            }
            CellView view = getCorrectView(event);
            if (view == null) {
                return;
            }
            SpreadsheetCell cell = view.getItem();
            if ((cell.getColumn() == 0) || (cell.getRow() == 0)) {
                return;
            }
            handleMouseEvent(event, cell, view);
            event.consume();
        }
    }

}