# 🏗️ Análise: Migração para Arquitetura Limpa
## Projeto: barbearia-backend → Estrutura do sgb-2025-01

---

## 📊 ESTRUTURA ATUAL vs ESTRUTURA DESEJADA

### ❌ **ESTRUTURA ATUAL (barbearia-backend)**
```
barbearia-backend/
└── src/main/java/com/cesarschool/barbearia_backend/
    ├── agendamento/
    │   ├── dto/
    │   ├── mapper/
    │   ├── model/          ← ENTIDADES COM @Entity (acopladas a JPA)
    │   ├── repository/     ← Spring Data JPA
    │   ├── rest/           ← Controllers REST
    │   └── service/        ← Lógica misturada
    │
    ├── profissionais/
    │   ├── controller/
    │   ├── dto/
    │   ├── mapper/
    │   ├── model/          ← ENTIDADES COM @Entity
    │   ├── repository/     ← Spring Data JPA
    │   └── service/
    │
    ├── vendas/
    ├── marketing/
    ├── atendimento/
    ├── common/
    └── config/
```

**Problemas:**
- ✖️ Todas as entidades estão **acopladas ao JPA** (`@Entity`, `@Table`)
- ✖️ Domínio **conhece infraestrutura** (Spring, Jakarta Persistence)
- ✖️ **Services misturados**: lógica de negócio + acesso a dados
- ✖️ **Sem separação clara de camadas**
- ✖️ **Tudo em um único módulo Maven**
- ✖️ **Impossível testar domínio sem banco de dados**

---

### ✅ **ESTRUTURA DESEJADA (sgb-2025-01)**
```
sgb-2025-01/
├── dominio-agendamento/         ← NÚCLEO DE NEGÓCIO (PURO)
│   └── src/main/java/.../dominio/agendamento/
│       ├── Agendamento.java     ← SEM @Entity
│       ├── AgendamentoServico.java
│       ├── AgendamentoRepositorio.java  ← INTERFACE
│       └── StatusAgendamento.java
│
├── dominio-profissionais/
│   └── src/main/java/.../dominio/profissionais/
│       ├── Profissional.java    ← SEM @Entity
│       ├── ProfissionalServico.java
│       └── ProfissionalRepositorio.java  ← INTERFACE
│
├── dominio-compartilhado/
│   └── src/main/java/.../dominio/
│       ├── Cpf.java
│       ├── Email.java
│       └── Telefone.java
│
├── aplicacao/                   ← CASOS DE USO
│   └── src/main/java/.../aplicacao/
│       ├── agendamento/
│       │   ├── AgendamentoServicoAplicacao.java
│       │   ├── AgendamentoRepositorioAplicacao.java  ← INTERFACE
│       │   ├── AgendamentoResumo.java               ← DTO
│       │   └── AgendamentoResumoExpandido.java
│       └── profissionais/
│           └── ProfissionalServicoAplicacao.java
│
├── infraestrutura/              ← DETALHES TÉCNICOS
│   └── src/main/java/.../infraestrutura/
│       ├── persistencia/jpa/
│       │   ├── AgendamentoJpa.java         ← COM @Entity
│       │   ├── AgendamentoJpaRepositorio.java
│       │   ├── ProfissionalJpa.java
│       │   └── JpaMapeador.java            ← Converte Dominio ↔ JPA
│       └── evento/
│           └── EventoBarramentoImpl.java
│
└── apresentacao-backend/        ← INTERFACE REST
    └── src/main/java/.../apresentacao/
        ├── agendamento/
        │   ├── AgendamentoControlador.java  ← REST Controller
        │   └── AgendamentoFormulario.java
        └── profissionais/
            └── ProfissionalControlador.java
```

---

## 🎯 O QUE FALTA NO SEU PROJETO

### 1️⃣ **SEPARAÇÃO EM MÓDULOS MAVEN** 📦

**Criar estrutura multi-módulo:**

```xml
<!-- pom.xml raiz -->
<modules>
    <module>dominio-agendamento</module>
    <module>dominio-profissionais</module>
    <module>dominio-vendas</module>
    <module>dominio-marketing</module>
    <module>dominio-atendimento</module>
    <module>dominio-compartilhado</module>
    <module>aplicacao</module>
    <module>infraestrutura</module>
    <module>apresentacao-backend</module>
    <module>pai</module>
</modules>
```

---

### 2️⃣ **CAMADA DE DOMÍNIO PURA** 🎯

