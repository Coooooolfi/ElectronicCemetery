package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.PlotDao;
import com.example.electroniccemetery.model.Plot;
import com.example.electroniccemetery.model.Section;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class VisitorPlotsController {

    @FXML private Label sectorLabel;
    @FXML private VBox plotList;
    @FXML private TextField searchField;

    private final PlotDao plotDao = new PlotDao();

    @FXML
    private void initialize() {
        Section s = SelectedContext.getSection();
        if (s != null) {
            sectorLabel.setText("Сектор " + s.getNumber());
            loadPlots(s.getId());
        }
    }

    private void loadPlots(int sectionId) {
        plotList.getChildren().clear();
        List<Plot> plots = plotDao.findBySectionId(sectionId);
        for (Plot p : plots) {
            Button btn = new Button("Участок " + p.getNumber());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> {
                SelectedContext.setPlot(p);
                SceneNavigator.switchTo("visitor_graves.fxml",
                        "Захоронения: участок " + p.getNumber());
            });
            plotList.getChildren().add(btn);
        }
    }
    @FXML
    private void onBackClick() {
        SceneNavigator.switchTo("visitor_sectors.fxml", "Секторы");
    }

    @FXML
    private void onSearchClick() {
        String q = searchField.getText();
        if (q == null || q.isBlank()) return;
        SearchResultsController.setQuery(q);
        SceneNavigator.switchTo("search_results.fxml", "Поиск: " + q);
    }
}
