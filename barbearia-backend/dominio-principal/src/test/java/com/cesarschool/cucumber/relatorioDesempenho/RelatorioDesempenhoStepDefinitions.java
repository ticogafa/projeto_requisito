package com.cesarschool.cucumber.relatorioDesempenho;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimento;
import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.Avaliacao;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.RelatorioDesempenho;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.RelatorioDesempenhoServico;
import com.cesarschool.cucumber.relatorioDesempenho.infraestrutura.AvaliacaoMockRepositorio;
import com.cesarschool.cucumber.relatorioDesempenho.infraestrutura.ExecucaoAtendimentoMockRepositorio;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class RelatorioDesempenhoStepDefinitions {

    private final ExecucaoAtendimentoMockRepositorio execRepo = new ExecucaoAtendimentoMockRepositorio();
    private final AvaliacaoMockRepositorio avaliacaoRepo = new AvaliacaoMockRepositorio();
    private final RelatorioDesempenhoServico servico = new RelatorioDesempenhoServico(execRepo, avaliacaoRepo);

    private ProfissionalId profissionalId;
    private LocalDate dia;
    private RelatorioDesempenho relatorio;

    private final DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Dado("que existe um profissional com id {int}")
    public void que_existe_um_profissional_com_id(Integer id) {
        this.profissionalId = new ProfissionalId(id);
        execRepo.limpar();
        avaliacaoRepo.limpar();
    }

    @E("que o relatório é para o dia {string}")
    public void que_o_relatorio_e_para_o_dia(String diaStr) {
        this.dia = LocalDate.parse(diaStr);
    }

    @E("foi concluído um atendimento de {string} até {string} no valor de {double}")
    public void foi_concluido_um_atendimento_de_ate_no_valor_de(String inicioStr, String fimStr, Double valor) {
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, dt);
        LocalDateTime fim = LocalDateTime.parse(fimStr, dt);
        ExecucaoAtendimento exec = ExecucaoAtendimento.iniciar(this.profissionalId, valor, inicio);
        exec.finalizar(fim);
        execRepo.salvar(exec);
    }

    @E("existe um atendimento em andamento iniciado às {string} no valor de {double}")
    public void existe_um_atendimento_em_andamento_iniciado_as_no_valor_de(String inicioStr, Double valor) {
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, dt);
        ExecucaoAtendimento exec = ExecucaoAtendimento.iniciar(this.profissionalId, valor, inicio);
        execRepo.salvar(exec);
    }

    @E("foram registradas avaliações {string}")
    public void foram_registradas_avaliacoes(String notasCSV) {
        String[] partes = notasCSV.split(",");
        for (int i = 0; i < partes.length; i++) {
            int nota = Integer.parseInt(partes[i].trim());
            var quando = this.dia.atTime(12, 0).plusMinutes(5L * i);
            avaliacaoRepo.salvar(new Avaliacao(
                this.profissionalId, null, nota, quando
            ));
        }
    }

    @Quando("eu gerar o relatório de desempenho")
    public void eu_gerar_o_relatorio_de_desempenho() {
        this.relatorio = servico.gerarParaDia(this.profissionalId, this.dia);
    }

    @Entao("o tempo de serviço deve ser {double} minutos")
    public void o_tempo_de_servico_deve_ser_minutos(Double esperado) {
        System.out.println("[DEBUG] Valor esperado do .feature para 'tempo de serviço': " + esperado);
        assertEquals(esperado, relatorio.getTempoServico());
    }

    @Entao("a receita gerada deve ser {double}")
    public void a_receita_gerada_deve_ser(Double esperado) {
        assertEquals(esperado, relatorio.getReceitaGerada());
    }

    @Entao("o número de atendimentos deve ser {int}")
    public void o_numero_de_atendimentos_deve_ser(Integer esperado) {
        assertEquals(esperado.intValue(), relatorio.getNumeroClientesAtendidos());
    }

    @Entao("a avaliação média do funcionário deve ser {double}")
    public void a_avaliacao_media_do_funcionario_deve_ser(Double esperado) {
        System.out.println("[DEBUG] Valor esperado do .feature para 'avaliação média': " + esperado);
        assertEquals(esperado, relatorio.getAvaliacaoFuncionario());
    }
}