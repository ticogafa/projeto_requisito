import AuthService from '@/services/AuthService';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

export default function LoginView() {
  const navigate = useNavigate();
  const { setLoading } = useLoadingStore();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  const selectedRole = localStorage.getItem('selectedRole') || 'cliente';

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const validation = AuthService.validateLoginData(email, password);
    if (!validation.valid) {
      toast.error(validation.error);
      return;
    }

    setLoading(true);

    const successCallback = () => {
      toast.success('Login realizado com sucesso!');

      // Obter a role real do usuário autenticado (vinda do backend/storage)
      const currentRole = AuthService.getCurrentUserRole();

      switch (currentRole) {
        case 'cliente':
          navigate('/cliente');
          break;
        case 'profissional':
          navigate('/profissional');
          break;
        case 'admin':
          navigate('/admin');
          break;
        default:
          navigate('/');
      }
    };

    const errorCallback = (error: string) => toast.error(error);

    const finallyCallback = () => setLoading(false);

    AuthService.login(email, password, successCallback, errorCallback, finallyCallback);
  };

  const handleResetData = () => {
    setLoading(true);
    MainService.getInstance().resetarDadosTeste(
      () => toast.success('Dados de teste recriados com sucesso! (Clientes, Profissionais, Serviços, Produtos e Agendamentos)'),
      () => toast.error('Erro ao recriar dados'),
      () => setLoading(false)
    );
  };

  const handleBackToProfiles = () => {
    navigate('/');
  };

  const handleGoToRegister = () => {
    navigate('/register');
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-8 bg-dark-900">
      <div className="bg-dark-800 rounded-2xl p-10 w-full max-w-md border border-dark-600">
        <div className="text-center mb-8">
          <div className="flex items-center justify-center gap-3 mb-4">
            <span className="material-icons text-primary text-5xl">content_cut</span>
            <h2 className="text-3xl font-bold">Barbearia César</h2>
          </div>
          <p className="text-gray-400">
            Entre com sua conta de{' '}
            <span className="text-primary font-semibold capitalize">{selectedRole}</span>
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium mb-2">Email</label>
            <div className="relative">
              <span className="material-icons absolute left-4 top-1/2 -translate-y-1/2 text-gray-400">
                email
              </span>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="seu@email.com"
                className="w-full bg-dark-700 border border-dark-600 rounded-lg pl-12 pr-4 py-3 text-white focus:border-primary focus:outline-none"
                required
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium mb-2">Senha</label>
            <div className="relative">
              <span className="material-icons absolute left-4 top-1/2 -translate-y-1/2 text-gray-400">
                lock
              </span>
              <input
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full bg-dark-700 border border-dark-600 rounded-lg pl-12 pr-12 py-3 text-white focus:border-primary focus:outline-none"
                required
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="material-icons absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-primary cursor-pointer"
              >
                {showPassword ? 'visibility_off' : 'visibility'}
              </button>
            </div>
          </div>

          <button
            type="submit"
            className="w-full bg-primary hover:bg-primary/90 text-white font-semibold py-3 rounded-lg transition flex items-center justify-center gap-2"
          >
            <span className="material-icons">login</span>
            Entrar
          </button>
        </form>

        {/* Seção de Contas Demo */}
        <div className="mt-8 pt-6 border-t border-dark-600">
          <div className="flex justify-between items-center mb-3">
             <h3 className="text-sm font-semibold text-gray-400 uppercase tracking-wider">
              Contas de Demonstração
            </h3>
            <button 
              onClick={handleResetData}
              className="text-xs text-primary hover:text-orange-400 underline"
              title="Recria agendamentos baseados na hora atual"
            >
              🔄 Resetar Dados
            </button>
          </div>
          
          <p className="text-xs text-center text-gray-500 mb-4">
            Clique para preencher. Se não conseguir entrar, use o botão "Cadastre-se" com estes dados.
          </p>
          <div className="grid grid-cols-1 gap-3">
            {[
              { role: 'Admin', email: 'admin@barbearia.com', pass: 'senha123', icon: 'admin_panel_settings', color: 'text-red-400' },
              { role: 'Profissional', email: 'carlos@barbearia.com', pass: 'senha123', icon: 'content_cut', color: 'text-blue-400' },
              { role: 'Cliente', email: 'joao.silva@email.com', pass: 'senha123', icon: 'person', color: 'text-green-400' }
            ].map((user) => (
              <button
                key={user.role}
                type="button"
                onClick={() => {
                  setEmail(user.email);
                  setPassword(user.pass);
                }}
                className="flex items-center p-3 bg-dark-700 hover:bg-dark-600 rounded-lg transition group border border-dark-600 hover:border-primary/50"
              >
                <div className={`p-2 rounded-full bg-dark-800 ${user.color} group-hover:scale-110 transition`}>
                  <span className="material-icons text-xl">{user.icon}</span>
                </div>
                <div className="ml-3 text-left">
                  <p className="text-sm font-bold text-white">{user.role}</p>
                  <p className="text-xs text-gray-400">{user.email}</p>
                </div>
                <span className="material-icons ml-auto text-gray-500 group-hover:text-primary">
                  touch_app
                </span>
              </button>
            ))}
          </div>
        </div>

        <div className="mt-6 text-center space-y-3">
          <button
            onClick={handleGoToRegister}
            className="text-primary hover:text-primary/80 transition font-medium"
          >
            Não tem uma conta? Cadastre-se
          </button>

          <button
            onClick={handleBackToProfiles}
            className="text-gray-400 hover:text-primary transition flex items-center justify-center gap-2 mx-auto"
          >
            <span className="material-icons">arrow_back</span>
            Voltar para seleção de perfil
          </button>
        </div>
      </div>
    </div>
  );
}