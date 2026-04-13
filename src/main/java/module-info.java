module com.example.electroniccemetery {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens com.example.electroniccemetery.model to javafx.base;
    opens com.example.electroniccemetery.controller to javafx.fxml;

    exports com.example.electroniccemetery;
    exports com.example.electroniccemetery.model;
    exports com.example.electroniccemetery.controller;
    exports com.example.electroniccemetery.util;
    exports com.example.electroniccemetery.dao;
}