#### **ANTES (Atual - ERRADO ❌):**
```java
// profissionais/model/Profissional.java
@Entity                          ← ACOPLADO AO JPA
@Table(name = "profissional")    ← ACOPLADO AO JPA
public class Profissional {
    @Id                          ← ACOPLADO AO JPA
    @GeneratedValue              ← ACOPLADO AO JPA
    private Integer id;
    
    @Column(nullable = false)    ← ACOPLADO AO JPA
    private String nome;
    
    @NonNull
    @Column(nullable = false, unique = true)
    private Email email;
}
```

#### **DEPOIS (Desejado - CORRETO ✅):**
```java
// dominio-profissionais/src/.../dominio/profissionais/Profissional.java
public class Profissional {  // SEM ANOTAÇÕES JPA!
    private final ProfissionalId id;
    private String nome;
    private Email email;
    private Cpf cpf;
    private Telefone telefone;
    
    public Profissional(ProfissionalId id, String nome, Email email, Cpf cpf) {
        // Validações de negócio puras
        notNull(id, "O id não pode ser nulo");
        notNull(nome, "O nome não pode ser nulo");
        notBlank(nome, "O nome não pode estar em branco");
        
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }
    
    // Métodos de negócio puros
    public void atualizarNome(String novoNome) {
        notNull(novoNome, "O nome não pode ser nulo");
        notBlank(novoNome, "O nome não pode estar em branco");
        this.nome = novoNome;
    }
}
```

**Criar interface de repositório no domínio:**
```java
// dominio-profissionais/.../dominio/profissionais/ProfissionalRepositorio.java
public interface ProfissionalRepositorio {
    void salvar(Profissional profissional);
    Profissional obter(ProfissionalId id);
    void deletar(ProfissionalId id);
}
```

**Criar serviço de domínio:**
```java
// dominio-profissionais/.../dominio/profissionais/ProfissionalServico.java
public class ProfissionalServico {
    private final ProfissionalRepositorio repositorio;
    
    public ProfissionalServico(ProfissionalRepositorio repositorio) {
        notNull(repositorio, "O repositório não pode ser nulo");
        this.repositorio = repositorio;
    }
    
    public void salvar(Profissional profissional) {
        // Validações de negócio
        validarProfissional(profissional);
        repositorio.salvar(profissional);
    }
    
    private void validarProfissional(Profissional profissional) {
        // Regras de negócio puras
    }
}
```

---

### 3️⃣ **CAMADA DE APLICAÇÃO** 📋

**Criar serviços de aplicação (casos de uso):**

```java
// aplicacao/src/.../aplicacao/profissionais/ProfissionalServicoAplicacao.java
public class ProfissionalServicoAplicacao {
    private final ProfissionalRepositorioAplicacao repositorio;
    
    public ProfissionalServicoAplicacao(ProfissionalRepositorioAplicacao repositorio) {
        this.repositorio = repositorio;
    }
    
    public List<ProfissionalResumo> listarProfissionais() {
        return repositorio.listarResumos();
    }
    
    public ProfissionalResumoExpandido obterDetalhes(Integer id) {
        return repositorio.obterResumoExpandido(id);
    }
}
```

**Criar DTOs de aplicação:**
```java
// aplicacao/.../aplicacao/profissionais/ProfissionalResumo.java
public record ProfissionalResumo(
    Integer id,
    String nome,
    String email,
    String telefone
) {}

// aplicacao/.../aplicacao/profissionais/ProfissionalResumoExpandido.java
public record ProfissionalResumoExpandido(
    Integer id,
    String nome,
    String email,
    String cpf,
    String telefone,
    List<ServicoResumo> servicos,
    List<HorarioTrabalhoResumo> horarios
) {}
```

**Criar interface de repositório de aplicação:**
```java
// aplicacao/.../aplicacao/profissionais/ProfissionalRepositorioAplicacao.java
public interface ProfissionalRepositorioAplicacao {
    List<ProfissionalResumo> listarResumos();
    ProfissionalResumoExpandido obterResumoExpandido(Integer id);
}
```

---

### 4️⃣ **CAMADA DE INFRAESTRUTURA** 🔧

**Criar entidades JPA (separadas do domínio):**

```java
// infraestrutura/.../infraestrutura/persistencia/jpa/ProfissionalJpa.java
@Entity
@Table(name = "profissional")
class ProfissionalJpa {  // Visibilidade package!
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    
    String nome;
    String email;
    String cpf;
    String telefone;
    
    @OneToMany(mappedBy = "profissional")
    Set<HorarioTrabalhoJpa> horarios;
}
```

