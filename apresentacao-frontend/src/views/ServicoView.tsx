import { useEffect, useState } from 'react';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import ServicoModal from '@/views/Administrador/components/ServicoModal';
import { ServicoOferecido } from '@/interfaces/ServicoOferecidoInterface';
import { toast } from 'react-toastify';
import { AxiosError } from 'axios';

export default function ServicosView() {
  const [servicos, setServicos] = useState<ServicoOferecido[]>([]);
  const { setLoading } = useLoadingStore();
  const mainService = MainService.getInstance();

  const [modalVisible, setModalVisible] = useState(false);
  const [servicoParaEditar, setServicoParaEditar] = useState<ServicoOferecido | null>(null);

  const [filtroCategoria, setFiltroCategoria] = useState('');
  const [filtroDestaque, setFiltroDestaque] = useState('');

  const fetchServicos = () => {
    setLoading(true);
    mainService.listarServicos(
      (data) => {
        setServicos(data);
        setLoading(false);
      },
      (error) => {
        console.error(error);
        toast.error('Erro ao carregar serviços.');
        setLoading(false);
      }
    );
  };

  useEffect(() => {
    fetchServicos();
  }, []);

  const handleEdit = (servico: ServicoOferecido) => {
    setServicoParaEditar(servico);
    setModalVisible(true);
  };

  const handleToggleStatus = (servico: ServicoOferecido) => {
    const idNumerico = typeof servico.id === 'object' ? servico.id.valor : servico.id;

    const isAtivo = servico.ativo;
    const actionText = isAtivo ? 'desativar' : 'reativar';

    if (!confirm(`Tem certeza que deseja ${actionText} este serviço?`)) return;

    setLoading(true);

    if (isAtivo) {
      mainService.deletarServico(
        idNumerico,
        () => {
          toast.success('Serviço desativado com sucesso!');
          fetchServicos();
        },
        (error: AxiosError) => {
          toast.error('Erro ao desativar serviço.');
          setLoading(false);
        },
        () => setLoading(false)
      );
    } else {
      const payload = {
        id: { valor: idNumerico },
        nome: servico.nome,
        preco: servico.preco,
        duracaoMinutos: servico.duracaoMinutos,
        descricao: servico.descricao || '',
        categoria: servico.categoria || '',
        destaque: servico.destaque || '',
        servicoDependente: !!servico.servicoDependente,
        ativo: true
      };

      mainService.atualizarServico(
        idNumerico,
        payload,
        () => {
          toast.success('Serviço reativado com sucesso!');
          fetchServicos();
        },
        (error: AxiosError) => {
          console.error('Erro ao reativar - Detalhes:', error.response?.data);
          toast.error('Erro ao reativar serviço.');
          setLoading(false);
        },
        () => setLoading(false)
      );
    }
  };

  const openNewModal = () => {
    setServicoParaEditar(null);
    setModalVisible(true);
  };

  const servicosFiltrados = servicos.filter(s => {
    const matchCategoria = filtroCategoria ? s.categoria === filtroCategoria : true;
    const matchDestaque = filtroDestaque ? s.destaque === filtroDestaque : true;
    return matchCategoria && matchDestaque;
  });

  const categoriasDisponiveis = Array.from(new Set(servicos.map(s => s.categoria).filter(Boolean)));

  return (
    <div className="p-6">
      {/* HEADER DA PÁGINA */}
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-2xl font-bold text-white flex items-center gap-2">
            <span className="material-icons text-primary">content_cut</span>
            Gestão de Serviços
          </h1>
        </div>

        <div className="flex items-center gap-4">
          <div className="relative">
            <span className="material-icons absolute left-3 top-1/2 -translate-y-1/2 text-gray-500 text-sm">search</span>
            <input
              type="text"
              placeholder="Buscar serviço..."
              className="bg-dark-800 border border-dark-600 rounded-lg pl-9 pr-4 py-2 text-sm text-white focus:outline-none focus:border-primary w-64"
            />
          </div>

          <button
            onClick={openNewModal}
            className="bg-primary hover:bg-orange-600 text-white font-bold py-2 px-4 rounded-lg flex items-center gap-2 transition text-sm"
          >
            <span className="material-icons text-sm">add</span>
            Cadastrar Novo
          </button>
        </div>
      </div>

      {/* --- BARRA DE FILTROS --- */}
      <div className="grid grid-cols-2 gap-4 mb-4">
        <div>
          <select
            value={filtroCategoria}
            onChange={e => setFiltroCategoria(e.target.value)}
            className="w-full bg-dark-800 border border-dark-600 rounded-lg px-4 py-2 text-white text-sm focus:outline-none focus:border-primary cursor-pointer"
          >
            <option value="">Todas as Categorias</option>
            {categoriasDisponiveis.map(cat => (
              <option key={cat} value={cat}>{cat}</option>
            ))}
          </select>
        </div>

        <div>
          <select
            value={filtroDestaque}
            onChange={e => setFiltroDestaque(e.target.value)}
            className="w-full bg-dark-800 border border-dark-600 rounded-lg px-4 py-2 text-white text-sm focus:outline-none focus:border-primary cursor-pointer"
          >
            <option value="">Todos os Destaques</option>
            <option value="POPULAR">Populares</option>
            <option value="NOVO">Novos</option>
          </select>
        </div>
      </div>

      {/* --- TABELA --- */}
      <div className="bg-dark-800 rounded-xl border border-dark-600 overflow-hidden">
        <table className="w-full text-left border-collapse">
          <thead className="bg-dark-900 text-gray-400 text-xs uppercase font-semibold">
            <tr>
              <th className="px-6 py-4">Serviço</th>
              <th className="px-6 py-4">Categoria</th>
              <th className="px-6 py-4">Duração</th>
              <th className="px-6 py-4">Preço</th>
              <th className="px-6 py-4">Status</th>
              <th className="px-6 py-4 text-right">Ações</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-dark-600 text-sm">
            {servicosFiltrados.length === 0 ? (
              <tr>
                <td colSpan={6} className="px-6 py-8 text-center text-gray-500">
                  Nenhum serviço encontrado.
                </td>
              </tr>
            ) : (
              servicosFiltrados.map((s) => (
                <tr key={typeof s.id === 'object' ? s.id.valor : s.id} className="hover:bg-dark-700/50 transition group">
                  <td className="px-6 py-4 font-medium text-white">
                    <div className="flex flex-col">
                      <span className="text-base">{s.nome}</span>
                      <div className="flex gap-1 mt-1">
                        {s.destaque === 'POPULAR' && <span className="text-[10px] bg-orange-500/20 text-orange-400 px-1.5 rounded border border-orange-500/30">Popular</span>}
                        {s.destaque === 'NOVO' && <span className="text-[10px] bg-blue-500/20 text-blue-400 px-1.5 rounded border border-blue-500/30">Novo</span>}
                        {s.servicoDependente && <span className="text-[10px] bg-purple-500/20 text-purple-400 px-1.5 rounded border border-purple-500/30">Add-on</span>}
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-gray-400">
                    {s.categoria || '-'}
                  </td>
                  <td className="px-6 py-4 text-gray-300">
                    {s.duracaoMinutos} min
                  </td>
                  <td className="px-6 py-4 text-white font-medium">
                    {s.preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}
                  </td>
                  <td className="px-6 py-4">
                    <span className={`px-2 py-0.5 rounded text-[11px] font-bold uppercase tracking-wider ${s.ativo ? 'bg-green-500/10 text-green-500 border border-green-500/20' : 'bg-red-500/10 text-red-500 border border-red-500/20'}`}>
                      {s.ativo ? 'Ativo' : 'Inativo'}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-right">
                    <div className="flex justify-end gap-2">
                      <button
                        onClick={() => handleEdit(s)}
                        className="bg-blue-600/20 hover:bg-blue-600/30 text-blue-400 text-xs font-bold px-3 py-1.5 rounded transition border border-blue-600/30"
                      >
                        Editar
                      </button>

                      <button
                        onClick={() => handleToggleStatus(s)}
                        className={`text-xs font-bold px-3 py-1.5 rounded transition border ${
                          s.ativo
                            ? 'bg-red-600/20 hover:bg-red-600/30 text-red-400 border-red-600/30'
                            : 'bg-green-600/20 hover:bg-green-600/30 text-green-400 border-green-600/30'
                        }`}
                      >
                        {s.ativo ? 'Desativar' : 'Reativar'}
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      <ServicoModal
        visible={modalVisible}
        servicoParaEditar={servicoParaEditar}
        closeModal={() => setModalVisible(false)}
        onSuccess={fetchServicos}
      />
    </div>
  );
}
