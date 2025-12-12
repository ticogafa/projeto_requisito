import { useAuth } from '@/auth/AuthContext';
import { useAgendamentosPorProfissional } from '@/hooks/useAgendamentosPorProfissional';
import { useServicosOferecidos } from '@/hooks/useServicosOferecidos';
import { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';
import { ServicoOferecido } from '@/interfaces/ServicoOferecidoInterface';
import AuthService from '@/services/AuthService';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import { AppointmentDetailsModal, ProfessionalAgendaTable, ProfessionalCalendar, ProfessionalHeader, ProfessionalLayout } from '@/views/Profissional/components';
import { ServiceTimer } from '@/views/Profissional/components/ServiceTimer';
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
  
  // State for active service
  const [activeService, setActiveService] = useState<{ id: number; startTime: Date } | null>(null);
  
  // State for revenue modal
  const [showRevenueModal, setShowRevenueModal] = useState(false);
  const [selectedServices, setSelectedServices] = useState<ServicoOferecido[]>([]);
  const [finishingAppointmentId, setFinishingAppointmentId] = useState<number | null>(null);

  // TODO: Pegar profissionalId do backend baseado no user.email
  const profissionalId = 1;
  const userName = user?.email?.split('@')[0] || 'Profissional';
  
  const { data: agendamentos, setData: setAgendamentos } = useAgendamentosPorProfissional(profissionalId);
  const { data: servicosOferecidos } = useServicosOferecidos();

  const handleStart = (id: number) => {
    if (activeService) {
      toast.warning('Você já tem um atendimento em andamento!');
      return;
    }
    
    // Start timer locally
    setActiveService({ id, startTime: new Date() });
    
    // Update status to EM_ANDAMENTO locally (and ideally backend too, but for now just local/timer focus)
    setAgendamentos(agendamentos.map(a => a.id === id ? { ...a, status: 'EM_ANDAMENTO' } : a));
    toast.success('Atendimento iniciado!');
    setSelectedAppointment(null);
  };

  const handleFinish = (id: number) => {
    if (!activeService || activeService.id !== id) {
       toast.error('Este atendimento não está em andamento.');
       return;
    }
    
    setFinishingAppointmentId(id);
    
    // Pre-select the service from the appointment
    const appointment = agendamentos.find(a => a.id === id);
    if (appointment) {
        const currentService = servicosOferecidos.find(s => {
            const sId = typeof s.id === 'object' ? s.id.valor : s.id;
            return sId === appointment.servicoId;
        });
        if (currentService) {
            setSelectedServices([currentService]);
        } else {
            setSelectedServices([]);
        }
    }
    
    setShowRevenueModal(true);
    setSelectedAppointment(null);
  };

  const toggleServiceSelection = (service: ServicoOferecido) => {
    const sId = typeof service.id === 'object' ? service.id.valor : service.id;
    const isSelected = selectedServices.some(s => {
        const currentId = typeof s.id === 'object' ? s.id.valor : s.id;
        return currentId === sId;
    });

    if (isSelected) {
        setSelectedServices(selectedServices.filter(s => {
            const currentId = typeof s.id === 'object' ? s.id.valor : s.id;
            return currentId !== sId;
        }));
    } else {
        setSelectedServices([...selectedServices, service]);
    }
  };

  const confirmFinish = () => {
    if (!finishingAppointmentId || !activeService) return;

    const totalRevenue = selectedServices.reduce((acc, s) => acc + s.preco, 0);

    setLoading(true);
    const endTime = new Date();
    
    // 1. Register execution in backend
    mainService.registrarAtendimento(
        {
            profissionalId,
            valor: totalRevenue,
            inicio: activeService.startTime.toISOString(),
            fim: endTime.toISOString()
        },
        () => {
            // 2. Update appointment status to CONCLUIDO
            setAgendamentos(agendamentos.map(a => a.id === finishingAppointmentId ? { ...a, status: 'CONCLUIDO' } : a));
            toast.success(`Atendimento finalizado! Receita: R$ ${totalRevenue.toFixed(2)}`);
            
            // Cleanup
            setActiveService(null);
            setFinishingAppointmentId(null);
            setShowRevenueModal(false);
            setSelectedServices([]);
        },
        (error) => toast.error('Erro ao registrar atendimento: ' + (error.response?.data as any)?.message || error.message),
        () => setLoading(false)
    );
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

      {activeService && (
        <ServiceTimer startTime={activeService.startTime} />
      )}

      {/* Revenue Modal */}
      {showRevenueModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
          <div className="bg-dark-800 rounded-xl p-6 w-full max-w-md border border-dark-600 shadow-xl max-h-[90vh] flex flex-col">
            <h3 className="text-xl font-bold text-white mb-4">Finalizar Atendimento</h3>
            <p className="text-gray-400 mb-4">Selecione os serviços realizados:</p>
            
            <div className="flex-1 overflow-y-auto mb-6 space-y-2 pr-2">
              {servicosOferecidos.map((servico) => {
                const sId = typeof servico.id === 'object' ? servico.id.valor : servico.id;
                const isSelected = selectedServices.some(s => {
                    const currentId = typeof s.id === 'object' ? s.id.valor : s.id;
                    return currentId === sId;
                });
                
                return (
                  <div 
                    key={sId}
                    onClick={() => toggleServiceSelection(servico)}
                    className={`p-3 rounded-lg border cursor-pointer transition flex justify-between items-center ${
                        isSelected 
                        ? 'bg-primary/20 border-primary text-white' 
                        : 'bg-dark-700 border-dark-600 text-gray-300 hover:bg-dark-600'
                    }`}
                  >
                    <div>
                        <div className="font-medium">{servico.nome}</div>
                        <div className="text-xs opacity-70">{servico.duracaoMinutos} min</div>
                    </div>
                    <div className="font-bold">R$ {servico.preco.toFixed(2)}</div>
                  </div>
                );
              })}
            </div>

            <div className="bg-dark-900 p-4 rounded-lg mb-6 flex justify-between items-center border border-dark-600">
                <span className="text-gray-400">Total Receita:</span>
                <span className="text-xl font-bold text-green-400">
                    R$ {selectedServices.reduce((acc, s) => acc + s.preco, 0).toFixed(2)}
                </span>
            </div>

            <div className="flex justify-end gap-3">
              <button
                onClick={() => setShowRevenueModal(false)}
                className="px-4 py-2 rounded-lg text-gray-400 hover:text-white hover:bg-dark-700 transition"
              >
                Cancelar
              </button>
              <button
                onClick={confirmFinish}
                className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg font-medium transition flex items-center gap-2"
              >
                <span className="material-icons text-sm">check</span>
                Confirmar e Finalizar
              </button>
            </div>
          </div>
        </div>
      )}

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
