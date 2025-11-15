-- Active: 1762782988513@@127.0.0.1@3306@barbearia_db
-- ========================================
-- SCRIPT COMPLETO DE SETUP
-- Execute manualmente caso o Flyway não funcione
-- Data: 15/11/2025
-- ========================================

-- ========================================
-- 1. CRIAR TABELAS BÁSICAS
-- ========================================

-- Tabela de Produtos
CREATE TABLE IF NOT EXISTS PRODUTO (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    NOME VARCHAR(200) NOT NULL UNIQUE,
    ESTOQUE INT NOT NULL DEFAULT 0,
    PRECO DECIMAL(10, 2) NOT NULL,
    ESTOQUE_MINIMO INT NOT NULL DEFAULT 0,
    INDEX idx_produto_nome (NOME),
    INDEX idx_produto_estoque (ESTOQUE)
);

-- Tabela de Movimentação de Estoque
CREATE TABLE IF NOT EXISTS MOVIMENTACAO_ESTOQUE (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    PRODUTO_ID INT NOT NULL,
    NOME_PRODUTO VARCHAR(200) NOT NULL,
    TIPO VARCHAR(50) NOT NULL,
    QUANTIDADE INT NOT NULL,
    ESTOQUE_ANTERIOR INT NOT NULL,
    ESTOQUE_ATUAL INT NOT NULL,
    DATA_HORA TIMESTAMP NOT NULL,
    OBSERVACAO VARCHAR(500),
    USUARIO_RESPONSAVEL VARCHAR(100),
    FOREIGN KEY (PRODUTO_ID) REFERENCES PRODUTO(ID),
    INDEX idx_movimentacao_produto (PRODUTO_ID),
    INDEX idx_movimentacao_data (DATA_HORA),
    INDEX idx_movimentacao_tipo (TIPO)
);

-- ========================================
-- 2. CRIAR TABELAS DE DOMÍNIO
-- ========================================

-- Tabela Cliente
CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    data_nascimento DATE,
    endereco VARCHAR(255),
    pontos INT NOT NULL DEFAULT 0,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_cliente_cpf (cpf),
    INDEX idx_cliente_email (email),
    INDEX idx_cliente_nome (nome)
);

-- Tabela Profissional
CREATE TABLE IF NOT EXISTS profissional (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    especialidade VARCHAR(100),
    senioridade VARCHAR(20) NOT NULL,
    comissao_percentual DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_profissional_cpf (cpf),
    INDEX idx_profissional_email (email),
    INDEX idx_profissional_ativo (ativo),
    INDEX idx_profissional_senioridade (senioridade)
);

-- Tabela Servico Oferecido
CREATE TABLE IF NOT EXISTS servico_oferecido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),
    preco DECIMAL(10,2) NOT NULL,
    duracao_minutos INT NOT NULL,
    categoria VARCHAR(50),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_servico_nome (nome),
    INDEX idx_servico_ativo (ativo),
    INDEX idx_servico_categoria (categoria)
);

-- Tabela Many-to-Many: Profissional <-> Servico (Qualificações)
CREATE TABLE IF NOT EXISTS profissional_servico (
    profissional_id INT NOT NULL,
    servicos_oferecidos_id INT NOT NULL,
    PRIMARY KEY (profissional_id, servicos_oferecidos_id),
    FOREIGN KEY (profissional_id) REFERENCES profissional(id) ON DELETE CASCADE,
    FOREIGN KEY (servicos_oferecidos_id) REFERENCES servico_oferecido(id) ON DELETE CASCADE,
    INDEX idx_prof_servico_prof (profissional_id),
    INDEX idx_prof_servico_servico (servicos_oferecidos_id)
);

-- Tabela Jornada de Trabalho
CREATE TABLE IF NOT EXISTS jornada_trabalho (
    id INT AUTO_INCREMENT PRIMARY KEY,
    profissional_id INT NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (profissional_id) REFERENCES profissional(id) ON DELETE CASCADE,
    INDEX idx_jornada_profissional (profissional_id),
    INDEX idx_jornada_dia (dia_semana),
    INDEX idx_jornada_ativo (ativo)
);

