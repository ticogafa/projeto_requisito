import { useState } from 'react';

interface NewProductModalProps {
  onClose: () => void;
  onSuccess: () => void;
}

export default function NewProductModal({ onClose, onSuccess }: NewProductModalProps) {
  const [formData, setFormData] = useState({
    nome: '',
    preco: '',
    estoque: '',
    estoqueMinimo: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch('http://localhost:8080/api/produtos', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nome: formData.nome,
          preco: parseFloat(formData.preco),
          estoqueInicial: parseInt(formData.estoque),
          estoqueMinimo: parseInt(formData.estoqueMinimo),
          usuarioResponsavel: 'Administrador',
        }),
      });

      if (response.ok) {
        onSuccess();
      } else {
        const errorData = await response.json();
        setError(errorData.message || 'Erro ao cadastrar produto');
      }
    } catch (error) {
      console.error('Erro ao cadastrar produto:', error);
      setError('Erro ao cadastrar produto');
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
            <div className="bg-primary/10 p-2 rounded-lg">
              <span className="material-icons text-primary text-2xl">add_shopping_cart</span>
            </div>
            <h2 className="text-2xl font-bold text-white">Novo Produto</h2>
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
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
              placeholder="Ex: Pomada modeladora"
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
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
              placeholder="0,00"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
                <span className="material-icons text-lg">inventory</span>
                Estoque Inicial
              </label>
              <input
                type="number"
                name="estoque"
                value={formData.estoque}
                onChange={handleChange}
                required
                min="0"
                className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                placeholder="0"
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
                className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                placeholder="0"
              />
            </div>
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
              className="flex-1 px-6 py-3 bg-primary hover:bg-orange-600 text-white rounded-lg font-semibold transition-all flex items-center justify-center gap-2 shadow-lg hover:shadow-primary/50 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? (
                <>
                  <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                  Cadastrando...
                </>
              ) : (
                <>
                  <span className="material-icons">check</span>
                  Cadastrar
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
