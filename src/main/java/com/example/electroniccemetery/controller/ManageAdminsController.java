package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.UserDao;
import com.example.electroniccemetery.model.Cemetery;
import com.example.electroniccemetery.model.User;
import com.example.electroniccemetery.util.SceneNavigator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class ManageAdminsController {

    @FXML private TableView<User> adminsTable;
    @FXML private TableColumn<User, Number> colId;
    @FXML private TableColumn<User, String> colLastName;
    @FXML private TableColumn<User, String> colFirstName;
    @FXML private TableColumn<User, String> colLogin;
    @FXML private TableColumn<User, String> colCemetery;

    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField othestvoField;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<Cemetery> cemeteryCombo;
    @FXML private Label statusLabel;
    @FXML private Button backBtn;

    private final UserDao userDao = new UserDao();
    private final ObservableList<User> admins = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTable();
        loadAdmins();
        loadCemeteries();
        fixWindowSize();
    }

    private void setupTable() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        colLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        colFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        colLogin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLogin()));
        colCemetery.setCellValueFactory(cellData -> {
            Integer cemeteryId = cellData.getValue().getCemeteryId();
            return new SimpleStringProperty(getCemeteryNameById(cemeteryId));
        });
        adminsTable.setItems(admins);
    }

    private String getCemeteryNameById(Integer cemeteryId) {
        if (cemeteryId == null) return "Не назначено";
        List<Cemetery> cemeteries = userDao.findAllCemeteries();
        for (Cemetery c : cemeteries) {
            if (c.getId() == cemeteryId) return c.getName();
        }
        return "Неизвестно";
    }

    private void loadAdmins() {
        admins.clear();
        admins.setAll(userDao.findAllAdmins());
    }

    private void loadCemeteries() {
        List<Cemetery> list = userDao.findAllCemeteries();
        cemeteryCombo.setItems(FXCollections.observableArrayList(list));

        cemeteryCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Cemetery item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        cemeteryCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Cemetery item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
    }

    @FXML
    private void onCreateAdminClick() {
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String othestvo = othestvoField.getText().trim();
        String login = loginField.getText().trim();
        String password = passwordField.getText();
        Cemetery selected = cemeteryCombo.getValue();

        resetFieldStyles();
        boolean hasError = false;

        if (lastName.isBlank()) {
            highlightError(lastNameField);
            hasError = true;
        }
        if (firstName.isBlank()) {
            highlightError(firstNameField);
            hasError = true;
        }
        if (login.isBlank()) {
            highlightError(loginField);
            hasError = true;
        }
        if (password.isBlank()) {
            highlightError(passwordField);
            hasError = true;
        }
        if (selected == null) {
            highlightError(cemeteryCombo);
            hasError = true;
        }

        if (hasError) {
            statusLabel.setText("Заполните все обязательные поля");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        int id = userDao.createAdmin(lastName, firstName, othestvo, login, password, selected.getId());
        if (id > 0) {
            statusLabel.setText("Администратор создан, ID = " + id);
            statusLabel.setStyle("-fx-text-fill: green;");
            clearForm();
            loadAdmins();
        } else {
            statusLabel.setText("Ошибка: логин уже существует");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void onDeleteAdminClick() {
        User selected = adminsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите администратора");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение");
        confirm.setHeaderText("Удаление администратора");
        confirm.setContentText("Удалить " + selected.getFullName() + "?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (userDao.deleteAdmin(selected.getId())) {
                statusLabel.setText("Администратор удалён");
                loadAdmins();
            } else {
                statusLabel.setText("Ошибка при удалении");
            }
        }
    }

    @FXML
    private void onAssignCemeteryClick() {
        User selected = adminsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите администратора");
            return;
        }
        // список кладбищ
        List<Cemetery> allCemeteries = userDao.findAllCemeteries();
        if (allCemeteries.isEmpty()) {
            statusLabel.setText("Нет доступных кладбищ");
            return;
        }

        // диалоговое окошко
        Dialog<Cemetery> dialog = new Dialog<>();
        dialog.setTitle("Назначение на кладбище");
        dialog.setHeaderText("Выберите новое кладбище для администратора " + selected.getFullName());

        // комбобокс для выбора кладбища для назначения
        ComboBox<Cemetery> combo = new ComboBox<>();
        combo.getItems().addAll(allCemeteries);
        combo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cemetery item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Cemetery item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        VBox content = new VBox(10, new Label("Кладбище:"), combo);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        ButtonType okButton = new ButtonType("Назначить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        // вывод результата
        dialog.setResultConverter(button -> button == okButton ? combo.getValue() : null);

        Optional<Cemetery> result = dialog.showAndWait();
        result.ifPresent(cemetery -> {
            if (userDao.assignAdminToCemetery(selected.getId(), cemetery.getId())) {
                statusLabel.setText("Администратор назначен на " + cemetery.getName());
                loadAdmins();
                adminsTable.refresh();
            } else {
                statusLabel.setText("Ошибка при назначении");
            }
        });
    }

    @FXML
    private void onBackClick() {
        SceneNavigator.switchTo("super_admin.fxml", "Панель супер-администратора");
    }

    private void clearForm() {
        lastNameField.clear();
        firstNameField.clear();
        othestvoField.clear();
        loginField.clear();
        passwordField.clear();
        cemeteryCombo.setValue(null);
    }

    private void highlightError(Control field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    }

    private void resetFieldStyles() {
        lastNameField.setStyle("");
        firstNameField.setStyle("");
        othestvoField.setStyle("");
        loginField.setStyle("");
        passwordField.setStyle("");
        cemeteryCombo.setStyle("");
    }

    private void fixWindowSize() {
        if (backBtn != null && backBtn.getScene() != null && backBtn.getScene().getWindow() != null) {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setWidth(800);
            stage.setHeight(700);
            stage.setMinWidth(800);
            stage.setMinHeight(700);
        }
    }
}