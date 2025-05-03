# Documentação Técnica - Atualle Estoque

## Componentes da Interface

### 1. Login

```java
// Autenticação com roles
public class LoginController {
    private Map<String, User> users = new HashMap<>();
    
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            openMainApp(user.role);
        }
    }
}
```

### 2. Produtos

Campos principais:
- SKU (único)
- Nome
- Categoria (FK)
- Foto
- Cor/Tamanho
- Custo/Preço
- Quantidade

Validações:
- SKU não pode duplicar
- Preço > Custo
- Quantidade >= 0

### 3. Categorias

Estrutura simples:
- ID
- Nome
- Descrição

Relacionamento:
- One-to-Many com Produtos

### 4. Movimentações

Tipos:
- Entrada
- Saída
- Ajuste

Campos:
- Tipo
- Produto
- Quantidade
- Data
- Usuário

## Banco de Dados

### Índices

```sql
-- Otimização de buscas
CREATE INDEX idx_product_sku ON products(sku);
CREATE INDEX idx_product_category ON products(category_id);
CREATE INDEX idx_movement_date ON stock_movements(date);
```

### Triggers

```sql
-- Atualiza estoque após movimentação
CREATE TRIGGER update_stock
AFTER INSERT ON stock_movements
BEGIN
    UPDATE products 
    SET quantity = quantity + NEW.quantity 
    WHERE id = NEW.product_id;
END;
```

## Padrões de Projeto

### 1. DAO

```java
public interface DAO<T> {
    void insert(T entity);
    void update(T entity);
    void delete(int id);
    T findById(int id);
    List<T> findAll();
}

public class ProductDAO implements DAO<Product> {
    private final Connection conn;
    
    @Override
    public void insert(Product product) {
        String sql = "INSERT INTO products (sku, name) VALUES (?, ?)";
        // Implementação...
    }
}
```

### 2. Factory

```java
public class DAOFactory {
    private static final Connection conn = Database.getConnection();
    
    public static ProductDAO createProductDAO() {
        return new ProductDAO(conn);
    }
    
    public static CategoryDAO createCategoryDAO() {
        return new CategoryDAO(conn);
    }
}
```

## Testes

### Unitários

```java
@Test
void shouldValidateProduct() {
    Product product = new Product();
    product.setPrice(10.0);
    product.setCost(20.0);
    
    assertThrows(ValidationException.class, () -> {
        productDAO.insert(product);
    });
}
```

### Integração

```java
@Test
void shouldUpdateStockOnMovement() {
    Product product = new Product();
    product.setQuantity(10);
    productDAO.insert(product);
    
    StockMovement movement = new StockMovement();
    movement.setProduct(product);
    movement.setQuantity(-5);
    stockMovementDAO.insert(movement);
    
    Product updated = productDAO.findById(product.getId());
    assertEquals(5, updated.getQuantity());
}
```

## Logs

Formato JSON para fácil parsing:

```json
{
  "timestamp": "2024-05-02T10:15:30",
  "level": "INFO",
  "class": "ProductController",
  "method": "handleAddProduct",
  "message": "Produto adicionado",
  "data": {
    "sku": "123",
    "name": "Produto Teste",
    "user": "admin"
  }
}
```

## Performance

### Otimizações

1. Carregamento lazy de imagens
2. Cache de produtos frequentes
3. Paginação nas listagens
4. Índices no banco
5. Pool de conexões

### Monitoramento

Métricas principais:
- Tempo de resposta do banco
- Uso de memória
- Operações por segundo
- Erros por minuto

## Segurança

1. Senhas hasheadas (BCrypt)
2. Validação de entrada
3. Escape de SQL
4. Controle de acesso por role
5. Logs de auditoria

## Build

Maven profiles:

```xml
<profiles>
    <profile>
        <id>dev</id>
        <properties>
            <db.url>jdbc:sqlite:dev.db</db.url>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <db.url>jdbc:sqlite:/opt/atualle/prod.db</db.url>
        </properties>
    </profile>
</profiles>
```

## Troubleshooting

Problemas comuns:

1. Banco locked
```bash
# Solução: Fechar conexões pendentes
lsof | grep atualle.db
kill -9 <PID>
```

2. Out of Memory
```bash
# Aumentar heap
java -Xmx1024m -jar atualle.jar
```

3. UI não responde
- Verificar operações pesadas na thread principal
- Usar Platform.runLater() para UI
- Implementar paginação
