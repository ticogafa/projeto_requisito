package com.cesarschool.cucumber.gestaoAgendamento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteRepositorio;

/**
 * Implementação mock do ClienteRepositorio para testes.
 * Simula persistência em memória sem necessidade de banco de dados.
 */
public class ClienteMockRepositorio implements ClienteRepositorio {
    
    private final Map<Integer, Cliente> clientes = new HashMap<>();
    private final AtomicInteger proximoId = new AtomicInteger(1);
    
    @Override
    public Cliente salvar(Cliente cliente) {
        int id;
        if (cliente.getId() == null || cliente.getId().getValor() == -1) {
            // Criar novo ID para cliente
            id = proximoId.getAndIncrement();
            // Recriar cliente com novo ID
            cliente = new Cliente(
                new ClienteId(id),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getCpf(),
                cliente.getTelefone(),
                cliente.getPontos()
            );
        } else {
            id = cliente.getId().getValor();
        }
        clientes.put(id, cliente);
        return cliente;
    }
    
    @Override
    public Cliente buscarPorId(Integer id) {
        Cliente cliente = clientes.get(id);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + id);
        }
        return cliente;
    }
    
    @Override
    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes.values());
    }
    
    @Override
    public void remover(Integer id) {
        clientes.remove(id);
    }
    
    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return clientes.values().stream()
            .filter(c -> c.getCpf().getValue().equals(cpf))
            .findFirst();
    }
    
    @Override
    public Optional<Cliente> buscarPorNome(String nome) {
        return clientes.values().stream()
            .filter(c -> c.getNome().equalsIgnoreCase(nome))
            .findFirst();
    }
    
    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return clientes.values().stream()
            .filter(c -> c.getEmail().getValue().equalsIgnoreCase(email))
            .findFirst();
    }
    
    @Override
    public Optional<Cliente> buscarPorTelefone(String telefone) {
        return clientes.values().stream()
            .filter(c -> c.getTelefone().getValue().equals(telefone))
            .findFirst();
    }
    
    /**
     * Limpa todos os dados do repositório.
     */
    public void limparDados() {
        clientes.clear();
        proximoId.set(1);
    }
}
