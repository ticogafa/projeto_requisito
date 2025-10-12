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

### 6. Entenda a Diferença entre DTOs e Resumos

O projeto de referência faz uma distinção importante entre **Resumos** (na camada de aplicação) e **DTOs** (na camada de apresentação):

#### Resumos (Interfaces na Camada de Aplicação)
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

**Características dos Resumos:**
- **Interface** read-only para consultas
- **Camada de Aplicação** - podem ser reutilizados por diferentes apresentações
- **Performance** - podem ser implementados como projeções JPA
- **Imutáveis** - apenas getters
- **Uso**: Listagens, consultas, relatórios

#### DTOs (Classes na Camada de Apresentação)
```java
// em apresentacao/api/agendamento/AgendamentoFormulario.java
public class AgendamentoFormulario {
    public static class AgendamentoDto {
        public String clienteId;
        public String profissionalId;
        public LocalDateTime dataHora;
        public List<Integer> servicoIds;
        public String observacoes;
    }
}
```

**Características dos DTOs:**
- **Classes concretas** para entrada de dados
- **Camada de Apresentação** - específicos para cada interface (web, mobile, etc.)
- **Mutáveis** - podem ser modificados durante validação
- **Uso**: Formulários, entrada de dados, comandos

#### Exemplo Prático de Uso
```java
@RestController
public class AgendamentoControlador {
    
    // CONSULTA - Usa Resumo (Interface)
    @GetMapping
    public List<AgendamentoResumo> listar() {
        return servicoAplicacao.buscarTodos(); // Retorna projeção JPA
    }
    
    // COMANDO - Usa DTO (Classe)
    @PostMapping
    public AgendamentoResumo criar(@RequestBody AgendamentoDto dto) {
        return servicoAplicacao.criarAgendamento(dto);
    }
}
```

### 7. Use Mapeadores para Conversão entre Camadas

O projeto de referência usa um `BackendMapeador` baseado em ModelMapper para converter entre DTOs da apresentação e entidades de domínio:

```java
@Component
public class BackendMapeador extends ModelMapper {
    
    // Converte DTO do formulário para entidade de domínio
    addConverter(new AbstractConverter<AgendamentoDto, Agendamento>() {
        @Override
        protected Agendamento convert(AgendamentoDto source) {
            Cliente cliente = clienteRepositorio.buscarPorId(new ClienteId(source.clienteId));
            Profissional profissional = profissionalRepositorio.buscarPorId(new ProfissionalId(source.profissionalId));
            
            return new Agendamento(
                cliente,
                profissional,
                new Horario(source.horaInicio, source.horaFim),
                source.observacoes
            );
        }
    });
    
    // Converte IDs simples para Value Objects
    addConverter(new AbstractConverter<Integer, ClienteId>() {
        @Override
        protected ClienteId convert(Integer source) {
            return new ClienteId(source);
        }
    });
}
```

**Vantagens do Mapeador:**
- **Centraliza** a lógica de conversão
- **Reutilizável** em diferentes controladores
- **Type-safe** - detecta erros de tipo em tempo de compilação
- **Flexível** - permite conversões complexas

### 8. Adapte os Controllers para a Camada de Apresentação

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

## Garantindo Consistência entre Entidades de Domínio e JPA

Uma das maiores preocupações ao separar modelos de domínio das entidades JPA é manter a consistência entre eles. Aqui estão as principais estratégias:

### 1. Estratégia de Mapeamento com Adaptadores

Crie adaptadores específicos para conversão entre entidades de domínio e JPA:

```java
// Adaptador para conversão
@Component
public class AgendamentoAdaptador {
    
    public Agendamento toDominio(AgendamentoJpa jpaEntity) {
        if (jpaEntity == null) return null;
        
        return new Agendamento(
            new AgendamentoId(jpaEntity.getId()),
            new ClienteId(jpaEntity.getClienteId()),
            new ProfissionalId(jpaEntity.getProfissionalId()),
            new Horario(jpaEntity.getHoraInicio(), jpaEntity.getHoraFim()),
            jpaEntity.getStatus(),
            jpaEntity.getObservacoes()
        );
    }
    
    public AgendamentoJpa toJpa(Agendamento dominio) {
        if (dominio == null) return null;
        
        AgendamentoJpa jpa = new AgendamentoJpa();
        jpa.setId(dominio.getId().getValor());
        jpa.setClienteId(dominio.getClienteId().getValor());
        jpa.setProfissionalId(dominio.getProfissionalId().getValor());
        jpa.setHoraInicio(dominio.getHorario().getInicio());
        jpa.setHoraFim(dominio.getHorario().getFim());
        jpa.setStatus(dominio.getStatus());
        jpa.setObservacoes(dominio.getObservacoes());
        return jpa;
    }
}
```

### 2. Testes de Consistência Automatizados

Crie testes que garantam que a conversão funciona corretamente em ambos os sentidos:

```java
@Test
public void deveManterConsistenciaEntreModeloEJpa() {
    // Arrange
    Agendamento agendamentoOriginal = criarAgendamentoTeste();
    
    // Act - Converte para JPA e volta para domínio
    AgendamentoJpa jpaEntity = adaptador.toJpa(agendamentoOriginal);
    Agendamento agendamentoConvertido = adaptador.toDominio(jpaEntity);
    
    // Assert - Deve ser igual ao original
    assertEquals(agendamentoOriginal.getId(), agendamentoConvertido.getId());
    assertEquals(agendamentoOriginal.getClienteId(), agendamentoConvertido.getClienteId());
    assertEquals(agendamentoOriginal.getHorario(), agendamentoConvertido.getHorario());
    // ... outros campos
}

@Test
public void devePreservarRegrasDeDominioAposConversao() {
    // Testa se as regras de domínio ainda funcionam após conversão
    Agendamento agendamento = criarAgendamentoTeste();
    
    // Converte ida e volta
    AgendamentoJpa jpa = adaptador.toJpa(agendamento);
    Agendamento agendamentoRecuperado = adaptador.toDominio(jpa);
    
    // Testa se as regras de domínio ainda funcionam
    assertThrows(IllegalArgumentException.class, () -> {
        agendamentoRecuperado.alterarHorario(new Horario(LocalTime.of(10, 0), LocalTime.of(9, 0)));
    });
}
```

### 3. Validação de Schema com ArchUnit

Use ArchUnit para garantir que as regras arquiteturais sejam respeitadas:

```java
@Test
public void entidadesDeDominioNaoDevemTerAnotacoesJPA() {
    classes()
        .that().resideInAPackage("..dominio..")
        .should().notBeAnnotatedWith(Entity.class)
        .andShould().notBeAnnotatedWith(Table.class)
        .andShould().notBeAnnotatedWith(Column.class);
}

@Test
public void entidadesJPADevemEstarNaInfraestrutura() {
    classes()
        .that().areAnnotatedWith(Entity.class)
        .should().resideInAPackage("..infraestrutura.persistencia..");
}
```

### 4. Geração Automática com Anotações Customizadas

Crie anotações para marcar campos que devem ser sincronizados:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SincronizarComJPA {
    String campo() default "";
}

// Na entidade de domínio
public class Agendamento {
    @SincronizarComJPA
    private AgendamentoId id;
    
    @SincronizarComJPA(campo = "clienteId")
    private ClienteId cliente;
    
    // ... outros campos
}
```

### 5. Padrão Factory para Criação Consistente

Use factories para garantir que a criação seja sempre consistente:

```java
@Component
public class AgendamentoFactory {
    