**Criar repositório JPA:**
```java
// infraestrutura/.../infraestrutura/persistencia/jpa/ProfissionalJpaRepository.java
interface ProfissionalJpaRepository extends JpaRepository<ProfissionalJpa, Integer> {
    @Query("SELECT new aplicacao.profissionais.ProfissionalResumo(p.id, p.nome, p.email, p.telefone) " +
           "FROM ProfissionalJpa p")
    List<ProfissionalResumo> listarResumos();
}
```

**Criar implementação do repositório de domínio:**
```java
// infraestrutura/.../infraestrutura/persistencia/jpa/ProfissionalRepositorioImpl.java
@Repository
@Transactional
public class ProfissionalRepositorioImpl 
    implements ProfissionalRepositorio, ProfissionalRepositorioAplicacao {
    
    private final ProfissionalJpaRepository jpaRepository;
    private final JpaMapeador mapeador;
    
    @Override
    public void salvar(Profissional profissional) {
        ProfissionalJpa jpa = mapeador.map(profissional, ProfissionalJpa.class);
        jpaRepository.save(jpa);
    }
    
    @Override
    public Profissional obter(ProfissionalId id) {
        ProfissionalJpa jpa = jpaRepository.findById(id.getValue())
            .orElseThrow(() -> new NotFoundException("Profissional não encontrado"));
        return mapeador.map(jpa, Profissional.class);
    }
    
    @Override
    public List<ProfissionalResumo> listarResumos() {
        return jpaRepository.listarResumos();
    }
}
```

**Criar mapeador:**
```java
// infraestrutura/.../infraestrutura/persistencia/jpa/JpaMapeador.java
@Component
public class JpaMapeador {
    private final ModelMapper modelMapper;
    
    public <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }
    
    // Mapeamentos customizados
    public Profissional toDominio(ProfissionalJpa jpa) {
        return new Profissional(
            new ProfissionalId(jpa.id),
            jpa.nome,
            new Email(jpa.email),
            new Cpf(jpa.cpf),
            new Telefone(jpa.telefone)
        );
    }
    
    public ProfissionalJpa toJpa(Profissional dominio) {
        ProfissionalJpa jpa = new ProfissionalJpa();
        jpa.id = dominio.getId().getValue();
        jpa.nome = dominio.getNome();
        jpa.email = dominio.getEmail().getValue();
        jpa.cpf = dominio.getCpf().getValue();
        jpa.telefone = dominio.getTelefone().getValue();
        return jpa;
    }
}
```

---

### 5️⃣ **CAMADA DE APRESENTAÇÃO** 🖥️

**Reorganizar controllers:**

```java
// apresentacao-backend/.../apresentacao/profissionais/ProfissionalControlador.java
@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
public class ProfissionalControlador {
    
    private final ProfissionalServico profissionalServico;  // DOMÍNIO
    private final ProfissionalServicoAplicacao servicoAplicacao;  // APLICAÇÃO
    private final BackendMapeador mapeador;
    
    @PostMapping
    public ResponseEntity<ProfissionalResponse> criar(@RequestBody ProfissionalDto dto) {
        // Converte DTO → Entidade de Domínio
        Profissional profissional = mapeador.map(dto, Profissional.class);
        
        // Usa serviço de DOMÍNIO para operações de escrita
        profissionalServico.salvar(profissional);
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping
    public ResponseEntity<List<ProfissionalResumo>> listar() {
        // Usa serviço de APLICAÇÃO para consultas
        List<ProfissionalResumo> resumos = servicoAplicacao.listarProfissionais();
        return ResponseEntity.ok(resumos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResumoExpandido> obterDetalhes(@PathVariable Integer id) {
        // Usa serviço de APLICAÇÃO para consultas expandidas
        ProfissionalResumoExpandido detalhes = servicoAplicacao.obterDetalhes(id);
        return ResponseEntity.ok(detalhes);
    }
}
```

---

### 6️⃣ **DOMÍNIO COMPARTILHADO** 🔄

**Mover Value Objects para módulo compartilhado:**

```java
// dominio-compartilhado/.../dominio/compartilhado/Email.java
public class Email {
    private final String valor;
    
    public Email(String valor) {
        validar(valor);
        this.valor = valor;
    }
    
    private void validar(String valor) {
        notNull(valor, "O email não pode ser nulo");
        if (!valor.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email inválido");
        }
    }
    
    public String getValue() {
        return valor;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return valor.equals(email.valor);
    }
}
```

