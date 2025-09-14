package com.cesarschool.barbearia_backend.agendamento;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.cesarschool.barbearia_backend.agendamento.service.AgendamentoService;


@SpringBootTest
class AgendamentoTestCasesTest {

  @Autowired
  private AgendamentoService repository;

  @Test
  void testCreateAgendamentoWhenHorarioDisponivelLivre_Positive() {
    // Given: Um horário disponível para um profissional
    // When: Tentar criar um agendamento nesse horário
    // Then: O agendamento deve ser criado com sucesso
    // Simule a criação e verifique se foi salvo corretamente
  }

  @Test
  void testCreateAgendamentoWhenHorarioDisponivelLivre_Negative() {
    // Given: Um horário já ocupado para um profissional
    // When: Tentar criar um agendamento nesse mesmo horário
    // Then: O sistema deve impedir a criação do agendamento
    // Simule conflito e verifique se ocorre exceção ou erro esperado
  }

  @Test
  void testCreateAgendamentoWhenHorarioForaJornadaTrabalho_Positive() {
    // Given: Jornada de trabalho definida para o profissional
    // When: Tentar criar um agendamento dentro desse horário
    // Then: O agendamento deve ser criado com sucesso
  }

  @Test
  void testCreateAgendamentoWhenHorarioForaJornadaTrabalho_Negative() {
    // Given: Jornada de trabalho definida para o profissional
    // When: Tentar criar um agendamento fora desse horário
    // Then: O sistema deve impedir a criação do agendamento
  }

  @Test
  void testCancelAgendamentoLessThan2HoursBefore_Positive() {
    // Given: Um agendamento marcado para daqui a mais de 2 horas
    // When: Tentar cancelar o agendamento
    // Then: O cancelamento deve ser permitido
  }

  @Test
  void testCancelAgendamentoLessThan2HoursBefore_Negative() {
    // Given: Um agendamento marcado para daqui a menos de 2 horas
    // When: Tentar cancelar o agendamento
    // Then: O cancelamento deve ser rejeitado
  }

  @Test
  void testAssignFirstAvailableProfessionalWhenNoneSpecified_Positive() {
    // Given: Múltiplos profissionais com horários livres
    // When: Criar um agendamento sem especificar profissional
    // Then: O sistema deve atribuir o primeiro profissional disponível
  }

  @Test
  void testAssignFirstAvailableProfessionalWhenNoneSpecified_Negative() {
    // Given: Nenhum profissional disponível no horário solicitado
    // When: Criar um agendamento sem especificar profissional
    // Then: O sistema deve rejeitar a criação do agendamento
  }
}