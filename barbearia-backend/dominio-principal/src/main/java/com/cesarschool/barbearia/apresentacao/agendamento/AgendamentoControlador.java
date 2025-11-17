package com.cesarschool.barbearia.apresentacao.agendamento;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoResumo;
import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoServicoAplicacao;
import com.cesarschool.barbearia.aplicacao.agendamento.CriarAgendamentoRequest;
import com.cesarschool.barbearia.aplicacao.agendamento.EditarAgendamentoRequest;
import com.cesarschool.barbearia.aplicacao.agendamento.ProfissionalDisponivelResumo;
import com.cesarschool.barbearia.dominio.compartilhado.exceptions.ExceptionHandler;
import com.cesarschool.barbearia.dominio.compartilhado.logger.LoggerSingleton;

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

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();

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
            logger.info("Criando agendamento - clienteId: " + request.getClienteId() + 
                       ", profissionalId: " + request.getProfissionalId() + 
                       ", servicoId: " + request.getServicoId());
            
            AgendamentoResumo agendamento = servicoAplicacao.criar(request);
            
            logger.success("Agendamento criado com sucesso - ID: " + agendamento.getId());
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
            logger.info("Listando agendamentos do cliente: " + clienteId);
            
            List<AgendamentoResumo> agendamentos = servicoAplicacao.listarPorCliente(clienteId);
            
            logger.info("Encontrados " + agendamentos.size() + " agendamentos para o cliente " + clienteId);
            return ResponseEntity.ok(agendamentos);
        });
    }

    /**
     * Edita um agendamento existente.
     * @param id ID do agendamento
     * @param request Novos dados do agendamento
     * @return Agendamento atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResumo> editar(
            @PathVariable Integer id,
            @RequestBody EditarAgendamentoRequest request) {
        
        return exceptionHandler.withHandler(() -> {
            logger.info("Editando agendamento - ID: " + id + 
                       ", nova dataHora: " + request.getDataHora());
            
            AgendamentoResumo agendamento = servicoAplicacao.editar(id, request);
            
            logger.success("Agendamento editado com sucesso - ID: " + id);
            return ResponseEntity.ok(agendamento);
        });
    }

    /**
     * Cancela um agendamento.
     * @param id ID do agendamento
     * @param clienteId ID do cliente solicitante
     * @return Agendamento cancelado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<AgendamentoResumo> cancelar(
            @PathVariable Integer id,
            @RequestParam Integer clienteId) {
        
        return exceptionHandler.withHandler(() -> {
            logger.info("Cancelando agendamento - ID: " + id + ", clienteId: " + clienteId);
            
            AgendamentoResumo agendamento = servicoAplicacao.cancelar(id, clienteId);
            
            logger.success("Agendamento cancelado com sucesso - ID: " + id);
            return ResponseEntity.ok(agendamento);
        });
    }
}
