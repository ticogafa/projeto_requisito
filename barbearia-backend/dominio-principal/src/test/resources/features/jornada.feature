Feature: Gestão de Jornada de Trabalho

  # ------------------------- Atualização de Jornada -------------------------
  Scenario: Atualizar jornada de trabalho com sucesso
    Given que o profissional "Carlos" deseja atualizar sua jornada
    And define que na "SEGUNDA" trabalha das "09:00" às "18:00"
    When o profissional envia a solicitação de atualização de jornada
    Then o sistema confirma a atualização da jornada com sucesso

  Scenario: Impedir atualização com horário de fim anterior ao início
    Given que o profissional "Carlos" deseja atualizar sua jornada
    And define que na "TERCA" trabalha das "18:00" às "09:00"
    When o profissional envia a solicitação de atualização de jornada
    Then o sistema deve recusar a atualização por horário inválido

  Scenario: Impedir atualização com intervalo fora do expediente
    Given que o profissional "Carlos" deseja atualizar sua jornada
    And define que na "QUARTA" trabalha das "08:00" às "17:00"
    And define um intervalo das "18:00" às "19:00"
    When o profissional envia a solicitação de atualização de jornada
    Then o sistema deve recusar a atualização por intervalo inválido
