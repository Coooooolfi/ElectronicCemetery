package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.DeceasedDao;
import com.example.electroniccemetery.dao.RelativeDao;
import com.example.electroniccemetery.dao.UserDao;
import com.example.electroniccemetery.model.Deceased;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class UserProfileController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<Deceased> relativesTable;
    @FXML private TableColumn<Deceased, String> colLastName;
    @FXML private TableColumn<Deceased, String> colFirstName;
    @FXML private TableColumn<Deceased, String> colOthestvo;
    @FXML private TableColumn<Deceased, LocalDate> colBirth;
    @FXML private TableColumn<Deceased, LocalDate> colDeath;
    @FXML private Label statusLabel;

    private final UserDao userDao = new UserDao();
    private final RelativeDao relativeDao = new RelativeDao();
    private final DeceasedDao deceasedDao = new DeceasedDao();
    private final ObservableList<Deceased> relatives = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        if (!SelectedContext.isUserLoggedIn()) {
            SceneNavigator.switchTo("user_login.fxml", "Вход пользователя");
            return;
        }

        String fullName = userDao.getUserFullName(SelectedContext.getCurrentUserId());
        welcomeLabel.setText("Добро пожаловать, " + fullName + "!");

        setupTable();
        loadRelatives();
    }

    private void setupTable() {
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colOthestvo.setCellValueFactory(new PropertyValueFactory<>("othestvo"));
        colBirth.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        colDeath.setCellValueFactory(new PropertyValueFactory<>("deathDate"));
        relativesTable.setItems(relatives);
    }

    private void loadRelatives() {
        relatives.clear();
        List<Deceased> list = relativeDao.getRelativesByUserId(SelectedContext.getCurrentUserId());
        relatives.setAll(list);
    }

    @FXML
    private void onRemoveClick() {
        Deceased selected = relativesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите родственника для удаления");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение");
        confirm.setHeaderText("Удалить из профиля?");
        confirm.setContentText(selected.getFullName());

        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (relativeDao.removeRelative(SelectedContext.getCurrentUserId(), selected.getId())) {
                statusLabel.setText("Удалено из профиля");
                loadRelatives();
            } else {
                statusLabel.setText("Ошибка удаления");
            }
        }
    }

    @FXML
    private void onOpenBurialClick() {
        Deceased selected = relativesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите родственника");
            return;
        }
        // Переход к странице захоронения
        com.example.electroniccemetery.model.Grave g =
                new com.example.electroniccemetery.model.Grave(selected.getGraveId(), 0, selected.getGraveId(), null);
        SelectedContext.setGrave(g);
        SelectedContext.setPreviousScene("profile");
        SceneNavigator.switchTo("visitor_burial.fxml", "Захоронение");
    }

    @FXML
    private void onFindRelativesClick() {
        SceneNavigator.switchTo("visitor_cemeteries.fxml", "Список кладбищ");
    }

    @FXML
    private void onLogoutClick() {
        SelectedContext.clear();
        SceneNavigator.switchTo("start.fxml", "Информационная система учёта захоронений");
    }
}