Feature: Gestão de profissionais

    # ------------------------- Cadastro de profissionais -------------------------
        Scenario: Cadastrar profissional com sucesso
            Given que sou um administrador logado
            When eu preencho o formulário com nome "Carlos Andrade" e email "carlos.a@email.com"
            Then o profissional é cadastrado com sucesso
            And o profissional aparece na lista de profissionais

        Scenario: Impedir cadastro de profissional sem nome
            Given que sou um administrador logado
            When eu tento cadastrar um profissional apenas com o email "carlos.a@email.com"
            Then o sistema rejeita a operação
            And exibe a mensagem de erro "O campo 'nome' é obrigatório."

    # ------------------------- Validação de e-mail duplicado -------------------------
        Scenario: Cadastrar profissional com e-mail único
            Given que já existe um profissional com o email "ana.s@email.com"
            When eu cadastro um novo profissional com nome "Beatriz Costa" e email "beatriz.c@email.com"
            Then o profissional "Beatriz Costa" é cadastrado com sucesso

        Scenario: Impedir cadastro com e-mail duplicado
            Given que já existe um profissional com o email "ana.s@email.com"
            When eu tento cadastrar um novo profissional com o mesmo email
            Then o sistema rejeita a operação
            And exibe a mensagem de erro "Este e-mail já está em uso."

    # ------------------------- Status padrão do profissional -------------------------
        Scenario: Verificar status padrão como Ativo
            Given que eu cadastro um novo profissional chamado "Daniel Farias"
            When eu visualizo os detalhes do perfil de "Daniel Farias"
            Then o status do profissional deve ser "Ativo"

        Scenario: Garantir que status não seja Inativo por padrão
            Given que eu cadastro um novo profissional chamado "Daniel Farias"
            When eu visualizo os detalhes do perfil de "Daniel Farias"
            Then o status do profissional não deve ser "Inativo" ou nulo

    # ------------------------- Criação automática de agenda -------------------------
        Scenario: Criar agenda ao cadastrar novo profissional
            Given que eu cadastro um novo profissional chamado "Eduardo Lima"
            When o sistema confirma o cadastro
            Then uma agenda vazia deve ser associada ao perfil do profissional

        Scenario: Impedir criação de agenda se cadastro falhar
            Given que tento cadastrar um profissional com e-mail duplicado
            When o sistema rejeita o cadastro
            Then nenhuma nova agenda deve ser criada

    # ------------------------- Nível de senioridade padrão -------------------------
        Scenario: Definir nível de senioridade como Júnior
            Given que eu cadastro um novo profissional chamado "Fernanda Alves"
            When eu visualizo o perfil de "Fernanda Alves"
            Then o nível de senioridade deve ser "Júnior"

        Scenario: Impedir cadastro com nível de senioridade inválido
            Given que sou um administrador logado
            When eu tento cadastrar um novo profissional com nível "Master"
            Then o sistema rejeita a operação
            And exibe a mensagem "Nível de senioridade inválido."

    # ------------------------- Atualização de informações -------------------------
        Scenario: Atualizar nome de profissional existente
            Given que existe um profissional chamado "Gabriel Lima"
            When eu altero o nome para "Gabriel L."
            Then o sistema atualiza as informações com sucesso

        Scenario: Impedir atualização com dados inválidos
            Given que existe um profissional chamado "Gabriel Lima"
            When eu tento atualizar o nome para vazio
            Then o sistema rejeita a operação
            And exibe a mensagem "O campo 'nome' é obrigatório."

    # ------------------------- Alteração de status -------------------------
        Scenario: Alterar status de Ativo para Inativo
            Given que existe um profissional ativo chamado "Helena Prado"
            When eu altero o status para "Inativo"
            Then o sistema atualiza o status corretamente

        Scenario: Impedir alteração de status inválido
            Given que existe um profissional chamado "Helena Prado"
            When eu tento alterar o status para "Suspenso"
            Then o sistema rejeita a operação
            And exibe a mensagem "Status inválido."

    # ------------------------- Exclusão de profissionais -------------------------
        Scenario: Excluir profissional inativo
            Given que existe um profissional chamado "Isabela Souza" com status "Inativo"
            When eu excluo o profissional
            Then o sistema remove o profissional com sucesso

        Scenario: Impedir exclusão de profissional ativo
            Given que existe um profissional chamado "Thiago Santos" com status "Ativo"
            When eu tento excluir o profissional
            Then o sistema rejeita a operação
            And exibe a mensagem "Não é possível excluir um profissional ativo."

    # ------------------------- Listagem e busca -------------------------
        Scenario: Buscar profissional existente pelo nome
            Given que existem profissionais cadastrados no sistema
            When eu busco por "Ana"
            Then o sistema retorna a lista contendo "Ana Silva"

        Scenario: Buscar profissional inexistente
            Given que existem profissionais cadastrados no sistema
            When eu busco por "Lucas"
            Then o sistema não retorna resultados
            And exibe a mensagem "Nenhum profissional encontrado."

    # ------------------------- Ordenação e filtragem -------------------------
        Scenario: Filtrar profissionais ativos e ordenar por nome
            Given que existem profissionais com diferentes status
            When eu filtro por "Ativo" e ordeno por "Nome A-Z"
            Then o sistema exibe a lista corretamente filtrada e ordenada

        Scenario: Impedir filtro inválido
            Given que existem profissionais cadastrados
            When eu aplico um filtro inexistente "Disponível"
            Then o sistema rejeita a operação
            And exibe a mensagem "Filtro inválido."

    # ------------------------- Atribuição de serviços -------------------------
        Scenario: Atribuir serviço a profissional
            Given que existe um profissional chamado "João Pereira"
            When eu atribuo o serviço "Corte Masculino"
            Then o serviço é vinculado corretamente ao profissional

        Scenario: Impedir atribuição de serviço inexistente
            Given que existe um profissional chamado "João Pereira"
            When eu tento atribuir o serviço "Tratamento VIP"
            Then o sistema rejeita a operação
            And exibe a mensagem "Serviço não encontrado."

    # ------------------------- Remoção de serviços -------------------------
        Scenario: Remover serviço de profissional
            Given que o profissional "Ana Silva" possui o serviço "Coloração Capilar"
            When eu removo o serviço
            Then o serviço é removido corretamente

        Scenario: Impedir remoção de serviço não vinculado
            Given que o profissional "Ana Silva" não possui o serviço "Penteado"
            When eu tento remover o serviço
            Then o sistema rejeita a operaçãoa
            And exibe a mensagem "Serviço não está vinculado ao profissional."