package com.cesarschool.barbearia_backend.profissionais.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.common.exceptions.DuplicateException;
import com.cesarschool.barbearia_backend.profissionais.dto.ServicoDTOs.AtualizarServicoRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.ServicoDTOs.CriarServicoRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.ServicoDTOs.ServicoResponse;
import com.cesarschool.barbearia_backend.profissionais.mapper.ServicoMapper;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;
import com.cesarschool.barbearia_backend.profissionais.repository.ServicoOferecidoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServicoOferecidoService {

    private static final String SERVICO_NAO_ENCONTRADO = "Serviço não encontrado";
    
    private final ServicoOferecidoRepository repository;

    public Optional<ServicoOferecido> findById(Integer id){
        return repository.findById(id);
    }

    public Optional<ServicoOferecido> findByNome(String nome){
        return repository.findByNome(nome);
    }

    public List<ServicoOferecido> findAll(){
        return repository.findAll();
    }

    public ServicoOferecido save(ServicoOferecido servico){
        return repository.save(servico);
    }

    public void delete(ServicoOferecido servico){
        repository.delete(servico);
    }

    public ServicoResponse cadastrarServico(CriarServicoRequest servico) {
        if (findByNome(servico.getNome()).isPresent()) {
            throw new DuplicateException("Serviço já cadastrado");
        }
        var servicoEntity = save(ServicoMapper.toEntity(servico));
        ServicoMapper.toResponse(servicoEntity);
        return ServicoMapper.toResponse(servicoEntity);
    }

    public ServicoResponse buscarPorId(Integer id) {
        ServicoOferecido servico = buscarEntidadePorId(id);
        return ServicoMapper.toResponse(servico);
    }

    public ServicoOferecido buscarEntidadePorId(Integer id) {
        return  findById(id)
            .orElseThrow(() -> new IllegalArgumentException(SERVICO_NAO_ENCONTRADO));
    }

    public ServicoResponse atualizarServico(Integer id, AtualizarServicoRequest request) {
        ServicoOferecido servico = findById(id)
            .orElseThrow(() -> new IllegalArgumentException(SERVICO_NAO_ENCONTRADO));
        
        ServicoMapper.updateEntityFromDto(request, servico);
        
        ServicoOferecido servicoAtualizado = save(servico);
        
        return ServicoMapper.toResponse(servicoAtualizado);
    }

    public List<ServicoResponse> listarServicos() {
        return repository
            .findAll()
            .stream()
            .map(ServicoMapper::toResponse)
            .toList();
        
    }

    public void deletarServico(Integer id) {
        ServicoOferecido servico = findById(id)
            .orElseThrow(() -> new IllegalArgumentException(SERVICO_NAO_ENCONTRADO));
        delete(servico);
    }
}
