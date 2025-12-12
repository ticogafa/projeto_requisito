package com.cesarschool.barbearia.apresentacao.dev;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoServicoAplicacao;
import com.cesarschool.barbearia.aplicacao.agendamento.ProfissionalDisponivelResumo;
import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

@RestController
@RequestMapping("/api/dev")
public class DevController {

    @Autowired
    private AgendamentoRepositorio agendamentoRepositorio;
    
    @Autowired
    private AgendamentoServicoAplicacao agendamentoServicoAplicacao;

    @PostMapping("/seed-agendamentos")
    public ResponseEntity<String> seedAgendamentos() {
        // 1. Limpar agendamentos existentes para evitar poluição
        List<Agendamento> todos = agendamentoRepositorio.listarTodos();
        // Nota: Em um cenário real, deletaríamos um por um ou teríamos um método deleteAll no repositório.
        // Como o repositório é uma interface do domínio, vamos supor que não queremos expor deleteAll lá.
        // Mas para este dev controller, vamos usar o fato de que a implementação é JPA por baixo dos panos 
        // ou apenas não deletar e adicionar novos. Para simplificar e evitar erros de cast, vamos apenas adicionar novos.
        
        LocalDateTime agora = LocalDateTime.now();

        // 2. Criar Cenário 1: Agendamento Cancelável (Daqui a 5 horas)
        // Profissional 1 (Carlos), Cliente 1 (João), Serviço 1
        criarAgendamento(
            agora.plusHours(5), 
            1, 1, 1, 
            "CENÁRIO: Pode Cancelar (Tempo > 2h)"
        );

        // 3. Criar Cenário 2: Agendamento Bloqueado (Daqui a 1 hora)
        // Profissional 1 (Carlos), Cliente 2, Serviço 2
        criarAgendamento(
            agora.plusHours(1), 
            2, 1, 2, 
            "CENÁRIO: Bloqueado (Tempo < 2h)"
        );

        // 4. Criar Cenário 3: Agendamento Futuro (Amanhã)
        // Profissional 1 (Carlos), Cliente 3, Serviço 3
        criarAgendamento(
            agora.plusDays(1).withHour(10).withMinute(0), 
            3, 1, 3, 
            "CENÁRIO: Futuro Seguro"
        );

        return ResponseEntity.ok("Dados de teste gerados com sucesso! Agendamentos criados para: " + agora);
    }
    
    @GetMapping("/test-profissionais-disponiveis")
    public ResponseEntity<?> testProfissionaisDisponiveis() {
        LocalDateTime dataHora = LocalDateTime.of(2025, 12, 16, 10, 0);
        List<ProfissionalDisponivelResumo> disponiveis = 
            agendamentoServicoAplicacao.buscarProfissionaisDisponiveis(1, dataHora);
        
        return ResponseEntity.ok(disponiveis);
    }

    private void criarAgendamento(LocalDateTime dataHora, Integer clienteId, Integer profissionalId, Integer servicoId, String obs) {
        Agendamento agendamento = new Agendamento(
            dataHora,
            new ClienteId(clienteId),
            new ProfissionalId(profissionalId),
            new ServicoOferecidoId(servicoId),
            obs
        );
        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        agendamentoRepositorio.salvar(agendamento);
    }
}
