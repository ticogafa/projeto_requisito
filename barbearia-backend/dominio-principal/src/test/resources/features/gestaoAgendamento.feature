Feature: Gestão de Agendamentos (cenários essenciais)
	Para garantir controle adequado de agendamentos na barbearia
	Como operador ou cliente do sistema
	Quero criar e gerenciar agendamentos respeitando regras de negócio

	Background:
		Given que o sistema está operacional

	# Cenários Básicos de Criação
	Scenario: Criar agendamento em horário livre (sucesso)
		Given que existe um profissional cadastrado com determinado horário livre
		When solicito a criação do agendamento em horário livre para o profissional
		Then o sistema exibe a mensagem: "Agendamento criado com sucesso"

	Scenario: Impedir criação quando já existe agendamento no mesmo horário (conflito)
		Given que existe um agendamento para o profissional cadastrado em um horário determinado
		When solicito a criação do agendamento no horário determinado para o profissional
		Then o sistema exibe a mensagem "Já existe um agendamento"

	# Cenários de Validação de Serviço e Profissional
	Scenario: Criar agendamento com serviço associado ao profissional (POSITIVO)
		Given que existe o profissional "João" qualificado para agendamento de "Corte Masculino"
		And que o serviço "Corte Masculino" está ativo
		When eu crio um agendamento do serviço "Corte Masculino" com o profissional "João"
		Then o agendamento é criado com sucesso

	Scenario: Impedir agendamento de serviço não associado ao profissional (NEGATIVO)
		Given que existe o profissional "João" sem qualificação para agendamento de "Manicure"
		When eu tento criar um agendamento do serviço "Manicure" com o profissional "João"
		Then o sistema rejeita a operação de agendamento

	Scenario: Impedir agendamento com serviço inativo (NEGATIVO)
		Given que o serviço "Maquiagem" está inativo para agendamento por "falta de insumos"
		When eu tento criar um agendamento do serviço "Maquiagem"
		Then o sistema rejeita a operação de agendamento

	# # Cenários de Horário e Duração
	# Scenario: Validar duração do serviço no agendamento (POSITIVO)
	# 	Given que o serviço "Corte Feminino" tem duração de 60 minutos
	# 	When eu crio um agendamento às "14:00" para o serviço "Corte Feminino"
	# 	Then o sistema reserva o horário até "15:00"

	# Scenario: Respeitar intervalo de limpeza entre serviços (POSITIVO)
	# 	Given que o serviço "Corte Feminino" tem um intervalo de limpeza de 10 minutos
	# 	And que existe um agendamento até "15:00"
	# 	When eu tento criar um agendamento às "15:05"
	# 	Then o sistema rejeita a operação por não respeitar o intervalo de limpeza

	# Scenario: Permitir agendamento após intervalo de limpeza (POSITIVO)
	# 	Given que o serviço "Corte Feminino" tem um intervalo de limpeza de 10 minutos
	# 	And que existe um agendamento até "15:00"
	# 	When eu crio um agendamento às "15:10"
	# 	Then o agendamento é criado com sucesso

	# # Cenários de Add-on
	# Scenario: Agendar serviço add-on junto com serviço principal (POSITIVO)
	# 	Given que o serviço "Hidratação" é um add-on para agendamento de "Corte"
	# 	When eu crio um agendamento do serviço "Corte" com add-on "Hidratação"
	# 	Then ambos os serviços são agendados em sequência

	# Scenario: Impedir agendamento de add-on sem serviço principal (NEGATIVO)
	# 	Given que o serviço "Hidratação" é um add-on para agendamento de "Corte"
	# 	When eu tento agendar apenas o serviço "Hidratação"
	# 	Then o sistema rejeita a operação de agendamento

	# # Cenários de Disponibilidade de Profissional
	# Scenario: Respeitar jornada de trabalho do profissional (NEGATIVO)
	# 	Given que o profissional "Paulo Reis" trabalha 8 horas por dia até "18:00"
	# 	When eu tento criar um agendamento às "18:30" para "Paulo Reis"
	# 	Then o sistema rejeita a operação de agendamento

	# Cenários de Cancelamento e Alteração
	Scenario: Cancelar agendamento com antecedência (POSITIVO)
		Given que existe um agendamento para amanhã às "14:00"
		When eu cancelo o agendamento
		Then o horário fica disponível novamente

	# Scenario: Impedir cancelamento de agendamento em andamento (NEGATIVO)
	# 	Given que existe um agendamento em andamento
	# 	When eu tento cancelar o agendamento
	# 	Then o sistema rejeita a operação de agendamento

	# Scenario: Reagendar para horário disponível (POSITIVO)
	# 	Given que existe um agendamento para "14:00"
	# 	And que o horário "16:00" está livre para o mesmo profissional
	# 	When eu reagendo o serviço para "16:00"
	# 	Then o agendamento é alterado com sucesso

	# # Cenários de Cliente
	# Scenario: Criar agendamento para cliente cadastrado (POSITIVO)
	# 	Given que existe um cliente cadastrado "Maria Silva"
	# 	When eu crio um agendamento para a cliente "Maria Silva"
	# 	Then o agendamento é vinculado ao cadastro da cliente

	# Scenario: Impedir agendamento sem identificação do cliente (NEGATIVO)
	# 	Given que não informo dados do cliente
	# 	When eu tento criar um agendamento
	# 	Then o sistema rejeita a operação de agendamento