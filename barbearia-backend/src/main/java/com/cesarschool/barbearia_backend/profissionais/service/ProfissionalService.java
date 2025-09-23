package com.cesarschool.barbearia_backend.profissionais.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.common.constants.ErrorMessages;
import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
import com.cesarschool.barbearia_backend.common.exceptions.DuplicateException;
import com.cesarschool.barbearia_backend.common.exceptions.NotFoundException;
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
import com.cesarschool.barbearia_backend.profissionais.model.ProfissionalServico;
import com.cesarschool.barbearia_backend.profissionais.repository.HorarioTrabalhoRepository;
import com.cesarschool.barbearia_backend.profissionais.repository.ProfissionalRepository;
import com.cesarschool.barbearia_backend.profissionais.mapper.ProfissionalMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository repository;
    private final HorarioTrabalhoRepository horarioRepository;



    /**
     * Busca o horário de trabalho de um profissional para um determinado dia da semana.
     *
     * @param profissionalId o identificador do profissional.
     * @param diaSemana o dia da semana para o qual se deseja buscar o horário de trabalho.
     * @return uma resposta contendo o horário de trabalho do profissional para o dia especificado.
     * @throws IllegalArgumentException se o profissional não possuir horário de trabalho cadastrado para o dia informado.
     */
    public HorarioTrabalhoResponse buscarHorarioDoDia(Integer profissionalId, DiaSemana diaSemana) {
        HorarioTrabalho horarioTrabalho = repository.findHorarioTrabalhoByProfissionalAndDiaSemana(profissionalId, diaSemana).orElseThrow(
            () -> new IllegalArgumentException(
                String.format(
                    "O profissional %s não possui horário de trabalho cadastrado para %s.",
                    buscarEntidadePorId(profissionalId).getNome(), diaSemana.getNome()
                )
            )
        );
        return HorarioTrabalhoMapper.toResponse(horarioTrabalho);
    }

    public List<ProfissionalServico> buscarProfissionaisPorServico(Integer servicoId){
        return repository.buscarProfissionaisPorServico(servicoId);
    }

    // CREATE
    public ProfissionalResponse criarProfissional(CriarProfissionalRequest request) {
        Profissional profissional = ProfissionalMapper.toEntity(request);
        Profissional profissionalSalvo = repository.save(profissional);
        return ProfissionalMapper.toResponse(profissionalSalvo);
    }

    // READ
    public Profissional buscarEntidadePorId(Integer id) {
        return repository.findById(id).orElseThrow(
            () -> new NotFoundException(ErrorMessages.ENTIDADE_NAO_ENCONTRADA.format("Profissional"))
        );
    }

    // READ
    public ProfissionalResponse buscarPorId(Integer id) {
        Profissional profissional = buscarEntidadePorId(id);
        return ProfissionalMapper.toResponse(profissional);
    }

    // UPDATE
    public ProfissionalResponse atualizarProfissional(Integer id, AtualizarProfissionalRequest request) {
        Profissional profissional = buscarEntidadePorId(id);

        ProfissionalMapper.updateEntityFromDto(request, profissional);
        Profissional profissionalAtualizado = repository.save(profissional);
        return ProfissionalMapper.toResponse(profissionalAtualizado);
    }

    // LIST
    public List<ProfissionalResponse> listarProfissionais() {
        return repository
            .findAll()
            .stream()
            .map(ProfissionalMapper::toResponse)
            .toList();
        }

    // DELETE
    public void deletarProfissional(Integer id) {
        Profissional profissional = buscarEntidadePorId(id);
        repository.delete(profissional);
    }

    // ------------------ MÉTODOS PARA HORÁRIOS DE TRABALHO ------------------

    // CREATE
    /**
     * Regras de negócio para criar um horário de trabalho:
     * 1. O profissional deve existir.
     * 2. Não pode haver horários duplicados para o mesmo dia da semana para o mesmo profissional.
     */
    public HorarioTrabalhoResponse criarHorarioTrabalho(Integer profissionalId, CriarHorarioTrabalhoRequest request) {
        Profissional profissional = buscarEntidadePorId(profissionalId);
        
        horarioRepository.buscarHorariosPorProfissionalId(profissionalId)
            .stream()
            .filter(horario -> horario.getDiaSemana().equals(request.getDiaSemana()))
            .findFirst().ifPresent(horario->{
                throw new DuplicateException(ErrorMessages.DIA_SEMANA_JA_CADASTRADO.format(request.getDiaSemana().getNome(), profissional.getNome()));
            });

        HorarioTrabalho horario = HorarioTrabalhoMapper.toEntity(request);
        horario.setProfissional(profissional);
        
        HorarioTrabalho horarioSalvo = horarioRepository.save(horario);

        return HorarioTrabalhoMapper.toResponse(horarioSalvo);
    }

    // READ
    public ListarHorariosTrabalhoResponse listarHorariosPorProfissional(Integer profissionalId) {
        buscarEntidadePorId(profissionalId);
        List<HorarioTrabalho> horarios = horarioRepository.buscarHorariosPorProfissionalId(profissionalId);
        
        List<HorarioTrabalhoResponse> horariosResponse = horarios
            .stream()
            .map(HorarioTrabalhoMapper::toResponse)
            .toList();
        
        ListarHorariosTrabalhoResponse response = new ListarHorariosTrabalhoResponse();
        response.setHorarios(horariosResponse);
        response.setTotal(horarios.size());
        
        return response;
    }

    // UPDATE
    /**
     * Regras de negócio para atualizar um horário de trabalho:
     * 1. O profissional deve existir.
     * 2. O horário de trabalho deve existir.
     * 3. O horário de trabalho deve pertencer ao profissional informado.
     * 4. O campo 'diaSemana' não pode ser alterado.
     * 5. Os demais campos do horário podem ser atualizados.
     */
    public HorarioTrabalhoResponse atualizarHorarioTrabalho(Integer profissionalId, Integer horarioId, AtualizarHorarioTrabalhoRequest request) {
        buscarEntidadePorId(profissionalId);
        
        HorarioTrabalho horario = horarioRepository.findById(horarioId)
            .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTIDADE_NAO_ENCONTRADA.format("Horário de trabalho")));
        
        if (!horario.getProfissional().getId().equals(profissionalId)) {
            throw new IllegalArgumentException(ErrorMessages.HORARIO_NAO_PERTENCE_A_PROFISSIONAL.format());
        }

        if(horario.getDiaSemana() != request.getDiaSemana()) {
            throw new IllegalArgumentException(ErrorMessages.CAMPO_NAO_PODE_SER_ALTERADO.format("Dia da semana"));
        }
        
        HorarioTrabalhoMapper.updateEntityFromDto(request, horario);
        HorarioTrabalho horarioAtualizado = horarioRepository.save(horario);
        return HorarioTrabalhoMapper.toResponse(horarioAtualizado);
    }

    // DELETE
    /**
     * Regras de negócio para deletar um horário de trabalho:
     * 1. O profissional deve existir.
     * 2. O horário de trabalho deve existir.
     * 3. O horário de trabalho deve pertencer ao profissional informado.
     */
    public void deletarHorarioTrabalho(Integer profissionalId, Integer horarioId) {
        buscarPorId(profissionalId);
        HorarioTrabalho horario = horarioRepository.findById(horarioId)
            .orElseThrow(() -> new NotFoundException(ErrorMessages.HORARIO_NAO_ENCONTRADO.format()));
        
        if (!horario.getProfissional().getId().equals(profissionalId)) {
            throw new IllegalArgumentException(ErrorMessages.HORARIO_NAO_PERTENCE_A_PROFISSIONAL.format());
        }
        horarioRepository.delete(horario);
    }
}