-- Tabela Agendamento
CREATE TABLE IF NOT EXISTS agendamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_hora TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    cliente_id INT NOT NULL,
    profissional_id INT,
    servico_id INT NOT NULL,
    observacoes VARCHAR(500),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (profissional_id) REFERENCES profissional(id),
    FOREIGN KEY (servico_id) REFERENCES servico_oferecido(id),
    INDEX idx_agendamento_data_hora (data_hora),
    INDEX idx_agendamento_cliente (cliente_id),
    INDEX idx_agendamento_profissional (profissional_id),
    INDEX idx_agendamento_status (status),
    INDEX idx_agendamento_conflito (profissional_id, data_hora, status)
);

-- ========================================
-- 3. POPULAR DADOS INICIAIS
-- ========================================

-- Produtos
INSERT IGNORE INTO PRODUTO (NOME, ESTOQUE, PRECO, ESTOQUE_MINIMO) VALUES
('Shampoo Anticaspa', 100, 29.90, 20),
('Gel Fixador', 50, 15.50, 10),
('Pomada Modeladora', 25, 45.00, 5),
('Pomada Forte', 30, 50.00, 10),
('Cera Modeladora', 40, 35.00, 10),
('Óleo para Barba', 60, 38.50, 15);

-- Clientes
INSERT IGNORE INTO cliente (id, nome, cpf, email, telefone, data_nascimento, endereco) VALUES
(1, 'João Pereira', '12345678901', 'joao.pereira@email.com', '81987654321', '1990-05-15', 'Rua das Flores, 123, Recife-PE'),
(2, 'Maria Silva', '23456789012', 'maria.silva@email.com', '81987654322', '1985-08-20', 'Av. Boa Viagem, 456, Recife-PE'),
(3, 'Pedro Santos', '34567890123', 'pedro.santos@email.com', '81987654323', '1995-03-10', 'Rua do Sol, 789, Recife-PE'),
(4, 'Ana Costa', '45678901234', 'ana.costa@email.com', '81987654324', '1992-11-25', 'Av. Norte, 321, Recife-PE');

-- Profissionais
INSERT IGNORE INTO profissional (id, nome, cpf, email, telefone, especialidade, senioridade, comissao_percentual, ativo) VALUES
(1, 'Carlos Silva', '98765432101', 'carlos.silva@barbearia.com', '81988881111', 'Corte e Barba', 'SENIOR', 40.00, true),
(2, 'Pedro Souza', '98765432102', 'pedro.souza@barbearia.com', '81988882222', 'Corte Masculino', 'PLENO', 30.00, true),
(3, 'Lucas Lima', '98765432103', 'lucas.lima@barbearia.com', '81988883333', 'Barba e Acabamento', 'JUNIOR', 20.00, true),
(4, 'Rafael Mendes', '98765432104', 'rafael.mendes@barbearia.com', '81988884444', 'Corte Premium', 'SENIOR', 45.00, true);

-- Serviços
INSERT IGNORE INTO servico_oferecido (id, nome, descricao, preco, duracao_minutos, categoria) VALUES
(1, 'Corte Masculino Simples', 'Corte tradicional masculino', 35.00, 30, 'CORTE'),
(2, 'Corte + Barba', 'Corte completo com barba aparada e finalizada', 60.00, 60, 'CORTE_BARBA'),
(3, 'Barba Completa', 'Barba aparada, finalizada e hidratada', 30.00, 30, 'BARBA'),
(4, 'Corte Premium', 'Corte estilizado com acabamento especial', 80.00, 45, 'CORTE'),
(5, 'Platinado', 'Descoloração e tonalização', 150.00, 120, 'COLORACAO'),
(6, 'Luzes', 'Mechas e luzes para estilo moderno', 120.00, 90, 'COLORACAO');

