package com.atualle.dao;

import com.atualle.model.Category;
import com.atualle.model.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDAOTest {

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private Category testCategory;

    @BeforeAll
    public void setup() throws SQLException {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        testCategory = new Category(0, "Test Category");
        categoryDAO.insert(testCategory);
    }

    @AfterAll
    public void cleanup() throws SQLException {
        categoryDAO.delete(testCategory.getId());
    }

    @Test
    public void testInsertAndFind() throws SQLException {
        Product product = new Product(0, "photo.jpg", "SKU123", "Test Product", testCategory, "Red", "M", 10.0, 20.0, 5);
        productDAO.insert(product);

        List<Product> products = productDAO.findAll();
        assertTrue(products.stream().anyMatch(p -> p.getSku().equals("SKU123")));

        productDAO.delete(product.getId());
    }

    @Test
    public void testUpdate() throws SQLException {
        Product product = new Product(0, "photo.jpg", "SKU124", "Test Product 2", testCategory, "Blue", "L", 15.0, 25.0, 10);
        productDAO.insert(product);

        product.setName("Updated Product");
        product.setPrice(30.0);
        productDAO.update(product);

        Product updated = productDAO.findBySKU(product.getSku());
        assertEquals("Updated Product", updated.getName());
        assertEquals(30.0, updated.getPrice());

        productDAO.delete(product.getId());
    }

    @Test
    public void testDelete() throws SQLException {
        Product product = new Product(0, "photo.jpg", "SKU125", "Test Product 3", testCategory, "Green", "S", 12.0, 22.0, 8);
        productDAO.insert(product);

        productDAO.delete(product.getId());

        Product deleted = productDAO.findBySKU(product.getSku());
        assertNull(deleted);
    }
}
