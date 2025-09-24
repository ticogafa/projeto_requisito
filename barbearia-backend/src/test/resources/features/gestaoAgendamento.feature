Feature: Gestão de Agendamentos (cenários essenciais)
	Foco nos dois casos mais críticos do negócio:
	1. Criar agendamento válido (fluxo feliz)
	2. Impedir criação em horário já ocupado (proteção contra conflito)

	Scenario: Criar agendamento em horário livre (sucesso)
		Given que existe um profissional cadastrado com determinado horário livre
		When solicito a criação do agendamento em horário livre para o profissional
		Then o sistema exibe a mensagem: "Agendamento criado com sucesso"

	Scenario: Impedir criação quando já existe agendamento no mesmo horário (conflito)
		Given que existe um agendamento para o profissional cadastrado em um horário determinado
		When solicito a criação do agendamento no horário determinado para o profissional
		Then o sistema exibe a mensagem "Já existe um agendamento"

	# Notas:
	# - O segundo cenário reaproveita os mesmos passos de criação, alterando apenas o Given inicial para causar o conflito.
	# - Implementar step: "que já existe um agendamento para o mesmo profissional e horário selecionado" criando dados pré-existentes antes de montar o request.
	# - O passo Then usa substring (contains) para tolerar formatação de data na mensagem.

