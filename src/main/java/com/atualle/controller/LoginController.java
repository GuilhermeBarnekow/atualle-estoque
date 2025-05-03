package com.atualle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

    // Simple in-memory user store: username -> (password, role)
    private final Map<String, User> users = new HashMap<>();

    public LoginController() {
        // Initialize with some users
        users.put("admin", new User("admin", "admin123", "admin"));
        users.put("operator", new User("operator", "operator123", "operator"));
    }

    @FXML
    public void initialize() {
        // Add enter key support to both fields
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
        
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        // Clear error message when typing
        usernameField.textProperty().addListener((obs, old, newValue) -> messageLabel.setText(""));
        passwordField.textProperty().addListener((obs, old, newValue) -> messageLabel.setText(""));

        // Set initial focus to username field
        usernameField.requestFocus();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            messageLabel.setText("Por favor, preencha usuário e senha.");
            return;
        }

        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            messageLabel.setText("");
            openMainApp(user.role);
        } else {
            messageLabel.setText("Usuário ou senha inválidos.");
        }
    }

    private void openMainApp(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Parent root = loader.load();

            // Pass user role to MainController
            MainController mainController = loader.getController();
            mainController.setUserRole(role);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root, 1024, 768);
            stage.setScene(scene);
            stage.setTitle("Atualle - Sistema de Gerenciamento de Estoque");
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            messageLabel.setText("Erro ao abrir a aplicação principal.");
            e.printStackTrace();
        }
    }

    private static class User {
        String username;
        String password;
        String role;

        User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }
}
