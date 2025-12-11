import { useState, useEffect } from 'react';
import AdminLayout from '@/views/Administrador/components/AdminLayout';
import NewProductModal from '@/components/Administrador/NewProductModal';
import EditProductModal from '@/components/Administrador/EditProductModal';
import StockMovementModal from '@/components/Administrador/StockMovementModal';

interface Produto {
  id: number;
  nome: string;
  estoque: number;
  estoqueMinimo: number;
  preco: number;
}

export default function EstoqueView() {
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [loading, setLoading] = useState(false);
  const [showNewModal, setShowNewModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showMovementModal, setShowMovementModal] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState<Produto | null>(null);
  const [movementType, setMovementType] = useState<'add' | 'remove' | 'sell'>('add');
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    loadProdutos();
  }, []);

  const loadProdutos = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/produtos');
      if (response.ok) {
        const data = await response.json();
        setProdutos(data);
      }
    } catch (error) {
      console.error('Erro ao carregar produtos:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (produto: Produto) => {
    setSelectedProduct(produto);
    setShowEditModal(true);
  };

  const handleMovement = (produto: Produto, type: 'add' | 'remove' | 'sell') => {
    setSelectedProduct(produto);
    setMovementType(type);
    setShowMovementModal(true);
  };

  const handleDelete = async (produto: Produto) => {
    if (!window.confirm(`Deseja realmente deletar o produto "${produto.nome}"? Esta ação não pode ser desfeita.`)) {
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/api/produtos/${produto.id}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        loadProdutos();
      } else {
        alert('Erro ao deletar produto');
      }
    } catch (error) {
      console.error('Erro ao deletar produto:', error);
      alert('Erro ao deletar produto');
    }
  };

  const getStockStatus = (produto: Produto) => {
    if (produto.estoque === 0) return { label: 'Sem Estoque', color: 'red' };
    if (produto.estoque <= produto.estoqueMinimo) return { label: 'Estoque Baixo', color: 'yellow' };
    return { label: 'Normal', color: 'green' };
  };

  const filteredProdutos = produtos.filter(produto =>
    produto.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <AdminLayout>
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-4xl font-bold mb-2 text-white flex items-center gap-3">
            <span className="material-icons text-5xl text-primary">inventory_2</span>
            Gestão de Estoque
          </h1>
          <p className="text-gray-400 text-lg">Controle completo do inventário de produtos</p>
        </div>

        {/* Search and Action */}
        <div className="mb-6 flex flex-col sm:flex-row gap-4 items-stretch sm:items-center justify-between">
          <div className="flex-1 max-w-md">
            <div className="relative">
              <span className="material-icons absolute left-4 top-1/2 -translate-y-1/2 text-gray-400">
                search
              </span>
              <input
                type="text"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                placeholder="Buscar produto por nome..."
                className="w-full pl-12 pr-4 py-3 bg-dark-800 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
              />
              {searchTerm && (
                <button
                  onClick={() => setSearchTerm('')}
                  className="absolute right-3 top-1/2 -translate-y-1/2 p-1 hover:bg-dark-700 rounded transition-colors"
                >
                  <span className="material-icons text-gray-400 text-sm">close</span>
                </button>
              )}
            </div>
          </div>
          <button
            onClick={() => setShowNewModal(true)}
            className="bg-primary hover:bg-orange-600 text-white px-6 py-3 rounded-lg font-semibold transition-all flex items-center gap-2 shadow-lg hover:shadow-primary/50 whitespace-nowrap"
          >
            <span className="material-icons">add_circle</span>
            Novo Produto
          </button>
        </div>

        {/* Products Table */}
        <div className="bg-dark-800 rounded-xl border border-dark-600 overflow-hidden shadow-2xl">
          {loading ? (
            <div className="flex justify-center items-center py-12">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
            </div>
          ) : (
            <table className="w-full">
              <thead className="bg-dark-700 border-b border-dark-600">
                <tr>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300 uppercase tracking-wider">
                    <div className="flex items-center gap-2">
                      <span className="material-icons text-lg">label</span>
                      Produto
                    </div>
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300 uppercase tracking-wider">
                    <div className="flex items-center gap-2">
                      <span className="material-icons text-lg">inventory</span>
                      Estoque
                    </div>
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300 uppercase tracking-wider">
                    <div className="flex items-center gap-2">
                      <span className="material-icons text-lg">warning</span>
                      Mínimo
                    </div>
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300 uppercase tracking-wider">
                    <div className="flex items-center gap-2">
                      <span className="material-icons text-lg">attach_money</span>
                      Preço
                    </div>
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300 uppercase tracking-wider">
                    <div className="flex items-center gap-2">
                      <span className="material-icons text-lg">info</span>
                      Status
                    </div>
                  </th>
                  <th className="px-6 py-4 text-right text-sm font-semibold text-gray-300 uppercase tracking-wider">
                    <div className="flex items-center justify-end gap-2">
                      <span className="material-icons text-lg">settings</span>
                      Ações
                    </div>
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-dark-600">
                {filteredProdutos.length === 0 ? (
                  <tr>
                    <td colSpan={6} className="px-6 py-8 text-center text-gray-400">
                      <div className="flex flex-col items-center gap-3">
                        <span className="material-icons text-6xl text-gray-600">inventory_2</span>
                        <p className="text-lg">
                          {searchTerm ? 'Nenhum produto encontrado' : 'Nenhum produto cadastrado'}
                        </p>
                      </div>
                    </td>
                  </tr>
                ) : (
                  filteredProdutos.map((produto) => {
                    const status = getStockStatus(produto);
                    return (
                      <tr key={produto.id} className="hover:bg-dark-700 transition-colors">
                        <td className="px-6 py-4">
                          <div className="flex items-center gap-2">
                            <span className="material-icons text-gray-400">shopping_bag</span>
                            <span className="text-sm font-medium text-white">{produto.nome}</span>
                          </div>
                        </td>
                        <td className="px-6 py-4">
                          <span className="text-sm text-gray-300 font-semibold">{produto.estoque}</span>
                        </td>
                        <td className="px-6 py-4">
                          <span className="text-sm text-gray-400">{produto.estoqueMinimo}</span>
                        </td>
                        <td className="px-6 py-4">
                          <span className="text-sm text-green-400 font-semibold">
                            R$ {produto.preco.toFixed(2)}
                          </span>
                        </td>
                        <td className="px-6 py-4">
                          <span
                            className={`inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium border ${
                              status.color === 'red'
                                ? 'bg-red-500/10 text-red-400 border-red-500/30'
                                : status.color === 'yellow'
                                ? 'bg-yellow-500/10 text-yellow-400 border-yellow-500/30'
                                : 'bg-green-500/10 text-green-400 border-green-500/30'
                            }`}
                          >
                            <span className="material-icons text-sm">
                              {status.color === 'red' ? 'error' : status.color === 'yellow' ? 'warning' : 'check_circle'}
                            </span>
                            {status.label}
                          </span>
                        </td>
                        <td className="px-6 py-4">
                          <div className="flex justify-end gap-2">
                            <button
                              onClick={() => handleEdit(produto)}
                              className="p-2 hover:bg-blue-500/10 rounded-lg transition-all group"
                              title="Editar"
                            >
                              <span className="material-icons text-blue-400 group-hover:scale-110 transition-transform">
                                edit
                              </span>
                            </button>
                            <button
                              onClick={() => handleMovement(produto, 'add')}
                              className="p-2 hover:bg-green-500/10 rounded-lg transition-all group"
                              title="Adicionar Estoque"
                            >
                              <span className="material-icons text-green-400 group-hover:scale-110 transition-transform">
                                add_circle
                              </span>
                            </button>
                            <button
                              onClick={() => handleMovement(produto, 'remove')}
                              className="p-2 hover:bg-orange-500/10 rounded-lg transition-all group"
                              title="Remover Estoque"
                            >
                              <span className="material-icons text-orange-400 group-hover:scale-110 transition-transform">
                                remove_circle
                              </span>
                            </button>
                            <button
                              onClick={() => handleMovement(produto, 'sell')}
                              className="p-2 hover:bg-primary/10 rounded-lg transition-all group"
                              title="Registrar Venda"
                            >
                              <span className="material-icons text-primary group-hover:scale-110 transition-transform">
                                point_of_sale
                              </span>
                            </button>
                            <button
                              onClick={() => handleDelete(produto)}
                              className="p-2 hover:bg-red-500/10 rounded-lg transition-all group"
                              title="Deletar Produto"
                            >
                              <span className="material-icons text-red-400 group-hover:scale-110 transition-transform">
                                delete
                              </span>
                            </button>
                          </div>
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          )}
        </div>
      </div>

      {/* Modals */}
      {showNewModal && (
        <NewProductModal
          onClose={() => setShowNewModal(false)}
          onSuccess={() => {
            loadProdutos();
            setShowNewModal(false);
          }}
        />
      )}

      {showEditModal && selectedProduct && (
        <EditProductModal
          produto={selectedProduct}
          onClose={() => {
            setShowEditModal(false);
            setSelectedProduct(null);
          }}
          onSuccess={() => {
            loadProdutos();
            setShowEditModal(false);
            setSelectedProduct(null);
          }}
        />
      )}

      {showMovementModal && selectedProduct && (
        <StockMovementModal
          produto={selectedProduct}
          type={movementType}
          onClose={() => {
            setShowMovementModal(false);
            setSelectedProduct(null);
          }}
          onSuccess={() => {
            loadProdutos();
            setShowMovementModal(false);
            setSelectedProduct(null);
          }}
        />
      )}
    </AdminLayout>
  );
}
