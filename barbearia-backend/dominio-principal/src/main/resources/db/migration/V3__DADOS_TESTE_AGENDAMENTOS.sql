-- Active: 1762782988513@@127.0.0.1@3306@barbearia_db
-- ========================================
-- V3: Dados de teste para agendamentos
-- Data de referência: 15/11/2025
-- ========================================

-- ========================================
-- 1. CLIENTES
-- ========================================
INSERT INTO cliente (id, nome, cpf, email, telefone, data_nascimento, endereco) VALUES
(1, 'João Pereira', '12345678901', 'joao.pereira@email.com', '81987654321', '1990-05-15', 'Rua das Flores, 123, Recife-PE'),
(2, 'Maria Silva', '23456789012', 'maria.silva@email.com', '81987654322', '1985-08-20', 'Av. Boa Viagem, 456, Recife-PE'),
(3, 'Pedro Santos', '34567890123', 'pedro.santos@email.com', '81987654323', '1995-03-10', 'Rua do Sol, 789, Recife-PE'),
(4, 'Ana Costa', '45678901234', 'ana.costa@email.com', '81987654324', '1992-11-25', 'Av. Norte, 321, Recife-PE');

-- ========================================
-- 2. PROFISSIONAIS
-- ========================================
INSERT INTO profissional (id, nome, cpf, email, telefone, especialidade, senioridade, comissao_percentual, inicio_jornada, fim_jornada, ativo) VALUES
(1, 'Carlos Silva', '98765432101', 'carlos.silva@barbearia.com', '81988881111', 'Corte e Barba', 'SENIOR', 40.00, '08:00:00', '18:00:00', true),
(2, 'Pedro Souza', '98765432102', 'pedro.souza@barbearia.com', '81988882222', 'Corte Masculino', 'PLENO', 30.00, '08:00:00', '18:00:00', true),
(3, 'Lucas Lima', '98765432103', 'lucas.lima@barbearia.com', '81988883333', 'Barba e Acabamento', 'JUNIOR', 20.00, '08:00:00', '18:00:00', true),
(4, 'Rafael Mendes', '98765432104', 'rafael.mendes@barbearia.com', '81988884444', 'Corte Premium', 'SENIOR', 45.00, '08:00:00', '18:00:00', true);

-- ========================================
-- 3. SERVIÇOS OFERECIDOS
-- ========================================
INSERT INTO servico_oferecido (nome, descricao, preco, duracao_minutos) VALUES
('Corte Masculino Simples', 'Corte tradicional masculino', 35.00, 30),
('Corte + Barba', 'Corte completo com barba aparada e finalizada', 60.00, 60),
('Barba Completa', 'Barba aparada, finalizada e hidratada', 30.00, 30),
('Corte Premium', 'Corte estilizado com acabamento especial', 80.00, 45),
('Platinado', 'Descoloração e tonalização', 150.00, 120),
('Luzes', 'Mechas e luzes para estilo moderno', 120.00, 90);

-- ========================================
-- 4. QUALIFICAÇÕES (profissional_servico)
-- Associa profissionais aos serviços que podem realizar
-- ========================================

-- Carlos Silva (SENIOR) - pode fazer todos os serviços
INSERT INTO profissional_servico (profissional_id, servicos_oferecidos_id) VALUES
(1, 1), -- Corte Simples
(1, 2), -- Corte + Barba
(1, 3), -- Barba Completa
(1, 4), -- Corte Premium
(1, 5), -- Platinado
(1, 6); -- Luzes

-- Pedro Souza (PLENO) - serviços intermediários
INSERT INTO profissional_servico (profissional_id, servicos_oferecidos_id) VALUES
(2, 1), -- Corte Simples
(2, 2), -- Corte + Barba
(2, 3), -- Barba Completa
(2, 4); -- Corte Premium

-- Lucas Lima (JUNIOR) - serviços básicos
INSERT INTO profissional_servico (profissional_id, servicos_oferecidos_id) VALUES
(3, 1), -- Corte Simples
(3, 3); -- Barba Completa

-- Rafael Mendes (SENIOR) - especialista em premium
INSERT INTO profissional_servico (profissional_id, servicos_oferecidos_id) VALUES
(4, 1), -- Corte Simples
(4, 2), -- Corte + Barba
(4, 4), -- Corte Premium
(4, 5), -- Platinado
(4, 6); -- Luzes

-- ========================================
-- 5. JORNADAS DE TRABALHO DOS PROFISSIONAIS
-- Segunda a Sexta: 08:00-12:00, 14:00-18:00
-- Sábado: 08:00-14:00
-- ========================================
INSERT INTO jornada_trabalho (profissional_id, dia_semana, hora_inicio, hora_fim, ativo) VALUES
-- Carlos Silva
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
(1, 'SABADO', '08:00:00', '14:00:00', true),

-- Pedro Souza
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
(2, 'SABADO', '08:00:00', '14:00:00', true),

-- Lucas Lima
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
(3, 'SABADO', '08:00:00', '14:00:00', true),

-- Rafael Mendes (trabalha terça a sábado)
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
-- 6. AGENDAMENTOS
-- Data atual: 15/11/2025 (sexta-feira)
-- Criando agendamentos para o futuro (semana seguinte)
-- ========================================

