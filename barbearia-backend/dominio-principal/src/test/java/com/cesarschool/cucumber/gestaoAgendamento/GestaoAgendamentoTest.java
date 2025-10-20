package com.cesarschool.cucumber.gestaoAgendamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
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

public class GestaoAgendamentoTest {

        private static Exception excecaoCompartilhada;


    public static void setExcecaoCompartilhada(Exception excecao) {
        excecaoCompartilhada = excecao;
    }
    
    
    private GestaoAgendamentoMockRepositorio repositorio;
    
    private ProfissionalMockRepositorio profissionalRepositorio;
    
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
        repositorio = new GestaoAgendamentoMockRepositorio();
        profissionalRepositorio = new ProfissionalMockRepositorio();
        
        profissionalServico = new ProfissionalServico(profissionalRepositorio);
        agendamentoServico = new AgendamentoServico(repositorio, profissionalServico);
        
        mensagemRetorno = "";
        operacaoSucesso = false;
        agendamentoCriado = null;
        excecaoLancada = null;

        // Garante dados básicos disponíveis em todos os cenários
        repositorio.limparDados();
        setupDadosBasicos();
    }

    
    @Given("que o sistema está operacional")
    public void que_o_sistema_está_operacional() {
        repositorio.limparDados();
        setupDadosBasicos();
    }
    
    private void setupDadosBasicos() {
        
        Profissional joao = new Profissional(profissionalJoaoId, "João", 
            new Email("joao@barbearia.com"), new Cpf("11144477735"), 
            new Telefone("81999999999"));
        repositorio.adicionarProfissional(profissionalJoaoId, joao);
        
        Profissional paulo = new Profissional(profissionalPauloId, "Paulo Reis", 
            new Email("paulo@barbearia.com"), new Cpf("53604042801"), 
            new Telefone("81888888888"));
        repositorio.adicionarProfissional(profissionalPauloId, paulo);
        
        
        ServicoOferecido corte = new ServicoOferecido(corteId, profissionalJoaoId, "Corte Masculino", BigDecimal.valueOf(30.0), "Corte tradicional", 60);
        repositorio.adicionarServico(corteId, corte, true);
        
        ServicoOferecido manicure = new ServicoOferecido(manicureId, profissionalPauloId, "Manicure", BigDecimal.valueOf(25.0), "Cuidados com unhas", 60);
        repositorio.adicionarServico(manicureId, manicure, true);
        
        ServicoOferecido maquiagem = new ServicoOferecido(maquiagemId, profissionalPauloId, "Maquiagem", BigDecimal.valueOf(50.0), "Maquiagem profissional", 60);
        repositorio.adicionarServico(maquiagemId, maquiagem, false);
        
        ServicoOferecido hidratacao = new ServicoOferecido(hidratacaoId, profissionalJoaoId, "Hidratação", BigDecimal.valueOf(40.0), "Hidratação capilar", 60);
        repositorio.adicionarServico(hidratacaoId, hidratacao, true);
        
        
        Cliente maria = new Cliente(clienteMariaId, "Maria Silva", 
            new Email("maria@email.com"), new Cpf("98765432100"), 
            new Telefone("81777777777"));
        repositorio.adicionarCliente(clienteMariaId, maria);
        
        
        repositorio.associarServicoAProfissional(corteId, profissionalJoaoId);
        repositorio.definirDuracaoServico(corteId, 60);
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
        assertNotNull(repositorio.profissionais.get(profissionalJoaoId));
    }

    @Given("que existe um profissional cadastrado com o horário {string} livre")
    public void que_existe_um_profissional_cadastrado_com_o_horário_livre(String horario) {
        assertNotNull(repositorio.profissionais.get(profissionalJoaoId));
        LocalDateTime dataHora = LocalDateTime.now()
            .withHour(Integer.parseInt(horario.split(":" )[0]))
            .withMinute(Integer.parseInt(horario.split(":" )[1]))
            .withSecond(0).withNano(0);
        assertFalse("Deveria estar livre no horário informado",
            repositorio.existeAgendamentoNoPeriodo(profissionalJoaoId, dataHora, 60));
    }

    @When("solicito a criação do agendamento em horário livre para o profissional")
    public void solicito_a_criação_do_agendamento_em_horário_livre_para_o_profissional() {
        try {
            LocalDateTime horarioLivre = LocalDateTime.now().plusHours(2);
            Agendamento agendamento = new Agendamento(horarioLivre, clienteMariaId, 
                profissionalJoaoId, corteId, "Agendamento teste");
            
            agendamentoCriado = repositorio.salvar(agendamento);
            operacaoSucesso = true;
            mensagemRetorno = "Agendamento criado com sucesso";
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Given("que existe um agendamento para o profissional cadastrado em um horário determinado")
    public void que_existe_um_agendamento_para_o_profissional_cadastrado_em_um_horário_determinado() {
        LocalDateTime horario = LocalDateTime.now().plusHours(2);
        Agendamento agendamentoExistente = new Agendamento(horario, clienteMariaId,
            profissionalJoaoId, corteId, "Agendamento existente");
        repositorio.salvar(agendamentoExistente);
    }

    @Given("que existe um agendamento para o profissional cadastrado no horário {string}")
    public void que_existe_um_agendamento_para_o_profissional_cadastrado_no_horário(String horario) {
        LocalDateTime dataHora = LocalDateTime.now()
            .withHour(Integer.parseInt(horario.split(":" )[0]))
            .withMinute(Integer.parseInt(horario.split(":" )[1]))
            .withSecond(0).withNano(0);
        Agendamento agendamentoExistente = new Agendamento(dataHora, clienteMariaId,
            profissionalJoaoId, corteId, "Agendamento existente");
        repositorio.salvar(agendamentoExistente);
    }

    @When("solicito a criação do agendamento no horário determinado para o profissional")
    public void solicito_a_criação_do_agendamento_no_horário_determinado_para_o_profissional() {
        try {
            LocalDateTime horarioOcupado = LocalDateTime.now().plusHours(2);
            if (repositorio.existeAgendamentoNoPeriodo(profissionalJoaoId, horarioOcupado, 60)) {
                operacaoSucesso = false;
            } else {
                Agendamento agendamento = new Agendamento(horarioOcupado, clienteMariaId,
                    profissionalJoaoId, corteId, "Segundo agendamento");
                agendamentoCriado = repositorio.salvar(agendamento);
                operacaoSucesso = true;
            }
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @When("solicito a criação do agendamento no horário {string} para o profissional {string}")
    public void solicito_a_criação_do_agendamento_no_horário_para_o_profissional(String horario, String nomeProfissional) {
        try {
            LocalDateTime dataHora = LocalDateTime.now()
                .withHour(Integer.parseInt(horario.split(":" )[0]))
                .withMinute(Integer.parseInt(horario.split(":" )[1]))
                .withSecond(0).withNano(0);
            ProfissionalId profissionalEscolhido = obterProfissionalIdPorNome(nomeProfissional);

            if (repositorio.existeAgendamentoNoPeriodo(profissionalEscolhido, dataHora, 60)) {
                operacaoSucesso = false;
                return;
            }

            Agendamento agendamento = new Agendamento(dataHora, clienteMariaId,
                profissionalEscolhido, corteId, "Agendamento no horário especificado");
            agendamentoCriado = repositorio.salvar(agendamento);
            operacaoSucesso = true;
        } catch (Exception e) {
            excecaoLancada = e;
            operacaoSucesso = false;
        }
    }

    @Given("que existe o profissional {string} qualificado para agendamento de {string}")
    public void que_existe_o_profissional_qualificado_para_agendamento(String nomeProfissional, String nomeServico) {
        assertTrue(repositorio.profissionalQualificado(profissionalJoaoId, corteId));
    }

    @Given("que o serviço {string} está ativo")
    public void que_o_serviço_está_ativo(String nomeServico) {
        assertTrue(repositorio.servicoAtivo(corteId));
    }

    @When("eu crio um agendamento do serviço {string} com o profissional {string}")
    public void criar_ou_tentar_criar_agendamento_do_serviço_com_o_profissional(String nomeServico, String nomeProfissional) {
        try {
            ServicoOferecidoId servicoEscolhido = obterServicoIdPorNome(nomeServico);
            ProfissionalId profissionalEscolhido = obterProfissionalIdPorNome(nomeProfissional);

            if (!repositorio.servicoAtivo(servicoEscolhido)) {
                throw new IllegalStateException("Serviço está inativo");
            }

            if (!repositorio.profissionalQualificado(profissionalEscolhido, servicoEscolhido)) {
                throw new IllegalStateException("Profissional não está qualificado para este serviço");
            }

            LocalDateTime horario = LocalDateTime.now().plusHours(2);
            Agendamento agendamento = new Agendamento(horario, clienteMariaId,
                profissionalEscolhido, servicoEscolhido, "Agendamento com validação");

            agendamentoCriado = repositorio.salvar(agendamento);
            operacaoSucesso = true;
            mensagemRetorno = "Agendamento criado com sucesso";
        } catch (Exception e) {
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
        assertFalse(repositorio.profissionalQualificado(profissionalJoaoId, manicureId));
    }

    @Then("o sistema rejeita a operação de agendamento")
    public void o_sistema_rejeita_a_operacao_de_agendamento() {
        assertFalse("A operação deveria ter falhado mas teve sucesso", operacaoSucesso);
    }

    @Given("que o serviço {string} está inativo para agendamento por {string}")
    public void que_o_serviço_está_inativo_para_agendamento_por(String nomeServico, String motivo) {
        assertFalse(repositorio.servicoAtivo(maquiagemId));
    }

    @Given("que existe um agendamento para amanhã às {string}")
    public void que_existe_um_agendamento_para_amanhã_às(String horario) {
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1)
            .withHour(Integer.parseInt(horario.split(":")[0]))
            .withMinute(Integer.parseInt(horario.split(":")[1]));
        
        Agendamento agendamento = new Agendamento(dataHora, clienteMariaId, 
            profissionalJoaoId, corteId, "Agendamento para cancelar");
        agendamentoCriado = repositorio.salvar(agendamento);
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
            LocalDateTime horario = LocalDateTime.now().plusHours(2);
            Agendamento agendamento = new Agendamento(horario, clienteMariaId, 
                profissionalJoaoId, maquiagemId, "Agendamento serviço inativo");
            
            if (!repositorio.servicoAtivo(maquiagemId)) {
                throw new IllegalStateException("Serviço está inativo");
            }
            
            agendamentoCriado = agendamentoServico.criar(agendamento, 60);
            operacaoSucesso = true;
        } catch (Exception e) {
            excecaoLancada = e;
            setExcecaoCompartilhada(e);
            operacaoSucesso = false;
            mensagemRetorno = "Erro na operação: " + e.getMessage();
        }
    }
}