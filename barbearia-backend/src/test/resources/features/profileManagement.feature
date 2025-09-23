# Feature: Cadastro de Profissionais 
#   Para garantir que a base de dados de profissionais esteja sempre atualizada e correta
#   Como um administrador do sistema
#   Quero cadastrar, editar e desativar profissionais

#   Context:
#     Given que estou logado como administrador
#     And que o sistema está funcionando e com acesso ao banco de dados

#   # ------------------------- Cadastrar novo profissional -------------------------
#   Scenario: Cadastrar novo profissional (sucesso)
#     Given que eu tenho as informações de um novo profissional (nome, contato, etc.)
#     When eu solicito o cadastro do profissional
#     Then o sistema responde sucesso
#     And o novo profissional é adicionado à lista de profissionais ativos

#   Scenario: Impedir cadastro com informações incompletas (falha)
#     Given que eu estou tentando cadastrar um novo profissional
#     And que eu não informo o nome obrigatório
#     When eu solicito o cadastro do profissional
#     Then o sistema rejeita a operação
#     And exibe a mensagem: "O campo 'nome' é obrigatório."

#   Scenario: Impedir cadastro com CPF duplicado (falha)
#     Given que já existe um profissional com CPF "123.456.789-00"
#     When eu solicito o cadastro de um novo profissional com o mesmo CPF
#     Then o sistema rejeita a operação
#     And retorna status 409
#     And exibe a mensagem: "CPF já cadastrado."

#   Scenario: Impedir cadastro com email inválido (falha)
#     Given que eu tento cadastrar um profissional com email "usuario@@dominio"
#     When eu solicito o cadastro do profissional
#     Then o sistema rejeita a operação
#     And retorna status 400
#     And exibe a mensagem: "Formato de e-mail inválido."

#   # ------------------------- Editar perfil do profissional -----------------------
#   Scenario: Editar o perfil do profissional (sucesso)
#     Given que eu tenho informações desatualizadas do meu profissional (Foto de perfil, nome, etc.)
#     When ele solicita mudança no seu perfil
#     Then o sistema responde sucesso
#     And o profissional consegue assim editar o proprio perfil para deixar-lo sempre em dia

#   Scenario: Impedir edição para exclusão de informações importantes
#     Given que eu tenho um perfil de profissional completo 
#     And quero tirar alguma informações que é obrigatória
#     When ele solicitar mudança no seu perfil
#     Then o sistema rejeita a mudança
#     And exibe mensagem: "O campo 'CPF' não pode ser alterado."

#     Scenario: Impedir alteração de campo sensível por usuário sem permissão (falha)
#     Given que estou autenticado como usuário com papel "USER" (não-admin)
#     When eu solicito alteração do campo "salario" do profissional
#     Then o sistema rejeita a operação
#     And retorna status 403
#     And exibe a mensagem: "Permissão negada para alterar 'salario'."

#   Scenario: Upload de foto inválida (falha)
#     Given que tento enviar uma foto de perfil com tamanho maior que 5MB ou formato .exe
#     When eu envio a requisição de upload de foto
#     Then o sistema rejeita a operação
#     And retorna status 400
#     And exibe a mensagem: "Arquivo excede 5MB ou formato não suportado."

#   # ------------------------- Desativar perfil do profissional -----------------------
#   Scenario: Desativar o perfil do profissional (sucesso)
#     Given que ocorreu uma remoção de um profissional de minha equipe
#     And eu quero desativar o perfil dele para impossibilitar o agendamento de atendimentos em seu nome
#     When eu solicitar desativação de seu perfil
#     Then o sistema responde com sucesso
#     And o profissional ficará indisponivel para marcar atendimentos e sua agenda prévia continuará no histórico

#   Scenario: Desativar o perfil do profissional (falha)
#     Given que existe um profissional com id "123" no sistema
#     And que esse profissional tem pelo menos um agendamento futuro confirmado
#     When eu solicitar desativação do perfil do profissional com id "123"
#     Then o sistema rejeita a operação
#     And o sistema retorna o status 400
#     And exibe a mensagem: "Não é possível desativar: existem agendamentos futuros."

#   Scenario: Desativar perfil (falha - id inexistente)
#     Given que não existe um profissional com id "999"
#     When eu solicitar desativação do perfil do profissional com id "999"
#     Then o sistema rejeita a operação
#     And retorna status 404
#     And exibe a mensagem: "Profissional não encontrado."

#   Scenario: Desativar perfil (falha - permissão insuficiente)
#     Given que estou logado como usuário comum sem permissão administrativa
#     When eu solicitar desativação do perfil do profissional com id "123"
#     Then o sistema rejeita a operação
#     And retorna status 403
#     And exibe a mensagem: "Permissão negada."

#   Scenario: Desativar perfil (resiliência - erro de infra)
#     Given que o banco de dados está temporariamente indisponível
#     When eu solicitar desativação do perfil do profissional com id "123"
#     Then o sistema responde com erro 500
#     And nenhum estado parcial é gravado (transação revertida)

#   Scenario: Reativar profissional (sucesso)
#     Given que existe um profissional com id "555" com status "INATIVO"
#     When eu solicitar reativação do profissional com id "555"
#     Then o sistema responde com sucesso
#     And o status do profissional passa para "ATIVO"
#     And o sistema registra quem realizou a reativação e quando

#   Scenario: DELETE idempotente / doble solicitação (comportamento definido)
#     Given que existe um profissional com id "888"
#     When eu solicitar desativação de 888 duas vezes consecutivas
#     Then a primeira requisição retorna 200 ou 204 (segundo política)
#     And a segunda retorna 404 ou 200 (segundo política definida)
#     And o comportamento escolhido está documentado na API

#   Scenario: Concorrência ao desativar (resiliência)
#     Given que existe um profissional com id "777" sem agendamentos futuros
#     When dois usuários enviam simultaneamente DELETE /api/profissionais/777
#     Then uma requisição retorna 200 OK
#     And a outra retorna 409 Conflict ou 404 Not Found (comportamento definido)
      