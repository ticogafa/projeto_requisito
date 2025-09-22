package com.cesarschool.barbearia_backend.agendamento.rest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.AgendamentoResponse;
import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
import com.cesarschool.barbearia_backend.agendamento.service.AgendamentoService;
import com.cesarschool.barbearia_backend.profissionais.dto.ProfissionalDTOs.ProfissionalResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    /**
     * Cria um novo agendamento.
     *
     * @param request Dados para criação do agendamento
     * @return ResponseEntity com o agendamento criado
     */
    @PostMapping("/criar-agendamento")
    public ResponseEntity<AgendamentoResponse> criarAgendamento(@RequestBody CriarAgendamentoRequest request) {
        var agendamento = agendamentoService.criarAgendamento(request);
        return ResponseEntity.ok(agendamento);
    }

    /**
     * Busca um agendamento pelo ID.
     *
     * @param id ID do agendamento
     * @return ResponseEntity com o agendamento encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarAgendamento(@PathVariable Integer id) {
        var agendamento = agendamentoService.buscarPorId(id);
        return ResponseEntity.ok(agendamento);
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listarTodos() {
        var agendamentos = agendamentoService.listarTodos();
        return ResponseEntity.ok(agendamentos);
    }

    /**
     * Confirma um agendamento específico.
     *
     * @param id ID do agendamento a ser confirmado
     * @return ResponseEntity com o agendamento confirmado
     */
    @PatchMapping("/confirmar-agendamento/{id}")
    public ResponseEntity<AgendamentoResponse> confirmarAgendamento(@PathVariable Integer id) {
        var agendamento = agendamentoService.confirmarAgendamento(id);
        return ResponseEntity.ok(agendamento);
    }

    /**
     * Cancela um agendamento específico.
     *
     * @param id ID do agendamento a ser cancelado
     * @return ResponseEntity com o agendamento cancelado
     */
    @PatchMapping("/cancelar-agendamento/{id}")
    public ResponseEntity<AgendamentoResponse> cancelarAgendamento(@PathVariable Integer id) {
        var agendamento = agendamentoService.cancelarAgendamento(id);
        return ResponseEntity.ok(agendamento);
    }

    /**
     * Lista os horários disponíveis para agendamento.
     * Endpoint para o primeiro passo do fluxo de agendamento em duas etapas.
     *
     * @param data Data desejada para o agendamento (formato: yyyy-MM-dd)
     * @param servicoId ID do serviço oferecido
     * @return ResponseEntity com lista de horários disponíveis em formato String (HH:mm)
     */
    @GetMapping("/horarios-disponiveis")
    public ResponseEntity<List<String>> listarHorariosDisponiveis(
            @RequestParam String data,
            @RequestParam Integer servicoId) {
        var horarios = agendamentoService.listarHorariosDisponiveis(data, servicoId);
        return ResponseEntity.ok(horarios);
    }

    /**
     * Lista os profissionais disponíveis para um horário específico.
     * Endpoint para o segundo passo do fluxo de agendamento em duas etapas.
     *
     * @param data Data do agendamento (formato: yyyy-MM-dd)
     * @param horario Horário desejado (formato: HH:mm)
     * @param servicoId ID do serviço oferecido
     * @return ResponseEntity com lista de profissionais disponíveis e seus dados
     */
    @GetMapping("/profissionais-disponiveis")
    public ResponseEntity<List<ProfissionalResponse>> listarProfissionaisDisponiveis(
            @RequestParam String data,
            @RequestParam String horario,
            @RequestParam Integer servicoId) {
        var profissionais = agendamentoService.listarProfissionaisDisponiveis(data, horario, servicoId);
        return ResponseEntity.ok(profissionais);
    }
}
