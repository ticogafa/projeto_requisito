# Padrões de Projeto Adotados

Este documento lista todos os padrões de projeto (Design Patterns) implementados no projeto Barbearia Backend, detalhando as classes criadas e/ou modificadas para cada padrão.

---

## 1. Padrão PROXY (Virtual Proxy com Lazy Loading)

### Descrição
Implementação de Virtual Proxy para controle de estoque: o proxy adia o carregamento dos produtos e das listas (todos e estoque baixo) até o primeiro acesso, reaproveitando dados em memória e invalidando seletivamente após gravações.

### Objetivo
Reduzir chamadas ao banco durante operações de estoque (cadastro, movimentação, alertas) sem alterar o contrato usado pelos serviços. O proxy entrega transparência ao cliente e mantém consistência ao invalidar apenas o que foi afetado.

### Classes usadas (código essencial)

**Subject (contrato comum)**
```java
// ProdutoRepositorio.java
public interface ProdutoRepositorio extends Repositorio<Produto, Integer> {
  Produto salvar(Produto produto);
  Produto buscarPorId(Integer id);
  List<Produto> listarTodos();
  void remover(Integer id);
  List<Produto> findProdutosComEstoqueBaixo();
  List<Produto> listarProdutosComEstoqueBaixo();
}
```

**Real Subject (acesso ao BD)**
```java
// ProdutoRepositorioJpa (classe package-private)
@Repository("produtoRepositorioJpa")
class ProdutoRepositorioJpa implements ProdutoRepositorio {
  @Autowired ProdutoJpaRepository repositorio;
  @Autowired JpaMapeador mapeador;

  public Produto salvar(Produto produto) {
    System.out.println("🔵 [REAL SUBJECT] salvar() - Acessando BD");
    var salvo = repositorio.save(mapeador.map(produto, ProdutoJpa.class));
    return mapeador.map(salvo, Produto.class);
  }

  public Produto buscarPorId(Integer id) {
    System.out.println("🔵 [REAL SUBJECT] buscarPorId(" + id + ") - Acessando BD");
    return repositorio.findById(id).map(p -> mapeador.map(p, Produto.class)).orElse(null);
  }

  public List<Produto> listarTodos() {
    System.out.println("🔵 [REAL SUBJECT] listarTodos() - Acessando BD");
    return repositorio.findAll().stream().map(p -> mapeador.map(p, Produto.class)).toList();
  }

  public void remover(Integer id) {
    System.out.println("🔵 [REAL SUBJECT] remover(" + id + ") - Acessando BD");
    repositorio.deleteById(id);
  }

  public List<Produto> findProdutosComEstoqueBaixo() {
    System.out.println("🔵 [REAL SUBJECT] findProdutosComEstoqueBaixo() - Acessando BD");
    return repositorio.findProdutosAbaixoEstoqueMinimo().stream()
        .map(p -> mapeador.map(p, Produto.class)).toList();
  }

  public List<Produto> listarProdutosComEstoqueBaixo() { return findProdutosComEstoqueBaixo(); }
}
```

