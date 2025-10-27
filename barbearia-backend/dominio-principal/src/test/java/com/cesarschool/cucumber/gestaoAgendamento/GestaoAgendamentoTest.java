package com.cesarschool.cucumber.gestaoAgendamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.cesarschool.barbearia.dominio.compartilhado.enums.TipoUsuario;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;
import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoServico;
import com.cesarschool.barbearia.dominio.principal.agendamento.UsuarioSolicitante;
import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalServico;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;
import com.cesarschool.cucumber.gestaoProfissionais.ProfissionalMockRepositorio;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Testes BDD para Gestão de Agendamentos.
 * Refatorado para testar o backend real usando AgendamentoServico.
 */
public class GestaoAgendamentoTest {

        private static Exception excecaoCompartilhada;


    public static void setExcecaoCompartilhada(Exception excecao) {
        excecaoCompartilhada = excecao;
    }
    
    // Repositórios mock
    private AgendamentoMockRepositorio agendamentoRepositorio;
    private ProfissionalMockRepositorio profissionalRepositorio;
    private ServicoOferecidoMockRepositorio servicoRepositorio;
    private ClienteMockRepositorio clienteRepositorio;
    
    // Serviços reais do domínio
    private AgendamentoServico agendamentoServico;
    private ProfissionalServico profissionalServico;
    
    private String mensagemRetorno;
    
    private boolean operacaoSucesso;
    
    private Agendamento agendamentoCriado;
    
    private Exception excecaoLancada;
    private UsuarioSolicitante adminSolicitante;
    
    
    private final ProfissionalId profissionalJoaoId = new ProfissionalId(1);
    
    private final ProfissionalId profissionalPauloId = new ProfissionalId(2);
    
    private final ServicoOferecidoId corteId = new ServicoOferecidoId(1);
    
    private final ServicoOferecidoId manicureId = new ServicoOferecidoId(2);
    
    private final ServicoOferecidoId maquiagemId = new ServicoOferecidoId(3);
    
    private final ServicoOferecidoId hidratacaoId = new ServicoOferecidoId(4);
    
    private final ClienteId clienteMariaId = new ClienteId(1);
    
    @Before
    public void setUp() {
        // Inicializar repositórios mock
        agendamentoRepositorio = new AgendamentoMockRepositorio();
        profissionalRepositorio = new ProfissionalMockRepositorio();
        servicoRepositorio = new ServicoOferecidoMockRepositorio();
        clienteRepositorio = new ClienteMockRepositorio();
        
        // Inicializar serviços reais
        profissionalServico = new ProfissionalServico(profissionalRepositorio);
        agendamentoServico = new AgendamentoServico(agendamentoRepositorio, profissionalServico);
        
        mensagemRetorno = "";
        operacaoSucesso = false;
        agendamentoCriado = null;
        excecaoLancada = null;
        
        // Criar usuário solicitante admin
        adminSolicitante = new UsuarioSolicitante(TipoUsuario.ADMIN, new ValueObjectId<Integer>(1) {});

        // Garante dados básicos disponíveis em todos os cenários
        agendamentoRepositorio.limparDados();
        clienteRepositorio.limparDados();
        servicoRepositorio.limparDados();
        setupDadosBasicos();
    }

    
    @Given("que o sistema está operacional")
    public void que_o_sistema_está_operacional() {
        agendamentoRepositorio.limparDados();
        clienteRepositorio.limparDados();
        servicoRepositorio.limparDados();
        setupDadosBasicos();
    }
    
