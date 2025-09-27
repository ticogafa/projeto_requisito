Feature: Gestão de profissionais

# ------------------------- Profissional deve ser cadastrado com nome e contato -------------------------
    Scenario: Cadastrar profissional com todos os campos obrigatorios preenchidos
        Given que sou um administrador logado
        When eu preencho o formulário de novo profissional com nome "Carlos Andrade" e email "carlos.a@email.com"
        Then o profissional é cadastrado com sucesso
        And "Carlos Andrade" aparece na lista de profissionais

    Scenario: Impedir cadastro de profissional sem o nome
        Given que sou um administrador logado
        When eu tento submeter o formulário de novo profissional apenas com o email "carlos.a@email.com"
        Then o sistema rejeita a operação
        And exibe a mensagem de erro "O campo 'nome' é obrigatório."

# ------------------------- Não permitir cadastro com e-mail duplicado -------------------------
    Scenario: Cadastrar um segundo profissional com email diferente
        Given que já existe um profissional com o email "ana.s@email.com"
        When eu cadastro um novo profissional com nome "Beatriz Costa" e email "beatriz.c@email.com"
        Then o profissional "Beatriz Costa" é cadastrado com sucesso

    Scenario: Impedir cadastro de profissional com email que ja existe
        Given que já existe um profissional com o email "ana.s@email.com"
        When eu tento cadastrar um novo profissional com o email "ana.s@email.com"
        Then o sistema rejeita a operação
        And exibe a mensagem de erro "Este e-mail já está em uso."

# ------------------------- Status padrão deve ser "Ativo" -------------------------
    Scenario: Verificar status "Ativo" apos o cadastro
        Given que eu cadastro um novo profissional chamado "Daniel Farias"
        When eu visualizo os detalhes do perfil de "Daniel Farias"
        Then o status do profissional deve ser "Ativo"

    Scenario: Verificar que o status do novo profissional nao é "Inativo"
        Given que eu cadastro um novo profissional chamado "Daniel Farias"
        When eu visualizo os detalhes do perfil de "Daniel Farias"
        Then o status do profissional não deve ser "Inativo" ou nulo

# ------------------------- Uma agenda deve ser criada no cadastro -------------------------
    Scenario: Verificar se uma agenda foi criada para o novo profissional
        Given que eu cadastro um novo profissional chamado "Eduardo Lima"
        When o sistema processa o cadastro com sucesso
        Then uma agenda, inicialmente vazia, deve ser associada ao perfil de "Eduardo Lima"

    Scenario: Garantir que nenhuma agenda seja criada se o cadastro falhar
        Given que tento cadastrar um profissional com um email duplicado
        When o sistema rejeita a operação de cadastro
        Then nenhuma nova agenda deve ser criada no sistema

# ------------------------- Nível de senioridade padrão deve ser "Júnior" -------------------------
    Scenario: Verificar senioridade "Junior" apos o cadastro
        Given que eu cadastro um novo profissional chamado "Fernanda Alves"
        When eu visualizo os detalhes do perfil de "Fernanda Alves"
        Then o nível de senioridade deve ser "Júnior"

    Scenario: Verificar que a senioridade do novo profissional nao é outra
        Given que eu cadastro um novo profissional chamado "Fernanda Alves"
        When eu visualizo os detalhes do perfil de "Fernanda Alves"
        Then o nível de senioridade não deve ser "Pleno" ou "Sênior"

# ------------------------- Administrador deve poder alterar informações -------------------------
    Scenario: Editar com sucesso o telefone de um profissional existente
        Given que existe um profissional "Gabriel Souza" com telefone "(81)98888-7777"
        When eu edito o perfil de "Gabriel Souza" e altero o telefone para "(81)99999-0000"
        Then a alteração é salva com sucesso
        And o novo telefone de "Gabriel Souza" é "(81)99999-0000"

    Scenario: Falha ao tentar editar um profissional com ID inexistente
        Given que não existe um profissional com o ID "xyz-999"
        When eu tento submeter uma alteração de dados para o profissional com ID "xyz-999"
        Then o sistema rejeita a operação
        And exibe a mensagem de erro "Profissional não encontrado."

