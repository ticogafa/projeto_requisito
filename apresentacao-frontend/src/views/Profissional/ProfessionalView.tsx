import { useAuth } from '@/auth/AuthContext';
import { useAgendamentosPorProfissional } from '@/hooks/useAgendamentosPorProfissional';
import { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';
import AuthService from '@/services/AuthService';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import { AppointmentDetailsModal, ProfessionalAgendaTable, ProfessionalCalendar, ProfessionalHeader, ProfessionalLayout } from '@/views/Profissional/components';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

export default function ProfessionalView() {
  const navigate = useNavigate();
  const { user } = useAuth();
  const { setLoading } = useLoadingStore();
  const mainService = MainService.getInstance();
  const [viewMode, setViewMode] = useState<'list' | 'calendar'>('calendar');
  const [selectedAppointment, setSelectedAppointment] = useState<AgendamentoInterface | null>(null);

  // TODO: Pegar profissionalId do backend baseado no user.email
  const profissionalId = 1;
  const userName = user?.email?.split('@')[0] || 'Profissional';
  
  const { data: agendamentos, setData: setAgendamentos } = useAgendamentosPorProfissional(profissionalId);

  const handleStart = (id: number) => {
    toast.info(`Iniciar atendimento ${id} - Em desenvolvimento`);
    // Optionally refetch or update local state
    // For now, close modal, actual logic would update status on backend.
    setSelectedAppointment(null);
  };

  const handleFinish = (id: number) => {
    toast.info(`Finalizar atendimento ${id} - Em desenvolvimento`);
    setSelectedAppointment(null);
  };

  const handleCancel = (id: number) => {
    if (!window.confirm('Tem certeza que deseja cancelar este agendamento?')) {
      return;
    }
    setLoading(true);
    mainService.cancelarAgendamentoPorProfissional(
      id,
      profissionalId,
      (response) => {
        toast.success('Agendamento cancelado!');
        setAgendamentos(agendamentos.map(a => a.id === id ? response.data : a));
        setSelectedAppointment(null); // Close modal on success
      },
      (error) => toast.error('Erro ao cancelar agendamento: ' + (error.response?.data as any)?.message),
      () => setLoading(false)
    );
  };

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
      activeMenuItem="agenda"
      onLogout={handleLogout}
    >
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6">
        <ProfessionalHeader userName={userName} />
        
        <div className="flex bg-dark-800 rounded-lg p-1 border border-dark-600">
          <button
            onClick={() => setViewMode('list')}
            className={`px-4 py-2 rounded-md text-sm font-medium transition flex items-center gap-2 ${
              viewMode === 'list' 
                ? 'bg-primary text-white shadow-sm' 
                : 'text-gray-400 hover:text-white hover:bg-dark-700'
            }`}
          >
            <span className="material-icons text-sm">list</span>
            Lista
          </button>
          <button
            onClick={() => setViewMode('calendar')}
            className={`px-4 py-2 rounded-md text-sm font-medium transition flex items-center gap-2 ${
              viewMode === 'calendar' 
                ? 'bg-primary text-white shadow-sm' 
                : 'text-gray-400 hover:text-white hover:bg-dark-700'
            }`}
          >
            <span className="material-icons text-sm">calendar_view_week</span>
            Calendário
          </button>
        </div>
      </div>

      {viewMode === 'list' ? (
        <ProfessionalAgendaTable
          agendamentos={agendamentos}
          onStart={handleStart}
          onFinish={handleFinish}
          onCancel={handleCancel}
        />
      ) : (
        <ProfessionalCalendar
          agendamentos={agendamentos}
          onStart={handleStart}
          onFinish={handleFinish}
          onCancel={handleCancel}
          onAppointmentClick={setSelectedAppointment} // Pass the handler
        />
      )}

      <AppointmentDetailsModal
        agendamento={selectedAppointment}
        onClose={() => setSelectedAppointment(null)}
        onStart={handleStart}
        onFinish={handleFinish}
        onCancel={handleCancel}
      />
    </ProfessionalLayout>
  );
}
