import { useState, useEffect } from 'react';

interface Produto {
  id: number;
  nome: string;
  estoque: number;
  estoqueMinimo: number;
  preco: number;
}

interface EditProductModalProps {
  produto: Produto;
  onClose: () => void;
  onSuccess: () => void;
}

export default function EditProductModal({ produto, onClose, onSuccess }: EditProductModalProps) {
  const [formData, setFormData] = useState({
    nome: produto.nome,
    preco: produto.preco.toString(),
    estoqueMinimo: produto.estoqueMinimo.toString(),
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch(`http://localhost:8080/api/produtos/${produto.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nome: formData.nome,
          preco: parseFloat(formData.preco),
          estoqueMinimo: parseInt(formData.estoqueMinimo),
        }),
      });

      if (response.ok) {
        onSuccess();
      } else {
        const errorData = await response.json();
        setError(errorData.message || 'Erro ao atualizar produto');
      }
    } catch (error) {
      console.error('Erro ao atualizar produto:', error);
      setError('Erro ao atualizar produto');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-dark-800 rounded-2xl border border-dark-600 shadow-2xl w-full max-w-md transform transition-all">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-dark-600">
          <div className="flex items-center gap-3">
            <div className="bg-blue-500/10 p-2 rounded-lg">
              <span className="material-icons text-blue-400 text-2xl">edit</span>
            </div>
            <h2 className="text-2xl font-bold text-white">Editar Produto</h2>
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

          {/* Current Stock (Read-only) */}
          <div className="bg-dark-700/50 border border-dark-600 rounded-lg p-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2 text-gray-400">
                <span className="material-icons">inventory</span>
                <span className="text-sm font-medium">Estoque Atual</span>
              </div>
              <span className="text-2xl font-bold text-white">{produto.estoque}</span>
            </div>
            <p className="text-xs text-gray-500 mt-2">Use as opções de movimentação para alterar o estoque</p>
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">label</span>
              Nome do Produto
            </label>
            <input
              type="text"
              name="nome"
              value={formData.nome}
              onChange={handleChange}
              required
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
              placeholder="Nome do produto"
            />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">attach_money</span>
              Preço (R$)
            </label>
            <input
              type="number"
              name="preco"
              value={formData.preco}
              onChange={handleChange}
              required
              step="0.01"
              min="0"
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
              placeholder="0,00"
            />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">warning</span>
              Estoque Mínimo
            </label>
            <input
              type="number"
              name="estoqueMinimo"
              value={formData.estoqueMinimo}
              onChange={handleChange}
              required
              min="0"
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
              placeholder="0"
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
              disabled={loading}
              className="flex-1 px-6 py-3 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-semibold transition-all flex items-center justify-center gap-2 shadow-lg hover:shadow-blue-600/50 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? (
                <>
                  <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                  Salvando...
                </>
              ) : (
                <>
                  <span className="material-icons">check</span>
                  Salvar
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
