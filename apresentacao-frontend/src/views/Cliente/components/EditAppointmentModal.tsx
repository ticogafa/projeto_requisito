import { useProfissionaisDisponiveis } from '@/hooks/UseFetch';
import type { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';
import type { ServicosOferecidosResponse } from '@/interfaces/ServicoOferecidoInterface';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';

interface EditAppointmentModalProps {
  visible: boolean;
  agendamento: AgendamentoInterface | null;
  servicos: ServicosOferecidosResponse;
  closeModal: () => void;
  onSuccess?: (agendamento: AgendamentoInterface) => void;
}

export default function EditAppointmentModal(props: EditAppointmentModalProps) {
  const [dataHora, setDataHora] = useState<string>('');
  const [profissionalId, setProfissionalId] = useState<number | null>(null);
  const [observacoes, setObservacoes] = useState('');

  const { setLoading } = useLoadingStore();
  const mainService = MainService.getInstance();

  const { data: profissionais } = useProfissionaisDisponiveis(
    props.agendamento?.servicoId || null,
    dataHora
  );

  // Inicializar campos quando modal abrir
  useEffect(() => {
    if (props.visible && props.agendamento) {
      setDataHora(props.agendamento.dataHora);
      setProfissionalId(props.agendamento.profissionalId || null);
      setObservacoes(props.agendamento.observacoes || '');
    }
  }, [props.visible, props.agendamento]);

  // Reset profissional quando mudar data
  useEffect(() => {
    if (props.visible && dataHora !== props.agendamento?.dataHora) {
      setProfissionalId(null);
    }
  }, [dataHora, props.visible, props.agendamento?.dataHora]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (!props.agendamento || !dataHora) {
      return;
    }

    setLoading(true);

    mainService.editarAgendamento(
      props.agendamento.id,
      {
        dataHora,
        profissionalId: profissionalId || null,
        observacoes
      },
      (response) => {
        toast.success('Agendamento editado com sucesso!');
        props.closeModal();
        props.onSuccess?.(response.data);
      },
      (error) => {
        const errorData = error.response?.data as { message?: string } | undefined;
        const message = errorData?.message || 'Erro ao editar agendamento';
        toast.error(message);
      },
      () => {
        setLoading(false);
      }
    );
  };

  if (!props.visible || !props.agendamento) return null;

  const servicoSelecionado = props.servicos.find(s => s.id === props.agendamento?.servicoId);

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
            <span className="material-icons text-primary text-4xl">edit_calendar</span>
            <h3 className="text-2xl font-bold">Editar Agendamento</h3>
          </div>
          <button
            onClick={props.closeModal}
            className="material-icons text-gray-400 hover:text-primary cursor-pointer"
          >
            close
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Serviço (readonly) */}
          <div>
            <label className="block text-sm font-medium mb-2">Serviço</label>
            <div className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-gray-400">
              {servicoSelecionado?.nome} - R$ {servicoSelecionado?.preco.toFixed(2)} ({servicoSelecionado?.duracaoMinutos} min)
            </div>
            <p className="mt-2 text-xs text-gray-400">
              <span className="material-icons text-xs align-middle">info</span>
              Para alterar o serviço, cancele este agendamento e crie um novo
            </p>
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
            <p className="mt-2 text-xs text-gray-400">
              <span className="material-icons text-xs align-middle">info</span>
              Horário de funcionamento: 8h às 18h
            </p>
          </div>

          {/* Profissional */}
          {dataHora && (
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
                {profissionais.map((prof) => (
                  <option key={prof.id} value={prof.id}>
                    {prof.nome} ({prof.senioridade})
                  </option>
                ))}
              </select>
              {profissionais.length === 0 && (
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
                    {profissionais.find(p => p.id === profissionalId)?.nome || 'Automático'}
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
              Salvar Alterações
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
