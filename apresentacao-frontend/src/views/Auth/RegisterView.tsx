import type { UserRole } from '@/interfaces/UserInterface';
import AuthService from '@/services/AuthService';
import { useLoadingStore } from '@/store/useLoadingStore';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

export default function RegisterView() {
  const navigate = useNavigate();
  const { setLoading } = useLoadingStore();
  const [name, setName] = useState('');
  const [cpf, setCpf] = useState('');
  const [phone, setPhone] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [role, setRole] = useState<UserRole>('cliente');
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!name || !cpf || !phone) {
      toast.error('Por favor, preencha todos os campos obrigatórios (Nome, CPF, Telefone)');
      return;
    }

    const validation = AuthService.validateLoginData(email, password);
    if (!validation.valid) {
      toast.error(validation.error);
      return;
    }

    if (password !== confirmPassword) {
      toast.error('As senhas não coincidem');
      return;
    }

    setLoading(true);

    const successCallback = () => {
      toast.success('Cadastro realizado com sucesso!');

      switch (role) {
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

    AuthService.register(email, password, role, name, cpf, phone, successCallback, errorCallback, finallyCallback);
  };

  const handleBackToLogin = () => {
    navigate('/login');
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
            Criar sua conta
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium mb-2">Tipo de Conta</label>
            <select
              value={role}
              onChange={(e) => setRole(e.target.value as UserRole)}
              className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
            >
              <option value="cliente">Cliente</option>
              <option value="profissional">Profissional</option>
              <option value="admin">Administrador</option>
            </select>
            <p className="text-xs text-gray-500 mt-1">Selecione o tipo de conta que deseja criar</p>
          </div>

          <div>
            <label className="block text-sm font-medium mb-2">Nome Completo</label>
            <div className="relative">
              <span className="material-icons absolute left-4 top-1/2 -translate-y-1/2 text-gray-400">
                person
              </span>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Seu Nome"
                className="w-full bg-dark-700 border border-dark-600 rounded-lg pl-12 pr-4 py-3 text-white focus:border-primary focus:outline-none"
                required
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium mb-2">CPF</label>
            <div className="relative">
              <span className="material-icons absolute left-4 top-1/2 -translate-y-1/2 text-gray-400">
                badge
              </span>
              <input
                type="text"
                value={cpf}
                onChange={(e) => setCpf(e.target.value)}
                placeholder="000.000.000-00"
                className="w-full bg-dark-700 border border-dark-600 rounded-lg pl-12 pr-4 py-3 text-white focus:border-primary focus:outline-none"
                required
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium mb-2">Telefone</label>
            <div className="relative">
              <span className="material-icons absolute left-4 top-1/2 -translate-y-1/2 text-gray-400">
                phone
              </span>
              <input
                type="text"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                placeholder="(00) 00000-0000"
                className="w-full bg-dark-700 border border-dark-600 rounded-lg pl-12 pr-4 py-3 text-white focus:border-primary focus:outline-none"
                required
              />
            </div>
          </div>

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
            <p className="text-xs text-gray-500 mt-1">Mínimo de 6 caracteres</p>
          </div>

          <div>
            <label className="block text-sm font-medium mb-2">Confirmar Senha</label>
            <div className="relative">
              <span className="material-icons absolute left-4 top-1/2 -translate-y-1/2 text-gray-400">
                lock
              </span>
              <input
                type={showConfirmPassword ? 'text' : 'password'}
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full bg-dark-700 border border-dark-600 rounded-lg pl-12 pr-12 py-3 text-white focus:border-primary focus:outline-none"
                required
              />
              <button
                type="button"
                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                className="material-icons absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-primary cursor-pointer"
              >
                {showConfirmPassword ? 'visibility_off' : 'visibility'}
              </button>
            </div>
          </div>

          <button
            type="submit"
            className="w-full bg-primary hover:bg-primary/90 text-white font-semibold py-3 rounded-lg transition flex items-center justify-center gap-2"
          >
            <span className="material-icons">person_add</span>
            Criar Conta
          </button>
        </form>

        <div className="mt-6 text-center space-y-3">
          <button
            onClick={handleBackToLogin}
            className="text-gray-400 hover:text-primary transition flex items-center justify-center gap-2 mx-auto"
          >
            <span className="material-icons">arrow_back</span>
            Já tem uma conta? Faça login
          </button>
        </div>
      </div>
    </div>
  );
}

