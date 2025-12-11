import { useState } from 'react';
import { useCadastrarProduto } from '@/hooks/useProdutoMutations';
import type { CadastrarProdutoRequest } from '@/interfaces/ProdutoInterface';

interface NewProductModalProps {
  onClose: () => void;
  onSuccess: () => void;
}

export default function NewProductModal({ onClose, onSuccess }: NewProductModalProps) {
  const { cadastrar } = useCadastrarProduto();
  const [formData, setFormData] = useState<CadastrarProdutoRequest>({
    nome: '',
    preco: 0,
    estoqueInicial: 0,
    estoqueMinimo: 0,
    usuarioResponsavel: 'admin', // TODO: pegar do contexto de autenticação
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    cadastrar(formData, onSuccess);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">Cadastrar Novo Produto</h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Nome *
            </label>
            <input
              type="text"
              required
              value={formData.nome}
              onChange={(e) => setFormData({ ...formData, nome: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="grid grid-cols-3 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Preço (R$) *
              </label>
              <input
                type="number"
                required
                min="0"
                step="0.01"
                value={formData.preco}
                onChange={(e) => setFormData({ ...formData, preco: parseFloat(e.target.value) })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Estoque Inicial *
              </label>
              <input
                type="number"
                required
                min="0"
                value={formData.estoqueInicial}
                onChange={(e) => setFormData({ ...formData, estoqueInicial: parseInt(e.target.value) })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Estoque Mínimo *
              </label>
              <input
                type="number"
                required
                min="0"
                value={formData.estoqueMinimo}
                onChange={(e) => setFormData({ ...formData, estoqueMinimo: parseInt(e.target.value) })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              Cadastrar Produto
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
