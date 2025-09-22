
# Feature: Agendamentos via Serviços

#   # ------------------------- Criação quando horário está livre -------------------------
#   Scenario: Criar agendamento em horário livre (sucesso)
#     Given que escolho um horário futuro livre para o profissional
#     And que informo as informações essenciais
#     When solicito a criação do agendamento
#     Then o sistema responde sucesso

#   Scenario: Impedir criação quando horário já está ocupado (falha)
#     Given que já existe um agendamento para "João Barbeiro" no mesmo horário
#     And que tento criar outro agendamento nesse horário com os mesmos Givens essenciais
#     When solicito a criação do novo agendamento
#     Then o sistema rejeita a operação

#   # ------------------------- Janela de jornada de trabalho -------------------------

#   Scenario: Impedir agendamento fora da jornada de trabalho (falha)
#     Given que escolho um agendamento inválido às 02:00 fora da jornada do profissional
#     And que informo as informações essenciais
#     When solicito a criação do agendamento
#     Then o sistema rejeita a operação

#   # ------------------------- Cancelamento com antecedência mínima -------------------------

#   Scenario: Impedir cancelamento com menos de 2 horas de antecedência (falha)
#     Given que existe um agendamento marcado para ocorrer em menos de 2 horas
#     And que o status atual do agendamento é "PENDENTE"
#     When solicito o cancelamento deste agendamento
#     Then o sistema rejeita a operação

#   # ------------------------- Atribuição automática de profissional -------------------------
#   Scenario: Atribuir o primeiro profissional disponível quando não informado (sucesso)
#     Given que não informei um profissional explicitamente
#     When solicito a criação do agendamento
#     Then o sistema responde sucesso

  # # Atribuição automática
  # Scenario: Atribuir o primeiro profissional disponível quando não informado (sucesso)
  #   Given que não informei um profissional explicitamente
  #   And que escolho um horário futuro livre às 11:00
  #   When solicito a criação do agendamento
  #   Then o sistema deve exibir a mensagem "Agendamento criado com sucesso."
  #   And o sistema deve exibir o profissional atribuído automaticamente
