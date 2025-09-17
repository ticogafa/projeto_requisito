// package com.cesarschool.barbearia_backend.financeiro;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// import java.math.BigDecimal;
// import java.time.LocalDate;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.context.ActiveProfiles;

// import com.cesarschool.barbearia_backend.BaseControllerTest;
// import com.cesarschool.barbearia_backend.financeiro.controller.CaixaController;
// import com.cesarschool.barbearia_backend.financeiro.controller.PagamentoController;
// import com.cesarschool.barbearia_backend.financeiro.controller.CaixaController.AbrirCaixaRequest;
// import com.cesarschool.barbearia_backend.financeiro.controller.PagamentoController.RegistrarPagamentoRequest;
// import com.cesarschool.barbearia_backend.financeiro.model.Caixa;
// import com.cesarschool.barbearia_backend.financeiro.model.enums.StatusCaixa;
// import com.cesarschool.barbearia_backend.financeiro.service.CaixaService;
// import com.cesarschool.barbearia_backend.financeiro.service.PagamentoService;


// @WebMvcTest({CaixaController.class, PagamentoController.class}) // Testando múltiplos controllers
// @ActiveProfiles("test")
// public class FinanceiroControllerTest extends BaseControllerTest {

//     @Autowired
//     private CaixaController caixaController;
    
//     @Autowired
//     private PagamentoController pagamentoController;

//     @MockBean
//     private CaixaService caixaService;
    
//     @MockBean
//     private PagamentoService pagamentoService;

//     private Caixa caixaAberto;

//     public void cleanup() {}

//     @BeforeEach
//     public void setUp() {
//         caixaAberto = new Caixa();
//         caixaAberto.setId(1L);
//         caixaAberto.setData(LocalDate.now());
//         caixaAberto.setSaldoInicial(new BigDecimal("100.00"));
//         caixaAberto.setStatus(StatusCaixa.ABERTO);
//     }

//     @Test
//     void testAbrirCaixa_Positive() {
//         // Given: Um saldo inicial válido para abrir o caixa
//         AbrirCaixaRequest request = new AbrirCaixaRequest(new BigDecimal("100.00"));
//         when(caixaService.abrirCaixa(any(BigDecimal.class))).thenReturn(caixaAberto);

//         // When: A requisição para abrir o caixa é feita
//         ResponseEntity<Caixa> response = caixaController.abrirCaixa(request);

//         // Then: O caixa deve ser aberto com sucesso
//         assertNotNull(response);
//         assertTrue(response.getStatusCode().is2xxSuccessful());
//         assertEquals(StatusCaixa.ABERTO, response.getBody().getStatus());
//         assertEquals(new BigDecimal("100.00"), response.getBody().getSaldoInicial());
//     }

//     @Test
//     void testAbrirCaixa_Negative_CaixaJaAberto() {
//         // Given: Já existe um caixa aberto no sistema
//         AbrirCaixaRequest request = new AbrirCaixaRequest(new BigDecimal("150.00"));
//         when(caixaService.abrirCaixa(any(BigDecimal.class))).thenThrow(new IllegalStateException("Já existe um caixa aberto para a data de hoje."));

//         // When: Tenta-se abrir um novo caixa
//         // Then: O sistema deve impedir a ação com uma exceção
//         assertThrows(IllegalStateException.class, () -> {
//             caixaController.abrirCaixa(request);
//         });
//     }

//     @Test
//     void testRegistrarPagamento_Positive() {
//         // Given: Um pagamento válido para um atendimento finalizado
//         RegistrarPagamentoRequest request = new RegistrarPagamentoRequest(1L, "PIX");
        
//         // O método no serviço não retorna nada (void), então usamos doNothing()
//         doNothing().when(pagamentoService).registrarPagamento(anyLong(), anyString());

//         // When: A requisição para registrar o pagamento é feita
//         ResponseEntity<Void> response = pagamentoController.registrarPagamento(request);

//         // Then: O pagamento deve ser registrado com sucesso (status 200 OK)
//         assertNotNull(response);
//         assertTrue(response.getStatusCode().is2xxSuccessful());
        
//         // E podemos verificar se o método do serviço foi chamado exatamente 1 vez
//         verify(pagamentoService, times(1)).registrarPagamento(1L, "PIX");
//     }

//     @Test
//     void testRegistrarPagamento_Negative_CaixaFechado() {
//         // Given: O caixa do dia está fechado
//         RegistrarPagamentoRequest request = new RegistrarPagamentoRequest(1L, "DINHEIRO");
        
//         // Configuramos o mock para lançar uma exceção quando o método for chamado
//         doThrow(new IllegalStateException("O caixa está fechado."))
//             .when(pagamentoService).registrarPagamento(anyLong(), anyString());

//         // When: Tenta-se registrar um pagamento
//         // Then: O sistema deve lançar uma exceção
//         assertThrows(IllegalStateException.class, () -> {
//             pagamentoController.registrarPagamento(request);
//         });
//     }

//     @Test
//     void testFecharCaixa_Positive() {
//         // Given: Um caixa aberto pronto para ser fechado
//         Caixa caixaFechado = new Caixa();
//         caixaFechado.setId(1L);
//         caixaFechado.setStatus(StatusCaixa.FECHADO);
//         caixaFechado.setSaldoFinalCalculado(new BigDecimal("550.75"));

//         when(caixaService.fecharCaixa()).thenReturn(caixaFechado);

//         // When: A requisição para fechar o caixa é feita
//         ResponseEntity<Caixa> response = caixaController.fecharCaixa();

//         // Then: O caixa é fechado e o relatório final é retornado
//         assertNotNull(response);
//         assertTrue(response.getStatusCode().is2xxSuccessful());
//         assertEquals(StatusCaixa.FECHADO, response.getBody().getStatus());
//         assertNotNull(response.getBody().getSaldoFinalCalculado());
//     }
// }