# ------------------------- Manter unicidade de e-mail na edição -------------------------
    Scenario: Alterar email de um profissional para outro email unico
        Given que existe o profissional "Hugo Dias" com email "hugo.d@email.com"
        When eu altero o email de "Hugo Dias" para "hugo.dias.novo@email.com"
        Then a alteração é salva com sucesso

    Scenario: Impedir alteracao de email para um que ja esta em uso
        Given que existe o profissional "Gabriel Souza" com email "gabriel.s@email.com"
        And existe o profissional "Hugo Dias" com outro email
        When eu tento alterar o email de "Hugo Dias" para "gabriel.s@email.com"
        Then o sistema rejeita a operação
        And exibe a mensagem de erro "Este e-mail já está em uso."

# ------------------------- Campos obrigatórios não podem ficar em branco na edição -------------------------
    Scenario: Editar um campo opcional mantendo os obrigatorios preenchidos
        Given que existe um profissional "Isabela Matos" com nome "Isabela Matos"
        When eu edito seu perfil para adicionar um apelido "Isa", mantendo o nome
        Then a alteração é salva com sucesso

    Scenario: Impedir edicao que torna um campo obrigatorio vazio
        Given que existe um profissional "Isabela Matos" com nome "Isabela Matos"
        When eu tento editar seu perfil e apagar o conteúdo do campo "nome"
        Then o sistema rejeita a operação
        And exibe a mensagem de erro "O campo 'nome' é obrigatório."

# ------------------------- Poder desativar um profissional "Ativo" -------------------------
    Scenario: Desativar um profissional com status "Ativo"
        Given que "Juliana Rocha" é uma profissional com status "Ativo"
        When eu solicito a desativação do perfil de "Juliana Rocha"
        Then o status de "Juliana Rocha" é alterado para "Inativo"

    Scenario: Impedir nova desativacao de um profissional que ja esta "Inativo"
        Given que "Juliana Rocha" é uma profissional com status "Inativo"
        When eu tento solicitar a desativação do perfil dela novamente
        Then o sistema informa que a ação não pode ser concluída
        And exibe a mensagem "Este profissional já está inativo."

# ------------------------- Profissional desativado não deve aparecer para agendamentos -------------------------
    Scenario: Profissional ativo aparece como opcao para agendamento
        Given que "Karla Mendes" é uma profissional com status "Ativo"
        When eu, como cliente, busco profissionais para o serviço "Corte Feminino"
        Then "Karla Mendes" aparece na lista de profissionais disponíveis

    Scenario: Profissional inativo nao aparece como opcao para agendamento
        Given que "Karla Mendes" foi desativada e seu status é "Inativo"
        When eu, como cliente, busco profissionais para o serviço "Corte Feminino"
        Then "Karla Mendes" não aparece na lista de profissionais disponíveis

# ------------------------- Dados históricos do profissional desativado devem ser mantidos -------------------------
    Scenario: Relatorios antigos ainda exibem dados de profissional que foi desativado
        Given que o profissional "Lucas Neto" realizou 50 atendimentos em Agosto
        And em Setembro, o perfil de "Lucas Neto" foi desativado
        When eu gero um relatório de atendimentos do mês de Agosto
        Then os 50 atendimentos de "Lucas Neto" são exibidos corretamente no relatório

    Scenario: Profissional desativado nao é incluido em novos relatorios de produtividade
        Given que o perfil de "Lucas Neto" foi desativado em Setembro
        When eu gero um relatório de produtividade de profissionais ativos para o mês de Outubro
        Then "Lucas Neto" não deve ser incluído na lista desse novo relatório

# ------------------------- Alertar sobre agendamentos futuros antes de desativar -------------------------
    Scenario: Permitir desativacao de profissional sem agendamentos futuros
        Given que a profissional "Mariana Dias" não possui nenhum agendamento futuro
        When eu tento desativar o perfil de "Mariana Dias"
        Then a operação é concluída com sucesso
        And o status dela muda para "Inativo"

    Scenario: Impedir desativacao de profissional com agendamentos futuros
        Given que a profissional "Mariana Dias" possui um agendamento para amanhã
        When eu tento desativar o perfil de "Mariana Dias"
        Then a operação é bloqueada pelo sistema
        And exibe a mensagem: "Não é possível desativar. Existem agendamentos futuros."

