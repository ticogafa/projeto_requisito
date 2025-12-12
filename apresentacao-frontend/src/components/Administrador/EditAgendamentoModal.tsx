import { useState, useEffect } from 'react';
import type { AgendamentoInterface, ProfissionalDisponivelInterface } from '@/interfaces/AgendamentoInterface';

interface EditAgendamentoModalProps {
  agendamento: AgendamentoInterface;
  onClose: () => void;
  onSuccess: () => void;
}

export default function EditAgendamentoModal({ agendamento, onClose, onSuccess }: EditAgendamentoModalProps) {
  const [profissionaisDisponiveis, setProfissionaisDisponiveis] = useState<ProfissionalDisponivelInterface[]>([]);
  const [formData, setFormData] = useState({
    dataHora: agendamento.dataHora.slice(0, 16), // Formato para datetime-local
    profissionalId: agendamento.profissionalId?.toString() || '',
    observacoes: agendamento.observacoes || '',
    status: agendamento.status || 'PENDENTE',
  });
  const [loading, setLoading] = useState(false);
  const [loadingProfissionais, setLoadingProfissionais] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (formData.dataHora) {
      loadProfissionaisDisponiveis();
    }
  }, [formData.dataHora]);

  const loadProfissionaisDisponiveis = async () => {
    setLoadingProfissionais(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/agendamentos/profissionais-disponiveis?servicoId=${agendamento.servicoId}&dataHora=${formData.dataHora}`
      );
      if (response.ok) {
        const data = await response.json();
        setProfissionaisDisponiveis(data);
      } else {
        setProfissionaisDisponiveis([]);
      }
    } catch (error) {
      console.error('Erro ao carregar profissionais disponíveis:', error);
      setProfissionaisDisponiveis([]);
    } finally {
      setLoadingProfissionais(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      // Verifica se apenas o status foi alterado (e não a data/hora)
      const statusMudou = formData.status !== agendamento.status;
      const dataHoraMudou = formData.dataHora !== agendamento.dataHora.slice(0, 16);
      const profissionalMudou = formData.profissionalId !== agendamento.profissionalId?.toString();
      const observacoesMudou = (formData.observacoes || '') !== (agendamento.observacoes || '');
      
      // Se apenas o status mudou, enviar apenas o status
      let requestData: any;
      if (statusMudou && !dataHoraMudou && !profissionalMudou && !observacoesMudou) {
        requestData = {
          status: formData.status,
        };
      } else {
        // Caso contrário, enviar todos os campos
        requestData = {
          dataHora: `${formData.dataHora}:00`,
          profissionalId: formData.profissionalId ? parseInt(formData.profissionalId) : undefined,
          observacoes: formData.observacoes || undefined,
          status: formData.status,
        };
      }

      const response = await fetch(`http://localhost:8080/api/agendamentos/${agendamento.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
      });

      if (response.ok) {
        onSuccess();
      } else {
        const errorData = await response.json();
        setError(errorData.message || 'Erro ao editar agendamento');
      }
    } catch (error) {
      console.error('Erro ao editar agendamento:', error);
      setError('Erro ao editar agendamento');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
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
            <div className="bg-blue-500/10 p-2 rounded-lg">
              <span className="material-icons text-blue-400 text-2xl">edit_calendar</span>
            </div>
            <div>
              <h2 className="text-2xl font-bold text-white">Editar Agendamento</h2>
              <p className="text-sm text-gray-400">{agendamento.servicoNome}</p>
            </div>
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

          {/* Info do Serviço (Readonly) */}
          <div className="bg-dark-700/50 border border-dark-600 rounded-lg p-4">
            <div className="flex items-center gap-3">
              <span className="material-icons text-secondary">content_cut</span>
              <div>
                <p className="text-xs text-gray-400">Serviço</p>
                <p className="text-white font-medium">{agendamento.servicoNome}</p>
              </div>
            </div>
          </div>

          {/* Status */}
          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">info</span>
              Status do Agendamento
            </label>
            <select
              name="status"
              value={formData.status}
              onChange={handleChange}
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
            >
              <option value="PENDENTE">Pendente</option>
              <option value="CONFIRMADO">Confirmado</option>
              <option value="EM_ANDAMENTO">Em Andamento</option>
              <option value="CONCLUIDO">Concluído</option>
              <option value="CANCELADO">Cancelado</option>
            </select>
            <p className="text-xs text-gray-500 mt-1">
              O administrador pode alterar o status do agendamento conforme necessário
            </p>
          </div>

          {/* Data e Hora */}
          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">schedule</span>
              Nova Data e Hora *
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
              Profissional
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
                  disabled={profissionaisDisponiveis.length === 0}
                  className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <option value="">Sistema escolherá automaticamente</option>
                  {profissionaisDisponiveis.map((prof) => (
                    <option key={prof.id} value={prof.id}>
                      {prof.nome} ({prof.senioridade})
                    </option>
                  ))}
                </select>
                {profissionaisDisponiveis.length === 0 && !loadingProfissionais && (
                  <p className="text-xs text-yellow-400 mt-1 flex items-center gap-1">
                    <span className="material-icons text-sm">warning</span>
                    Nenhum profissional disponível neste horário. O sistema alocará quando possível.
                  </p>
                )}
                {formData.profissionalId && (
                  <p className="text-xs text-green-400 mt-1 flex items-center gap-1">
                    <span className="material-icons text-sm">check_circle</span>
                    Profissional selecionado: {profissionaisDisponiveis.find(p => p.id === parseInt(formData.profissionalId))?.nome}
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
              className="flex-1 px-6 py-3 bg-gradient-to-r from-blue-500 to-blue-600 text-white rounded-lg hover:shadow-lg hover:shadow-blue-500/50 transition-all font-semibold disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
            >
              {loading ? (
                <>
                  <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                  <span>Salvando...</span>
                </>
              ) : (
                <>
                  <span className="material-icons">save</span>
                  <span>Salvar Alterações</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
