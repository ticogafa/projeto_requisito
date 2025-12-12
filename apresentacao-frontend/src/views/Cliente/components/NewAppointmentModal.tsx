import { useAuth } from '@/auth/AuthContext';
import { useCriarAgendamento } from '@/hooks/useCriarAgendamento';
import { useProfissionaisDisponiveis } from '@/hooks/useProfissionaisDisponiveis';
import type { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';
import type { ServicosOferecidosResponse } from '@/interfaces/ServicoOferecidoInterface';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';

interface NewAppointmentModalProps {
  visible: boolean;
  servicos: ServicosOferecidosResponse;
  closeModal: () => void;
  onSuccess?: (agendamento: AgendamentoInterface) => void;
}

export default function NewAppointmentModal(props: NewAppointmentModalProps) {
  const [servicoId, setServicoId] = useState<number | null>(null);
  const [dataHora, setDataHora] = useState<string>('');
  const [profissionalId, setProfissionalId] = useState<number | null>(null);
  const [observacoes, setObservacoes] = useState('');

  const { user } = useAuth();
  const { data: profissionais } = useProfissionaisDisponiveis(
    servicoId,
    dataHora
  );
  const { criar } = useCriarAgendamento();

  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const value = e.target.value;
    console.log('Valor selecionado:', value, 'Tipo:', typeof value);
    const numeroId = value ? Number(value) : null;
    console.log('ID convertido:', numeroId, 'É NaN?', isNaN(numeroId as number));
    setServicoId(numeroId);
  }

  // Reset profissional quando mudar serviço ou data
  useEffect(() => {
    setProfissionalId(null);
  }, [servicoId, dataHora]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (!servicoId || !dataHora) {
      toast.error('Por favor, selecione um serviço e data/hora');
      return;
    }

    if (!user) {
      toast.error('Você precisa estar autenticado para agendar');
      return;
    }

    // Enviar a data/hora local como está, apenas garantindo o formato ISO-8601 sem timezone (LocalTime)
    // O backend espera LocalDateTime e assume que é o horário local do negócio
    const dataHoraISO = `${dataHora}:00`;

    criar(
      {
        clienteId: 1, // TODO: Substituir por user.uid quando tivermos mapeamento de Firebase UID para Cliente ID
        servicoId,
        dataHora: dataHoraISO,
        profissionalId: profissionalId || undefined,
        observacoes
      },
      (agendamento) => {
        props.closeModal();
        props.onSuccess?.(agendamento);

        // Limpar formulário
        setServicoId(null);
        setDataHora('');
        setProfissionalId(null);
        setObservacoes('');
      }
    );
  };

  if (!props.visible) return null;

  const servicoSelecionado = props.servicos.find(s => (typeof s.id === 'object' ? s.id.valor : s.id) === servicoId);

  return (
    <div
      onClick={(e) => {
        if (e.target === e.currentTarget) props.closeModal();
      }}
      className="fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4"
    >
      <div
        className="bg-dark-800 rounded-2xl p-8 max-w-2xl w-full border border-dark-600 max-h-[90vh] overflow-y-auto"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-3">
            <span className="material-icons text-primary text-4xl">event_available</span>
            <h3 className="text-2xl font-bold">Novo Agendamento</h3>
          </div>
          <button
            onClick={props.closeModal}
            className="material-icons text-gray-400 hover:text-primary cursor-pointer"
          >
            close
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Serviço */}
          <div>
            <label className="block text-sm font-medium mb-2">Serviço *</label>
            <select
              required
              value={servicoId || ''}
              onChange={handleChange}
              className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
            >
              <option value="">Selecione um serviço</option>
              {props.servicos.map((servico, index) => (
                <option key={`servico-${(typeof servico.id === 'object' ? servico.id.valor : servico.id)}-${index}`} value={typeof servico.id === 'object' ? servico.id.valor : servico.id}>
                  {servico.nome} - R$ {servico.preco.toFixed(2)} ({servico.duracaoMinutos} min)
                </option>
              ))}
            </select>
            {servicoSelecionado && (
              <div className="mt-2 text-sm text-gray-400">
                <div className="flex items-center gap-2">
                  <span className="material-icons text-xs">schedule</span>
                  <span>Duração: {servicoSelecionado.duracaoMinutos} minutos</span>
                  <span className="mx-2">•</span>
                  <span className="material-icons text-xs">payments</span>
                  <span>Valor: R$ {servicoSelecionado.preco?.toFixed(2) || '0.00'}</span>
                </div>
              </div>
            )}
          </div>

          {/* Data e Hora */}
          <div>
            <label className="block text-sm font-medium mb-2">Data e Hora *</label>
            <input
              type="datetime-local"
              required
              value={dataHora}
              onChange={(e) => setDataHora(e.target.value)}
              min={new Date().toISOString().slice(0, 16)}
              className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
            />
            <div className="mt-2 space-y-1">
              <p className="text-xs text-gray-400">
                <span className="material-icons text-xs align-middle">schedule</span>
                Horário de funcionamento: 8h às 18h (Segunda a Sexta) | 8h às 14h (Sábado)
              </p>
              <p className="text-xs text-primary">
                <span className="material-icons text-xs align-middle">tips_and_updates</span>
                Dica: Para testar, selecione uma próxima Segunda-feira às 10h ou Quarta-feira às 15h
              </p>
            </div>
          </div>

          {/* Profissional */}
          {servicoId && dataHora && (
            <div>
              <label className="flex text-sm font-medium mb-2 items-center gap-2">
                Profissional
                <span className="text-xs text-gray-400 font-normal">
                  (opcional - sistema escolherá automaticamente)
                </span>
              </label>
              <select
                value={profissionalId || ''}
                onChange={(e) => setProfissionalId(e.target.value ? Number(e.target.value) : null)}
                className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
              >
                <option value="">Sistema escolherá o primeiro disponível</option>
                {profissionais && profissionais.length > 0 && profissionais.map((prof, index) => (
                  <option key={`prof-${prof.id}-${index}`} value={prof.id}>
                    {prof.nome} ({prof.senioridade})
                  </option>
                ))}
              </select>
              {profissionais && profissionais.length === 0 && (
                <p className="text-sm text-yellow-400 mt-2">
                  <span className="material-icons text-xs align-middle">warning</span>
                  Nenhum profissional qualificado disponível neste horário. O sistema buscará automaticamente.
                </p>
              )}
            </div>
          )}

          {/* Observações */}
          <div>
            <label className="block text-sm font-medium mb-2">Observações</label>
            <textarea
              value={observacoes}
              onChange={(e) => setObservacoes(e.target.value)}
              rows={3}
              placeholder="Adicione informações adicionais (opcional)"
              className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none resize-none"
            />
          </div>

          {/* Resumo */}
          {servicoSelecionado && dataHora && (
            <div className="bg-dark-700 rounded-lg p-4 space-y-2">
              <h4 className="font-semibold text-primary mb-3">
                Resumo do Agendamento
              </h4>
              <div className="space-y-2 text-sm">
                <div className="flex justify-between">
                  <span className="text-gray-400">Serviço:</span>
                  <span className="font-medium">{servicoSelecionado.nome}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-400">Data/Hora:</span>
                  <span className="font-medium">
                    {new Date(dataHora).toLocaleString('pt-BR')}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-400">Duração:</span>
                  <span className="font-medium">{servicoSelecionado.duracaoMinutos} min</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-400">Profissional:</span>
                  <span className="font-medium">
                    {profissionalId && profissionais 
                      ? profissionais.find(p => p.id === profissionalId)?.nome || 'Automático'
                      : 'Automático'}
                  </span>
                </div>
                <div className="flex justify-between border-t border-dark-600 pt-2 mt-2">
                  <span className="text-gray-400">Valor:</span>
                  <span className="font-semibold text-primary text-lg">
                    R$ {servicoSelecionado.preco.toFixed(2)}
                  </span>
                </div>
              </div>
            </div>
          )}

          {/* Botões */}
          <div className="flex gap-4">
            <button
              type="button"
              onClick={props.closeModal}
              className="flex-1 bg-dark-700 hover:bg-dark-600 text-gray-300 py-3 rounded-lg font-medium transition"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 bg-primary hover:bg-primary/90 text-white py-3 rounded-lg font-medium transition flex items-center justify-center gap-2"
            >
              <span className="material-icons">check</span>
              Confirmar Agendamento
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
