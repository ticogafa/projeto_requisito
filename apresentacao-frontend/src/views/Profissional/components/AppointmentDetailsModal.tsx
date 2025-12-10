import { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';
import { useEffect, useRef } from 'react';

interface AppointmentDetailsModalProps {
  agendamento: AgendamentoInterface | null;
  onClose: () => void;
  onStart: (id: number) => void;
  onFinish: (id: number) => void;
  onCancel: (id: number) => void;
}

export default function AppointmentDetailsModal({
  agendamento,
  onClose,
  onStart,
  onFinish,
  onCancel
}: AppointmentDetailsModalProps) {
  const modalRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    window.addEventListener('keydown', handleEscape);
    return () => window.removeEventListener('keydown', handleEscape);
  }, [onClose]);

  if (!agendamento) return null;

  const handleBackdropClick = (e: React.MouseEvent) => {
    if (modalRef.current && !modalRef.current.contains(e.target as Node)) {
      onClose();
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'CONFIRMADO': return 'bg-blue-500/10 text-blue-400 border-blue-500/20';
      case 'PENDENTE': return 'bg-yellow-500/10 text-yellow-400 border-yellow-500/20';
      case 'EM_ANDAMENTO': return 'bg-purple-500/10 text-purple-400 border-purple-500/20';
      case 'CONCLUIDO': return 'bg-green-500/10 text-green-400 border-green-500/20';
      case 'CANCELADO': return 'bg-red-500/10 text-red-400 border-red-500/20';
      default: return 'bg-gray-500/10 text-gray-400 border-gray-500/20';
    }
  };

  const getStatusLabel = (status: string) => {
     const labels: Record<string, string> = {
        CONFIRMADO: 'Confirmado',
        PENDENTE: 'Pendente',
        EM_ANDAMENTO: 'Em Andamento',
        CONCLUIDO: 'Concluído',
        CANCELADO: 'Cancelado'
     };
     return labels[status] || status;
  };

  const date = new Date(agendamento.dataHora);

  return (
    <div 
      className="fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4 backdrop-blur-sm"
      onClick={handleBackdropClick}
    >
      <div 
        ref={modalRef}
        className="bg-dark-800 rounded-2xl w-full max-w-md border border-dark-600 shadow-2xl overflow-hidden transform transition-all"
      >
        {/* Header */}
        <div className="bg-dark-700/50 p-6 border-b border-dark-600 flex justify-between items-start">
          <div>
            <h3 className="text-xl font-bold text-white mb-1">
              Detalhes do Agendamento
            </h3>
            <p className="text-sm text-gray-400">
              #{agendamento.id}
            </p>
          </div>
          <button 
            onClick={onClose}
            className="text-gray-400 hover:text-white transition p-1 hover:bg-dark-600 rounded-full"
          >
            <span className="material-icons">close</span>
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-6">
          
          {/* Status Badge */}
          <div className={`inline-flex items-center gap-2 px-3 py-1.5 rounded-full text-sm font-medium border ${getStatusColor(agendamento.status)}`}>
            <span className="material-icons text-base">
              {agendamento.status === 'CONFIRMADO' && 'check_circle'}
              {agendamento.status === 'PENDENTE' && 'schedule'}
              {agendamento.status === 'EM_ANDAMENTO' && 'autorenew'}
              {agendamento.status === 'CONCLUIDO' && 'done_all'}
              {agendamento.status === 'CANCELADO' && 'cancel'}
            </span>
            {getStatusLabel(agendamento.status)}
          </div>

          {/* Info Grid */}
          <div className="grid grid-cols-1 gap-4">
             <div className="flex items-start gap-3 p-3 bg-dark-700/30 rounded-lg">
                <span className="material-icons text-gray-400 mt-0.5">event</span>
                <div>
                   <p className="text-sm text-gray-400">Data e Hora</p>
                   <p className="font-medium text-white">
                      {date.toLocaleDateString('pt-BR', { weekday: 'long', day: 'numeric', month: 'long' })}
                   </p>
                   <p className="text-lg font-bold text-primary">
                      {date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })}
                   </p>
                </div>
             </div>

             <div className="flex items-start gap-3 p-3 bg-dark-700/30 rounded-lg">
                <span className="material-icons text-gray-400 mt-0.5">person</span>
                <div>
                   <p className="text-sm text-gray-400">Cliente</p>
                   <p className="font-medium text-white">{agendamento.clienteNome || 'Cliente Não Identificado'}</p>
                </div>
             </div>

             <div className="flex items-start gap-3 p-3 bg-dark-700/30 rounded-lg">
                <span className="material-icons text-gray-400 mt-0.5">content_cut</span>
                <div>
                   <p className="text-sm text-gray-400">Serviço</p>
                   <p className="font-medium text-white">{agendamento.servicoNome}</p>
                </div>
             </div>

             {agendamento.observacoes && (
               <div className="flex items-start gap-3 p-3 bg-dark-700/30 rounded-lg">
                  <span className="material-icons text-gray-400 mt-0.5">notes</span>
                  <div>
                     <p className="text-sm text-gray-400">Observações</p>
                     <p className="text-sm text-white italic">"{agendamento.observacoes}"</p>
                  </div>
               </div>
             )}
          </div>
        </div>

        {/* Actions Footer */}
        <div className="p-6 pt-0 flex flex-col gap-3">
            {(agendamento.status === 'PENDENTE' || agendamento.status === 'CONFIRMADO') && (
              <>
                <button
                  onClick={() => { onStart(agendamento.id); onClose(); }}
                  className="w-full bg-green-600 hover:bg-green-500 text-white py-3 rounded-lg font-medium transition flex items-center justify-center gap-2"
                >
                  <span className="material-icons">play_arrow</span>
                  Iniciar Atendimento
                </button>
                <button
                  onClick={() => { onCancel(agendamento.id); onClose(); }}
                  className="w-full bg-dark-700 hover:bg-red-500/10 hover:text-red-400 text-gray-300 py-3 rounded-lg font-medium transition flex items-center justify-center gap-2 border border-transparent hover:border-red-500/20"
                >
                  <span className="material-icons">cancel</span>
                  Cancelar Agendamento
                </button>
              </>
            )}

            {agendamento.status === 'EM_ANDAMENTO' && (
              <button
                onClick={() => { onFinish(agendamento.id); onClose(); }}
                className="w-full bg-primary hover:bg-primary/90 text-white py-3 rounded-lg font-medium transition flex items-center justify-center gap-2"
              >
                <span className="material-icons">check_circle</span>
                Finalizar Atendimento
              </button>
            )}

            {(agendamento.status === 'CONCLUIDO' || agendamento.status === 'CANCELADO') && (
               <div className="text-center text-gray-500 text-sm py-2">
                 Não há ações disponíveis para este agendamento.
               </div>
            )}
        </div>
      </div>
    </div>
  );
}
