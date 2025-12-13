# Padrões de Projeto Adotados

Este documento lista todos os padrões de projeto (Design Patterns) implementados no projeto Barbearia Backend, detalhando as classes criadas e/ou modificadas para cada padrão.

---

## 1. Padrão PROXY (Estrutural)

### Descrição
O padrão **Proxy** fornece um substituto ou placeholder para outro objeto, controlando o acesso ao objeto original. No projeto, implementamos um **Virtual Proxy com Lazy Loading** para otimizar a performance das operações de repositório, adiando o carregamento de dados do banco de dados até que sejam realmente necessários.

### Objetivo
Implementar Lazy Loading transparente entre o cliente e o repositório real, economizando recursos ao carregar dados SOB DEMANDA sem modificar o código cliente.

### Classes Criadas

#### 1. `ProdutoRepositorioVirtualProxy.java`
- **Pacote:** `com.cesarschool.barbearia.infraestrutura.proxy`
- **Tipo:** Virtual Proxy com Lazy Loading
- **Responsabilidade:** Adia o carregamento de dados até que sejam realmente necessários (lazy loading)
- **Características:**
  - Implementa a interface `ProdutoRepositorio` (mesma interface do Real Subject)
  - Usa composição: contém uma referência ao Real Subject (`ProdutoRepositorioJpa`)
  - **Lazy Initialization:** Carrega dados SOB DEMANDA
  - Cache thread-safe com `ConcurrentHashMap` para dados já carregados
  - Invalidação seletiva em operações de escrita (preserva outros produtos carregados)
  - Rastreia estatísticas: Lazy Loads vs Reuso
  - Anotado com `@Primary` para injeção de dependência automática
- **Linhas de código:** ~336 linhas
- **Métodos principais:**
  - `buscarPorId()`: Lazy loading com verificação de carregamento prévio
  - `buscarTodos()`: Lazy loading de lista completa
  - `buscarProdutosComEstoqueBaixo()`: Lazy loading de lista filtrada
  - `salvar()`: Delega e invalida seletivamente
  - `excluir()`: Delega e invalida seletivamente
  - `limparDadosCarregados()`: Limpa todos os dados carregados
  - `getEstatisticas()`: Retorna métricas de lazy loading

#### 2. `DemonstradorProxy.java`
- **Pacote:** `com.cesarschool.barbearia`
- **Tipo:** Demonstrador / Cliente do Proxy
- **Responsabilidade:** Demonstra o funcionamento do Virtual Proxy através de cenários práticos
- **Características:**
  - Implementa `CommandLineRunner` para execução automática
  - Perfil Spring `@Profile("demo")` para execução isolada
  - 8 cenários de teste demonstrando Lazy Loading vs Reuso
  - Logs visuais com emojis (🟣 Virtual Proxy, 🔵 Real Subject)
  - Pausas interativas entre testes
  - Exibe estatísticas finais (lazy loads vs reuso)
- **Linhas de código:** ~330 linhas
- **Cenários de teste:**
  1. Cadastrar produto (invalidação seletiva)
  2. Primeira busca por ID (LAZY LOAD - acessa BD)
  3. Segunda busca por ID (REUSO - já carregado)
  4. Terceira busca por ID (REUSO - já carregado)
  5. Listar todos os produtos (LAZY LOAD - acessa BD)
  6. Listar todos novamente (REUSO - já carregado)
  7. Atualizar produto (invalidação seletiva)
  8. Exibir estatísticas finais

### Classes Modificadas

#### 1. `ProdutoRepositorioJpa.java` (antes: `ProdutoRepositorioImpl.java`)
- **Pacote:** `com.cesarschool.barbearia.infraestrutura.persistencia`
- **Tipo:** Real Subject
- **Modificações:**
  - **Renomeação:** De `ProdutoRepositorioImpl` para `ProdutoRepositorioJpa`
  - **JavaDoc:** Adicionada documentação explicando o papel de "Real Subject" no padrão Proxy
  - **Logs:** Adicionados `System.out.println("🔵 [REAL SUBJECT] ...")` em todos os métodos para demonstração
  - **Bean naming:** Adicionado `@Repository("produtoRepositorioJpa")` para identificação no Spring DI
- **Impacto:** Classe agora é explicitamente identificada como o Real Subject do padrão

