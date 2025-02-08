package simulation.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;


public class ChartController {

    @FXML
    private LineChart<Number, Number> scatterChart;

    private XYChart.Series<Number, Number> uninterestedSeries;
    private XYChart.Series<Number, Number> interestedSeries;
    private XYChart.Series<Number, Number> gymBroSeries;
    private XYChart.Series<Number, Number> competitiveSeries;
    private XYChart.Series<Number, Number> abandonedSeries;
    private int timeStep = 0;

    @FXML
    public void initialize() {
        // Inicializar las series del gráfico
        uninterestedSeries = new XYChart.Series<>();
        uninterestedSeries.setName("Uninterested");

        interestedSeries = new XYChart.Series<>();
        interestedSeries.setName("Interested");

        gymBroSeries = new XYChart.Series<>();
        gymBroSeries.setName("GymBro");

        competitiveSeries = new XYChart.Series<>();
        competitiveSeries.setName("Competitive");

        abandonedSeries = new XYChart.Series<>();
        abandonedSeries.setName("Abandoned");

        // Añadir las series al gráfico
        scatterChart.getData().addAll(uninterestedSeries, interestedSeries, gymBroSeries, competitiveSeries, abandonedSeries);

    }

    public void updateChart(int numInterested, int numGymBro, int numCompetitive, int numAbandoned,
            int numUninterested) {
        XYChart.Data<Number, Number> uninterestedData = new XYChart.Data<>(timeStep, numUninterested);
        XYChart.Data<Number, Number> interestedData = new XYChart.Data<>(timeStep, numInterested);
        XYChart.Data<Number, Number> gymBroData = new XYChart.Data<>(timeStep, numGymBro);
        XYChart.Data<Number, Number> competitiveData = new XYChart.Data<>(timeStep, numCompetitive);
        XYChart.Data<Number, Number> abandonedData = new XYChart.Data<>(timeStep, numAbandoned);

        uninterestedSeries.getData().add(uninterestedData);
        interestedSeries.getData().add(interestedData);
        gymBroSeries.getData().add(gymBroData);
        competitiveSeries.getData().add(competitiveData);
        abandonedSeries.getData().add(abandonedData);

        timeStep++;
    }

    public void clearChart() {
        uninterestedSeries.getData().clear();
        interestedSeries.getData().clear();
        gymBroSeries.getData().clear();
        competitiveSeries.getData().clear();
        abandonedSeries.getData().clear();
        timeStep = 0;
    }
}