package com.cesarschool.cucumber.fidelidade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteRepositorio;

public class ClienteRepositorioMock implements ClienteRepositorio {

    private final Map<Integer, Cliente> storage = new HashMap<>();
    private int sequence = 1;

    public void limpar() {
        storage.clear();
        sequence = 1;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        Integer id = cliente.getId() != null ? cliente.getId().getValor() : null;
        if (id == null) {
            id = sequence++;
            cliente.setId(new com.cesarschool.barbearia.dominio.principal.cliente.ClienteId(id));
        }
        storage.put(id, cliente);
        return cliente;
    }

    @Override
    public Cliente buscarPorId(Integer id) {
        return storage.get(id);
    }

    @Override
    public List<Cliente> listarTodos() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void remover(Integer id) {
        storage.remove(id);
    }

    @Override
    public Optional<Cliente> buscarPorNome(String nome) {
        return storage.values().stream()
            .filter(c -> c.getNome().equalsIgnoreCase(nome))
            .findFirst();
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return storage.values().stream()
            .filter(c -> c.getEmail().getValue().equalsIgnoreCase(email))
            .findFirst();
    }

    @Override
    public Optional<Cliente> buscarPorTelefone(String telefone) {
        return storage.values().stream()
            .filter(c -> c.getTelefone().getValue().equalsIgnoreCase(telefone))
            .findFirst();
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return storage.values().stream()
            .filter(c -> c.getCpf().getValue().equalsIgnoreCase(cpf))
            .findFirst();
    }
}
