package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.CemeteryDao;
import com.example.electroniccemetery.dao.SectionDao;
import com.example.electroniccemetery.model.Cemetery;
import com.example.electroniccemetery.model.Section;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class AdminSectionsController {

    @FXML private ComboBox<Cemetery> cemeteryCombo;
    @FXML private TextField numberField;
    @FXML private Label statusLabel;
    @FXML private TableView<Section> sectionsTable;
    @FXML private TableColumn<Section, Number> colId;
    @FXML private TableColumn<Section, Number> colNumber;
    @FXML private Button backBtn;
    @FXML private Button saveBtn;

    private final CemeteryDao cemeteryDao = new CemeteryDao();
    private final SectionDao sectionDao = new SectionDao();

    private final ObservableList<Cemetery> cemeteries = FXCollections.observableArrayList();
    private final ObservableList<Section> sections = FXCollections.observableArrayList();

    private int currentCemeteryId = 0;
    private boolean isSuperAdmin = false;

    @FXML
    private void initialize() {
        setupTable();
        loadCemeteries();
        setupComboBox();
        checkUserRole();
        fixWindowSize();
    }

    private void checkUserRole() {
        Integer adminCemeteryId = SelectedContext.getCurrentUserCemeteryId();
        String role = SelectedContext.getCurrentUserRole();
        isSuperAdmin = "Супер-администратор".equals(role);

        if (!isSuperAdmin && adminCemeteryId != null) {
            // Обычным не дано выбирать (сокрытие комбобокса от админа)
            cemeteryCombo.setVisible(false);
            currentCemeteryId = adminCemeteryId;
            loadSections();
        } else {
            cemeteryCombo.setVisible(true);
        }
    }

    private void loadCemeteries() {
        List<Cemetery> list = cemeteryDao.findAll();
        cemeteries.setAll(list);
        cemeteryCombo.setItems(cemeteries);
    }

    private void setupComboBox() {
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

    private void setupTable() {
        colId.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getId()));
        colNumber.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getNumber()));
        sectionsTable.setItems(sections);
    }

    private void loadSections() {
        sections.clear();
        if (currentCemeteryId > 0) {
            List<Section> list = sectionDao.findByCemeteryId(currentCemeteryId);
            sections.setAll(list);
        }
    }

    @FXML
    private void onCemeteryChanged() {
        if (!isSuperAdmin) return;

        Cemetery selected = cemeteryCombo.getValue();
        sections.clear();
        statusLabel.setText("");
        if (selected != null) {
            currentCemeteryId = selected.getId();
            List<Section> list = sectionDao.findByCemeteryId(selected.getId());
            sections.setAll(list);
        }
    }

    @FXML
    private void onSaveClick() {
        String numText = numberField.getText();
        statusLabel.setText("");

        if (currentCemeteryId == 0) {
            statusLabel.setText("Выберите кладбище");
            return;
        }
        if (numText == null || numText.isBlank()) {
            statusLabel.setText("Укажите номер сектора");
            return;
        }

        int number;
        try {
            number = Integer.parseInt(numText);
        } catch (NumberFormatException e) {
            statusLabel.setText("Номер сектора должен быть числом");
            return;
        }

        if (sectionDao.existsByCemeteryAndNumber(currentCemeteryId, number)) {
            statusLabel.setText("Сектор с таким номером уже существует");
            return;
        }

        int id = sectionDao.insertSection(currentCemeteryId, number);
        if (id > 0) {
            statusLabel.setText("Сектор добавлен, ID = " + id);
            numberField.clear();
            loadSections();
        } else {
            statusLabel.setText("Ошибка при добавлении сектора");
        }
    }

    @FXML
    private void onBackClick() {
        fixWindowSize();

        String role = SelectedContext.getCurrentUserRole();

        if ("Супер-администратор".equals(role)) {
            SceneNavigator.switchTo("super_admin.fxml", "Панель супер-администратора");
        } else {
            SceneNavigator.switchTo("admin.fxml", "Администрирование");
        }
    }

    private void fixWindowSize() {
        if (backBtn != null && backBtn.getScene() != null && backBtn.getScene().getWindow() != null) {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setWidth(650);
            stage.setHeight(700);
            stage.setMinWidth(650);
            stage.setMinHeight(700);
        }
    }
}