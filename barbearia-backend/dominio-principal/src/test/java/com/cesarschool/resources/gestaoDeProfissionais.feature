# Feature: Gestão de profissionais

#     Scenario: Atribuir disponibilidade total por padrão
#         Given que eu cadastro um novo profissional chamado "Paulo Reis"
#         When eu visualizo a agenda de "Paulo Reis"
#         Then a disponibilidade deve estar configurada para 8 horas por dia

#     Scenario: Impedir alteração de disponibilidade por um usuário não autorizado
#         Given que sou um usuário não administrador
#         When eu tento configurar a jornada de trabalho de um profissional
#         Then o sistema rejeita a operação

#     Scenario: Atribuir nível de senioridade válido
#         Given que sou um administrador logado
#         When eu cadastro um novo profissional com nível "Pleno"
#         Then o sistema responde com sucesso

#     Scenario: Impedir cadastro com nível de senioridade inválido
#         Given que sou um administrador logado
#         When eu tento cadastrar um novo profissional com nível "Master"
#         Then o sistema rejeita a operação

#     Scenario: Atribuir serviço a profissional
#         Given que existe um profissional chamado "João Pereira"
#         When eu atribuo o serviço "Corte Masculino"
#         Then o sistema responde com sucesso

#     Scenario: Impedir atribuição de serviço inexistente
#         Given que existe um profissional chamado "João Pereira"
#         When eu tento atribuir o serviço "Tratamento VIP"
#         Then o sistema rejeita a operação

#     Scenario: Remover serviço de profissional
#         Given que o profissional "Ana Silva" possui o serviço "Coloração Capilar"
#         When eu removo o serviço "Coloração Capilar"
#         Then o serviço é removido corretamente

#     Scenario: Impedir remoção de serviço com agendamentos ativos
#         Given que o profissional "Ana Silva" possui o serviço "Coloração Capilar" com agendamentos ativos
#         When eu tento remover o serviço "Coloração Capilar"
#         Then o sistema rejeita a operação