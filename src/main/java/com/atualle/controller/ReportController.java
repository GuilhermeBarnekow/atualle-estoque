package com.atualle.controller;

import com.atualle.dao.ProductDAO;
import com.atualle.dao.StockMovementDAO;
import com.atualle.model.Product;
import com.atualle.model.StockMovement;
import com.atualle.util.PdfReportGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class ReportController {

    @FXML
    private Button btnGenerateStockReport;

    @FXML
    private Button btnGenerateMovementReport;

    @FXML
    private Stage primaryStage;

    private ProductDAO productDAO;
    private StockMovementDAO movementDAO;

    @FXML
    public void initialize() {
        try {
            productDAO = new ProductDAO();
            movementDAO = new StockMovementDAO();
        } catch (SQLException e) {
            showAlert("Erro", "Falha ao inicializar DAOs: " + e.getMessage());
        }
    }

    @FXML
    private void handleGenerateStockReport() {
        try {
            List<Product> products = productDAO.findAll();
            File file = showSaveDialog("Salvar relatório de estoque atual", "estoque_atual.pdf");
            if (file != null) {
                PdfReportGenerator.generateProductReport(products, file);
                showAlert("Sucesso", "Relatório de estoque gerado com sucesso.");
            }
        } catch (Exception e) {
            showAlert("Erro", "Falha ao gerar relatório de estoque: " + e.getMessage());
        }
    }

    @FXML
    private void handleGenerateMovementReport() {
        try {
            List<StockMovement> movements = movementDAO.findAll();
            File file = showSaveDialog("Salvar relatório de movimentações", "movimentacoes.pdf");
            if (file != null) {
                PdfReportGenerator.generateStockMovementReport(movements, file);
                showAlert("Sucesso", "Relatório de movimentações gerado com sucesso.");
            }
        } catch (Exception e) {
            showAlert("Erro", "Falha ao gerar relatório de movimentações: " + e.getMessage());
        }
    }

    private File showSaveDialog(String title, String defaultFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialFileName(defaultFileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        return fileChooser.showSaveDialog(primaryStage);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
