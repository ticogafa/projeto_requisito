# Funcionalidade: Gestão de Pontos de Fidelidade

#   Cenário: Cliente ganha pontos ao realizar um serviço
#     Dado um cliente com 100 pontos de fidelidade
#     Quando ele realiza um serviço que vale 20 pontos
#     Então seu saldo de pontos de fidelidade deve ser 120

#   Cenário: Cliente resgata pontos para obter um desconto
#     Dado um cliente com 150 pontos de fidelidade
#     Quando ele resgata 100 pontos
#     Então seu saldo de pontos de fidelidade deve ser 50
#     E um desconto deve ser aplicado na sua compra

#   Cenário: Cliente acumula pontos de múltiplos serviços em um único atendimento
#     Dado um cliente com pontos de fidelidade
#     E que o serviço de "corte e barba" ganha pontos
#     Quando o cliente finaliza um atendimento com os serviços de "corte" e "barba"
#     Então seu saldo final de pontos de fidelidade deve ser igual ao seu inicial mais os pontos dos dois serviços

#   Cenário: Cliente perde pontos por não comparecimento
#     Dado um cliente com 100 pontos de fidelidade
#     E que este cliente possui um atendimento agendado
#     Quando o atendimento é marcado como "não compareceu"
#     Então o saldo de pontos de fidelidade do cliente deve ser 50  
