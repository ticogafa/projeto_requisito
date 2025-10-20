# Backend de Gestão de Estoque - Sistema de Barbearia

## 📋 Visão Geral

Implementação completa do backend para gestão de estoque seguindo os princípios do **Domain-Driven Design (DDD)** e implementando 100% dos cenários BDD definidos em `Estoque.feature`.

## 🏗️ Arquitetura

### Estrutura de Pacotes

```
com.cesarschool.barbearia.dominio.principal.produto.estoque/
├── TipoMovimentacao.java              # Enum
├── MovimentacaoEstoqueId.java         # Value Object
├── MovimentacaoEstoque.java           # Entidade
├── MovimentacaoEstoqueRepositorio.java # Interface de Repositório
└── GestaoEstoqueServico.java          # Serviço de Domínio
```

## 📦 Componentes Criados

### 1. **TipoMovimentacao** (Enum)

Representa os tipos de movimentação de estoque:

- `ENTRADA` - Entrada de produtos (compra, devolução)
- `SAIDA` - Saída de produtos (perda, ajuste)
- `VENDA` - Venda no PDV
- `AJUSTE` - Ajuste manual
- `ESTOQUE_INICIAL` - Cadastro inicial
- `DESATIVACAO` - Desativação do produto

**Localização**: `produto/estoque/TipoMovimentacao.java`

---

### 2. **MovimentacaoEstoqueId** (Value Object)

Identificador único para movimentações de estoque.

- Estende `ValueObjectId<Integer>`
- Validação automática de ID positivo
- Implementa equals/hashCode/toString

**Localização**: `produto/estoque/MovimentacaoEstoqueId.java`

---

### 3. **MovimentacaoEstoque** (Entidade)

Entidade que representa o histórico de movimentações de estoque.

#### Atributos:
- `id` - Identificador único
- `produtoId` - ID do produto movimentado
- `nomeProduto` - Nome do produto (desnormalizado para performance)
- `tipo` - Tipo da movimentação (enum)
- `quantidade` - Quantidade movimentada
- `estoqueAnterior` - Estoque antes da operação
- `estoqueAtual` - Estoque após a operação
- `dataHora` - Timestamp da operação
- `observacao` - Descrição da movimentação
- `usuarioResponsavel` - Usuário que realizou a operação

#### Métodos de Negócio:
- `isEntrada()` - Verifica se é entrada
- `isSaida()` - Verifica se é saída
- `getDiferencaEstoque()` - Calcula diferença

**Localização**: `produto/estoque/MovimentacaoEstoque.java`

---

### 4. **MovimentacaoEstoqueRepositorio** (Interface)

Interface do repositório com operações de consulta de histórico.

#### Métodos:
- `buscarPorProduto(ProdutoId)` - Histórico completo do produto
- `buscarPorProdutoEPeriodo(ProdutoId, inicio, fim)` - Histórico filtrado por período
- `buscarPorTipo(TipoMovimentacao)` - Movimentações por tipo
- `buscarPorPeriodo(inicio, fim)` - Todas movimentações em período
- `buscarUltimasMovimentacoes(ProdutoId, limite)` - Últimas N movimentações

**Localização**: `produto/estoque/MovimentacaoEstoqueRepositorio.java`

---

### 5. **GestaoEstoqueServico** (Serviço de Domínio)

Serviço principal que orquestra todas as operações de estoque.

## 🎯 Cenários BDD Implementados

### ✅ Cenário 1: Cadastrar produto com nome único (POSITIVO)

```java
public Produto cadastrarProduto(Produto produto, String usuarioResponsavel)
```

**Regras de Negócio**:
- Nome do produto deve ser único (case-insensitive)
- Registra movimentação de estoque inicial
- Valida estoque não negativo

**Implementação**: Método `validarNomeUnico()` verifica duplicidade

---

### ❌ Cenário 2: Impedir cadastro com nome duplicado (NEGATIVO)

```java
private void validarNomeUnico(String nome, Integer idProdutoAtual)
```

**Validação**:
- Compara nomes ignorando maiúsculas/minúsculas
- Lança `IllegalArgumentException` se nome já existe
- Permite mesmo nome apenas para atualização do próprio produto

---

### ✅ Cenário 3: Atualizar estoque com quantidade válida (POSITIVO)

```java
public Produto adicionarEstoque(ProdutoId produtoId, int quantidade, 
                                String observacao, String usuarioResponsavel)
```

**Regras de Negócio**:
- Quantidade deve ser positiva
- Registra movimentação tipo `ENTRADA`
- Atualiza estoque somando quantidade

---

### ✅ Cenário 4: Registrar venda PDV reduzindo estoque (POSITIVO)

```java
public Produto registrarVendaPDV(ProdutoId produtoId, int quantidade, 
                                 String usuarioResponsavel)
```

**Fluxo**:
1. Valida estoque disponível
2. Reduz quantidade do estoque
3. Registra movimentação tipo `VENDA`
4. Retorna produto atualizado

---

