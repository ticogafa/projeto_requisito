# Guia: Como Seguir o Padrão do Saulo para Testes com Repositórios

## Resumo do Padrão

O padrão do Saulo para testes de domínio envolve:

1. **Criar repositórios mock/fake** para simular o comportamento do repositório real
2. **Instanciar serviços com os mocks** injetando as dependências
3. **Configurar cenários específicos** através de variações dos mocks
4. **Executar ações via serviços** (não diretamente no repositório)
5. **Validar resultados** com asserts adequados

## Estrutura Implementada no Projeto Barbearia

### 1. Repositórios Mock

Criamos classes mock que implementam as interfaces de repositório:

```java
// Mock padrão - simula sucesso
class AgendamentoMockRepositorio implements AgendamentoRepositorio {
    private Agendamento ultimoAgendamentoSalvo;

    @Override
    public Agendamento salvar(Agendamento agendamento) {
        ultimoAgendamentoSalvo = agendamento;
        return agendamento;
    }

    @Override
    public Agendamento buscarPorId(Integer id) {
        return ultimoAgendamentoSalvo;
    }
    // ... outros métodos
}

// Mock especializado - simula conflito de horário
class AgendamentoConflitoRepositorio extends AgendamentoMockRepositorio {
    @Override
    public boolean existeAgendamentoNoPeriodo(...) {
        return true; // Sempre retorna que há conflito
    }
}

// Mock especializado - simula ausência de profissional
class ProfissionalSemDisponivelRepositorio extends ProfissionalMockRepositorio {
    @Override
    public Profissional buscarPrimeiroProfissionalDisponivel(...) {
        throw new IllegalStateException("Não há profissionais disponíveis");
    }
}
```

### 2. Injeção de Dependências nos Testes

```java
public class AgendamentoTest {
    private AgendamentoRepositorio repositorio;
    private ProfissionalRepositorio repositorioProfissional;
    private ProfissionalServico profissionalServico;
    private AgendamentoServico servico;

    // Inicialização padrão
    private void inicializarServicoPadrao() {
        repositorio = new AgendamentoMockRepositorio();
        repositorioProfissional = new ProfissionalMockRepositorio();
        profissionalServico = new ProfissionalServico(repositorioProfissional);
        servico = new AgendamentoServico(repositorio, profissionalServico);
    }
}
```

### 3. Configuração de Cenários

Em cada cenário BDD, configure o mock apropriado:

```java
@Given("que já existe um agendamento ativo para o profissional {string} no mesmo horário")
public void existeAgendamentoAtivoMesmoHorario(String nomeProfissional) {
    // Troca o mock para simular conflito
    repositorio = new AgendamentoConflitoRepositorio();
    servico = new AgendamentoServico(repositorio, profissionalServico);
    horario = LocalDateTime.now().plusHours(2);
}

@When("o cliente tenta criar outro agendamento para o mesmo horário")
public void clienteTentaCriarOutroAgendamentoMesmoHorario() {
    agendamento = AgendamentoFactory.criarParaHorario(horario);
    lancou = false;
    try {
        servico.criar(agendamento, 30);
    } catch (IllegalStateException e) {
        lancou = true;
    }
}

@Then("o sistema deve recusar a criação por conflito de horário")
public void sistemaRecusaCriacaoPorConflito() {
    assertTrue("Deveria recusar a criação por conflito de horário", lancou);
}
```

### 4. Factories para Criar Objetos de Teste

Use factories para criar objetos de domínio de forma consistente:

```java
public final class AgendamentoFactory {
    public static Agendamento criarPadrao() {
        return Agendamento.builder()
            .dataHora(LocalDateTime.now().plusHours(2))
            .clienteId(new ClienteId(1))
            .profissionalId(new ProfissionalId(1))
            .servicoId(new ServicoOferecidoId(1))
            .observacoes("Agendamento padrão para testes")
            .build();
    }

    public static Agendamento criarParaHorario(LocalDateTime dataHora) {
        Agendamento agendamento = criarPadrao();
        agendamento.setDataHora(dataHora);
        return agendamento;
    }

    public static Agendamento criarComStatus(StatusAgendamento status) {
        LocalDateTime horarioProximo = LocalDateTime.now().plusMinutes(90);
        Agendamento agendamento = criarParaHorario(horarioProximo);
        agendamento.setStatus(status);
        agendamento.setId(new AgendamentoId(1)); // Simula agendamento já salvo
        return agendamento;
    }
}
```

## Vantagens do Padrão

✅ **Isolamento**: Testes não dependem de banco de dados real  
✅ **Velocidade**: Testes executam rapidamente  
✅ **Controle**: Fácil simular diferentes cenários (sucesso, erro, conflito)  
✅ **Clareza**: Cada mock tem uma responsabilidade específica  
✅ **Manutenibilidade**: Fácil adicionar novos cenários  

## Checklist para Novos Testes

- [ ] Criar mock do repositório implementando a interface
- [ ] Criar variações do mock para cenários específicos (conflito, erro, etc)
- [ ] Criar factory para objetos de domínio
- [ ] Injetar mock no serviço que será testado
- [ ] Configurar o mock apropriado em cada cenário `@Given`
- [ ] Executar a ação via serviço no `@When`
- [ ] Validar o resultado no `@Then`
- [ ] Garantir que horários de teste estão dentro das regras de negócio

## Resultado Final

Todos os 6 cenários de teste do `AgendamentoTest` passaram com sucesso:

✔ Criar agendamento em horário livre com sucesso  
✔ Impedir criação de agendamento quando o horário já está ocupado  
✔ Bloquear agendamento fora da jornada de trabalho  
✔ Impedir cancelamento com menos de duas horas de antecedência  
✔ Atribuir profissional automaticamente quando não informado  
✔ Falhar ao atribuir profissional quando não há disponíveis  

**BUILD SUCCESS** ✨
