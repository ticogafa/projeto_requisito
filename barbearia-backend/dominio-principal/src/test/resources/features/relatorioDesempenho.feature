# language: pt

Funcionalidade: Relatório de Desempenho do Profissional

  Como gerente da barbearia,
  Eu quero gerar o relatório diário de desempenho de um profissional,
  Para saber tempo de serviço, receita, número de atendimentos e média de avaliação,

  Cenário: Tempo e receita de um único atendimento concluído
    Dado que existe um profissional com id 1
    E que o relatório é para o dia "2025-10-12"
    E foi concluído um atendimento de "2025-10-12 09:00" até "2025-10-12 09:45" no valor de 50,0
    Quando eu gerar o relatório de desempenho
    Então o tempo de serviço deve ser 45,0 minutos
    E a receita gerada deve ser 50,0
    E o número de atendimentos deve ser 1
    E a avaliação média do funcionário deve ser 0,0

  Cenário: Soma de múltiplos atendimentos concluídos
    Dado que existe um profissional com id 1
    E que o relatório é para o dia "2025-10-12"
    E foi concluído um atendimento de "2025-10-12 10:00" até "2025-10-12 10:30" no valor de 35,0
    E foi concluído um atendimento de "2025-10-12 11:00" até "2025-10-12 11:20" no valor de 25,0
    Quando eu gerar o relatório de desempenho
    Então o tempo de serviço deve ser 50,0 minutos
    E a receita gerada deve ser 60,0
    E o número de atendimentos deve ser 2
    E a avaliação média do funcionário deve ser 0,0

  Cenário: Atendimentos em andamento não contam para o relatório
    Dado que existe um profissional com id 1
    E que o relatório é para o dia "2025-10-12"
    E existe um atendimento em andamento iniciado às "2025-10-12 12:00" no valor de 40,0
    Quando eu gerar o relatório de desempenho
    Então o tempo de serviço deve ser 0,0 minutos
    E a receita gerada deve ser 0,0
    E o número de atendimentos deve ser 0
    E a avaliação média do funcionário deve ser 0,0

  Cenário: Execuções fora do dia não impactam o relatório
    Dado que existe um profissional com id 1
    E que o relatório é para o dia "2025-10-12"
    E foi concluído um atendimento de "2025-10-11 17:00" até "2025-10-11 17:30" no valor de 30,0
    E foi concluído um atendimento de "2025-10-13 09:00" até "2025-10-13 09:20" no valor de 20,0
    Quando eu gerar o relatório de desempenho
    Então o tempo de serviço deve ser 0,0 minutos
    E a receita gerada deve ser 0,0
    E o número de atendimentos deve ser 0
    E a avaliação média do funcionário deve ser 0,0

  Cenário: Cálculo da média de avaliações no dia
    Dado que existe um profissional com id 1
    E que o relatório é para o dia "2025-10-12"
    E foram registradas avaliações "4, 5, 3"
    Quando eu gerar o relatório de desempenho
    Então o tempo de serviço deve ser 0,0 minutos
    E a receita gerada deve ser 0,0
    E o número de atendimentos deve ser 0
    E a avaliação média do funcionário deve ser 4,0

  Cenário: Sem atendimentos e sem avaliações
    Dado que existe um profissional com id 2
    E que o relatório é para o dia "2025-10-13"
    Quando eu gerar o relatório de desempenho
    Então o tempo de serviço deve ser 0,0 minutos
    E a receita gerada deve ser 0,0
    E o número de atendimentos deve ser 0
    E a avaliação média do funcionário deve ser 0,0