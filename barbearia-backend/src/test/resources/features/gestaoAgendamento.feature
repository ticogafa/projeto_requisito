Feature: Gestão de Agendamentos (cenários essenciais)
	Foco nos dois casos mais críticos do negócio:
	1. Criar agendamento válido (fluxo feliz)
	2. Impedir criação em horário já ocupado (proteção contra conflito)

	Scenario: Criar agendamento em horário livre (sucesso)
		Given que escolho um horário futuro livre para o profissional
		And que informo as informações essenciais para o agendamento
		When solicito a criação do agendamento em horário livre
		Then exibe a mensagem: "Agendamento criado com sucesso"

	Scenario: Impedir criação quando já existe agendamento no mesmo horário (conflito)
		Given que já existe um agendamento para o mesmo profissional e horário selecionado
		And que informo as informações essenciais para o agendamento
		When solicito a criação do agendamento em horário livre
		Then exibe a mensagem: "Já existe um agendamento com"

	# Notas:
	# - O segundo cenário reaproveita os mesmos passos de criação, alterando apenas o Given inicial para causar o conflito.
	# - Implementar step: "que já existe um agendamento para o mesmo profissional e horário selecionado" criando dados pré-existentes antes de montar o request.
	# - O passo Then usa substring (contains) para tolerar formatação de data na mensagem.

