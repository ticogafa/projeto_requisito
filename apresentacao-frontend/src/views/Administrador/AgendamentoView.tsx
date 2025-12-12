import { useState, useEffect } from 'react';
import NewAgendamentoModal from '@/components/Administrador/NewAgendamentoModal';
import EditAgendamentoModal from '@/components/Administrador/EditAgendamentoModal';
import type { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';

export default function AgendamentoView() {
  const [agendamentos, setAgendamentos] = useState<AgendamentoInterface[]>([]);
  const [loading, setLoading] = useState(false);
  const [showNewModal, setShowNewModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedAgendamento, setSelectedAgendamento] = useState<AgendamentoInterface | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState<string>('TODOS');

  useEffect(() => {
    loadAgendamentos();
  }, []);

  const loadAgendamentos = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/agendamentos');
      if (response.ok) {
        const data = await response.json();
        console.log('Agendamentos carregados:', data.length, 'registros');
        console.log('Primeiro agendamento:', data[0]);
        setAgendamentos(Array.isArray(data) ? data : []);
      }
    } catch (error) {
      console.error('Erro ao carregar agendamentos:', error);
      setAgendamentos([]);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (agendamento: AgendamentoInterface) => {
    setSelectedAgendamento(agendamento);
    setShowEditModal(true);
  };

  const handleCancelar = async (agendamento: AgendamentoInterface) => {
    if (!confirm(`Deseja realmente cancelar o agendamento de ${agendamento.servicoNome}?`)) {
      return;
    }

    try {
      const clienteIdParam = agendamento.clienteId || clienteId;
      const response = await fetch(
        `http://localhost:8080/api/agendamentos/${agendamento.id}?clienteId=${clienteIdParam}`,
        { method: 'DELETE' }
      );

      if (response.ok) {
        loadAgendamentos();
      } else {
        const error = await response.json();
        alert(error.message || 'Erro ao cancelar agendamento');
      }
    } catch (error) {
      console.error('Erro ao cancelar agendamento:', error);
      alert('Erro ao cancelar agendamento');
    }
  };

  const handleConcluir = async (agendamento: AgendamentoInterface) => {
    if (!confirm(`Deseja marcar como concluído o agendamento de ${agendamento.servicoNome}?`)) {
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/agendamentos/${agendamento.id}`,
        {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            dataHora: agendamento.dataHora,
            profissionalId: agendamento.profissionalId,
            observacoes: agendamento.observacoes,
            status: 'CONCLUIDO',
          }),
        }
      );

      if (response.ok) {
        loadAgendamentos();
      } else {
        const error = await response.json();
        alert(error.message || 'Erro ao concluir agendamento');
      }
    } catch (error) {
      console.error('Erro ao concluir agendamento:', error);
      alert('Erro ao concluir agendamento');
    }
  };

  const filteredAgendamentos = agendamentos.filter((agendamento) => {
    const matchSearch = 
      agendamento.servicoNome.toLowerCase().includes(searchTerm.toLowerCase()) ||
      agendamento.profissionalNome.toLowerCase().includes(searchTerm.toLowerCase()) ||
      (agendamento.clienteNome?.toLowerCase() || '').includes(searchTerm.toLowerCase());
    
    const matchStatus = filterStatus === 'TODOS' || agendamento.status === filterStatus;
    
    return matchSearch && matchStatus;
  });

  const getStatusBadge = (status: string) => {
    const statusConfig: Record<string, { bg: string; text: string; icon: string }> = {
      PENDENTE: { bg: 'bg-yellow-500/10 border-yellow-500/30', text: 'text-yellow-400', icon: 'schedule' },
      CONFIRMADO: { bg: 'bg-green-500/10 border-green-500/30', text: 'text-green-400', icon: 'check_circle' },
      EM_ANDAMENTO: { bg: 'bg-purple-500/10 border-purple-500/30', text: 'text-purple-400', icon: 'autorenew' },
      CANCELADO: { bg: 'bg-red-500/10 border-red-500/30', text: 'text-red-400', icon: 'cancel' },
      CONCLUIDO: { bg: 'bg-blue-500/10 border-blue-500/30', text: 'text-blue-400', icon: 'done_all' },
    };

    const config = statusConfig[status] || statusConfig.PENDENTE;

    return (
      <span className={`flex items-center gap-1 px-3 py-1 rounded-full border ${config.bg} ${config.text} text-xs font-medium`}>
        <span className="material-icons text-sm">{config.icon}</span>
        {status}
      </span>
    );
  };

  const formatDateTime = (dateTime: string) => {
    const date = new Date(dateTime);
    return new Intl.DateTimeFormat('pt-BR', {
      dateStyle: 'short',
      timeStyle: 'short',
    }).format(date);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-dark-900 via-dark-800 to-dark-900 p-6">
      {/* Header */}
      <div className="mb-8">        <div className="flex items-center gap-3 mb-2">
          <div className="bg-primary/10 p-3 rounded-xl">
            <span className="material-icons text-primary text-3xl">event</span>
          </div>
          <div>
            <h1 className="text-3xl font-bold text-white">Gestão de Agendamentos</h1>
            <p className="text-gray-400">Gerencie os agendamentos da barbearia</p>
          </div>
          </div>
        </div>

      {/* Filtros e Ações */}
      <div className="bg-dark-800 rounded-2xl border border-dark-600 p-6 mb-6">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {/* Busca */}
          <div className="relative">
            <span className="material-icons absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">
              search
            </span>
            <input
              type="text"
              placeholder="Buscar por cliente, serviço ou profissional..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
            />
          </div>

          {/* Filtro de Status */}
          <div className="relative">
            <span className="material-icons absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">
              filter_list
            </span>
            <select
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="w-full pl-10 pr-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent appearance-none cursor-pointer"
            >
              <option value="TODOS">Todos os Status</option>
              <option value="PENDENTE">Pendente</option>
              <option value="CONFIRMADO">Confirmado</option>
              <option value="EM_ANDAMENTO">Em Andamento</option>
              <option value="CONCLUIDO">Concluído</option>
              <option value="CANCELADO">Cancelado</option>
            </select>
          </div>

          {/* Botão Novo Agendamento */}
          <button
            onClick={() => setShowNewModal(true)}
            className="bg-gradient-to-r from-primary to-secondary text-white px-6 py-3 rounded-lg hover:shadow-lg hover:shadow-primary/50 transition-all flex items-center justify-center gap-2 font-semibold"
          >
            <span className="material-icons">add</span>
            <span className="hidden sm:inline">Novo Agendamento</span>
          </button>
        </div>
      </div>

      {/* Lista de Agendamentos */}
      <div className="bg-dark-800 rounded-2xl border border-dark-600 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center p-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
          </div>
        ) : filteredAgendamentos.length === 0 ? (
          <div className="text-center p-12">
            <span className="material-icons text-gray-600 text-6xl mb-4">event_busy</span>
            <p className="text-gray-400 text-lg">Nenhum agendamento encontrado</p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-dark-700/50 border-b border-dark-600">
                <tr>
                  <th className="px-6 py-4 text-left text-xs font-semibold text-gray-400 uppercase tracking-wider">
                    Data/Hora
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold text-gray-400 uppercase tracking-wider">
                    Cliente
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold text-gray-400 uppercase tracking-wider">
                    Serviço
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold text-gray-400 uppercase tracking-wider">
                    Profissional
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold text-gray-400 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-4 text-left text-xs font-semibold text-gray-400 uppercase tracking-wider">
                    Observações
                  </th>
                  <th className="px-6 py-4 text-right text-xs font-semibold text-gray-400 uppercase tracking-wider">
                    Ações
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-dark-600">
                {filteredAgendamentos.map((agendamento) => (
                  <tr
                    key={agendamento.id}
                    className="hover:bg-dark-700/30 transition-colors"
                  >
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center gap-2">
                        <span className="material-icons text-primary text-sm">schedule</span>
                        <span className="text-white font-medium">
                          {formatDateTime(agendamento.dataHora)}
                        </span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <span className="material-icons text-purple-400 text-sm">person</span>
                        <span className="text-white">{agendamento.clienteNome || 'N/A'}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <span className="material-icons text-secondary text-sm">content_cut</span>
                        <span className="text-white">{agendamento.servicoNome}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <span className="material-icons text-blue-400 text-sm">person</span>
                        <span className="text-gray-300">{agendamento.profissionalNome}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      {getStatusBadge(agendamento.status)}
                    </td>
                    <td className="px-6 py-4">
                      <span className="text-gray-400 text-sm">
                        {agendamento.observacoes || '-'}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <div className="flex items-center justify-end gap-2">
                        {agendamento.status !== 'CANCELADO' && agendamento.status !== 'CONCLUIDO' && (
                          <>
                            <button
                              onClick={() => handleEdit(agendamento)}
                              className="p-2 bg-blue-500/10 hover:bg-blue-500/20 rounded-lg transition-colors group"
                              title="Editar"
                            >
                              <span className="material-icons text-blue-400 text-sm">edit</span>
                            </button>
                            {agendamento.status === 'CONFIRMADO' && (
                              <button
                                onClick={() => handleConcluir(agendamento)}
                                className="p-2 bg-green-500/10 hover:bg-green-500/20 rounded-lg transition-colors group"
                                title="Concluir"
                              >
                                <span className="material-icons text-green-400 text-sm">check_circle</span>
                              </button>
                            )}
                            <button
                              onClick={() => handleCancelar(agendamento)}
                              className="p-2 bg-red-500/10 hover:bg-red-500/20 rounded-lg transition-colors group"
                              title="Cancelar"
                            >
                              <span className="material-icons text-red-400 text-sm">cancel</span>
                            </button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Total */}
      <div className="mt-6 flex items-center justify-between px-6">
        <p className="text-gray-400">
          Total de agendamentos: <span className="text-white font-semibold">{filteredAgendamentos.length}</span>
        </p>
      </div>

      {/* Modals */}
      {showNewModal && (
        <NewAgendamentoModal
          onClose={() => setShowNewModal(false)}
          onSuccess={() => {
            setShowNewModal(false);
            loadAgendamentos();
        }}
        />
      )}

      {showEditModal && selectedAgendamento && (
        <EditAgendamentoModal
          agendamento={selectedAgendamento}
          onClose={() => {
            setShowEditModal(false);
            setSelectedAgendamento(null);
          }}
          onSuccess={() => {
            setShowEditModal(false);
            setSelectedAgendamento(null);
            loadAgendamentos();
          }}
        />
      )}
    </div>
  );
}
