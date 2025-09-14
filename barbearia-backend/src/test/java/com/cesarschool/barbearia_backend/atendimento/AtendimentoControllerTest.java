package com.cesarschool.barbearia_backend.atendimento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.cesarschool.barbearia_backend.BaseControllerTest;
import com.cesarschool.barbearia_backend.atendimento.controller.AtendimentoController;
import com.cesarschool.barbearia_backend.atendimento.controller.AtendimentoController.IniciarAtendimentoRequest;
import com.cesarschool.barbearia_backend.atendimento.model.Atendimento;
import com.cesarschool.barbearia_backend.atendimento.model.enums.StatusAtendimento;
import com.cesarschool.barbearia_backend.atendimento.service.AtendimentoService;

@WebMvcTest(AtendimentoController.class)
@ActiveProfiles("test")
public class AtendimentoControllerTest extends BaseControllerTest {

    @Autowired
    private AtendimentoController controller;

    @MockBean
    private AtendimentoService atendimentoService;

    private Atendimento atendimentoEmAndamento;
    private Atendimento atendimentoFinalizado;

    public void cleanup() {
        // Nada para limpar neste caso
    }

    @BeforeEach
    public void setUp() {
        // Mock de um atendimento que está em andamento
        atendimentoEmAndamento = new Atendimento();
        atendimentoEmAndamento.setId(1L);
        atendimentoEmAndamento.setAgendamentoId(100L);
        atendimentoEmAndamento.setStatus(StatusAtendimento.EM_ANDAMENTO);
        atendimentoEmAndamento.setInicioAtendimento(LocalDateTime.now());

        // Mock de um atendimento já finalizado
        atendimentoFinalizado = new Atendimento();
        atendimentoFinalizado.setId(2L);
        atendimentoFinalizado.setAgendamentoId(101L);
        atendimentoFinalizado.setStatus(StatusAtendimento.FINALIZADO);
        atendimentoFinalizado.setInicioAtendimento(LocalDateTime.now().minusMinutes(30));
        atendimentoFinalizado.setFimAtendimento(LocalDateTime.now());
        atendimentoFinalizado.setDuracao(Duration.ofMinutes(30));
    }

    @Test
    void testIniciarAtendimento_Positive() {
        // Given: Um ID de agendamento válido para iniciar
        IniciarAtendimentoRequest request = new IniciarAtendimentoRequest(100L);
        when(atendimentoService.iniciarAtendimento(anyLong())).thenReturn(atendimentoEmAndamento);

        // When: A requisição para iniciar o atendimento é feita
        ResponseEntity<Atendimento> response = controller.iniciarAtendimento(request);

        // Then: O atendimento deve ser iniciado com sucesso e retornado
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(StatusAtendimento.EM_ANDAMENTO, response.getBody().getStatus());
        assertEquals(100L, response.getBody().getAgendamentoId());
    }
    
    @Test
    void testIniciarAtendimento_Negative_AgendamentoNaoEncontrado() {
        // Given: Um ID de agendamento que não existe
        IniciarAtendimentoRequest request = new IniciarAtendimentoRequest(999L);
        when(atendimentoService.iniciarAtendimento(999L)).thenThrow(new RuntimeException("Agendamento não encontrado!"));

        // When: A requisição para iniciar o atendimento é feita
        // Then: O sistema deve lançar uma exceção
        assertThrows(RuntimeException.class, () -> {
            controller.iniciarAtendimento(request);
        });
    }

    @Test
    void testFinalizarAtendimento_Positive() {
        // Given: Um ID de um atendimento que está em andamento
        Long atendimentoId = 1L;
        when(atendimentoService.finalizarAtendimento(atendimentoId)).thenReturn(atendimentoFinalizado);

        // When: A requisição para finalizar o atendimento é feita
        ResponseEntity<Atendimento> response = controller.finalizarAtendimento(atendimentoId);

        // Then: O atendimento deve ser finalizado com sucesso
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(StatusAtendimento.FINALIZADO, response.getBody().getStatus());
        assertNotNull(response.getBody().getDuracao());
    }

    @Test
    void testFinalizarAtendimento_Negative_AtendimentoJaFinalizado() {
        // Given: Um ID de um atendimento que já foi finalizado
        Long atendimentoId = 2L;
        when(atendimentoService.finalizarAtendimento(atendimentoId)).thenThrow(new IllegalStateException("Só é possível finalizar um atendimento que está em andamento."));

        // When: A requisição para finalizar o atendimento é feita
        // Then: O sistema deve impedir a ação com uma exceção
        assertThrows(IllegalStateException.class, () -> {
            controller.finalizarAtendimento(atendimentoId);
        });
    }
}