# Análise Comparativa de Arquitetura DDD

## Estrutura do Projeto de Referência (sgb-2025-01)

O projeto `sgb-2025-01` segue uma arquitetura Domain-Driven Design (DDD) com uma estrutura modular claramente definida. Este documento explica como essa estrutura difere da abordagem tradicional de DTOs, models, services e controllers que você está acostumado.

### Camadas do Projeto de Referência

1. **Domínio** - Múltiplos módulos (`dominio-acervo`, `dominio-administracao`, `dominio-analise`, `dominio-compartilhado`)
   - Contém as entidades de domínio, objetos de valor, agregados, repositórios (interfaces) e serviços de domínio
   - Exemplos: `Autor`, `Livro`, `Socio`, `Email` (objeto de valor), `AutorRepositorio`, `LivroServico`
   - Esta é a camada central e mais importante em DDD

2. **Aplicação** (`aplicacao`)
   - Orquestra os objetos de domínio para executar tarefas específicas
   - Fornece DTOs para transferência de dados entre camadas (interfaces como `LivroResumo`, `AutorResumo`)
   - Implementa casos de uso da aplicação através de `*ServicoAplicacao`
   - Define interfaces de repositórios específicas para a aplicação (`*RepositorioAplicacao`)

3. **Infraestrutura** (`infraestrutura`)
   - Implementa interfaces técnicas como persistência e mensageria
   - Contém classes JPA para persistência de dados (`*Jpa`)
   - Implementações dos repositórios definidos no domínio
   - Migrações de banco de dados (Flyway)

4. **Apresentação** - Múltiplos módulos (`apresentacao-backend`, `apresentacao-frontend`, `apresentacao-vaadin`)
   - Interfaces com o usuário (REST, web, desktop)
   - Controladores REST (`*Controlador`)
   - Formulários e DTOs específicos para UI (`*Formulario`)

### Principais Características do DDD no Projeto de Referência

1. **Isolamento do Domínio**: O domínio é independente de frameworks e tecnologias externas
2. **Modularização por Contextos**: Separação em subdomínios (acervo, administracao, analise)
3. **Linguagem Ubíqua**: Nomes de classes e métodos refletem o vocabulário do negócio
4. **Camadas Bem Definidas**: Separação clara entre domínio, aplicação, infraestrutura e apresentação
5. **Inversão de Dependência**: Interfaces de repositórios no domínio, implementações na infraestrutura

## Estrutura Atual do Seu Projeto (barbearia-backend)

Seu projeto `barbearia-backend` segue uma estrutura mais tradicional de aplicação Spring Boot:

1. **Models** (`*.model`) - Entidades JPA com anotações
2. **Repositories** (`*.repository`) - Interfaces que estendem `JpaRepository`
3. **Services** (`*.service`) - Serviços para lógica de negócios
4. **Controllers** (`*.controller`) - Controladores REST
5. **DTOs** (`*.dto`) - Objetos de transferência de dados
6. **Mappers** (`*.mapper`) - Conversores entre entidades e DTOs

A organização é feita principalmente por pacotes funcionais (profissionais, vendas, etc.).

## Como Adaptar seu Projeto para a Arquitetura DDD

### 1. Reorganize a Estrutura de Pacotes

Mude de uma organização por camadas técnicas para uma organização por contextos de negócio:

```
com.cesarschool.barbearia_backend/
  dominio/
    agendamento/      # Subdomínio de agendamento
    atendimento/      # Subdomínio de atendimento
    estoque/          # Subdomínio de estoque
    cadastros/        # Subdomínio de cadastros
    pagamento/        # Subdomínio de pagamento
    compartilhado/    # Elementos compartilhados entre domínios
  aplicacao/
    agendamento/
    atendimento/
    estoque/
    cadastros/
    pagamento/
  infraestrutura/
    persistencia/
      jpa/
    eventos/
    email/
  apresentacao/
    api/
      agendamento/
      atendimento/
      estoque/
      cadastros/
      pagamento/
```

### 2. Separe Modelos de Domínio das Entidades de Persistência

No DDD, as entidades de domínio devem estar livres de detalhes de persistência:

1. **Entidades de Domínio** (em `dominio/*`):
   - Sem anotações JPA
   - Foco na lógica de negócios
   - Encapsulamento forte

2. **Entidades de Persistência** (em `infraestrutura/persistencia/jpa`):
   - Com anotações JPA
   - Mapeamento para tabelas
   - Classes conversoras entre entidades de domínio e persistência

### 3. Defina Repositórios como Interfaces no Domínio

1. Crie interfaces de repositório no domínio:
   ```java
   // em dominio/agendamento/AgendamentoRepositorio.java
   public interface AgendamentoRepositorio {
     void salvar(Agendamento agendamento);
     Agendamento buscarPorId(AgendamentoId id);
     List<Agendamento> buscarTodos();
     // outros métodos específicos do domínio
   }
   ```

