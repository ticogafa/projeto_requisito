import React, { useState, useEffect } from 'react';
import MainService from '@/services/MainService';
import { useServicosOferecidos } from '@/hooks/useServicosOferecidos';
import { useLoadingStore } from '@/store/useLoadingStore';
import { toast } from 'react-toastify';
import { AxiosError } from 'axios';
import { ProfissionalInterface } from '@/interfaces/ProfissionaisInterfaces';

interface EditProfessionalModalProps {
  visible: boolean;
  profissional: ProfissionalInterface | null;
  closeModal: () => void;
  onSuccess: () => void;
}

export default function EditProfessionalModal({ visible, profissional, closeModal, onSuccess }: EditProfessionalModalProps) {
  const { data: servicos } = useServicosOferecidos();
  const { setLoading } = useLoadingStore();
  const mainService = MainService.getInstance();

  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [telefone, setTelefone] = useState('');
  const [inicioJornada, setInicioJornada] = useState('08:00:00');
  const [fimJornada, setFimJornada] = useState('18:00:00');
  const [servicosSelecionados, setServicosSelecionados] = useState<number[]>([]);

  useEffect(() => {
    if (visible && profissional) {
      setNome(profissional.nome);
      setEmail(profissional.email.value);
      setTelefone(profissional.telefone.value);
      setInicioJornada(profissional.agenda?.inicioJornada || '08:00:00');
      setFimJornada(profissional.agenda?.fimJornada || '18:00:00');

      if (profissional.servicoOferecidoIds) {
        setServicosSelecionados(profissional.servicoOferecidoIds.map(s => s.valor));
      }
    }
  }, [visible, profissional]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!profissional) return;
    setLoading(true);

    const payload = {
      id: profissional.id.valor,
      nome,
      email,
      telefone,
      cpf: profissional.cpf.value,
      senioridade: profissional.senioridade,
      ativo: profissional.ativo,
      agenda: { inicioJornada, fimJornada },
      servicoOferecidoIds: servicosSelecionados.map(id => ({ valor: id }))
    };

    mainService.atualizarProfissional(
      profissional.id.valor,
      payload,
      () => {
        toast.success('Profissional atualizado com sucesso!');
        onSuccess();
        closeModal();
      },
      (error: AxiosError) => {
        const errorData = error.response?.data as { message?: string } | undefined;
        const message = errorData?.message || 'Erro ao atualizar profissional';
        toast.error(message);
      },
      () => setLoading(false)
    );
  };

  const toggleServico = (id: number) => {
    setServicosSelecionados(prev =>
      prev.includes(id) ? prev.filter(s => s !== id) : [...prev, id]
    );
  };

  if (!visible || !profissional) return null;

  return (
    <div className="fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4" onClick={closeModal}>
      <div className="bg-dark-800 rounded-2xl p-8 max-w-2xl w-full border border-dark-600 max-h-[90vh] overflow-y-auto" onClick={e => e.stopPropagation()}>
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-2xl font-bold flex items-center gap-2">
            <span className="material-icons text-primary">edit</span> Editar Profissional
          </h3>
          <button onClick={closeModal} className="text-gray-400 hover:text-white"><span className="material-icons">close</span></button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">Nome</label>
              <input required type="text" value={nome} onChange={e => setNome(e.target.value)} className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Telefone</label>
              <input required type="tel" value={telefone} onChange={e => setTelefone(e.target.value)} className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Email</label>
            <input required type="email" value={email} onChange={e => setEmail(e.target.value)} className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" />
          </div>

          <div className="bg-blue-900/20 p-3 rounded-lg border border-blue-900/50">
            <h4 className="text-sm font-bold text-blue-400 mb-2 flex gap-2 items-center"><span className="material-icons text-sm">schedule</span> Jornada de Trabalho</h4>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-xs text-gray-400 mb-1">Início</label>
                <input type="time" value={inicioJornada} onChange={e => setInicioJornada(e.target.value)} className="bg-dark-900 border border-dark-600 rounded px-2 py-1 text-white w-full" />
              </div>
              <div>
                <label className="block text-xs text-gray-400 mb-1">Fim</label>
                <input type="time" value={fimJornada} onChange={e => setFimJornada(e.target.value)} className="bg-dark-900 border border-dark-600 rounded px-2 py-1 text-white w-full" />
              </div>
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium mb-2">Serviços que realiza:</label>
            <div className="grid grid-cols-2 gap-2 max-h-32 overflow-y-auto p-2 bg-dark-700 rounded-lg">
              {servicos.map(servico => (
                <label key={servico.id} className="flex items-center gap-2 cursor-pointer hover:bg-dark-600 p-1 rounded">
                  <input type="checkbox" checked={servicosSelecionados.includes(servico.id)} onChange={() => toggleServico(servico.id)} className="accent-primary" />
                  <span className="text-sm">{servico.nome}</span>
                </label>
              ))}
            </div>
          </div>
          <button type="submit" className="w-full bg-primary hover:bg-orange-600 text-white font-bold py-3 rounded-lg mt-4 transition">Salvar Alterações</button>
        </form>
      </div>
    </div>
  );
}
