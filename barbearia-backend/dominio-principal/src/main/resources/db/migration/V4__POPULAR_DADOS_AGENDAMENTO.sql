-- ========================================
-- V4: Popular dados para teste de gestão de agendamento
-- Data: 11/12/2025
-- ========================================

-- Limpar dados existentes (se necessário)
DELETE FROM agendamento WHERE id > 0;
DELETE FROM profissional_servico WHERE profissional_id > 0;
DELETE FROM jornada_trabalho WHERE profissional_id > 0;
DELETE FROM servico_oferecido WHERE id > 0;
DELETE FROM profissional WHERE id > 0;
DELETE FROM cliente WHERE id > 0;

-- Resetar auto-increment
ALTER TABLE cliente AUTO_INCREMENT = 1;
ALTER TABLE profissional AUTO_INCREMENT = 1;
ALTER TABLE servico_oferecido AUTO_INCREMENT = 1;
ALTER TABLE agendamento AUTO_INCREMENT = 1;

-- ========================================
-- 1. CLIENTES
-- ========================================
INSERT INTO cliente (nome, cpf, email, telefone, data_nascimento, endereco) VALUES
('João Pereira', '12345678901', 'joao.pereira@email.com', '81987654321', '1990-05-15', 'Rua das Flores, 123, Recife-PE'),
('Maria Silva', '23456789012', 'maria.silva@email.com', '81987654322', '1985-08-20', 'Av. Boa Viagem, 456, Recife-PE'),
('Pedro Santos', '34567890123', 'pedro.santos@email.com', '81987654323', '1995-03-10', 'Rua do Sol, 789, Recife-PE'),
('Ana Costa', '45678901234', 'ana.costa@email.com', '81987654324', '1992-11-25', 'Av. Norte, 321, Recife-PE'),
('Carlos Oliveira', '56789012345', 'carlos.oliveira@email.com', '81987654325', '1988-07-18', 'Rua Sul, 654, Recife-PE');

-- ========================================
-- 2. PROFISSIONAIS
-- ========================================
INSERT INTO profissional (nome, cpf, email, telefone, especialidade, senioridade, comissao_percentual, inicio_jornada, fim_jornada, ativo) VALUES
('Carlos Silva', '98765432101', 'carlos.silva@barbearia.com', '81988881111', 'Corte e Barba', 'SENIOR', 40.00, '08:00:00', '18:00:00', true),
('Pedro Souza', '98765432102', 'pedro.souza@barbearia.com', '81988882222', 'Corte Masculino', 'PLENO', 30.00, '08:00:00', '18:00:00', true),
('Lucas Lima', '98765432103', 'lucas.lima@barbearia.com', '81988883333', 'Barba e Acabamento', 'JUNIOR', 20.00, '08:00:00', '18:00:00', true),
('Rafael Mendes', '98765432104', 'rafael.mendes@barbearia.com', '81988884444', 'Corte Premium', 'SENIOR', 45.00, '08:00:00', '18:00:00', true);

-- ========================================
-- 3. SERVIÇOS OFERECIDOS (TODOS ATIVOS)
-- ========================================
INSERT INTO servico_oferecido (nome, descricao, preco, duracao_minutos, ativo, motivo_inatividade) VALUES
('Corte Masculino Simples', 'Corte tradicional masculino', 35.00, 30, true, NULL),
('Corte + Barba', 'Corte completo com barba aparada e finalizada', 60.00, 60, true, NULL),
('Barba Completa', 'Barba aparada, finalizada e hidratada', 30.00, 30, true, NULL),
('Corte Premium', 'Corte estilizado com acabamento especial', 80.00, 45, true, NULL),
('Platinado', 'Descoloração e tonalização', 150.00, 120, true, NULL),
('Luzes', 'Mechas e luzes para estilo moderno', 120.00, 90, true, NULL),
('Degradê', 'Degradê fade profissional', 45.00, 35, true, NULL),
('Barba + Sobrancelha', 'Combo barba e design de sobrancelha', 40.00, 40, true, NULL);

-- ========================================
-- 4. QUALIFICAÇÕES (profissional_servico)
-- ========================================

-- Carlos Silva (SENIOR) - pode fazer todos os serviços
INSERT INTO profissional_servico (profissional_id, servicos_oferecidos_id) 
SELECT 1, id FROM servico_oferecido;

