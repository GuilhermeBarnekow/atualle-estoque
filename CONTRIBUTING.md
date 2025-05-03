# Guia de Contribuição

## Fluxo de Trabalho

1. Fork do repositório
2. Clone local
3. Crie uma branch para sua feature
4. Commit das alterações
5. Push para seu fork
6. Pull Request

## Padrões de Código

### Commits

- Mensagens em português
- Verbo no imperativo
- Primeira linha <= 50 caracteres
- Descrição detalhada após linha em branco

Exemplo:
```
Adiciona validação de SKU duplicado

- Implementa verificação no ProductDAO
- Adiciona mensagem de erro na UI
- Atualiza testes unitários
```

### Java

- Indentação: 4 espaços
- Nomes em inglês
- Javadoc em português
- Máximo 100 caracteres por linha

```java
/**
 * Valida produto antes de salvar.
 * 
 * @param product Produto a ser validado
 * @throws ValidationException Se houver campos inválidos
 */
public void validate(Product product) throws ValidationException {
    if (product.getSku() == null || product.getSku().isEmpty()) {
        throw new ValidationException("SKU é obrigatório");
    }
}
```

### FXML

- IDs em camelCase
- Comentários para seções
- Um componente por linha

```xml
<!-- Formulário de Produto -->
<GridPane hgap="10" vgap="10">
    <Label text="SKU:" 
           GridPane.rowIndex="0" 
           GridPane.columnIndex="0"/>
    <TextField fx:id="skuField"
               GridPane.rowIndex="0" 
               GridPane.columnIndex="1"/>
</GridPane>
```

### CSS

- Classes em kebab-case
- Propriedades agrupadas
- Comentários por seção

```css
/* Botões principais */
.primary-button {
    /* Layout */
    -fx-padding: 8px 16px;
    -fx-min-width: 100px;
    
    /* Visual */
    -fx-background-color: #3498db;
    -fx-text-fill: white;
}
```

## Testes

### Unitários

- Um assert por teste
- Nomes descritivos
- Setup em @BeforeEach

```java
@Test
void shouldNotSaveProductWithDuplicateSku() {
    Product product = new Product();
    product.setSku("123");
    
    assertThrows(ValidationException.class, () -> {
        productDAO.insert(product);
    });
}
```

### Integração

- Rollback após testes
- Dados realistas
- Testa fluxo completo

## Pull Requests

### Template

```markdown
## Descrição
Breve descrição das alterações

## Tipo de Mudança
- [ ] Bug fix
- [ ] Nova feature
- [ ] Breaking change
- [ ] Documentação

## Checklist
- [ ] Testes adicionados
- [ ] Documentação atualizada
- [ ] Build passa localmente
```

### Review

- Máximo 400 linhas por PR
- Screenshots para mudanças visuais
- Todos testes passando

## Documentação

### Código

- Javadoc em classes públicas
- Comentários em lógicas complexas
- README por pacote

### Usuário

- Screenshots atualizados
- Exemplos práticos
- Passo a passo

## Build

1. Instale dependências:
```bash
mvn clean install
```

2. Rode testes:
```bash
mvn test
```

3. Build local:
```bash
mvn package
```

## Dúvidas

- Abra uma issue
- Use labels apropriadas
- Seja específico
