package simulation.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import simulation.model.Grid;
import simulation.view.GymPersonView;

public class GridController {

    @FXML
    private GridPane gridPane;

    private Grid grid;
    private GymPersonView[][] gridView;

    @FXML
    public void initialize() {
        // Crear e inicializar el modelo de la cuadrícula
        grid = null;
        initializeAll();
    }

    private void initializeAll() {
        if (grid != null) {
            // Inicializar la vista de la cuadrícula
            initializeGrid();
        } else {
            gridPane.getChildren().clear();
        }
    }

    private void initializeGrid() {
        gridView = new GymPersonView[grid.getRows()][grid.getCols()];
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                GymPersonView gymPersonCellView = new GymPersonView(20, 20, grid.getState(row, col));
                gridView[row][col] = gymPersonCellView;
                gridPane.add(gymPersonCellView, col, row);
            }
        }
        updateGrid();
    }

    public void updateGrid() {
        // Actualizar el modelo de la cuadrícula
        grid.updateGrid();

        // Actualizar la vista de la cuadrícula
        updateGridView();
    }

    private void updateGridView() {
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                GymPersonView gymPersonCellView = gridView[row][col];
                gymPersonCellView.updateColor(grid.getState(row, col));
            }
        }
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
        initializeAll();
    }

    public Grid getGrid() {
        return grid;
    }
}