**Fazer o mesmo para:** `Cpf`, `Telefone`, `StatusAgendamento`, etc.

---

## 📦 DEPENDÊNCIAS DOS MÓDULOS

### **dominio-* (Domínio)**
```xml
<dependencies>
    <!-- APENAS bibliotecas utilitárias puras -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
    </dependency>
    <dependency>
        <groupId>commons-validator</groupId>
        <artifactId>commons-validator</artifactId>
    </dependency>
    <!-- SEM Spring, SEM JPA, SEM Lombok -->
</dependencies>
```

### **aplicacao (Aplicação)**
```xml
<dependencies>
    <!-- Depende APENAS dos domínios -->
    <dependency>
        <groupId>com.cesarschool</groupId>
        <artifactId>dominio-agendamento</artifactId>
    </dependency>
    <dependency>
        <groupId>com.cesarschool</groupId>
        <artifactId>dominio-profissionais</artifactId>
    </dependency>
    <!-- SEM Spring, SEM JPA -->
</dependencies>
```

### **infraestrutura**
```xml
<dependencies>
    <!-- Implementa interfaces da aplicação -->
    <dependency>
        <groupId>com.cesarschool</groupId>
        <artifactId>aplicacao</artifactId>
    </dependency>
    
    <!-- Frameworks de infraestrutura -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.modelmapper</groupId>
        <artifactId>modelmapper</artifactId>
    </dependency>
</dependencies>
```

### **apresentacao-backend**
```xml
<dependencies>
    <!-- Usa aplicação e domínio -->
    <dependency>
        <groupId>com.cesarschool</groupId>
        <artifactId>aplicacao</artifactId>
    </dependency>
    <dependency>
        <groupId>com.cesarschool</groupId>
        <artifactId>infraestrutura</artifactId>
    </dependency>
    
    <!-- Framework web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

---

## 🔄 FLUXO DE DEPENDÊNCIAS

```
┌─────────────────────────────────────────────────────┐
│         apresentacao-backend (REST API)             │
│  Controllers, DTOs de entrada/saída, Validações    │
└────────────────────┬────────────────────────────────┘
                     │ usa
                     ↓
┌─────────────────────────────────────────────────────┐
│              aplicacao (Casos de Uso)               │
│    Serviços de Aplicação, DTOs de consulta         │
└────────────────────┬────────────────────────────────┘
                     │ usa
                     ↓
┌─────────────────────────────────────────────────────┐
│         dominio-* (Regras de Negócio)               │
│  Entidades, Value Objects, Serviços de Domínio,    │
│         Interfaces de Repositório                   │
└─────────────────────────────────────────────────────┘
                     ↑ implementa
                     │
┌─────────────────────────────────────────────────────┐
│       infraestrutura (Detalhes Técnicos)            │
│  Entidades JPA, Repositórios JPA, Mapeadores,      │
│        Implementações de Interfaces                 │
└─────────────────────────────────────────────────────┘
```

---

## 🎯 BENEFÍCIOS DA MIGRAÇÃO

### ✅ **Antes (Atual)**
```java
// Service faz TUDO - mistura responsabilidades
@Service
public class ProfissionalService {
    private final ProfissionalRepository repository;  // Spring Data JPA
    
    public ProfissionalResponse criar(CriarProfissionalRequest request) {
        // Validação
        // Conversão
        // Lógica de negócio
        // Persistência
        // Mapeamento de resposta
        // TUDO junto!
    }
}
```

**Problemas:**
- ❌ Impossível testar sem Spring/JPA
- ❌ Lógica de negócio misturada com infraestrutura
- ❌ Difícil trocar banco de dados
- ❌ Difícil adicionar novas interfaces (GraphQL, gRPC)

### ✅ **Depois (Arquitetura Limpa)**

```java
// DOMÍNIO - Lógica pura, testável sem frameworks
public class ProfissionalServico {
    public void salvar(Profissional profissional) {
        // APENAS regras de negócio
    }
}

// APLICAÇÃO - Orquestração de casos de uso
public class ProfissionalServicoAplicacao {
    public List<ProfissionalResumo> listar() {
        // APENAS consultas otimizadas
    }
}

// INFRAESTRUTURA - Detalhes técnicos isolados
@Repository
public class ProfissionalRepositorioImpl implements ProfissionalRepositorio {
    // APENAS persistência
}

