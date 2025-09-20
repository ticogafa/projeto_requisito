package com.cesarschool.barbearia_backend.marketing.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.common.exceptions.NotFoundException;
import com.cesarschool.barbearia_backend.marketing.dto.ClienteDTOs.AtualizarClienteRequest;
import com.cesarschool.barbearia_backend.marketing.dto.ClienteDTOs.ClienteResponse;
import com.cesarschool.barbearia_backend.marketing.dto.ClienteDTOs.CriarClienteRequest;
import com.cesarschool.barbearia_backend.marketing.mapper.ClienteMapper;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.marketing.repository.ClienteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService {

    private static final String CLIENTE_NAO_ENCONTRADO = "Cliente não encontrado";

    // -- • A cada 100 pontos → R$ 10,00 de desconto
    // -- • Quando o cliente troca pontos, gera um voucher

    // ainda não decidi aonde colocar essa regra?
    // -- • Quando o voucher é utilizado, ele é vinculado à venda (campo voucherId)

    private final ClienteRepository repository;

    /**
     * Regras de negócio para criação de cliente:
     * - O CPF deve ser único.
     * - O email deve ser único.
     * - O telefone deve ser único.
     */
    public ClienteResponse criarCliente(CriarClienteRequest request) {
        if(repository.findByCpf(request.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        if(repository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if(repository.findByTelefone(request.getTelefone()).isPresent()) {
            throw new IllegalArgumentException("Telefone já cadastrado");
        }
        Cliente cliente = ClienteMapper.toEntity(request);
        Cliente clienteSalvo = repository.save(cliente);
        return ClienteMapper.toResponse(clienteSalvo);
    }

    public Cliente buscarEntidadePorId(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException(CLIENTE_NAO_ENCONTRADO));
    }

    public ClienteResponse buscarPorId(Integer id) {
        Cliente cliente = buscarEntidadePorId(id);
        return ClienteMapper.toResponse(cliente);
    }


    public ClienteResponse atualizarCliente(Integer id, AtualizarClienteRequest request) {
        Cliente cliente = buscarEntidadePorId(id);

        ClienteMapper.updateEntityFromDto(request, cliente);
        Cliente clienteAtualizado = repository.save(cliente);
        return ClienteMapper.toResponse(clienteAtualizado);
    }

    public List<ClienteResponse> listarClientes() {
        return repository.findAll().
        stream()
        .map(ClienteMapper::toResponse)
        .toList();
    }


    public ClienteResponse adicionarPontos(Integer id, int pontos) {
        Cliente cliente = buscarEntidadePorId(id);

        cliente.adicionarPontos(pontos);
        Cliente clienteAtualizado = repository.save(cliente);
        
        return ClienteMapper.toResponse(clienteAtualizado);
    }

    public ClienteResponse usarPontos(Integer id, int pontos) {
        Cliente cliente = buscarEntidadePorId(id);

        cliente.usarPontos(pontos);
        Cliente clienteAtualizado = repository.save(cliente);
        
        return ClienteMapper.toResponse(clienteAtualizado);
    }

    public void deletarCliente(Integer id) {
        Cliente cliente = buscarEntidadePorId(id);
        repository.delete(cliente);
    }
}
