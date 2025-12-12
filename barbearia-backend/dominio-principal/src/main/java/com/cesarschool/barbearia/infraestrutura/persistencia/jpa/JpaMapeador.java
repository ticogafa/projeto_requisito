package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.util.ArrayList;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.stereotype.Component;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ExecucaoAtendimentoId;
import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoId;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.produto.Produto;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.MovimentacaoEstoque;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.MovimentacaoEstoqueId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

/**
 * Mapeador centralizado para conversões entre entidades JPA e entidades de domínio.
 * Estende ModelMapper e configura conversores personalizados para Value Objects.
 * Seguindo o padrão do projeto SGB.
 */
@Component
class JpaMapeador extends ModelMapper {
    
    JpaMapeador() {
        configurarModelMapper();
        configurarConversores();
    }
    
    private void configurarModelMapper() {
        var configuracao = getConfiguration();
        configuracao.setFieldMatchingEnabled(true);
        configuracao.setFieldAccessLevel(AccessLevel.PRIVATE);
    }
    
    private void configurarConversores() {
        configurarConversoresProduto();
        configurarConversoresMovimentacaoEstoque();
        configurarConversoresAgendamento();
        configurarConversoresProfissional();
        configurarConversoresServicos();
        configurarConversoresCliente();
        configurarConversoresIds();
        configurarConversoresExecucaoAtendimento();
    }
    
    private void configurarConversoresExecucaoAtendimento() {
        // String -> ExecucaoAtendimentoId
        addConverter(new AbstractConverter<String, ExecucaoAtendimentoId>() {
            @Override
            public ExecucaoAtendimentoId convert(String source) {
                return source != null ? new ExecucaoAtendimentoId(java.util.UUID.fromString(source)) : null;
            }
        });
    }

    // ==================== CONVERSORES DE PRODUTO ====================
    
    private void configurarConversoresProduto() {
        // ProdutoJpa -> Produto
        addConverter(new AbstractConverter<ProdutoJpa, Produto>() {
            @Override
            public Produto convert(ProdutoJpa source) {
                return new Produto(
                    source.id,
                    source.nome,
                    source.estoque,
                    source.preco,
                    source.estoqueMinimo
                );
            }
        });
        
        // Produto -> ProdutoJpa
        addConverter(new AbstractConverter<Produto, ProdutoJpa>() {
            @Override
            public ProdutoJpa convert(Produto source) {
                var produtoJpa = new ProdutoJpa();
                produtoJpa.id = source.getId();
                produtoJpa.nome = source.getNome();
                produtoJpa.estoque = source.getEstoque();
                produtoJpa.preco = source.getPreco();
                produtoJpa.estoqueMinimo = source.getEstoqueMinimo();
                return produtoJpa;
            }
        });
    }
    
    // ==================== CONVERSORES DE MOVIMENTAÇÃO ESTOQUE ====================
    
    private void configurarConversoresMovimentacaoEstoque() {
        // MovimentacaoEstoqueJpa -> MovimentacaoEstoque
        addConverter(new AbstractConverter<MovimentacaoEstoqueJpa, MovimentacaoEstoque>() {
            @Override
            public MovimentacaoEstoque convert(MovimentacaoEstoqueJpa source) {
                var id = source.id != null ? new MovimentacaoEstoqueId(source.id) : null;
                var produtoId = new ProdutoId(source.produto.id);
                
                return new MovimentacaoEstoque(
                    id,
                    produtoId,
                    source.nomeProduto,
                    source.tipo,
                    source.quantidade,
                    source.estoqueAnterior,
                    source.estoqueAtual,
                    source.dataHora,
                    source.observacao,
                    source.usuarioResponsavel
                );
            }
        });
        
        // MovimentacaoEstoque -> MovimentacaoEstoqueJpa
        addConverter(new AbstractConverter<MovimentacaoEstoque, MovimentacaoEstoqueJpa>() {
            @Override
            public MovimentacaoEstoqueJpa convert(MovimentacaoEstoque source) {
                var movimentacaoJpa = new MovimentacaoEstoqueJpa();
                movimentacaoJpa.id = source.getId() != null ? source.getId().getValor() : null;
                
                // Busca o produto pelo ID (precisa de repositório)
                var produtoJpa = new ProdutoJpa();
                produtoJpa.id = source.getProdutoId().getValor();
                movimentacaoJpa.produto = produtoJpa;
                
                movimentacaoJpa.nomeProduto = source.getNomeProduto();
                movimentacaoJpa.tipo = source.getTipo();
                movimentacaoJpa.quantidade = source.getQuantidade();
                movimentacaoJpa.estoqueAnterior = source.getEstoqueAnterior();
                movimentacaoJpa.estoqueAtual = source.getEstoqueAtual();
                movimentacaoJpa.dataHora = source.getDataHora();
                movimentacaoJpa.observacao = source.getObservacao();
                movimentacaoJpa.usuarioResponsavel = source.getUsuarioResponsavel();
                
                return movimentacaoJpa;
            }
        });
    }
    
