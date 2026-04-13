package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.SectionDao;
import com.example.electroniccemetery.model.Cemetery;
import com.example.electroniccemetery.model.Section;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class VisitorSectorsController {

    @FXML private Label cemeteryLabel;
    @FXML private VBox sectorTiles;
    @FXML private TextField searchField;

    private final SectionDao sectionDao = new SectionDao();

    @FXML
    private void initialize() {
        Cemetery c = SelectedContext.getCemetery();
        if (c != null) {
            cemeteryLabel.setText(c.getName());
            loadSectors(c.getId());
        }
    }

    private void loadSectors(int cemeteryId) {
        sectorTiles.getChildren().clear();
        List<Section> sections = sectionDao.findByCemeteryId(cemeteryId);
        for (Section s : sections) {
            Button btn = new Button("Сектор " + s.getNumber());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> {
                SelectedContext.setSection(s);
                SceneNavigator.switchTo("visitor_plots.fxml",
                        "Участки: сектор " + s.getNumber());
            });
            sectorTiles.getChildren().add(btn);
        }
    }
    @FXML
    private void onBackClick() {
        SceneNavigator.switchTo("visitor_cemeteries.fxml",
                "Список кладбищ");
    }
    @FXML

    private void onSearchClick() {
        String q = searchField.getText();
        if (q == null || q.isBlank()) return;
        SearchResultsController.setQuery(q);
        SceneNavigator.switchTo("search_results.fxml", "Поиск: " + q);
    }
}
