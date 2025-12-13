import { useAuth } from '@/auth/AuthContext';
import AuthService from '@/services/AuthService';
import JornadaManager from '@/components/Profissional/JornadaManager';
import { ProfessionalLayout } from '@/views/Profissional/components';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';

export default function ProfessionalJornadaView() {
  const navigate = useNavigate();
  const { user } = useAuth();
  const mainService = MainService.getInstance();
  const { setLoading } = useLoadingStore();
  
  const [profissionalId, setProfissionalId] = useState<number | null>(null);
  const userName = user?.email?.split('@')[0] || 'Profissional';

  useEffect(() => {
    if (user?.email) {
      setLoading(true);
      mainService.getProfissionalPorEmail(
        user.email,
        (response) => {
          setProfissionalId(response.data.id.valor);
        },
        (error) => {
           console.error('Erro ao buscar profissional:', error);
           toast.error('Não foi possível carregar os dados do profissional.');
           // Optionally redirect if professional not found
           // navigate('/login'); 
        },
        () => setLoading(false)
      );
    }
  }, [user?.email, navigate, mainService, setLoading]);

  const handleLogout = async () => {
    const successCallback = () => {
      toast.success('Logout realizado com sucesso!');
      navigate('/');
    };

    const errorCallback = (error: string) => {
      toast.error(error);
    };

    AuthService.logout(successCallback, errorCallback);
  };

  if (profissionalId === null) {
    return (
        <ProfessionalLayout
            userName={userName}
            activeMenuItem="jornada"
            onLogout={handleLogout}
        >
            <div className="text-center text-gray-400 p-8">
                Carregando dados da jornada...
            </div>
        </ProfessionalLayout>
    );
  }

  return (
    <ProfessionalLayout
      userName={userName}
      activeMenuItem="jornada"
      onLogout={handleLogout}
    >
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-white mb-2">Minha Jornada</h1>
        <p className="text-gray-400">Configure seus horários de atendimento semanal.</p>
      </div>

      <JornadaManager profissionalId={profissionalId} />
    </ProfessionalLayout>
  );
}