    public Agendamento criarDoBancoDeDados(AgendamentoJpa jpaEntity) {
        // Validações específicas
        if (jpaEntity.getHoraInicio().isAfter(jpaEntity.getHoraFim())) {
            throw new IllegalStateException("Dados inconsistentes no banco de dados");
        }
        
        return new Agendamento(
            new AgendamentoId(jpaEntity.getId()),
            new ClienteId(jpaEntity.getClienteId()),
            new ProfissionalId(jpaEntity.getProfissionalId()),
            new Horario(jpaEntity.getHoraInicio(), jpaEntity.getHoraFim()),
            jpaEntity.getStatus(),
            jpaEntity.getObservacoes()
        );
    }
}
```

### 6. Versionamento de Schema e Migração

Mantenha controle de versões do schema para detectar mudanças:

```sql
-- V2__adicionar_campo_observacoes_agendamento.sql
ALTER TABLE agendamento ADD COLUMN observacoes TEXT;

-- Sempre que adicionar um campo na JPA, adicione na entidade de domínio também
```

### 7. Documentação dos Mapeamentos

Mantenha documentação clara dos mapeamentos:

```java
/**
 * Mapeamento entre Agendamento (Domínio) e AgendamentoJpa (Infraestrutura)
 * 
 * Domínio               -> JPA
 * ----------------------------------------
 * AgendamentoId.valor   -> id (Long)
 * ClienteId.valor       -> clienteId (Long)
 * ProfissionalId.valor  -> profissionalId (Long)
 * Horario.inicio        -> horaInicio (LocalTime)
 * Horario.fim           -> horaFim (LocalTime)
 * status                -> status (String)
 * observacoes           -> observacoes (String)
 * 
 * ATENÇÃO: Sempre manter sincronizado!
 */
public class AgendamentoAdaptador {
    // implementação
}
```

### 8. Pipeline de CI/CD com Verificações

Configure seu pipeline para verificar consistência:

```yaml
# .github/workflows/consistency-check.yml
- name: Verificar Consistência de Modelos
  run: |
    mvn test -Dtest=*ConsistenciaTest
    mvn test -Dtest=*ArchTest
```

### 9. Estratégia de Versionamento de Entidades

Para mudanças evolutivas, use versionamento:

```java
public class AgendamentoV1 {
    // versão anterior
}

public class AgendamentoV2 extends AgendamentoV1 {
    // nova versão com migração automática
}
```

### 10. Monitoria em Runtime

Implemente logs e métricas para detectar inconsistências:

```java
@Component
public class ConsistenciaMonitor {
    
    @EventListener
    public void verificarConsistenciaAposSalvar(AgendamentoSalvoEvent event) {
        Agendamento dominio = event.getAgendamento();
        AgendamentoJpa jpa = agendamentoJpaRepository.findById(dominio.getId().getValor());
        
        if (!saoConsistentes(dominio, jpa)) {
            log.error("Inconsistência detectada para agendamento {}", dominio.getId());
            // Enviar alerta, corrigir, etc.
        }
    }
}
```

### Dicas Importantes

1. **Automatize ao Máximo**: Use testes, geração de código e validações automáticas
2. **Mantenha Simples**: Não complique demais os mapeamentos
3. **Documente Tudo**: Mapeamentos complexos devem ser bem documentados
4. **Use Ferramentas**: ArchUnit, MapStruct, etc. podem ajudar
5. **Monitore**: Implemente verificações de consistência em runtime
6. **Versione**: Mantenha controle de versões tanto do schema quanto das entidades

## Conclusão

A transição para DDD é um processo gradual que exige mudanças tanto na estrutura do código quanto na forma de pensar sobre o domínio. A principal mudança é colocar o domínio no centro da arquitetura, isolando-o de preocupações técnicas. 

Manter consistência entre entidades de domínio e JPA requer disciplina, automação e boas práticas. O investimento inicial em testes, adaptadores e monitoramento vale a pena pelo ganho em flexibilidade e manutenibilidade do código. Isso leva a um código mais alinhado com o negócio e mais adaptável a mudanças.