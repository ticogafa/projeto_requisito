import { useEffect, useState } from 'react';
import { AdminLayout } from '@/views/Administrador/components/index';
import { useNavigate } from 'react-router-dom';

interface Agendamento {
  id: number;
  dataHora: string;
  status: string;
  clienteNome?: string;
  profissionalNome?: string;
  servicoNome?: string;
  servicoPreco?: number;
}

interface Profissional {
  id: number;
  nome: string;
  ativo: boolean;
}

interface Produto {
  id: number;
  nome: string;
  quantidadeEstoque: number;
  estoqueMinimo: number;
}

export default function AdminDashboardView() {
  const navigate = useNavigate();
  const [agendamentos, setAgendamentos] = useState<Agendamento[]>([]);
  const [profissionais, setProfissionais] = useState<Profissional[]>([]);
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      
      // Buscar todos os agendamentos
      const agendamentosResponse = await fetch('http://localhost:8080/api/agendamentos');
      const agendamentosData = await agendamentosResponse.json();
      setAgendamentos(Array.isArray(agendamentosData) ? agendamentosData : []);

      // Buscar profissionais
      const profissionaisResponse = await fetch('http://localhost:8080/api/profissionais');
      const profissionaisData = await profissionaisResponse.json();
      setProfissionais(Array.isArray(profissionaisData) ? profissionaisData : []);

      // Buscar produtos do estoque
      const produtosResponse = await fetch('http://localhost:8080/api/produto');
      const produtosData = await produtosResponse.json();
      setProdutos(Array.isArray(produtosData) ? produtosData : []);
    } catch (error) {
      console.error('Erro ao carregar dados da dashboard:', error);
      setAgendamentos([]);
      setProfissionais([]);
      setProdutos([]);
    } finally {
      setLoading(false);
    }
  };

  // Filtrar agendamentos de hoje
  const hoje = new Date();
  hoje.setHours(0, 0, 0, 0);
  const amanha = new Date(hoje);
  amanha.setDate(amanha.getDate() + 1);

  const agendamentosHoje = Array.isArray(agendamentos) ? agendamentos.filter(ag => {
    const dataAg = new Date(ag.dataHora);
    return dataAg >= hoje && dataAg < amanha;
  }) : [];

  const profissionaisAtivos = Array.isArray(profissionais) ? profissionais.filter(p => p.ativo).length : 0;
  
  const faturamentoHoje = agendamentosHoje
    .filter(ag => ag.status === 'CONCLUIDO')
    .reduce((total, ag) => total + (ag.servicoPreco || 0), 0);

  const produtosBaixoEstoque = Array.isArray(produtos) ? produtos.filter(p => p.quantidadeEstoque <= p.estoqueMinimo).length : 0;

  const stats = [
    { label: 'Agendamentos Hoje', value: agendamentosHoje.length.toString(), icon: 'event', color: 'text-primary', bg: 'bg-primary/10' },
    { label: 'Profissionais Ativos', value: profissionaisAtivos.toString(), icon: 'people', color: 'text-green-400', bg: 'bg-green-500/10' },
    { label: 'Faturamento Hoje', value: `R$ ${faturamentoHoje.toFixed(2)}`, icon: 'attach_money', color: 'text-blue-400', bg: 'bg-blue-500/10' },
    { label: 'Estoque Baixo', value: produtosBaixoEstoque.toString(), icon: 'inventory_2', color: 'text-yellow-400', bg: 'bg-yellow-500/10' }
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
            {loading ? (
              <tr>
                <td colSpan={5} className="px-6 py-8 text-center text-gray-400">
                  Carregando...
                </td>
              </tr>
            ) : agendamentos.length === 0 ? (
              <tr>
                <td colSpan={5} className="px-6 py-8 text-center text-gray-400">
                  Nenhum agendamento encontrado
                </td>
              </tr>
            ) : (
              agendamentos.slice(0, 5).map((ag) => {
                const data = new Date(ag.dataHora);
                const statusConfig: Record<string, { bg: string; text: string; label: string }> = {
                  PENDENTE: { bg: 'bg-yellow-500/10', text: 'text-yellow-400', label: 'Pendente' },
                  CONFIRMADO: { bg: 'bg-primary/10', text: 'text-primary', label: 'Confirmado' },
                  CONCLUIDO: { bg: 'bg-blue-500/10', text: 'text-blue-400', label: 'Concluído' },
                  CANCELADO: { bg: 'bg-red-500/10', text: 'text-red-400', label: 'Cancelado' },
                };
                const config = statusConfig[ag.status] || statusConfig.PENDENTE;

                return (
                  <tr key={ag.id} className="hover:bg-dark-700">
                    <td className="px-6 py-3 text-sm text-gray-300">
                      {data.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' })} {data.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })}
                    </td>
                    <td className="px-6 py-3 text-sm text-gray-300">{ag.clienteNome || '-'}</td>
                    <td className="px-6 py-3 text-sm text-gray-300">{ag.profissionalNome || '-'}</td>
                    <td className="px-6 py-3 text-sm text-gray-300">{ag.servicoNome || '-'}</td>
                    <td className="px-6 py-3 text-center">
                      <span className={`${config.bg} ${config.text} px-2 py-1 rounded-full text-xs`}>
                        {config.label}
                      </span>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>
    </AdminLayout>
  );
}
