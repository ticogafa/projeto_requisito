import { useAgendamentosPorCliente, useServicosOferecidos } from '@/hooks/UseFetch';
import type { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';
import AppointmentsTable from '@/views/Cliente/AppointmentsTable';
import NewAppointmentModal from '@/views/Cliente/NewAppointmentModal';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';

export default function ClientView() {
  const [modalVisible, setModalVisible] = useState(false);
  const { data: servicos } = useServicosOferecidos();

  // TODO: Pegar clienteId do contexto de autenticação
  const clienteId = 1;
  const { data: agendamentos, setData: setAgendamentos } = useAgendamentosPorCliente(clienteId);

  useEffect(() => {
    document.title = 'Página do Cliente';
    toast.success('Bem-vindo!');
  }, []);

  const handleEdit = (id: number) => {
    toast.info(`Editar agendamento ${id} - Em desenvolvimento`);
  };

  const handleCancel = (id: number) => {
    toast.info(`Cancelar agendamento ${id} - Em desenvolvimento`);
  };

  const handleRate = (id: number) => {
    toast.info(`Avaliar agendamento ${id} - Em desenvolvimento`);
  };

  const handleSuccess = (novoAgendamento: AgendamentoInterface) => {
    // Adiciona o novo agendamento à lista
    setAgendamentos([...agendamentos, novoAgendamento]);
  };

  return (
    <div className="min-h-screen">
      <NewAppointmentModal
        servicos={servicos}
        visible={modalVisible}
        closeModal={() => setModalVisible(false)}
        onSuccess={handleSuccess}
      />

      <header className="bg-dark-800 border-b border-dark-600 px-8 py-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <span className="material-icons text-primary text-4xl">
              content_cut
            </span>
            <h1 className="text-2xl font-bold">Sistema Barbearia</h1>
          </div>
          <div className="flex items-center gap-6">
            <div
              className="flex items-center gap-2 bg-primary/10% px-4 py-2 rounded-lg" // <-- Corrigido
            >
              <span className="material-icons text-primary">person</span>
              <span className="font-medium">Cliente</span>
            </div>
            <div className="flex items-center gap-3">
              <span className="text-sm text-gray-400">João Pereira</span>
              <button
                onClick={
                  () => {
                    // logout()
                  }
                }
                className="material-icons text-gray-400 hover:text-primary cursor-pointer"
              >
                logout
              </button>
            </div>
          </div>
        </div>
      </header>

      <div className="flex">
        <aside className="w-64 bg-dark-800 min-h-screen border-r border-dark-600 p-6">
          <nav className="space-y-2">
            <a
              onClick={
                () => {
                  // navigateToAppointments()
                }
              }
              href="#"
              className="flex items-center gap-3 px-4 py-3 rounded-lg bg-primary/10% text-primary" // <-- Corrigido
            >
              <span className="material-icons">event</span>
              <span className="font-medium">Meus Agendamentos</span>
            </a>
            <a
              onClick={
                () => {
                  // showToast('Em breve', 'Esta funcionalidade estará disponível em breve.', 'info'); return false;
                }
              }
              href="#"
              className="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
            >
              <span className="material-icons">history</span>
              <span className="font-medium">Histórico</span>
            </a>
            <a
              onClick={
                () => {
                  // showToast('Em breve', 'Esta funcionalidade estará disponível em breve.', 'info'); return false;
                }
              }
              href="#"
              className="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
            >
              <span className="material-icons">card_giftcard</span>
              <span className="font-medium">Vouchers e Pontos</span>
            </a>
            <a
              onClick={
                () => {
                  // showToast('Em breve', 'Esta funcionalidade estará disponível em breve.', 'info'); return false;
                }
              }
              href="#"
              className="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
            >
              <span className="material-icons">person</span>
              <span className="font-medium">Perfil</span>
            </a>
          </nav>
        </aside>

        <main className="flex-1 p-8">
          <div className="mb-8 flex items-center justify-between">
            <div>
              <h2 className="text-3xl font-bold mb-2">Olá, Cliente</h2>
              <p className="text-gray-400">
                Gerencie seus agendamentos e avaliações
              </p>
            </div>
            <button
              onClick={() => setModalVisible(true)}
              className="bg-primary hover:bg-primary/90 text-white px-6 py-3 rounded-lg font-medium transition flex items-center gap-2"
            >
              <span className="material-icons">add</span>
              Novo Agendamento
            </button>
          </div>

          <AppointmentsTable
            agendamentos={agendamentos}
            onEdit={handleEdit}
            onCancel={handleCancel}
            onRate={handleRate}
          />
        </main>
      </div>
    </div>
  );
}
