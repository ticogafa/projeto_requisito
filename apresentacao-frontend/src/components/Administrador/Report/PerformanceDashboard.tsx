import { useState } from 'react';
import { toast } from 'react-toastify';
import PerformanceService, { RelatorioDesempenho } from '@/services/PerformanceService';
import { useProfissionais } from '@/hooks/useProfissionais';
import type { AxiosError } from 'axios';

export default function PerformanceDashboard() {
  const [metrics, setMetrics] = useState<RelatorioDesempenho | null>(null);
  const [loading, setLoading] = useState(false);
  const [selectedProfissional, setSelectedProfissional] = useState<number | ''>('');
  const [selectedDate, setSelectedDate] = useState<string>(new Date().toISOString().split('T')[0]);

  const { data: profissionais } = useProfissionais();

  const fetchPerformanceMetrics = () => {
    if (!selectedProfissional) {
      toast.warning('Selecione um profissional');
      return;
    }

    setLoading(true);
    PerformanceService.getInstance().getRelatorioDesempenho(
      Number(selectedProfissional),
      selectedDate,
      (data) => {
        setMetrics(data);
      },
      (error: AxiosError) => {
        console.error('Error fetching performance metrics:', error);
        toast.error('Erro ao buscar relatório de desempenho.');
      },
      () => {
        setLoading(false);
      }
    );
  };

  return (
    <div className="bg-dark-800 rounded-xl p-6 border border-dark-600 shadow-lg">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-bold text-white flex items-center gap-2">
          <span className="material-icons text-primary">analytics</span>
          Relatório de Desempenho do Profissional
        </h2>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div>
          <label className="block text-sm font-medium text-gray-400 mb-1">Profissional</label>
          <select
            value={selectedProfissional}
            onChange={(e) => setSelectedProfissional(Number(e.target.value))}
            className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-2 text-white focus:border-primary focus:outline-none"
          >
            <option value="">Selecione...</option>
            {profissionais.map((prof) => (
              <option key={prof.id} value={prof.id}>
                {prof.nome}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-400 mb-1">Data</label>
          <input
            type="date"
            value={selectedDate}
            onChange={(e) => setSelectedDate(e.target.value)}
            className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-2 text-white focus:border-primary focus:outline-none"
          />
        </div>
        <div className="flex items-end">
          <button
            onClick={fetchPerformanceMetrics}
            disabled={loading || !selectedProfissional}
            className="w-full bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg font-medium flex items-center justify-center gap-2 transition disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? (
              <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
            ) : (
              <>
                <span className="material-icons">search</span>
                Gerar Relatório
              </>
            )}
          </button>
        </div>
      </div>

      {metrics ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="bg-dark-700 p-4 rounded-lg border border-dark-600">
            <div className="text-gray-400 text-sm mb-1">Tempo de Serviço</div>
            <div className="text-2xl font-bold text-white">{metrics.tempoServico} min</div>
            <div className="text-xs text-gray-500 mt-1">Total em minutos</div>
          </div>

          <div className="bg-dark-700 p-4 rounded-lg border border-dark-600">
            <div className="text-gray-400 text-sm mb-1">Receita Gerada</div>
            <div className="text-2xl font-bold text-green-400">
              {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(metrics.receitaGerada)}
            </div>
            <div className="text-xs text-gray-500 mt-1">Total faturado</div>
          </div>

          <div className="bg-dark-700 p-4 rounded-lg border border-dark-600">
            <div className="text-gray-400 text-sm mb-1">Clientes Atendidos</div>
            <div className="text-2xl font-bold text-blue-400">{metrics.numeroClientesAtendidos}</div>
            <div className="text-xs text-gray-500 mt-1">Total de atendimentos</div>
          </div>

          <div className="bg-dark-700 p-4 rounded-lg border border-dark-600">
            <div className="text-gray-400 text-sm mb-1">Avaliação Média</div>
            <div className="text-2xl font-bold text-yellow-400 flex items-center gap-1">
              {metrics.avaliacaoFuncionario.toFixed(1)}
              <span className="material-icons text-yellow-400 text-lg">star</span>
            </div>
            <div className="text-xs text-gray-500 mt-1">Média das avaliações</div>
          </div>
        </div>
      ) : (
        <div className="text-center py-12 text-gray-500 bg-dark-700/30 rounded-lg border border-dashed border-dark-600">
          <span className="material-icons text-4xl mb-2 opacity-50">analytics</span>
          <p>Selecione um profissional e uma data para visualizar o relatório.</p>
        </div>
      )}
    </div>
  );
}
