# Implementação JPA - Estoque e Agendamento

## 📋 Visão Geral

Esta implementação segue rigorosamente a metodologia do projeto **SGB (Sistema de Gerenciamento de Biblioteca)**, aplicando os princípios de **Domain-Driven Design (DDD)** e **Clean Architecture**.

## 🏗️ Estrutura Implementada

### **1. Classes JPA Criadas**

#### **Estoque:**
- `ProdutoJpa.java` - Entidade JPA para Produto
- `MovimentacaoEstoqueJpa.java` - Entidade JPA para Movimentação de Estoque
- `ProdutoRepositorioImpl` - Implementação do repositório de domínio
- `MovimentacaoEstoqueRepositorioImpl` - Implementação do repositório de domínio

#### **Agendamento:**
- `AgendamentoJpa.java` - Entidade JPA para Agendamento
- `AgendamentoRepositorioImpl` - Implementação do repositório de domínio

#### **Mapeamento:**
- `JpaMapeador.java` - Conversão entre entidades JPA e entidades de domínio

### **2. Migrations Flyway**

- `V1__ESTOQUE_AGENDAMENTO.sql` - Criação das tabelas
- `V2__DADOS_INICIAIS.sql` - Dados iniciais para testes

## 🎯 Padrões Aplicados

### **1. Separação de Responsabilidades**

```
Domínio (Puro)          →    Infraestrutura (JPA)
─────────────────            ───────────────────────
Produto                 →    ProdutoJpa
MovimentacaoEstoque     →    MovimentacaoEstoqueJpa
Agendamento             →    AgendamentoJpa
```

### **2. Entidades JPA (Package-Private)**

```java
@Entity
@Table(name = "PRODUTO")
class ProdutoJpa {  // SEM public!
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    
    String nome;
    int estoque;
    BigDecimal preco;
    int estoqueMinimo;
}
```

**Características:**
- ✅ Classes **package-private** (sem `public`)
- ✅ Campos **package-private** (acesso direto, sem getters/setters)
- ✅ Sufixo `Jpa` no nome
- ✅ Tabelas em **UPPER_CASE**
- ✅ Anotações Jakarta Persistence

### **3. Repositórios JPA (Spring Data)**

```java
interface ProdutoJpaRepository extends JpaRepository<ProdutoJpa, Integer> {
    @Query("SELECT p FROM ProdutoJpa p WHERE LOWER(p.nome) = LOWER(:nome)")
    ProdutoJpa findByNomeIgnoreCase(String nome);
    
    boolean existsByNomeIgnoreCase(String nome);
}
```

**Características:**
- ✅ Interfaces **package-private**
- ✅ Sufixo `JpaRepository`
- ✅ Queries JPQL com `@Query`
- ✅ Métodos de busca customizados

### **4. Implementação de Repositórios**

```java
@Repository
class ProdutoRepositorioImpl implements ProdutoRepositorio {
    @Autowired
    ProdutoJpaRepository repositorio;
    
    @Autowired
    JpaMapeador mapeador;
    
    @Override
    public void salvar(Produto produto) {
        var produtoJpa = mapeador.map(produto, ProdutoJpa.class);
        repositorio.save(produtoJpa);
    }
    
    @Override
    public Produto obter(ProdutoId id) {
        var produtoJpa = repositorio.findById(id.getValor())
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        return mapeador.map(produtoJpa, Produto.class);
    }
}
```

**Características:**
- ✅ Classes **package-private**
- ✅ Implementam interface de domínio
- ✅ Usam `JpaMapeador` para conversões
- ✅ Tratam exceções de forma apropriada

### **5. JpaMapeador (ModelMapper)**

```java
@Component
class JpaMapeador extends ModelMapper {
    
    JpaMapeador() {
        configurarModelMapper();
        configurarConversores();
    }
    
    private void configurarConversores() {
        addConverter(new AbstractConverter<ProdutoJpa, Produto>() {
            @Override
            protected Produto convert(ProdutoJpa source) {
                return new Produto(
                    source.id,
                    source.nome,
                    source.estoque,
                    source.preco,
                    source.estoqueMinimo
                );
            }
        });
    }
}
```

**Características:**
- ✅ Estende `ModelMapper`
- ✅ Acesso a campos privados habilitado
- ✅ Conversores customizados para cada entidade
- ✅ Tratamento de valores nulos
- ✅ Conversões bidirecionais (JPA ↔ Domínio)

## 🗄️ Banco de Dados

### **Tabela PRODUTO**
```sql
CREATE TABLE PRODUTO (
    ID INT GENERATED ALWAYS AS IDENTITY NOT NULL,
    NOME VARCHAR(200) NOT NULL UNIQUE,
    ESTOQUE INT NOT NULL DEFAULT 0,
    PRECO DECIMAL(10, 2) NOT NULL,
    ESTOQUE_MINIMO INT NOT NULL DEFAULT 0,
    PRIMARY KEY (ID)
);
```

### **Tabela MOVIMENTACAO_ESTOQUE**
```sql
CREATE TABLE MOVIMENTACAO_ESTOQUE (
    ID INT GENERATED ALWAYS AS IDENTITY NOT NULL,
    PRODUTO_ID INT NOT NULL,
    NOME_PRODUTO VARCHAR(200) NOT NULL,
    TIPO VARCHAR(50) NOT NULL,
    QUANTIDADE INT NOT NULL,
    ESTOQUE_ANTERIOR INT NOT NULL,
    ESTOQUE_ATUAL INT NOT NULL,
    DATA_HORA TIMESTAMP NOT NULL,
    OBSERVACAO VARCHAR(500),
    USUARIO_RESPONSAVEL VARCHAR(100),
    PRIMARY KEY (ID),
    FOREIGN KEY (PRODUTO_ID) REFERENCES PRODUTO(ID)
);
```

