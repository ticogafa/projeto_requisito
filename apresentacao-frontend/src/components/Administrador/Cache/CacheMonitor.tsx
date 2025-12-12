import { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import CacheService from '@/services/CacheService';
import type { AxiosError } from 'axios';

interface CacheStats {
  hits: number;
  misses: number;
  size: number;
}

export default function CacheMonitor() {
  const [cacheStats, setCacheStats] = useState<CacheStats | null>(null);
  const [loading, setLoading] = useState(true);
  const [clearing, setClearing] = useState(false);

  useEffect(() => {
    fetchCacheStats();
  }, []);

  const fetchCacheStats = () => {
    setLoading(true);
    CacheService.getInstance().getCacheStats(
      (data) => {
        setCacheStats(data);
      },
      (error: AxiosError) => {
        console.error('Error fetching cache stats:', error);
        toast.error('Erro ao buscar estatísticas do cache.');
      },
      () => {
        setLoading(false);
      }
    );
  };

  const handleClearCache = () => {
    if (window.confirm('Tem certeza que deseja limpar o cache? Esta ação não pode ser desfeita.')) {
      setClearing(true);
      CacheService.getInstance().clearCache(
        () => {
          toast.success('Cache limpo com sucesso!');
          fetchCacheStats(); // Refresh stats after clearing
        },
        (error: AxiosError) => {
          console.error('Error clearing cache:', error);
          toast.error('Erro ao limpar o cache.');
        },
        () => {
          setClearing(false);
        }
      );
    }
  };

  return (
    <div className="bg-dark-800 rounded-xl p-6 border border-dark-600 shadow-lg">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-bold text-white flex items-center gap-2">
          <span className="material-icons text-primary">storage</span>
          Monitoramento de Cache
        </h2>
        <button
          onClick={handleClearCache}
          disabled={clearing}
          className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg font-medium flex items-center gap-2 transition disabled:opacity-50"
        >
          {clearing ? (
            <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
          ) : (
            <span className="material-icons">delete_forever</span>
          )}
          Limpar Cache
        </button>
      </div>

      {loading ? (
        <div className="text-center text-gray-400">Carregando estatísticas do cache...</div>
      ) : cacheStats ? (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="bg-dark-700 p-4 rounded-lg border border-dark-600">
            <h3 className="text-gray-400 text-sm">Receita Gerada</h3>
            <p className="text-white text-2xl font-bold">{cacheStats.hits}</p>
          </div>
          <div className="bg-dark-700 p-4 rounded-lg border border-dark-600">
            <h3 className="text-gray-400 text-sm">Receita Gasta</h3>
            <p className="text-white text-2xl font-bold">{cacheStats.misses}</p>
          </div>
          <div className="bg-dark-700 p-4 rounded-lg border border-dark-600">
            <h3 className="text-gray-400 text-sm">Tamanho do Cache (items)</h3>
            <p className="text-white text-2xl font-bold">{cacheStats.size}</p>
          </div>
        </div>
      ) : (
        <div className="text-center text-gray-400">Nenhuma estatística de cache disponível.</div>
      )}
    </div>
  );
}
