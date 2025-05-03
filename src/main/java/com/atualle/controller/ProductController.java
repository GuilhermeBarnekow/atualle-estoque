package com.atualle.controller;

import com.atualle.dao.CategoryDAO;
import com.atualle.dao.ProductDAO;
import com.atualle.model.Category;
import com.atualle.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public class ProductController {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, ImageView> photoColumn;

    @FXML
    private TableColumn<Product, String> skuColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, String> colorColumn;

    @FXML
    private TableColumn<Product, String> sizeColumn;

    @FXML
    private TableColumn<Product, Double> costColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView photoView;

    @FXML
    private TextField skuField;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private TextField colorField;

    @FXML
    private TextField sizeField;

    @FXML
    private TextField costField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    private File selectedImageFile; // Consider if this is used or should be removed

    @FXML
    public void initialize() throws SQLException { // Added throws SQLException
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();

        setupTableColumns();
        // Load categories and products, handling potential errors internally within the methods
        loadCategories();
        loadProducts();
        // Removed the try-catch block here

        productTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProductDetails(newValue)
        );

        setupRowFactory();
        setupKeyboardShortcuts();
    }

    private void setupRowFactory() {
        productTable.setRowFactory(tv -> new TableRow<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                    getStyleClass().remove("low-stock");
                } else {
                    // Zebra striping handled by CSS :odd and :even
                    if (item.getQuantity() <= getMinimumStock(item)) {
                        if (!getStyleClass().contains("low-stock")) {
                            getStyleClass().add("low-stock");
                        }
                    } else {
                        getStyleClass().remove("low-stock");
                    }
                }
            }
        });
    }

    private int getMinimumStock(Product product) {
        // Assuming minimum stock is defined somewhere; if not, default to 5
        // This method can be adjusted to get actual minimum stock from product or config
        return 5;
    }

    private void setupKeyboardShortcuts() {
        // Add event handler directly to the TableView
        productTable.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                clearInputFields();
                event.consume();
            }
        });

        // Add a persistent scene property listener
        productTable.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (oldScene != null) {
                oldScene.removeEventFilter(KeyEvent.KEY_PRESSED, this::handleEscapeKey);
            }
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleEscapeKey);
            }
        });

        // If scene is already available, add the filter
        if (productTable.getScene() != null) {
            productTable.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::handleEscapeKey);
        }
    }

    private void handleEscapeKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            clearInputFields();
            event.consume();
        }
    }

    private void setupTableColumns() {
        photoColumn.setCellValueFactory(cellData -> {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            if (cellData.getValue().getPhoto() != null) {
                try {
                    // Assuming getPhoto() returns a valid URL or path string
                    Image image = new Image(cellData.getValue().getPhoto());
                    imageView.setImage(image);
                } catch (Exception e) {
                    // Handle potential errors loading the image (e.g., invalid URL, file not found)
                    System.err.println("Error loading image: " + cellData.getValue().getPhoto() + " - " + e.getMessage());
                    imageView.setImage(null); // Set to null or a placeholder image
                }
            } else {
                 imageView.setImage(null);
            }
            return new SimpleObjectProperty<>(imageView);
        });
        skuColumn.setCellValueFactory(cellData -> cellData.getValue().skuProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        categoryColumn.setCellValueFactory(cellData -> {
            Category category = cellData.getValue().getCategory();
            return new SimpleStringProperty(category != null ? category.getName() : "");
        });
        colorColumn.setCellValueFactory(cellData -> cellData.getValue().colorProperty());
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        costColumn.setCellValueFactory(cellData -> cellData.getValue().costProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
    }

    // Handles SQLException internally
    private void loadCategories() { // Removed 'throws SQLException'
        try {
            List<Category> categories = categoryDAO.findAll();
            categoryComboBox.setItems(FXCollections.observableArrayList(categories));
        } catch (SQLException e) {
             showAlert("Erro", "Não foi possível carregar as categorias: " + e.getMessage());
        }
    }

    // Handles SQLException internally
    private void loadProducts() { // Removed 'throws SQLException'
         try {
            List<Product> products = productDAO.findAll();
            productList = FXCollections.observableArrayList(products);
            productTable.setItems(productList);
         } catch (SQLException e) {
             showAlert("Erro", "Não foi possível carregar os produtos: " + e.getMessage());
         }
    }

    private void showProductDetails(Product product) {
        if (product != null) {
            if (product.getPhoto() != null) {
                try {
                    Image image = new Image(product.getPhoto());
                    photoView.setImage(image);
                } catch (Exception e) {
                    System.err.println("Error loading image for details view: " + product.getPhoto() + " - " + e.getMessage());
                    photoView.setImage(null);
                }
            } else {
                photoView.setImage(null);
            }
            skuField.setText(product.getSku());
            nameField.setText(product.getName());
            categoryComboBox.setValue(product.getCategory());
            colorField.setText(product.getColor());
            sizeField.setText(product.getSize());
            costField.setText(String.valueOf(product.getCost()));
            priceField.setText(String.valueOf(product.getPrice()));
            quantityField.setText(String.valueOf(product.getQuantity()));
            selectedImageFile = null; // Reset selected file when showing existing product
        } else {
            clearInputFields();
        }
    }

    @FXML
    private void handleAddProduct() {
        try {
            // Basic validation could be added here (e.g., check for empty fields)
            Product product = new Product(0, null, "", "", null, "", "", 0, 0, 0);

            // Handle image path setting - needs refinement based on how images are stored/retrieved
            if (photoView.getImage() != null) {
                 // This assumes getUrl() gives a usable path/identifier. May need adjustment.
                 // If selectedImageFile is used, handle its path instead.
                product.setPhoto(photoView.getImage().getUrl());
            } else {
                product.setPhoto(null);
            }

            product.setSku(skuField.getText());
            product.setName(nameField.getText());
            product.setCategory(categoryComboBox.getValue()); // Ensure category is selected
            product.setColor(colorField.getText());
            product.setSize(sizeField.getText());
            product.setCost(Double.parseDouble(costField.getText()));
            product.setPrice(Double.parseDouble(priceField.getText()));
            product.setQuantity(Integer.parseInt(quantityField.getText()));

            // Assuming productDAO.insert does NOT throw SQLException
            productDAO.insert(product);

            // Reload products (errors handled internally by loadProducts)
            loadProducts(); // Removed try-catch block here
            clearInputFields();

        } catch (NumberFormatException e) {
            showAlert("Erro de Formato", "Por favor, insira valores numéricos válidos para custo, preço e quantidade.");
        } catch (Exception e) { // Catch other potential runtime errors
             showAlert("Erro Inesperado", "Ocorreu um erro ao adicionar o produto: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                // Handle image path setting - similar to handleAddProduct
                 if (photoView.getImage() != null) {
                    selectedProduct.setPhoto(photoView.getImage().getUrl());
                 } else {
                    selectedProduct.setPhoto(null);
                 }

                selectedProduct.setSku(skuField.getText());
                selectedProduct.setName(nameField.getText());
                selectedProduct.setCategory(categoryComboBox.getValue());
                selectedProduct.setColor(colorField.getText());
                selectedProduct.setSize(sizeField.getText());
                selectedProduct.setCost(Double.parseDouble(costField.getText()));
                selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
                selectedProduct.setQuantity(Integer.parseInt(quantityField.getText()));

                // Assuming productDAO.update does NOT throw SQLException
                productDAO.update(selectedProduct);

                // Reload products (errors handled internally by loadProducts)
                loadProducts(); // Removed try-catch block here
                clearInputFields();

            } catch (NumberFormatException e) {
                showAlert("Erro de Formato", "Por favor, insira valores numéricos válidos para custo, preço e quantidade.");
            } catch (Exception e) { // Catch other potential runtime errors
                 showAlert("Erro Inesperado", "Ocorreu um erro ao atualizar o produto: " + e.getMessage());
            }
        } else {
            showAlert("Nenhum Produto Selecionado", "Por favor, selecione um produto na tabela para atualizar.");
        }
    }

    @FXML
    private void handleDeleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Confirmation dialog is recommended here
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja deletar o produto '" + selectedProduct.getName() + "'?", ButtonType.YES, ButtonType.NO);
            confirmation.setTitle("Confirmar Deleção");
            confirmation.setHeaderText(null);
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        // Assuming productDAO.delete does NOT throw SQLException
                        productDAO.delete(selectedProduct.getId());

                        // Reload products (errors handled internally by loadProducts)
                        loadProducts(); // Removed try-catch block here
                        clearInputFields();
                    } catch (Exception e) { // Catch potential runtime errors during delete/reload
                        showAlert("Erro Inesperado", "Ocorreu um erro ao deletar o produto: " + e.getMessage());
                    }
                }
            });
        } else {
            showAlert("Nenhum Produto Selecionado", "Por favor, selecione um produto na tabela para deletar.");
        }
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        // Reload products (errors handled internally by loadProducts)
        loadProducts(); // Removed try-catch block here
    }

    // Add method for handling image selection if needed
    @FXML
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem do Produto");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("Todos os Arquivos", "*.*")
        );
        File file = fileChooser.showOpenDialog(photoView.getScene().getWindow());
        if (file != null) {
            try {
                Image image = new Image(new FileInputStream(file));
                photoView.setImage(image);
                selectedImageFile = file; // Store the selected file if needed for saving
                 // Update the product's photo path immediately or store file path for saving later
                 // Example: skuField.getScene().setUserData(file.getAbsolutePath()); // Store path temporarily
            } catch (FileNotFoundException e) {
                showAlert("Erro", "Arquivo de imagem não encontrado: " + e.getMessage());
            } catch (Exception e) {
                 showAlert("Erro", "Não foi possível carregar a imagem: " + e.getMessage());
            }
        }
    }


    private void clearInputFields() {
        photoView.setImage(null); // Clear image view
        skuField.clear();
        nameField.clear();
        categoryComboBox.setValue(null);
        colorField.clear();
        sizeField.clear();
        costField.clear();
        priceField.clear();
        quantityField.clear();
        selectedImageFile = null; // Clear selected file reference
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING); // Consider using INFORMATION or ERROR types too
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