-- AGENDAMENTOS PARA SEGUNDA-FEIRA 18/11/2025
INSERT INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes, criado_em) VALUES
(1, 1, 2, '2025-11-18 09:00:00', 'CONFIRMADO', 'Cliente preferencial', '2025-11-15 10:00:00'),
(2, 2, 1, '2025-11-18 10:00:00', 'CONFIRMADO', NULL, '2025-11-15 11:30:00'),
(3, 3, 1, '2025-11-18 14:00:00', 'PENDENTE', 'Primeira vez na barbearia', '2025-11-15 14:20:00'),
(4, 4, 4, '2025-11-18 15:00:00', 'CONFIRMADO', 'Evento especial', '2025-11-15 15:45:00');

-- AGENDAMENTOS PARA TERÇA-FEIRA 19/11/2025
INSERT INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes, criado_em) VALUES
(1, 2, 3, '2025-11-19 08:30:00', 'CONFIRMADO', 'Barba por fazer', '2025-11-15 16:00:00'),
(2, 1, 2, '2025-11-19 10:30:00', 'PENDENTE', NULL, '2025-11-15 17:10:00'),
(3, 4, 6, '2025-11-19 14:00:00', 'CONFIRMADO', 'Luzes californianas', '2025-11-15 18:00:00');

-- AGENDAMENTOS PARA QUARTA-FEIRA 20/11/2025 (Dia da Consciência Negra)
INSERT INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes, criado_em) VALUES
(4, 1, 4, '2025-11-20 09:00:00', 'CONFIRMADO', 'Corte especial feriado', '2025-11-15 19:00:00'),
(1, 3, 1, '2025-11-20 11:00:00', 'CONFIRMADO', NULL, '2025-11-15 19:30:00');

-- AGENDAMENTOS PARA QUINTA-FEIRA 21/11/2025
INSERT INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes, criado_em) VALUES
(2, 4, 5, '2025-11-21 09:00:00', 'CONFIRMADO', 'Platinado completo', '2025-11-15 20:00:00'),
(3, 2, 2, '2025-11-21 14:30:00', 'PENDENTE', NULL, '2025-11-15 20:30:00'),
(4, 1, 3, '2025-11-21 16:00:00', 'CONFIRMADO', 'Manutenção barba', '2025-11-15 21:00:00');

-- AGENDAMENTOS PARA SEXTA-FEIRA 22/11/2025
INSERT INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes, criado_em) VALUES
(1, 1, 2, '2025-11-22 08:00:00', 'CONFIRMADO', 'Preparação fim de semana', '2025-11-15 08:00:00'),
(2, 3, 1, '2025-11-22 10:00:00', 'CONFIRMADO', NULL, '2025-11-15 09:00:00'),
(3, 2, 4, '2025-11-22 14:00:00', 'PENDENTE', 'Evento sábado', '2025-11-15 10:00:00'),
(4, 4, 6, '2025-11-22 15:30:00', 'CONFIRMADO', 'Retoque luzes', '2025-11-15 11:00:00');

-- AGENDAMENTOS PARA SÁBADO 23/11/2025
INSERT INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes, criado_em) VALUES
(1, 2, 1, '2025-11-23 08:00:00', 'CONFIRMADO', NULL, '2025-11-15 12:00:00'),
(2, 1, 2, '2025-11-23 09:00:00', 'CONFIRMADO', 'Pacote completo', '2025-11-15 13:00:00'),
(3, 3, 3, '2025-11-23 10:30:00', 'CONFIRMADO', 'Barba sábado', '2025-11-15 14:00:00'),
(4, 4, 4, '2025-11-23 11:00:00', 'PENDENTE', 'Último horário', '2025-11-15 15:00:00');

-- AGENDAMENTOS MAIS DISTANTES (SEMANA SEGUINTE 25-29/11/2025)
INSERT INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes, criado_em) VALUES
(1, 1, 5, '2025-11-25 09:00:00', 'PENDENTE', 'Platinado agendado', '2025-11-15 16:00:00'),
(2, 4, 4, '2025-11-26 14:00:00', 'CONFIRMADO', NULL, '2025-11-15 17:00:00'),
(3, 2, 2, '2025-11-27 10:00:00', 'PENDENTE', 'Pacote completo', '2025-11-15 18:00:00'),
(4, 3, 1, '2025-11-28 15:00:00', 'CONFIRMADO', NULL, '2025-11-15 19:00:00'),
(1, 1, 3, '2025-11-29 09:30:00', 'CONFIRMADO', 'Manutenção regular', '2025-11-15 20:00:00');

-- ========================================
-- RESUMO DOS DADOS CRIADOS:
-- - 4 Clientes (João Pereira ID=1 como principal)
-- - 4 Profissionais (Carlos, Pedro, Lucas, Rafael)
-- - 6 Serviços (Corte Simples, Corte+Barba, Barba, Premium, Platinado, Luzes)
-- - Qualificações profissionais configuradas
-- - Jornadas de trabalho (Seg-Sáb)
-- - 25 Agendamentos futuros (18/11 a 29/11/2025)
-- - Status: CONFIRMADO (maioria) e PENDENTE (alguns)
-- ========================================
