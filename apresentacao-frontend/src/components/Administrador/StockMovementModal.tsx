import { useState } from 'react';

interface Produto {
  id: number;
  nome: string;
  estoque: number;
  estoqueMinimo: number;
  preco: number;
}

interface StockMovementModalProps {
  produto: Produto;
  type: 'add' | 'remove' | 'sell';
  onClose: () => void;
  onSuccess: () => void;
}

export default function StockMovementModal({ produto, type, onClose, onSuccess }: StockMovementModalProps) {
  const [quantidade, setQuantidade] = useState('');
  const [observacao, setObservacao] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const getConfig = () => {
    switch (type) {
      case 'add':
        return {
          title: 'Adicionar ao Estoque',
          icon: 'add_circle',
          color: 'green',
          endpoint: 'adicionar-estoque',
          action: 'Adicionar',
          bgClass: 'bg-green-500/10',
          iconClass: 'text-green-400',
          buttonClass: 'bg-green-600 hover:bg-green-700 hover:shadow-green-600/50',
        };
      case 'remove':
        return {
          title: 'Remover do Estoque',
          icon: 'remove_circle',
          color: 'orange',
          endpoint: 'remover-estoque',
          action: 'Remover',
          bgClass: 'bg-orange-500/10',
          iconClass: 'text-orange-400',
          buttonClass: 'bg-orange-600 hover:bg-orange-700 hover:shadow-orange-600/50',
        };
      case 'sell':
        return {
          title: 'Registrar Venda',
          icon: 'point_of_sale',
          color: 'primary',
          endpoint: 'registrar-venda',
          action: 'Vender',
          bgClass: 'bg-primary/10',
          iconClass: 'text-primary',
          buttonClass: 'bg-primary hover:bg-orange-600 hover:shadow-primary/50',
        };
    }
  };

  const config = getConfig();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch(
        `http://localhost:8080/api/produtos/${produto.id}/${config.endpoint}`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            quantidade: parseInt(quantidade),
            ...(observacao && { observacao }),
            usuarioResponsavel: 'Administrador',
          }),
        }
      );

      if (response.ok) {
        onSuccess();
      } else {
        const errorData = await response.json();
        setError(errorData.message || `Erro ao ${config.action.toLowerCase()}`);
      }
    } catch (error) {
      console.error('Erro na movimentação:', error);
      setError(`Erro ao ${config.action.toLowerCase()}`);
    } finally {
      setLoading(false);
    }
  };

  const getEstimatedStock = () => {
    const qty = parseInt(quantidade) || 0;
    if (type === 'add') return produto.estoque + qty;
    return produto.estoque - qty;
  };

  const estimatedStock = getEstimatedStock();
  const willBeLow = estimatedStock <= produto.estoqueMinimo && estimatedStock > 0;
  const willBeEmpty = estimatedStock <= 0;

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-dark-800 rounded-2xl border border-dark-600 shadow-2xl w-full max-w-md transform transition-all">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-dark-600">
          <div className="flex items-center gap-3">
            <div className={`${config.bgClass} p-2 rounded-lg`}>
              <span className={`material-icons ${config.iconClass} text-2xl`}>{config.icon}</span>
            </div>
            <div>
              <h2 className="text-2xl font-bold text-white">{config.title}</h2>
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

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-5">
          {error && (
            <div className="bg-red-500/10 border border-red-500/30 rounded-lg p-4 flex items-center gap-2">
              <span className="material-icons text-red-400">error</span>
              <p className="text-red-400 text-sm">{error}</p>
            </div>
          )}

          {/* Current Stock Info */}
          <div className="bg-dark-700/50 border border-dark-600 rounded-lg p-4">
            {type === 'sell' && quantidade && (
              <div className="mb-3 pb-3 border-b border-dark-600">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2 text-gray-400">
                    <span className="material-icons">sell</span>
                    <span className="text-sm font-medium">Valor da Venda</span>
                  </div>
                  <span className="text-2xl font-bold text-green-400">
                    R$ {(parseInt(quantidade) * produto.preco).toFixed(2)}
                  </span>
                </div>
                <p className="text-xs text-gray-500 mt-1">
                  {quantidade} × R$ {produto.preco.toFixed(2)}
                </p>
              </div>
            )}
            <div className="flex items-center justify-between mb-3">
              <div className="flex items-center gap-2 text-gray-400">
                <span className="material-icons">inventory</span>
                <span className="text-sm font-medium">Estoque Atual</span>
              </div>
              <span className="text-2xl font-bold text-white">{produto.estoque}</span>
            </div>
            
            {quantidade && (
              <div className="pt-3 border-t border-dark-600 flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <span className={`material-icons text-sm ${willBeEmpty ? 'text-red-400' : willBeLow ? 'text-yellow-400' : 'text-gray-400'}`}>
                    {willBeEmpty ? 'error' : willBeLow ? 'warning' : 'trending_flat'}
                  </span>
                  <span className="text-sm text-gray-400">Estoque Previsto</span>
                </div>
                <span className={`text-xl font-bold ${willBeEmpty ? 'text-red-400' : willBeLow ? 'text-yellow-400' : 'text-white'}`}>
                  {estimatedStock}
                </span>
              </div>
            )}
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">calculate</span>
              Quantidade
            </label>
            <input
              type="number"
              value={quantidade}
              onChange={(e) => setQuantidade(e.target.value)}
              required
              min="1"
              max={type !== 'add' ? produto.estoque : undefined}
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all text-lg font-semibold"
              placeholder="0"
              autoFocus
            />
            {type !== 'add' && quantidade && parseInt(quantidade) > produto.estoque && (
              <p className="mt-2 text-red-400 text-sm flex items-center gap-1">
                <span className="material-icons text-sm">error</span>
                Quantidade maior que o estoque disponível
              </p>
            )}
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">notes</span>
              Observação <span className="text-gray-500 text-xs font-normal">(opcional)</span>
            </label>
            <textarea
              value={observacao}
              onChange={(e) => setObservacao(e.target.value)}
              rows={3}
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all resize-none"
              placeholder="Adicione uma observação sobre esta movimentação..."
            />
          </div>

          {/* Actions */}
          <div className="flex gap-3 pt-4">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-6 py-3 bg-dark-700 hover:bg-dark-600 text-gray-300 rounded-lg font-semibold transition-all border border-dark-600"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={loading || (type !== 'add' && parseInt(quantidade) > produto.estoque)}
              className={`flex-1 px-6 py-3 ${config.buttonClass} text-white rounded-lg font-semibold transition-all flex items-center justify-center gap-2 shadow-lg disabled:opacity-50 disabled:cursor-not-allowed`}
            >
              {loading ? (
                <>
                  <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                  Processando...
                </>
              ) : (
                <>
                  <span className="material-icons">{config.icon}</span>
                  {config.action}
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
