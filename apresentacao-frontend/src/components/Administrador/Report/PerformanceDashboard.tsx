import { useState, useEffect, useCallback } from 'react';
import { toast } from 'react-toastify';
import PerformanceService, { RelatorioDesempenho } from '@/services/PerformanceService';
import { useProfissionais } from '@/hooks/useProfissionais';
import { ProfissionalInterface } from '@/interfaces/ProfissionaisInterfaces';


interface ProfessionalReport {
  profissional: ProfissionalInterface;
  relatorio: RelatorioDesempenho | null;
}

export default function PerformanceDashboard() {
  const [reports, setReports] = useState<ProfessionalReport[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedDate, setSelectedDate] = useState<string>(new Date().toISOString().split('T')[0]);

  const { data: profissionais } = useProfissionais();

  const fetchAllReports = useCallback(async () => {
    if (!profissionais || profissionais.length === 0) {
      return;
    }

    setLoading(true);
    const newReports: ProfessionalReport[] = [];
    const service = PerformanceService.getInstance();

    let failures = 0;

    const promises = profissionais.map((prof) => {
      return new Promise<void>((resolve) => {
        // Handle both object id and primitive id just in case, though interface says object
        const profId = typeof prof.id === 'object' ? prof.id.valor : prof.id;
        
        service.getRelatorioDesempenho(
          profId,
          selectedDate,
          (data) => {
            newReports.push({ profissional: prof, relatorio: data });
            resolve();
          },
          (error) => {
            console.error(`Erro ao buscar relatório para ${prof.nome}:`, error);
            newReports.push({ profissional: prof, relatorio: null });
            failures += 1;
            resolve();
          },
          () => {}
        );
      });
    });

    try {
      await Promise.all(promises);
      // Sort by name for consistency
      newReports.sort((a, b) => a.profissional.nome.localeCompare(b.profissional.nome));
      setReports(newReports);

      if (failures > 0) {
        toast.error(`Falha ao carregar ${failures} relatório(s). Veja o console.`);
      }
    } catch (error) {
      console.error('Erro ao processar relatórios:', error);
      toast.error('Erro ao gerar relatórios.');
    } finally {
      setLoading(false);
    }
  }, [profissionais, selectedDate]);

  useEffect(() => {
    fetchAllReports();
  }, [fetchAllReports]);

  return (
    <div className="bg-dark-800 rounded-xl p-6 border border-dark-600 shadow-lg">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-bold text-white flex items-center gap-2">
          <span className="material-icons text-primary">analytics</span>
          Relatório de Desempenho da Equipe
        </h2>
      </div>

      <div className="flex flex-col md:flex-row gap-4 mb-6 items-end">
        <div className="flex-1">
          <label className="block text-sm font-medium text-gray-400 mb-1">Data de Referência</label>
          <input
            type="date"
            value={selectedDate}
            onChange={(e) => setSelectedDate(e.target.value)}
            className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-2 text-white focus:border-primary focus:outline-none"
          />
        </div>
        <div className="flex-none">
          <button
            onClick={fetchAllReports}
            disabled={loading}
            className="bg-primary hover:bg-primary/90 text-white px-6 py-2 rounded-lg font-medium flex items-center justify-center gap-2 transition disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? (
              <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
            ) : (
              <>
                <span className="material-icons">sync</span>
                Atualizar Relatório
              </>
            )}
          </button>
        </div>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="border-b border-dark-600 text-gray-400 text-sm uppercase">
              <th className="py-3 px-4">Profissional</th>
              <th className="py-3 px-4 text-right">Receita Gerada</th>
              <th className="py-3 px-4 text-center">Tempo de Serviço</th>
              <th className="py-3 px-4 text-center">Clientes</th>
              <th className="py-3 px-4 text-center">Avaliação</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-dark-600">
            {reports.length === 0 && !loading && (
              <tr>
                <td colSpan={5} className="py-8 text-center text-gray-500">
                  Nenhum dado carregado. Clique em "Atualizar Relatório".
                </td>
              </tr>
            )}
            {reports.map((item) => (
              <tr key={typeof item.profissional.id === 'object' ? item.profissional.id.valor : item.profissional.id} className="hover:bg-dark-700/50 transition">
                <td className="py-3 px-4 font-medium text-white">{item.profissional.nome}</td>
                <td className="py-3 px-4 text-right text-green-400 font-medium">
                  {item.relatorio 
                    ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(item.relatorio.receitaGerada)
                    : '-'}
                </td>
                <td className="py-3 px-4 text-center text-gray-300">
                  {item.relatorio 
                    ? `${item.relatorio.tempoServico} min` 
                    : '-'}
                </td>
                <td className="py-3 px-4 text-center text-gray-300">
                  {item.relatorio 
                    ? item.relatorio.numeroClientesAtendidos 
                    : '-'}
                </td>
                <td className="py-3 px-4 text-center">
                  {item.relatorio ? (
                    <span className="inline-flex items-center gap-1 px-2 py-1 rounded-full bg-yellow-500/10 text-yellow-400 text-sm">
                      <span className="material-icons text-sm">star</span>
                      {item.relatorio.avaliacaoFuncionario.toFixed(1)}
                    </span>
                  ) : '-'}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
