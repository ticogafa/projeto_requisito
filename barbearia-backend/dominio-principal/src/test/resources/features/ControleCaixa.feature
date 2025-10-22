# language: pt

Funcionalidade: Gestão de Caixa da Barbearia

  Como gerente da barbearia,
  Eu quero registrar entradas, saídas e dívidas de clientes,
  Para manter o controle financeiro do estabelecimento.

  Cenário: Registrar uma entrada de pagamento em dinheiro
    Dado que o caixa da barbearia tem um saldo inicial de "100,0"
    Quando um serviço de "Corte de Cabelo" no valor de "50,0" é pago em "DINHEIRO"
    Então o saldo final do caixa deve ser "150,0"

  Cenário: Registrar uma saída para despesas
    Dado que o caixa da barbearia tem um saldo inicial de "150,0"
    Quando uma despesa de "Compra de Shampoo" no valor de "30,0" é registrada
    Então o saldo final do caixa deve ser "120,0"

  Cenário: Registrar uma dívida quando um cliente não paga
    Dado que o caixa da barbearia tem um saldo inicial de "200,0"
    Quando um cliente finaliza um serviço de "Barba" no valor de "40,0" mas não paga
    Então o saldo final do caixa deve ser "200,0"
    E uma dívida de "40,0" deve ser registrada para o cliente

  Cenário: Registrar o pagamento de uma dívida pendente
    Dado que o caixa da barbearia tem um saldo inicial de "300,0"
    E um cliente possui uma dívida pendente de "40,0"
    Quando o cliente paga a dívida de "40,0" em "PIX"
    Então o saldo final do caixa deve ser "340,0"
    E a dívida do cliente deve ser marcada como "PAGO"