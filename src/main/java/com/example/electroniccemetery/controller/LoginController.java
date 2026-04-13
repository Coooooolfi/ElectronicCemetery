package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.UserDao;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

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

        String role = userDao.checkAndGetRole(login, password);

        if (role != null) {
            errorLabel.setText("");

            // Сохраняем данные пользователя в контексте
            SelectedContext.setCurrentUserLogin(login);
            SelectedContext.setCurrentUserRole(role);
            SelectedContext.setCurrentUserCemeteryId(userDao.getUserCemeteryId(login));

            // Отладка
            System.out.println("Login successful: role=" + role + ", cemeteryId=" + SelectedContext.getCurrentUserCemeteryId());

            if (role.equals("Супер-администратор")) {
                SceneNavigator.switchTo("super_admin.fxml", "Панель супер-администратора");
            } else if (role.equals("Администратор")) {
                SceneNavigator.switchTo("admin.fxml", "Администрирование");
            } else {
                errorLabel.setText("Неизвестная роль пользователя");
            }
        } else {
            errorLabel.setText("Неверный логин или пароль");
        }
    }

    @FXML
    private void onBackClick() {
        SceneNavigator.switchTo("start.fxml", "Информационная система учёта захоронений");
    }
}