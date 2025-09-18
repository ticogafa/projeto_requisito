package com.cesarschool.barbearia_backend.profissionais.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.AtualizarHorarioTrabalhoRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.CriarHorarioTrabalhoRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.HorarioTrabalhoResponse;
import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.ListarHorariosTrabalhoResponse;
import com.cesarschool.barbearia_backend.profissionais.dto.ProfissionalDTOs.AtualizarProfissionalRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.ProfissionalDTOs.CriarProfissionalRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.ProfissionalDTOs.ProfissionalResponse;
import com.cesarschool.barbearia_backend.profissionais.mapper.HorarioTrabalhoMapper;
import com.cesarschool.barbearia_backend.profissionais.model.HorarioTrabalho;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.repository.HorarioTrabalhoRepository;
import com.cesarschool.barbearia_backend.profissionais.repository.ProfissionalRepository;
import com.cesarschool.barbearia_backend.profissionais.mapper.ProfissionalMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfissionalService {

    private static final String PROFISSIONAL_NAO_ENCONTRADO = "Profissional não encontrado";
    private static final String HORARIO_NAO_ENCONTRADO = "Horário de trabalho não encontrado";
    
    private final ProfissionalRepository repository;
    private final HorarioTrabalhoRepository horarioRepository;
    private final HorarioTrabalhoMapper horarioMapper;

    public Optional<Profissional> findById(Integer id){
        return repository.findById(id);
    }

    public List<Profissional> findAll(){
        return repository.findAll();
    }

    public Profissional save(Profissional profissional){
        return repository.save(profissional);
    }

    public void delete(Profissional profissional){
        repository.delete(profissional);
    }

    public ProfissionalResponse criarProfissional(CriarProfissionalRequest request) {
        Profissional profissional = ProfissionalMapper.toEntity(request);
        
        Profissional profissionalSalvo = save(profissional);
        
        return ProfissionalMapper.toResponse(profissionalSalvo);
    }

    public ProfissionalResponse buscarPorId(Integer id) {
        Profissional profissional = findById(id)
            .orElseThrow(() -> new IllegalArgumentException(PROFISSIONAL_NAO_ENCONTRADO));
        return ProfissionalMapper.toResponse(profissional);
    }

    public ProfissionalResponse atualizarProfissional(Integer id, AtualizarProfissionalRequest request) {
        Profissional profissional = findById(id)
            .orElseThrow(() -> new IllegalArgumentException(PROFISSIONAL_NAO_ENCONTRADO));
        
        ProfissionalMapper.updateEntityFromDto(request, profissional);
        
        Profissional profissionalAtualizado = save(profissional);
        
        return ProfissionalMapper.toResponse(profissionalAtualizado);
    }

    public List<ProfissionalResponse> listarProfissionais() {
        return repository
            .findAll()
            .stream()
            .map(ProfissionalMapper::toResponse)
            .toList();
        }

    public void deletarProfissional(Integer id) {
        Profissional profissional = findById(id)
            .orElseThrow(() -> new IllegalArgumentException(PROFISSIONAL_NAO_ENCONTRADO));
        delete(profissional);
    }

    // ===============================
    // Métodos para Horários de Trabalho
    // ===============================

    public HorarioTrabalhoResponse criarHorarioTrabalho(Integer profissionalId, CriarHorarioTrabalhoRequest request) {
        // Verificar se o profissional existe
        Profissional profissional = findById(profissionalId)
            .orElseThrow(() -> new IllegalArgumentException(PROFISSIONAL_NAO_ENCONTRADO));
        

        horarioRepository.findByProfissionalId(profissionalId)
            .stream()
            .filter(horario -> horario.getDiaSemana().equals(request.getDiaSemana()))
            .findFirst().ifPresent(horario->{
                throw new IllegalArgumentException("Já existe um horário cadastrado para este dia da semana para " + profissional.getNome());
            });

        
        // Converter DTO para entidade
        HorarioTrabalho horario = horarioMapper.toEntity(request);
        horario.setProfissional(profissional);
        
        // Salvar horário
        HorarioTrabalho horarioSalvo = horarioRepository.save(horario);
        
        // Converter para DTO de resposta
        return horarioMapper.toResponse(horarioSalvo);
    }

    public ListarHorariosTrabalhoResponse listarHorariosPorProfissional(Integer profissionalId) {
        // Verificar se o profissional existe
        findById(profissionalId)
            .orElseThrow(() -> new IllegalArgumentException(PROFISSIONAL_NAO_ENCONTRADO));
        
        // Buscar horários do profissional
        List<HorarioTrabalho> horarios = horarioRepository.findByProfissionalId(profissionalId);
        
        List<HorarioTrabalhoResponse> horariosResponse = horarios
            .stream()
            .map(horarioMapper::toResponse)
            .toList();
        
        ListarHorariosTrabalhoResponse response = new ListarHorariosTrabalhoResponse();
        response.setHorarios(horariosResponse);
        response.setTotalElements(horarios.size());
        
        return response;
    }

    public HorarioTrabalhoResponse atualizarHorarioTrabalho(Integer profissionalId, Integer horarioId, AtualizarHorarioTrabalhoRequest request) {
        // Verificar se o profissional existe
        findById(profissionalId)
            .orElseThrow(() -> new IllegalArgumentException(PROFISSIONAL_NAO_ENCONTRADO));
        
        // Buscar horário específico
        HorarioTrabalho horario = horarioRepository.findById(horarioId)
            .orElseThrow(() -> new IllegalArgumentException(HORARIO_NAO_ENCONTRADO));
        
        // Verificar se o horário pertence ao profissional
        if (!horario.getProfissional().getId().equals(profissionalId)) {
            throw new IllegalArgumentException("Horário não pertence ao profissional informado");
        }
        
        // Atualizar campos usando mapper
        horarioMapper.updateEntityFromDto(request, horario);
        
        // Salvar alterações
        HorarioTrabalho horarioAtualizado = horarioRepository.save(horario);
        
        return horarioMapper.toResponse(horarioAtualizado);
    }

    public void deletarHorarioTrabalho(Integer profissionalId, Integer horarioId) {
        // Verificar se o profissional existe
        findById(profissionalId)
            .orElseThrow(() -> new IllegalArgumentException(PROFISSIONAL_NAO_ENCONTRADO));
        
        // Buscar horário específico
        HorarioTrabalho horario = horarioRepository.findById(horarioId)
            .orElseThrow(() -> new IllegalArgumentException(HORARIO_NAO_ENCONTRADO));
        
        // Verificar se o horário pertence ao profissional
        if (!horario.getProfissional().getId().equals(profissionalId)) {
            throw new IllegalArgumentException("Horário não pertence ao profissional informado");
        }
        
        horarioRepository.delete(horario);
    }
}
