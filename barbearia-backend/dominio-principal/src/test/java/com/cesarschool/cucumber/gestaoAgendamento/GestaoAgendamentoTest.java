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
    
    
    private AgendamentoMockRepositorio agendamentoRepositorio;
    private ProfissionalMockRepositorio profissionalRepositorio;
    private ServicoOferecidoMockRepositorio servicoRepositorio;
    private ClienteMockRepositorio clienteRepositorio;
    
    
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
        
        agendamentoRepositorio = new AgendamentoMockRepositorio();
        profissionalRepositorio = new ProfissionalMockRepositorio();
        servicoRepositorio = new ServicoOferecidoMockRepositorio();
        clienteRepositorio = new ClienteMockRepositorio();
        
        
        profissionalServico = new ProfissionalServico(profissionalRepositorio);
        agendamentoServico = new AgendamentoServico(agendamentoRepositorio, profissionalServico, servicoRepositorio);
        
        mensagemRetorno = "";
        operacaoSucesso = false;
        agendamentoCriado = null;
        excecaoLancada = null;
        
        
        adminSolicitante = new UsuarioSolicitante(TipoUsuario.ADMIN, new ValueObjectId<Integer>(1) {});

        
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
        
        
        ServicoOferecido corte = new ServicoOferecido(corteId, "Corte Masculino", BigDecimal.valueOf(30.0), "Corte tradicional", 60);
        servicoRepositorio.salvar(corte);
        servicoRepositorio.definirAtivo(corteId.getValor(), true);
        
        ServicoOferecido manicure = new ServicoOferecido(manicureId, "Manicure", BigDecimal.valueOf(25.0), "Cuidados com unhas", 60);
        servicoRepositorio.salvar(manicure);
        servicoRepositorio.definirAtivo(manicureId.getValor(), true);
        
        ServicoOferecido maquiagem = new ServicoOferecido(maquiagemId, "Maquiagem", BigDecimal.valueOf(50.0), "Maquiagem profissional", 60);
        servicoRepositorio.salvar(maquiagem);
        servicoRepositorio.definirAtivo(maquiagemId.getValor(), false);
        
        ServicoOferecido hidratacao = new ServicoOferecido(hidratacaoId, "Hidratação", BigDecimal.valueOf(40.0), "Hidratação capilar", 60);
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
        
        
        profissionalRepositorio.adicionarQualificacao(profissionalJoaoId, corteId);
        profissionalRepositorio.adicionarQualificacao(profissionalJoaoId, hidratacaoId);
        profissionalRepositorio.adicionarQualificacao(profissionalPauloId, manicureId);
        profissionalRepositorio.adicionarQualificacao(profissionalPauloId, maquiagemId);
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
            Agendamento agendamento = new Agendamento(
                horarioLivre,
                clienteMariaId,
                profissionalJoaoId,
                corteId,
                "Agendamento teste"
            );
            
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
        Agendamento agendamentoExistente = new Agendamento(
            horario,
            clienteMariaId,
            profissionalJoaoId,
            corteId,
            "Agendamento existente"
        );
        agendamentoServico.criar(agendamentoExistente, 60);
    }

    @Given("que existe um agendamento para o profissional cadastrado no horário {string}")
    public void que_existe_um_agendamento_para_o_profissional_cadastrado_no_horário(String horario) {
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1)
            .withHour(Integer.parseInt(horario.split(":")[0]))
            .withMinute(Integer.parseInt(horario.split(":")[1]))
            .withSecond(0).withNano(0);
        
        Agendamento agendamentoExistente = new Agendamento(
            dataHora,
            clienteMariaId,
            profissionalJoaoId,
            corteId,
            "Agendamento existente"
        );
        agendamentoServico.criar(agendamentoExistente, 60);
    }

    @When("solicito a criação do agendamento no horário determinado para o profissional")
    public void solicito_a_criação_do_agendamento_no_horário_determinado_para_o_profissional() {
        try {
            LocalDateTime horarioOcupado = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
            
            Agendamento agendamento = new Agendamento(
                horarioOcupado,
                clienteMariaId,
                profissionalJoaoId,
                corteId,
                "Segundo agendamento"
            );
            
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

            Agendamento agendamento = new Agendamento(
                dataHora,
                clienteMariaId,
                profissionalEscolhido,
                corteId,
                "Agendamento no horário especificado"
            );
            
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

            
            LocalDateTime horario = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
            Agendamento agendamento = new Agendamento(
                horario,
                clienteMariaId,
                profissionalEscolhido,
                servicoEscolhido,
                "Agendamento com validação"
            );

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
        
        Agendamento agendamento = new Agendamento(
            dataHora,
            clienteMariaId,
            profissionalJoaoId,
            corteId,
            "Agendamento para cancelar"
        );
        
        agendamentoCriado = agendamentoServico.criar(agendamento, 60);
    }

    @When("eu cancelo o agendamento")
    public void eu_cancelo_o_agendamento() {
        try {
            
            if (agendamentoCriado == null) {
                throw new IllegalStateException("Nenhum agendamento foi criado para cancelar");
            }
            if (agendamentoCriado.getId() == null) {
                throw new IllegalStateException("Agendamento criado não possui ID");
            }
            
            
            agendamentoServico.cancelar(agendamentoCriado.getId(), adminSolicitante);
            operacaoSucesso = true;
            mensagemRetorno = "Agendamento cancelado com sucesso";
        } catch (Exception e) {
            excecaoLancada = e;
            setExcecaoCompartilhada(e);
            operacaoSucesso = false;
            mensagemRetorno = "Erro ao cancelar: " + e.getMessage();
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
            Agendamento agendamento = new Agendamento(
                horario,
                clienteMariaId,
                profissionalJoaoId,
                maquiagemId,
                "Agendamento serviço inativo"
            );
            
            
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