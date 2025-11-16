import { useAgendamentosPorCliente, useServicosOferecidos } from '@/hooks/UseFetch';
import type { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';

import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';

import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { ClientLayout, NewAppointmentModal, EditAppointmentModal, ClientHeader, AppointmentsTable } from '@/views/Cliente/components';

export default function ClientView() {
  const [modalVisible, setModalVisible] = useState(false);
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [agendamentoParaEditar, setAgendamentoParaEditar] = useState<AgendamentoInterface | null>(null);
  const { data: servicos } = useServicosOferecidos();
  const { setLoading } = useLoadingStore();
  const mainService = MainService.getInstance();

  // TODO: Pegar clienteId e userName do contexto de autenticação
  const clienteId = 1;
  const userName = 'João Pereira';
  const { data: agendamentos, setData: setAgendamentos } = useAgendamentosPorCliente(clienteId);

  useEffect(() => {
    document.title = 'Página do Cliente';
    toast.success('Bem-vindo!');
  }, []);

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

    mainService.cancelarAgendamento(
      id,
      clienteId,
      (response) => {
        toast.success('Agendamento cancelado com sucesso!');
        // Atualizar lista localmente
        setAgendamentos(
          agendamentos.map(a => a.id === id ? response.data : a)
        );
      },
      (error) => {
        const errorData = error.response?.data as { message?: string } | undefined;
        const message = errorData?.message || 'Erro ao cancelar agendamento';
        toast.error(message);
      },
      () => {
        setLoading(false);
      }
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

  const handleLogout = () => {
    // TODO: Implementar logout
    toast.info('Logout - Em desenvolvimento');
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
