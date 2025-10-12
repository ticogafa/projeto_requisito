package com.cesarschool.barbearia_backend.dominio_profissionais.dominio.profissionais;

public interface ProfissionalRepositorio  extends Repositorio{
    void salvar(Profissional profissional);
    Profissional obter(ProfissionalId id);
    void deletar(ProfissionalId id);
    Optional<HorarioTrabalho> buscarHorarioDiaSemana(Integer id, DiaSemana diaSemana);
    List<Profissional> buscarProfissionaisPorServico(Integer servicoId);

}