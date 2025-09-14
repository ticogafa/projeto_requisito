-- Active: 1755607744798@@127.0.0.1@3307@aerocenter
-- =========================================================================
-- SCRIPT DE POVOAMENTO PARA SISTEMA DE BARBEARIA/AEROCENTER
-- Data: 14/09/2025
-- =========================================================================

-- Usar a base de dados aerocenter
USE aerocenter;

-- Habilitar inserção de UUIDs explícitos
SET @@session.sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

-- ===================== VARIÁVEIS PARA PKs ===================

-- Limpar dados existentes (opcional - descomente se necessário)
-- DELETE FROM pagamento;
-- DELETE FROM item_venda;
-- DELETE FROM venda;
-- DELETE FROM voucher;
-- DELETE FROM agendamento;
-- DELETE FROM horario_trabalho;
-- DELETE FROM produto;
-- DELETE FROM cliente;
-- DELETE FROM profissional;
-- DELETE FROM servico;

-- Variáveis para IDs de Serviços
SET @servico_corte_masc = '550e8400-e29b-41d4-a716-446655440001';
SET @servico_barba = '550e8400-e29b-41d4-a716-446655440002';
SET @servico_corte_barba = '550e8400-e29b-41d4-a716-446655440003';
SET @servico_corte_infantil = '550e8400-e29b-41d4-a716-446655440004';
SET @servico_corte_degrade = '550e8400-e29b-41d4-a716-446655440005';
SET @servico_sobrancelha = '550e8400-e29b-41d4-a716-446655440006';
SET @servico_limpeza_pele = '550e8400-e29b-41d4-a716-446655440007';
SET @servico_corte_social = '550e8400-e29b-41d4-a716-446655440008';

-- Variáveis para IDs de Profissionais
SET @prof_carlos = '660e8400-e29b-41d4-a716-446655440001';
SET @prof_joao = '660e8400-e29b-41d4-a716-446655440002';
SET @prof_lucas = '660e8400-e29b-41d4-a716-446655440003';
SET @prof_roberto = '660e8400-e29b-41d4-a716-446655440004';
SET @prof_andre = '660e8400-e29b-41d4-a716-446655440005';

-- Variáveis para IDs de Clientes
SET @cliente_maria = '880e8400-e29b-41d4-a716-446655440001';
SET @cliente_jose = '880e8400-e29b-41d4-a716-446655440002';
SET @cliente_ana = '880e8400-e29b-41d4-a716-446655440003';
SET @cliente_pedro = '880e8400-e29b-41d4-a716-446655440004';
SET @cliente_carla = '880e8400-e29b-41d4-a716-446655440005';
SET @cliente_paulo = '880e8400-e29b-41d4-a716-446655440006';
SET @cliente_fernanda = '880e8400-e29b-41d4-a716-446655440007';
SET @cliente_ricardo = '880e8400-e29b-41d4-a716-446655440008';
SET @cliente_juliana = '880e8400-e29b-41d4-a716-446655440009';
SET @cliente_marcos = '880e8400-e29b-41d4-a716-446655440010';

-- ===================== SERVIÇOS ===================

INSERT IGNORE INTO servico (id, nome, preco, duracao_minutos) VALUES
(@servico_corte_masc, 'Corte de Cabelo Masculino', 35.00, 30),
(@servico_barba, 'Barba', 25.00, 20),
(@servico_corte_barba, 'Corte + Barba', 55.00, 45),
(@servico_corte_infantil, 'Corte Infantil', 25.00, 25),
(@servico_corte_degrade, 'Corte Degradê', 40.00, 35),
(@servico_sobrancelha, 'Sobrancelha', 15.00, 15),
(@servico_limpeza_pele, 'Limpeza de Pele', 60.00, 60),
(@servico_corte_social, 'Corte Social', 30.00, 25);

-- ===================== PROFISSIONAIS ===================

INSERT IGNORE INTO profissional (id, nome, email, telefone) VALUES
(@prof_carlos, 'Carlos Silva', 'carlos.silva@aerocenter.com', '11987654321'),
(@prof_joao, 'João Mendes', 'joao.mendes@aerocenter.com', '11987654322'),
(@prof_lucas, 'Lucas Santos', 'lucas.santos@aerocenter.com', '11987654323'),
(@prof_roberto, 'Roberto Costa', 'roberto.costa@aerocenter.com', '11987654324'),
(@prof_andre, 'André Oliveira', 'andre.oliveira@aerocenter.com', '11987654325');

