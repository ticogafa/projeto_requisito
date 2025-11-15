package com.cesarschool.barbearia.apresentacao.agendamento;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoResumo;
import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoServicoAplicacao;
import com.cesarschool.barbearia.aplicacao.agendamento.CriarAgendamentoRequest;
import com.cesarschool.barbearia.aplicacao.agendamento.ProfissionalDisponivelResumo;
import com.cesarschool.barbearia.dominio.compartilhado.exceptions.ExceptionHandler;

/**
 * Controlador REST para operações de agendamento.
 * Seguindo o padrão SGB-2025-01:
 * - Controlador delega tudo para camada de aplicação (AgendamentoServicoAplicacao)
 * - Não contém lógica de negócio ou conversões manuais
 * - Retorna apenas DTOs/projeções, nunca entidades de domínio
 * - Usa ExceptionHandler customizado para tratamento de erros
 */
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoControlador {

    @Autowired
    private AgendamentoServicoAplicacao servicoAplicacao;
    
    @Autowired
    private ExceptionHandler exceptionHandler;

    /**
     * Busca profissionais disponíveis para um serviço em uma data/hora específica.
     * @param servicoId ID do serviço
     * @param dataHora Data e hora desejada (formato ISO: 2025-11-15T14:30:00)
     * @return Lista de profissionais disponíveis
     */
    @GetMapping("/profissionais-disponiveis")
    public ResponseEntity<List<ProfissionalDisponivelResumo>> buscarProfissionaisDisponiveis(
            @RequestParam Integer servicoId,
            @RequestParam String dataHora) {
        
        return exceptionHandler.withHandler(() -> {
            LocalDateTime data = LocalDateTime.parse(dataHora);
            List<ProfissionalDisponivelResumo> disponiveis = 
                servicoAplicacao.buscarProfissionaisDisponiveis(servicoId, data);
            
            return ResponseEntity.ok(disponiveis);
        });
    }

    /**
     * Cria um novo agendamento.
     * @param request Dados do agendamento
     * @return Agendamento criado
     */
    @PostMapping("/criar")
    public ResponseEntity<AgendamentoResumo> criar(@RequestBody CriarAgendamentoRequest request) {
        return exceptionHandler.withHandler(() -> {
            AgendamentoResumo agendamento = servicoAplicacao.criar(request);
            return ResponseEntity.ok(agendamento);
        });
    }

    /**
     * Lista todos os agendamentos de um cliente.
     * @param clienteId ID do cliente
     * @return Lista de agendamentos
     */
    @GetMapping("/por-cliente")
    public ResponseEntity<List<AgendamentoResumo>> listarPorCliente(@RequestParam Integer clienteId) {
        return exceptionHandler.withHandler(() -> {
            List<AgendamentoResumo> agendamentos = servicoAplicacao.listarPorCliente(clienteId);
            return ResponseEntity.ok(agendamentos);
        });
    }
}
