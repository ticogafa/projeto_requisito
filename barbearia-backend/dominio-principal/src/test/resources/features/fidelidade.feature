Feature: Pontos de fidelidade
  Para recompensar clientes por atendimentos
  Como gestor do sistema
  Eu quero creditar pontos de fidelidade por valor pago

  Scenario: Cliente ganha pontos proporcionais ao valor pago
    Given um cliente com 0 pontos
    When registrar um atendimento no valor de 150.75 reais
    Then o cliente deve ter 150 pontos de fidelidade
