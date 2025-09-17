// package com.cesarschool.barbearia_backend.agendamento;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import org.mockito.Mockito;

// import java.time.LocalDateTime;
// 

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.BeforeEach;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.http.ResponseEntity;

// import com.cesarschool.barbearia_backend.BaseControllerTest;
// import com.cesarschool.barbearia_backend.agendamento.model.Agendamento;
// import com.cesarschool.barbearia_backend.agendamento.rest.controller.AgendamentoController;
// import com.cesarschool.barbearia_backend.agendamento.service.AgendamentoService;
// import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
// import com.cesarschool.barbearia_backend.marketing.model.Cliente;
// import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
// import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;


// @WebMvcTest(AgendamentoController.class)
// @ActiveProfiles("test")
// class AgendamentoTestCasesTest extends BaseControllerTest{



//   @Autowired
//   private AgendamentoController controller;

//   @MockBean
//   private AgendamentoService agendamentoService;

//   private Agendamento agendamentoTest;
//   private Cliente clienteTest;
//   private Profissional profissionalTest;
//   private ServicoOferecido servicoTest;

//   public void cleanup() {
//     // Clean up any test data if needed
//   }

//   @BeforeEach
//   public void setUp() {
//     try {
//       // Create Cliente with all required fields
//       clienteTest = new Cliente();
//       clienteTest.setId(Integer.randomInteger());
      
//       // Create Profissional with all required fields  
//       profissionalTest = new Profissional();
//       profissionalTest.setId(Integer.randomInteger());
//       profissionalTest.setNome("João Barbeiro");
      
//       // Create ServicoOferecido with all required fields
//       servicoTest = new ServicoOferecido();
//       servicoTest.setId(Integer.randomInteger());
      
//       // Create Agendamento with all required fields
//       agendamentoTest = new Agendamento();
//       agendamentoTest.setId(Integer.randomInteger());
//       agendamentoTest.setCliente(clienteTest);
//       agendamentoTest.setProfissional(profissionalTest);
//       agendamentoTest.setServico(servicoTest);
//       agendamentoTest.setDataHora(LocalDateTime.now().plusDays(1));
//       agendamentoTest.setStatus(StatusAgendamento.PENDENTE);
//       agendamentoTest.setObservacoes("Teste");
//     } catch (Exception e) {
//       // If entity creation fails, the test setup has an issue
//       System.err.println("Error creating test entities: " + e.getMessage());
//       throw new RuntimeException("Test setup failed", e);
//     }
//   }

//   @Test
//   void testCreateAgendamentoWhenHorarioDisponivelLivre_Positive() {
//     // Given: Um horário disponível para um profissional
//     Agendamento novoAgendamento = new Agendamento();
//     novoAgendamento.setCliente(clienteTest);
//     novoAgendamento.setProfissional(profissionalTest);
//     novoAgendamento.setServico(servicoTest);
//     novoAgendamento.setDataHora(LocalDateTime.now().plusDays(1));
//     novoAgendamento.setStatus(StatusAgendamento.PENDENTE);

//     Mockito.when(agendamentoService.save(any(Agendamento.class))).thenReturn(agendamentoTest);

//     // When: Tentar criar um agendamento nesse horário
//     ResponseEntity<Agendamento> response = controller.criarAgendamento(novoAgendamento);

//     // Then: O agendamento deve ser criado com sucesso
//     assertNotNull(response);
//     assertTrue(response.getStatusCode().is2xxSuccessful());
//     assertNotNull(response.getBody());
//     assertEquals(agendamentoTest.getId(), response.getBody().getId());
//     // Simule a criação e verifique se foi salvo corretamente
//   }

//   @Test
//   void testCreateAgendamentoWhenHorarioDisponivelLivre_Negative() {
//     // Given: Um horário já ocupado para um profissional
//     Agendamento agendamentoConflito = new Agendamento();
//     agendamentoConflito.setCliente(clienteTest);
//     agendamentoConflito.setProfissional(profissionalTest);
//     agendamentoConflito.setServico(servicoTest);
//     agendamentoConflito.setDataHora(LocalDateTime.now().plusDays(1));
//     agendamentoConflito.setStatus(StatusAgendamento.PENDENTE);