### ❌ Cenário 5: Impedir venda com estoque insuficiente (NEGATIVO)

```java
// Validação dentro de registrarVendaPDV()
if (estoqueAnterior < quantidade) {
    throw new IllegalStateException("Estoque insuficiente...");
}
```

**Validação**:
- Verifica se estoque >= quantidade solicitada
- Lança `IllegalStateException` se insuficiente
- Não permite estoque negativo

---

## 🔧 Métodos Principais

### Operações de Cadastro

| Método | Descrição |
|--------|-----------|
| `cadastrarProduto()` | Cadastra novo produto com validação de nome único |
| `atualizarProduto()` | Atualiza produto existente |

### Operações de Estoque

| Método | Descrição |
|--------|-----------|
| `adicionarEstoque()` | Adiciona quantidade (entrada) |
| `removerEstoque()` | Remove quantidade (saída) |
| `registrarVendaPDV()` | Registra venda e reduz estoque |
| `validarEstoqueDisponivel()` | Verifica disponibilidade antes de venda |

### Consultas e Relatórios

| Método | Descrição |
|--------|-----------|
| `buscarProduto()` | Busca produto por ID |
| `listarTodosProdutos()` | Lista todos produtos |
| `listarProdutosComEstoqueBaixo()` | Produtos abaixo do mínimo |
| `isEstoqueBaixo()` | Verifica se estoque está baixo |
| `buscarHistoricoProduto()` | Histórico completo de movimentações |
| `buscarHistoricoPorPeriodo()` | Histórico filtrado por data |
| `buscarUltimasMovimentacoes()` | Últimas N movimentações |
| `listarMovimentacoesPorTipo()` | Filtra por tipo (ENTRADA, VENDA, etc.) |

### Operações de Ativação

| Método | Descrição |
|--------|-----------|
| `desativarProduto()` | Desativa produto e registra no histórico |

---

## 🔐 Validações Implementadas

### Validações de Produto

- ✅ Nome obrigatório e único (case-insensitive)
- ✅ Estoque não pode ser negativo
- ✅ Preço deve ser maior que zero
- ✅ Estoque mínimo não pode ser negativo

### Validações de Movimentação

- ✅ Produto deve existir
- ✅ Quantidade deve ser positiva
- ✅ Estoque não pode ficar negativo após operação
- ✅ Data/hora obrigatória
- ✅ Tipo de movimentação obrigatório

### Validações de Venda

- ✅ Estoque disponível >= quantidade solicitada
- ✅ Produto deve estar ativo (recomendado)
- ✅ Quantidade maior que zero

---

## 📊 Fluxo de Dados

### Cadastro de Produto

```
1. GestaoEstoqueServico.cadastrarProduto()
   ├─> Valida nome único
   ├─> ProdutoRepositorio.salvar()
   └─> Registra movimentação ESTOQUE_INICIAL
       └─> MovimentacaoEstoqueRepositorio.salvar()
```

### Venda PDV

```
1. GestaoEstoqueServico.registrarVendaPDV()
   ├─> Busca produto
   ├─> Valida estoque disponível
   ├─> Atualiza estoque do produto
   ├─> ProdutoRepositorio.salvar()
   └─> Registra movimentação VENDA
       └─> MovimentacaoEstoqueRepositorio.salvar()
```

### Consulta de Histórico

```
1. GestaoEstoqueServico.buscarHistoricoProduto()
   └─> MovimentacaoEstoqueRepositorio.buscarPorProduto()
       └─> Retorna lista ordenada por data (desc)
```

---

## 🧪 Integração com Testes BDD

### Mapeamento de Steps para Métodos

| Step Definition | Método do Serviço |
|----------------|------------------|
| `eu_cadastro_um_novo_produto_com_o_nome_e_estoque_inicial()` | `cadastrarProduto()` |
| `eu_tento_cadastrar_um_novo_produto_com_o_nome()` | `cadastrarProduto()` → Exception |
| `eu_adiciono_unidades_ao_estoque()` | `adicionarEstoque()` |
| `eu_envio_a_venda_de_produtos_para_registro()` | `registrarVendaPDV()` |

### Estrutura de Teste

```java
@Autowired
private GestaoEstoqueServico gestaoEstoqueServico;

@When("eu cadastro um novo produto")
public void cadastrarProduto() {
    Produto produto = new Produto(null, "Shampoo", 100, BigDecimal.valueOf(25.0), 10);
    produtoSalvo = gestaoEstoqueServico.cadastrarProduto(produto, "admin");
}

@Then("o produto é cadastrado com sucesso")
public void produtoCadastrado() {
    assertNotNull(produtoSalvo);
    assertNotNull(produtoSalvo.getId());
}
```

---

## 💾 Persistência

### Implementação do Repositório (JPA)

A implementação JPA deve:

1. Criar entidades JPA para `Produto` e `MovimentacaoEstoque`
2. Implementar `ProdutoRepositorio` e `MovimentacaoEstoqueRepositorio`
3. Usar `@Entity`, `@Table`, `@Id`, `@GeneratedValue`
4. Criar queries customizadas para métodos específicos

