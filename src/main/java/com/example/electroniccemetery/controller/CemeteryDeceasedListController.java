package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.DeceasedDao;
import com.example.electroniccemetery.dao.GraveDao;
import com.example.electroniccemetery.dao.PlotDao;
import com.example.electroniccemetery.dao.SectionDao;
import com.example.electroniccemetery.model.*;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


import java.time.format.DateTimeFormatter;
import java.util.List;

public class CemeteryDeceasedListController {

    @FXML private Label cemeteryNameLabel;
    @FXML private VBox deceasedContainer;

    private final DeceasedDao deceasedDao = new DeceasedDao();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @FXML
    private void initialize() {
        Cemetery cemetery = SelectedContext.getCemetery();
        if (cemetery != null) {
            cemeteryNameLabel.setText(cemetery.getName());
            loadDeceasedList(cemetery.getId());
        }
    }

    private void loadDeceasedList(int cemeteryId) {
        deceasedContainer.getChildren().clear();
        List<Deceased> list = deceasedDao.findByCemeteryId(cemeteryId);
        if (list.isEmpty()) {
            deceasedContainer.getChildren().add(new Text("На данном кладбище нет захороненных."));
            return;
        }
        for (Deceased d : list) {
            VBox card = new VBox(5);
            card.getStyleClass().add("deceased-card");
            card.setMaxWidth(Double.MAX_VALUE);

            Text name = new Text(d.getFullName());
            String dates = "";
            if (d.getBirthDate() != null) dates += d.getBirthDate().format(fmt);
            dates += " - ";
            if (d.getDeathDate() != null) dates += d.getDeathDate().format(fmt);
            Text dateText = new Text(dates);

            String path = deceasedDao.getBurialPathByDeceasedId(d.getId());
            Text pathText = new Text(path == null ? "" : "Место: " + path);

            Button openBtn = new Button("Открыть захоронение");
            openBtn.setOnAction(e -> {
                // получаем захоронение по ID
                int graveId = d.getGraveId();
                GraveDao graveDao = new GraveDao();
                Grave grave = graveDao.findById(graveId);
                if (grave != null) {
                    PlotDao plotDao = new PlotDao();
                    Plot plot = plotDao.findPlotById(grave.getPlotId());
                    SelectedContext.setPlot(plot);

                    SectionDao sectionDao = new SectionDao();
                    Section section = sectionDao.findById(plot.getSectionId());
                    SelectedContext.setSection(section);
                }
                SelectedContext.setGrave(new Grave(graveId, 0, graveId, null));
                SceneNavigator.switchTo("visitor_burial.fxml", "Захоронение");
            });

            card.getChildren().addAll(name, dateText, pathText, openBtn);
            deceasedContainer.getChildren().add(card);
        }
    }

    @FXML
    private void onBackClick() {
        SceneNavigator.switchTo("visitor_cemeteries.fxml", "Список кладбищ");
    }
}