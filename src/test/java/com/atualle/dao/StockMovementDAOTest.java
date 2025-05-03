package com.atualle.dao;

import com.atualle.model.Category;
import com.atualle.model.StockMovement;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockMovementDAOTest {

    private StockMovementDAO stockMovementDAO;
    private CategoryDAO categoryDAO;
    private Category testCategory;

    @BeforeAll
    public void setup() throws SQLException {
        stockMovementDAO = new StockMovementDAO();
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
        StockMovement movement = new StockMovement(0, "Entrada", java.time.LocalDateTime.now(), "admin", 10, null);
        stockMovementDAO.insert(movement);

        List<StockMovement> movements = stockMovementDAO.findAll();
        assertTrue(movements.stream().anyMatch(m -> m.getId() == movement.getId()));

        stockMovementDAO.delete(movement.getId());
    }

    @Test
    public void testUpdate() throws SQLException {
        StockMovement movement = new StockMovement(0, "Saída", java.time.LocalDateTime.now(), "admin", 5, null);
        stockMovementDAO.insert(movement);

        movement.setQuantity(7);
        stockMovementDAO.update(movement);

        StockMovement updated = stockMovementDAO.findById(movement.getId());
        assertEquals(7, updated.getQuantity());

        stockMovementDAO.delete(movement.getId());
    }

    @Test
    public void testDelete() throws SQLException {
        StockMovement movement = new StockMovement(0, "Ajuste", java.time.LocalDateTime.now(), "admin", 3, null);
        stockMovementDAO.insert(movement);

        stockMovementDAO.delete(movement.getId());

        StockMovement deleted = stockMovementDAO.findById(movement.getId());
        assertNull(deleted);
    }
}
