package simulation.controller;

import java.util.Arrays;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import simulation.model.Grid;
import simulation.model.State;

public class RightPanelController {

    @FXML
    private TextField rowsField;

    @FXML
    private TextField colsField;

    @FXML
    private RadioButton exactNumbersRadio;

    @FXML
    private RadioButton percentageRadio;

    @FXML
    private RadioButton centralPersonRadio;

    @FXML
    private TextField interestedNumberField;

    @FXML
    private TextField gymbroNumberField;

    @FXML
    private TextField competitiveNumberField;

    @FXML
    private TextField abandonedNumberField;

    @FXML
    private TextField interestedPercentField;

    @FXML
    private TextField gymbroPercentField;

    @FXML
    private TextField competitivePercentField;

    @FXML
    private TextField abandonedPercentField;

    @FXML
    private ComboBox<State> centralPersonStateCombo;

    private Grid grid;
    private Timeline timeline;
    private GridController gridController;

    private ChartController chartController;

    @FXML
    public void initialize() {
        // Initialize the timeline for automatic animation
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> step()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        centralPersonStateCombo.setItems(FXCollections.observableList(Arrays.asList(State.values())));

        // Set up listeners for radio buttons
        exactNumbersRadio.setOnAction(_ -> {
            setExactNumbersFieldsEnabled(true);
            setPercentageFieldsEnabled(false);
            centralPersonStateCombo.setDisable(true);
        });

        percentageRadio.setOnAction(_ -> {
            setExactNumbersFieldsEnabled(false);
            setPercentageFieldsEnabled(true);
            centralPersonStateCombo.setDisable(true);
        });

        centralPersonRadio.setOnAction(_ -> {
            setExactNumbersFieldsEnabled(false);
            setPercentageFieldsEnabled(false);
            centralPersonStateCombo.setDisable(false);
        });
    }

    private void setExactNumbersFieldsEnabled(boolean enabled) {
        interestedNumberField.setDisable(!enabled);
        gymbroNumberField.setDisable(!enabled);
        competitiveNumberField.setDisable(!enabled);
        abandonedNumberField.setDisable(!enabled);
    }

    private void setPercentageFieldsEnabled(boolean enabled) {
        interestedPercentField.setDisable(!enabled);
        gymbroPercentField.setDisable(!enabled);
        competitivePercentField.setDisable(!enabled);
        abandonedPercentField.setDisable(!enabled);
    }

    public void setGridController(GridController gridController) {
        this.gridController = gridController;
    }

    public void setChartController(ChartController chartController) {
        this.chartController = chartController;
    }

    @FXML
    private void handleStepAction() {
        step();
    }

    @FXML
    private void handleStartAutoAction() {
        timeline.play();
    }

    @FXML
    private void handleStopAutoAction() {
        timeline.stop();
    }

    private void step() {
        // Update the grid model
        gridController.updateGrid();
        updateChart();
    }

    @FXML
    private void handleInitializeGrid() {
        gridController.initialize();
        int rows = 10;
        int cols = 10;
        try {
            rows = Integer.parseInt(rowsField.getText());
            cols = Integer.parseInt(colsField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid grid size. Using default 10x10.");
        }

        // Create and initialize the grid model
        grid = new Grid(rows, cols);

        // Initialize the grid based on the selected distribution type
        if (exactNumbersRadio.isSelected()) {
            try {
                int numInterested = Integer.parseInt(interestedNumberField.getText());
                int numGymBro = Integer.parseInt(gymbroNumberField.getText());
                int numCompetitive = Integer.parseInt(competitiveNumberField.getText());
                int numAbandoned = Integer.parseInt(abandonedNumberField.getText());
                grid.initialize(numInterested, numGymBro, numCompetitive, numAbandoned);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number input. Using default center person state.");
                grid.initialize(State.INTERESTED);
            }
        } else if (percentageRadio.isSelected()) {
            try {
                double percentInterested = Double.parseDouble(interestedPercentField.getText().replace("%", ""));
                double percentGymBro = Double.parseDouble(gymbroPercentField.getText().replace("%", ""));
                double percentCompetitive = Double.parseDouble(competitivePercentField.getText().replace("%", ""));
                double percentAbandoned = Double.parseDouble(abandonedPercentField.getText().replace("%", ""));
                grid.initialize(percentInterested, percentGymBro, percentCompetitive, percentAbandoned);
            } catch (NumberFormatException e) {
                System.out.println("Invalid percentage input. Using default center person state.");
                grid.initialize(State.INTERESTED);
            }
        } else if (centralPersonRadio.isSelected()) {
            State centralState = centralPersonStateCombo.getValue();
            if (centralState != null) {
                grid.initialize(centralState);
            } else {
                System.out.println("No central state selected. Using default UNINTERESED.");
                grid.initialize(State.INTERESTED);
            }
        } else {
            System.out.println("Invalid distribution type. Using default center person state.");
            grid.initialize(State.UNINTERESED);
        }

        // Set the grid in the GridController
        initializeChart();
        gridController.setGrid(grid);
    }

    private void initializeChart() {
        chartController.clearChart();
        chartController.updateChart(grid.getNumInterested(), grid.getNumGymBro(), grid.getNumCompetitive(),
                grid.getNumAbandoned(), grid.getNumUninterested());
    }

    private void updateChart() {
        chartController.updateChart(grid.getNumInterested(), grid.getNumGymBro(), grid.getNumCompetitive(),
                grid.getNumAbandoned(), grid.getNumUninterested());
    }
}
