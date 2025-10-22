package com.cesarschool.cucumber.relatorioDesempenho;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimento;
import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.Avaliacao;
import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.Nota;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.RelatorioDesempenho;
import com.cesarschool.barbearia.dominio.principal.profissional.relatorio.RelatorioDesempenhoServico;
import com.cesarschool.cucumber.relatorioDesempenho.infraestrutura.AvaliacaoMockRepositorio;
import com.cesarschool.cucumber.relatorioDesempenho.infraestrutura.ExecucaoAtendimentoMockRepositorio;

import io.cucumber.java.pt.Dado;
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

    private Double parseDoublePt(String valorStr) {
        return Double.parseDouble(valorStr.replace(',', '.'));
    }

    @Dado("que existe um profissional com id {int}")
    public void que_existe_um_profissional_com_id(Integer id) {
        this.profissionalId = new ProfissionalId(id);
        execRepo.limpar();
        avaliacaoRepo.limpar();
    }

    @Dado("que o relatório é para o dia {string}")
    public void que_o_relatorio_e_para_o_dia(String diaStr) {
        this.dia = LocalDate.parse(diaStr);
    }

    @Dado("foi concluído um atendimento de {string} até {string} no valor de {string}")
    public void foi_concluido_um_atendimento_de_ate_no_valor_de(String inicioStr, String fimStr, String valorStr) {
        Double valor = parseDoublePt(valorStr);
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, dt);
        LocalDateTime fim = LocalDateTime.parse(fimStr, dt);
        ExecucaoAtendimento exec = ExecucaoAtendimento.iniciar(this.profissionalId, valor, inicio);
        exec.finalizar(fim);
        execRepo.salvar(exec);
    }

    @Dado("existe um atendimento em andamento iniciado às {string} no valor de {string}")
    public void existe_um_atendimento_em_andamento_iniciado_as_no_valor_de(String inicioStr, String valorStr) {
        Double valor = parseDoublePt(valorStr);
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, dt);
        ExecucaoAtendimento exec = ExecucaoAtendimento.iniciar(this.profissionalId, valor, inicio);
        execRepo.salvar(exec);
    }

    @Dado("foram registradas avaliações {string}")
    public void foram_registradas_avaliacoes(String notasCSV) {
        String[] partes = notasCSV.split(",");
        for (int i = 0; i < partes.length; i++) {
            int notaInt = Integer.parseInt(partes[i].trim());
            Nota nota = new Nota(notaInt);
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

    @Entao("o tempo de serviço deve ser {string} minutos")
    public void o_tempo_de_servico_deve_ser_minutos(String esperadoStr) {
        Double esperado = parseDoublePt(esperadoStr);
        assertEquals(esperado, relatorio.getTempoServico(), 0.01);
    }

    @Entao("a receita gerada deve ser {string}")
    public void a_receita_gerada_deve_ser(String esperadoStr) {
        
        BigDecimal esperado = new BigDecimal(esperadoStr.replace(',', '.'));
        assertEquals(0, esperado.compareTo(relatorio.getReceitaGerada()), "A receita gerada não corresponde ao esperado.");
    }

    @Entao("o número de atendimentos deve ser {int}")
    public void o_numero_de_atendimentos_deve_ser(Integer esperado) {
        assertEquals(esperado.intValue(), relatorio.getNumeroClientesAtendidos());
    }

    @Entao("a avaliação média do funcionário deve ser {string}")
    public void a_avaliacao_media_do_funcionario_deve_ser(String esperadoStr) {
        Double esperado = parseDoublePt(esperadoStr);
        assertEquals(esperado, relatorio.getAvaliacaoFuncionario(), 0.01);
    }
}