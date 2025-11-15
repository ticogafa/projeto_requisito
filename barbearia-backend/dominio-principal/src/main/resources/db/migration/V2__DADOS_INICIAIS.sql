-- active: 1762782988513@@127.0.0.1@3306@barbearia_db
-- ========================================
-- v2: dados iniciais para testes
-- ========================================

-- produtos iniciais
insert into produto (nome, estoque, preco, estoque_minimo) values
('shampoo anticaspa', 100, 29.90, 20),
('gel fixador', 50, 15.50, 10),
('pomada modeladora', 25, 45.00, 5),
('pomada forte', 30, 50.00, 10),
('cera modeladora', 40, 35.00, 10),
('óleo para barba', 60, 38.50, 15);

-- movimentações iniciais (estoque inicial dos produtos)
insert into movimentacao_estoque 
(produto_id, nome_produto, tipo, quantidade, estoque_anterior, estoque_atual, data_hora, observacao, usuario_responsavel) 
select 
    id, 
    nome, 
    'estoque_inicial', 
    estoque, 
    0, 
    estoque, 
    current_timestamp,
    'estoque inicial do produto',
    'sistema'
from produto;