-- ===================== HORÁRIOS DE TRABALHO ===================

-- Carlos Silva - Segunda a Sábado
INSERT IGNORE INTO horario_trabalho (id, profissional_id, dia_semana, hora_inicio, hora_fim, inicio_pausa, fim_pausa) VALUES
(UUID(), @prof_carlos, 'SEGUNDA', '08:00:00', '18:00:00', '12:00:00', '13:00:00'),
(UUID(), @prof_carlos, 'TERCA', '08:00:00', '18:00:00', '12:00:00', '13:00:00'),
(UUID(), @prof_carlos, 'QUARTA', '08:00:00', '18:00:00', '12:00:00', '13:00:00'),
(UUID(), @prof_carlos, 'QUINTA', '08:00:00', '18:00:00', '12:00:00', '13:00:00'),
(UUID(), @prof_carlos, 'SEXTA', '08:00:00', '18:00:00', '12:00:00', '13:00:00'),
(UUID(), @prof_carlos, 'SABADO', '08:00:00', '16:00:00', '12:00:00', '13:00:00');

-- João Mendes - Terça a Sábado
INSERT IGNORE INTO horario_trabalho (id, profissional_id, dia_semana, hora_inicio, hora_fim, inicio_pausa, fim_pausa) VALUES
(UUID(), @prof_joao, 'TERCA', '09:00:00', '19:00:00', '12:30:00', '13:30:00'),
(UUID(), @prof_joao, 'QUARTA', '09:00:00', '19:00:00', '12:30:00', '13:30:00'),
(UUID(), @prof_joao, 'QUINTA', '09:00:00', '19:00:00', '12:30:00', '13:30:00'),
(UUID(), @prof_joao, 'SEXTA', '09:00:00', '19:00:00', '12:30:00', '13:30:00'),
(UUID(), @prof_joao, 'SABADO', '09:00:00', '17:00:00', '12:30:00', '13:30:00');

-- Lucas Santos - Segunda a Sexta
INSERT IGNORE INTO horario_trabalho (id, profissional_id, dia_semana, hora_inicio, hora_fim, inicio_pausa, fim_pausa) VALUES
(UUID(), @prof_lucas, 'SEGUNDA', '10:00:00', '20:00:00', '13:00:00', '14:00:00'),
(UUID(), @prof_lucas, 'TERCA', '10:00:00', '20:00:00', '13:00:00', '14:00:00'),
(UUID(), @prof_lucas, 'QUARTA', '10:00:00', '20:00:00', '13:00:00', '14:00:00'),
(UUID(), @prof_lucas, 'QUINTA', '10:00:00', '20:00:00', '13:00:00', '14:00:00'),
(UUID(), @prof_lucas, 'SEXTA', '10:00:00', '20:00:00', '13:00:00', '14:00:00');

-- Roberto Costa - Quarta a Domingo
INSERT IGNORE INTO horario_trabalho (id, profissional_id, dia_semana, hora_inicio, hora_fim, inicio_pausa, fim_pausa) VALUES
(UUID(), @prof_roberto, 'QUARTA', '08:30:00', '17:30:00', '12:00:00', '13:00:00'),
(UUID(), @prof_roberto, 'QUINTA', '08:30:00', '17:30:00', '12:00:00', '13:00:00'),
(UUID(), @prof_roberto, 'SEXTA', '08:30:00', '17:30:00', '12:00:00', '13:00:00'),
(UUID(), @prof_roberto, 'SABADO', '08:30:00', '16:30:00', '12:00:00', '13:00:00'),
(UUID(), @prof_roberto, 'DOMINGO', '09:00:00', '15:00:00', NULL, NULL);

-- André Oliveira - Sexta a Terça (fim de semana)
INSERT IGNORE INTO horario_trabalho (id, profissional_id, dia_semana, hora_inicio, hora_fim, inicio_pausa, fim_pausa) VALUES
(UUID(), @prof_andre, 'SEXTA', '14:00:00', '22:00:00', '18:00:00', '19:00:00'),
(UUID(), @prof_andre, 'SABADO', '10:00:00', '20:00:00', '14:00:00', '15:00:00'),
(UUID(), @prof_andre, 'DOMINGO', '10:00:00', '18:00:00', '13:00:00', '14:00:00'),
(UUID(), @prof_andre, 'SEGUNDA', '14:00:00', '22:00:00', '18:00:00', '19:00:00'),
(UUID(), @prof_andre, 'TERCA', '14:00:00', '22:00:00', '18:00:00', '19:00:00');

