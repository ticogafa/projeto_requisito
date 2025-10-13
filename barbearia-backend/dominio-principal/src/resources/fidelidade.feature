Funcionalidade: Gestão de Pontos de Fidelidade

  Cenário: Cliente ganha pontos ao realizar um serviço
    Dado um cliente com 100 pontos de fidelidade
    Quando ele realiza um serviço que vale 20 pontos
    Então seu saldo de pontos de fidelidade deve ser 120

  Cenário: Cliente resgata pontos para obter um desconto
    Dado um cliente com 150 pontos de fidelidade
    Quando ele resgata 100 pontos
    Então seu saldo de pontos de fidelidade deve ser 50
    E um desconto deve ser aplicado na sua compra

  Cenário: Cliente acumula pontos de múltiplos serviços em um único atendimento
    Dado um cliente com pontos de fidelidade
    E que o serviço de "corte e barba" ganha pontos
    Quando o cliente finaliza um atendimento com os serviços de "corte" e "barba"
    Então seu saldo final de pontos de fidelidade deve ser igual ao seu inicial mais os pontos dos dois serviços

  Cenário: Cliente perde pontos por não comparecimento
    Dado um cliente com 100 pontos de fidelidade
    E que este cliente possui um atendimento agendado
    Quando o atendimento é marcado como "não compareceu"
    Então o saldo de pontos de fidelidade do cliente deve ser 50  

Funcionalidade: Gestão de Caixa

  Cenário: Registro de uma entrada no caixa após um serviço
    Dado que o caixa da barbearia tem um saldo inicial de 100.00
    Quando um serviço de "Corte" no valor de 50.00 é finalizado e pago
    Então o saldo final do caixa deve ser 150.00

  Cenário: Registro de uma saída no caixa para compra de material
    Dado que o caixa da barbearia tem um saldo inicial de 200.00
    Quando uma despesa de "Compra de Shampoos" no valor de 80.00 é registrada
    Então o saldo final do caixa deve ser 120.00

  Cenário: Cálculo do saldo final após múltiplas transações
    Dado que o caixa da barbearia tem um saldo inicial de 300.00
    Quando um serviço de "Barba" no valor de 30.00 é pago
    E uma despesa de "Pagamento de conta de luz" no valor de 110.00 é registrada
    E um serviço de "Corte" no valor de 50.00 é pago
    Então o saldo final do caixa deve ser 270.00

  Cenário: Cliente fica devendo um serviço e não altera o caixa
    Dado que o caixa da barbearia tem um saldo inicial de 500.00
    Quando um cliente finaliza um serviço de "Corte Especial" no valor de 70.00 mas não paga
    Então o saldo do caixa deve permanecer 500.00
    E uma dívida de 70.00 deve ser registrada para o cliente

  Cenário: Cliente paga uma dívida pendente
    Dado que o caixa da barbearia tem um saldo inicial de 400.00
    E um cliente possui uma dívida pendente de 70.00
    Quando o cliente paga a dívida de 70.00
    Então o saldo final do caixa deve ser 470.00
    E a dívida do cliente deve ser marcada como "paga"