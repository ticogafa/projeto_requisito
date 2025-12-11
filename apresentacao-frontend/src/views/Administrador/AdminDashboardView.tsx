import React from 'react';
import { AdminLayout } from '@/views/Administrador/components/index';
import { useNavigate } from 'react-router-dom';

export default function AdminDashboardView() {
  const navigate = useNavigate();

  const stats = [
    { label: 'Agendamentos Hoje', value: '24', icon: 'event', color: 'text-primary', bg: 'bg-primary/10' },
    { label: 'Profissionais Ativos', value: '8', icon: 'people', color: 'text-green-400', bg: 'bg-green-500/10' },
    { label: 'Faturamento Hoje', value: 'R$ 1.850', icon: 'attach_money', color: 'text-blue-400', bg: 'bg-blue-500/10' },
    { label: 'Média Avaliação', value: '4.8', icon: 'star', color: 'text-yellow-400', bg: 'bg-yellow-500/10' }
  ];

  return (
    <AdminLayout>
      <div className="mb-8">
        <h2 className="text-3xl font-bold mb-2 text-white">Painel Administrativo</h2>
        <p className="text-gray-400">Visão geral da operação</p>
      </div>

      {/* Cards de Estatísticas */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {stats.map((stat, index) => (
          <div key={index} className="bg-dark-800 border border-dark-600 rounded-xl p-6">
            <div className="flex items-center justify-between mb-4">
              <span className={`material-icons text-4xl ${stat.color}`}>{stat.icon}</span>
              <span className={`${stat.bg} ${stat.color} px-3 py-1 rounded-full text-sm font-semibold`}>
                Hoje
              </span>
            </div>
            <h3 className="text-3xl font-bold mb-1 text-white">{stat.value}</h3>
            <p className="text-gray-400 text-sm">{stat.label}</p>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4 mb-8">
        {[
          { icon: 'event_note', label: 'Agenda Geral', desc: 'Gerenciar tudo', path: '/admin/agendamentos', color: 'text-primary' },
          { icon: 'schedule', label: 'Jornada', desc: 'Configurar horários', path: '/admin/profissionais', color: 'text-green-400' },
          { icon: 'content_cut', label: 'Serviços', desc: 'Gerenciar catálogo', path: '/admin/servicos', color: 'text-blue-400' },
          { icon: 'account_balance_wallet', label: 'Caixa', desc: 'Entradas e saídas', path: '/admin/financeiro', color: 'text-green-400' },
          { icon: 'bar_chart', label: 'Relatórios', desc: 'Métricas', path: '/admin/relatorios', color: 'text-blue-400' }
        ].map((action, idx) => (
          <button
            key={idx}
            onClick={() => navigate(action.path)}
            className="bg-dark-800 border border-dark-600 hover:border-primary rounded-xl p-4 text-left transition group"
          >
            <span className={`material-icons text-3xl mb-2 group-hover:scale-110 transition ${action.color}`}>
              {action.icon}
            </span>
            <h4 className="font-semibold mb-1 text-white">{action.label}</h4>
            <p className="text-xs text-gray-400">{action.desc}</p>
          </button>
        ))}
      </div>

      {/* Tabela Recente (Simplificada para visualização) */}
      <div className="bg-dark-800 rounded-xl border border-dark-600 overflow-hidden mb-6">
        <div className="px-6 py-4 bg-dark-700 flex items-center justify-between">
          <h3 className="text-xl font-semibold text-white">Agendamentos Recentes</h3>
          <button
            onClick={() => navigate('/admin/agendamentos')}
            className="text-primary hover:text-orange-400 text-sm font-medium cursor-pointer"
          >
            Ver todos
          </button>
        </div>
        <table className="w-full">
          <thead className="bg-dark-700 border-t border-dark-600">
            <tr>
              <th className="px-6 py-3 text-left text-sm font-semibold text-gray-300">Data/Hora</th>
              <th className="px-6 py-3 text-left text-sm font-semibold text-gray-300">Cliente</th>
              <th className="px-6 py-3 text-left text-sm font-semibold text-gray-300">Profissional</th>
              <th className="px-6 py-3 text-left text-sm font-semibold text-gray-300">Serviço</th>
              <th className="px-6 py-3 text-center text-sm font-semibold text-gray-300">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-dark-600">
            <tr className="hover:bg-dark-700">
              <td className="px-6 py-3 text-sm text-gray-300">19/10 09:00</td>
              <td className="px-6 py-3 text-sm text-gray-300">João Pereira</td>
              <td className="px-6 py-3 text-sm text-gray-300">Carlos Silva</td>
              <td className="px-6 py-3 text-sm text-gray-300">Corte + Barba</td>
              <td className="px-6 py-3 text-center">
                <span className="bg-primary/10 text-primary px-2 py-1 rounded-full text-xs">
                  Em andamento
                </span>
              </td>
            </tr>
            <tr className="hover:bg-dark-700">
              <td className="px-6 py-3 text-sm text-gray-300">19/10 10:30</td>
              <td className="px-6 py-3 text-sm text-gray-300">Lucas Lima</td>
              <td className="px-6 py-3 text-sm text-gray-300">Pedro Souza</td>
              <td className="px-6 py-3 text-sm text-gray-300">Corte Social</td>
              <td className="px-6 py-3 text-center">
                <span className="bg-blue-500/10 text-blue-400 px-2 py-1 rounded-full text-xs">
                  Agendado
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </AdminLayout>
  );
}