-- ===================== CLIENTES ===================

INSERT IGNORE INTO cliente (id, nome, email, telefone, pontos_fidelidade) VALUES
(@cliente_maria, 'Maria Silva', 'maria.silva@email.com', '11987654331', 150),
(@cliente_jose, 'José Santos', 'jose.santos@email.com', '11987654332', 80),
(@cliente_ana, 'Ana Costa', 'ana.costa@email.com', '11987654333', 220),
(@cliente_pedro, 'Pedro Oliveira', 'pedro.oliveira@email.com', '11987654334', 45),
(@cliente_carla, 'Carla Mendes', 'carla.mendes@email.com', '11987654335', 320),
(@cliente_paulo, 'Paulo Rodrigues', 'paulo.rodrigues@email.com', '11987654336', 0),
(@cliente_fernanda, 'Fernanda Lima', 'fernanda.lima@email.com', '11987654337', 175),
(@cliente_ricardo, 'Ricardo Alves', 'ricardo.alves@email.com', '11987654338', 90),
(@cliente_juliana, 'Juliana Ferreira', 'juliana.ferreira@email.com', '11987654339', 280),
(@cliente_marcos, 'Marcos Pereira', 'marcos.pereira@email.com', '11987654340', 125);

-- ===================== VOUCHERS ===================

-- Vouchers para clientes com mais de 100 pontos
INSERT IGNORE INTO voucher (id, cliente_id, codigo, valor_desconto, status, expira_em) VALUES
(UUID(), @cliente_maria, 'FIDELIDADE10-001', 10.00, 'GERADO', '2025-12-31 23:59:59'),
(UUID(), @cliente_ana, 'FIDELIDADE20-002', 20.00, 'GERADO', '2025-12-31 23:59:59'),
(UUID(), @cliente_carla, 'FIDELIDADE30-003', 30.00, 'UTILIZADO', '2025-12-31 23:59:59'),
(UUID(), @cliente_fernanda, 'FIDELIDADE10-004', 10.00, 'GERADO', '2025-12-31 23:59:59'),
(UUID(), @cliente_juliana, 'FIDELIDADE20-005', 20.00, 'GERADO', '2025-12-31 23:59:59'),
(UUID(), @cliente_marcos, 'FIDELIDADE10-006', 10.00, 'GERADO', '2025-12-31 23:59:59');

-- ===================== AGENDAMENTOS ===================

-- Agendamentos para os próximos dias (alguns confirmados, outros pendentes)
INSERT IGNORE INTO agendamento (id, cliente_id, profissional_id, servico_id, data_hora, status, observacoes) VALUES
-- Agendamentos para hoje
(UUID(), @cliente_maria, @prof_carlos, @servico_corte_masc, '2025-09-14 09:00:00', 'CONFIRMADO', 'Cliente preferencial'),
(UUID(), @cliente_jose, @prof_carlos, @servico_corte_barba, '2025-09-14 10:00:00', 'CONFIRMADO', NULL),
(UUID(), @cliente_ana, @prof_joao, @servico_barba, '2025-09-14 14:00:00', 'PENDENTE', 'Primeira vez na barbearia'),

-- Agendamentos para amanhã
(UUID(), @cliente_pedro, @prof_lucas, @servico_corte_degrade, '2025-09-15 11:00:00', 'CONFIRMADO', NULL),
(UUID(), @cliente_carla, @prof_lucas, @servico_limpeza_pele, '2025-09-15 15:00:00', 'CONFIRMADO', 'Limpeza completa'),
(UUID(), @cliente_paulo, @prof_roberto, @servico_corte_infantil, '2025-09-15 16:00:00', 'PENDENTE', 'Criança de 8 anos'),

-- Agendamentos para o fim de semana
(UUID(), @cliente_fernanda, @prof_andre, @servico_corte_barba, '2025-09-20 15:00:00', 'CONFIRMADO', NULL),
(UUID(), @cliente_ricardo, @prof_andre, @servico_corte_masc, '2025-09-21 11:00:00', 'CONFIRMADO', NULL),

