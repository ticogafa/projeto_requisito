package com.cesarschool.barbearia.dominio.principal.cliente;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;

public class ClienteServico {

    private final ClienteRepositorio repository;

    public ClienteServico(ClienteRepositorio repository) {
        Validacoes.validarObjetoObrigatorio(repository, "O repositório");
        this.repository = repository;
    }

    /**
     * Cria um novo cliente.
     * Regras de negócio:
     * - O CPF deve ser único
     * - O email deve ser único
     * - O telefone deve ser único
     */
    public Cliente criarCliente(Cliente request) {
        Validacoes.validarObjetoObrigatorio(request, "O cliente");
        Validacoes.validarObjetoObrigatorio(request.getCpf(), "O CPF");
        Validacoes.validarObjetoObrigatorio(request.getEmail(), "O email");
        Validacoes.validarObjetoObrigatorio(request.getTelefone(), "O telefone");
        
        if (repository.buscarPorCpf(request.getCpf().getValue()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado: " + request.getCpf().getValue());
        }
        if (repository.buscarPorEmail(request.getEmail().getValue()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado: " + request.getEmail().getValue());
        }
        if (repository.buscarPorTelefone(request.getTelefone().getValue()).isPresent()) {
            throw new IllegalArgumentException("Telefone já cadastrado: " + request.getTelefone().getValue());
        }
        
        return repository.salvar(request);
    }

    public Cliente buscarPorId(ClienteId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do cliente");
        return repository.buscarPorId(id.getValor())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Cliente não encontrado com ID: " + id
        ));
    }

    /**
     * Atualiza um cliente existente.
     */
    public Cliente atualizarCliente(ClienteId id, Cliente request) {
        Validacoes.validarObjetoObrigatorio(id, "ID do cliente");
        Validacoes.validarObjetoObrigatorio(request, "O cliente");
        
        Cliente cliente = buscarPorId(id);
        
        // Atualiza campos se fornecidos
        if (request.getNome() != null) {
            cliente.setNome(request.getNome());
        }
        if (request.getEmail() != null) {
            // Verifica se o email já está em uso por outro cliente
            repository.buscarPorEmail(request.getEmail().getValue()).ifPresent(c -> {
                if (!c.getId().equals(cliente.getId())) {
                    throw new IllegalArgumentException("Email já cadastrado por outro cliente");
                }
            });
            cliente.setEmail(request.getEmail());
        }
        if (request.getTelefone() != null) {
            // Verifica se o telefone já está em uso por outro cliente
            repository.buscarPorTelefone(request.getTelefone().getValue()).ifPresent(c -> {
                if (!c.getId().equals(cliente.getId())) {
                    throw new IllegalArgumentException("Telefone já cadastrado por outro cliente");
                }
            });
            cliente.setTelefone(request.getTelefone());
        }
        
        return repository.salvar(cliente);
    }

    public List<Cliente> listarClientes() {
        return repository.listarTodos();
    }

    /**
     * Adiciona pontos de fidelidade a um cliente.
     */
    public Cliente adicionarPontos(ClienteId id, int pontos) {
        Validacoes.validarObjetoObrigatorio(id, "ID do cliente");
        Validacoes.validarInteiroPositivo(pontos, "Pontos a adicionar");
        
        Cliente cliente = buscarPorId(id);
        cliente.adicionarPontos(pontos);
        return repository.salvar(cliente);
    }

    /**
     * Usa pontos de fidelidade de um cliente.
     * Regra de negócio: cliente deve ter pontos suficientes.
     */
    public Cliente usarPontos(ClienteId id, int pontos) {
        Validacoes.validarObjetoObrigatorio(id, "ID do cliente");
        Validacoes.validarInteiroPositivo(pontos, "Pontos a usar");
        
        Cliente cliente = buscarPorId(id);
        cliente.usarPontos(pontos);
        return repository.salvar(cliente);
    }

    public void deletarCliente(ClienteId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do cliente");
        Cliente cliente = buscarPorId(id);
        repository.remover(cliente.getId().getValor());
    }
}
