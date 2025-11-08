<script setup lang="ts">
import { useNotificationStore } from '@/stores/notificationStore';
import { onMounted } from 'vue';

const { showToast } = useNotificationStore();

onMounted(()=> {
  console.log('toasted!')
  showToast('Em breve', 'Esta funcionalidade estará disponível em breve.',)
})

</script>

<template>
<div id="clientPanel" class="min-h-screen">
        <header class="bg-dark-800 border-b border-dark-600 px-8 py-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-3">
              <span class="material-icons text-primary text-4xl"
                >content_cut</span>
              <h1 class="text-2xl font-bold">Sistema Barbearia</h1>
            </div>
            <div class="flex items-center gap-6">
              <div
                class="flex items-center gap-2 bg-primary/10 px-4 py-2 rounded-lg"
              >
                <span class="material-icons text-primary">person</span>
                <span class="font-medium">Cliente</span>
              </div>
              <div class="flex items-center gap-3">
                <span class="text-sm text-gray-400">João Pereira</span>
                <button
                  onclick="logout()"
                  class="material-icons text-gray-400 hover:text-primary cursor-pointer"
                >
                  logout
                </button>
              </div>
            </div>
          </div>
        </header>

        <div class="flex">
          <aside
            class="w-64 bg-dark-800 min-h-screen border-r border-dark-600 p-6"
          >
            <nav class="space-y-2">
              <a
                onclick="return false;"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg bg-primary/10 text-primary"
              >
                <span class="material-icons">event</span>
                <span class="font-medium">Meus Agendamentos</span>
              </a>
              <a
                onclick="showToast('Em breve', 'Esta funcionalidade estará disponível em breve.', 'info'); return false;"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
              >
                <span class="material-icons">history</span>
                <span class="font-medium">Histórico</span>
              </a>
              <a
                onclick="showToast('Em breve', 'Esta funcionalidade estará disponível em breve.', 'info'); return false;"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
              >
                <span class="material-icons">card_giftcard</span>
                <span class="font-medium">Vouchers e Pontos</span>
              </a>
              <a
                onclick="showToast('Em breve', 'Esta funcionalidade estará disponível em breve.', 'info'); return false;"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
              >
                <span class="material-icons">person</span>
                <span class="font-medium">Perfil</span>
              </a>
            </nav>
          </aside>

          <main class="flex-1 p-8">
            <div class="mb-8 flex items-center justify-between">
              <div>
                <h2 class="text-3xl font-bold mb-2">Olá, João Pereira</h2>
                <p class="text-gray-400">
                  Gerencie seus agendamentos e avaliações
                </p>
              </div>
              <button
                onclick="openNewAppointmentModal()"
                class="bg-primary hover:bg-primary/90 text-white px-6 py-3 rounded-lg font-medium transition flex items-center gap-2"
              >
                <span class="material-icons">add</span>
                Novo Agendamento
              </button>
            </div>

            <div
              class="bg-dark-800 rounded-xl border border-dark-600 overflow-hidden"
            >
              <table class="w-full">
                <thead class="bg-dark-700">
                  <tr>
                    <th class="px-6 py-4 text-left text-sm font-semibold">
                      Data
                    </th>
                    <th class="px-6 py-4 text-left text-sm font-semibold">
                      Profissional
                    </th>
                    <th class="px-6 py-4 text-left text-sm font-semibold">
                      Serviço
                    </th>
                    <th class="px-6 py-4 text-left text-sm font-semibold">
                      Status
                    </th>
                    <th class="px-6 py-4 text-left text-sm font-semibold">
                      Ações
                    </th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-dark-600">
                  <tr class="hover:bg-dark-700">
                    <td class="px-6 py-4 font-medium">09/10 09:00</td>
                    <td class="px-6 py-4">Carlos Silva</td>
                    <td class="px-6 py-4">Corte + Barba</td>
                    <td class="px-6 py-4">
                      <span
                        class="status-badge bg-blue-500/10 text-blue-400 px-3 py-1 rounded-full text-sm"
                      >
                        <span class="material-icons text-xs">check_circle</span>
                        Confirmado
                      </span>
                    </td>
                    <td class="px-6 py-4">
                      <div class="flex gap-2">
                        <button
                          onclick="openEditAppointmentModal(1, '09/10 09:00', 'Carlos Silva', 'Corte + Barba', 'Confirmado', '')"
                          class="bg-blue-500/10 text-blue-400 hover:bg-blue-500/20 px-3 py-2 rounded-lg text-sm font-medium transition"
                        >
                          Editar
                        </button>
                        <button
                          onclick="cancelAppointment(1)"
                          class="bg-red-500/10 text-red-400 hover:bg-red-500/20 px-3 py-2 rounded-lg text-sm font-medium transition"
                        >
                          Cancelar
                        </button>
                      </div>
                    </td>
                  </tr>
                  <tr class="hover:bg-dark-700">
                    <td class="px-6 py-4 font-medium">07/10 10:30</td>
                    <td class="px-6 py-4">Lucas Lima</td>
                    <td class="px-6 py-4">Corte Social</td>
                    <td class="px-6 py-4">
                      <span
                        class="status-badge bg-green-500/10 text-green-400 px-3 py-1 rounded-full text-sm"
                      >
                        <span class="material-icons text-xs">done_all</span>
                        Concluído
                      </span>
                    </td>
                    <td class="px-6 py-4">
                      <button
                        onclick="openRatingModal('Lucas Lima', 'Corte Social')"
                        class="bg-primary/10 text-primary hover:bg-primary/20 px-4 py-2 rounded-lg text-sm font-medium transition"
                      >
                        Avaliar Profissional
                      </button>
                    </td>
                  </tr>
                  <tr class="hover:bg-dark-700">
                    <td class="px-6 py-4 font-medium">05/10 15:00</td>
                    <td class="px-6 py-4">João Pereira</td>
                    <td class="px-6 py-4">Corte Infantil</td>
                    <td class="px-6 py-4">
                      <span
                        class="status-badge bg-red-500/10 text-red-400 px-3 py-1 rounded-full text-sm"
                      >
                        <span class="material-icons text-xs">cancel</span>
                        Cancelado
                      </span>
                    </td>
                    <td class="px-6 py-4 text-gray-500">—</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </main>
        </div>
      </div>
</template>