-- Agendamentos históricos (concluídos)
(UUID(), @cliente_juliana, @prof_carlos, @servico_corte_barba, '2025-09-10 14:00:00', 'CONCLUIDO', NULL),
(UUID(), @cliente_marcos, @prof_joao, @servico_corte_masc, '2025-09-11 16:00:00', 'CONCLUIDO', 'Cliente satisfeito');

-- ===================== PRODUTOS ===================

INSERT IGNORE INTO produto (id, nome, preco, estoque) VALUES
(UUID(), 'Shampoo Anticaspa', 25.90, 45),
(UUID(), 'Condicionador Hidratante', 22.50, 38),
(UUID(), 'Pomada Modeladora', 35.00, 22),
(UUID(), 'Gel Fixador Forte', 18.90, 55),
(UUID(), 'Cera Para Cabelo', 42.00, 15),
(UUID(), 'Óleo Para Barba', 65.00, 28),
(UUID(), 'Balm Para Barba', 38.50, 32),
(UUID(), 'Loção Pós-Barba', 29.90, 41),
(UUID(), 'Spray Texturizador', 31.00, 8),
(UUID(), 'Água de Colônia', 45.00, 20),
(UUID(), 'Máscara Capilar', 55.00, 12),
(UUID(), 'Pente Profissional', 15.00, 25);

-- ===================== VENDAS ===================

-- Definir variáveis para IDs de vendas
SET @venda1 = UUID();
SET @venda2 = UUID();
SET @venda3 = UUID();
SET @venda4 = UUID();
SET @venda5 = UUID();
SET @venda6 = UUID();

-- Vendas históricas com produtos e serviços
INSERT IGNORE INTO venda (id, cliente_id, data_venda, voucher_id, valor_total, observacoes) VALUES
(@venda1, @cliente_maria, '2025-09-10 14:45:00', NULL, 90.90, 'Venda após atendimento'),
(@venda2, @cliente_jose, '2025-09-11 17:30:00', NULL, 125.50, NULL),
(@venda3, @cliente_carla, '2025-09-12 10:15:00', NULL, 95.00, 'Desconto fidelidade aplicado'),
(@venda4, NULL, '2025-09-13 15:20:00', NULL, 67.00, 'Cliente sem cadastro'),
(@venda5, @cliente_ricardo, '2025-09-13 18:45:00', NULL, 183.90, 'Compra de produtos'),
(@venda6, @cliente_ana, '2025-09-14 09:30:00', NULL, 35.00, 'Só serviço');

-- ===================== ITENS DE VENDA ===================

-- Como não podemos usar referências de produtos específicos (UUIDs dinâmicos),
-- vamos simplificar e usar apenas descrições para os itens de venda

-- Itens da primeira venda
INSERT IGNORE INTO item_venda (id, venda_id, produto_id, descricao, quantidade, preco_unitario, preco_total, tipo) VALUES
(UUID(), @venda1, NULL, 'Shampoo Anticaspa', 1, 25.90, 25.90, 'PRODUTO'),
(UUID(), @venda1, NULL, 'Óleo Para Barba', 1, 65.00, 65.00, 'PRODUTO');

-- Itens da segunda venda
INSERT IGNORE INTO item_venda (id, venda_id, produto_id, descricao, quantidade, preco_unitario, preco_total, tipo) VALUES
(UUID(), @venda2, NULL, 'Pomada Modeladora', 2, 35.00, 70.00, 'PRODUTO'),
(UUID(), @venda2, NULL, 'Água de Colônia', 1, 45.00, 45.00, 'PRODUTO'),
(UUID(), @venda2, NULL, 'Pente Profissional', 1, 15.00, 15.00, 'PRODUTO');

-- Itens da terceira venda (com desconto)
INSERT IGNORE INTO item_venda (id, venda_id, produto_id, descricao, quantidade, preco_unitario, preco_total, tipo) VALUES
(UUID(), @venda3, NULL, 'Cera Para Cabelo', 1, 42.00, 42.00, 'PRODUTO'),
(UUID(), @venda3, NULL, 'Balm Para Barba', 1, 38.50, 38.50, 'PRODUTO'),
(UUID(), @venda3, NULL, 'Condicionador Hidratante', 1, 22.50, 22.50, 'PRODUTO');

