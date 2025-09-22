Feature: Agendamentos via Serviços

  # Criação e conflitos de horário
  Scenario: Criar agendamento em horário livre (sucesso)
    Given que escolho um horário futuro livre para o profissional "João Barbeiro" às 10:00
    When solicito a criação do agendamento
    Then o sistema deve exibir a mensagem "Agendamento criado com sucesso."

  # Scenario: Impedir criação quando horário já está ocupado (falha)
  #   Given que já existe um agendamento para "João Barbeiro" às 10:00
  #   And que tento criar outro agendamento no mesmo horário
  #   When solicito a criação do agendamento
  #   Then o sistema deve exibir a mensagem "Já existe um agendamento com João Barbeiro no horário especificado."

  # # Jornada de trabalho
  # Scenario: Impedir agendamento fora da jornada de trabalho (falha)
  #   Given que escolho um horário fora da jornada do profissional "João Barbeiro" às 02:00
  #   When solicito a criação do agendamento
  #   Then o sistema deve exibir a mensagem "João Barbeiro não está disponível no horário solicitado."

  # # Cancelamento com antecedência mínima
  # Scenario: Impedir cancelamento com menos de 2 horas de antecedência (falha)
  #   Given que existe um agendamento marcado para ocorrer em menos de 2 horas com status "PENDENTE"
  #   When solicito o cancelamento deste agendamento
  #   Then o sistema deve exibir a mensagem "Não é permitido cancelar agendamentos com menos de 2 horas de antecedência."

  # # Atribuição automática
  # Scenario: Atribuir o primeiro profissional disponível quando não informado (sucesso)
  #   Given que não informei um profissional explicitamente
  #   And que escolho um horário futuro livre às 11:00
  #   When solicito a criação do agendamento
  #   Then o sistema deve exibir a mensagem "Agendamento criado com sucesso."
  #   And o sistema deve exibir o profissional atribuído automaticamente
