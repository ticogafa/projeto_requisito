#  Feature: Gestão de Serviços
#     Scenario: Criar serviço com nome único com sucesso (POSITIVO)
#         Given que não existe um serviço chamado "Massagem Capilar"
#          When eu crio um novo serviço com o nome "Massagem Capilar"
#          Then o serviço é criado com sucesso

#      Scenario: Impedir criação de serviço com nome duplicado (NEGATIVO)
#          Given que já existe um serviço chamado "Corte Masculino"
#          When eu tento criar um novo serviço com o nome "Corte Masculino"
#          Then o sistema rejeita a operação

#      Scenario: Atualizar um serviço com preço e duração válidos (POSITIVO)
#          Given que existe um serviço chamado "Corte Feminino"
#          When eu altero a duração para "60" e o preço para "70.00"
#          Then o sistema salva as alterações com sucesso

#      Scenario: Impedir atualização de serviço com duração ou preço inválidos (NEGATIVO)
#          Given que existe um serviço chamado "Corte Feminino"
#          When eu tento alterar a duração para um valor negativo
#          Then o sistema rejeita a operação

#      Scenario: Desativar um serviço por período de indisponibilidade com sucesso (POSITIVO)
#          Given que existe um serviço chamado "Maquiagem" ativo
#          When eu desativo o serviço por motivo de "falta de insumos"
#          Then o serviço aparece como "Inativo" na lista de opções para agendamento

#      Scenario: Impedir que o cliente visualize um serviço inativo (NEGATIVO)
#          Given que o serviço "Maquiagem" está inativo por "falta de insumos"
#          When o cliente acessa as opções de agendamento
#          Then o sistema não exibe o serviço "Maquiagem" na lista

#      Scenario: Associar serviço a um profissional qualificado com sucesso (POSITIVO)
#          Given que existe o profissional "João" qualificado para "Corte Masculino"
#          When eu associo o serviço "Corte Masculino" ao profissional "João"
#          Then o sistema salva a associação com sucesso

#      Scenario: Impedir associação de serviço a profissional não qualificado (NEGATIVO)
#          Given que existe o profissional "João" sem qualificação para "Manicure"
#          When eu tento associar o serviço "Manicure" ao profissional "João"
#          Then o sistema rejeita a operação

#      Scenario: Definir intervalo de limpeza após o serviço com sucesso (POSITIVO)
#          Given que existe um serviço chamado "Corte Feminino"
#          When eu defino um intervalo de limpeza de 10 minutos após o serviço
#          Then o sistema salva o intervalo corretamente

#      Scenario: Impedir que o agendamento ignore o intervalo de limpeza (NEGATIVO)
#          Given que o serviço "Corte Feminino" tem um intervalo de limpeza de 10 minutos
#          When um novo agendamento tenta começar imediatamente após o fim do "Corte Feminino"
#          Then o sistema rejeita a operação e exige o tempo de intervalo

#      Scenario: Definir um serviço como add-on de outro com sucesso (POSITIVO)
#          Given que existe o serviço "Corte" e o serviço "Hidratação"
#          When eu configuro "Hidratação" como um add-on de "Corte"
#          Then o sistema salva a dependência corretamente

#      Scenario: Impedir agendamento de add-on sem o serviço principal (NEGATIVO)
#          Given que o serviço "Hidratação" é um add-on de "Corte"
#          When o cliente tenta agendar apenas o serviço "Hidratação"
#          Then o sistema rejeita a operação