-- Pedro Souza (PLENO) - serviços intermediários
INSERT INTO profissional_servico (profissional_id, servicos_oferecidos_id) VALUES
(2, 1), -- Corte Simples
(2, 2), -- Corte + Barba
(2, 3), -- Barba Completa
(2, 4), -- Corte Premium
(2, 7), -- Degradê
(2, 8); -- Barba + Sobrancelha

-- Lucas Lima (JUNIOR) - serviços básicos
INSERT INTO profissional_servico (profissional_id, servicos_oferecidos_id) VALUES
(3, 1), -- Corte Simples
(3, 3), -- Barba Completa
(3, 7); -- Degradê

-- Rafael Mendes (SENIOR) - especialista em premium
INSERT INTO profissional_servico (profissional_id, servicos_oferecidos_id) VALUES
(4, 1), -- Corte Simples
(4, 2), -- Corte + Barba
(4, 4), -- Corte Premium
(4, 5), -- Platinado
(4, 6), -- Luzes
(4, 7); -- Degradê

-- ========================================
-- 5. JORNADAS DE TRABALHO
-- Segunda a Sexta: 08:00-12:00, 14:00-18:00
-- Sábado: 08:00-14:00
-- ========================================

-- Carlos Silva - Trabalha todos os dias
INSERT INTO jornada_trabalho (profissional_id, dia_semana, hora_inicio, hora_fim, ativo) VALUES
(1, 'SEGUNDA', '08:00:00', '12:00:00', true),
(1, 'SEGUNDA', '14:00:00', '18:00:00', true),
(1, 'TERCA', '08:00:00', '12:00:00', true),
(1, 'TERCA', '14:00:00', '18:00:00', true),
(1, 'QUARTA', '08:00:00', '12:00:00', true),
(1, 'QUARTA', '14:00:00', '18:00:00', true),
(1, 'QUINTA', '08:00:00', '12:00:00', true),
(1, 'QUINTA', '14:00:00', '18:00:00', true),
(1, 'SEXTA', '08:00:00', '12:00:00', true),
(1, 'SEXTA', '14:00:00', '18:00:00', true),
(1, 'SABADO', '08:00:00', '14:00:00', true);

-- Pedro Souza - Trabalha todos os dias
INSERT INTO jornada_trabalho (profissional_id, dia_semana, hora_inicio, hora_fim, ativo) VALUES
(2, 'SEGUNDA', '08:00:00', '12:00:00', true),
(2, 'SEGUNDA', '14:00:00', '18:00:00', true),
(2, 'TERCA', '08:00:00', '12:00:00', true),
(2, 'TERCA', '14:00:00', '18:00:00', true),
(2, 'QUARTA', '08:00:00', '12:00:00', true),
(2, 'QUARTA', '14:00:00', '18:00:00', true),
(2, 'QUINTA', '08:00:00', '12:00:00', true),
(2, 'QUINTA', '14:00:00', '18:00:00', true),
(2, 'SEXTA', '08:00:00', '12:00:00', true),
(2, 'SEXTA', '14:00:00', '18:00:00', true),
(2, 'SABADO', '08:00:00', '14:00:00', true);

-- Lucas Lima - Trabalha todos os dias
INSERT INTO jornada_trabalho (profissional_id, dia_semana, hora_inicio, hora_fim, ativo) VALUES
(3, 'SEGUNDA', '08:00:00', '12:00:00', true),
(3, 'SEGUNDA', '14:00:00', '18:00:00', true),
(3, 'TERCA', '08:00:00', '12:00:00', true),
(3, 'TERCA', '14:00:00', '18:00:00', true),
(3, 'QUARTA', '08:00:00', '12:00:00', true),
(3, 'QUARTA', '14:00:00', '18:00:00', true),
(3, 'QUINTA', '08:00:00', '12:00:00', true),
(3, 'QUINTA', '14:00:00', '18:00:00', true),
(3, 'SEXTA', '08:00:00', '12:00:00', true),
(3, 'SEXTA', '14:00:00', '18:00:00', true),
(3, 'SABADO', '08:00:00', '14:00:00', true);

