Feature: Gestão de Profissionais 

    Scenario: Atribuir disponibilidade total por padrão
        Given que eu cadastro um novo profissional chamado "Paulo Reis"
        When eu visualizo a agenda de "Paulo Reis"
        Then a disponibilidade deve estar configurada para 8 horas por dia

    Scenario: Atribuir nível de senioridade válido
        Given que sou um administrador logado
        When eu cadastro um novo profissional com nível "Pleno"
        Then o sistema responde com sucesso

    Scenario: Impedir cadastro com nível de senioridade inválido
        Given que sou um administrador logado
        When eu tento cadastrar um novo profissional com nível "Master"
        Then o sistema vai rejeitar a operação

    Scenario: Impedir alteração de disponibilidade por um usuário não autorizado
        Given que sou um usuário não administrador
        When eu tento configurar a jornada de trabalho de um profissional
        Then o sistema vai rejeitar a operação