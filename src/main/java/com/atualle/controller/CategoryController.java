package com.atualle.controller;

import com.atualle.dao.CategoryDAO;
import com.atualle.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;

public class CategoryController {

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, Integer> idColumn;

    @FXML
    private TableColumn<Category, String> nameColumn;

    @FXML
    private TextField searchField;

    @FXML
    private TextField idField; // Consider setting editable to false

    @FXML
    private TextField nameField;

    private ObservableList<Category> categoryList = FXCollections.observableArrayList();
    private CategoryDAO categoryDAO;

    @FXML
    public void initialize() { // Removed throws SQLException
        try {
             // DAO constructor can throw SQLException
            categoryDAO = new CategoryDAO();
        } catch (SQLException e) {
             showAlert("Erro Crítico", "Não foi possível conectar ao banco de dados: " + e.getMessage());
             // Consider disabling UI elements or exiting if connection fails
             return;
        }

        setupTableColumns();
        // Handle SQLException for initial load
        try {
            loadCategories(); // This method throws SQLException
        } catch (SQLException e) {
            showAlert("Erro", "Não foi possível carregar as categorias iniciais: " + e.getMessage());
        }

        categoryTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCategoryDetails(newValue)
        );
        // idField.setEditable(false);
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    }

    // Declares throws SQLException, handled by callers
    private void loadCategories() throws SQLException { // Added throws SQLException
        // Removed internal try-catch
        List<Category> categories = categoryDAO.findAll();
        categoryList = FXCollections.observableArrayList(categories);
        categoryTable.setItems(categoryList);
    }

    private void showCategoryDetails(Category category) {
        if (category != null) {
            idField.setText(String.valueOf(category.getId()));
            nameField.setText(category.getName());
        } else {
            clearInputFields();
        }
    }

    @FXML
    private void handleAddCategory() {
        try {
            // Basic validation
            if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
                 showAlert("Campo Obrigatório", "O nome da categoria não pode estar vazio.");
                 return;
            }
            Category category = new Category(0, nameField.getText().trim());
            // DAO insert throws SQLException
            categoryDAO.insert(category);
            // Reload categories, handling potential SQLException
            try {
                loadCategories(); // This method throws SQLException
            } catch (SQLException ex) {
                showAlert("Erro", "Erro ao recarregar categorias após adição: " + ex.getMessage());
            }
            clearInputFields();
        } catch (SQLException e) { // Catch SQLException from DAO insert
             showAlert("Erro de Banco de Dados", "Erro ao adicionar categoria: " + e.getMessage());
        } catch (Exception e) { // Catch other potential errors
            showAlert("Erro Inesperado", "Ocorreu um erro ao adicionar categoria: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateCategory() {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                 // Basic validation
                if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
                     showAlert("Campo Obrigatório", "O nome da categoria não pode estar vazio.");
                     return;
                }
                selectedCategory.setName(nameField.getText().trim());
                // DAO update throws SQLException
                categoryDAO.update(selectedCategory);
                // Reload categories, handling potential SQLException
                try {
                    loadCategories(); // This method throws SQLException
                } catch (SQLException ex) {
                    showAlert("Erro", "Erro ao recarregar categorias após atualização: " + ex.getMessage());
                }
                clearInputFields();
            } catch (SQLException e) { // Catch SQLException from DAO update
                 showAlert("Erro de Banco de Dados", "Erro ao atualizar categoria: " + e.getMessage());
            } catch (Exception e) { // Catch other potential errors
                showAlert("Erro Inesperado", "Ocorreu um erro ao atualizar categoria: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Nenhuma Categoria Selecionada", "Por favor, selecione uma categoria para atualizar.");
        }
    }

    @FXML
    private void handleDeleteCategory() {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
             Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja deletar a categoria '" + selectedCategory.getName() + "'?", ButtonType.YES, ButtonType.NO);
             confirmation.setTitle("Confirmar Deleção");
             confirmation.setHeaderText(null);
             confirmation.showAndWait().ifPresent(response -> {
                 if (response == ButtonType.YES) {
                    try {
                        // DAO delete throws SQLException
                        categoryDAO.delete(selectedCategory.getId());
                        // Reload categories, handling potential SQLException
                        try {
                            loadCategories(); // This method throws SQLException
                        } catch (SQLException ex) {
                            showAlert("Erro", "Erro ao recarregar categorias após deleção: " + ex.getMessage());
                        }
                        clearInputFields();
                    } catch (SQLException e) { // Catch SQLException from DAO delete
                         showAlert("Erro de Banco de Dados", "Erro ao deletar categoria: " + e.getMessage());
                    } catch (Exception e) { // Catch potential runtime errors
                        showAlert("Erro Inesperado", "Ocorreu um erro ao deletar categoria: " + e.getMessage());
                        e.printStackTrace();
                    }
                 }
             });
        } else {
            showAlert("Nenhuma Categoria Selecionada", "Por favor, selecione uma categoria para deletar.");
        }
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        // Reload categories, handling potential SQLException
        try {
            loadCategories(); // This method throws SQLException
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao recarregar categorias ao limpar busca: " + e.getMessage());
        }
    }

    private void clearInputFields() {
        idField.clear();
        nameField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING); // Consider using INFORMATION or ERROR
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
