package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.CemeteryDao;
import com.example.electroniccemetery.model.Cemetery;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;

public class AdminController {

    @FXML private Label infoLabel;
    @FXML private Button manageSectionsBtn;
    @FXML private Button managePlotsBtn;
    @FXML private Button manageGravesBtn;
    @FXML private Button manageDeceasedBtn;
    @FXML private Button logoutBtn;

    @FXML
    private void initialize() {
        refreshUI();
        fixWindowSize();
    }

    public void refreshUI() {
        Integer cemeteryId = SelectedContext.getCurrentUserCemeteryId();
        String role = SelectedContext.getCurrentUserRole();

        System.out.println("AdminController refreshUI: role=" + role + ", cemeteryId=" + cemeteryId);

        if ("Супер-администратор".equals(role)) {
            infoLabel.setText("Супер-администратор. Доступны все функции.");
            manageGravesBtn.setDisable(false);
            manageDeceasedBtn.setDisable(false);
        } else if (cemeteryId != null && cemeteryId > 0) {
            String cemeteryName = getCemeteryNameById(cemeteryId);
            infoLabel.setText("Вы управляете кладбищем: " + cemeteryName);
            manageGravesBtn.setDisable(true);
            manageDeceasedBtn.setDisable(true);
        } else {
            infoLabel.setText("Ошибка: не удалось определить права доступа.");
        }

        // Кнопки секторов и участков всегда активны
        manageSectionsBtn.setDisable(false);
        managePlotsBtn.setDisable(false);
    }

    private String getCemeteryNameById(int cemeteryId) {
        CemeteryDao dao = new CemeteryDao();
        List<Cemetery> cemeteries = dao.findAll();
        for (Cemetery c : cemeteries) {
            if (c.getId() == cemeteryId) return c.getName();
        }
        return "Неизвестно";
    }

    @FXML
    private void onLogoutClick() {
        SelectedContext.clear();
        fixWindowSize();
        SceneNavigator.switchTo("start.fxml", "Информационная система учёта захоронений");
    }

    @FXML
    private void onSectionsClick() {
        fixWindowSize();
        SceneNavigator.switchTo("admin_sections.fxml", "Администрирование секторов");
    }

    @FXML
    private void onPlotsClick() {
        fixWindowSize();
        SceneNavigator.switchTo("admin_plots.fxml", "Администрирование участков");
    }

    @FXML
    private void onGravesClick() {
        fixWindowSize();
        SceneNavigator.switchTo("admin_graves.fxml", "Администрирование захоронений");
    }

    @FXML
    private void onDeceasedClick() {
        fixWindowSize();
        SceneNavigator.switchTo("admin_deceased.fxml", "Администрирование усопших");
    }

    private void fixWindowSize() {
        if (logoutBtn != null && logoutBtn.getScene() != null && logoutBtn.getScene().getWindow() != null) {
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setWidth(650);
            stage.setHeight(700);
            stage.setMinWidth(650);
            stage.setMinHeight(700);
        }
    }
}