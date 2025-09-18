package com.cesarschool.barbearia_backend.marketing.service;

import java.util.List;


import org.springframework.stereotype.Service;

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

    public ClienteResponse criarCliente(CriarClienteRequest request) {
        Cliente cliente = ClienteMapper.toEntity(request);
        Cliente clienteSalvo = repository.save(cliente);
        return ClienteMapper.toResponse(clienteSalvo);
    }

    public ClienteResponse buscarPorId(Integer id) {
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(CLIENTE_NAO_ENCONTRADO));
        return ClienteMapper.toResponse(cliente);
    }

    public ClienteResponse atualizarCliente(Integer id, AtualizarClienteRequest request) {
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(CLIENTE_NAO_ENCONTRADO));
        
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
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(CLIENTE_NAO_ENCONTRADO));
        
        cliente.adicionarPontos(pontos);
        Cliente clienteAtualizado = repository.save(cliente);
        
        return ClienteMapper.toResponse(clienteAtualizado);
    }

    public ClienteResponse usarPontos(Integer id, int pontos) {
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(CLIENTE_NAO_ENCONTRADO));
        
        cliente.usarPontos(pontos);
        Cliente clienteAtualizado = repository.save(cliente);
        
        return ClienteMapper.toResponse(clienteAtualizado);
    }

    public void deletarCliente(Integer id) {
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(CLIENTE_NAO_ENCONTRADO));
        repository.delete(cliente);
    }
}
