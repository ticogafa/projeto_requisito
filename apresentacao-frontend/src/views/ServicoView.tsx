import React, { useState } from 'react';
import { useServicos } from '@/hooks/useServico';
import ServicoModal from '@/views/Administrador/components/ServicoModal';
import MainService from '@/services/MainService';
import { toast } from 'react-toastify';
import { useLoadingStore } from '@/store/useLoadingStore';
import { ServicoOferecido } from '@/interfaces/ServicoOferecidoInterface';
import { AxiosError } from 'axios';

export default function ServicosView() {
  const { data: servicos } = useServicos();
  const [searchTerm, setSearchTerm] = useState('');

  const [modalVisible, setModalVisible] = useState(false);
  const [servicoEdit, setServicoEdit] = useState<ServicoOferecido | null>(null);

  const { setLoading } = useLoadingStore();
  const mainService = MainService.getInstance();

  const filteredServicos = servicos.filter(s =>
    s.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleSuccess = () => window.location.reload();

  const openNew = () => {
    setServicoEdit(null);
    setModalVisible(true);
  };

  const openEdit = (servico: ServicoOferecido) => {
    setServicoEdit(servico);
    setModalVisible(true);
  };

  const handleToggleStatus = (servico: ServicoOferecido) => {
    const idNumerico = typeof servico.id === 'object' ? (servico.id as any).valor : servico.id;

    const estaAtivo = servico.ativo !== false;

    const acao = estaAtivo ? 'desativar' : 'reativar';
    if (!window.confirm(`Deseja realmente ${acao} este serviço?`)) return;

    setLoading(true);

    if (estaAtivo) {
      mainService.desativarServico(
        idNumerico,
        () => {
          toast.success('Serviço desativado com sucesso!');
          window.location.reload();
        },
        (error: AxiosError) => {
          if (error.response?.status === 500) {
            toast.error('Não é possível desativar: Existem agendamentos vinculados a este serviço.');
          } else {
            toast.error('Erro ao desativar serviço.');
          }
          setLoading(false);
        },
        () => {}
      );
    } else {
      const payload = {
        id: { valor: idNumerico },
        nome: servico.nome,
        preco: servico.preco,
        duracaoMinutos: servico.duracaoMinutos,
        descricao: servico.descricao,
        ativo: true
      };

      mainService.atualizarServico(
        idNumerico,
        payload,
        () => {
          toast.success('Serviço reativado com sucesso!');
          window.location.reload();
        },
        (error) => {
          toast.error('Erro ao reativar serviço.');
          setLoading(false);
        },
        () => {}
      );
    }
  };

  return (
    <div>
      <ServicoModal
        visible={modalVisible}
        servicoParaEditar={servicoEdit}
        closeModal={() => setModalVisible(false)}
        onSuccess={handleSuccess}
      />

      <div className="flex flex-col md:flex-row md:items-center justify-between mb-6 gap-4">
        <div className="flex items-center gap-3">
          <span className="material-icons text-blue-400 text-4xl">content_cut</span>
          <h2 className="text-2xl font-bold text-white">Gestão de Serviços</h2>
        </div>

        <div className="flex gap-3">
          <div className="relative">
            <input
              type="text" placeholder="Buscar serviço..." value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="bg-dark-700 border border-dark-600 text-gray-200 rounded-lg pl-4 pr-10 py-2 focus:outline-none focus:border-primary transition"
            />
            <span className="material-icons absolute right-3 top-2 text-gray-500">search</span>
          </div>

          <button
            className="bg-primary hover:bg-orange-600 text-white font-medium px-5 py-2 rounded-lg transition flex items-center gap-2"
            onClick={openNew}
          >
            <span className="material-icons">add</span> Novo Serviço
          </button>
        </div>
      </div>

      <div className="bg-dark-700 rounded-xl overflow-hidden border border-dark-600">
        <table className="w-full">
          <thead className="bg-dark-600">
            <tr>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Serviço</th>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Descrição</th>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Duração</th>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Preço</th>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Status</th>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Ações</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-dark-600">
            {filteredServicos.length === 0 ? (
              <tr><td colSpan={6} className="px-6 py-8 text-center text-gray-500">Nenhum serviço encontrado.</td></tr>
            ) : (
              filteredServicos.map((s) => {
                const isAtivo = s.ativo !== false;
                const key = typeof s.id === 'object' ? (s.id as any).valor : s.id;

                return (
                  <tr key={key} className={`hover:bg-dark-800 transition ${!isAtivo ? 'opacity-60' : ''}`}>
                    <td className="px-6 py-4 font-medium text-white">{s.nome}</td>
                    <td className="px-6 py-4 text-gray-300 text-sm">{s.descricao || '-'}</td>
                    <td className="px-6 py-4 text-gray-300">{s.duracaoMinutos} min</td>
                    <td className="px-6 py-4 text-gray-300">R$ {s.preco.toFixed(2)}</td>
                    <td className="px-6 py-4">
                      <span className={`px-3 py-1 rounded-full text-sm ${isAtivo ? 'bg-green-500/10 text-green-400' : 'bg-red-500/10 text-red-400'}`}>
                        {isAtivo ? 'Ativo' : 'Inativo'}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex gap-2">
                        <button onClick={() => openEdit(s)} className="bg-yellow-500/10 text-yellow-400 hover:bg-yellow-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">
                          Editar
                        </button>
                        <button onClick={() => handleToggleStatus(s)} className={`px-3 py-1.5 rounded-lg text-sm font-medium transition ${isAtivo ? 'bg-red-500/10 text-red-400 hover:bg-red-500/20' : 'bg-green-500/10 text-green-400 hover:bg-green-500/20'}`}>
                          {isAtivo ? 'Desativar' : 'Reativar'}
                        </button>
                      </div>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