// APRESENTAÇÃO - Interface REST isolada
@RestController
public class ProfissionalControlador {
    // APENAS entrada/saída HTTP
}
```

**Vantagens:**
- ✅ Testes unitários puros no domínio (sem mock)
- ✅ Trocar banco de dados alterando só infraestrutura
- ✅ Adicionar GraphQL sem tocar no domínio
- ✅ Código organizado e fácil de manter
- ✅ Responsabilidades bem separadas

---

## 📝 CHECKLIST DE MIGRAÇÃO

### Fase 1: Preparação
- [ ] Criar estrutura multi-módulo Maven
- [ ] Criar módulo `pai` com dependências compartilhadas
- [ ] Criar módulo `dominio-compartilhado`

### Fase 2: Domínio
- [ ] Criar módulos `dominio-agendamento`, `dominio-profissionais`, etc.
- [ ] Migrar entidades removendo anotações JPA
- [ ] Criar Value Objects (ProfissionalId, AgendamentoId, etc.)
- [ ] Criar interfaces de repositório de domínio
- [ ] Criar serviços de domínio
- [ ] Escrever testes de domínio (sem frameworks!)

### Fase 3: Aplicação
- [ ] Criar módulo `aplicacao`
- [ ] Criar serviços de aplicação
- [ ] Criar DTOs de consulta (Resumos)
- [ ] Criar interfaces de repositório de aplicação

### Fase 4: Infraestrutura
- [ ] Criar módulo `infraestrutura`
- [ ] Criar entidades JPA (separadas do domínio)
- [ ] Criar repositórios JPA
- [ ] Criar mapeadores (Domínio ↔ JPA)
- [ ] Implementar interfaces de repositório

### Fase 5: Apresentação
- [ ] Criar módulo `apresentacao-backend`
- [ ] Reorganizar controllers
- [ ] Criar mapeadores (DTO ↔ Domínio)
- [ ] Ajustar injeção de dependências

### Fase 6: Testes e Validação
- [ ] Migrar testes de integração
- [ ] Validar BDD features
- [ ] Garantir que todos os testes passam
- [ ] Documentar arquitetura

---

## 🚀 PRÓXIMOS PASSOS

1. **Comece pelo domínio compartilhado**: Migre `Email`, `Cpf`, `Telefone`
2. **Escolha um bounded context**: Sugiro começar por `profissionais`
3. **Crie o domínio puro**: Remova anotações JPA
4. **Crie a infraestrutura**: Entidades JPA separadas
5. **Ajuste a apresentação**: Controllers usando as novas camadas
6. **Repita para outros contexts**: `agendamento`, `vendas`, etc.

---

## 💡 EXEMPLO PRÁTICO DE MIGRAÇÃO

### ANTES: Estrutura Atual
```
profissionais/
├── model/Profissional.java        (@Entity - acoplado)
├── repository/ProfissionalRepository.java  (Spring Data)
├── service/ProfissionalService.java  (tudo misturado)
└── controller/ProfissionalController.java
```

### DEPOIS: Arquitetura Limpa
```
dominio-profissionais/
└── src/main/java/.../dominio/profissionais/
    ├── Profissional.java               (PURO - sem @Entity)
    ├── ProfissionalId.java             (Value Object)
    ├── ProfissionalRepositorio.java    (Interface)
    └── ProfissionalServico.java        (Lógica de negócio)

aplicacao/
└── src/main/java/.../aplicacao/profissionais/
    ├── ProfissionalServicoAplicacao.java
    ├── ProfissionalRepositorioAplicacao.java
    ├── ProfissionalResumo.java
    └── ProfissionalResumoExpandido.java

infraestrutura/
└── src/main/java/.../infraestrutura/persistencia/jpa/
    ├── ProfissionalJpa.java            (COM @Entity)
    ├── ProfissionalJpaRepository.java
    ├── ProfissionalRepositorioImpl.java
    └── JpaMapeador.java

apresentacao-backend/
└── src/main/java/.../apresentacao/profissionais/
    ├── ProfissionalControlador.java
    └── ProfissionalDto.java
```

---

## 📚 REFERÊNCIAS

- **Clean Architecture** - Robert C. Martin (Uncle Bob)
- **Domain-Driven Design** - Eric Evans
- **Projeto de referência**: sgb-2025-01

---

**Data de criação**: 11/10/2025
**Versão**: 1.0