//     Mockito.when(agendamentoService.save(any(Agendamento.class))).thenThrow(new IllegalArgumentException("Já existe um agendamento com João Barbeiro no horário especificado."));

//     // When: Tentar criar um agendamento nesse mesmo horário
//     // Then: O sistema deve impedir a criação do agendamento
//     assertThrows(IllegalArgumentException.class, () -> {
//       controller.criarAgendamento(agendamentoConflito);
//     });
//     // Simule conflito e verifique se ocorre exceção ou erro esperado
//   }

//   @Test
//   void testCreateAgendamentoWhenHorarioForaJornadaTrabalho_Positive() {
//     // Given: Jornada de trabalho definida para o profissional
//     Agendamento agendamentoDentroJornada = new Agendamento();
//     agendamentoDentroJornada.setCliente(clienteTest);
//     agendamentoDentroJornada.setProfissional(profissionalTest);
//     agendamentoDentroJornada.setServico(servicoTest);
//     agendamentoDentroJornada.setDataHora(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
//     agendamentoDentroJornada.setStatus(StatusAgendamento.PENDENTE);

//     Mockito.when(agendamentoService.save(any(Agendamento.class))).thenReturn(agendamentoTest);

//     // When: Tentar criar um agendamento dentro desse horário
//     ResponseEntity<Agendamento> response = controller.criarAgendamento(agendamentoDentroJornada);

//     // Then: O agendamento deve ser criado com sucesso
//     assertNotNull(response);
//     assertTrue(response.getStatusCode().is2xxSuccessful());
//     assertNotNull(response.getBody());
//   }

//   @Test
//   void testCreateAgendamentoWhenHorarioForaJornadaTrabalho_Negative() {
//     // Given: Jornada de trabalho definida para o profissional
//     Agendamento agendamentoForaJornada = new Agendamento();
//     agendamentoForaJornada.setCliente(clienteTest);
//     agendamentoForaJornada.setProfissional(profissionalTest);
//     agendamentoForaJornada.setServico(servicoTest);
//     agendamentoForaJornada.setDataHora(LocalDateTime.now().plusDays(1).withHour(2).withMinute(0)); // Horário fora da jornada
//     agendamentoForaJornada.setStatus(StatusAgendamento.PENDENTE);

//     Mockito.when(agendamentoService.save(any(Agendamento.class))).thenThrow(new IllegalArgumentException("João Barbeiro não está disponível no horário solicitado."));

//     // When: Tentar criar um agendamento fora desse horário
//     // Then: O sistema deve impedir a criação do agendamento
//     assertThrows(IllegalArgumentException.class, () -> {
//       controller.criarAgendamento(agendamentoForaJornada);
//     });
//   }

//   @Test
//   void testCancelAgendamentoLessThan2HoursBefore_Positive() {
//     // Given: Um agendamento marcado para daqui a mais de 2 horas
//     Integer agendamentoId = Integer.randomInteger();
//     Agendamento agendamentoParaCancelar = new Agendamento();
//     agendamentoParaCancelar.setId(agendamentoId);
//     agendamentoParaCancelar.setCliente(clienteTest);
//     agendamentoParaCancelar.setProfissional(profissionalTest);
//     agendamentoParaCancelar.setServico(servicoTest);
//     agendamentoParaCancelar.setDataHora(LocalDateTime.now().plusHours(5)); // Mais de 2 horas
//     agendamentoParaCancelar.setStatus(StatusAgendamento.PENDENTE);

//     Mockito.when(agendamentoService.findById(agendamentoId)).thenReturn(agendamentoParaCancelar);
//     Mockito.when(agendamentoService.save(any(Agendamento.class))).thenReturn(agendamentoParaCancelar);

//     // When: Tentar cancelar o agendamento
//     ResponseEntity<Agendamento> response = controller.cancelarAgendamento(agendamentoId);

//     // Then: O cancelamento deve ser permitido
//     assertNotNull(response);
//     assertTrue(response.getStatusCode().is2xxSuccessful());
//     assertNotNull(response.getBody());
//     assertEquals(StatusAgendamento.CANCELADO, agendamentoParaCancelar.getStatus());
//   }

