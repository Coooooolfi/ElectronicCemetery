package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.DeceasedDao;
import com.example.electroniccemetery.model.Deceased;
import com.example.electroniccemetery.model.Grave;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


import java.time.format.DateTimeFormatter;
import java.util.List;

public class SearchResultsController {

    @FXML private VBox resultsBox;

    private static String lastQuery;
    private final DeceasedDao deceasedDao = new DeceasedDao();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");



    public static void setQuery(String q) {
        lastQuery = q;
    }

    @FXML
    private void initialize() {
        if (lastQuery != null && !lastQuery.isBlank()) {
            loadResults(lastQuery);
        }
    }

    private void loadResults(String query) {
        resultsBox.getChildren().clear();
        List<Deceased> list = deceasedDao.searchByFio(query);
        if (list.isEmpty()) {
            resultsBox.getChildren().add(new Text("Ничего не найдено по запросу: " + query));
            return;
        }
        for (Deceased d : list) {
            VBox card = new VBox(3);
            card.getStyleClass().add("deceased-card");
            card.setMaxWidth(Double.MAX_VALUE);
            card.setMinWidth(400);

            Text name = new Text(d.getFullName());
            String dates = "";
            if (d.getBirthDate() != null) dates += d.getBirthDate().format(fmt);
            dates += " - ";
            if (d.getDeathDate() != null) dates += d.getDeathDate().format(fmt);
            Text dateText = new Text(dates);
            Text desc = new Text(d.getDescription() == null ? "" : d.getDescription());

            String path = deceasedDao.getBurialPathByDeceasedId(d.getId());
            Text pathText = new Text(path == null ? "" : path);

            Button openBtn = new Button("Просмотреть захоронение");
            openBtn.setOnAction(e -> openBurial(d));

            card.getChildren().addAll(name, pathText, dateText, desc, openBtn);
            resultsBox.getChildren().add(card);
        }
    }

    private void openBurial(Deceased d) {
        // создание захоронения
        Grave g = new Grave(d.getGraveId(), 0, d.getGraveId(), null);
        SelectedContext.setGrave(g);
        SceneNavigator.switchTo("visitor_burial.fxml", "Захоронение");
    }

    @FXML
    private void onBackClick() {
        SceneNavigator.switchTo("visitor_cemeteries.fxml", "Список кладбищ");
    }
}
