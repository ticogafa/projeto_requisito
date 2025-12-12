import { useAgendamentosPorCliente } from '@/hooks/useAgendamentosPorCliente';
import { useServicosOferecidos } from '@/hooks/useServicosOferecidos';
import type { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';

import { useAuth } from '@/auth/AuthContext';
import AuthService from '@/services/AuthService';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';

import { AppointmentsTable, ClientHeader, ClientLayout, EditAppointmentModal, NewAppointmentModal } from '@/views/Cliente/components';
import { AxiosError, AxiosResponse } from 'axios';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

export default function ClientView() {
  const [modalVisible, setModalVisible] = useState(false);
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [agendamentoParaEditar, setAgendamentoParaEditar] = useState<AgendamentoInterface | null>(null);
  const { data: servicos } = useServicosOferecidos();
  const { setLoading } = useLoadingStore();
  const mainService = MainService.getInstance();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [clienteId, setClienteId] = useState<number | null>(null);
  const userName = user?.email?.split('@')[0] || 'Usuário';

  useEffect(() => {
    if (user?.email) {
      setLoading(true);
      mainService.getClientePorEmail(
        user.email,
        (response) => {
          setClienteId(response.data.id);
        },
        (error) => {
           console.error('Erro ao buscar cliente:', error);
           toast.error('Não foi possível carregar os dados do cliente.');
        },
        () => setLoading(false)
      );
    }
  }, [user]);

  const { data: agendamentos, setData: setAgendamentos } = useAgendamentosPorCliente(clienteId || 0);

  const handleEdit = (id: number) => {
    const agendamento = agendamentos.find(a => a.id === id);
    if (agendamento) {
      setAgendamentoParaEditar(agendamento);
      setEditModalVisible(true);
    }
  };

  const handleCancel = (id: number) => {
    if (!clienteId) {
        toast.error('Erro de identificação do cliente.');
        return;
    }

    if (!window.confirm('Tem certeza que deseja cancelar este agendamento?')) {
      return;
    }

    setLoading(true);

    const successCallback = (response: AxiosResponse<AgendamentoInterface>) => {
      toast.success('Agendamento cancelado com sucesso!');
      setAgendamentos(
        agendamentos.map(a => a.id === id ? response.data : a)
      );
    };

    const errorCallback = (error: AxiosError) => {
      const errorData = error.response?.data as { message?: string } | undefined;
      const message = errorData?.message || 'Erro ao cancelar agendamento';
      toast.error(message);
    };

    const finallyCallback = () => setLoading(false);

    mainService.cancelarAgendamento(
      id,
      clienteId,
      successCallback,
      errorCallback,
      finallyCallback
    );
  };

  const [ratingModalVisible, setRatingModalVisible] = useState(false);
  const [ratingAppointmentId, setRatingAppointmentId] = useState<number | null>(null);
  const [ratingValue, setRatingValue] = useState(5);

  const handleRate = (id: number) => {
    setRatingAppointmentId(id);
    setRatingValue(5);
    setRatingModalVisible(true);
  };

  const confirmRating = () => {
    if (!ratingAppointmentId) return;

    const agendamento = agendamentos.find(a => a.id === ratingAppointmentId);
    if (!agendamento || !agendamento.profissionalId) {
        toast.error('Erro ao identificar o profissional.');
        return;
    }

    setLoading(true);
    mainService.registrarAvaliacao(
        {
            profissionalId: agendamento.profissionalId,
            nota: ratingValue
        },
        () => {
            toast.success('Avaliação enviada com sucesso!');
            setRatingModalVisible(false);
            setRatingAppointmentId(null);
        },
        (error) => toast.error('Erro ao enviar avaliação: ' + (error.response?.data as any)?.message || error.message),
        () => setLoading(false)
    );
  };

  const handleSuccess = (novoAgendamento: AgendamentoInterface) => {
    setAgendamentos([...agendamentos, novoAgendamento]);
  };

  const handleEditSuccess = (agendamentoEditado: AgendamentoInterface) => {
    setAgendamentos(
      agendamentos.map(a => a.id === agendamentoEditado.id ? agendamentoEditado : a)
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
    <ClientLayout
      userName={userName}
      activeMenuItem="appointments"
      onLogout={handleLogout}
    >
      <NewAppointmentModal
        servicos={servicos}
        visible={modalVisible}
        closeModal={() => setModalVisible(false)}
        onSuccess={handleSuccess}
      />

      <EditAppointmentModal
        servicos={servicos}
        visible={editModalVisible}
        agendamento={agendamentoParaEditar}
        closeModal={() => {
          setEditModalVisible(false);
          setAgendamentoParaEditar(null);
        }}
        onSuccess={handleEditSuccess}
      />

      <ClientHeader
        userName={userName}
        onNewAppointment={() => setModalVisible(true)}
      />

      <AppointmentsTable
        agendamentos={agendamentos}
        onEdit={handleEdit}
        onCancel={handleCancel}
        onRate={handleRate}
      />

      {/* Rating Modal */}
      {ratingModalVisible && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
          <div className="bg-dark-800 rounded-xl p-6 w-full max-w-md border border-dark-600 shadow-xl">
            <h3 className="text-xl font-bold text-white mb-4">Avaliar Atendimento</h3>
            <p className="text-gray-400 mb-4">Como foi sua experiência?</p>
            
            <div className="flex justify-center gap-2 mb-6">
              {[1, 2, 3, 4, 5].map((star) => (
                <button
                  key={star}
                  onClick={() => setRatingValue(star)}
                  className="transition transform hover:scale-110 focus:outline-none"
                >
                  <span className={`material-icons text-4xl ${star <= ratingValue ? 'text-yellow-400' : 'text-gray-600'}`}>
                    star
                  </span>
                </button>
              ))}
            </div>

            <div className="flex justify-end gap-3">
              <button
                onClick={() => setRatingModalVisible(false)}
                className="px-4 py-2 rounded-lg text-gray-400 hover:text-white hover:bg-dark-700 transition"
              >
                Cancelar
              </button>
              <button
                onClick={confirmRating}
                className="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg font-medium transition"
              >
                Enviar Avaliação
              </button>
            </div>
          </div>
        </div>
      )}
    </ClientLayout>
  );
}
