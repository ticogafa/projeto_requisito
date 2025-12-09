-- ========================================
-- v1: criação das tabelas principais
-- estoque e agendamento (MySQL Syntax Fixed)
-- ========================================

-- tabela de produtos
create table produto (
    id int not null auto_increment,
    nome varchar(200) not null unique,
    estoque int not null default 0,
    preco decimal(10, 2) not null,
    estoque_minimo int not null default 0,
    primary key (id)
);

-- índices para produto
create index idx_produto_nome on produto(nome);
create index idx_produto_estoque on produto(estoque);

-- tabela de movimentação de estoque
create table movimentacao_estoque (
    id int not null auto_increment,
    produto_id int not null,
    nome_produto varchar(200) not null,
    tipo varchar(50) not null,
    quantidade int not null,
    estoque_anterior int not null,
    estoque_atual int not null,
    data_hora timestamp not null default current_timestamp,
    observacao varchar(500),
    usuario_responsavel varchar(100),
    primary key (id),
    foreign key (produto_id) references produto(id)
);

-- índices para movimentação de estoque
create index idx_movimentacao_produto on movimentacao_estoque(produto_id);
create index idx_movimentacao_data on movimentacao_estoque(data_hora);
create index idx_movimentacao_tipo on movimentacao_estoque(tipo);

-- tabela de agendamentos
create table agendamento (
    id int not null auto_increment,
    data_hora timestamp not null default current_timestamp,
    status varchar(20) not null,
    cliente_id int not null,
    profissional_id int,
    servico_id int not null,
    observacoes varchar(500),
    primary key (id)
);

-- índices para agendamento
create index idx_agendamento_data_hora on agendamento(data_hora);
create index idx_agendamento_cliente on agendamento(cliente_id);
create index idx_agendamento_profissional on agendamento(profissional_id);
create index idx_agendamento_status on agendamento(status);
-- create index idx_agendamento_conflito on agendamento(profissional_id, data_hora, status);

-- comentários nas tabelas
alter table produto comment = 'cadastro de produtos disponíveis para venda';
alter table movimentacao_estoque comment = 'histórico de todas as movimentações de estoque';
alter table agendamento comment = 'registro de agendamentos de serviços';