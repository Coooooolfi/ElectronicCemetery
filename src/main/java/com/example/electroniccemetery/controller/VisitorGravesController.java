package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.GraveDao;
import com.example.electroniccemetery.model.Grave;
import com.example.electroniccemetery.model.Plot;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class VisitorGravesController {

    @FXML private Label plotLabel;
    @FXML private VBox graveList;
    @FXML private TextField searchField;

    private final GraveDao graveDao = new GraveDao();

    @FXML
    private void initialize() {
        Plot p = SelectedContext.getPlot();
        if (p != null) {
            plotLabel.setText("Участок " + p.getNumber());
            loadGraves(p.getId());
        }
    }

    private void loadGraves(int plotId) {
        graveList.getChildren().clear();
        List<Grave> graves = graveDao.findByPlotId(plotId);
        for (Grave g : graves) {
            HBox card = new HBox(10);
            card.getStyleClass().add("grave-card");

            ImageView imageView = new ImageView();
            if (g.getPhotoUrl() != null) {
                imageView.setImage(new Image(g.getPhotoUrl(), 80, 80, true, true));
            }

            Text number = new Text("Захоронение №" + g.getNumber());
            Button open = new Button("Просмотреть");
            open.setOnAction(e -> {
                SelectedContext.setGrave(g);
                SceneNavigator.switchTo("visitor_burial.fxml",
                        "Захоронение №" + g.getNumber());
            });

            card.getChildren().addAll(imageView, new VBox(number), open);
            graveList.getChildren().add(card);
        }
    }
    @FXML
    private void onBackClick() {
        SceneNavigator.switchTo("visitor_plots.fxml", "Участки");
    }

    @FXML
    private void onSearchClick() {
        String q = searchField.getText();
        if (q == null || q.isBlank()) return;
        SearchResultsController.setQuery(q);
        SceneNavigator.switchTo("search_results.fxml", "Поиск: " + q);
    }
}
