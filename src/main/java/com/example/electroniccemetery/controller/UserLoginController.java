package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.UserDao;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import com.example.electroniccemetery.util.LoggerUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserLoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserDao userDao = new UserDao();

    @FXML
    private void onLoginClick() {
        String login = loginField.getText();
        String password = passwordField.getText();

        if (login.isBlank() || password.isBlank()) {
            errorLabel.setText("Введите логин и пароль");
            return;
        }

        int userId = userDao.checkUserLogin(login, password);
        if (userId > 0) {
            errorLabel.setText("");
            SelectedContext.clear();
            SelectedContext.setCurrentUserId(userId);
            SelectedContext.setCurrentUserLogin(login);
            SelectedContext.setCurrentUserRole("Пользователь");
            SceneNavigator.switchTo("user_profile.fxml", "Личный кабинет");
            LoggerUtil.info("Вход выполнен");
        } else {
            LoggerUtil.warning("Неудачная попытка входа: " + login);
            errorLabel.setText("Неверный логин или пароль");
        }
    }

    @FXML
    private void onRegisterClick() {
        SceneNavigator.switchTo("user_register.fxml", "Регистрация");
    }

    @FXML
    private void onBackClick() {
        LoggerUtil.info("Выход из системы (Пользователь)");
        SceneNavigator.switchTo("start.fxml", "Информационная система учёта захоронений");
    }
    @FXML
    private void onProfileClick() {
        SceneNavigator.switchTo("user_profile.fxml", "Личный кабинет");
    }
}