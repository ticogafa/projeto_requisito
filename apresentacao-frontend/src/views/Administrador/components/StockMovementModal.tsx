import { useState } from 'react';
import {
  useAdicionarEstoque,
  useRemoverEstoque,
  useRegistrarVenda,
} from '@/hooks/useProdutoMutations';
import type {
  ProdutoResumo,
  AdicionarEstoqueRequest,
  RemoverEstoqueRequest,
  RegistrarVendaRequest,
} from '@/interfaces/ProdutoInterface';

interface StockMovementModalProps {
  produto: ProdutoResumo;
  action: 'adicionar' | 'remover' | 'venda';
  onClose: () => void;
  onSuccess: () => void;
}

export default function StockMovementModal({
  produto,
  action,
  onClose,
  onSuccess,
}: StockMovementModalProps) {
  const { adicionar } = useAdicionarEstoque();
  const { remover } = useRemoverEstoque();
  const { registrar } = useRegistrarVenda();

  const [quantidade, setQuantidade] = useState<number>(1);
  const [observacao, setObservacao] = useState<string>('');

  const getTitle = () => {
    switch (action) {
      case 'adicionar':
        return 'Adicionar ao Estoque';
      case 'remover':
        return 'Remover do Estoque';
      case 'venda':
        return 'Registrar Venda';
    }
  };

  const getColor = () => {
    switch (action) {
      case 'adicionar':
        return 'green';
      case 'remover':
        return 'orange';
      case 'venda':
        return 'purple';
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    switch (action) {
      case 'adicionar': {
        const request: AdicionarEstoqueRequest = {
          quantidade,
          observacao: observacao || undefined,
          usuarioResponsavel: 'admin', // TODO: pegar do contexto de autenticação
        };
        adicionar(produto.id, request, onSuccess);
        break;
      }
      case 'remover': {
        const request: RemoverEstoqueRequest = {
          quantidade,
          observacao: observacao || undefined,
          usuarioResponsavel: 'admin', // TODO: pegar do contexto de autenticação
        };
        remover(produto.id, request, onSuccess);
        break;
      }
      case 'venda': {
        const request: RegistrarVendaRequest = {
          quantidade,
          usuarioResponsavel: 'admin', // TODO: pegar do contexto de autenticação
        };
        registrar(produto.id, request, onSuccess);
        break;
      }
    }
  };

  const color = getColor();

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
        <h2 className="text-2xl font-bold text-gray-800 mb-4">{getTitle()}</h2>

        <div className="bg-gray-50 p-4 rounded mb-6">
          <p className="text-sm text-gray-600">
            <strong>Produto:</strong> {produto.nome}
          </p>
          <p className="text-sm text-gray-600">
            <strong>ID:</strong> #{produto.id}
          </p>
          <p className="text-sm text-gray-600">
            <strong>Estoque Atual:</strong>{' '}
            <span
              className={
                produto.estoque <= produto.estoqueMinimo
                  ? 'text-red-600 font-semibold'
                  : ''
              }
            >
              {produto.estoque} unidades
            </span>
          </p>
          {action === 'venda' && (
            <p className="text-sm text-gray-600">
              <strong>Preço Unitário:</strong> R$ {produto.preco.toFixed(2)}
            </p>
          )}
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Quantidade *
            </label>
            <input
              type="number"
              required
              min="1"
              max={action === 'adicionar' ? undefined : produto.estoque}
              value={quantidade}
              onChange={(e) => setQuantidade(parseInt(e.target.value))}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            {action !== 'adicionar' && quantidade > produto.estoque && (
              <p className="text-red-600 text-xs mt-1">
                Quantidade não pode ser maior que o estoque disponível
              </p>
            )}
          </div>

          {action !== 'venda' && (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Observação {action === 'remover' ? '*' : ''}
              </label>
              <textarea
                required={action === 'remover'}
                value={observacao}
                onChange={(e) => setObservacao(e.target.value)}
                rows={3}
                placeholder={
                  action === 'adicionar'
                    ? 'Ex: Compra de fornecedor, Devolução de cliente'
                    : 'Ex: Produto danificado, Perda, Ajuste de inventário'
                }
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          )}

          {action === 'venda' && (
            <div className="bg-blue-50 p-3 rounded">
              <p className="text-sm text-gray-700">
                <strong>Valor Total:</strong> R${' '}
                {(quantidade * produto.preco).toFixed(2)}
              </p>
            </div>
          )}

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
              disabled={
                action !== 'adicionar' &&
                (quantidade > produto.estoque || quantidade < 1)
              }
              className={`px-4 py-2 bg-${color}-600 text-white rounded-md hover:bg-${color}-700 transition-colors disabled:bg-gray-300 disabled:cursor-not-allowed`}
              style={{
                backgroundColor:
                  action !== 'adicionar' &&
                  (quantidade > produto.estoque || quantidade < 1)
                    ? '#d1d5db'
                    : color === 'green'
                    ? '#059669'
                    : color === 'orange'
                    ? '#ea580c'
                    : '#9333ea',
              }}
            >
              Confirmar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
