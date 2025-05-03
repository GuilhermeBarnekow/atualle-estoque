package com.atualle.dao;

import com.atualle.model.Category;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryDAOTest {

    private CategoryDAO categoryDAO;
    private Category testCategory;

    @BeforeAll
    public void setup() throws SQLException {
        categoryDAO = new CategoryDAO();
        testCategory = new Category(0, "Test Category");
        categoryDAO.insert(testCategory);
    }

    @AfterAll
    public void cleanup() throws SQLException {
        if (testCategory != null && testCategory.getId() != 0) {
            categoryDAO.delete(testCategory.getId());
        }
    }

    @Test
    public void testInsertAndFind() throws SQLException {
        Category category = new Category(0, "New Category");
        categoryDAO.insert(category);

        List<Category> categories = categoryDAO.findAll();
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("New Category")));

        categoryDAO.delete(category.getId());
    }

    @Test
    public void testUpdate() throws SQLException {
        Category category = new Category(0, "Update Category");
        categoryDAO.insert(category);

        category.setName("Updated Category");
        categoryDAO.update(category);

        Category updated = categoryDAO.findById(category.getId());
        assertEquals("Updated Category", updated.getName());

        categoryDAO.delete(category.getId());
    }

    @Test
    public void testDelete() throws SQLException {
        Category category = new Category(0, "Delete Category");
        categoryDAO.insert(category);

        categoryDAO.delete(category.getId());

        Category deleted = categoryDAO.findById(category.getId());
        assertNull(deleted);
    }
}