#### 2. `ProdutoRepositorio.java`
- **Pacote:** `com.cesarschool.barbearia.dominio.principal.produto`
- **Tipo:** Subject (Interface)
- **Modificações:**
  - **JavaDoc:** Adicionada documentação explicando o papel de "Subject" no padrão Proxy
  - **Comentários:** Esclarecimento de que esta interface é implementada tanto pelo Proxy quanto pelo Real Subject
- **Impacto:** Interface agora documenta explicitamente seu papel no padrão

### Estrutura do Padrão

```
┌─────────────────┐
│     Cliente     │ (DemonstradorProxy, Controllers, Services)
│                 │
└────────┬────────┘
         │ usa
         ▼
┌─────────────────────────────┐
│   ProdutoRepositorio        │  ← Subject (Interface)
│   (interface)               │
└─────────────────────────────┘
         △
         │ implementa
         ├─────────────────────────┬──────────────────────────────┐
         │                         │                              │
┌────────────────────┐  ┌──────────────────────────────┐  ┌────────────────────┐
│ ProdutoRepositorioJpa│  │ProdutoRepositorioVirtualProxy│  │  (outros proxies) │
│   (Real Subject)   │  │    (Virtual Proxy)           │  │    possíveis       │
│                    │  │                              │  └────────────────────┘
│ - Acessa BD        │  │ - Lazy Loading               │
│ - JPA/Hibernate    │  │ - Dados sob demanda          │
│                    │◄─┤ - Invalidação seletiva       │
└────────────────────┘  │ - Rastreamento (reuso/loads) │
                        │ - @Primary                   │
                        └──────────────────────────────┘
```

### Benefícios Obtidos

1. **Performance e Economia de Recursos:**
   - Inicialização rápida (não carrega tudo de uma vez)
   - Economia de memória (só carrega o que é usado)
   - Dados já carregados são reutilizados instantaneamente
   - Redução significativa de operações I/O no banco de dados

2. **Lazy Loading Efetivo:**
   - Dados carregados SOB DEMANDA (apenas quando necessário)
   - Primeira chamada: LAZY LOAD (acessa BD)
   - Chamadas subsequentes: REUSO (não acessa BD)
   - Invalidação seletiva preserva outros dados carregados

3. **Transparência:**
   - Cliente não precisa saber que está usando Virtual Proxy
   - Spring DI injeta automaticamente o Proxy via `@Primary`
   - Mesma interface para Proxy e Real Subject
   - Comportamento lazy transparente para o usuário

4. **Thread Safety:**
   - Uso de `ConcurrentHashMap` para acesso concorrente
   - Seguro para uso em ambiente multi-thread
   - Sincronização automática de dados carregados

5. **Observabilidade:**
   - Logs detalhados de Lazy Loads vs Reuso
   - Estatísticas em tempo real (lazy load count vs reuso count)
   - Fácil depuração e monitoramento do padrão
   - Métricas acessíveis via API REST

### Como Executar a Demonstração

```bash
# Navegar até o diretório do projeto
cd barbearia-backend/dominio-principal

# Executar com perfil demo
mvn spring-boot:run -Dspring-boot.run.profiles=demo -Dmaven.test.skip=true
```
Virtual Proxy Statistics:
   Lazy Loads: 3 | Reuso: 4 | Total: 7
   Reuso Rate: 57,14%
   Dados Carregados: 1 produto + 1 lista

 ANÁLISE:
   • Primeira busca = LAZY LOAD (carrega do BD)
   • Buscas subsequentes = REUSO (não acessa BD)
   • Reuso rate > 50% = lazy loading efetivo
   • Operações de escrita invalidam seletivamente
   • Dados preservados são reutilizados automaticamente
   • Economia de recursos: só carrega o necessário
ANÁLISE:
   • Múltiplas buscas ao mesmo produto = Receita Gerada
   • Hit rate > 50% = cache está funcionando bem
   • Operações de escrita invalidam cache (garantem consistência)
   • PVirtual Proxy:** Substituto com Lazy Loading (`ProdutoRepositorioVirtualProxy`)
