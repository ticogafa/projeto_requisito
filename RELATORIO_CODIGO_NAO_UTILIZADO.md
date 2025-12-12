# Relatório de Código Não Utilizado e Inconsistências

Este relatório identifica partes do código backend que parecem não estar sendo utilizadas, bem como endpoints que existem no backend mas não são chamados pelo frontend atual.

## 1. Endpoints Backend Não Chamados pelo Frontend

Com base na análise dos controladores Java (`@RestController`) e do código Frontend (`MainService.ts`, `URLConstants.ts`), os seguintes endpoints parecem não ter uso ativo na aplicação cliente principal:

### A. Endpoints de Desenvolvimento/Teste
*   **Controller:** `DevController.java`
*   **Path:** `/api/dev/seed-agendamentos`
*   **Path:** `/api/dev/test-profissionais-disponiveis`
*   **Status:** Estes endpoints existem para facilitar o desenvolvimento (popular banco, testar queries), mas não devem estar expostos em produção. Não há chamadas para eles no frontend "vivo" (exceto talvez em comentários ou scripts manuais).

### B. Endpoints de Monitoramento
*   **Controller:** `HealthController.java`
*   **Path:** `/api/health`
*   **Status:** Útil para DevOps/Monitoramento (Health Check), mas não integrado à interface do usuário.

### C. Funcionalidade de Relatórios (Desativada?)
*   **Controller:** `RelatorioController.java`
*   **Status:** O código da classe está **comentado** (`// @RestController`).
*   **Consequência:** Embora a lógica de negócio (Serviços e Decorators) exista e esteja configurada em `RelatorioConfig.java`, não há "porta de entrada" HTTP ativa para essa funcionalidade. O frontend não tem como acessar esses relatórios.

---

## 2. Classes e Componentes Potencialmente Não Utilizados

### A. Módulo de Relatórios (`com.cesarschool.barbearia.dominio.principal.profissional.relatorio`)
Como o `RelatorioController` está comentado, toda a cadeia de classes abaixo é instanciada pelo Spring (via `RelatorioConfig`), mas nunca executada por uma requisição externa:
*   `IGeradorRelatorio.java`
*   `RelatorioDesempenhoServico.java`
*   `GeradorRelatorioDecorator.java`
*   `GeradorRelatorioLoggingDecorator.java`
*   `ValidadorDataRelatorioDecorator.java`

*Recomendação:* Se a funcionalidade de relatórios não for ser implementada agora, essas classes e a configuração `RelatorioConfig` são código morto consumindo memória na inicialização.

### B. Classes de Demonstração
*   **Arquivo:** `DemonstradorProxy.java`
*   **Status:** É um `CommandLineRunner` usado para demonstrar o padrão Proxy no console ao iniciar a aplicação.
*   **Uso:** Apenas educativo/demonstrativo. Em um ambiente de produção real, não teria utilidade prática.

### C. Arquivos de Mock em Produção?
A análise listou vários arquivos com nome `MockRepositorio` dentro da pasta `src/test/java`. Isso está **correto** (código de teste deve ficar em `test`). No entanto, vale a pena verificar se não há dependências de mocks vazando para o código principal (não detectado nesta varredura rápida).

## 3. Conclusão

A aplicação está relativamente limpa, com a maior parte dos controladores sendo consumidos pelo frontend. O principal ponto de atenção é o **Módulo de Relatórios**, que está "ligado" no backend (Beans criados) mas "desconectado" do mundo externo (Controller comentado).

**Ações Sugeridas:**
1.  Avaliar a remoção ou ativação definitiva do `RelatorioController`.
2.  Remover `DevController` em builds de produção (usando `@Profile("dev")`).
3.  Manter `HealthController` para monitoramento de infraestrutura.