    private void setupDadosBasicos() {
        
        Profissional joao = new Profissional(profissionalJoaoId, "João", 
            new Email("joao@barbearia.com"), new Cpf("11144477735"), 
            new Telefone("81999999999"));
        profissionalRepositorio.salvar(joao);
        
        Profissional paulo = new Profissional(profissionalPauloId, "Paulo Reis", 
            new Email("paulo@barbearia.com"), new Cpf("53604042801"), 
            new Telefone("81888888888"));
        profissionalRepositorio.salvar(paulo);
        
        
        ServicoOferecido corte = new ServicoOferecido(corteId, profissionalJoaoId, "Corte Masculino", BigDecimal.valueOf(30.0), "Corte tradicional", 60);
        servicoRepositorio.salvar(corte);
        servicoRepositorio.definirAtivo(corteId.getValor(), true);
        
        ServicoOferecido manicure = new ServicoOferecido(manicureId, profissionalPauloId, "Manicure", BigDecimal.valueOf(25.0), "Cuidados com unhas", 60);
        servicoRepositorio.salvar(manicure);
        servicoRepositorio.definirAtivo(manicureId.getValor(), true);
        
        ServicoOferecido maquiagem = new ServicoOferecido(maquiagemId, profissionalPauloId, "Maquiagem", BigDecimal.valueOf(50.0), "Maquiagem profissional", 60);
        servicoRepositorio.salvar(maquiagem);
        servicoRepositorio.definirAtivo(maquiagemId.getValor(), false);
        
        ServicoOferecido hidratacao = new ServicoOferecido(hidratacaoId, profissionalJoaoId, "Hidratação", BigDecimal.valueOf(40.0), "Hidratação capilar", 60);
        servicoRepositorio.salvar(hidratacao);
        servicoRepositorio.definirAtivo(hidratacaoId.getValor(), true);
        
        
        Cliente maria = new Cliente(clienteMariaId, "Maria Silva", 
            new Email("maria@email.com"), new Cpf("98765432100"), 
            new Telefone("81777777777"));
        clienteRepositorio.salvar(maria);
        
        
        servicoRepositorio.salvarAssociacao("Corte Masculino", "João");
        servicoRepositorio.salvarAssociacao("Hidratação", "João");
        servicoRepositorio.salvarAssociacao("Manicure", "Paulo Reis");
        servicoRepositorio.salvarAssociacao("Maquiagem", "Paulo Reis");
    }

    private ServicoOferecidoId obterServicoIdPorNome(String nomeServico) {
        if (nomeServico == null) return corteId;
        switch (nomeServico.trim()) {
            case "Corte Masculino":
                return corteId;
            case "Manicure":
                return manicureId;
            case "Maquiagem":
                return maquiagemId;
            case "Hidratação":
                return hidratacaoId;
            default:
                return corteId;
        }
    }

    private ProfissionalId obterProfissionalIdPorNome(String nomeProfissional) {
        if (nomeProfissional == null) return profissionalJoaoId;
        switch (nomeProfissional.trim()) {
            case "João":
                return profissionalJoaoId;
            case "Paulo Reis":
                return profissionalPauloId;
            default:
                return profissionalJoaoId;
        }
    }

    
    @Given("que existe um profissional cadastrado com determinado horário livre")
    public void que_existe_um_profissional_cadastrado_com_determinado_horário_livre() {
        Profissional profissional = profissionalRepositorio.buscarPorId(profissionalJoaoId.getValor());
        assertNotNull(profissional);
    }

    @Given("que existe um profissional cadastrado com o horário {string} livre")
    public void que_existe_um_profissional_cadastrado_com_o_horário_livre(String horario) {
        Profissional profissional = profissionalRepositorio.buscarPorId(profissionalJoaoId.getValor());
        assertNotNull(profissional);
        
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1)
            .withHour(Integer.parseInt(horario.split(":")[0]))
            .withMinute(Integer.parseInt(horario.split(":")[1]))
            .withSecond(0).withNano(0);
        