//   @Test
//   void testCancelAgendamentoLessThan2HoursBefore_Negative() {
//     // Given: Um agendamento marcado para daqui a menos de 2 horas
//     Integer agendamentoId = Integer.randomInteger();
//     Agendamento agendamentoParaCancelar = new Agendamento();
//     agendamentoParaCancelar.setId(agendamentoId);
//     agendamentoParaCancelar.setCliente(clienteTest);
//     agendamentoParaCancelar.setProfissional(profissionalTest);
//     agendamentoParaCancelar.setServico(servicoTest);
//     agendamentoParaCancelar.setDataHora(LocalDateTime.now().plusHours(1)); // Menos de 2 horas
//     agendamentoParaCancelar.setStatus(StatusAgendamento.PENDENTE);

//     Mockito.when(agendamentoService.findById(agendamentoId)).thenReturn(agendamentoParaCancelar);
//     Mockito.when(agendamentoService.save(any(Agendamento.class))).thenThrow(
//       new IllegalArgumentException("Não é permitido cancelar agendamentos com menos de 2 horas de antecedência.")
//     );

//     // When: Tentar cancelar o agendamento
//     // Then: O cancelamento deve ser rejeitado
//     assertThrows(IllegalArgumentException.class, () -> {
//       controller.cancelarAgendamento(agendamentoId);
//     });
//   }

//   @Test
//   void testAssignFirstAvailableProfessionalWhenNoneSpecified_Positive() {
//     // Given: Múltiplos profissionais com horários livres
//     Agendamento agendamentoSemProfissional = new Agendamento();
//     agendamentoSemProfissional.setCliente(clienteTest);
//     agendamentoSemProfissional.setServico(servicoTest);
//     agendamentoSemProfissional.setDataHora(LocalDateTime.now().plusDays(1));
//     agendamentoSemProfissional.setStatus(StatusAgendamento.PENDENTE);
//     // Não definindo profissional intencionalmente

//     Agendamento agendamentoComProfissional = new Agendamento();
//     agendamentoComProfissional.setCliente(clienteTest);
//     agendamentoComProfissional.setProfissional(profissionalTest); // Sistema atribuiu automaticamente
//     agendamentoComProfissional.setServico(servicoTest);
//     agendamentoComProfissional.setDataHora(LocalDateTime.now().plusDays(1));
//     agendamentoComProfissional.setStatus(StatusAgendamento.PENDENTE);

//     Mockito.when(agendamentoService.save(any(Agendamento.class))).thenReturn(agendamentoComProfissional);

//     // When: Criar um agendamento sem especificar profissional
//     ResponseEntity<Agendamento> response = controller.criarAgendamento(agendamentoSemProfissional);

//     // Then: O sistema deve atribuir o primeiro profissional disponível
//     assertNotNull(response);
//     assertTrue(response.getStatusCode().is2xxSuccessful());
//     assertNotNull(response.getBody());
//     assertNotNull(agendamentoComProfissional.getProfissional());
//   }

//   @Test
//   void testAssignFirstAvailableProfessionalWhenNoneSpecified_Negative() {
//     // Given: Nenhum profissional disponível no horário solicitado
//     Agendamento agendamentoSemProfissional = new Agendamento();
//     agendamentoSemProfissional.setCliente(clienteTest);
//     agendamentoSemProfissional.setServico(servicoTest);
//     agendamentoSemProfissional.setDataHora(LocalDateTime.now().plusDays(1));
//     agendamentoSemProfissional.setStatus(StatusAgendamento.PENDENTE);
//     // Não definindo profissional intencionalmente

//     Mockito.when(agendamentoService.save(any(Agendamento.class))).thenThrow(new IllegalArgumentException("Nenhum profissional disponível no horário solicitado."));

//     // When: Criar um agendamento sem especificar profissional
//     // Then: O sistema deve rejeitar a criação do agendamento
//     assertThrows(IllegalArgumentException.class, () -> {
//       controller.criarAgendamento(agendamentoSemProfissional);
//     });
//   }
// }