**Virtual Proxy com lazy loading**
```java
// ProdutoRepositorioVirtualProxy.java
@Component
@Primary
public class ProdutoRepositorioVirtualProxy implements ProdutoRepositorio {
  private final ProdutoRepositorio realSubject;
  private final Map<Integer, Produto> produtosCarregados = new ConcurrentHashMap<>();
  private List<Produto> listaTodosCarregada;
  private List<Produto> listaEstoqueBaixoCarregada;
  private int reusoContador;
  private int lazyLoadContador;

  @Autowired
  public ProdutoRepositorioVirtualProxy(@Qualifier("produtoRepositorioJpa") ProdutoRepositorio realSubject) {
    this.realSubject = realSubject;
  }

  public Produto salvar(Produto produto) {
    Produto salvo = realSubject.salvar(produto);
    if (salvo != null && salvo.getId() != null) { produtosCarregados.remove(salvo.getId()); }
    listaTodosCarregada = null;
    listaEstoqueBaixoCarregada = null;
    if (salvo != null && salvo.getId() != null) { produtosCarregados.put(salvo.getId(), salvo); }
    return salvo;
  }

  public Produto buscarPorId(Integer id) {
    if (produtosCarregados.containsKey(id)) { reusoContador++; return produtosCarregados.get(id); }
    lazyLoadContador++;
    Produto produto = realSubject.buscarPorId(id);
    if (produto != null) { produtosCarregados.put(id, produto); }
    return produto;
  }

  public List<Produto> listarTodos() {
    if (listaTodosCarregada != null) { reusoContador++; return listaTodosCarregada; }
    lazyLoadContador++;
    listaTodosCarregada = realSubject.listarTodos();
    listaTodosCarregada.forEach(p -> produtosCarregados.put(p.getId(), p));
    return listaTodosCarregada;
  }

  public void remover(Integer id) {
    realSubject.remover(id);
    produtosCarregados.remove(id);
    listaTodosCarregada = null;
    listaEstoqueBaixoCarregada = null;
  }

  public List<Produto> findProdutosComEstoqueBaixo() {
    if (listaEstoqueBaixoCarregada != null) { reusoContador++; return listaEstoqueBaixoCarregada; }
    lazyLoadContador++;
    listaEstoqueBaixoCarregada = realSubject.findProdutosComEstoqueBaixo();
    return listaEstoqueBaixoCarregada;
  }

  public List<Produto> listarProdutosComEstoqueBaixo() { return findProdutosComEstoqueBaixo(); }

  public void invalidarDadosCarregados() {
    produtosCarregados.clear();
    listaTodosCarregada = null;
    listaEstoqueBaixoCarregada = null;
  }

  public void resetarEstatisticas() { reusoContador = 0; lazyLoadContador = 0; }
  public Map<String, Object> getMetricas() { return Map.of("reuso", reusoContador, "lazyLoads", lazyLoadContador); }
  public String getEstatisticas() { return "Reuso=" + reusoContador + " LazyLoads=" + lazyLoadContador; }
}
```

**Cliente de estoque (usa o contrato sem conhecer o proxy)**
```java
// GestaoEstoqueServico.java (trecho)
public class GestaoEstoqueServico {
  private final ProdutoRepositorio produtoRepositorio;
  private final MovimentacaoEstoqueRepositorio movimentacaoRepositorio;

  public GestaoEstoqueServico(ProdutoRepositorio produtoRepositorio,
                              MovimentacaoEstoqueRepositorio movimentacaoRepositorio) {
    this.produtoRepositorio = produtoRepositorio;
    this.movimentacaoRepositorio = movimentacaoRepositorio;
  }

  public Produto cadastrarProduto(Produto produto, String usuario) {
    validarNomeUnico(produto.getNome(), null);
    Produto salvo = produtoRepositorio.salvar(produto);
    // registra movimentação inicial se houver estoque...
    return salvo;
  }

  public Produto adicionarEstoque(ProdutoId id, int quantidade, String obs, String usuario) {
    Produto produto = buscarProduto(id);
    produto.setEstoque(produto.getEstoque() + quantidade);
    Produto atualizado = produtoRepositorio.salvar(produto);
    registrarMovimentacao(id, produto.getNome(), TipoMovimentacao.ENTRADA,
        quantidade, produto.getEstoque() - quantidade, produto.getEstoque(), obs, usuario);
    return atualizado;
  }
}
```

**Observabilidade via HTTP**

Duas controllers expõem endpoints para monitoramento do Virtual Proxy:

```java
// ProxyMetricasControlador.java
@RestController
@RequestMapping("/api/proxy")
public class ProxyMetricasControlador {
  private final ProdutoRepositorioVirtualProxy virtualProxy;

  @GetMapping("/statistics")
  public ResponseEntity<Map<String, Object>> getStatistics() {
    return ResponseEntity.ok(virtualProxy.getMetricas());
  }
  
  @GetMapping("/statistics/text")
  public ResponseEntity<String> getStatisticsText() {
    return ResponseEntity.ok(virtualProxy.getEstatisticas());
  }

  @DeleteMapping("/cache")
  public ResponseEntity<Map<String, String>> clearCache() {
    virtualProxy.invalidarDadosCarregados();
    return ResponseEntity.ok(Map.of("message", "Cache limpo", "status", "success"));
  }
  
  @DeleteMapping("/statistics")
  public ResponseEntity<Map<String, String>> resetStatistics() {
    virtualProxy.resetarEstatisticas();
    return ResponseEntity.ok(Map.of("message", "Estatísticas resetadas", "status", "success"));
  }
}

// CacheMonitorControlador.java
@RestController
@RequestMapping("/api/cache")
public class CacheMonitorControlador {
  private final ProdutoRepositorioVirtualProxy virtualProxy;

  @GetMapping("/metricas")
  public ResponseEntity<Map<String, Object>> getMetricas() {
    return ResponseEntity.ok(virtualProxy.getMetricas());
  }
  
  @GetMapping("/estatisticas")
  public ResponseEntity<String> getEstatisticas() {
    return ResponseEntity.ok(virtualProxy.obterEstatisticas());
  }
  
  @PostMapping("/resetar")
  public ResponseEntity<Map<String, String>> resetar() {
    virtualProxy.resetarEstatisticas();
    return ResponseEntity.ok(Map.of("message", "Estatísticas resetadas"));
  }
  
  @PostMapping("/limpar")
  public ResponseEntity<Map<String, String>> limpar() {
    virtualProxy.invalidarDadosCarregados();
    return ResponseEntity.ok(Map.of("message", "Cache limpo"));
  }
}
```

