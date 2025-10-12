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