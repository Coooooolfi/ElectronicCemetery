package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.CemeteryDao;
import com.example.electroniccemetery.model.Cemetery;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class VisitorCemeteriesController {

    @FXML private VBox cemeteryList;
    @FXML private TextField searchField;

    private final CemeteryDao cemeteryDao = new CemeteryDao();

    @FXML
    private void initialize() {
        loadCemeteries();
    }

    private void loadCemeteries() {
        cemeteryList.getChildren().clear();
        List<Cemetery> cemeteries = cemeteryDao.findAll();
        for (Cemetery c : cemeteries) {
            HBox card = new HBox(10);
            card.getStyleClass().add("cemetery-card");

            Text name = new Text(c.getName());
            Text addr = new Text(c.getAddress());
            Button open = new Button("Открыть");
            open.setOnAction(e -> openSectors(c));

            card.getChildren().addAll(new VBox(name, addr), open);
            cemeteryList.getChildren().add(card);
        }
    }

    private void openSectors(Cemetery cemetery) {
        SelectedContext.setCemetery(cemetery);
        SceneNavigator.switchTo("visitor_sectors.fxml",
                "Секторы: " + cemetery.getName());
    }
    @FXML
    private void onHomeClick() {
        SceneNavigator.switchTo("start.fxml",
                "Информационная система учёта захоронений");

    }

    @FXML
    private void onSearchClick() {
        String q = searchField.getText();
        if (q == null || q.isBlank()) return;
        SearchResultsController.setQuery(q);
        SceneNavigator.switchTo("search_results.fxml", "Поиск: " + q);
    }
}
