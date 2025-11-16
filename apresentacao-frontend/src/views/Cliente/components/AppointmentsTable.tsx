import { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';

interface Props {
  agendamentos: AgendamentoInterface[];
  onEdit: (id: number) => void;
  onCancel: (id: number) => void;
  onRate: (id: number) => void;
}

export default function AppointmentsTable({ agendamentos, onEdit, onCancel, onRate }: Props) {
  const getStatusBadge = (status: string) => {
    const statusConfig = {
      CONFIRMADO: {
        bg: 'bg-blue-500/10',
        text: 'text-blue-400',
        icon: 'check_circle',
        label: 'Confirmado'
      },
      PENDENTE: {
        bg: 'bg-yellow-500/10',
        text: 'text-yellow-400',
        icon: 'schedule',
        label: 'Pendente'
      },
      CONCLUIDO: {
        bg: 'bg-green-500/10',
        text: 'text-green-400',
        icon: 'done_all',
        label: 'Concluído'
      },
      CANCELADO: {
        bg: 'bg-red-500/10',
        text: 'text-red-400',
        icon: 'cancel',
        label: 'Cancelado'
      }
    };

    const config = statusConfig[status as keyof typeof statusConfig] || statusConfig.PENDENTE;

    return (
      <span className={`status-badge ${config.bg} ${config.text} px-3 py-1 rounded-full text-sm`}>
        <span className="material-icons text-xs">{config.icon}</span>
        {config.label}
      </span>
    );
  };

  const formatDate = (date: string) => {
    const d = new Date(date);
    return `${d.toLocaleDateString('pt-BR')} ${d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })}`;
  };

  return (
    <div className="bg-dark-800 rounded-xl border border-dark-600 overflow-hidden">
      <table className="w-full">
        <thead className="bg-dark-700">
          <tr>
            <th className="px-6 py-4 text-left text-sm font-semibold">Data</th>
            <th className="px-6 py-4 text-left text-sm font-semibold">Profissional</th>
            <th className="px-6 py-4 text-left text-sm font-semibold">Serviço</th>
            <th className="px-6 py-4 text-left text-sm font-semibold">Status</th>
            <th className="px-6 py-4 text-left text-sm font-semibold">Ações</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-dark-600">
          {agendamentos.length === 0 ? (
            <tr>
              <td colSpan={5} className="px-6 py-8 text-center text-gray-400">
                Nenhum agendamento encontrado
              </td>
            </tr>
          ) : (
            agendamentos.map((agendamento) => (
              <tr key={agendamento.id} className="hover:bg-dark-700">
                <td className="px-6 py-4 font-medium">{formatDate(agendamento.dataHora)}</td>
                <td className="px-6 py-4">{agendamento.profissionalNome}</td>
                <td className="px-6 py-4">{agendamento.servicoNome}</td>
                <td className="px-6 py-4">{getStatusBadge(agendamento.status)}</td>
                <td className="px-6 py-4">
                  {agendamento.status === 'CONFIRMADO' || agendamento.status === 'PENDENTE' ? (
                    <div className="flex gap-2">
                      <button
                        onClick={() => onEdit(agendamento.id)}
                        className="bg-blue-500/10 text-blue-400 hover:bg-blue-500/20 px-3 py-2 rounded-lg text-sm font-medium transition"
                      >
                        Editar
                      </button>
                      <button
                        onClick={() => onCancel(agendamento.id)}
                        className="bg-red-500/10 text-red-400 hover:bg-red-500/20 px-3 py-2 rounded-lg text-sm font-medium transition"
                      >
                        Cancelar
                      </button>
                    </div>
                  ) : agendamento.status === 'CONCLUIDO' ? (
                    <button
                      onClick={() => onRate(agendamento.id)}
                      className="bg-primary/10 text-primary hover:bg-primary/20 px-4 py-2 rounded-lg text-sm font-medium transition"
                    >
                      Avaliar Profissional
                    </button>
                  ) : (
                    <span className="text-gray-500">—</span>
                  )}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
