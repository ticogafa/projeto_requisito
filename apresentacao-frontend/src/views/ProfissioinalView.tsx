import React, { useState } from 'react';
import { useProfissionais } from '@/hooks/useProfissionais';
import NewProfessionalModal from '@/views/Administrador/components/NewProfessionalModal';
import EditProfessionalModal from '@/views/Administrador/components/EditProfissionalModal';
import { toast } from 'react-toastify';
import MainService from '@/services/MainService';
import { ProfissionalInterface } from '@/interfaces/ProfissionaisInterfaces';
import { useLoadingStore } from '@/store/useLoadingStore';

export default function ProfissionaisView() {
  const { data: profissionais } = useProfissionais();
  const [searchTerm, setSearchTerm] = useState('');

  const [newModalVisible, setNewModalVisible] = useState(false);
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [selectedProf, setSelectedProf] = useState<ProfissionalInterface | null>(null);

  const { setLoading } = useLoadingStore();
  const mainService = MainService.getInstance();

  const filteredProfissionais = profissionais.filter(prof =>
    prof.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleSuccess = () => {
    window.location.reload();
  };

  const handleEdit = (id: number) => {
    const prof = profissionais.find(p => p.id.valor === id);
    if (prof) {
      setSelectedProf(prof);
      setEditModalVisible(true);
    }
  };

  const handleToggleStatus = (id: number, statusAtual: boolean) => {
    if (!window.confirm(`Tem certeza que deseja ${statusAtual ? 'desativar' : 'reativar'} este profissional?`)) return;

    setLoading(true);

    mainService.desativarProfissional(
      id,
      () => {
        toast.success('Status alterado com sucesso!');
        window.location.reload();
      },
      (error) => toast.error('Erro ao alterar status'),
      () => setLoading(false)
    );
  };

  return (
    <div>
      {/* Modais */}
      <NewProfessionalModal
        visible={newModalVisible}
        closeModal={() => setNewModalVisible(false)}
        onSuccess={handleSuccess}
      />

      <EditProfessionalModal
        visible={editModalVisible}
        profissional={selectedProf}
        closeModal={() => {
          setEditModalVisible(false);
          setSelectedProf(null);
        }}
        onSuccess={handleSuccess}
      />

      {/* Cabeçalho */}
      <div className="flex flex-col md:flex-row md:items-center justify-between mb-6 gap-4">
        <div className="flex items-center gap-3">
          <span className="material-icons text-primary text-4xl">people</span>
          <h2 className="text-2xl font-bold text-white">Gestão de Profissionais</h2>
        </div>

        <div className="flex gap-3">
          <div className="relative">
            <input
              type="text" placeholder="Buscar..." value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="bg-dark-700 border border-dark-600 text-gray-200 rounded-lg pl-4 pr-10 py-2 focus:outline-none focus:border-primary transition"
            />
            <span className="material-icons absolute right-3 top-2 text-gray-500">search</span>
          </div>

          <button
            className="bg-primary hover:bg-orange-600 text-white font-medium px-5 py-2 rounded-lg transition flex items-center gap-2"
            onClick={() => setNewModalVisible(true)}
          >
            <span className="material-icons">add</span> Cadastrar Novo
          </button>
        </div>
      </div>

      {/* Tabela */}
      <div className="bg-dark-700 rounded-xl overflow-hidden border border-dark-600">
        <table className="w-full">
          <thead className="bg-dark-600">
            <tr>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Profissional</th>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Nível</th>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Jornada</th>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Avaliação</th>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Status</th>
              <th className="px-6 py-4 text-left text-sm font-semibold text-gray-300">Ações</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-dark-600">
            {filteredProfissionais.map((prof) => (
              <tr key={prof.id.valor} className={`hover:bg-dark-800 transition ${!prof.ativo ? 'opacity-60' : ''}`}>
                <td className="px-6 py-4 font-medium text-white">{prof.nome}</td>
                <td className="px-6 py-4 text-gray-300 capitalize">{prof.senioridade.toLowerCase()}</td>
                <td className="px-6 py-4 text-gray-300">
                  {prof.agenda ? `Seg-Sex (${prof.agenda.inicioJornada.slice(0,5)}-${prof.agenda.fimJornada.slice(0,5)})` : 'N/A'}
                </td>
                <td className="px-6 py-4 text-gray-300">5.0/5.0</td>
                <td className="px-6 py-4">
                  <span className={`px-3 py-1 rounded-full text-sm ${prof.ativo ? 'bg-green-500/10 text-green-400' : 'bg-red-500/10 text-red-400'}`}>
                    {prof.ativo ? 'Ativo' : 'Inativo'}
                  </span>
                </td>
                <td className="px-6 py-4">
                  <div className="flex gap-2">
                    <button onClick={() => handleEdit(prof.id.valor)} className="bg-blue-500/10 text-blue-400 hover:bg-blue-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">
                      Jornada
                    </button>
                    <button onClick={() => handleEdit(prof.id.valor)} className="bg-yellow-500/10 text-yellow-400 hover:bg-yellow-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">
                      Editar
                    </button>
                    <button onClick={() => handleToggleStatus(prof.id.valor, prof.ativo)} className="bg-red-500/10 text-red-400 hover:bg-red-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">
                      {prof.ativo ? 'Desativar' : 'Reativar'}
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
