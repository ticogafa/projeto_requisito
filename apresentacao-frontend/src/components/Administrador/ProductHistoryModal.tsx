import { useState, useEffect } from 'react';

interface Produto {
  id: number;
  nome: string;
}

interface Movimentacao {
  id: number;
  tipo: string;
  quantidade: number;
  estoqueAnterior: number;
  estoqueNovo: number;
  dataHora: string;
  observacao?: string;
  usuarioResponsavel?: string;
}

interface ProductHistoryModalProps {
  produto: Produto;
  onClose: () => void;
}

export default function ProductHistoryModal({ produto, onClose }: ProductHistoryModalProps) {
  const [movimentacoes, setMovimentacoes] = useState<Movimentacao[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadMovimentacoes();
  }, [produto.id]);

  const loadMovimentacoes = async () => {
    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/api/produtos/${produto.id}/movimentacoes`);
      if (response.ok) {
        const data = await response.json();
        setMovimentacoes(data);
      }
    } catch (error) {
      console.error('Erro ao carregar movimentações:', error);
    } finally {
      setLoading(false);
    }
  };

  const getTipoConfig = (tipo: string) => {
    const configs: Record<string, { label: string; icon: string; color: string; bg: string }> = {
      'ENTRADA': { label: 'Entrada', icon: 'add_circle', color: 'text-green-400', bg: 'bg-green-500/10' },
      'SAIDA': { label: 'Saída', icon: 'remove_circle', color: 'text-orange-400', bg: 'bg-orange-500/10' },
      'VENDA': { label: 'Venda', icon: 'point_of_sale', color: 'text-primary', bg: 'bg-primary/10' },
      'ESTOQUE_INICIAL': { label: 'Estoque Inicial', icon: 'inventory', color: 'text-blue-400', bg: 'bg-blue-500/10' },
      'AJUSTE': { label: 'Ajuste', icon: 'tune', color: 'text-purple-400', bg: 'bg-purple-500/10' },
      'DESATIVACAO': { label: 'Desativação', icon: 'block', color: 'text-red-400', bg: 'bg-red-500/10' },
    };
    return configs[tipo] || { label: tipo, icon: 'sync', color: 'text-gray-400', bg: 'bg-gray-500/10' };
  };

  const formatDateTime = (dateTime: string) => {
    const date = new Date(dateTime);
    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    }).format(date);
  };

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-dark-800 rounded-2xl border border-dark-600 shadow-2xl w-full max-w-4xl max-h-[90vh] flex flex-col">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-dark-600">
          <div className="flex items-center gap-3">
            <div className="bg-blue-500/10 p-2 rounded-lg">
              <span className="material-icons text-blue-400 text-2xl">history</span>
            </div>
            <div>
              <h2 className="text-2xl font-bold text-white">Histórico de Movimentações</h2>
              <p className="text-sm text-gray-400">{produto.nome}</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-2 hover:bg-dark-700 rounded-lg transition-colors group"
          >
            <span className="material-icons text-gray-400 group-hover:text-white">close</span>
          </button>
        </div>

        {/* Content */}
        <div className="flex-1 overflow-y-auto p-6">
          {loading ? (
            <div className="flex justify-center items-center py-12">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
            </div>
          ) : movimentacoes.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-12 text-gray-400">
              <span className="material-icons text-6xl text-gray-600 mb-3">history</span>
              <p className="text-lg">Nenhuma movimentação registrada</p>
            </div>
          ) : (
            <div className="space-y-4">
              {movimentacoes.map((mov) => {
                const config = getTipoConfig(mov.tipo);
                const variacao = mov.estoqueNovo - mov.estoqueAnterior;
                
                return (
                  <div
                    key={mov.id}
                    className="bg-dark-700 border border-dark-600 rounded-xl p-5 hover:border-primary/50 transition-colors"
                  >
                    <div className="flex items-start justify-between mb-3">
                      <div className="flex items-center gap-3">
                        <div className={`${config.bg} p-2 rounded-lg`}>
                          <span className={`material-icons ${config.color}`}>{config.icon}</span>
                        </div>
                        <div>
                          <h3 className="text-lg font-semibold text-white">{config.label}</h3>
                          <p className="text-sm text-gray-400">{formatDateTime(mov.dataHora)}</p>
                        </div>
                      </div>
                      
                      <div className="text-right">
                        <div className="flex items-center gap-2 mb-1">
                          <span className="text-sm text-gray-400">Quantidade:</span>
                          <span className="text-lg font-bold text-white">{mov.quantidade}</span>
                        </div>
                        <div className={`text-sm font-medium ${variacao >= 0 ? 'text-green-400' : 'text-red-400'}`}>
                          {variacao >= 0 ? '+' : ''}{variacao} unidades
                        </div>
                      </div>
                    </div>

                    <div className="grid grid-cols-2 gap-4 mb-3 p-3 bg-dark-800 rounded-lg">
                      <div>
                        <span className="text-xs text-gray-500 uppercase">Estoque Anterior</span>
                        <p className="text-lg font-semibold text-gray-300">{mov.estoqueAnterior}</p>
                      </div>
                      <div>
                        <span className="text-xs text-gray-500 uppercase">Estoque Novo</span>
                        <p className="text-lg font-semibold text-white">{mov.estoqueNovo}</p>
                      </div>
                    </div>

                    {mov.observacao && (
                      <div className="p-3 bg-dark-900 border border-dark-600 rounded-lg mb-2">
                        <div className="flex items-start gap-2">
                          <span className="material-icons text-sm text-gray-400 mt-0.5">notes</span>
                          <div className="flex-1">
                            <p className="text-xs text-gray-500 uppercase mb-1">Observação</p>
                            <p className="text-sm text-gray-300">{mov.observacao}</p>
                          </div>
                        </div>
                      </div>
                    )}

                    {mov.usuarioResponsavel && (
                      <div className="flex items-center gap-2 text-xs text-gray-500">
                        <span className="material-icons text-sm">person</span>
                        <span>Responsável: <span className="text-gray-400">{mov.usuarioResponsavel}</span></span>
                      </div>
                    )}
                  </div>
                );
              })}
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="p-6 border-t border-dark-600 flex justify-end">
          <button
            onClick={onClose}
            className="px-6 py-3 bg-dark-700 hover:bg-dark-600 text-gray-300 rounded-lg font-semibold transition-all border border-dark-600"
          >
            Fechar
          </button>
        </div>
      </div>
    </div>
  );
}
