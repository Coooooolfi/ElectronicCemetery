package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SuperAdminController {

    @FXML private Label statusLabel;
    @FXML private Button logoutBtn;

    @FXML
    private void initialize() {
        String role = SelectedContext.getCurrentUserRole();
        if (!"Супер-администратор".equals(role)) {
            statusLabel.setText("Доступ запрещён. Вы не супер-администратор.");
            return;
        }
        statusLabel.setText("Добро пожаловать, супер-администратор!");
        fixWindowSize();
    }

    @FXML
    private void onManageAdminsClick() {
        SceneNavigator.switchTo("manage_admins.fxml", "Управление администраторами");
    }

    @FXML
    private void onManageCemeteriesClick() {
        SceneNavigator.switchTo("admin_cemeteries.fxml", "Управление кладбищами");
    }

    @FXML
    private void onManageSectionsClick() {
        SceneNavigator.switchTo("admin_sections.fxml", "Управление секторами");
    }

    @FXML
    private void onManagePlotsClick() {
        SceneNavigator.switchTo("admin_plots.fxml", "Управление участками");
    }

    @FXML
    private void onLogoutClick() {
        SelectedContext.clear();
        SceneNavigator.switchTo("start.fxml", "Информационная система учёта захоронений");
    }

    private void fixWindowSize() {
        if (logoutBtn != null && logoutBtn.getScene() != null && logoutBtn.getScene().getWindow() != null) {
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setWidth(800);
            stage.setHeight(700);
            stage.setMinWidth(800);
            stage.setMinHeight(700);
        }
    }
}