# ------------------------- Poder reativar um profissional "Inativo" -------------------------
    Scenario: Reativar um profissional com status "Inativo"
        Given que "Nuno Farias" é um profissional com status "Inativo"
        When eu solicito a reativação do perfil de "Nuno Farias"
        Then o status de "Nuno Farias" é alterado para "Ativo"

    Scenario: Impedir reativacao de um profissional que ja esta "Ativo"
        Given que "Nuno Farias" é um profissional com status "Ativo"
        When eu tento solicitar a reativação do perfil dele novamente
        Then o sistema informa que a ação não pode ser concluída
        And exibe a mensagem "Este profissional já está ativo."

# ------------------------- Jornada de trabalho deve ser definida com início e fim -------------------------
    Scenario: Definir uma jornada de trabalho valida para um profissional
        Given que o profissional "Otávio Pinto" não tem jornada definida para segunda-feira
        When eu defino a jornada de segunda-feira para "Otávio Pinto" como "09:00" às "18:00"
        Then a jornada é salva com sucesso
        And a agenda de "Otávio Pinto" reflete essa disponibilidade

    Scenario: Impedir definicao de jornada de trabalho com formato de hora invalido
        Given que o profissional "Otávio Pinto" não tem jornada definida para terça-feira
        When eu tento definir a jornada de terça-feira com horário de início "nove horas"
        Then o sistema rejeita a operação
        And exibe a mensagem: "Formato de hora inválido. Use HH:MM."

# ------------------------- Horário de fim da jornada deve ser posterior ao de início -------------------------
    Scenario: Definir jornada de trabalho com horario de fim posterior ao de inicio
        Given que o profissional "Pedro Paiva" não tem jornada definida para quarta-feira
        When eu defino a jornada de quarta-feira com início "10:00" e fim "19:00"
        Then a jornada é salva com sucesso

    Scenario: Impedir definicao de jornada de trabalho com horario de fim anterior ao de inicio
        Given que o profissional "Pedro Paiva" não tem jornada definida para quinta-feira
        When eu tento definir a jornada de quinta-feira com início "19:00" e fim "10:00"
        Then o sistema rejeita a operação
        And exibe a mensagem: "O horário de fim deve ser posterior ao horário de início."

# ------------------------- Deve ser possível registrar um período de ausência -------------------------
    Scenario: Registrar um periodo de ausencia para um profissional
        Given que a profissional "Paula Vieira" está ativa
        When eu registro um período de férias para "Paula Vieira" de "01/10/2025" a "15/10/2025"
        Then o período é salvo com sucesso
        And a agenda de "Paula Vieira" aparece como indisponível entre essas datas

    Scenario: Impedir registro de ausencia com data final anterior a inicial
        Given que a profissional "Paula Vieira" está ativa
        When eu tento registrar um período de férias para "Paula Vieira" de "15/10/2025" a "01/10/2025"
        Then o sistema rejeita a operação
        And exibe a mensagem: "A data final da ausência deve ser posterior à data inicial."

# ------------------------- Períodos de ausência devem bloquear a agenda -------------------------
    Scenario: Verificar que um dia fora do periodo de ausencia esta disponivel
        Given que "Paula Vieira" tem férias registradas de "01/10/2025" a "15/10/2025"
        When um cliente consulta a agenda dela para o dia "30/09/2025"
        Then o dia aparece como disponível para agendamento

    Scenario: Impedir agendamento durante o periodo de ausencia registrado
        Given que "Paula Vieira" tem férias registradas de "01/10/2025" a "15/10/2025"
        When um cliente tenta agendar um serviço com ela para o dia "10/10/2025"
        Then o sistema informa que o profissional não está disponível
        And não permite o agendamento

# ------------------------- Não permitir sobreposição de jornadas de trabalho -------------------------
    Scenario: Definir duas jornadas de trabalho distintas no mesmo dia
        Given que quero definir a jornada de "Raquel Neves" para sábado
        When eu defino uma jornada das "08:00" às "12:00" e outra das "14:00" às "18:00"
        Then ambas as jornadas são salvas com sucesso

    Scenario: Impedir definicao de jornada que sobrepoe uma ja existente
        Given que "Raquel Neves" já tem uma jornada definida das "08:00" às "12:00" no sábado
        When eu tento definir uma nova jornada das "11:00" às "15:00" no mesmo dia
        Then o sistema rejeita a operação
        And exibe a mensagem: "Esta jornada de trabalho conflita com uma já existente."

