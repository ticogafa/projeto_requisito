-- ====================================================================
-- ESQUEMA DE BANCO DE DADOS - (BARBEARIA)
-- ====================================================================

-- ===================== PROFISSIONAIS E SERVIÇOS ===================

-- REGRAS DE NEGÓCIO - SERVIÇO:
-- • Um Serviço deve ter uma Duração e um Preço
CREATE TABLE servico (
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    nome VARCHAR(255) NOT NULL,
    preco DECIMAL(15, 2) NOT NULL,
    duracaoMinutos INT NOT NULL,
    ativo BOOLEAN DEFAULT TRUE, -- opcional
);

-- REGRAS DE NEGÓCIO - PROFISSIONAL:
-- • Um Profissional pode ser associado a múltiplos Serviços
-- • A Configuração de Horário deve respeitar o horário de funcionamento da barbearia
CREATE TABLE profissional (
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(127) NOT NULL UNIQUE,
    telefone VARCHAR(13) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE, -- opcional
);

-- Tabela de associação entre profissionais e serviços
CREATE TABLE profissionalServico (
    profissionalId VARCHAR(36) NOT NULL,
    servicoId VARCHAR(36) NOT NULL,
    PRIMARY KEY (profissionalId, servicoId),
    FOREIGN KEY (profissionalId) REFERENCES profissional(id) ON DELETE CASCADE,
    FOREIGN KEY (servicoId) REFERENCES servico(id) ON DELETE CASCADE
);
-- criar um método no código pra simplificar
// Pseudo-código
function podeAgendar() {
}



-- ========================== CLIENTE ================================

-- REGRAS DE NEGÓCIO - CLIENTE:
-- • A cada R$ 1,00 gasto no agendamento → 1 ponto de fidelidade
CREATE TABLE cliente (
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(127) NOT NULL UNIQUE,
    telefone VARCHAR(13) NOT NULL,
    pontosFidelidade INT DEFAULT 0, -- opcional
    ativo BOOLEAN DEFAULT TRUE, -- opcional
);

-- ========================== AGENDAMENTO ============================

-- REGRAS DE NEGÓCIO - AGENDAMENTO:
-- • Um Agendamento só pode ser criado se o Horário Disponível estiver livre
-- • Um Agendamento não pode ser criado em um horário fora da jornada de trabalho do profissional
-- • Quando um Agendamento é criado, ele tem o Status de Pendente e só muda para Confirmado após a validação
-- • Um Agendamento só pode ser cancelado até 2 horas antes do horário
CREATE TABLE agendamento (
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    clienteId VARCHAR(36) NOT NULL,
    profissionalId VARCHAR(36) NOT NULL,
    servicoId VARCHAR(36) NOT NULL,
    dataHora DATETIME NOT NULL,
    status ENUM('PENDENTE', 'CONFIRMADO', 'CANCELADO', 'CONCLUIDO') DEFAULT 'PENDENTE', -- opcional
    observacoes TEXT, -- opcional
    FOREIGN KEY (clienteId) REFERENCES cliente(id) ON DELETE RESTRICT,
    FOREIGN KEY (profissionalId) REFERENCES profissional(id) ON DELETE RESTRICT,
    FOREIGN KEY (servicoId) REFERENCES servico(id) ON DELETE RESTRICT
);

-- ======================= PRODUTOS E ESTOQUE ========================

-- REGRAS DE NEGÓCIO - PRODUTO:
-- • Não pode ser feita venda de um produto sem estoque
CREATE TABLE produto (
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    nome VARCHAR(255) NOT NULL,
    preco DECIMAL(15, 2) NOT NULL,
    estoque INT NOT NULL DEFAULT 0,
    estoqueMinimo INT DEFAULT 0, -- opcional
    ativo BOOLEAN DEFAULT TRUE, -- opcional
);

-- ======================= VENDAS ====================================

-- REGRAS DE NEGÓCIO - VENDA:
-- • A Venda de um Produto deve dar baixa no Estoque
-- • Um Pagamento deve ser registrado para cada Serviço e Produto vendidos
CREATE TABLE venda (
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    clienteId VARCHAR(36), -- opcional
    dataVenda DATETIME DEFAULT NOW(),
    voucherId VARCHAR(36), -- opcional
    valorTotal DECIMAL(15, 2) NOT NULL,
    desconto DECIMAL(15, 2) DEFAULT 0.00, -- opcional
    observacoes TEXT, -- opcional
    FOREIGN KEY (clienteId) REFERENCES cliente(id) ON DELETE SET NULL,
    FOREIGN KEY (voucherId) REFERENCES voucher(id) ON DELETE SET NULL
);

CREATE TABLE itemVenda (
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    vendaId VARCHAR(36) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    quantidade INT NOT NULL DEFAULT 1,
    precoUnitario DECIMAL(15, 2) NOT NULL,
    precoTotal DECIMAL(15, 2) NOT NULL,
    tipo ENUM('PRODUTO', 'SERVICO') NOT NULL,
    FOREIGN KEY (vendaId) REFERENCES venda(id) ON DELETE CASCADE,
    FOREIGN KEY (produtoId) REFERENCES produto(id) ON DELETE SET NULL,
    FOREIGN KEY (servicoId) REFERENCES servico(id) ON DELETE SET NULL
);

-- REGRAS DE NEGÓCIO - PAGAMENTO:
-- • Um Pagamento deve ser registrado para cada venda
CREATE TABLE pagamento (
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    vendaId VARCHAR(36) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    metodo ENUM('DINHEIRO', 'CREDITO', 'DEBITO', 'PIX') NOT NULL,
    status ENUM('PENDENTE', 'CONFIRMADO', 'CANCELADO') DEFAULT 'PENDENTE', -- opcional
    dataProcessamento DATETIME DEFAULT NOW(),
    FOREIGN KEY (vendaId) REFERENCES venda(id) ON DELETE CASCADE
);

-- ========================== VOUCHER =================================

-- REGRAS DE NEGÓCIO - VOUCHER:
-- • A cada 100 pontos → R$ 10,00 de desconto
-- • Quando o cliente troca pontos, gera um voucher
-- • Quando o voucher é utilizado, ele é vinculado à venda (campo voucherId)
-- • Mantém controle de expiração e auditoria
CREATE TABLE voucher (
    id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    clienteId VARCHAR(36) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    valorDesconto DECIMAL(15,2) NOT NULL,
    status ENUM('GERADO','UTILIZADO','EXPIRADO') DEFAULT 'GERADO', -- opcional
    expiraEm DATETIME, -- opcional
    FOREIGN KEY (clienteId) REFERENCES cliente(id) ON DELETE CASCADE
);
