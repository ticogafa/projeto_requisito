Feature: Gestão de Agendamentos (cenários essenciais)
	Para garantir controle adequado de agendamentos na barbearia
	Como operador ou cliente do sistema
	Quero criar e gerenciar agendamentos respeitando regras de negócio

	# Cenários Básicos de Criação
	Scenario: Criar agendamento em horário livre (sucesso)
		Given que existe um profissional cadastrado com o horário "14:00" livre
		When solicito a criação do agendamento no horário "14:00" para o profissional "João"
		Then o agendamento é criado com sucesso

	Scenario: Impedir criação quando já existe agendamento no mesmo horário (conflito)
		Given que existe um agendamento para o profissional cadastrado no horário "14:00"
		When solicito a criação do agendamento no horário "14:00" para o profissional "João"
		Then o sistema rejeita a operação de agendamento

	# Cenários de Validação de Serviço e Profissional
	Scenario: Criar agendamento com serviço associado ao profissional (POSITIVO)
		Given que existe o profissional "João" qualificado para agendamento de "Corte Masculino"
		And que o serviço "Corte Masculino" está ativo
		When eu crio um agendamento do serviço "Corte Masculino" com o profissional "João"
		Then o agendamento é criado com sucesso

	Scenario: Impedir agendamento de serviço não associado ao profissional (NEGATIVO)
		Given que existe o profissional "João" sem qualificação para agendamento de "Manicure"
		When eu crio um agendamento do serviço "Manicure" com o profissional "João"
		Then o sistema rejeita a operação de agendamento

	Scenario: Impedir agendamento com serviço inativo (NEGATIVO)
		Given que o serviço "Maquiagem" está inativo para agendamento por "falta de insumos"
		When eu tento criar um agendamento do serviço "Maquiagem"
		Then o sistema rejeita a operação de agendamento

	# Cenários de Cancelamento e Alteração
	Scenario: Cancelar agendamento com antecedência (POSITIVO)
		Given que existe um agendamento para amanhã às "14:00"
		When eu cancelo o agendamento
		Then o horário fica disponível novamente