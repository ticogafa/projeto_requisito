
Feature: Agendamento via AgendamentoController
  Para organizar horários na barbearia
  Como cliente do sistema
  Quero criar, cancelar e validar agendamentos conforme regras de negócio

  Context:
    Given que existe um Cliente válido
    And que existe um Profissional chamado "João Barbeiro" com jornada de trabalho configurada
    And que existe um Serviço oferecido válido
    And que o Status padrão de novos agendamentos é "PENDENTE"

  # ------------------------- Criação quando horário está livre -------------------------
  Scenario: Criar agendamento em horário livre (sucesso)
    Given que escolho um horário futuro livre para o profissional
    And que informo cliente, profissional, serviço e data/hora
    When solicito a criação do agendamento
    Then o sistema responde sucesso (HTTP 2xx)
    And retorna o agendamento criado com um ID preenchido

  Scenario: Impedir criação quando horário já está ocupado (falha)
    Given que já existe um agendamento para "João Barbeiro" no mesmo horário
    And que tento criar outro agendamento nesse horário com os mesmos Givens essenciais
    When solicito a criação do novo agendamento
    Then o sistema rejeita a operação
    And exibe a mensagem: "Já existe um agendamento com João Barbeiro no horário especificado."

  # ------------------------- Janela de jornada de trabalho -------------------------
  Scenario: Criar agendamento dentro da jornada de trabalho (sucesso)
    Given que escolho um horário futuro às 10:00 dentro da jornada do profissional
    And que informo cliente, profissional, serviço e data/hora
    When solicito a criação do agendamento
    Then o sistema responde sucesso (HTTP 2xx)
    And retorna o corpo do agendamento criado

  Scenario: Impedir agendamento fora da jornada de trabalho (falha)
    Given que escolho um horário futuro às 02:00 fora da jornada do profissional
    And que informo cliente, profissional, serviço e data/hora
    When solicito a criação do agendamento
    Then o sistema rejeita a operação
    And exibe a mensagem: "João Barbeiro não está disponível no horário solicitado."

  # ------------------------- Cancelamento com antecedência mínima -------------------------
  Scenario: Cancelar agendamento com mais de 2 horas de antecedência (sucesso)
    Given que existe um agendamento marcado para ocorrer em mais de 2 horas
    And que o status atual do agendamento é "PENDENTE"
    When solicito o cancelamento deste agendamento
    Then o sistema responde sucesso (HTTP 2xx)
    And o status do agendamento passa a ser "CANCELADO"

  Scenario: Impedir cancelamento com menos de 2 horas de antecedência (falha)
    Given que existe um agendamento marcado para ocorrer em menos de 2 horas
    And que o status atual do agendamento é "PENDENTE"
    When solicito o cancelamento deste agendamento
    Then o sistema rejeita a operação
    And exibe a mensagem: "Não é permitido cancelar agendamentos com menos de 2 horas de antecedência."

  # ------------------------- Atribuição automática de profissional -------------------------
  Scenario: Atribuir o primeiro profissional disponível quando não informado (sucesso)
    Given que não informei um profissional explicitamente
    And que há pelo menos um profissional disponível no horário solicitado
    And que informo cliente, serviço e data/hora
    When solicito a criação do agendamento
    Then o sistema responde sucesso (HTTP 2xx)
    And o agendamento retornado possui um profissional atribuído automaticamente

  Scenario: Impedir criação sem profissional quando nenhum está disponível (falha)
    Given que não informei um profissional explicitamente
    And que nenhum profissional está disponível no horário solicitado
    And que informo cliente, serviço e data/hora
    When solicito a criação do agendamento
    Then o sistema rejeita a operação
    And exibe a mensagem: "Nenhum profissional disponível no horário solicitado."
