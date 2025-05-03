package com.atualle.controller;

import com.atualle.dao.StockMovementDAO;
import com.atualle.model.StockMovement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date; // Keep only one import for Date
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
// import java.sql.Date; // Remove duplicate import

public class StockMovementController {

    @FXML
    private TableView<StockMovement> stockMovementTable;

    @FXML
    private TableColumn<StockMovement, Integer> idColumn;

    @FXML
    private TableColumn<StockMovement, String> typeColumn;

    @FXML
    private TableColumn<StockMovement, LocalDate> dateColumn; // Should likely be LocalDate if using PropertyValueFactory

    @FXML
    private TableColumn<StockMovement, String> userColumn;

    @FXML
    private TableColumn<StockMovement, Integer> quantityColumn;

    @FXML
    private TextField searchField;

    @FXML
    private TextField idField; // Usually not editable, consider setEditable(false)

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField userField;

    @FXML
    private TextField quantityField;

    private ObservableList<StockMovement> stockMovementList = FXCollections.observableArrayList();
    private StockMovementDAO stockMovementDAO;

    @FXML
    public void initialize() throws SQLException { // Added throws SQLException
        stockMovementDAO = new StockMovementDAO();

        setupTableColumns();

        loadStockMovements(); // Errors handled internally

        typeComboBox.setItems(FXCollections.observableArrayList("Entrada", "Saída", "Ajuste"));
        datePicker.setValue(LocalDate.now()); // Set default date

        stockMovementTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStockMovementDetails(newValue)
        );

        // Consider making ID field non-editable
        // idField.setEditable(false);
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        // Ensure StockMovement model uses LocalDate for 'date' property if using DatePicker directly
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    // Handles SQLException internally
    private void loadStockMovements() { // Removed 'throws SQLException'
        try {
            List<StockMovement> movements = stockMovementDAO.findAll();
            stockMovementList = FXCollections.observableArrayList(movements);
            stockMovementTable.setItems(stockMovementList);
        } catch (SQLException e) {
            showAlert("Erro", "Não foi possível carregar as movimentações: " + e.getMessage());
        }
    }

    private void showStockMovementDetails(StockMovement movement) {
        if (movement != null) {
            idField.setText(String.valueOf(movement.getId()));
            typeComboBox.setValue(movement.getType());
            // Assuming StockMovement.getDate() returns LocalDateTime, convert to LocalDate for DatePicker
            if (movement.getDate() != null) {
                 datePicker.setValue(movement.getDate().toLocalDate());
            } else {
                 datePicker.setValue(null);
            }
            userField.setText(movement.getUser());
            quantityField.setText(String.valueOf(movement.getQuantity()));
        } else {
            clearInputFields();
        }
    }

    @FXML
    private void handleAddStockMovement() {
        try {
            // Basic validation
            if (typeComboBox.getValue() == null || datePicker.getValue() == null || userField.getText().isEmpty()) {
                 showAlert("Campos Obrigatórios", "Tipo, Data e Usuário são obrigatórios.");
                 return;
            }

            StockMovement movement = new StockMovement(
                    0,
                    typeComboBox.getValue(),
                    datePicker.getValue().atStartOfDay(), // Convert LocalDate to LocalDateTime
                    userField.getText(),
                    Integer.parseInt(quantityField.getText()),
                    null // Assuming productId is not set here or handled differently
            );
            // Assuming insert does not throw SQLException
            stockMovementDAO.insert(movement);
            loadStockMovements(); // Reload list (errors handled internally)
            clearInputFields();
        } catch (NumberFormatException e) {
            showAlert("Erro de Formato", "Por favor, insira um valor numérico válido para quantidade.");
        } catch (Exception e) { // Catch other potential errors
             showAlert("Erro Inesperado", "Ocorreu um erro ao adicionar movimentação: " + e.getMessage());
             e.printStackTrace(); // Log the stack trace for debugging
        }
    }

    @FXML
    private void handleUpdateStockMovement() {
        StockMovement selectedMovement = stockMovementTable.getSelectionModel().getSelectedItem();
        if (selectedMovement != null) {
            try {
                // Basic validation
                if (typeComboBox.getValue() == null || datePicker.getValue() == null || userField.getText().isEmpty()) {
                     showAlert("Campos Obrigatórios", "Tipo, Data e Usuário são obrigatórios.");
                     return;
                }

                selectedMovement.setType(typeComboBox.getValue());
                selectedMovement.setDate(datePicker.getValue().atStartOfDay()); // Convert LocalDate to LocalDateTime
                selectedMovement.setUser(userField.getText());
                selectedMovement.setQuantity(Integer.parseInt(quantityField.getText()));

                // Assuming update does not throw SQLException
                stockMovementDAO.update(selectedMovement);
                loadStockMovements(); // Reload list (errors handled internally)
                clearInputFields();
            } catch (NumberFormatException e) {
                showAlert("Erro de Formato", "Por favor, insira um valor numérico válido para quantidade.");
            } catch (Exception e) { // Catch other potential errors
                 showAlert("Erro Inesperado", "Ocorreu um erro ao atualizar movimentação: " + e.getMessage());
                 e.printStackTrace(); // Log the stack trace for debugging
            }
        } else {
            showAlert("Nenhuma Movimentação Selecionada", "Por favor, selecione uma movimentação para atualizar.");
        }
    }

    @FXML
    private void handleDeleteStockMovement() {
        StockMovement selectedMovement = stockMovementTable.getSelectionModel().getSelectedItem();
        if (selectedMovement != null) {
             Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja deletar a movimentação ID " + selectedMovement.getId() + "?", ButtonType.YES, ButtonType.NO);
             confirmation.setTitle("Confirmar Deleção");
             confirmation.setHeaderText(null);
             confirmation.showAndWait().ifPresent(response -> {
                 if (response == ButtonType.YES) {
                    try {
                        // Assuming delete does not throw SQLException
                        stockMovementDAO.delete(selectedMovement.getId());
                        loadStockMovements(); // Reload list (errors handled internally)
                        clearInputFields();
                    } catch (Exception e) { // Catch potential runtime errors
                        showAlert("Erro Inesperado", "Ocorreu um erro ao deletar movimentação: " + e.getMessage());
                        e.printStackTrace(); // Log the stack trace for debugging
                    }
                 }
             });
        } else {
            showAlert("Nenhuma Movimentação Selecionada", "Por favor, selecione uma movimentação para deletar.");
        }
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        loadStockMovements(); // Reload list (errors handled internally)
    }

    private void clearInputFields() {
        idField.clear(); // ID field might not need clearing if non-editable
        typeComboBox.setValue(null);
        datePicker.setValue(LocalDate.now()); // Reset to default or null
        userField.clear();
        quantityField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING); // Consider using INFORMATION or ERROR
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