2. Implemente essas interfaces na camada de infraestrutura:
   ```java
   // em infraestrutura/persistencia/AgendamentoRepositorioImpl.java
   @Repository
   public class AgendamentoRepositorioImpl implements AgendamentoRepositorio {
     @Autowired
     private AgendamentoJpaRepository jpaRepository;
     
     @Override
     public void salvar(Agendamento agendamento) {
       // conversão e persistência
     }
     // outras implementações
   }
   ```

### 4. Crie Objetos de Valor para Conceitos Importantes

Identifique conceitos que não são entidades, mas têm significado no domínio:

```java
// em dominio/agendamento/Horario.java
public class Horario {
  private final LocalTime inicio;
  private final LocalTime fim;
  
  public Horario(LocalTime inicio, LocalTime fim) {
    if (inicio.isAfter(fim)) {
      throw new IllegalArgumentException("Horário inicial deve ser antes do final");
    }
    this.inicio = inicio;
    this.fim = fim;
  }
  
  public boolean conflitaCom(Horario outro) {
    // lógica para verificar conflitos
  }
  
  // outros métodos de domínio
}
```

### 5. Adicione Serviços de Aplicação

A camada de aplicação orquestra objetos de domínio e fornece DTOs:

```java
// em aplicacao/agendamento/AgendamentoServicoAplicacao.java
@Service
public class AgendamentoServicoAplicacao {
  @Autowired
  private AgendamentoRepositorio agendamentoRepositorio;
  @Autowired
  private ClienteRepositorio clienteRepositorio;
  @Autowired
  private ProfissionalRepositorio profissionalRepositorio;
  
  public AgendamentoResumo criarAgendamento(AgendamentoFormulario formulario) {
    // Obter entidades de domínio
    Cliente cliente = clienteRepositorio.buscarPorId(new ClienteId(formulario.getClienteId()));
    Profissional profissional = profissionalRepositorio.buscarPorId(new ProfissionalId(formulario.getProfissionalId()));
    
    // Criar objeto de domínio
    Agendamento agendamento = new Agendamento(
      cliente, 
      profissional,
      new Horario(formulario.getHoraInicio(), formulario.getHoraFim()),
      formulario.getServicos().stream()
        .map(id -> servicoRepositorio.buscarPorId(new ServicoId(id)))
        .collect(Collectors.toList())
    );
    
    // Validações de domínio ocorrem na entidade
    
    // Persistir
    agendamentoRepositorio.salvar(agendamento);
    
    // Retornar DTO
    return converterParaDTO(agendamento);
  }
}
```

### 6. Use DTOs na Camada de Aplicação como Interfaces

O projeto de referência usa interfaces para definir DTOs:

```java
// em aplicacao/agendamento/AgendamentoResumo.java
public interface AgendamentoResumo {
  String getId();
  String getCliente();
  String getProfissional();
  LocalDateTime getDataHora();
  int getDuracao();
  List<String> getServicos();
  BigDecimal getValorTotal();
}
```

### 7. Adapte os Controllers para a Camada de Apresentação

Controllers devem usar serviços da camada de aplicação, não diretamente o domínio:

```java
// em apresentacao/api/agendamento/AgendamentoControlador.java
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoControlador {
  @Autowired
  private AgendamentoServicoAplicacao servicoAplicacao;
  
  @PostMapping
  public ResponseEntity<AgendamentoResumo> criar(@RequestBody AgendamentoFormulario formulario) {
    AgendamentoResumo resumo = servicoAplicacao.criarAgendamento(formulario);
    return ResponseEntity.ok(resumo);
  }
  
  // outros endpoints
}
```

## Passos Práticos para a Migração

1. **Não mude tudo de uma vez**: Comece com um subdomínio específico (ex: agendamento)
2. **Identifique o Modelo de Domínio**: Crie um diagrama do modelo de domínio para entender melhor os conceitos
3. **Mova as Regras de Negócio para as Entidades**: As entidades de domínio devem conter suas próprias regras
4. **Separe Camadas Gradualmente**: Comece separando o domínio da infraestrutura
5. **Use Adaptadores**: Crie adaptadores para converter entre modelos de domínio e persistência
6. **Refatore os Casos de Uso**: Mova a lógica dos serviços atuais para serviços de aplicação

## Benefícios da Adoção do DDD

1. **Melhor Alinhamento com o Negócio**: O código reflete diretamente os conceitos do domínio
2. **Maior Testabilidade**: O domínio pode ser testado independentemente da infraestrutura
3. **Flexibilidade**: Facilita mudanças na infraestrutura sem afetar o domínio
4. **Escalabilidade Organizacional**: Facilita trabalhar com equipes separadas em diferentes contextos
5. **Expressividade do Código**: O código se torna mais autoexplicativo e reflete o domínio

## Conclusão

A transição para DDD é um processo gradual que exige mudanças tanto na estrutura do código quanto na forma de pensar sobre o domínio. A principal mudança é colocar o domínio no centro da arquitetura, isolando-o de preocupações técnicas. Isso leva a um código mais alinhado com o negócio e mais adaptável a mudanças.