    // ==================== CONVERSORES DE AGENDAMENTO ====================
    
    private void configurarConversoresAgendamento() {
        // AgendamentoJpa -> Agendamento
        addConverter(new AbstractConverter<AgendamentoJpa, Agendamento>() {
            @Override
            public Agendamento convert(AgendamentoJpa source) {
                var id = source.id != null ? new AgendamentoId(source.id) : null;
                var clienteId = new ClienteId(source.clienteId);
                var profissionalId = source.profissionalId != null ? new ProfissionalId(source.profissionalId) : null;
                var servicoId = new ServicoOferecidoId(source.servicoId);
                
                return Agendamento.builderCompleto()
                    .id(id)
                    .dataHora(source.dataHora)
                    .status(source.status)
                    .clienteId(clienteId)
                    .profissionalId(profissionalId)
                    .servicoId(servicoId)
                    .observacoes(source.observacoes)
                    .build();
            }
        });
        
        // Agendamento -> AgendamentoJpa
        addConverter(new AbstractConverter<Agendamento, AgendamentoJpa>() {
            @Override
            public AgendamentoJpa convert(Agendamento source) {
                var agendamentoJpa = new AgendamentoJpa();
                agendamentoJpa.id = source.getId() != null ? source.getId().getValor() : null;
                agendamentoJpa.dataHora = source.getDataHora();
                agendamentoJpa.status = source.getStatus();
                agendamentoJpa.clienteId = source.getClienteId().getValor();
                agendamentoJpa.profissionalId = source.getProfissionalId() != null ? 
                    source.getProfissionalId().getValor() : null;
                agendamentoJpa.servicoId = source.getServicoId().getValor();
                agendamentoJpa.observacoes = source.getObservacoes();
                return agendamentoJpa;
            }
        });
    }
    
    // ==================== CONVERSORES DE SERVICOS ====================

    public void configurarConversoresServicos() {
        
        addConverter(new AbstractConverter<ServicoOferecidoJpa, ServicoOferecido>() {
            @Override
            public ServicoOferecido convert(ServicoOferecidoJpa source) {
                var id = source.getId() != null ? new ServicoOferecidoId(source.getId()) : null;
                
                return new ServicoOferecido(
                    id,
                    source.getNome(),
                    source.getPreco(),
                    source.getDescricao(),
                    source.getDuracaoMinutos()
                );
            }
        });
        
        addConverter(new AbstractConverter<ServicoOferecido, ServicoOferecidoJpa>() {
            @Override
            public ServicoOferecidoJpa convert(ServicoOferecido source) {
                var servicoJpa = new ServicoOferecidoJpa();
                servicoJpa.setId(source.getId() != null ? source.getId().getValor() : null);
                servicoJpa.setNome(source.getNome());
                servicoJpa.setPreco(source.getPreco());
                servicoJpa.setDescricao(source.getDescricao());
                servicoJpa.setDuracaoMinutos(source.getDuracaoMinutos());
                return servicoJpa;
            }
        });
    }

    // ==================== CONVERSORES DE PROFISSIONAL ====================
    