- **Composição:** Proxy HAS-A Real Subject (não usa herança)
- **Delegação:** Proxy delega para Real Subject APENAS quando necessário (lazy)
- **Lazy Initialization:** Dados carregados SOB DEMANDA
- **Controle:** Proxy adiciona lazy loading, invalidação seletiva e estatísticas
- **Transparência:** Cliente desconhece existência do Proxy
- **Economia de Recursos:** Evita carregar dados desnecessários
- **Subject:** Interface comum (`ProdutoRepositorio`)
- **Real Subject:** Implementação real (`ProdutoRepositorioJpa`)
- **Proxy:** Substituto com comportamento adicional (`ProdutoRepositorioCacheProxy`)
- **Composição:** Proxy HAS-A Real Subject (não usa herança)
- **Delegação:** Proxy delega para Real Subject quando necessário
- **Controle:** Proxy adiciona cache, invalidação e estatísticas
-Variante Implementada:** Virtual Proxy (Lazy Loading)
- **Outras Variantes:** Cache Proxy, Protection Proxy, Remote Proxy, Smart Reference

---

## Observações

- Este documento será atualizado conforme novos padrões de projeto forem implementados no sistema
- Data da última atualização: 12/12/2025
- Responsável pela implementação do Virtual Proxy: Tiago Gurgel
- **Nota Importante:** O proxy implementado é um **Virtual Proxy** (adiamento de carregamento), não um Cache Proxy tradicional. A distinção é importante pois Virtual Proxy foca em **lazy initialization** enquanto Cache Proxy foca em **reutilização de resultados já computados**.
- **Aplicabilidade:** Cache, Lazy Loading, Access Control, Logging, Remote Proxy

# Ver estatísticas em JSON
curl http://localhost:8080/api/proxy/statistics | jq .

# Ver estatísticas em texto
curl http://localhost:8080/api/proxy/statistics/text

# Ver informações do padrão
curl http://localhost:8080/api/proxy/info | jq .

# Limpar cache
curl -X DELETE http://localhost:8080/api/proxy/cache

# Resetar estatísticas
curl -X DELETE http://localhost:8080/api/proxy/statistics

---

## Observações

- Este documento será atualizado conforme novos padrões de projeto forem implementados no sistema
- Data da última atualização: 10/12/2025
- Responsável pela implementação do Proxy: Tiago Gurgel

## 2. Padrão DECORATOR (Estrutural) - Gestão de Caixa

### Descrição
O padrão **Decorator** foi aplicado na funcionalidade de caixa para adicionar validação de saldo sem alterar a implementação base do serviço. A cadeia de decorators envolve o serviço de caixa e bloqueia saídas que deixariam o saldo negativo.

### Objetivo
Impedir que o saldo do caixa fique menor que zero ao registrar saídas, mantendo o código do serviço base enxuto e permitindo novas responsabilidades em camadas futuras.

### Classes Criadas (Decorators)

