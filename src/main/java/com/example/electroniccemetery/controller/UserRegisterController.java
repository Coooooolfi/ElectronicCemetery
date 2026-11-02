package com.example.electroniccemetery.controller;

import com.example.electroniccemetery.dao.UserDao;
import com.example.electroniccemetery.util.SceneNavigator;
import com.example.electroniccemetery.util.SelectedContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserRegisterController {

    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField othestvoField;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    private final UserDao userDao = new UserDao();

    @FXML
    private void onRegisterClick() {
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String othestvo = othestvoField.getText().trim();
        String login = loginField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        errorLabel.setStyle("-fx-text-fill: red;");

        if (lastName.isBlank() || firstName.isBlank() || login.isBlank() || password.isBlank()) {
            errorLabel.setText("Заполните обязательные поля");
            return;
        }
        if (password.length() < 4) {
            errorLabel.setText("Пароль должен быть не менее 4 символов");
            return;
        }
        if (!password.equals(confirm)) {
            errorLabel.setText("Пароли не совпадают");
            return;
        }
        if (userDao.loginExists(login)) {
            errorLabel.setText("Такой логин уже существует");
            return;
        }

        int id = userDao.registerUser(lastName, firstName, othestvo, login, password);
        if (id > 0) {
            SelectedContext.clear();
            SelectedContext.setCurrentUserId(id);
            SelectedContext.setCurrentUserLogin(login);
            SelectedContext.setCurrentUserRole("Пользователь");
            SceneNavigator.switchTo("user_profile.fxml", "Личный кабинет");
        } else {
            errorLabel.setText("Ошибка при регистрации");
        }
    }

    @FXML
    private void onBackToLoginClick() {
        SceneNavigator.switchTo("user_login.fxml", "Вход пользователя");
    }

    @FXML
    private void onBackClick() {
        SceneNavigator.switchTo("start.fxml", "Информационная система учёта захоронений");
    }

    private void clearForm() {
        lastNameField.clear();
        firstNameField.clear();
        othestvoField.clear();
        loginField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}