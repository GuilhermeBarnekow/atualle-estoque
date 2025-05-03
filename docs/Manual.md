# Manual do Usuário - Atualle Estoque

## Acesso ao Sistema

1. Login
- Usuário admin: acesso total
- Usuário operator: somente movimentações

```
Admin:     admin / admin123
Operador:  operator / operator123
```

## Produtos

### Cadastro de Produto

1. Clique em "Produtos" no menu lateral
2. Clique em "Adicionar"
3. Preencha:
   - SKU (código único)
   - Nome
   - Categoria
   - Cor e Tamanho (opcional)
   - Custo e Preço
   - Quantidade inicial
4. Clique em "Salvar"

### Busca

- Use o campo "Buscar" para filtrar por:
  - SKU
  - Nome
  - Cor
  - Tamanho

### Edição

1. Selecione o produto na tabela
2. Altere os campos necessários
3. Clique em "Atualizar"

## Categorias

### Nova Categoria

1. Acesse "Categorias"
2. Preencha:
   - Nome
   - Descrição (opcional)
3. Clique em "Adicionar"

### Gerenciamento

- Edição: selecione e atualize
- Exclusão: somente categorias sem produtos

## Movimentações

### Entrada de Estoque

1. Acesse "Movimentações"
2. Selecione tipo "Entrada"
3. Informe:
   - Data
   - Quantidade
   - Observação (opcional)
4. Confirme

### Saída de Estoque

Similar à entrada, mas:
- Tipo "Saída"
- Sistema valida quantidade disponível

### Ajustes

Use para correções:
1. Tipo "Ajuste"
2. Quantidade positiva (aumenta) ou negativa (diminui)
3. Registre motivo na observação

## Relatórios

### Estoque Atual

1. Menu "Relatórios"
2. Tipo "Estoque Atual"
3. Filtros disponíveis:
   - Categoria
   - Nível de estoque
   - Ordenação

### Movimentações

1. Selecione período
2. Escolha tipo(s)
3. Opções de agrupamento:
   - Por dia
   - Por produto
   - Por categoria

### Exportação

- PDF: relatórios formatados
- CSV: dados para Excel

## Alertas

Sistema notifica:
1. Estoque baixo (< mínimo)
2. Produto sem movimentação (30 dias)
3. Divergências de saldo

## Atalhos de Teclado

```
Ctrl + P    Produtos
Ctrl + C    Categorias
Ctrl + M    Movimentações
Ctrl + R    Relatórios
ESC         Limpa formulário
Enter       Confirma operação
```

## Dicas

1. Estoque
- Faça contagem física mensal
- Registre todas movimentações
- Documente ajustes

2. Cadastros
- SKU único e significativo
- Fotos padronizadas
- Descrições claras

3. Relatórios
- Exporte backups semanais
- Revise alertas diariamente
- Analise tendências mensais

## Problemas Comuns

1. Produto não cadastra
- Verifique SKU duplicado
- Campos obrigatórios
- Formato de valores

2. Saldo incorreto
- Confira última contagem
- Revise movimentações
- Ajuste se necessário

3. Relatório não gera
- Filtros conflitantes
- Período muito extenso
- Tente exportar por partes

## Boas Práticas

1. Cadastros
- Padronize nomenclatura
- Use categorias adequadas
- Mantenha fotos atualizadas

2. Operação
- Registre movimentações na hora
- Documente ajustes
- Faça backup frequente

3. Análise
- Acompanhe giro de estoque
- Monitore custos
- Planeje reposições
