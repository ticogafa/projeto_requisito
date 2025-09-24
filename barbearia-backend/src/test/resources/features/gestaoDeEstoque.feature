Feature: Gestão de Estoque e PDV
  Para garantir controle adequado de produtos e vendas no PDV
  Como operador ou administrador do sistema
  Quero cadastrar produtos e registrar vendas corretamente

  Background:
    Given que o sistema está operacional
    And que estou autenticado como operador de PDV

  # Cenário positivo representativo: fluxo completo de venda envolvendo estoque
  Scenario: Registrar venda PDV com produto reduzindo estoque (sucesso)
    Given que existe um produto "Gel Fixador" com estoque 50
    When eu envio a venda de 2 produtos "Gel Fixador" para registro
    Then o sistema responde sucesso
    And o estoque atual do produto "Gel Fixador" passa a ser 48

  # Cenário negativo essencial: validação crítica de regra de estoque insuficiente
  Scenario: Impedir venda PDV com estoque insuficiente (falha)
    Given que existe um produto "Pomada Forte" com estoque 2
    When envio uma venda PDV com 5 unidades do produto "Pomada Forte"
    Then o sistema rejeita a operação