### **Tabela AGENDAMENTO**
```sql
CREATE TABLE AGENDAMENTO (
    ID INT GENERATED ALWAYS AS IDENTITY NOT NULL,
    DATA_HORA TIMESTAMP NOT NULL,
    STATUS VARCHAR(20) NOT NULL,
    CLIENTE_ID INT NOT NULL,
    PROFISSIONAL_ID INT,
    SERVICO_ID INT NOT NULL,
    OBSERVACOES VARCHAR(500),
    PRIMARY KEY (ID)
);
```

## 🔍 Índices Criados

Para otimização de consultas:

```sql
-- Produto
CREATE INDEX IDX_PRODUTO_NOME ON PRODUTO(NOME);
CREATE INDEX IDX_PRODUTO_ESTOQUE ON PRODUTO(ESTOQUE);

-- Movimentação
CREATE INDEX IDX_MOVIMENTACAO_PRODUTO ON MOVIMENTACAO_ESTOQUE(PRODUTO_ID);
CREATE INDEX IDX_MOVIMENTACAO_DATA ON MOVIMENTACAO_ESTOQUE(DATA_HORA);
CREATE INDEX IDX_MOVIMENTACAO_TIPO ON MOVIMENTACAO_ESTOQUE(TIPO);

-- Agendamento
CREATE INDEX IDX_AGENDAMENTO_DATA_HORA ON AGENDAMENTO(DATA_HORA);
CREATE INDEX IDX_AGENDAMENTO_CLIENTE ON AGENDAMENTO(CLIENTE_ID);
CREATE INDEX IDX_AGENDAMENTO_PROFISSIONAL ON AGENDAMENTO(PROFISSIONAL_ID);
CREATE INDEX IDX_AGENDAMENTO_STATUS ON AGENDAMENTO(STATUS);
CREATE INDEX IDX_AGENDAMENTO_CONFLITO ON AGENDAMENTO(PROFISSIONAL_ID, DATA_HORA, STATUS);
```

## 📦 Dependências Necessárias

Para que o código funcione, adicione ao `pom.xml` do módulo `dominio-principal`:

```xml
<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Flyway -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>

<!-- ModelMapper -->
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
</dependency>

<!-- Banco H2 (para testes) -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- PostgreSQL (para produção) -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

## 🎓 Funcionalidades Implementadas

### **Estoque:**

#### **Cadastro de Produto**
- ✅ Validação de nome único
- ✅ Controle de estoque mínimo
- ✅ Preço com precisão decimal

#### **Movimentação de Estoque**
- ✅ Histórico completo de movimentações
- ✅ Tipos: ENTRADA, SAIDA, VENDA, AJUSTE
- ✅ Rastreabilidade (usuário responsável)
- ✅ Estoque antes/depois da movimentação

#### **Consultas:**
- ✅ Produtos abaixo do estoque mínimo
- ✅ Histórico por produto
- ✅ Movimentações por tipo
- ✅ Movimentações por período

### **Agendamento:**

#### **Criação de Agendamento**
- ✅ Associação com Cliente, Profissional e Serviço
- ✅ Controle de status (PENDENTE, CONFIRMADO, CANCELADO, CONCLUIDO)
- ✅ Validação de conflitos de horário
- ✅ Observações opcionais

#### **Consultas:**
- ✅ Agendamentos por cliente
- ✅ Agendamentos por profissional
- ✅ Agendamentos por status
- ✅ Agendamentos por período
- ✅ Verificação de conflitos
- ✅ Agendamentos do dia

## 🚀 Como Usar

### **1. Configurar application.properties**

```properties
# Datasource
spring.datasource.url=jdbc:h2:mem:barbearia
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```

### **2. Habilitar JPA Repositories**

Adicione na classe principal:

```java
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.cesarschool.barbearia.infraestrutura.persistencia.jpa")
@EntityScan(basePackages = "com.cesarschool.barbearia.infraestrutura.persistencia.jpa")
public class BarbeariaApplication {
    public static void main(String[] args) {
        SpringApplication.run(BarbeariaApplication.class, args);
    }
}
```

## ✅ Conformidade com Metodologia SGB

| Aspecto | SGB | Barbearia |
|---------|-----|-----------|
| Classes JPA package-private | ✅ | ✅ |
| Sufixo Jpa | ✅ | ✅ |
| Tabelas UPPER_CASE | ✅ | ✅ |
| ModelMapper | ✅ | ✅ |
| Conversores customizados | ✅ | ✅ |
| Repositórios implementam interface de domínio | ✅ | ✅ |
| Flyway migrations | ✅ | ✅ |
| Separação domínio/infra | ✅ | ✅ |

## 📝 Notas Importantes

1. **Desacoplamento**: As entidades de domínio (`Produto`, `Agendamento`) não conhecem JPA
2. **Value Objects**: IDs são tratados como Value Objects imutáveis
3. **Conversões**: Todas as conversões são feitas via `JpaMapeador`
4. **Queries**: Queries JPQL customizadas para casos específicos
5. **Índices**: Índices estratégicos para consultas frequentes

## 🔧 Próximos Passos

Para completar a implementação:

1. ✅ Adicionar dependências ao `pom.xml`
2. ✅ Configurar `application.properties`
3. ✅ Criar classe de configuração Spring Boot
4. ✅ Executar testes de integração
5. ✅ Implementar camada de aplicação (serviços)
6. ✅ Implementar controllers REST

---

**Documentação criada seguindo a metodologia do projeto SGB** 🎯