-- Itens da quarta venda (cliente sem cadastro)
INSERT IGNORE INTO item_venda (id, venda_id, produto_id, descricao, quantidade, preco_unitario, preco_total, tipo) VALUES
(UUID(), @venda4, NULL, 'Gel Fixador Forte', 2, 18.90, 37.80, 'PRODUTO'),
(UUID(), @venda4, NULL, 'Loção Pós-Barba', 1, 29.90, 29.90, 'PRODUTO');

-- Itens da quinta venda (muitos produtos)
INSERT IGNORE INTO item_venda (id, venda_id, produto_id, descricao, quantidade, preco_unitario, preco_total, tipo) VALUES
(UUID(), @venda5, NULL, 'Óleo Para Barba', 1, 65.00, 65.00, 'PRODUTO'),
(UUID(), @venda5, NULL, 'Máscara Capilar', 1, 55.00, 55.00, 'PRODUTO'),
(UUID(), @venda5, NULL, 'Pomada Modeladora', 1, 35.00, 35.00, 'PRODUTO'),
(UUID(), @venda5, NULL, 'Spray Texturizador', 1, 31.00, 31.00, 'PRODUTO');

-- Itens da sexta venda (só serviços)
INSERT IGNORE INTO item_venda (id, venda_id, produto_id, descricao, quantidade, preco_unitario, preco_total, tipo) VALUES
(UUID(), @venda6, @servico_corte_masc, 'Corte de Cabelo Masculino', 1, 35.00, 35.00, 'SERVICO');

-- ===================== PAGAMENTOS ===================

-- Pagamentos das vendas
INSERT IGNORE INTO pagamento (id, venda_id, valor, metodo, status, data_processamento) VALUES
(UUID(), @venda1, 90.90, 'PIX', 'CONFIRMADO', '2025-09-10 14:45:30'),
(UUID(), @venda2, 125.50, 'CREDITO', 'CONFIRMADO', '2025-09-11 17:31:15'),
(UUID(), @venda3, 95.00, 'DEBITO', 'CONFIRMADO', '2025-09-12 10:16:00'),
(UUID(), @venda4, 67.00, 'DINHEIRO', 'CONFIRMADO', '2025-09-13 15:20:30'),
(UUID(), @venda5, 183.90, 'PIX', 'CONFIRMADO', '2025-09-13 18:46:10'),
(UUID(), @venda6, 35.00, 'DINHEIRO', 'CONFIRMADO', '2025-09-14 09:31:00');

-- =========================================================================
-- CONSULTAS DE VERIFICAÇÃO
-- =========================================================================

-- Verificar profissionais e seus horários
SELECT 
    p.nome AS profissional,
    h.diaSemana,
    h.horaInicio,
    h.horaFim,
    h.inicioPausa,
    h.fimPausa
FROM profissional p
JOIN horarioTrabalho h ON p.id = h.profissionalId
ORDER BY p.nome, 
    CASE h.diaSemana 
        WHEN 'SEGUNDA' THEN 1
        WHEN 'TERCA' THEN 2
        WHEN 'QUARTA' THEN 3
        WHEN 'QUINTA' THEN 4
        WHEN 'SEXTA' THEN 5
        WHEN 'SABADO' THEN 6
        WHEN 'DOMINGO' THEN 7
    END;

-- Verificar agendamentos do dia
SELECT 
    a.dataHora,
    c.nome AS cliente,
    p.nome AS profissional,
    s.nome AS servico,
    a.status,
    a.observacoes
FROM agendamento a
JOIN cliente c ON a.clienteId = c.id
JOIN profissional p ON a.profissionalId = p.id
JOIN servico s ON a.servicoId = s.id
WHERE DATE(a.dataHora) = '2025-09-14'
ORDER BY a.dataHora;

-- Verificar estoque baixo (menos de 15 unidades)
SELECT 
    nome,
    estoque,
    preco
FROM produto
WHERE estoque < 15
ORDER BY estoque ASC;

-- Verificar clientes com mais pontos de fidelidade
SELECT 
    nome,
    email,
    pontosFidelidade
FROM cliente
WHERE pontosFidelidade > 100
ORDER BY pontosFidelidade DESC;

-- =========================================================================
-- FIM DO SCRIPT DE POVOAMENTO
-- =========================================================================
