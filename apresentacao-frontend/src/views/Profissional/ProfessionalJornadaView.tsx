import { useAuth } from '@/auth/AuthContext';
import AuthService from '@/services/AuthService';
import JornadaManager from '@/components/Profissional/JornadaManager';
import { ProfessionalLayout } from '@/views/Profissional/components';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

export default function ProfessionalJornadaView() {
  const navigate = useNavigate();
  const { user } = useAuth();
  
  // TODO: Pegar profissionalId do backend baseado no user.email
  const profissionalId = 1;
  const userName = user?.email?.split('@')[0] || 'Profissional';

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