### Classes criadas e suas responsabilidades

**Classes do padrão:**
- `ProdutoRepositorio` (interface): Subject - contrato comum entre Proxy e Real Subject
- `ProdutoRepositorioVirtualProxy`: Proxy - controla acesso e implementa lazy loading
- `ProdutoRepositorioJpa`: Real Subject - acessa o banco de dados (classe interna em ProdutoJpa.java)
- `ProxyMetricasControlador`: Expõe endpoints `/api/proxy/*` para monitoramento
- `CacheMonitorControlador`: Expõe endpoints `/api/cache/*` para gestão de cache

**Classes modificadas:**
- `GestaoEstoqueServico`: Cliente do padrão - usa ProdutoRepositorio sem conhecer o Proxy


### Fluxo resumido
1) Cliente chama `ProdutoRepositorio` sem saber da existência do proxy.
2) Proxy verifica se dados estão carregados; se não, delega ao Real Subject (lazy load) e armazena localmente.
3) Operações de escrita invalidam somente o que foi impactado (produto alterado e listas relacionadas).
4) Métricas são exibidas no console durante a demonstração.

### Execução de demonstração
```bash
cd barbearia-backend/dominio-principal
mvn spring-boot:run -Dspring-boot.run.profiles=demo -Dmaven.test.skip=true
```

### Comandos úteis para teste

**Pré-requisitos:**
```bash
# Verificar se o MySQL está rodando
sudo docker ps | grep barbearia-mysql

# Iniciar o backend (em outro terminal)
cd barbearia-backend/dominio-principal
mvn spring-boot:run -Dmaven.test.skip=true
```

**Testar endpoints de métricas do proxy:**
```bash
# Ver estatísticas do Virtual Proxy (JSON)
curl http://localhost:8080/api/proxy/statistics | jq .

# Ver estatísticas do Virtual Proxy (texto)
curl http://localhost:8080/api/proxy/statistics/text

# Limpar cache do proxy
curl -X DELETE http://localhost:8080/api/proxy/cache | jq .

# Resetar estatísticas
curl -X DELETE http://localhost:8080/api/proxy/statistics | jq .
```


**Monitorar logs do backend:**
```bash
# Os logs mostrarão:
# 🟣 [VIRTUAL PROXY] = Ação do proxy (lazy loading, reuso, invalidação)
# 🔵 [REAL SUBJECT] = Acesso ao banco de dados
# ✅ REUSO = Dados retornados do cache
# 📥 LAZY LOAD = Carregamento do banco
```


## 2. Padrão DECORATOR (Estrutural) - Gestão de Caixa

### Descrição e objetivo
- Decorator adiciona validação de saldo sem alterar a implementação base do serviço de caixa e permite acoplar novas responsabilidades em cadeia (logging, auditoria etc.).

