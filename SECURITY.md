# Política de Segurança

## Versões Suportadas

| Versão  | Suporte          |
| ------- | ---------------- |
| 1.0.x   | :white_check_mark: |
| 0.9.x   | :x:                |
| < 0.9   | :x:                |

## Reportando Vulnerabilidades

1. **Não** abra uma issue pública para vulnerabilidades de segurança
2. Envie um email para security@atualle.com.br com:
   - Descrição do problema
   - Passos para reproduzir
   - Possível impacto
   - Sugestão de correção (opcional)

## Processo de Resposta

1. Confirmação de recebimento em até 24h
2. Avaliação inicial em até 72h
3. Plano de correção em até 7 dias
4. Patch de segurança conforme severidade:
   - Crítica: 24h
   - Alta: 72h
   - Média: 7 dias
   - Baixa: próxima release

## Práticas de Segurança

### Senhas
- Armazenamento com BCrypt
- Mínimo 8 caracteres
- Complexidade obrigatória
- Expiração em 90 dias

### Dados
- Backup diário
- Criptografia em repouso
- Logs de auditoria
- Sanitização de inputs

### Acesso
- RBAC (Role-Based Access Control)
- Timeout de sessão
- Bloqueio após 3 tentativas
- 2FA para admins

### Código
- Análise estática (SonarQube)
- Dependências atualizadas
- Code review obrigatório
- Testes de segurança

## Atualizações de Segurança

1. Patches distribuídos via GitHub Releases
2. Changelog detalhado de correções
3. Instruções de atualização
4. Verificação de integridade (SHA-256)

## Recompensas

Agradecemos reportes de vulnerabilidades com:

- Créditos no CHANGELOG
- Menção na documentação
- Certificado de contribuição

## Escopo

Incluído:
- Código-fonte
- Banco de dados
- API
- Interface web

Excluído:
- Configurações locais
- Dados de teste
- Documentação
- Issues públicas

## Contato

- Email: security@atualle.com.br
- PGP Key: [security.asc](https://atualle.com.br/security.asc)
- Telegram: @atuallesec

## Links Úteis

- [Atualizações de Segurança](https://atualle.com.br/security/updates)
- [Guia de Configuração Segura](https://atualle.com.br/security/setup)
- [FAQ de Segurança](https://atualle.com.br/security/faq)