- `GestaoCaixaDecorator` – abstração que repassa todas as chamadas para o próximo componente da cadeia, permitindo empilhar responsabilidades. Código em [barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/dominio/principal/cliente/caixa/GestaoCaixaDecorator.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/dominio/principal/cliente/caixa/GestaoCaixaDecorator.java#L1-L35).
- `ValidadorSaldoDecorator` – intercepta `registrarSaida` e lança `IllegalStateException` quando o valor da saída supera o saldo atual, impedindo saldo negativo. Código em [barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/dominio/principal/cliente/caixa/ValidadorSaldoDecorator.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/dominio/principal/cliente/caixa/ValidadorSaldoDecorator.java#L1-L22).

### Classes Modificadas / Consumidoras

- `DomainServicesConfig` – cria o bean `gestaoCaixaServico`, encadeando `GestaoCaixaServico` com `ValidadorSaldoDecorator`, para que qualquer injeção de `IGestaoCaixa` já receba a versão segura. Trecho em [barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/config/DomainServicesConfig.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/config/DomainServicesConfig.java#L148-L172).
- `CaixaControlador` – controlador REST que injeta `IGestaoCaixa`; ao registrar saídas ele passa automaticamente pelo decorator de validação, garantindo que o saldo não fique negativo. Código em [barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/apresentacao/caixa/CaixaControlador.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/apresentacao/caixa/CaixaControlador.java#L1-L64).

### Fluxo do Decorator na Gestão de Caixa

1. Controller chama `IGestaoCaixa.registrarSaida` com o valor informado.
2. `ValidadorSaldoDecorator` lê o saldo atual; se o valor exceder o saldo, lança exceção e impede o registro.
3. Caso contrário, delega para o serviço base (`GestaoCaixaServico`), que persiste o lançamento normalmente.

### Participantes

- **Componente base:** `IGestaoCaixa` + `GestaoCaixaServico` (registra lançamentos e calcula saldo).
- **Decorators:** `GestaoCaixaDecorator` (infraestrutura de encadeamento) e `ValidadorSaldoDecorator` (regra de saldo >= 0).
- **Cliente:** `CaixaControlador` consome `IGestaoCaixa` e obtém o comportamento adicional de forma transparente via bean configurado em `DomainServicesConfig`.

# Padrão Strategy - Sistema de Tratamento de Exceções

## Mapeamento para o Padrão GoF

### 1. **Strategy** (Interface/Contrato)

**Classe:** `ExceptionHandlerStrategy` (interface)

- Define a interface comum para todas as estratégias de tratamento de exceções
- Declara métodos que todas as concrete strategies devem implementar

### 2. **ConcreteStrategy** (Implementação Concreta)

**Classe:** `GenericExceptionHandlerStrategy` (class)

- Implementa o algoritmo de tratamento de exceção genérico
- Converte exceções em `ResponseEntity` HTTP
- Pode ser estendido com outras estratégias específicas no futuro

### 3. **Context** (Contexto de Uso)

**Classe:** `ExceptionHandler` (class)

- Mantém referência ao registry que fornece as strategies
- Delega o tratamento de exceções para a strategy apropriada
- Método `withHandler()` executa operações com tratamento automático

### 4. **Componentes Auxiliares**

#### **ExceptionRegistry** (Factory/Registry Pattern)
- Responsável por criar e fornecer a strategy apropriada
- Usa padrão Factory para instanciar strategies
- Mantém mapeamento de Exception → Strategy

#### **ExceptionEntry** (Value Object)
- Encapsula metadados de cada exceção registrada
- Associa tipo de exceção com sua strategy e HTTP status

---

## Diagrama de Sequência - Fluxo de Execução

![Diagrama Strategy](/DOCUMENTAÇÃO/PADROES/strategy.png)

---

## Benefícios do Padrão Strategy Nesta Implementação

### 1. **Open/Closed Principle**
- Aberto para extensão: Novas strategies podem ser adicionadas sem modificar código existente
- Fechado para modificação: O Context (`ExceptionHandler`) não precisa ser alterado

### 2. **Single Responsibility**
- Cada strategy é responsável por um tipo específico de tratamento
- ExceptionRegistry gerencia o mapeamento
- ExceptionHandler coordena o fluxo

### 3. **Flexibilidade**
- Diferentes exceções podem ter diferentes estratégias de tratamento
- HTTP status codes podem variar por tipo de exceção
- Fácil adicionar novos tipos de exceção

### 4. **Testabilidade**
- Strategies podem ser testadas isoladamente
- Mock strategies podem ser injetadas para testes
- Context pode ser testado independentemente

---

## Exemplo de Uso

```java
@RestController
public class ProdutoControlador {
    
    private final ExceptionHandler exceptionHandler;
    
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return exceptionHandler.withHandler(() -> {
            // Lógica que pode lançar exceções
            Produto produto = produtoServico.buscarPorId(new ProdutoId(id));
            return ResponseEntity.ok(mapeador.toResponse(produto));
        });
    }
}
```

### Fluxo:
1. Controller chama `withHandler()` passando uma lambda
2. ExceptionHandler executa a lambda dentro de try-catch
3. Se exceção ocorrer:
   - ExceptionHandler pede ao Registry a strategy apropriada
   - Registry cria `GenericExceptionHandlerStrategy` com status HTTP adequado
   - Strategy converte exceção em `ResponseEntity<Map<String, String>>`
4. Response é retornado ao cliente

---

## Extensibilidade Futura

### Adicionar Nova ConcreteStrategy

```java
public class ValidationExceptionHandlerStrategy implements ExceptionHandlerStrategy {
    private final ValidationException exception;
    private final HttpStatus status;
    
    public ValidationExceptionHandlerStrategy(ValidationException ex) {
        this.exception = ex;
        this.status = HttpStatus.UNPROCESSABLE_ENTITY;
    }
    
    @Override
    public ResponseEntity<Map<String, String>> toResponseEntity() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "ValidationError");
        body.put("message", exception.getMessage());
        body.put("field", exception.getFieldName());
        body.put("invalidValue", exception.getInvalidValue());
        body.put("statusCode", "422");
        body.put("timestamp", ZonedDateTime.now().toString());
        return ResponseEntity.status(status).body(body);
    }
    
    // ... outros métodos
}
```

### Registrar Nova Strategy

```java
@Component
public class ExceptionRegistry {
    
    public void configureDefaults() {
        // Registros existentes...
        
        // Nova strategy específica
        register(ValidationException.class, 
                 ValidationExceptionHandlerStrategy.class, 
                 HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
```

---

## Comparação com o Padrão GoF Original

| Componente GoF | Implementação | Responsabilidade |
|----------------|---------------|------------------|
| **Strategy** (interface) | `ExceptionHandlerStrategy` | Define contrato para tratamento |
| **ConcreteStrategy** | `GenericExceptionHandlerStrategy` | Implementa tratamento genérico |
| **Context** | `ExceptionHandler` | Coordena execução e delegação |
| **Cliente** | Controllers | Usa o Context para tratar erros |
| **(Extra) Factory** | `ExceptionRegistry` | Cria strategies apropriadas |

---

## Conclusão

A implementação segue fielmente o padrão **Strategy** do GoF, com adições de padrões complementares:

- **Strategy**: Para algoritmos intercambiáveis de tratamento
- **Factory**: Para criação de strategies (ExceptionRegistry)
- **Registry**: Para mapeamento de exceções
- **Value Object**: Para encapsular metadados (ExceptionEntry)

Esta arquitetura permite:
-Adicionar novos tipos de exceção sem alterar código existente
-Diferentes estratégias de serialização por tipo de erro
-Mapeamento flexível de HTTP status codes
-Código limpo, testável e manutenível

---

### Clientes do Padrão Strategy (Tratamento de Exceções)

O `ExceptionHandler` (Context) é injetado e utilizado por diversos controladores para garantir um tratamento de exceções consistente em toda a API. Abaixo estão exemplos reais de uso:

#### 1. AgendamentoControlador.java (Uso Principal)

**Pacote:** `com.cesarschool.barbearia.apresentacao.agendamento`
**Caminho:** [AgendamentoControlador.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/apresentacao/agendamento/AgendamentoControlador.java)

**Injeção do Context:**
```java
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoControlador {
    
    @Autowired
    private AgendamentoServicoAplicacao servicoAplicacao;
    
    @Autowired
    private ExceptionHandler exceptionHandler;  // ← Context do padrão Strategy
    
    // ... métodos
}
```

**Exemplo 1 - Criar Agendamento:**
```java
@PostMapping("/criar")
public ResponseEntity<AgendamentoResumo> criar(@RequestBody CriarAgendamentoRequest request) {
    return exceptionHandler.withHandler(() -> {
        logger.info("=== CRIAR AGENDAMENTO ===");
        logger.info("ClienteId: " + request.getClienteId());
        logger.info("ServicoId: " + request.getServicoId()); 
        logger.info("ProfissionalId: " + request.getProfissionalId());
        
        AgendamentoResumo agendamento = servicoAplicacao.criar(request);
        logger.info("Agendamento criado: " + agendamento.getId());
        
        return ResponseEntity.status(201).body(agendamento);
    });
}
```
- **Exceções tratadas:** `ClienteNaoEncontradoException`, `ServicoNaoEncontradoException`, `ProfissionalNaoEncontradoException`, `HorarioIndisponivelException`
- **Strategy aplicada:** `GenericExceptionHandlerStrategy` converte cada exceção em `ResponseEntity` com HTTP status apropriado
- **Resultado:** Cliente recebe JSON estruturado com `name`, `message`, `statusCode`, `timestamp`

**Exemplo 2 - Editar Agendamento:**
```java
@PutMapping("/{id}")
public ResponseEntity<AgendamentoResumo> editar(
        @PathVariable Integer id,
        @RequestBody EditarAgendamentoRequest request) {
    
    return exceptionHandler.withHandler(() -> {
        logger.info("Editando agendamento - ID: " + id + 
                   ", nova dataHora: " + request.getDataHora());
        
        AgendamentoResumo agendamento = servicoAplicacao.editar(id, request);
        
        logger.info("Agendamento editado com sucesso - ID: " + id);
        return ResponseEntity.ok(agendamento);
    });
}
```
- **Exceções tratadas:** `AgendamentoNaoEncontradoException`, `HorarioIndisponivelException`, `StatusInvalidoException`
- **Benefício:** Todas as exceções são capturadas e convertidas automaticamente pela strategy

**Exemplo 3 - Cancelar Agendamento:**
```java
@DeleteMapping("/{id}")
public ResponseEntity<AgendamentoResumo> cancelar(
        @PathVariable Integer id,
        @RequestParam(required = false) Integer clienteId,
        @RequestParam(required = false) Integer profissionalId) {
    
    return exceptionHandler.withHandler(() -> {
        logger.info("Cancelando agendamento - ID: " + id);
        
        if (clienteId != null) {
            AgendamentoResumo agendamento = servicoAplicacao.cancelar(id, clienteId, TipoUsuario.CLIENTE);
            return ResponseEntity.ok(agendamento);
        } 
        
        if (profissionalId != null) {
            AgendamentoResumo agendamento = servicoAplicacao.cancelar(id, profissionalId, TipoUsuario.PROFISSIONAL);
            return ResponseEntity.ok(agendamento);
        }
        
        throw new IllegalArgumentException("É necessário informar clienteId ou profissionalId");
    });
}
```
- **Exceções tratadas:** `AgendamentoNaoEncontradoException`, `OperacaoNaoPermitidaException`, `IllegalArgumentException`
- **Resultado:** Cada tipo de exceção recebe tratamento apropriado via Strategy

**Exemplo 4 - Listar Agendamentos por Cliente:**
```java
@GetMapping("/por-cliente")
public ResponseEntity<List<AgendamentoResumo>> listarPorCliente(@RequestParam Integer clienteId) {
    return exceptionHandler.withHandler(() -> {
        logger.info("Listando agendamentos do cliente: " + clienteId);
        
        List<AgendamentoResumo> agendamentos = servicoAplicacao.listarPorCliente(clienteId);
        
        logger.info("Encontrados " + agendamentos.size() + " agendamentos");
        return ResponseEntity.ok(agendamentos);
    });
}
```

**Exemplo 5 - Atualizar Status:**
```java
@PutMapping("/{id}/status")
public ResponseEntity<AgendamentoResumo> atualizarStatus(
        @PathVariable Integer id,
        @RequestParam String status) {
    
    return exceptionHandler.withHandler(() -> {
        logger.info("Atualizando status do agendamento " + id + " para " + status);
        
        StatusAgendamento statusEnum;
        try {
            statusEnum = StatusAgendamento.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
        
        AgendamentoResumo agendamento = servicoAplicacao.atualizarStatus(id, statusEnum);
        return ResponseEntity.ok(agendamento);
    });
}
```

#### 2. ServicoOferecidoControlador.java

**Caminho:** [ServicoOferecidoControlador.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/apresentacao/servico/ServicoOferecidoControlador.java)

**Exemplo de Uso:**
```java
@GetMapping
public ResponseEntity<List<ServicoOferecidoResponse>> listarTodos() {
    return exceptionHandler.withHandler(() -> {
        List<ServicoOferecidoResponse> servicos = servicoAplicacao.listarTodos();
        return ResponseEntity.ok(servicos);
    });
}

@PostMapping
public ResponseEntity<ServicoOferecidoResponse> criar(@RequestBody CriarServicoRequest request) {
    return exceptionHandler.withHandler(() -> {
        ServicoOferecidoResponse servico = servicoAplicacao.criar(request);
        return ResponseEntity.status(201).body(servico);
    });
}
```

#### 3. ProdutoControlador.java

**Caminho:** [ProdutoControlador.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/apresentacao/produto/ProdutoControlador.java)

**Exemplo de Uso:**
```java
@GetMapping("/{id}")
public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
    return exceptionHandler.withHandler(() -> {
        Produto produto = produtoServico.buscarPorId(new ProdutoId(id));
        return ResponseEntity.ok(mapeador.toResponse(produto));
    });
}
```

#### 4. ProfissionalControlador.java

**Caminho:** [ProfissionalControlador.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/apresentacao/profissional/ProfissionalControlador.java)

**Exemplo de Uso:**
```java
@PostMapping
public ResponseEntity<ProfissionalResponse> criar(@RequestBody CriarProfissionalRequest request) {
    return exceptionHandler.withHandler(() -> {
        ProfissionalResponse profissional = servicoAplicacao.criar(request);
        return ResponseEntity.status(201).body(profissional);
    });
}
```

#### 5. ProfissionalJornadaControlador.java

**Caminho:** [ProfissionalJornadaControlador.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/apresentacao/profissional/ProfissionalJornadaControlador.java)

**Exemplo de Uso:**
```java
@PostMapping("/{profissionalId}/jornada")
public ResponseEntity<Void> definirJornada(
        @PathVariable Integer profissionalId,
        @RequestBody DefinirJornadaRequest request) {
    
    return exceptionHandler.withHandler(() -> {
        servicoAplicacao.definirJornada(profissionalId, request);
        return ResponseEntity.ok().build();
    });
}
```

---

### Fluxo Completo em Agendamentos

**Cenário:** Cliente tenta criar agendamento em horário já ocupado

```
1. Request HTTP POST /api/agendamentos/criar
   ↓
2. AgendamentoControlador.criar()
   ↓
3. exceptionHandler.withHandler(() -> {...})  ← Context executa lambda
   ↓
4. servicoAplicacao.criar(request)
   ↓
5. AgendamentoServico.criar() - validação de horário
   ↓
6. Lança HorarioIndisponivelException("Profissional já possui agendamento neste horário")
   ↓
7. ExceptionHandler captura exceção (try-catch)
   ↓
8. registry.getStrategy(HorarioIndisponivelException.class)
   ↓
9. ExceptionRegistry busca strategy registrada
   ↓
10. Retorna GenericExceptionHandlerStrategy(ex, HttpStatus.CONFLICT)
    ↓
11. strategy.toResponseEntity() cria resposta:
    {
      "name": "HorarioIndisponivelException",
      "message": "Profissional já possui agendamento neste horário",
      "statusCode": "409 CONFLICT",
      "timestamp": "2025-12-12T15:30:00-03:00"
    }
    ↓
12. ResponseEntity retornado ao cliente com HTTP 409
```

---

###Benefícios Observados na Prática

1. **Consistência:** Todas as exceções em todos os controladores são tratadas de forma uniforme
2. **Manutenibilidade:** Adicionar novo tipo de exceção não requer alterar controladores
3. **Separação de Responsabilidades:** Controladores focam na lógica de negócio, Strategy cuida da serialização
4. **Testabilidade:** Fácil testar estratégias isoladamente
5. **Extensibilidade:** Novos controladores automaticamente se beneficiam do tratamento centralizado

---

## 3. Padrão OBSERVER (Comportamental)

### Descrição
O padrão **Observer** define um mecanismo de assinatura para notificar múltiplos objetos sobre quaisquer eventos que aconteçam com o objeto que eles estão observando.  

No projeto, foi adotada a implementação **Canônica/Acadêmica**, conforme a literatura de **Engenharia de Software Moderna**, utilizando **interfaces explícitas** (`Subject` e `Observer`) para garantir o **desacoplamento total** entre a regra de negócio e o mecanismo de notificação.

---

### Objetivo
Permitir que o sistema reaja a mudanças de estado críticas — como **contratação**, **atualização** ou **demissão** de um profissional — disparando ações secundárias (envio de e-mail, geração de logs, etc.) **sem acoplar a regra de negócio principal à infraestrutura de notificação**.

---

### Classes Criadas

#### 1. `Sujeito.java` (Interface)
- **Pacote:** `com.cesarschool.barbearia.dominio.compartilhado.padraoobserver`
- **Tipo:** Subject Interface (Contrato)
- **Responsabilidade:**  
  Define o contrato padronizado para qualquer classe que deseje emitir notificações.

**Características:**
- Utiliza **Generics `<T>`** para flexibilidade de eventos
- Métodos canônicos:
  - `adicionarObservador`
  - `removerObservador`
  - `notificarObservadores`
- Garante o **Princípio da Inversão de Dependência (DIP)**

---

#### 2. `Observador.java` (Interface)
- **Pacote:** `com.cesarschool.barbearia.dominio.compartilhado.padraoobserver`
- **Tipo:** Observer Interface (Contrato)
- **Responsabilidade:**  
  Define o contrato para qualquer classe que deseje escutar eventos.

**Características:**
- Método funcional único: `atualizar(T evento)`
- Permite a criação de múltiplos observadores (Log, Email, Push) sem alterar o sujeito

---

#### 3. `NotificacaoProfissionalObservador.java`
- **Pacote:** `com.cesarschool.barbearia.dominio.principal.profissional.observadores`
- **Tipo:** Concrete Observer
- **Responsabilidade:**  
  Recebe o evento de mudança e executa a lógica de envio de e-mail.

**Características:**
- Implementa `Observador<ProfissionalEvent>`
- Contém a lógica de formatação da mensagem (Boas-vindas / Desligamento)
- Isolada da lógica de persistência do banco de dados

---

### Classes Modificadas

#### 1. `ProfissionalServico.java`
- **Pacote:** `com.cesarschool.barbearia.dominio.principal.profissional`
- **Tipo:** Concrete Subject

**Modificações:**
- **Implementação de Interface:**  
  A classe passou a implementar `Sujeito<ProfissionalEvent>`.

  **Justificativa:**  
  Cumprir o contrato acadêmico do padrão Observer e permitir que o serviço seja tratado genericamente como um emissor de eventos.

- **Gestão de Estado (Lista de Observadores):**  
  Adição de uma lista interna `private final List<Observador>`.

  **Justificativa:**  
  Armazenar os observadores registrados sem depender de implementações concretas.

- **Gatilhos de Notificação:**  
  Inserção da chamada `notificarObservadores()` ao final dos métodos:
  - `registrarNovo`
  - `atualizar`
  - `desativar`

  **Justificativa:**  
  Transformar operações CRUD passivas em **gatilhos ativos de propagação de eventos** para o restante do sistema.

**Impacto:**  
A classe deixou de ser apenas um processador de dados e passou a atuar como o **Subject central do domínio de Profissionais**.

---

### Estrutura do Padrão
```
 ┌─────────────────┐           notifica            ┌──────────────────┐
 │  Sujeito (Intf) │ ────────────────────────────> │ Observador (Intf)│
 └─────────────────┘                               └──────────────────┘
          ▲                                                  ▲
          │ implementa                                       │ implementa
          │                                                  │
 ┌───────────────────────┐                         ┌──────────────────────────────┐
 │ #  ProfissionalServico│                         │NotificacaoProfissionalObserv.│
 │  (Concrete Subject)   │                         │     (Concrete Observer)      │
 │                       │                         │                              │
 │ - Lista de Observ.    │                         │ - Envia E-mail               │
 │ - Regra de Negócio    │                         │ - Gera Log                   │
 └───────────────────────┘                         └──────────────────────────────┘
```

### Benefícios Obtidos

1. **Princípio Aberto/Fechado (OCP):**
   - Novos observadores (ex: `EstoqueObservador`) podem ser adicionados sem modificar o código do `ProfissionalServico`.

2. **Baixo Acoplamento:**
   - O serviço de domínio não depende de bibliotecas de e-mail ou infraestrutura externa.

3. **Conformidade Acadêmica:**
   - Implementação segue rigorosamente a separação entre **Subject** e **Observer**, conforme descrito na literatura clássica de padrões de projeto.

---

## 5. Menção Honrosa: Padrão SINGLETON

### Descrição
O padrão **Singleton** garante que uma classe tenha apenas uma instância e fornece um ponto global de acesso a ela.

---

### Objetivo
Economizar recursos de memória e garantir a **consistência do estado** em serviços que não precisam ser instanciados múltiplas vezes (*Stateless Services*).

---

### Implementação no Projeto
No ecossistema **Spring Boot**, o padrão Singleton é gerenciado automaticamente pelo **Container de Injeção de Dependência (IoC)**.

- **Classes afetadas:**  
  Todas anotadas com `@Service`, `@Repository` e `@RestController`.

- **Modificação:**  
  Uso das anotações do framework (ex: `@Service` em `ProfissionalServico`) instrui o Spring a gerenciar essas classes como **Singletons**.

- **Justificativa:**  
  Delegar ao framework o controle do ciclo de vida dos objetos, evitando a criação manual de instâncias (`new ProfissionalServico()`) e garantindo que todos os controladores compartilhem a mesma instância de serviço e recursos de infraestrutura.
