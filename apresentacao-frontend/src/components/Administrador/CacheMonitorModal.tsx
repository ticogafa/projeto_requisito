import { useState, useEffect } from 'react';

interface CacheMetrics {
  cacheHits: number;
  cacheMisses: number;
  cacheExpirations: number;
  totalAcessos: number;
  hitRate: number;
  cacheSizePorId: number;
  cacheSizePorNome: number;
  ttlMinutos: number;
}

export default function CacheMonitorModal({ onClose }: { onClose: () => void }) {
  const [metrics, setMetrics] = useState<CacheMetrics | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadMetrics();
    const interval = setInterval(loadMetrics, 5000); // Atualiza a cada 5 segundos
    return () => clearInterval(interval);
  }, []);

  const loadMetrics = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/cache/metricas');
      if (response.ok) {
        const data = await response.json();
        setMetrics(data);
      }
    } catch (error) {
      console.error('Erro ao carregar métricas do cache:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleResetStats = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/cache/resetar', {
        method: 'POST',
      });
      if (response.ok) {
        loadMetrics();
      }
    } catch (error) {
      console.error('Erro ao resetar estatísticas:', error);
    }
  };

  const handleClearCache = async () => {
    if (!window.confirm('Tem certeza que deseja limpar todo o cache?')) {
      return;
    }
    try {
      const response = await fetch('http://localhost:8080/api/cache/limpar', {
        method: 'POST',
      });
      if (response.ok) {
        loadMetrics();
      }
    } catch (error) {
      console.error('Erro ao limpar cache:', error);
    }
  };

  const getHitRateColor = (rate: number) => {
    if (rate >= 80) return 'text-green-400';
    if (rate >= 50) return 'text-yellow-400';
    return 'text-red-400';
  };

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-dark-800 rounded-2xl border border-dark-600 shadow-2xl w-full max-w-4xl max-h-[90vh] flex flex-col">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-dark-600">
          <div className="flex items-center gap-3">
            <div className="bg-purple-500/10 p-2 rounded-lg">
              <span className="material-icons text-purple-400 text-2xl">memory</span>
            </div>
            <div>
              <h2 className="text-2xl font-bold text-white">Monitor de Cache Proxy</h2>
              <p className="text-sm text-gray-400">Padrão de Projeto: Cache Proxy (Virtual Proxy)</p>
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
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-purple-400"></div>
            </div>
          ) : metrics ? (
            <div className="space-y-6">
              {/* Performance Metrics */}
              <div>
                <h3 className="text-lg font-semibold text-white mb-4 flex items-center gap-2">
                  <span className="material-icons text-green-400">speed</span>
                  Performance do Cache
                </h3>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                  <div className="bg-dark-700 border border-dark-600 rounded-lg p-4">
                    <div className="flex items-center gap-2 mb-2">
                      <span className="material-icons text-green-400 text-sm">check_circle</span>
                      <span className="text-xs text-gray-400 uppercase">Cache Hits</span>
                    </div>
                    <p className="text-2xl font-bold text-white">{metrics.cacheHits}</p>
                  </div>
                  
                  <div className="bg-dark-700 border border-dark-600 rounded-lg p-4">
                    <div className="flex items-center gap-2 mb-2">
                      <span className="material-icons text-red-400 text-sm">cancel</span>
                      <span className="text-xs text-gray-400 uppercase">Cache Misses</span>
                    </div>
                    <p className="text-2xl font-bold text-white">{metrics.cacheMisses}</p>
                  </div>
                  
                  <div className="bg-dark-700 border border-dark-600 rounded-lg p-4">
                    <div className="flex items-center gap-2 mb-2">
                      <span className="material-icons text-orange-400 text-sm">schedule</span>
                      <span className="text-xs text-gray-400 uppercase">Expirações</span>
                    </div>
                    <p className="text-2xl font-bold text-white">{metrics.cacheExpirations}</p>
                  </div>
                  
                  <div className="bg-dark-700 border border-dark-600 rounded-lg p-4">
                    <div className="flex items-center gap-2 mb-2">
                      <span className="material-icons text-blue-400 text-sm">analytics</span>
                      <span className="text-xs text-gray-400 uppercase">Total Acessos</span>
                    </div>
                    <p className="text-2xl font-bold text-white">{metrics.totalAcessos}</p>
                  </div>
                </div>
              </div>

              {/* Hit Rate */}
              <div className="bg-dark-700 border border-dark-600 rounded-lg p-6">
                <h3 className="text-lg font-semibold text-white mb-4 flex items-center gap-2">
                  <span className="material-icons text-primary">trending_up</span>
                  Taxa de Acerto (Hit Rate)
                </h3>
                <div className="flex items-center gap-4">
                  <div className="flex-1">
                    <div className="w-full bg-dark-800 rounded-full h-8 overflow-hidden">
                      <div
                        className={`h-full flex items-center justify-center font-bold text-sm transition-all ${
                          metrics.hitRate >= 80
                            ? 'bg-gradient-to-r from-green-600 to-green-400'
                            : metrics.hitRate >= 50
                            ? 'bg-gradient-to-r from-yellow-600 to-yellow-400'
                            : 'bg-gradient-to-r from-red-600 to-red-400'
                        }`}
                        style={{ width: `${metrics.hitRate}%` }}
                      >
                        {metrics.hitRate > 10 && `${metrics.hitRate.toFixed(2)}%`}
                      </div>
                    </div>
                  </div>
                  <span className={`text-3xl font-bold ${getHitRateColor(metrics.hitRate)}`}>
                    {metrics.hitRate.toFixed(2)}%
                  </span>
                </div>
                <p className="text-xs text-gray-500 mt-2">
                  {metrics.hitRate >= 80 ? '🚀 Excelente!' : metrics.hitRate >= 50 ? '📊 Bom' : '⚠️ Pode melhorar'}
                </p>
              </div>

              {/* Cache State */}
              <div>
                <h3 className="text-lg font-semibold text-white mb-4 flex items-center gap-2">
                  <span className="material-icons text-blue-400">storage</span>
                  Estado do Cache
                </h3>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="bg-dark-700 border border-dark-600 rounded-lg p-4">
                    <div className="flex items-center gap-2 mb-2">
                      <span className="material-icons text-blue-400 text-sm">tag</span>
                      <span className="text-xs text-gray-400 uppercase">Cache por ID</span>
                    </div>
                    <p className="text-xl font-bold text-white">{metrics.cacheSizePorId} produtos</p>
                  </div>
                  
                  <div className="bg-dark-700 border border-dark-600 rounded-lg p-4">
                    <div className="flex items-center gap-2 mb-2">
                      <span className="material-icons text-purple-400 text-sm">label</span>
                      <span className="text-xs text-gray-400 uppercase">Cache por Nome</span>
                    </div>
                    <p className="text-xl font-bold text-white">{metrics.cacheSizePorNome} produtos</p>
                  </div>
                  
                  <div className="bg-dark-700 border border-dark-600 rounded-lg p-4">
                    <div className="flex items-center gap-2 mb-2">
                      <span className="material-icons text-green-400 text-sm">timer</span>
                      <span className="text-xs text-gray-400 uppercase">TTL (Expiração)</span>
                    </div>
                    <p className="text-xl font-bold text-white">{metrics.ttlMinutos} minutos</p>
                  </div>
                </div>
              </div>

              {/* Info Box */}
              <div className="bg-purple-500/10 border border-purple-500/30 rounded-lg p-4">
                <div className="flex items-start gap-3">
                  <span className="material-icons text-purple-400 mt-0.5">info</span>
                  <div className="flex-1">
                    <h4 className="font-semibold text-white mb-1">Sobre o Cache Proxy</h4>
                    <p className="text-sm text-gray-300">
                      O padrão Cache Proxy intercepta requisições ao repositório de produtos e armazena 
                      resultados em memória. Isso reduz drasticamente o acesso ao banco de dados, 
                      melhorando a performance do sistema. O cache expira automaticamente após {metrics.ttlMinutos} 
                      minutos ou quando dados são modificados.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          ) : (
            <div className="flex flex-col items-center justify-center py-12 text-gray-400">
              <span className="material-icons text-6xl text-gray-600 mb-3">error_outline</span>
              <p className="text-lg">Erro ao carregar métricas</p>
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="p-6 border-t border-dark-600 flex justify-between items-center">
          <div className="text-xs text-gray-500">
            Atualização automática a cada 5 segundos
          </div>
          <div className="flex gap-3">
            <button
              onClick={handleResetStats}
              className="px-4 py-2 bg-yellow-600/20 hover:bg-yellow-600/30 text-yellow-400 rounded-lg font-semibold transition-all border border-yellow-600/30"
            >
              <span className="flex items-center gap-2">
                <span className="material-icons text-sm">restart_alt</span>
                Resetar Stats
              </span>
            </button>
            <button
              onClick={handleClearCache}
              className="px-4 py-2 bg-red-600/20 hover:bg-red-600/30 text-red-400 rounded-lg font-semibold transition-all border border-red-600/30"
            >
              <span className="flex items-center gap-2">
                <span className="material-icons text-sm">delete_sweep</span>
                Limpar Cache
              </span>
            </button>
            <button
              onClick={onClose}
              className="px-6 py-2 bg-dark-700 hover:bg-dark-600 text-gray-300 rounded-lg font-semibold transition-all border border-dark-600"
            >
              Fechar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
