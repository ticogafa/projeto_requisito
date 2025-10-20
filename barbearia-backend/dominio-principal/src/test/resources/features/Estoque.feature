Feature: Gestão de Estoque e PDV
  Para garantir controle adequado de produtos e vendas no PDV
  Como operador ou administrador do sistema
  Quero cadastrar produtos e registrar vendas corretamente

  # Cenários de Cadastro de Produtos
  Scenario: Cadastrar produto com nome único com sucesso (POSITIVO)
    Given que não existe um produto chamado "Shampoo Anticaspa"
    When eu cadastro um novo produto com o nome "Shampoo Anticaspa" e estoque inicial 100
    Then o produto é cadastrado com sucesso

  Scenario: Impedir cadastro de produto com nome duplicado (NEGATIVO)
    Given que já existe um produto chamado "Gel Fixador"
    When eu tento cadastrar um novo produto com o nome "Gel Fixador"
    Then o sistema rejeita a operação de cadastro com nome duplicado

  # Cenários de Atualização de Estoque
  Scenario: Atualizar estoque com quantidade válida (POSITIVO)
    Given que existe um produto "Pomada Modeladora" com estoque 25
    When eu adiciono 15 unidades ao estoque
    Then o estoque atual do produto "Pomada Modeladora" passa a ser 40

  # Cenários de Venda PDV
  Scenario: Registrar venda PDV com produto reduzindo estoque (sucesso)
    Given que existe um produto "Gel Fixador" com estoque 50
    When eu envio a venda de 2 produtos "Gel Fixador" para registro
    Then o sistema responde sucesso e registra a venda
    And o estoque atual do produto "Gel Fixador" passa a ser 48

  Scenario: Impedir venda PDV com estoque insuficiente (falha)
    Given que existe um produto "Pomada Forte" com estoque 2
    When eu envio a venda de 5 produtos "Pomada Forte" para registro
    Then o sistema rejeita a operação de venda com estoque insuficiente