    private void configurarConversoresProfissional() {
        // ProfissionalJpa -> Profissional
        addConverter(new AbstractConverter<ProfissionalJpa, com.cesarschool.barbearia.dominio.principal.profissional.Profissional>() {
            @Override
            public com.cesarschool.barbearia.dominio.principal.profissional.Profissional convert(ProfissionalJpa source) {
                var id = source.getId() != null ? new ProfissionalId(source.getId()) : null;
                var email = new com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email(source.getEmail());
                var cpf = new com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf(source.getCpf());
                var telefone = new com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone(source.getTelefone());
                var agenda = new com.cesarschool.barbearia.dominio.principal.profissional.Agenda();
                
                return new com.cesarschool.barbearia.dominio.principal.profissional.Profissional(
                    id,
                    source.getNome(),
                    email,
                    cpf,
                    telefone,
                    agenda,
                    new ArrayList<>(),
                    source.getSenioridade(),
                    source.isAtivo(),
                    source.getMotivoInatividade()
                );
            }
        });
        
        // Profissional -> ProfissionalJpa
        addConverter(new AbstractConverter<com.cesarschool.barbearia.dominio.principal.profissional.Profissional, ProfissionalJpa>() {
            @Override
            public ProfissionalJpa convert(com.cesarschool.barbearia.dominio.principal.profissional.Profissional source) {
                return ProfissionalJpa.builder()
                    .id(source.getId() != null ? source.getId().getValor() : null)
                    .nome(source.getNome())
                    .email(source.getEmail().getValue())
                    .cpf(source.getCpf().getValue())
                    .telefone(source.getTelefone().getValue())
                    .senioridade(source.getSenioridade())
                    .ativo(source.isAtivo())
                    .motivoInatividade(source.getMotivoInatividade())
                    .build();
            }
        });
    }

    // ==================== CONVERSORES DE CLIENTE ====================
    
    private void configurarConversoresCliente() {
        // ClienteJpa -> Cliente
        addConverter(new AbstractConverter<ClienteJpa, com.cesarschool.barbearia.dominio.principal.cliente.Cliente>() {
            @Override
            public com.cesarschool.barbearia.dominio.principal.cliente.Cliente convert(ClienteJpa source) {
                var id = source.getId() != null ? new ClienteId(source.getId()) : null;
                var email = new com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email(source.getEmail());
                var cpf = new com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf(source.getCpf());
                var telefone = new com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone(source.getTelefone());
                
                return new com.cesarschool.barbearia.dominio.principal.cliente.Cliente(
                    id,
                    source.getNome(),
                    email,
                    cpf,
                    telefone,
                    source.getPontos()
                );
            }
        });
        
        // Cliente -> ClienteJpa
        addConverter(new AbstractConverter<com.cesarschool.barbearia.dominio.principal.cliente.Cliente, ClienteJpa>() {
            @Override
            public ClienteJpa convert(com.cesarschool.barbearia.dominio.principal.cliente.Cliente source) {
                return ClienteJpa.builder()
                    .id(source.getId() != null ? source.getId().getValor() : null)
                    .nome(source.getNome())
                    .email(source.getEmail().getValue())
                    .cpf(source.getCpf().getValue())
                    .telefone(source.getTelefone().getValue())
                    .pontos(source.getPontos())
                    .ativo(true)
                    .build();
            }
        });
    }
    
    // ==================== CONVERSORES DE IDs ====================
    
    private void configurarConversoresIds() {
        // Integer -> ProdutoId
        addConverter(new AbstractConverter<Integer, ProdutoId>() {
            @Override
            public ProdutoId convert(Integer source) {
                return new ProdutoId(source);
            }
        });
        
        // Integer -> MovimentacaoEstoqueId
        addConverter(new AbstractConverter<Integer, MovimentacaoEstoqueId>() {
            @Override
            public MovimentacaoEstoqueId convert(Integer source) {
                return new MovimentacaoEstoqueId(source);
            }
        });
        
        // Integer -> AgendamentoId
        addConverter(new AbstractConverter<Integer, AgendamentoId>() {
            @Override
            public AgendamentoId convert(Integer source) {
                return new AgendamentoId(source);
            }
        });
        
        // Integer -> ClienteId
        addConverter(new AbstractConverter<Integer, ClienteId>() {
            @Override
            public ClienteId convert(Integer source) {
                return new ClienteId(source);
            }
        });
        
        // Integer -> ProfissionalId
        addConverter(new AbstractConverter<Integer, ProfissionalId>() {
            @Override
            public ProfissionalId convert(Integer source) {
                return new ProfissionalId(source);
            }
        });
        
        // Integer -> ServicoOferecidoId
        addConverter(new AbstractConverter<Integer, ServicoOferecidoId>() {
            @Override
            public ServicoOferecidoId convert(Integer source) {
                return new ServicoOferecidoId(source);
            }
        });
    }
    
    //

    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        return source != null ? super.map(source, destinationType) : null;
    }
}
