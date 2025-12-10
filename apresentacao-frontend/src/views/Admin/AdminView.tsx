import { useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';

export default function AdminView() {
  const navigate = useNavigate();

  const handleLogout = async () => {
    await AuthService.logout(
      () => navigate('/'),
      (error) => console.error(error)
    );
  };

  return (
    <div className="min-h-screen bg-dark-900 text-white p-8">
      <header className="flex justify-between items-center mb-8 border-b border-dark-600 pb-4">
        <div>
          <h1 className="text-3xl font-bold text-primary">Painel Administrativo</h1>
          <p className="text-gray-400">Visão geral e gestão do sistema</p>
        </div>
        <button
          onClick={handleLogout}
          className="bg-red-500/10 hover:bg-red-500/20 text-red-500 px-4 py-2 rounded-lg transition flex items-center gap-2"
        >
          <span className="material-icons">logout</span>
          Sair
        </button>
      </header>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-dark-800 p-6 rounded-xl border border-dark-600">
          <h3 className="text-xl font-semibold mb-2">Profissionais</h3>
          <p className="text-gray-400">Gerenciar equipe.</p>
        </div>
        
        <div className="bg-dark-800 p-6 rounded-xl border border-dark-600">
          <h3 className="text-xl font-semibold mb-2">Serviços</h3>
          <p className="text-gray-400">Catálogo de serviços.</p>
        </div>

        <div className="bg-dark-800 p-6 rounded-xl border border-dark-600">
          <h3 className="text-xl font-semibold mb-2">Estoque</h3>
          <p className="text-gray-400">Controle de produtos.</p>
        </div>

        <div className="bg-dark-800 p-6 rounded-xl border border-dark-600">
          <h3 className="text-xl font-semibold mb-2">Relatórios</h3>
          <p className="text-gray-400">Desempenho e financeiro.</p>
        </div>
      </div>
    </div>
  );
}
