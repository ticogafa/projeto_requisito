Feature: Gestão de Estoque e PDV
  Para garantir controle adequado de produtos e vendas no PDV
  Como operador ou administrador do sistema
  Quero cadastrar produtos e registrar vendas corretamente

  Background:
    Given que o sistema está operacional
    And que estou autenticado como operador de PDV

  # Cenários de Cadastro de Produtos
  Scenario: Cadastrar produto com nome único com sucesso (POSITIVO)
    Given que não existe um produto chamado "Shampoo Anticaspa"
    When eu cadastro um novo produto com o nome "Shampoo Anticaspa" e estoque inicial 100
    Then o produto é cadastrado com sucesso

  Scenario: Impedir cadastro de produto com nome duplicado (NEGATIVO)
    Given que já existe um produto chamado "Gel Fixador"
    When eu tento cadastrar um novo produto com o nome "Gel Fixador"
    Then o sistema rejeita a operação

  # Cenários de Atualização de Estoque
  Scenario: Atualizar estoque com quantidade válida (POSITIVO)
    Given que existe um produto "Pomada Modeladora" com estoque 25
    When eu adiciono 15 unidades ao estoque
    Then o estoque atual do produto "Pomada Modeladora" passa a ser 40

  Scenario: Impedir atualização de estoque com valor negativo (NEGATIVO)
    Given que existe um produto "Cera Capilar" com estoque 10
    When eu tento reduzir o estoque em -5 unidades diretamente
    Then o sistema rejeita a operação

  # Cenários de Venda PDV
  Scenario: Registrar venda PDV com produto reduzindo estoque (sucesso)
    Given que existe um produto "Gel Fixador" com estoque 50
    When eu envio a venda de 2 produtos "Gel Fixador" para registro
    Then o sistema responde sucesso
    And o estoque atual do produto "Gel Fixador" passa a ser 48

  Scenario: Impedir venda PDV com estoque insuficiente (falha)
    Given que existe um produto "Pomada Forte" com estoque 2
    When envio uma venda PDV com 5 unidades do produto "Pomada Forte"
    Then o sistema rejeita a operação

  # Cenários de Status de Produto
  Scenario: Desativar produto por indisponibilidade com sucesso (POSITIVO)
    Given que existe um produto "Óleo Capilar" ativo
    When eu desativo o produto por motivo de "descontinuado pelo fornecedor"
    Then o produto aparece como "Inativo" na lista de vendas do PDV

  Scenario: Impedir venda de produto inativo (NEGATIVO)
    Given que o produto "Óleo Capilar" está inativo por "descontinuado pelo fornecedor"
    When o operador tenta registrar uma venda do produto "Óleo Capilar"
    Then o sistema rejeita a operação

  # Cenários de Alerta de Estoque
  Scenario: Gerar alerta quando estoque atinge nível mínimo (POSITIVO)
    Given que existe um produto "Condicionador" com estoque mínimo configurado para 10 unidades
    When o estoque do produto "Condicionador" chega a 10 unidades após uma venda
    Then o sistema gera um alerta de "Estoque baixo"

  Scenario: Impedir definição de estoque mínimo inválido (NEGATIVO)
    Given que existe um produto "Máscara Hidratante"
    When eu tento configurar o estoque mínimo para um valor negativo
    Then o sistema rejeita a operação

  # Cenários de Relatório
  Scenario: Gerar relatório de produtos com estoque baixo (POSITIVO)
    Given que existem produtos com estoque abaixo do mínimo configurado
    When eu solicito o relatório de estoque baixo
    Then o sistema exibe a lista de produtos com estoque insuficiente

  Scenario: Validar histórico de movimentação de estoque (POSITIVO)
    Given que existe um produto "Spray Fixador" com histórico de movimentações
    When eu consulto o histórico de movimentação do produto
    Then o sistema exibe todas as entradas, saídas e vendas registradas