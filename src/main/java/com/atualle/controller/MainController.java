package com.atualle.controller;

import com.atualle.model.Category;
import com.atualle.model.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private String userRole;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private StackPane contentPane;

    @FXML
    private Button btnProducts;

    @FXML
    private Button btnCategories;

    @FXML
    private Button btnStockMovements;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnSettings;

    @FXML
    private Label statusLabel;

    public void setUserRole(String role) {
        this.userRole = role;
        // You can add role-based UI adjustments here
        System.out.println("User role set to: " + role);
    }

    @FXML
    public void initialize() {
        showProductsView();
        // setupKeyboardShortcuts(); // Removed because method is undefined
    }

    private Button activeButton = null;

    private void setActiveButton(Button button) {
        // Remove active class from all buttons
        btnProducts.getStyleClass().remove("active");
        btnCategories.getStyleClass().remove("active");
        btnStockMovements.getStyleClass().remove("active");
        btnReports.getStyleClass().remove("active");
        btnSettings.getStyleClass().remove("active");

        // Add active class to selected button
        if (button != null) {
            button.getStyleClass().add("active");
        }
    }

    @FXML
    private void showProductsView() {
        loadView("/fxml/ProductView.fxml");
        updateStatus("Produtos");
        setActiveButton(btnProducts);
    }

    @FXML
    private void showCategoriesView() {
        loadView("/fxml/CategoryView.fxml");
        updateStatus("Categorias");
        setActiveButton(btnCategories);
    }

    @FXML
    private void showStockMovementsView() {
        loadView("/fxml/StockMovementView.fxml");
        updateStatus("Movimentações");
        setActiveButton(btnStockMovements);
    }

    @FXML
    private void showReportsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReportView.fxml"));
            Node view = loader.load();
            ReportController reportController = loader.getController();
            reportController.setPrimaryStage((Stage) contentPane.getScene().getWindow());
            contentPane.getChildren().setAll(view);
            updateStatus("Relatórios");
            setActiveButton(btnReports);
        } catch (IOException e) {
            e.printStackTrace();
            updateStatus("Erro ao carregar a view de relatórios");
        }
    }

    @FXML
    private void showSettingsView() {
        loadView("/fxml/SettingsView.fxml"); // Load the settings view
        updateStatus("Configurações");
        setActiveButton(btnSettings);
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            
            // Clear existing content and add new view
            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);
            
            // Force a layout pass
            contentPane.applyCss();
            contentPane.requestLayout();
        } catch (IOException e) {
            e.printStackTrace();
            updateStatus("Erro ao carregar a view: " + fxmlPath);
        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.isControlDown()) {
            if (event.getCode() == KeyCode.P) {
                showProductsView();
            } else if (event.getCode() == KeyCode.C) {
                showCategoriesView();
            } else if (event.getCode() == KeyCode.M) {
                showStockMovementsView();
            } else if (event.getCode() == KeyCode.R) {
                showReportsView();
            } else if (event.getCode() == KeyCode.S) {
                showSettingsView();
            }
        }
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText("Seção atual: " + message);
        }
    }
}
