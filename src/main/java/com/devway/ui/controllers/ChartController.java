package com.devway.ui.controllers;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

import java.util.Map;

public class ChartController {

    // Defining config
    CategoryAxis xAxis = new CategoryAxis();
    CategoryAxis yAxis = new CategoryAxis();
    BarChart barChart;

    ChartController(ObservableList<String> xCategories, ObservableList<String> yCategories) {
        this.xAxis.setCategories(xCategories);
        this.yAxis.setCategories(yCategories);

        // Setting labels
        this.xAxis.setLabel("Axe des abscisses");
        this.yAxis.setLabel("Axe des ordonnées");
    }

    // Methods
    public BarChart<String, String> getBarChart() {
        barChart = new BarChart<>(this.xAxis, this.yAxis);
        return barChart;
    }

    public void setAxisLabel(String axis, String label) {
        switch (axis) {
            case "x":
                this.xAxis.setLabel(label);
                break;

            case "y":
                this.yAxis.setLabel(label);
                break;
        }
    }

    public <X,Y> void setSerie(String title, Map<X,Y> data) {
        XYChart.Series<X,Y> series = new XYChart.Series<>();

        if (title != null) {
            series.setName(title);
        }

        for (X key : data.keySet()) {
            series.getData().add(new XYChart.Data<>(key, data.get(key)));
        }

        barChart.getData().add(series);
    }
}
