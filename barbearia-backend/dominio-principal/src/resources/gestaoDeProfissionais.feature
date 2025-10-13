Feature: Gestão de profissionais

    Scenario: Definir nível de senioridade como Júnior por padrão
        Given que eu cadastro um novo profissional chamado "Giulia Magalhães"
        When eu visualizo o perfil de "Giulia Magalhães"
        Then o nível de senioridade deve ser "Júnior"

    Scenario: Impedir cadastro com nível de senioridade inválido
        Given que sou um administrador logado
        When eu tento cadastrar um novo profissional com nível "Master"
        Then o sistema rejeita a operação
        And exibe a mensagem "Nível de senioridade inválido."

    Scenario: Criar agenda vazia ao cadastrar novo profissional
        Given que eu cadastro um novo profissional chamado "Vinicius Diniz"
        When o sistema confirma o cadastro
        Then uma agenda vazia deve ser associada ao perfil do profissional

    Scenario: Impedir criação de agenda se cadastro falhar
        Given que tento cadastrar um profissional com e-mail duplicado
        When o sistema rejeita o cadastro
        Then nenhuma nova agenda deve ser criada

    Scenario: Bloquear disponibilidade por período de ausência (férias)
        Given que existe um profissional chamado "Vinicius Diniz"
        When eu registro um período de ausência de 5 dias
        Then o profissional deve aparecer como "Indisponível" na agenda para este período

    Scenario: Atribuir serviço a profissional
        Given que existe um profissional chamado "Giulia Magalhães"
        When eu atribuo o serviço "Corte Masculino"
        Then o serviço é vinculado corretamente ao profissional

    Scenario: Impedir atribuição de serviço inexistente
        Given que existe um profissional chamado "Giulia Magalhães"
        When eu tento atribuir o serviço "Tratamento VIP"
        Then o sistema rejeita a operação
        And exibe a mensagem "Serviço não encontrado."

    Scenario: Remover serviço de profissional
        Given que o profissional "Giulia Magalhães" possui o serviço "Coloração Capilar"
        When eu removo o serviço
        Then o serviço é removido corretamente

    Scenario: Impedir remoção de serviço não vinculado
        Given que o profissional "Giulia Magalhães" não possui o serviço "Penteado"
        When eu tento remover o serviço
        Then o sistema rejeita a operaçãoa
        And exibe a mensagem "Serviço não está vinculado ao profissional."

    Scenario: Filtrar profissionais ativos e ordenar por nome
        Given que existem profissionais com diferentes status
        When eu filtro por "Ativo" e ordeno por "Nome A-Z"
        Then o sistema exibe a lista corretamente filtrada e ordenada

    Scenario: Impedir filtro inválido
        Given que existem profissionais cadastrados
        When eu aplico um filtro inexistente "Disponível"
        Then o sistema rejeita a operação
        And exibe a mensagem "Filtro inválido."

    Scenario: Visualizar o desempenho médio do profissional
        Given que o profissional "Vinicius Diniz" completou 10 atendimentos
        And sua avaliação média atual é 4.5
        When eu visualizo o painel de métricas de "Vinicius Diniz"
        Then o número de atendimentos concluídos deve ser 10
        And a avaliação média exibida deve ser "4.5"