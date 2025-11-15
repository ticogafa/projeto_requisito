-- ========================================
-- V1.5: Criação das tabelas de domínio principal
-- Cliente, Profissional, Serviço, Jornada
-- ========================================

-- ========================================
-- TABELA CLIENTE
-- ========================================
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
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Índices para Cliente
CREATE INDEX idx_cliente_cpf ON cliente(cpf);
CREATE INDEX idx_cliente_email ON cliente(email);
CREATE INDEX idx_cliente_nome ON cliente(nome);

-- ========================================
-- TABELA PROFISSIONAL
-- ========================================
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
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Índices para Profissional
CREATE INDEX idx_profissional_cpf ON profissional(cpf);
CREATE INDEX idx_profissional_email ON profissional(email);
CREATE INDEX idx_profissional_ativo ON profissional(ativo);
CREATE INDEX idx_profissional_senioridade ON profissional(senioridade);

-- ========================================
-- TABELA SERVICO_OFERECIDO
-- ========================================
CREATE TABLE IF NOT EXISTS servico_oferecido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),
    preco DECIMAL(10,2) NOT NULL,
    duracao_minutos INT NOT NULL,
    categoria VARCHAR(50),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Índices para Servico Oferecido
CREATE INDEX idx_servico_nome ON servico_oferecido(nome);
CREATE INDEX idx_servico_ativo ON servico_oferecido(ativo);
CREATE INDEX idx_servico_categoria ON servico_oferecido(categoria);

-- ========================================
-- TABELA PROFISSIONAL_SERVICO (Many-to-Many)
-- Representa as qualificações dos profissionais
-- ========================================
CREATE TABLE IF NOT EXISTS profissional_servico (
    profissional_id INT NOT NULL,
    servicos_oferecidos_id INT NOT NULL,
    PRIMARY KEY (profissional_id, servicos_oferecidos_id),
    FOREIGN KEY (profissional_id) REFERENCES profissional(id) ON DELETE CASCADE,
    FOREIGN KEY (servicos_oferecidos_id) REFERENCES servico_oferecido(id) ON DELETE CASCADE
);

-- Índices para a tabela de qualificações
CREATE INDEX idx_prof_servico_prof ON profissional_servico(profissional_id);
CREATE INDEX idx_prof_servico_servico ON profissional_servico(servicos_oferecidos_id);

-- ========================================
-- TABELA JORNADA_TRABALHO
-- Define horários de trabalho dos profissionais
-- ========================================
CREATE TABLE IF NOT EXISTS jornada_trabalho (
    id INT AUTO_INCREMENT PRIMARY KEY,
    profissional_id INT NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (profissional_id) REFERENCES profissional(id) ON DELETE CASCADE
);

-- Índices para Jornada
CREATE INDEX idx_jornada_profissional ON jornada_trabalho(profissional_id);
CREATE INDEX idx_jornada_dia ON jornada_trabalho(dia_semana);
CREATE INDEX idx_jornada_ativo ON jornada_trabalho(ativo);

-- ========================================
-- ATUALIZAR TABELA AGENDAMENTO
-- Adicionar foreign keys e campos faltantes
-- ========================================
ALTER TABLE agendamento 
    ADD COLUMN IF NOT EXISTS criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN IF NOT EXISTS atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- Adicionar foreign keys (se não existirem)
-- Nota: Em MySQL você não pode adicionar FK se ela já existe, então usamos DROP IF EXISTS primeiro
ALTER TABLE agendamento DROP FOREIGN KEY IF EXISTS fk_agendamento_cliente;
ALTER TABLE agendamento DROP FOREIGN KEY IF EXISTS fk_agendamento_profissional;
ALTER TABLE agendamento DROP FOREIGN KEY IF EXISTS fk_agendamento_servico;

ALTER TABLE agendamento 
    ADD CONSTRAINT fk_agendamento_cliente 
        FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    ADD CONSTRAINT fk_agendamento_profissional 
        FOREIGN KEY (profissional_id) REFERENCES profissional(id),
    ADD CONSTRAINT fk_agendamento_servico 
        FOREIGN KEY (servico_id) REFERENCES servico_oferecido(id);

-- ========================================
-- COMENTÁRIOS
-- ========================================
ALTER TABLE cliente COMMENT = 'Cadastro de clientes da barbearia';
ALTER TABLE profissional COMMENT = 'Cadastro de profissionais (barbeiros)';
ALTER TABLE servico_oferecido COMMENT = 'Catálogo de serviços oferecidos';
ALTER TABLE profissional_servico COMMENT = 'Qualificações - quais serviços cada profissional pode realizar';
ALTER TABLE jornada_trabalho COMMENT = 'Horários de trabalho dos profissionais';
