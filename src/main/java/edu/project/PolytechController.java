package edu.project;

import edu.project.client.GigaChatClient;
import edu.project.first_tab.Point;
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
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.project.first_tab.FirstTabUtils.*;
import static edu.project.second_tab.SecondTabUtils.*;

@Component
@RequiredArgsConstructor
public class PolytechController {
    private final Map<Point, Integer> BUTTON_CLICKS = new HashMap<>();
    private final Map<Point, Integer> PARAMETER_CLICKS = new HashMap<>();
    private final List<Entity> ENTITIES = new ArrayList<>();
    public static final int LAYOUT_X = 40;
    public static final int LAYOUT_Y = 80;
    public static final double SPACING = 1.0;
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

    private final GigaChatClient client;

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
        if (!thirdTabPane.getChildren().isEmpty()) {
            return;
        }
        int tableSize = ENTITIES.size() + 1;
        GridBase gridBase = new GridBase(tableSize, tableSize);
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        getRows(gridBase, rows);
        gridBase.setRows(rows);
        gridBase.setRowHeightCallback(integer -> 100.0);
        SpreadsheetView spreadsheetView = new SpreadsheetView(gridBase);
        spreadsheetView.setPrefHeight(tableSize * 400);
        spreadsheetView.setPrefWidth(tableSize * 400);
        spreadsheetView.getColumns().forEach(spreadsheetColumn -> spreadsheetColumn.setPrefWidth(100));
        spreadsheetView.addEventFilter(MouseEvent.MOUSE_CLICKED, new MouseHandler());
        thirdTabPane.getChildren().add(spreadsheetView);
    }

    private void handleMouseEvent(MouseEvent event, SpreadsheetCell cell, CellView view) {
        if (event.getButton().equals(MouseButton.SECONDARY)) {
            TextField verticalEntity = (TextField) ENTITIES.get(cell.getColumn() - 1).node;
            TextField horizontalEntity = (TextField) ENTITIES.get(cell.getRow() - 1).node;
            mainTabPane.getSelectionModel().select(3);
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
                    createCell(row, column, currentRow, client.getGigaChatToken().getAccessToken(), true);
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
        String construct = "Задача - описать связь между компонентами газораспределительной "
                + "станции в контексте системной инженерии. "
                + "Описать нужно конкретно без вводных и заключительных фраз. "
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