package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.DeceasedDao;
import com.example.electroniccemetery.model.Deceased;
import com.example.electroniccemetery.model.Grave;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class VisitorBurialController {

    @FXML private Label graveLabel;
    @FXML private Label burialPathLabel;
    @FXML private VBox deceasedList;
    @FXML private TextField searchField;

    private final DeceasedDao deceasedDao = new DeceasedDao();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @FXML
    private void initialize() {
        Grave g = SelectedContext.getGrave();
        if (g != null) {
            graveLabel.setText("Захоронение №" + g.getNumber());
            loadDeceased(g.getId());
        }
    }

    private void loadDeceased(int graveId) {
        deceasedList.getChildren().clear();
        List<Deceased> deceased = deceasedDao.findByGraveId(graveId);
        if (!deceased.isEmpty()) {
            String path = new DeceasedDao().getBurialPathByDeceasedId(deceased.get(0).getId());
            burialPathLabel.setText(path == null ? "" : path);
        }
        for (Deceased d : deceased) {
            VBox card = new VBox(5);
            card.getStyleClass().add("deceased-card");
            card.setMaxWidth(Double.MAX_VALUE);
            card.setMinWidth(400);

            ImageView photo = new ImageView();
            if (d.getPhotoUrl() != null) {
                photo.setImage(new Image(d.getPhotoUrl(), 100, 100, true, true));
            }

            Text name = new Text(d.getFullName());
            String dates = "";
            if (d.getBirthDate() != null) dates += d.getBirthDate().format(fmt);
            dates += " - ";
            if (d.getDeathDate() != null) dates += d.getDeathDate().format(fmt);
            Text dateText = new Text(dates);
            Text desc = new Text(d.getDescription() == null ? "" : d.getDescription());

            card.getChildren().addAll(photo, name, dateText, desc);
            deceasedList.getChildren().add(card);
        }
    }
    @FXML
    private void onBackClick() {
        String prev = SelectedContext.getPreviousScene();
        if ("deceased_list".equals(prev)) {
            SceneNavigator.switchTo("cemetery_deceased_list.fxml", "Захороненные на кладбище");
        } else if ("search".equals(prev)) {
            SceneNavigator.switchTo("search_results.fxml", "Результаты поиска");
        } else {
            SceneNavigator.switchTo("visitor_graves.fxml", "Захоронения");
        }
    }
    @FXML
    private void onSearchClick() {
        String q = searchField.getText();
        if (q == null || q.isBlank()) return;
        SearchResultsController.setQuery(q);
        SceneNavigator.switchTo("search_results.fxml", "Поиск: " + q);
    }
}