-- Qualificações (Many-to-Many)
INSERT IGNORE INTO profissional_servico (profissional_id, servicos_oferecidos_id) VALUES
-- Carlos (SENIOR - todos)
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
-- Pedro (PLENO - intermediários)
(2, 1), (2, 2), (2, 3), (2, 4),
-- Lucas (JUNIOR - básicos)
(3, 1), (3, 3),
-- Rafael (SENIOR - premium)
(4, 1), (4, 2), (4, 4), (4, 5), (4, 6);

-- Jornadas de Trabalho (Segunda a Sábado)
INSERT IGNORE INTO jornada_trabalho (profissional_id, dia_semana, hora_inicio, hora_fim, ativo) VALUES
-- Carlos
(1, 'SEGUNDA', '08:00:00', '12:00:00', true), (1, 'SEGUNDA', '14:00:00', '18:00:00', true),
(1, 'TERCA', '08:00:00', '12:00:00', true), (1, 'TERCA', '14:00:00', '18:00:00', true),
(1, 'QUARTA', '08:00:00', '12:00:00', true), (1, 'QUARTA', '14:00:00', '18:00:00', true),
(1, 'QUINTA', '08:00:00', '12:00:00', true), (1, 'QUINTA', '14:00:00', '18:00:00', true),
(1, 'SEXTA', '08:00:00', '12:00:00', true), (1, 'SEXTA', '14:00:00', '18:00:00', true),
(1, 'SABADO', '08:00:00', '14:00:00', true),
-- Pedro
(2, 'SEGUNDA', '08:00:00', '12:00:00', true), (2, 'SEGUNDA', '14:00:00', '18:00:00', true),
(2, 'TERCA', '08:00:00', '12:00:00', true), (2, 'TERCA', '14:00:00', '18:00:00', true),
(2, 'QUARTA', '08:00:00', '12:00:00', true), (2, 'QUARTA', '14:00:00', '18:00:00', true),
(2, 'QUINTA', '08:00:00', '12:00:00', true), (2, 'QUINTA', '14:00:00', '18:00:00', true),
(2, 'SEXTA', '08:00:00', '12:00:00', true), (2, 'SEXTA', '14:00:00', '18:00:00', true),
(2, 'SABADO', '08:00:00', '14:00:00', true),
-- Lucas
(3, 'SEGUNDA', '08:00:00', '12:00:00', true), (3, 'SEGUNDA', '14:00:00', '18:00:00', true),
(3, 'TERCA', '08:00:00', '12:00:00', true), (3, 'TERCA', '14:00:00', '18:00:00', true),
(3, 'QUARTA', '08:00:00', '12:00:00', true), (3, 'QUARTA', '14:00:00', '18:00:00', true),
(3, 'QUINTA', '08:00:00', '12:00:00', true), (3, 'QUINTA', '14:00:00', '18:00:00', true),
(3, 'SEXTA', '08:00:00', '12:00:00', true), (3, 'SEXTA', '14:00:00', '18:00:00', true),
(3, 'SABADO', '08:00:00', '14:00:00', true),
-- Rafael (Terça a Sábado)
(4, 'TERCA', '08:00:00', '12:00:00', true), (4, 'TERCA', '14:00:00', '18:00:00', true),
(4, 'QUARTA', '08:00:00', '12:00:00', true), (4, 'QUARTA', '14:00:00', '18:00:00', true),
(4, 'QUINTA', '08:00:00', '12:00:00', true), (4, 'QUINTA', '14:00:00', '18:00:00', true),
(4, 'SEXTA', '08:00:00', '12:00:00', true), (4, 'SEXTA', '14:00:00', '18:00:00', true),
(4, 'SABADO', '08:00:00', '14:00:00', true);

