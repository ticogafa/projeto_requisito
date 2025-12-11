import React, { useState, useEffect } from 'react';
import MainService from '@/services/MainService';
import { useServicosOferecidos } from '@/hooks/useServicosOferecidos';
import { useLoadingStore } from '@/store/useLoadingStore';
import { toast } from 'react-toastify';
import { AxiosError } from 'axios';

interface NewProfessionalModalProps {
  visible: boolean;
  closeModal: () => void;
  onSuccess: () => void;
}

export default function NewProfessionalModal({ visible, closeModal, onSuccess }: NewProfessionalModalProps) {
  const { data: servicos } = useServicosOferecidos();
  const { setLoading } = useLoadingStore();
  const mainService = MainService.getInstance();

  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [cpf, setCpf] = useState('');
  const [telefone, setTelefone] = useState('');
  const [senioridade, setSenioridade] = useState('JUNIOR');
  const [inicioJornada, setInicioJornada] = useState('08:00:00');
  const [fimJornada, setFimJornada] = useState('18:00:00');
  const [servicosSelecionados, setServicosSelecionados] = useState<number[]>([]);

  useEffect(() => {
    if (visible) {
      setNome('');
      setEmail('');
      setCpf('');
      setTelefone('');
      setSenioridade('JUNIOR');
      setServicosSelecionados([]);
    }
  }, [visible]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    const payload = {
      nome,
      email,
      cpf,
      telefone,
      senioridade,
      ativo: true,
      agenda: {
        inicioJornada: inicioJornada.length === 5 ? inicioJornada + ':00' : inicioJornada,
        fimJornada: fimJornada.length === 5 ? fimJornada + ':00' : fimJornada
      },
      servicoOferecidoIds: servicosSelecionados.map(id => ({ valor: id }))
    };

    mainService.criarProfissional(
      payload,
      () => {
        toast.success('Profissional cadastrado com sucesso!');
        onSuccess();
        closeModal();
      },
      (error: AxiosError) => {
        const errorData = error.response?.data as { message?: string } | undefined;
        const message = errorData?.message || 'Erro ao cadastrar profissional';
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

  if (!visible) return null;

  return (
    <div className="fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4" onClick={closeModal}>
      <div className="bg-dark-800 rounded-2xl p-8 max-w-2xl w-full border border-dark-600 max-h-[90vh] overflow-y-auto" onClick={e => e.stopPropagation()}>
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-2xl font-bold flex items-center gap-2">
            <span className="material-icons text-primary">person_add</span>
            Novo Profissional
          </h3>
          <button onClick={closeModal} className="text-gray-400 hover:text-white">
            <span className="material-icons">close</span>
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">Nome *</label>
              <input required type="text" value={nome} onChange={e => setNome(e.target.value)}
                className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">CPF *</label>
              <input required type="text" value={cpf} onChange={e => setCpf(e.target.value)} placeholder="000.000.000-00"
                className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" />
            </div>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">Email *</label>
              <input required type="email" value={email} onChange={e => setEmail(e.target.value)}
                className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Telefone *</label>
              <input required type="tel" value={telefone} onChange={e => setTelefone(e.target.value)}
                className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Senioridade *</label>
            <select value={senioridade} onChange={e => setSenioridade(e.target.value)}
              className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none">
              <option value="JUNIOR">Júnior</option>
              <option value="PLENO">Pleno</option>
              <option value="SENIOR">Sênior</option>
            </select>
          </div>
          <div className="grid grid-cols-2 gap-4 bg-dark-700 p-3 rounded-lg border border-dark-600">
            <div>
              <label className="block text-xs text-gray-400 mb-1">Início Jornada</label>
              <input type="time" value={inicioJornada} onChange={e => setInicioJornada(e.target.value)}
                className="bg-transparent text-white font-mono focus:outline-none" />
            </div>
            <div>
              <label className="block text-xs text-gray-400 mb-1">Fim Jornada</label>
              <input type="time" value={fimJornada} onChange={e => setFimJornada(e.target.value)}
                className="bg-transparent text-white font-mono focus:outline-none" />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium mb-2">Serviços que realiza:</label>
            <div className="grid grid-cols-2 gap-2 max-h-32 overflow-y-auto p-2 bg-dark-700 rounded-lg">
              {servicos.map((servico: any) => {
                const rawId = servico.id?.valor ?? servico.id;
                const servicoId = Number(rawId);

                return (
                  <label key={servicoId} className="flex items-center gap-2 cursor-pointer hover:bg-dark-600 p-1 rounded">
                    <input type="checkbox"
                      checked={servicosSelecionados.includes(servicoId)}
                      onChange={() => toggleServico(servicoId)}
                      className="accent-primary"
                    />
                    <span className="text-sm">{servico.nome}</span>
                  </label>
                );
              })}
            </div>
          </div>
          <button type="submit" className="w-full bg-primary hover:bg-orange-600 text-white font-bold py-3 rounded-lg mt-4 transition">
            Salvar Profissional
          </button>
        </form>
      </div>
    </div>
  );
}
