package com.atualle.dao;

import com.atualle.model.Product;
import com.atualle.model.StockMovement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockMovementDAO {

    private final Connection connection;

    public StockMovementDAO() throws SQLException {
        this.connection = Database.getConnection();
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS stock_movements (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "user TEXT," +
                "quantity INTEGER NOT NULL," +
                "product_id INTEGER," +
                "FOREIGN KEY(product_id) REFERENCES products(id)" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void insert(StockMovement movement) throws SQLException {
        String sql = "INSERT INTO stock_movements (type, date, user, quantity, product_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, movement.getType());
            pstmt.setString(2, movement.getDate().toString());
            pstmt.setString(3, movement.getUser());
            pstmt.setInt(4, movement.getQuantity());
            pstmt.setObject(5, movement.getProduct() != null ? movement.getProduct().getId() : null);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movement.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(StockMovement movement) throws SQLException {
        String sql = "UPDATE stock_movements SET type=?, date=?, user=?, quantity=?, product_id=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, movement.getType());
            pstmt.setString(2, movement.getDate().toString());
            pstmt.setString(3, movement.getUser());
            pstmt.setInt(4, movement.getQuantity());
            pstmt.setObject(5, movement.getProduct() != null ? movement.getProduct().getId() : null);
            pstmt.setInt(6, movement.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM stock_movements WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<StockMovement> findAll() throws SQLException {
        List<StockMovement> movements = new ArrayList<>();
        String sql = "SELECT sm.id, sm.type, sm.date, sm.user, sm.quantity, sm.product_id, p.name as product_name " +
                "FROM stock_movements sm LEFT JOIN products p ON sm.product_id = p.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = null;
                int productId = rs.getInt("product_id");
                if (!rs.wasNull()) {
                    product = new Product(productId, "", "", rs.getString("product_name"), null, "", "", 0, 0, 0);
                }
                StockMovement movement = new StockMovement(
                        rs.getInt("id"),
                        rs.getString("type"),
                        java.time.LocalDateTime.parse(rs.getString("date")),
                        rs.getString("user"),
                        rs.getInt("quantity"),
                        product
                );
                movements.add(movement);
            }
        }
        return movements;
    }

    public StockMovement findById(int id) throws SQLException {
        String sql = "SELECT sm.id, sm.type, sm.date, sm.user, sm.quantity, sm.product_id, p.name as product_name " +
                "FROM stock_movements sm LEFT JOIN products p ON sm.product_id = p.id WHERE sm.id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Product product = null;
                    int productId = rs.getInt("product_id");
                    if (!rs.wasNull()) {
                        product = new Product(productId, "", "", rs.getString("product_name"), null, "", "", 0, 0, 0);
                    }
                    return new StockMovement(
                            rs.getInt("id"),
                            rs.getString("type"),
                            java.time.LocalDateTime.parse(rs.getString("date")),
                            rs.getString("user"),
                            rs.getInt("quantity"),
                            product
                    );
                }
            }
        }
        return null;
    }
}
