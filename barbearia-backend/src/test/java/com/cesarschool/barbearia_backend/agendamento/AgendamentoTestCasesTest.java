package com.cesarschool.barbearia_backend.agendamento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.AgendamentoResponse;
import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
import com.cesarschool.barbearia_backend.agendamento.rest.controller.AgendamentoController;
import com.cesarschool.barbearia_backend.agendamento.service.AgendamentoService;
import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.profissionais.dto.ProfissionalDTOs.ProfissionalResponse;

@WebMvcTest(AgendamentoController.class)
@ActiveProfiles("test")
class AgendamentoTestCasesTest {

    @Autowired
    private AgendamentoController controller;

    @MockBean
    private AgendamentoService agendamentoService;

    private CriarAgendamentoRequest agendamentoRequest;
    private AgendamentoResponse agendamentoResponse;

    @BeforeEach
    void setUp() {
        // Create request DTO
        agendamentoRequest = new CriarAgendamentoRequest();
        agendamentoRequest.setClienteId(1);
        agendamentoRequest.setProfissionalId(1);
        agendamentoRequest.setServicoId(1);
        agendamentoRequest.setDataHora(LocalDateTime.now().plusDays(1));
        agendamentoRequest.setObservacoes("Teste de agendamento");

        // Create response DTO
        agendamentoResponse = new AgendamentoResponse();
        agendamentoResponse.setId(1);
        agendamentoResponse.setDataHora(agendamentoRequest.getDataHora());
        agendamentoResponse.setStatus(StatusAgendamento.CONFIRMADO);
        agendamentoResponse.setObservacoes(agendamentoRequest.getObservacoes());
        agendamentoResponse.setClienteId(1);
        agendamentoResponse.setClienteNome("Miguel Batista");
        agendamentoResponse.setClienteEmail("miguel@example.com");
        agendamentoResponse.setClienteTelefone("11999999999");
        agendamentoResponse.setProfissionalId(1);
        agendamentoResponse.setProfissionalNome("João Barbeiro");
        agendamentoResponse.setProfissionalEmail("joao@example.com");
        agendamentoResponse.setServicoId(1);
        agendamentoResponse.setServicoNome("Corte de Cabelo");
        agendamentoResponse.setServicoPreco(new BigDecimal("30.00"));
        agendamentoResponse.setServicoDuracaoMinutos(30);
    }

    @Test
    void testCriarAgendamento_Success() {
        // Given
        when(agendamentoService.criarAgendamento(any(CriarAgendamentoRequest.class)))
            .thenReturn(agendamentoResponse);

        // When
        ResponseEntity<AgendamentoResponse> response = controller.criarAgendamento(agendamentoRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(agendamentoResponse.getId(), response.getBody().getId());
        assertEquals(StatusAgendamento.CONFIRMADO, response.getBody().getStatus());
        
        verify(agendamentoService, times(1)).criarAgendamento(any(CriarAgendamentoRequest.class));
    }

    @Test
    void testBuscarAgendamento_Success() {
        // Given
        Integer agendamentoId = 1;
        when(agendamentoService.buscarPorId(agendamentoId))
            .thenReturn(agendamentoResponse);

        // When
        ResponseEntity<AgendamentoResponse> response = controller.buscarAgendamento(agendamentoId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(agendamentoId, response.getBody().getId());
        
        verify(agendamentoService, times(1)).buscarPorId(agendamentoId);
    }

    @Test
    void testConfirmarAgendamento_Success() {
        // Given
        Integer agendamentoId = 1;
        agendamentoResponse.setStatus(StatusAgendamento.CONFIRMADO);
        when(agendamentoService.confirmarAgendamento(agendamentoId))
            .thenReturn(agendamentoResponse);

        // When
        ResponseEntity<AgendamentoResponse> response = controller.confirmarAgendamento(agendamentoId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(StatusAgendamento.CONFIRMADO, response.getBody().getStatus());
        
        verify(agendamentoService, times(1)).confirmarAgendamento(agendamentoId);
    }

    @Test
    void testCancelarAgendamento_Success() {
        // Given
        Integer agendamentoId = 1;
        agendamentoResponse.setStatus(StatusAgendamento.CANCELADO);
        when(agendamentoService.cancelarAgendamento(agendamentoId))
            .thenReturn(agendamentoResponse);

        // When
        ResponseEntity<AgendamentoResponse> response = controller.cancelarAgendamento(agendamentoId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(StatusAgendamento.CANCELADO, response.getBody().getStatus());
        
        verify(agendamentoService, times(1)).cancelarAgendamento(agendamentoId);
    }

    @Test
    void testListarHorariosDisponiveis_Success() {
        // Given
        String data = "2025-09-22";
        Integer servicoId = 1;
        List<String> horariosDisponiveis = Arrays.asList("09:00", "10:00", "11:00", "14:00", "15:00");
        
        when(agendamentoService.listarHorariosDisponiveis(data, servicoId))
            .thenReturn(horariosDisponiveis);

        // When
        ResponseEntity<List<String>> response = controller.listarHorariosDisponiveis(data, servicoId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().size());
        assertTrue(response.getBody().contains("09:00"));
        assertTrue(response.getBody().contains("15:00"));
        
        verify(agendamentoService, times(1)).listarHorariosDisponiveis(data, servicoId);
    }

    @Test
    void testListarProfissionaisDisponiveis_Success() {
        // Given
        String data = "2025-09-22";
        String horario = "10:00";
        Integer servicoId = 1;
        
        ProfissionalResponse profissional = new ProfissionalResponse();
        profissional.setId(1);
        profissional.setNome("João Barbeiro");
        profissional.setEmail("joao@example.com");
        profissional.setCpf("12345678901");
        profissional.setTelefone("11999999999");
        
        List<ProfissionalResponse> profissionaisDisponiveis = Arrays.asList(profissional);
        
        when(agendamentoService.listarProfissionaisDisponiveis(data, horario, servicoId))
            .thenReturn(profissionaisDisponiveis);

        // When
        ResponseEntity<List<ProfissionalResponse>> response = controller.listarProfissionaisDisponiveis(data, horario, servicoId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("João Barbeiro", response.getBody().get(0).getNome());
        
        verify(agendamentoService, times(1)).listarProfissionaisDisponiveis(data, horario, servicoId);
    }
}
