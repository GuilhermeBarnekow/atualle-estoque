# Atualle Estoque

Sistema de gerenciamento de estoque desenvolvido em JavaFX para controle de produtos, 
categorias e movimentações.

## Documentação

- [Manual do Usuário](docs/Manual.md) - Guia completo de uso do sistema
- [Documentação Técnica](docs/Tecnico.md) - Detalhes de implementação e arquitetura

## 1. Visão Geral

O Atualle Estoque resolve o problema de controle de estoque para pequenas e médias 
empresas. Desenvolvido com JavaFX e SQLite, oferece:

- Cadastro de produtos com fotos e categorias
- Controle de entrada/saída de estoque
- Alertas de baixo estoque
- Relatórios em PDF
- Interface moderna e responsiva
- Controle de acesso por usuário

Público-alvo: gestores e operadores de estoque que precisam de uma solução leve e 
prática para controle de produtos.

## 2. Arquitetura MVC

```
[View (FXML)] <--> [Controller] <--> [DAO] <--> [Model]
     |                  |             |          |
  Interface        Regras de      Acesso ao    Entidades
   do User         Negócio         Banco      de Negócio
```

### Models
- Product: produto com foto, SKU, preço, quantidade
- Category: agrupamento de produtos
- StockMovement: registros de entrada/saída
- User: controle de acesso (admin/operator)

### DAOs 
- Camada de persistência usando SQLite
- Operações CRUD por entidade
- Queries otimizadas com índices

### Controllers
- Separação por funcionalidade (produtos, categorias, etc)
- Validações de entrada
- Controle de estado da UI

### Views
- Layouts FXML com CSS moderno
- Componentes JavaFX reutilizáveis
- Feedback visual para o usuário

## 3. Estrutura de Pastas

```
src/
├── main/
│   ├── java/com/atualle/
│   │   ├── controller/     # Controladores MVC
│   │   ├── dao/           # Acesso a dados
│   │   ├── model/         # Entidades
│   │   └── util/          # Classes utilitárias
│   └── resources/
│       ├── fxml/          # Layouts da interface
│       ├── css/           # Estilos da aplicação
│       └── icons/         # Ícones e imagens
└── test/
    └── java/com/atualle/  # Testes unitários
```

## 4. Build e Execução

Pré-requisitos:
- Java 17+
- Maven 3.9+
- SQLite 3

Comandos:

```bash
# Build
mvn clean package

# Executar
mvn javafx:run

# Testes
mvn test
```

## 5. Próximas Versões

v1.1:
- API REST para integração
- Dashboard com gráficos
- Backup automático

v2.0:
- App mobile React Native
- Sincronização em tempo real
- Multi-empresa

## Contribuição

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
