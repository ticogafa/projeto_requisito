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

/**
 * Controlador REST para operações de agendamento.
 * Seguindo o padrão SGB-2025-01:
 * - Controlador delega tudo para camada de aplicação (AgendamentoServicoAplicacao)
 * - Não contém lógica de negócio ou conversões manuais
 * - Retorna apenas DTOs/projeções, nunca entidades de domínio
 * 
 * CORS configurado globalmente em CorsConfig.
 */
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoControlador {

    @Autowired
    private AgendamentoServicoAplicacao servicoAplicacao;

    /**
     * Busca profissionais disponíveis para um serviço em uma data/hora específica.
     * 
     * @param servicoId ID do serviço
     * @param dataHora Data e hora desejada (formato ISO: 2025-11-15T14:30:00)
     * @return Lista de profissionais disponíveis
     */
    @GetMapping("/profissionais-disponiveis")
    public ResponseEntity<List<ProfissionalDisponivelResumo>> buscarProfissionaisDisponiveis(
            @RequestParam Integer servicoId,
            @RequestParam String dataHora) {
        
        LocalDateTime data = LocalDateTime.parse(dataHora);
        List<ProfissionalDisponivelResumo> disponiveis = 
            servicoAplicacao.buscarProfissionaisDisponiveis(servicoId, data);
        
        return ResponseEntity.ok(disponiveis);
    }

    /**
     * Cria um novo agendamento.
     * 
     * @param request Dados do agendamento
     * @return Agendamento criado
     */
    @PostMapping("/criar")
    public ResponseEntity<AgendamentoResumo> criar(@RequestBody CriarAgendamentoRequest request) {
        AgendamentoResumo agendamento = servicoAplicacao.criar(request);
        return ResponseEntity.ok(agendamento);
    }

    /**
     * Lista todos os agendamentos de um cliente.
     * 
     * @param clienteId ID do cliente
     * @return Lista de agendamentos
     */
    @GetMapping("/por-cliente")
    public ResponseEntity<List<AgendamentoResumo>> listarPorCliente(@RequestParam Integer clienteId) {
        List<AgendamentoResumo> agendamentos = servicoAplicacao.listarPorCliente(clienteId);
        return ResponseEntity.ok(agendamentos);
    }
}


// import java.time.LocalDateTime;
// import java.time.LocalTime;
// import java.util.List;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
// import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
// import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoId;
// import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
// import com.cesarschool.barbearia.dominio.principal.agendamento.UsuarioSolicitante;
// import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
// import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
// import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

// @RestController
// @RequestMapping("/agendamentos")
// public class AgendamentoControlador {

    
//     public ResponseEntity<Agendamento> criar(Agendamento agendamento, int duracaoServicoMinutos) {
//         if (agendamento.getClienteId() == null) {
//             throw new IllegalArgumentException("Cliente deve ser informado para criar agendamento");
//         }
        
//         // Validar horário de funcionamento (8h às 18h)
//         var data = agendamento.getDataHora();
//         var hora = agendamento.getDataHora().toLocalTime();
//         if(hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(18, 0))) {
//             throw new IllegalStateException(
//                 "Agendamentos só podem ser feitos entre 08:00 e 18:00"
//             );
//         }
        
//         // Se profissional não informado, buscar automaticamente
//         if(agendamento.getProfissionalId() == null){
//             Profissional profissional = profissionalServico.buscarPrimeiroProfissionalDisponivel(data, duracaoServicoMinutos);
//             agendamento.setProfissional(profissional.getId());
//         }

//         // Verificar se existe conflito de horário
//         if (repositorio.existeAgendamentoNoPeriodo(
//                 agendamento.getProfissionalId(), 
//                 agendamento.getDataHora(), 
//                 duracaoServicoMinutos)) {
//             throw new IllegalStateException(
//                 "Já existe um agendamento neste horário para o profissional"
//             );
//         }
        
//         return repositorio.salvar(agendamento);
//     }

//     public Agendamento buscarPorId(AgendamentoId id) {
//         Validacoes.validarObjetoObrigatorio(id, "ID do agendamento");
//         return repositorio.buscarPorId(id.getValor());
//     }

//     /**
//      * Confirma um agendamento.
//      */
//     public Agendamento confirmar(AgendamentoId id) {
//         Validacoes.validarObjetoObrigatorio(id, "ID do agendamento");
//         Agendamento agendamento = buscarPorId(id);
//         agendamento.confirmar();
//         return repositorio.salvar(agendamento);
//     }

//     /**
//      * Cancela um agendamento.
//      */
//     public Agendamento cancelar(AgendamentoId id, UsuarioSolicitante usuario) {
//         Validacoes.validarObjetoObrigatorio(id, "ID do agendamento");
//         Agendamento agendamento = buscarPorId(id);
//         agendamento.cancelar(usuario);
//         return repositorio.salvar(agendamento);
//     }

//     public List<Agendamento> listarPorCliente(ClienteId clienteId) {
//         Validacoes.validarObjetoObrigatorio(clienteId, "ID do cliente");
//         return repositorio.buscarPorCliente(clienteId);
//     }

//     public List<Agendamento> listarPorProfissional(ProfissionalId profissionalId) {
//         Validacoes.validarObjetoObrigatorio(profissionalId, "ID do profissional");
//         return repositorio.buscarPorProfissional(profissionalId);
//     }

//     public List<Agendamento> listarPorStatus(StatusAgendamento status) {
//         Validacoes.validarObjetoObrigatorio(status, "Status do agendamento");
//         return repositorio.buscarPorStatus(status);
//     }

//     public List<Agendamento> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
//         Validacoes.validarObjetoObrigatorio(inicio, "A data de início");
//         Validacoes.validarObjetoObrigatorio(fim, "A data de fim");
//         Validacoes.validarInicioAntesFim(inicio, fim);

//         return repositorio.buscarPorPeriodo(inicio, fim);
//     }

//     public List<Agendamento> listarTodos() {
//         return repositorio.listarTodos();
//     }
// }
