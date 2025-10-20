Feature: Agendamentos via Serviços

  # ------------------------- Criação quando horário está livre -------------------------
  Scenario: Criar agendamento em horário livre com sucesso
    Given que o cliente selecionou um horário futuro livre para o profissional
    When o cliente envia a solicitação de criação do agendamento
    Then o sistema confirma a criação do agendamento com sucesso

  # ------------------------- Conflito de horário -------------------------
  Scenario: Impedir criação de agendamento quando o horário já está ocupado
    Given que já existe um agendamento ativo para o profissional "João Barbeiro" no mesmo horário
    When o cliente tenta criar outro agendamento para o mesmo horário
    Then o sistema deve recusar a criação por conflito de horário

  # ------------------------- Janela de jornada de trabalho -------------------------
  Scenario: Bloquear agendamento fora da jornada de trabalho
    Given Dado que o sistema funciona das 8h às 18h
    When quando cliente solicita um horário fora da jornada
    Then o sistema deve negar o agendamento por estar não estar entre 8h e 18h

  # ------------------------- Cancelamento com antecedência mínima -------------------------
  Scenario: Impedir cancelamento com menos de duas horas de antecedência
    Given que existe um agendamento marcado para ocorrer em menos de duas horas e com status "PENDENTE"
    When o cliente solicita o cancelamento desse agendamento
    Then o sistema deve recusar o cancelamento por descumprir o prazo mínimo

  # ------------------------- Atribuição automática de profissional -------------------------
  Scenario: Atribuir profissional automaticamente quando não informado (sucesso simples)
    Given que o cliente não informou nenhum profissional ao criar o agendamento
    When a solicitação de agendamento é processada pelo sistema
    Then o sistema deve criar o agendamento atribuindo o primeiro profissional disponível

  Scenario: Falhar ao atribuir profissional automaticamente quando não há profissionais disponíveis
    Given que o cliente não informou nenhum profissional ao criar o agendamento e não existie profissional disponível no horário
    When a solicitação de agendamento é processada pelo sistema
    Then o sistema deve recusar a criação do agendamento informando que não há profissionais disponíveis
