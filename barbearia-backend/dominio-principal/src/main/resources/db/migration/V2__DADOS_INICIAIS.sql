-- ========================================
-- V2: Dados iniciais para testes
-- ========================================

-- Produtos iniciais
INSERT INTO PRODUTO (NOME, ESTOQUE, PRECO, ESTOQUE_MINIMO) VALUES
('Shampoo Anticaspa', 100, 29.90, 20),
('Gel Fixador', 50, 15.50, 10),
('Pomada Modeladora', 25, 45.00, 5),
('Pomada Forte', 30, 50.00, 10),
('Cera Modeladora', 40, 35.00, 10),
('Óleo para Barba', 60, 38.50, 15);

-- Movimentações iniciais (estoque inicial dos produtos)
INSERT INTO MOVIMENTACAO_ESTOQUE 
(PRODUTO_ID, NOME_PRODUTO, TIPO, QUANTIDADE, ESTOQUE_ANTERIOR, ESTOQUE_ATUAL, DATA_HORA, OBSERVACAO, USUARIO_RESPONSAVEL) 
SELECT 
    ID, 
    NOME, 
    'ESTOQUE_INICIAL', 
    ESTOQUE, 
    0, 
    ESTOQUE, 
    CURRENT_TIMESTAMP,
    'Estoque inicial do produto',
    'SISTEMA'
FROM PRODUTO;