-- Agendamentos Futuros (18/11 a 29/11/2025)
INSERT IGNORE INTO agendamento (cliente_id, profissional_id, servico_id, data_hora, status, observacoes, criado_em) VALUES
-- Segunda 18/11
(1, 1, 2, '2025-11-18 09:00:00', 'CONFIRMADO', 'Cliente preferencial', '2025-11-15 10:00:00'),
(2, 2, 1, '2025-11-18 10:00:00', 'CONFIRMADO', NULL, '2025-11-15 11:30:00'),
(3, 3, 1, '2025-11-18 14:00:00', 'PENDENTE', 'Primeira vez', '2025-11-15 14:20:00'),
(4, 4, 4, '2025-11-18 15:00:00', 'CONFIRMADO', 'Evento especial', '2025-11-15 15:45:00'),
-- Terça 19/11
(1, 2, 3, '2025-11-19 08:30:00', 'CONFIRMADO', 'Barba por fazer', '2025-11-15 16:00:00'),
(2, 1, 2, '2025-11-19 10:30:00', 'PENDENTE', NULL, '2025-11-15 17:10:00'),
(3, 4, 6, '2025-11-19 14:00:00', 'CONFIRMADO', 'Luzes californianas', '2025-11-15 18:00:00'),
-- Quarta 20/11
(4, 1, 4, '2025-11-20 09:00:00', 'CONFIRMADO', 'Corte especial feriado', '2025-11-15 19:00:00'),
(1, 3, 1, '2025-11-20 11:00:00', 'CONFIRMADO', NULL, '2025-11-15 19:30:00'),
-- Quinta 21/11
(2, 4, 5, '2025-11-21 09:00:00', 'CONFIRMADO', 'Platinado completo', '2025-11-15 20:00:00'),
(3, 2, 2, '2025-11-21 14:30:00', 'PENDENTE', NULL, '2025-11-15 20:30:00'),
(4, 1, 3, '2025-11-21 16:00:00', 'CONFIRMADO', 'Manutenção barba', '2025-11-15 21:00:00'),
-- Sexta 22/11
(1, 1, 2, '2025-11-22 08:00:00', 'CONFIRMADO', 'Preparação fim de semana', '2025-11-15 08:00:00'),
(2, 3, 1, '2025-11-22 10:00:00', 'CONFIRMADO', NULL, '2025-11-15 09:00:00'),
(3, 2, 4, '2025-11-22 14:00:00', 'PENDENTE', 'Evento sábado', '2025-11-15 10:00:00'),
(4, 4, 6, '2025-11-22 15:30:00', 'CONFIRMADO', 'Retoque luzes', '2025-11-15 11:00:00'),
-- Sábado 23/11
(1, 2, 1, '2025-11-23 08:00:00', 'CONFIRMADO', NULL, '2025-11-15 12:00:00'),
(2, 1, 2, '2025-11-23 09:00:00', 'CONFIRMADO', 'Pacote completo', '2025-11-15 13:00:00'),
(3, 3, 3, '2025-11-23 10:30:00', 'CONFIRMADO', 'Barba sábado', '2025-11-15 14:00:00'),
(4, 4, 4, '2025-11-23 11:00:00', 'PENDENTE', 'Último horário', '2025-11-15 15:00:00'),
-- Semana seguinte
(1, 1, 5, '2025-11-25 09:00:00', 'PENDENTE', 'Platinado agendado', '2025-11-15 16:00:00'),
(2, 4, 4, '2025-11-26 14:00:00', 'CONFIRMADO', NULL, '2025-11-15 17:00:00'),
(3, 2, 2, '2025-11-27 10:00:00', 'PENDENTE', 'Pacote completo', '2025-11-15 18:00:00'),
(4, 3, 1, '2025-11-28 15:00:00', 'CONFIRMADO', NULL, '2025-11-15 19:00:00'),
(1, 1, 3, '2025-11-29 09:30:00', 'CONFIRMADO', 'Manutenção regular', '2025-11-15 20:00:00');

SELECT 'Setup completo executado com sucesso!' AS status;
