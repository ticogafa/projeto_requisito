import { useAgendamentosPorCliente } from '@/hooks/useAgendamentosPorCliente';
import { useServicosOferecidos } from '@/hooks/useServicosOferecidos';
import type { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';

import { useAuth } from '@/auth/AuthContext';
import AuthService from '@/services/AuthService';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';

import { AppointmentsTable, ClientHeader, ClientLayout, EditAppointmentModal, NewAppointmentModal } from '@/views/Cliente/components';
import { AxiosError, AxiosResponse } from 'axios';
import { useState } from 'react';
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

  // TODO: Pegar clienteId do backend baseado no user.email
  const clienteId = 1;
  const userName = user?.email?.split('@')[0] || 'Usuário';
  const { data: agendamentos, setData: setAgendamentos } = useAgendamentosPorCliente(clienteId);

  const handleEdit = (id: number) => {
    const agendamento = agendamentos.find(a => a.id === id);
    if (agendamento) {
      setAgendamentoParaEditar(agendamento);
      setEditModalVisible(true);
    }
  };

  const handleCancel = (id: number) => {
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

  const handleRate = (id: number) => {
    toast.info(`Avaliar agendamento ${id} - Em desenvolvimento`);
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
    </ClientLayout>
  );
}
