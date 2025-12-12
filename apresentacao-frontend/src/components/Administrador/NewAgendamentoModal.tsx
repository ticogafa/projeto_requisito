import type { CriarAgendamentoRequest, ProfissionalDisponivelInterface } from '@/interfaces/AgendamentoInterface';
import type { ServicoOferecido } from '@/interfaces/ServicoOferecidoInterface';
import MainService from '@/services/MainService';
import { normalizeIds } from '@/utils/apiHelpers';
import { type AxiosError, type AxiosResponse } from 'axios';
import { useEffect, useState } from 'react';

interface NewAgendamentoModalProps {
  onClose: () => void;
  onSuccess: () => void;
  clienteId: number;
}

export default function NewAgendamentoModal({ onClose, onSuccess, clienteId }: NewAgendamentoModalProps) {
  const [servicos, setServicos] = useState<ServicoOferecido[]>([]);
  const [profissionaisDisponiveis, setProfissionaisDisponiveis] = useState<ProfissionalDisponivelInterface[]>([]);
  const [formData, setFormData] = useState({
    servicoId: '',
    dataHora: '',
    profissionalId: '',
    observacoes: '',
  });
  const [loading, setLoading] = useState(false);
  const [loadingServicos, setLoadingServicos] = useState(true);
  const [loadingProfissionais, setLoadingProfissionais] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    loadServicos();
  }, []);

  useEffect(() => {
    if (formData.servicoId && formData.dataHora) {
      loadProfissionaisDisponiveis();
    } else {
      setProfissionaisDisponiveis([]);
      setFormData((prev) => ({ ...prev, profissionalId: '' }));
    }
  }, [formData.servicoId, formData.dataHora]);

  const loadServicos = () => {
    setLoadingServicos(true);
    MainService.getInstance().getServicosOferecidos(
      {},
      {},
      (response: AxiosResponse) => {
        const data = response.data;
        console.log('Serviços carregados:', data);
        // Normalizar IDs e filtrar apenas serviços ativos
        const normalized = normalizeIds(data) as ServicoOferecido[];
        const servicosAtivos = normalized.filter((s) => s.ativo === true || s.ativo === undefined);
        console.log('Serviços ativos:', servicosAtivos);
        setServicos(servicosAtivos);
      },
      (error: AxiosError) => {
        console.error('Erro ao carregar serviços:', error);
        setError('Erro ao carregar serviços. Verifique a conexão com o servidor.');
      },
      () => {
        setLoadingServicos(false);
      }
    );
  };

  const loadProfissionaisDisponiveis = () => {
    setLoadingProfissionais(true);
    MainService.getInstance().getProfissionaisDisponiveis(
      {
        servicoId: formData.servicoId,
        dataHora: formData.dataHora
      },
      {},
      (response: AxiosResponse) => {
        const data = response.data;
        setProfissionaisDisponiveis(data);
      },
      (error: AxiosError) => {
        console.error('Erro ao carregar profissionais disponíveis:', error);
        setProfissionaisDisponiveis([]);
      },
      () => {
        setLoadingProfissionais(false);
      }
    );
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    // Enviar a data/hora local como está, garantindo formato compatível com LocalDateTime no backend
    // Evita conversão para UTC que causava erro de fuso horário
    const dataHoraISO = `${formData.dataHora}:00`;

    const requestData: CriarAgendamentoRequest = {
      clienteId,
      servicoId: parseInt(formData.servicoId),
      dataHora: dataHoraISO,
      profissionalId: formData.profissionalId ? parseInt(formData.profissionalId) : undefined,
      observacoes: formData.observacoes || undefined,
    };

    MainService.getInstance().criarAgendamento(
      requestData,
      (_response: AxiosResponse) => {
        onSuccess();
      },
      (error: AxiosError) => {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const message = (error.response?.data as any)?.message || 'Erro ao criar agendamento';
        setError(message);
        console.error('Erro ao criar agendamento:', error);
      },
      () => {
        setLoading(false);
      }
    );
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    console.log(`Campo ${e.target.name} alterado para: ${e.target.value}`);
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  // Gera data/hora mínima (agora + 2 horas)
  const getMinDateTime = () => {
    const now = new Date();
    now.setHours(now.getHours() + 2);
    return now.toISOString().slice(0, 16);
  };

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-dark-800 rounded-2xl border border-dark-600 shadow-2xl w-full max-w-2xl transform transition-all max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-dark-600 sticky top-0 bg-dark-800 z-10">
          <div className="flex items-center gap-3">
            <div className="bg-primary/10 p-2 rounded-lg">
              <span className="material-icons text-primary text-2xl">event</span>
            </div>
            <h2 className="text-2xl font-bold text-white">Novo Agendamento</h2>
          </div>
          <button
            onClick={onClose}
            className="p-2 hover:bg-dark-700 rounded-lg transition-colors group"
          >
            <span className="material-icons text-gray-400 group-hover:text-white">close</span>
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-5">
          {error && (
            <div className="bg-red-500/10 border border-red-500/30 rounded-lg p-4 flex items-center gap-2">
              <span className="material-icons text-red-400">error</span>
              <p className="text-red-400 text-sm">{error}</p>
            </div>
          )}

          {/* Serviço */}
          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">content_cut</span>
              Serviço *
            </label>
            {loadingServicos ? (
              <div className="flex items-center gap-2 px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg">
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-primary"></div>
                <span className="text-gray-400 text-sm">Carregando serviços...</span>
              </div>
            ) : (
              <>
                <select
                  name="servicoId"
                  value={formData.servicoId}
                  onChange={handleChange}
                  required
                  disabled={servicos.length === 0}
                  className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <option value="">{servicos.length === 0 ? 'Nenhum serviço disponível' : 'Selecione um serviço'}</option>
                  {servicos.map((servico, index) => (
                                         <option key={(typeof servico.id === 'object' ? servico.id.valor : servico.id) + index} value={typeof servico.id === 'object' ? servico.id.valor : servico.id}>                      {servico.nome} - R$ {servico.preco.toFixed(2)} ({servico.duracaoMinutos} min)
                    </option>
                  ))}
                </select>
                {servicos.length === 0 && !loadingServicos && (
                  <p className="text-xs text-yellow-400 mt-1 flex items-center gap-1">
                    <span className="material-icons text-sm">warning</span>
                    Nenhum serviço cadastrado. Cadastre serviços antes de criar agendamentos.
                  </p>
                )}
              </>
            )}
          </div>

          {/* Data e Hora */}
          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">schedule</span>
              Data e Hora *
            </label>
            <input
              type="datetime-local"
              name="dataHora"
              value={formData.dataHora}
              onChange={handleChange}
              min={getMinDateTime()}
              required
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
            />
            <p className="text-xs text-gray-500 mt-1">
              Agendamentos devem ser feitos com pelo menos 1 hora de antecedência
            </p>
          </div>

          {/* Profissional Disponível */}
          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">person</span>
              Profissional (opcional)
            </label>
            {loadingProfissionais ? (
              <div className="flex items-center gap-2 px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg">
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-primary"></div>
                <span className="text-gray-400 text-sm">Carregando profissionais disponíveis...</span>
              </div>
            ) : (
              <>
                <select
                  name="profissionalId"
                  value={formData.profissionalId}
                  onChange={handleChange}
                  disabled={!formData.servicoId || !formData.dataHora || profissionaisDisponiveis.length === 0}
                  className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <option value="">Sistema escolherá automaticamente</option>
                  {profissionaisDisponiveis.map((prof, index) => (
                    <option key={`prof-${prof.id}-${index}`} value={prof.id}>
                      {prof.nome} ({prof.senioridade})
                    </option>
                  ))}
                </select>
                {formData.servicoId && formData.dataHora && profissionaisDisponiveis.length === 0 && !loadingProfissionais && (
                  <p className="text-xs text-yellow-400 mt-1 flex items-center gap-1">
                    <span className="material-icons text-sm">warning</span>
                    Nenhum profissional disponível neste horário. O sistema alocará quando possível.
                  </p>
                )}
              </>
            )}
          </div>

          {/* Observações */}
          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">notes</span>
              Observações
            </label>
            <textarea
              name="observacoes"
              value={formData.observacoes}
              onChange={handleChange}
              rows={3}
              maxLength={500}
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent resize-none"
              placeholder="Informações adicionais sobre o agendamento..."
            />
            <p className="text-xs text-gray-500 mt-1 text-right">
              {formData.observacoes.length}/500 caracteres
            </p>
          </div>

          {/* Botões */}
          <div className="flex gap-3 pt-4 border-t border-dark-600">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-6 py-3 bg-dark-700 hover:bg-dark-600 text-white rounded-lg transition-colors font-medium"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 px-6 py-3 bg-gradient-to-r from-primary to-secondary text-white rounded-lg hover:shadow-lg hover:shadow-primary/50 transition-all font-semibold disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
            >
              {loading ? (
                <>
                  <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                  <span>Criando...</span>
                </>
              ) : (
                <>
                  <span className="material-icons">check</span>
                  <span>Criar Agendamento</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
