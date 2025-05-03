package com.atualle.dao;

import com.atualle.model.Category;
import com.atualle.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private final Connection connection;

    public ProductDAO() throws SQLException {
        this.connection = Database.getConnection();
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "photo TEXT," +
                "sku TEXT," +
                "name TEXT NOT NULL," +
                "category_id INTEGER," +
                "color TEXT," +
                "size TEXT," +
                "cost REAL," +
                "price REAL," +
                "quantity INTEGER," +
                "FOREIGN KEY(category_id) REFERENCES categories(id)" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void insert(Product product) throws SQLException {
        String sql = "INSERT INTO products (photo, sku, name, category_id, color, size, cost, price, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, product.getPhoto());
            pstmt.setString(2, product.getSku());
            pstmt.setString(3, product.getName());
            pstmt.setObject(4, product.getCategory() != null ? product.getCategory().getId() : null);
            pstmt.setString(5, product.getColor());
            pstmt.setString(6, product.getSize());
            pstmt.setDouble(7, product.getCost());
            pstmt.setDouble(8, product.getPrice());
            pstmt.setInt(9, product.getQuantity());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(Product product) throws SQLException {
        String sql = "UPDATE products SET photo=?, sku=?, name=?, category_id=?, color=?, size=?, cost=?, price=?, quantity=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getPhoto());
            pstmt.setString(2, product.getSku());
            pstmt.setString(3, product.getName());
            pstmt.setObject(4, product.getCategory() != null ? product.getCategory().getId() : null);
            pstmt.setString(5, product.getColor());
            pstmt.setString(6, product.getSize());
            pstmt.setDouble(7, product.getCost());
            pstmt.setDouble(8, product.getPrice());
            pstmt.setInt(9, product.getQuantity());
            pstmt.setInt(10, product.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.id, p.photo, p.sku, p.name, p.category_id, p.color, p.size, p.cost, p.price, p.quantity, c.name as category_name " +
                "FROM products p LEFT JOIN categories c ON p.category_id = c.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Category category = null;
                int categoryId = rs.getInt("category_id");
                if (!rs.wasNull()) {
                    category = new Category(categoryId, rs.getString("category_name"));
                }
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("photo"),
                        rs.getString("sku"),
                        rs.getString("name"),
                        category,
                        rs.getString("color"),
                        rs.getString("size"),
                        rs.getDouble("cost"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
                products.add(product);
            }
        }
        return products;
    }

    public Product findBySKU(String sku) throws SQLException {
        String sql = "SELECT p.id, p.photo, p.sku, p.name, p.category_id, p.color, p.size, p.cost, p.price, p.quantity, c.name as category_name " +
                "FROM products p LEFT JOIN categories c ON p.category_id = c.id WHERE p.sku = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, sku);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Category category = null;
                    int categoryId = rs.getInt("category_id");
                    if (!rs.wasNull()) {
                        category = new Category(categoryId, rs.getString("category_name"));
                    }
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("photo"),
                            rs.getString("sku"),
                            rs.getString("name"),
                            category,
                            rs.getString("color"),
                            rs.getString("size"),
                            rs.getDouble("cost"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    );
                }
            }
        }
        return null;
    }
}
