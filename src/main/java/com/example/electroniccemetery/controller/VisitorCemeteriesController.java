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

            VBox info = new VBox(new Text(c.getName()), new Text(c.getAddress()));
            Button open = new Button("Открыть");
            Button showDeceased = new Button("Список захороненных");

            open.setOnAction(e -> openSectors(c));
            showDeceased.setOnAction(e -> showDeceasedList(c));

            card.getChildren().addAll(info, new HBox(10, open, showDeceased));
            cemeteryList.getChildren().add(card);
        }
    }

    // открытие окна списка секторов выбранного кладбища
    private void openSectors(Cemetery cemetery) {
        SelectedContext.setCemetery(cemetery);
        SceneNavigator.switchTo("visitor_sectors.fxml",
                "Секторы: " + cemetery.getName());
    }

    // открытие окна усопших выбранного кладбища
    @FXML
    private void showDeceasedList(Cemetery cemetery) {
        SelectedContext.setCemetery(cemetery);
        SceneNavigator.switchTo("cemetery_deceased_list.fxml",
                "Захороненные на кладбище: " + cemetery.getName());
    }

    // возвращение назад
    @FXML
    private void onHomeClick() {
        SceneNavigator.switchTo("start.fxml",
                "Информационная система учёта захоронений");

    }

    // переход на окно поиска
    @FXML
    private void onSearchClick() {
        String q = searchField.getText();
        if (q == null || q.isBlank()) return;
        SearchResultsController.setQuery(q);
        SceneNavigator.switchTo("search_results.fxml", "Поиск: " + q);
    }
}