-- Rafael Mendes - Trabalha terça a sábado
INSERT INTO jornada_trabalho (profissional_id, dia_semana, hora_inicio, hora_fim, ativo) VALUES
(4, 'TERCA', '08:00:00', '12:00:00', true),
(4, 'TERCA', '14:00:00', '18:00:00', true),
(4, 'QUARTA', '08:00:00', '12:00:00', true),
(4, 'QUARTA', '14:00:00', '18:00:00', true),
(4, 'QUINTA', '08:00:00', '12:00:00', true),
(4, 'QUINTA', '14:00:00', '18:00:00', true),
(4, 'SEXTA', '08:00:00', '12:00:00', true),
(4, 'SEXTA', '14:00:00', '18:00:00', true),
(4, 'SABADO', '08:00:00', '14:00:00', true);

-- ========================================
-- 6. AGENDAMENTOS DE TESTE
-- Data atual: 11/12/2025
-- Agendamentos para datas futuras
-- ========================================

-- AGENDAMENTOS PARA AMANHÃ (12/12/2025 - Quinta-feira)
INSERT INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes) VALUES
(1, 1, 2, '2025-12-12 09:00:00', 'CONFIRMADO', 'Cliente VIP'),
(2, 2, 1, '2025-12-12 10:00:00', 'CONFIRMADO', NULL),
(3, 3, 1, '2025-12-12 14:00:00', 'PENDENTE', 'Primeira vez'),
(4, 4, 4, '2025-12-12 15:00:00', 'CONFIRMADO', 'Evento especial');

-- AGENDAMENTOS PARA SEXTA-FEIRA (13/12/2025)
INSERT INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes) VALUES
(1, 2, 3, '2025-12-13 08:30:00', 'CONFIRMADO', 'Barba por fazer'),
(2, 1, 2, '2025-12-13 10:30:00', 'PENDENTE', NULL),
(3, 4, 6, '2025-12-13 14:00:00', 'CONFIRMADO', 'Luzes californianas'),
(5, 3, 7, '2025-12-13 16:00:00', 'CONFIRMADO', 'Degradê fade');

-- AGENDAMENTOS PARA SÁBADO (14/12/2025)
INSERT INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes) VALUES
(1, 1, 4, '2025-12-14 08:00:00', 'CONFIRMADO', 'Corte fim de semana'),
(2, 2, 1, '2025-12-14 09:00:00', 'CONFIRMADO', NULL),
(3, 3, 3, '2025-12-14 10:30:00', 'CONFIRMADO', 'Barba sábado'),
(4, 4, 8, '2025-12-14 11:00:00', 'PENDENTE', 'Combo completo');

-- AGENDAMENTOS PARA PRÓXIMA SEMANA (16-20/12/2025)
INSERT INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes) VALUES
-- Segunda (16/12)
(1, 1, 5, '2025-12-16 09:00:00', 'PENDENTE', 'Platinado agendado'),
(2, 2, 2, '2025-12-16 14:00:00', 'CONFIRMADO', NULL),
-- Terça (17/12)
(3, 4, 4, '2025-12-17 10:00:00', 'CONFIRMADO', 'Corte premium'),
(4, 1, 3, '2025-12-17 15:00:00', 'CONFIRMADO', 'Manutenção barba'),
-- Quarta (18/12)
(5, 2, 7, '2025-12-18 09:30:00', 'CONFIRMADO', 'Degradê profissional'),
(1, 3, 1, '2025-12-18 14:00:00', 'PENDENTE', NULL),
-- Quinta (19/12)
(2, 4, 6, '2025-12-19 10:00:00', 'CONFIRMADO', 'Luzes e mechas'),
(3, 1, 2, '2025-12-19 15:30:00', 'CONFIRMADO', 'Pacote completo'),
-- Sexta (20/12)
(4, 2, 4, '2025-12-20 08:00:00', 'CONFIRMADO', 'Preparação fim de semana'),
(5, 3, 3, '2025-12-20 16:00:00', 'PENDENTE', 'Barba express');

-- ========================================
-- RESUMO DOS DADOS:
-- - 5 Clientes (IDs: 1-5)
-- - 4 Profissionais (IDs: 1-4)
-- - 8 Serviços ATIVOS (IDs: 1-8)
-- - Qualificações configuradas
-- - Jornadas de trabalho completas
-- - 20 Agendamentos futuros (12/12 a 20/12/2025)
-- ========================================
