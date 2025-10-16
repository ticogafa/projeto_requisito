package com.cesarschool.cucumber.fidelidade;
// package com.cesarschool.cucumber;

// import io.cucumber.java.pt.Dado;
// import io.cucumber.java.pt.Quando;
// import io.cucumber.java.pt.Então;
// import static org.junit.jupiter.api.Assertions.assertEquals;

// // Supondo a existência dessas classes do seu domínio
// import com.cesarschool.barbearia.dominio.principal.fidelidade.Pontuacao;
// import com.cesarschool.barbearia.dominio.principal.fidelidade.FidelidadeServico;

// public class FidelidadeStepDefinitions {

//     private Pontuacao pontuacaoCliente;
//     private FidelidadeServico fidelidadeServico = new FidelidadeServico(/*mock do repositório aqui*/);
//     private boolean descontoAplicado = false;

//     @Dado("um cliente com {int} pontos de fidelidade")
//     public void um_cliente_com_pontos_de_fidelidade(Integer pontosIniciais) {
//         // Aqui você criaria um mock do cliente e sua pontuação
//         this.pontuacaoCliente = new Pontuacao(/*clienteId*/ pontosIniciais);
//     }

//     @Quando("ele realiza um serviço que vale {int} pontos")
//     public void ele_realiza_um_servico_que_vale_pontos(Integer pontosGanhos) {
//         // Simula a chamada ao serviço que adiciona pontos
//         this.fidelidadeServico.adicionarPontos(this.pontuacaoCliente, pontosGanhos);
//     }

//     @Quando("ele resgata {int} pontos")
//     public void ele_resgata_pontos(Integer pontosResgatados) {
//         // Simula a chamada ao serviço que resgata pontos
//         this.fidelidadeServico.resgatarPontos(this.pontuacaoCliente, pontosResgatados);
//         this.descontoAplicado = true; // Simula que o resgate gerou um desconto
//     }

//     @Então("seu saldo de pontos de fidelidade deve ser {int}")
//     public void seu_saldo_de_pontos_de_fidelidade_deve_ser(Integer pontosFinais) {
//         assertEquals(pontosFinais, this.pontuacaoCliente.getSaldoDePontos());
//     }

//     @Então("um desconto deve ser aplicado na sua compra")
//     public void um_desconto_deve_ser_aplicado_na_sua_compra() {
//         assertEquals(true, this.descontoAplicado);
//     }
// }