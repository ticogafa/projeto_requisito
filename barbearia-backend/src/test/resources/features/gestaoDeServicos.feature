Feature: Gestão de Serviços
  Como administrador
  Quero gerenciar os serviços do sistema
  Para manter as opções disponíveis para os clientes atualizadas


  # ------------------------- Criação de Serviço -------------------------

  Scenario: Criar um novo serviço com sucesso
    Given que sou um administrador autenticado na tela de criação de serviços
    When eu preencho o nome "Corte Masculino", duração "30" e preço "50.00"
    Then o serviço é criado com sucesso
    And o serviço "Corte Masculino" aparece na lista de serviços

  Scenario: Impedir criação de serviço com dados inválidos
    Given que sou um administrador na tela de criação de serviços
    When eu tento criar um serviço com duração "30" e preço "50.00", mas sem nome
    Then o sistema rejeita a operação
    And exibe a mensagem de erro "O campo 'nome' é obrigatório."


  # ------------------------- Nome Único -------------------------

  Scenario: Criar serviço com nome único com sucesso
    Given que não existe um serviço chamado "Barba Desenhada"
    When eu crio um novo serviço com o nome "Barba Desenhada"
    Then o serviço é criado com sucesso

  Scenario: Impedir criação de serviço com nome duplicado
    Given que já existe um serviço chamado "Corte Masculino"
    When eu tento criar um novo serviço com o nome "Corte Masculino"
    Then o sistema rejeita a operação
    And exibe a mensagem de erro "Já existe um serviço com este nome."


  # ------------------------- Atualização de Serviço -------------------------

  Scenario: Atualizar um serviço existente com sucesso
    Given que existe um serviço chamado "Corte Feminino"
    When eu altero o preço do serviço para "70.00"
    Then o sistema salva as alterações
    And o novo preço é exibido corretamente

  Scenario: Impedir atualização de serviço com dados inválidos
    Given que existe um serviço chamado "Corte Feminino"
    When eu tento alterar a duração para um valor negativo
    Then o sistema rejeita a operação
    And exibe a mensagem de erro "A duração deve ser um número positivo."


  # ------------------------- Exclusão de Serviço -------------------------

  Scenario: Excluir um serviço com sucesso
    Given que existe um serviço chamado "Hidratação Capilar"
    When eu removo o serviço da lista
    Then o sistema exclui o serviço com sucesso
    And ele não aparece mais na lista de serviços

  Scenario: Impedir exclusão de serviço inexistente
    Given que não existe um serviço chamado "Coloração Infantil"
    When eu tento excluir este serviço
    Then o sistema rejeita a operação
    And exibe a mensagem "Serviço não encontrado."


  # ------------------------- Listagem de Serviços -------------------------

  Scenario: Listar todos os serviços disponíveis com sucesso
    Given que existem vários serviços cadastrados
    When eu acesso a tela de listagem de serviços
    Then o sistema exibe todos os serviços com seus respectivos preços e durações

  Scenario: Exibir mensagem quando não houver serviços cadastrados
    Given que não existem serviços no sistema
    When eu acesso a tela de listagem
    Then o sistema exibe a mensagem "Nenhum serviço cadastrado."


  # ------------------------- Status do Serviço -------------------------

  Scenario: Ativar e desativar um serviço com sucesso
    Given que existe um serviço chamado "Depilação Completa" ativo
    When eu desativo o serviço
    Then o sistema altera o status para "Inativo"
    And o serviço não aparece nas opções disponíveis para o cliente

  Scenario: Impedir alteração de status de um serviço inexistente
    Given que não existe um serviço chamado "Massagem Relaxante"
    When eu tento desativar o serviço
    Then o sistema rejeita a operação
    And exibe a mensagem "Serviço não encontrado."


  # ------------------------- Validação de Campos -------------------------

  Scenario: Validar campos obrigatórios no cadastro de serviço
    Given que sou um administrador na tela de criação de serviços
    When eu deixo os campos nome, duração e preço em branco
    Then o sistema rejeita o cadastro
    And exibe a mensagem "Todos os campos são obrigatórios."

  Scenario: Validar formato de preço e duração inválidos
    Given que sou um administrador na tela de criação de serviços
    When eu informo a duração "abc" e o preço "-10.00"
    Then o sistema rejeita a operação
    And exibe a mensagem "Formato inválido para duração ou preço."


  # ------------------------- Pesquisa de Serviços -------------------------

  Scenario: Pesquisar serviço existente com sucesso
    Given que existe um serviço chamado "Barba Completa"
    When eu pesquiso pelo termo "Barba"
    Then o sistema retorna o serviço "Barba Completa"

  Scenario: Pesquisar serviço inexistente
    Given que não existe nenhum serviço com o termo "Laser"
    When eu pesquiso por "Laser"
    Then o sistema exibe a mensagem "Nenhum serviço encontrado."


  # ------------------------- Associação com Profissional -------------------------

  Scenario: Associar serviço a um profissional com sucesso
    Given que existe o profissional "João" e o serviço "Corte Masculino"
    When eu associo o serviço ao profissional
    Then o sistema salva a associação com sucesso
    And o serviço aparece no perfil do profissional

  Scenario: Impedir associação de serviço inexistente
    Given que existe o profissional "João" mas não existe o serviço "Banho de Lua"
    When eu tento associar o serviço inexistente
    Then o sistema rejeita a operação
    And exibe a mensagem "Serviço não encontrado."
