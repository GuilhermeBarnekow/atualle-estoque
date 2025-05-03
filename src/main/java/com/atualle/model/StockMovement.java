package com.atualle.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class StockMovement {
    private final IntegerProperty id;
    private final StringProperty type;
    private final ObjectProperty<LocalDateTime> date;
    private final StringProperty user;
    private final IntegerProperty quantity;
    private final ObjectProperty<Product> product;

    public StockMovement(int id, String type, LocalDateTime date, String user, int quantity, Product product) {
        this.id = new SimpleIntegerProperty(id);
        this.type = new SimpleStringProperty(type);
        this.date = new SimpleObjectProperty<>(date);
        this.user = new SimpleStringProperty(user);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.product = new SimpleObjectProperty<>(product);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public LocalDateTime getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    public String getUser() {
        return user.get();
    }

    public StringProperty userProperty() {
        return user;
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public Product getProduct() {
        return product.get();
    }

    public ObjectProperty<Product> productProperty() {
        return product;
    }

    public void setProduct(Product product) {
        this.product.set(product);
    }
}
