import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import AuthService from '@/services/AuthService';

type UserRole = 'cliente' | 'profissional' | 'admin';

interface ProfileOption {
  role: UserRole;
  icon: string;
  title: string;
  description: string;
  color: string;
}

const profiles: ProfileOption[] = [
  {
    role: 'cliente',
    icon: 'person',
    title: 'Cliente',
    description: 'Agende serviços e acompanhe seu histórico',
    color: 'from-blue-500 to-blue-600'
  },
  {
    role: 'profissional',
    icon: 'content_cut',
    title: 'Profissional',
    description: 'Gerencie sua agenda e atendimentos',
    color: 'from-primary to-orange-600'
  },
  {
    role: 'admin',
    icon: 'settings',
    title: 'Administrador',
    description: 'Controle completo do sistema',
    color: 'from-purple-500 to-purple-600'
  }
];

export default function ProfileSelection() {
  const navigate = useNavigate();
  const [selectedRole, setSelectedRole] = useState<UserRole | null>(null);
  const [isAdminLoggedIn, setIsAdminLoggedIn] = useState(false);
  const [showAdminRedirectMessage, setShowAdminRedirectMessage] = useState(false);

  useEffect(() => {
    const checkAdminStatus = () => {
      const currentUserRole = AuthService.getCurrentUserRole();
      if (currentUserRole === 'admin') {
        setIsAdminLoggedIn(true);
        // Only show message if we are on the root path and not already navigating to admin
        if (location.pathname === '/') {
          setShowAdminRedirectMessage(true);
        }
      } else {
        setIsAdminLoggedIn(false);
        setShowAdminRedirectMessage(false);
      }
    };

    // Check status initially and whenever the path changes (e.g., after login and redirect attempts)
    checkAdminStatus();
    // Re-check if user data might have changed (e.g., after login)
    // This is a simple re-check, more robust would be a listener
    window.addEventListener('storage', checkAdminStatus);
    return () => {
      window.removeEventListener('storage', checkAdminStatus);
    };
  }, [location.pathname]);


  const handleSelectProfile = (role: UserRole) => {
    setSelectedRole(role);
    // Salvar role selecionado no localStorage para uso após login
    localStorage.setItem('selectedRole', role);
    toast.info(`Perfil ${role} selecionado`);
    navigate('/login');
  };

  const handleAdminRedirect = () => {
    navigate('/admin');
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-8 bg-dark-900">
      <div className="text-center mb-12">
        <div className="flex items-center justify-center gap-3 mb-4">
          <span className="material-icons text-primary text-6xl">content_cut</span>
          <h1 className="text-5xl font-bold">Barbearia César</h1>
        </div>
        <p className="text-gray-400 text-lg">
          Selecione seu perfil para continuar
        </p>

        {showAdminRedirectMessage && isAdminLoggedIn && (
          <div className="mt-8 bg-yellow-600/20 text-yellow-300 border border-yellow-500 rounded-lg p-4 flex flex-col md:flex-row items-center justify-between shadow-md">
            <p className="text-base md:text-lg text-center md:text-left mb-3 md:mb-0">
              Você logou como admin, mas não foi redirecionado? Clique aqui para acessar o painel.
            </p>
            <button
              onClick={handleAdminRedirect}
              className="px-6 py-2 bg-yellow-500 hover:bg-yellow-600 text-white font-semibold rounded-lg transition-colors duration-300 shadow-lg"
            >
              Ir para Admin
            </button>
          </div>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-6xl w-full">
        {profiles.map((profile) => (
          <button
            key={profile.role}
            onClick={() => handleSelectProfile(profile.role)}
            className={`profile-card bg-dark-800 rounded-2xl p-8 border-2 transition-all duration-300 ${
              selectedRole === profile.role
                ? 'border-primary shadow-lg shadow-primary/50'
                : 'border-dark-600 hover:border-primary/50'
            }`}
          >
            <div
              className={`w-20 h-20 rounded-full bg-linear-to-br ${profile.color} flex items-center justify-center mb-6 mx-auto`}
            >
              <span className="material-icons text-white text-4xl">
                {profile.icon}
              </span>
            </div>
            <h3 className="text-2xl font-bold mb-2">{profile.title}</h3>
            <p className="text-gray-400">{profile.description}</p>
          </button>
        ))}
      </div>
    </div>
  );
}