### Classes (código essencial)
**Interface + serviço base**
```java
// IGestaoCaixa.java
public interface IGestaoCaixa {
  void registrarEntrada(String descricao, BigDecimal valor, MeioPagamento meio);
  void registrarSaida(String descricao, BigDecimal valor, MeioPagamento meio);
  void registrarDivida(ClienteId clienteId, String descricao, BigDecimal valor, MeioPagamento meio);
  BigDecimal saldoAtual();
}

// GestaoCaixaServico.java
public class GestaoCaixaServico implements IGestaoCaixa {
  private final LancamentoRepositorio repositorio;

  public GestaoCaixaServico(LancamentoRepositorio repositorio) { this.repositorio = repositorio; }

  public void registrarEntrada(String descricao, BigDecimal valor, MeioPagamento meio) {
    repositorio.salvar(Lancamento.novoRecibemento(descricao, valor, meio));
  }

  public void registrarSaida(String descricao, BigDecimal valor, MeioPagamento meio) {
    repositorio.salvar(Lancamento.novoGasto(descricao, valor, meio));
  }

  public void registrarDivida(ClienteId clienteId, String descricao, BigDecimal valor, MeioPagamento meio) {
    repositorio.salvar(Lancamento.novaDivida(clienteId, descricao, valor, meio));
  }

  public BigDecimal saldoAtual() { /* soma entradas - saídas */ }
}
```

**Decorator base + regra de saldo**
```java
// GestaoCaixaDecorator.java
public abstract class GestaoCaixaDecorator implements IGestaoCaixa {
  protected final IGestaoCaixa proximo;
  public GestaoCaixaDecorator(IGestaoCaixa proximo) { this.proximo = proximo; }
  public void registrarEntrada(String d, BigDecimal v, MeioPagamento m) { proximo.registrarEntrada(d, v, m); }
  public void registrarSaida(String d, BigDecimal v, MeioPagamento m) { proximo.registrarSaida(d, v, m); }
  public void registrarDivida(ClienteId c, String d, BigDecimal v, MeioPagamento m) { proximo.registrarDivida(c, d, v, m); }
  public BigDecimal saldoAtual() { return proximo.saldoAtual(); }
}

// ValidadorSaldoDecorator.java
public class ValidadorSaldoDecorator extends GestaoCaixaDecorator {
  public ValidadorSaldoDecorator(IGestaoCaixa proximo) { super(proximo); }

  @Override
  public void registrarSaida(String descricao, BigDecimal valor, MeioPagamento meio) {
    if (valor.compareTo(proximo.saldoAtual()) > 0) {
      throw new IllegalStateException("Operação bloqueada: Saldo insuficiente.");
    }
    super.registrarSaida(descricao, valor, meio);
  }
}
```

**Montagem da cadeia (bean Spring)**
```java
// DomainServicesConfig.java
@Bean
public IGestaoCaixa gestaoCaixaServico(LancamentoRepositorio repo) {
  IGestaoCaixa base = new GestaoCaixaServico(repo);
  return new ValidadorSaldoDecorator(base); // decorado ao expor o bean
}
```

**Uso na borda (controller já recebe decorado)**
```java
// CaixaControlador.java
@PostMapping
public ResponseEntity<Void> adicionarLancamento(@RequestBody LancamentoRequest request) {
  if (request.getTipo() == Caixa.TipoLancamento.ENTRADA) {
    gestaoCaixa.registrarEntrada(request.getDescricao(), request.getValor(), MeioPagamento.DINHEIRO);
  } else {
    gestaoCaixa.registrarSaida(request.getDescricao(), request.getValor(), MeioPagamento.DINHEIRO);
  }
  return ResponseEntity.ok().build();
}
```

### Testes automatizados
```java
// GestaoCaixaDecoratorTest.java
@Test
void deveBloquearSaidaQuandoSaldoInsuficiente() {
  IGestaoCaixa servico = new ValidadorSaldoDecorator(new GestaoCaixaServico(new LancamentoMockRepositorio()));
  servico.registrarEntrada("Saldo Inicial", new BigDecimal("100.00"));
  assertThrows(IllegalStateException.class, () ->
    servico.registrarSaida("Compra", new BigDecimal("150.00"))
  );
}

@Test
void devePermitirSaidaQuandoSaldoSuficiente() {
  IGestaoCaixa servico = new ValidadorSaldoDecorator(new GestaoCaixaServico(new LancamentoMockRepositorio()));
  servico.registrarEntrada("Saldo Inicial", new BigDecimal("100.00"));
  servico.registrarSaida("Conta de Luz", new BigDecimal("40.00"));
  assertEquals(0, new BigDecimal("60.00").compareTo(servico.saldoAtual()));
}
```

### Como usar
- Qualquer injeção de `IGestaoCaixa` já vem decorada pelo bean de configuração; clientes não precisam conhecer os decorators.

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
