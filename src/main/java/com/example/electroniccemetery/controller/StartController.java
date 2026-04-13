package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class StartController {

    @FXML
    public void onLoginClick(ActionEvent event) {
        SceneNavigator.switchTo("login.fxml", "Авторизация администратора");
    }

    @FXML
    public void onVisitorClick(ActionEvent event) {
        SceneNavigator.switchTo("visitor_cemeteries.fxml", "Список кладбищ");
    }
}
