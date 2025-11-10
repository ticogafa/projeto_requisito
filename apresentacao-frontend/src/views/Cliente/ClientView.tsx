// import { useState } from "react";
import { useServicosOferecidos } from '@/hooks/UseFetch';
import NewAppointmentModal from '@/views/Cliente/NewAppointmentModal';
import { useEffect, useState } from 'react';

export default function ClientView() {
  const [modalVisible, setModalVisible] = useState(false);
  const { data: servicos } = useServicosOferecidos();

  useEffect(() => {
    document.title = 'Página do Cliente';
  }, []);

  return (
    <div className="min-h-screen">
      <NewAppointmentModal
        servicos={servicos}
        visible={modalVisible}
        closeModal={() => setModalVisible(false)}
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
              <h2 className="text-3xl font-bold mb-2">Olá, João Pereira</h2>
              <p className="text-gray-400">
                Gerencie seus agendamentos e avaliações
              </p>
            </div>
            <button
              onClick={setModalVisible.bind(null, true)}
              className="bg-primary hover:bg-primary/90% text-white px-6 py-3 rounded-lg font-medium transition flex items-center gap-2" // <-- Corrigido
            >
              <span className="material-icons">add</span>
              Novo Agendamento
            </button>
          </div>

          <div className="bg-dark-800 rounded-xl border border-dark-600 overflow-hidden">
            <table className="w-full">
              <thead className="bg-dark-700">
                <tr>
                  <th className="px-6 py-4 text-left text-sm font-semibold">
                    Data
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold">
                    Profissional
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold">
                    Serviço
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold">
                    Status
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold">
                    Ações
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-dark-600">
                <tr className="hover:bg-dark-700">
                  <td className="px-6 py-4 font-medium">09/10 09:00</td>
                  <td className="px-6 py-4">Carlos Silva</td>
                  <td className="px-6 py-4">Corte + Barba</td>
                  <td className="px-6 py-4">
                    <span
                      className="status-badge bg-blue-500/10% text-blue-400 px-3 py-1 rounded-full text-sm" // <-- Corrigido
                    >
                      <span className="material-icons text-xs">
                        check_circle
                      </span>
                      Confirmado
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex gap-2">
                      <button
                        onClick={
                          () => {
                            // openEditAppointmentModal(1, '09/10 09:00', 'Carlos Silva', 'Corte + Barba', 'Confirmado', '')
                          }
                        }
                        className="bg-blue-500/10% text-blue-400 hover:bg-blue-500/20% px-3 py-2 rounded-lg text-sm font-medium transition" // <-- Corrigido (2x)
                      >
                        Editar
                      </button>
                      <button
                        onClick={
                          () => {
                            // cancelAppointment(1)
                          }
                        }
                        className="bg-red-500/10% text-red-400 hover:bg-red-500/20% px-3 py-2 rounded-lg text-sm font-medium transition" // <-- Corrigido
                      >
                        Cancelar
                      </button>
                    </div>
                  </td>
                </tr>
                <tr className="hover:bg-dark-700">
                  <td className="px-6 py-4 font-medium">07/10 10:30</td>
                  <td className="px-6 py-4">Lucas Lima</td>
                  <td className="px-6 py-4">Corte Social</td>
                  <td className="px-6 py-4">
                    <span
                      className="status-badge bg-green-500/10% text-green-400 px-3 py-1 rounded-full text-sm" // <-- Corrigido
                    >
                      <span className="material-icons text-xs">done_all</span>
                      Concluído
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <button
                      onClick={
                        () => {}
                        // openRatingModal('Lucas Lima', 'Corte Social')
                      }
                      className="bg-primary/10% text-primary hover:bg-primary/20% px-4 py-2 rounded-lg text-sm font-medium transition" // <-- Corrigido (2x)
                    >
                      Avaliar Profissional
                    </button>
                  </td>
                </tr>
                <tr className="hover:bg-dark-700">
                  <td className="px-6 py-4 font-medium">05/10 15:00</td>
                  <td className="px-6 py-4">João Pereira</td>
                  <td className="px-6 py-4">Corte Infantil</td>
                  <td className="px-6 py-4">
                    <span
                      className="status-badge bg-red-500/10% text-red-400 px-3 py-1 rounded-full text-sm" // <-- Corrigido
                    >
                      <span className="material-icons text-xs">cancel</span>
                      Cancelado
                    </span>
                  </td>
                  <td className="px-6 py-4 text-gray-500">—</td>
                </tr>
              </tbody>
            </table>
          </div>
        </main>
      </div>
    </div>
  );
}