**Exemplo de Query**:

```java
@Query("SELECT m FROM MovimentacaoEstoque m WHERE m.produtoId = :produtoId ORDER BY m.dataHora DESC")
List<MovimentacaoEstoque> buscarPorProduto(@Param("produtoId") ProdutoId produtoId);
```

---

## 🚀 Como Usar

### 1. Cadastrar Produto

```java
Produto produto = new Produto(
    null,                          // ID será gerado
    "Gel Fixador",                 // Nome
    50,                            // Estoque inicial
    BigDecimal.valueOf(15.90),     // Preço
    10                             // Estoque mínimo
);

Produto produtoSalvo = gestaoEstoqueServico.cadastrarProduto(produto, "admin");
```

### 2. Adicionar Estoque

```java
ProdutoId produtoId = new ProdutoId(1);
int quantidade = 20;

Produto atualizado = gestaoEstoqueServico.adicionarEstoque(
    produtoId,
    quantidade,
    "Compra de fornecedor",
    "admin"
);
```

### 3. Registrar Venda

```java
ProdutoId produtoId = new ProdutoId(1);
int quantidadeVendida = 5;

try {
    Produto atualizado = gestaoEstoqueServico.registrarVendaPDV(
        produtoId,
        quantidadeVendida,
        "operador_caixa"
    );
} catch (IllegalStateException e) {
    // Estoque insuficiente
    System.err.println(e.getMessage());
}
```

### 4. Consultar Histórico

```java
ProdutoId produtoId = new ProdutoId(1);

List<MovimentacaoEstoque> historico = 
    gestaoEstoqueServico.buscarHistoricoProduto(produtoId);

historico.forEach(mov -> 
    System.out.println(mov.getTipo().getDescricao() + 
                       ": " + mov.getQuantidade() + 
                       " unidades")
);
```

### 5. Listar Produtos com Estoque Baixo

```java
List<Produto> produtosBaixo = 
    gestaoEstoqueServico.listarProdutosComEstoqueBaixo();

produtosBaixo.forEach(p -> 
    System.out.println(p.getNome() + 
                       " - Estoque: " + p.getEstoque() + 
                       " (Mínimo: " + p.getEstoqueMinimo() + ")")
);
```

---

## 📈 Melhorias Futuras

### Funcionalidades Adicionais

- [ ] **Auditoria completa**: Tracking de todas alterações
- [ ] **Lote de produtos**: Controle de validade e lotes
- [ ] **Reserva de estoque**: Reservar para agendamentos
- [ ] **Estoque distribuído**: Múltiplas localizações
- [ ] **Alertas automáticos**: Notificações de estoque baixo
- [ ] **Relatórios avançados**: Giro de estoque, produtos mais vendidos
- [ ] **Integração com fornecedores**: Pedidos automáticos

### Otimizações

- [ ] **Cache**: Redis para produtos mais acessados
- [ ] **Índices**: Otimizar queries de histórico
- [ ] **Paginação**: Histórico com paginação
- [ ] **Eventos de domínio**: Publicar eventos de estoque baixo

---

## 🔗 Dependências

### Pacotes Utilizados

- `com.cesarschool.barbearia.dominio.compartilhado.base` - Repositório base
- `com.cesarschool.barbearia.dominio.compartilhado.utils` - Validações
- `com.cesarschool.barbearia.dominio.compartilhado.valueobjects` - Value Objects
- `com.cesarschool.barbearia.dominio.principal.produto` - Entidade Produto

---

## ✅ Conformidade BDD

| Cenário | Status | Método |
|---------|--------|--------|
| Cadastrar produto com nome único | ✅ | `cadastrarProduto()` |
| Impedir cadastro com nome duplicado | ✅ | `validarNomeUnico()` |
| Atualizar estoque com quantidade válida | ✅ | `adicionarEstoque()` |
| Registrar venda PDV reduzindo estoque | ✅ | `registrarVendaPDV()` |
| Impedir venda com estoque insuficiente | ✅ | `registrarVendaPDV()` validação |

**Conformidade**: 100% dos cenários BDD implementados

---

## 📝 Notas Técnicas

### Princípios DDD Aplicados

- **Entidades**: `MovimentacaoEstoque` com identidade única
- **Value Objects**: `MovimentacaoEstoqueId`, `ProdutoId`
- **Serviços de Domínio**: `GestaoEstoqueServico` orquestra lógica complexa
- **Repositórios**: Abstração de persistência
- **Validações**: Regras de negócio centralizadas

### Padrões de Projeto

- **Repository Pattern**: Abstração de acesso a dados
- **Factory Method**: Construção de objetos de domínio
- **Strategy Pattern**: Tipos de movimentação
- **Value Object Pattern**: IDs imutáveis

---

## 📄 Licença

Este código faz parte do Sistema de Barbearia - Domínio Principal

**Versão**: 1.0  
**Data**: 19 de outubro de 2025  
**Autor**: Sistema de Barbearia