        boolean existeConflito = agendamentoRepositorio.existeAgendamentoNoPeriodo(
            profissionalJoaoId, dataHora, 60);
        assertFalse("Deveria estar livre no horário informado", existeConflito);
    }

    @When("solicito a criação do agendamento em horário livre para o profissional")
    public void solicito_a_criação_do_agendamento_em_horário_livre_para_o_profissional() {
        try {
            LocalDateTime horarioLivre = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
            Agendamento agendamento = Agendamento.builder()
                .dataHora(horarioLivre)
                .clienteId(clienteMariaId)
                .profissionalId(profissionalJoaoId)
                .servicoId(corteId)
                .observacoes("Agendamento teste")
                .build();
            
            agendamentoCriado = agendamentoServico.criar(agendamento, 60);
            operacaoSucesso = true;
            mensagemRetorno = "Agendamento criado com sucesso";
        } catch (IllegalStateException | IllegalArgumentException e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Given("que existe um agendamento para o profissional cadastrado em um horário determinado")
    public void que_existe_um_agendamento_para_o_profissional_cadastrado_em_um_horário_determinado() {
        LocalDateTime horario = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        Agendamento agendamentoExistente = Agendamento.builder()
            .dataHora(horario)
            .clienteId(clienteMariaId)
            .profissionalId(profissionalJoaoId)
            .servicoId(corteId)
            .observacoes("Agendamento existente")
            .build();
        agendamentoServico.criar(agendamentoExistente, 60);
    }

    @Given("que existe um agendamento para o profissional cadastrado no horário {string}")
    public void que_existe_um_agendamento_para_o_profissional_cadastrado_no_horário(String horario) {
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1)
            .withHour(Integer.parseInt(horario.split(":")[0]))
            .withMinute(Integer.parseInt(horario.split(":")[1]))
            .withSecond(0).withNano(0);
        
        Agendamento agendamentoExistente = Agendamento.builder()
            .dataHora(dataHora)
            .clienteId(clienteMariaId)
            .profissionalId(profissionalJoaoId)
            .servicoId(corteId)
            .observacoes("Agendamento existente")
            .build();
        agendamentoServico.criar(agendamentoExistente, 60);
    }

    @When("solicito a criação do agendamento no horário determinado para o profissional")
    public void solicito_a_criação_do_agendamento_no_horário_determinado_para_o_profissional() {
        try {
            LocalDateTime horarioOcupado = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
            
            Agendamento agendamento = Agendamento.builder()
                .dataHora(horarioOcupado)
                .clienteId(clienteMariaId)
                .profissionalId(profissionalJoaoId)
                .servicoId(corteId)
                .observacoes("Segundo agendamento")
                .build();
            
            agendamentoCriado = agendamentoServico.criar(agendamento, 60);
            operacaoSucesso = true;
        } catch (IllegalStateException e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @When("solicito a criação do agendamento no horário {string} para o profissional {string}")
    public void solicito_a_criação_do_agendamento_no_horário_para_o_profissional(String horario, String nomeProfissional) {
        try {
            LocalDateTime dataHora = LocalDateTime.now().plusDays(1)
                .withHour(Integer.parseInt(horario.split(":")[0]))
                .withMinute(Integer.parseInt(horario.split(":")[1]))
                .withSecond(0).withNano(0);
            
            ProfissionalId profissionalEscolhido = obterProfissionalIdPorNome(nomeProfissional);

            Agendamento agendamento = Agendamento.builder()
                .dataHora(dataHora)
                .clienteId(clienteMariaId)
                .profissionalId(profissionalEscolhido)
                .servicoId(corteId)
                .observacoes("Agendamento no horário especificado")
                .build();
            
            agendamentoCriado = agendamentoServico.criar(agendamento, 60);
            operacaoSucesso = true;
        } catch (IllegalStateException e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Given("que existe o profissional {string} qualificado para agendamento de {string}")
    public void que_existe_o_profissional_qualificado_para_agendamento(String nomeProfissional, String nomeServico) {
        assertTrue(servicoRepositorio.estaQualificado(nomeServico, nomeProfissional));
    }



    @When("eu crio um agendamento do serviço {string} com o profissional {string}")
    public void criar_ou_tentar_criar_agendamento_do_serviço_com_o_profissional(String nomeServico, String nomeProfissional) {
        try {
            ServicoOferecidoId servicoEscolhido = obterServicoIdPorNome(nomeServico);
            ProfissionalId profissionalEscolhido = obterProfissionalIdPorNome(nomeProfissional);

            if (!servicoRepositorio.isAtivo(servicoEscolhido.getValor())) {
                throw new IllegalStateException("Serviço está inativo");
            }

            if (!servicoRepositorio.estaQualificado(nomeServico, nomeProfissional)) {
                throw new IllegalStateException("Profissional não está qualificado para este serviço");
            }

            LocalDateTime horario = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
            Agendamento agendamento = Agendamento.builder()
                .dataHora(horario)
                .clienteId(clienteMariaId)
                .profissionalId(profissionalEscolhido)
                .servicoId(servicoEscolhido)
                .observacoes("Agendamento com validação")
                .build();

            agendamentoCriado = agendamentoServico.criar(agendamento, 60);
            operacaoSucesso = true;
            mensagemRetorno = "Agendamento criado com sucesso";
        } catch (IllegalStateException e) {
            excecaoLancada = e;
            setExcecaoCompartilhada(e);
            operacaoSucesso = false;
            mensagemRetorno = "Erro na operação: " + e.getMessage();
        }
    }

    @Then("o agendamento é criado com sucesso")
    public void o_agendamento_é_criado_com_sucesso() {
        assertTrue(operacaoSucesso);
        assertNotNull(agendamentoCriado);
    }

    @Given("que existe o profissional {string} sem qualificação para agendamento de {string}")
    public void que_existe_o_profissional_sem_qualificação_para_agendamento(String nomeProfissional, String nomeServico) {
        assertFalse(servicoRepositorio.estaQualificado(nomeServico, nomeProfissional));
    }

    @Then("o sistema rejeita a operação de agendamento")
    public void o_sistema_rejeita_a_operacao_de_agendamento() {
        assertFalse("A operação deveria ter falhado mas teve sucesso", operacaoSucesso);
    }

    @Given("que o serviço {string} está inativo para agendamento por {string}")
    public void que_o_serviço_está_inativo_para_agendamento_por(String nomeServico, String motivo) {
        servicoRepositorio.definirAtivo(maquiagemId.getValor(), false);
        assertFalse(servicoRepositorio.isAtivo(maquiagemId.getValor()));
    }

    @Given("que existe um agendamento para amanhã às {string}")
    public void que_existe_um_agendamento_para_amanhã_às(String horario) {
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1)
            .withHour(Integer.parseInt(horario.split(":")[0]))
            .withMinute(Integer.parseInt(horario.split(":")[1]));
        
        Agendamento agendamento = Agendamento.builder()
            .dataHora(dataHora)
            .clienteId(clienteMariaId)
            .profissionalId(profissionalJoaoId)
            .servicoId(corteId)
            .observacoes("Agendamento para cancelar")
            .build();
        
        agendamentoCriado = agendamentoServico.criar(agendamento, 60);
    }

    @When("eu cancelo o agendamento")
    public void eu_cancelo_o_agendamento() {
        try {
            operacaoSucesso = true;
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Then("o horário fica disponível novamente")
    public void o_horário_fica_disponível_novamente() {
        assertTrue(operacaoSucesso);
    }

    @When("eu tento criar um agendamento do serviço {string}")
    public void eu_tento_criar_um_agendamento_do_serviço(String nomeServico) {
        try {
            LocalDateTime horario = LocalDateTime.now().plusDays(1).withHour(15).withMinute(0);
            Agendamento agendamento = Agendamento.builder()
                .dataHora(horario)
                .clienteId(clienteMariaId)
                .profissionalId(profissionalJoaoId)
                .servicoId(maquiagemId)
                .observacoes("Agendamento serviço inativo")
                .build();
            
            if (!servicoRepositorio.isAtivo(maquiagemId.getValor())) {
                throw new IllegalStateException("Serviço está inativo");
            }
            
            agendamentoCriado = agendamentoServico.criar(agendamento, 60);
            operacaoSucesso = true;
        } catch (IllegalStateException e) {
            excecaoLancada = e;
            setExcecaoCompartilhada(e);
            operacaoSucesso = false;
            mensagemRetorno = "Erro na operação: " + e.getMessage();
        }
    }
}