package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.*;
import com.example.electroniccemetery.model.*;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class AdminPlotsController {

    @FXML private ComboBox<Cemetery> cemeteryCombo;
    @FXML private ComboBox<Section> sectionCombo;
    @FXML private TextField numberField;
    @FXML private Label statusLabel;
    @FXML private TableView<Plot> plotsTable;
    @FXML private TableColumn<Plot, Number> colId;
    @FXML private TableColumn<Plot, Number> colNumber;
    @FXML private TableColumn<Plot, String> colSection;
    @FXML private Button backBtn;
    @FXML private Button saveBtn;

    private final CemeteryDao cemeteryDao = new CemeteryDao();
    private final SectionDao sectionDao = new SectionDao();
    private final PlotDao plotDao = new PlotDao();

    private final ObservableList<Cemetery> cemeteries = FXCollections.observableArrayList();
    private final ObservableList<Section> sections = FXCollections.observableArrayList();
    private final ObservableList<Plot> plots = FXCollections.observableArrayList();

    private Integer adminCemeteryId = null;
    private boolean isSuperAdmin = false;

    @FXML
    private void initialize() {
        loadCemeteries();
        setupComboBoxes();
        setupTable();
        checkUserRole();
        fixWindowSize();
    }

    private void checkUserRole() {
        adminCemeteryId = SelectedContext.getCurrentUserCemeteryId();
        String role = SelectedContext.getCurrentUserRole();
        isSuperAdmin = "Супер-администратор".equals(role);

        if (!isSuperAdmin && adminCemeteryId != null) {
            // Обычным не дано выбирать (сокрытие комбобокса от админа)
            cemeteryCombo.setVisible(false);
            // прогрузка секторов кладбища админа
            loadSectionsForAdmin(adminCemeteryId);
        } else {
            cemeteryCombo.setVisible(true);
        }
    }

    private void loadSectionsForAdmin(int cemeteryId) {
        sections.clear();
        sectionCombo.setValue(null);
        plots.clear();
        statusLabel.setText("");

        List<Section> list = sectionDao.findByCemeteryId(cemeteryId);
        sections.setAll(list);
        sectionCombo.setItems(sections);
    }

    private void loadCemeteries() {
        List<Cemetery> list = cemeteryDao.findAll();
        cemeteries.setAll(list);
        cemeteryCombo.setItems(cemeteries);
    }

    private void setupComboBoxes() {
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

        sectionCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Section item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "Сектор " + item.getNumber());
            }
        });
        sectionCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Section item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "Сектор " + item.getNumber());
            }
        });
    }

    private void setupTable() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        colNumber.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNumber()));
        colSection.setCellValueFactory(data -> {
            Section s = sectionDao.findById(data.getValue().getSectionId());
            return new javafx.beans.property.SimpleStringProperty("Сектор " + (s != null ? s.getNumber() : "?"));
        });
        plotsTable.setItems(plots);
    }

    @FXML
    private void onCemeteryChanged() {
        if (!isSuperAdmin) return;

        Cemetery selected = cemeteryCombo.getValue();
        sections.clear();
        sectionCombo.setValue(null);
        plots.clear();
        statusLabel.setText("");

        if (selected != null) {
            List<Section> list = sectionDao.findByCemeteryId(selected.getId());
            sections.setAll(list);
            sectionCombo.setItems(sections);
        }
    }

    @FXML
    private void onSectionChanged() {
        Section selected = sectionCombo.getValue();
        plots.clear();
        statusLabel.setText("");

        if (selected != null) {
            List<Plot> list = plotDao.findBySectionId(selected.getId());
            plots.setAll(list);
        }
    }

    @FXML
    private void onSaveClick() {
        Section selectedSection = sectionCombo.getValue();
        String numText = numberField.getText();
        statusLabel.setText("");

        if (selectedSection == null) {
            statusLabel.setText("Выберите сектор");
            return;
        }
        if (numText == null || numText.isBlank()) {
            statusLabel.setText("Укажите номер участка");
            return;
        }

        int number;
        try {
            number = Integer.parseInt(numText);
        } catch (NumberFormatException e) {
            statusLabel.setText("Номер участка должен быть числом");
            return;
        }

        if (plotDao.existsBySectionAndNumber(selectedSection.getId(), number)) {
            statusLabel.setText("Участок с таким номером уже существует");
            return;
        }

        int id = plotDao.insertPlot(selectedSection.getId(), number);
        if (id > 0) {
            statusLabel.setText("Участок добавлен, ID = " + id);
            numberField.clear();
            onSectionChanged();
        } else {
            statusLabel.setText("Ошибка при добавлении участка");
        }
    }

    @FXML
    private void onDeleteClick() {
        Plot selected = plotsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите участок для удаления");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение");
        confirm.setHeaderText("Удаление участка");
        confirm.setContentText("Вы уверены, что хотите удалить участок №" + selected.getNumber() + "?");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (plotDao.deletePlot(selected.getId())) {
                statusLabel.setText("Участок удалён");
                onSectionChanged();
            } else {
                statusLabel.setText("Ошибка при удалении");
            }
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