package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.CemeteryDao;
import com.example.electroniccemetery.dao.SectionDao;
import com.example.electroniccemetery.model.Cemetery;
import com.example.electroniccemetery.util.SceneNavigator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class AdminCemeteriesController {

    @FXML private TableView<Cemetery> cemeteriesTable;
    @FXML private TableColumn<Cemetery, Number> colId;
    @FXML private TableColumn<Cemetery, String> colName;
    @FXML private TableColumn<Cemetery, String> colAddress;

    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField sectionsCountField;
    @FXML private Label statusLabel;
    @FXML private Button backBtn;
    @FXML private Button saveBtn;
    @FXML private Button deleteBtn;

    private final CemeteryDao cemeteryDao = new CemeteryDao();
    private final SectionDao sectionDao = new SectionDao();
    private final ObservableList<Cemetery> cemeteries = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTable();
        loadCemeteries();
        fixWindowSize();
    }

    private void setupTable() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        cemeteriesTable.setItems(cemeteries);

        cemeteriesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                nameField.setText(selected.getName());
                addressField.setText(selected.getAddress());
            }
        });
    }

    private void loadCemeteries() {
        cemeteries.clear();
        cemeteries.setAll(cemeteryDao.findAll());
    }

    @FXML
    private void onSaveClick() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String sectionsCountText = sectionsCountField.getText().trim();

        if (name.isBlank() || address.isBlank()) {
            statusLabel.setText("Заполните название и адрес");
            return;
        }

        int sectionsCount = 0;

        if (!sectionsCountText.isBlank()) {
            try {
                sectionsCount = Integer.parseInt(sectionsCountText);
                if (sectionsCount < 1) {
                    statusLabel.setText("Количество секторов должно быть больше 0");
                    return;
                }
            } catch (NumberFormatException e) {
                statusLabel.setText("Количество секторов должно быть числом");
                return;
            }
        }

        // Создание кладбища
        int cemeteryId = cemeteryDao.insertCemetery(name, address);
        if (cemeteryId > 0) {
            // + сектора, если указано количество
            if (sectionsCount > 0) {
                for (int s = 1; s <= sectionsCount; s++) {
                    sectionDao.insertSection(cemeteryId, s);
                }
            }
            statusLabel.setText("Кладбище добавлено, ID = " + cemeteryId);
            clearForm();
            loadCemeteries();
        } else {
            statusLabel.setText("Ошибка при добавлении");
        }
    }

    @FXML
    private void onDeleteClick() {
        Cemetery selected = cemeteriesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите кладбище для удаления");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение");
        confirm.setHeaderText("Удаление кладбища");
        confirm.setContentText("Удалить " + selected.getName() + "? Все секторы, участки и захоронения будут удалены.");

        if (cemeteryDao.deleteCascadeCemetery(selected.getId())) {
            statusLabel.setText("Кладбище удалено");
            clearForm();
            loadCemeteries();
        } else {
            statusLabel.setText("Ошибка при удалении");
        }
    }

    @FXML
    private void onBackClick() {
        fixWindowSize();
        SceneNavigator.switchTo("super_admin.fxml", "Панель супер-администратора");
    }

    private void clearForm() {
        nameField.clear();
        addressField.clear();
        sectionsCountField.clear();
        cemeteriesTable.getSelectionModel().clearSelection();
    }

    private void fixWindowSize() {
        if (backBtn != null && backBtn.getScene() != null && backBtn.getScene().getWindow() != null) {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setWidth(700);
            stage.setHeight(650);
            stage.setMinWidth(700);
            stage.setMinHeight(650);
        }
    }
}