# ------------------------- Profissional deve ter apenas um nível de senioridade por vez -------------------------
    Scenario: Alterar o nivel de senioridade de um profissional
        Given que o profissional "Ricardo Gomes" tem o nível de senioridade "Júnior"
        When eu altero seu nível para "Pleno"
        Then a alteração é salva com sucesso
        And o nível de senioridade de "Ricardo Gomes" é "Pleno"

    Scenario: Garantir que o nivel de senioridade antigo foi substituido
        Given que o profissional "Ricardo Gomes" tem o nível de senioridade "Júnior"
        When eu altero seu nível para "Pleno"
        Then o perfil de "Ricardo Gomes" não possui mais o nível "Júnior" associado

# ------------------------- Lista de níveis de senioridade deve ser pré-definida -------------------------
    Scenario: Atribuir um nivel de senioridade que existe na lista pre-definida
        Given que a lista de senioridades válidas é ("Júnior", "Pleno", "Sênior")
        And o profissional "Sara Lima" é "Júnior"
        When eu edito o perfil de "Sara Lima" e seleciono a senioridade "Sênior"
        Then a alteração é salva com sucesso

    Scenario: Impedir atribuicao de um nivel de senioridade que nao existe na lista
        Given que a lista de senioridades válidas é ("Júnior", "Pleno", "Sênior")
        When eu tento atribuir a senioridade "Estagiário" para "Sara Lima"
        Then o sistema rejeita a operação
        And exibe a mensagem: "Nível de senioridade inválido."

# ------------------------- Um profissional pode ter um ou mais selos -------------------------
    Scenario: Adicionar múltiplos selos ao perfil de um profissional
        Given que a profissional "Sofia Andrade" não possui selos
        When eu adiciono os selos "Especialista em Loiras" e "Penteados de Festa"
        Then ambos os selos são associados com sucesso ao seu perfil

    Scenario: Impedir adicao do mesmo selo duas vezes
        Given que a profissional "Sofia Andrade" já possui o selo "Especialista em Loiras"
        When eu tento adicionar o selo "Especialista em Loiras" novamente
        Then o sistema informa que o profissional já possui este selo
        And a lista de selos de "Sofia Andrade" permanece inalterada

# ------------------------- Nível de senioridade pode impactar o preço do serviço -------------------------
    Scenario: Preco do servico reflete o nivel de senioridade do profissional
        Given que o serviço "Corte" custa R$50 com um "Júnior" e R$80 com um "Sênior"
        And "Tiago Barros" é um profissional "Sênior"
        When um cliente seleciona o serviço "Corte" com "Tiago Barros"
        Then o preço exibido para o agendamento é R$80

    Scenario: Preco do servico nao reflete o nivel de um profissional nao qualificado
        Given que o serviço "Coloração" custa R$200
        And "Tiago Barros" é um profissional "Sênior", mas não está habilitado para "Coloração"
        When um cliente tenta agendar "Coloração" com "Tiago Barros"
        Then o sistema não permite a seleção
        And exibe a mensagem "Este profissional não realiza o serviço selecionado."

# ------------------------- Métricas devem ser calculadas apenas para profissionais ativos -------------------------
    Scenario: Profissional ativo é contabilizado nas metricas do mes
        Given que "Tiago Barros" está com status "Ativo" e realizou 10 atendimentos este mês
        When eu gero o relatório de atendimentos de profissionais ativos do mês atual
        Then os 10 atendimentos de "Tiago Barros" são contabilizados

    Scenario: Profissional inativo nao é contabilizado nas metricas do mes
        Given que "Vânia Melo" está com status "Inativo"
        And ela realizou 5 atendimentos neste mês antes de ser desativada
        When eu gero o relatório de atendimentos de profissionais ativos do mês atual
        Then os atendimentos de "Vânia Melo" não são contabilizados neste relatório

# ------------------------- Cálculo da avaliação média -------------------------
    Scenario: Calcular e exibir a avaliacao media de um profissional com múltiplas notas
        Given que a profissional "Vanessa Lima" recebeu duas avaliações: nota 4 e nota 5
        When eu visualizo o perfil público de "Vanessa Lima"
        Then a avaliação média exibida deve ser 4.5

    Scenario: Exibir status neutro para profissional sem avaliacoes
        Given que o profissional "William Borges" acaba de ser cadastrado e não tem avaliações
        When eu visualizo o perfil público de "William Borges"
        Then o campo de avaliação média deve exibir "N/A" ou "Ainda não avaliado"