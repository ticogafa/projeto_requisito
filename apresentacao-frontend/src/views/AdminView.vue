<template>
<div id="adminPanel" class="min-h-screen">
        <header class="bg-dark-800 border-b border-dark-600 px-8 py-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-3">
              <span class="material-icons text-primary text-4xl"
                >content_cut</span
              >
              <h1 class="text-2xl font-bold">Sistema Barbearia</h1>
            </div>
            <div class="flex items-center gap-6">
              <div
                class="flex items-center gap-2 bg-primary/10 px-4 py-2 rounded-lg"
              >
                <span class="material-icons text-primary"
                  >admin_panel_settings</span
                >
                <span class="font-medium">Administrador</span>
              </div>
              <div class="flex items-center gap-3">
                <span class="text-sm text-gray-400">Admin Master</span>
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
                <span class="material-icons">dashboard</span>
                <span class="font-medium">Dashboard</span>
              </a>
              <a
                onclick="openAdminModal('appointmentManagement'); return false;"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
              >
                <span class="material-icons">event</span>
                <span class="font-medium">Agendamentos</span>
              </a>

              <a
                onclick="openAdminModal('professionalManagement'); return false;"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
              >
                <span class="material-icons">people</span>
                <span class="font-medium">Profissionais</span>
              </a>

              <a
                onclick="openAdminModal('serviceManagement'); return false;"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
              >
                <span class="material-icons">content_cut</span>
                <span class="font-medium">Serviços</span>
              </a>

              <a
                onclick="openAdminModal('stockManagement'); return false;"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
              >
                <span class="material-icons">inventory_2</span>
                <span class="font-medium">Estoque</span>
              </a>
              <a
                onclick="openAdminModal('cashControl'); return false;"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
              >
                <span class="material-icons">attach_money</span>
                <span class="font-medium">Financeiro</span>
              </a>
              <a
                onclick="showToast('Em breve', 'Esta funcionalidade estará disponível em breve.', 'info'); return false;"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
              >
                <span class="material-icons">group</span>
                <span class="font-medium">Clientes</span>
              </a>
            </nav>
          </aside>

          <main class="flex-1 p-8">
            <div class="mb-8">
              <h2 class="text-3xl font-bold mb-2">Painel Administrativo</h2>
              <p class="text-gray-400">Visão geral da operação</p>
            </div>

            <div class="grid grid-cols-4 gap-6 mb-8">
              <div class="bg-dark-800 border border-dark-600 rounded-xl p-6">
                <div class="flex items-center justify-between mb-4">
                  <span class="material-icons text-primary text-4xl"
                    >event</span
                  >
                  <span
                    class="bg-primary/10 text-primary px-3 py-1 rounded-full text-sm font-semibold"
                    >Hoje</span
                  >
                </div>
                <h3 class="text-3xl font-bold mb-1">24</h3>
                <p class="text-gray-400 text-sm">Agendamentos do Dia</p>
              </div>

              <div class="bg-dark-800 border border-dark-600 rounded-xl p-6">
                <div class="flex items-center justify-between mb-4">
                  <span class="material-icons text-green-400 text-4xl"
                    >people</span
                  >
                  <span
                    class="bg-green-500/10 text-green-400 px-3 py-1 rounded-full text-sm font-semibold"
                    >Ativos</span
                  >
                </div>
                <h3 class="text-3xl font-bold mb-1">8</h3>
                <p class="text-gray-400 text-sm">Profissionais Ativos</p>
              </div>

              <div class="bg-dark-800 border border-dark-600 rounded-xl p-6">
                <div class="flex items-center justify-between mb-4">
                  <span class="material-icons text-blue-400 text-4xl"
                    >attach_money</span
                  >
                  <span
                    class="bg-blue-500/10 text-blue-400 px-3 py-1 rounded-full text-sm font-semibold"
                    >Hoje</span
                  >
                </div>
                <h3 class="text-3xl font-bold mb-1">R$ 1.850</h3>
                <p class="text-gray-400 text-sm">Faturamento</p>
              </div>

              <div class="bg-dark-800 border border-dark-600 rounded-xl p-6">
                <div class="flex items-center justify-between mb-4">
                  <span class="material-icons text-yellow-400 text-4xl"
                    >star</span
                  >
                  <span
                    class="bg-yellow-500/10 text-yellow-400 px-3 py-1 rounded-full text-sm font-semibold"
                    >Média</span
                  >
                </div>
                <h3 class="text-3xl font-bold mb-1">4.8</h3>
                <p class="text-gray-400 text-sm">Avaliação Geral</p>
              </div>
            </div>

            <div class="grid grid-cols-4 gap-4 mb-8">
              <button
                onclick="openAdminModal('appointments')"
                class="bg-dark-800 border border-dark-600 hover:border-primary rounded-xl p-4 text-left transition group"
              >
                <span
                  class="material-icons text-primary text-3xl mb-2 group-hover:scale-110 transition"
                  >event_note</span
                >
                <h4 class="font-semibold mb-1">Agenda Geral</h4>
                <p class="text-xs text-gray-400">
                  Gerenciar todos os agendamentos
                </p>
              </button>

              <button
                onclick="openAdminModal('workSchedule')"
                class="bg-dark-800 border border-dark-600 hover:border-primary rounded-xl p-4 text-left transition group"
              >
                <span
                  class="material-icons text-green-400 text-3xl mb-2 group-hover:scale-110 transition"
                  >schedule</span
                >
                <h4 class="font-semibold mb-1">Jornada de Trabalho</h4>
                <p class="text-xs text-gray-400">
                  Configurar horários e bloqueios
                </p>
              </button>

              <button
                onclick="openAdminModal('services')"
                class="bg-dark-800 border border-dark-600 hover:border-primary rounded-xl p-4 text-left transition group"
              >
                <span
                  class="material-icons text-blue-400 text-3xl mb-2 group-hover:scale-110 transition"
                  >content_cut</span
                >
                <h4 class="font-semibold mb-1">Serviços e Produtos</h4>
                <p class="text-xs text-gray-400">Gerenciar catálogo</p>
              </button>

              <button
                onclick="openAdminModal('cashControl')"
                class="bg-dark-800 border border-dark-600 hover:border-primary rounded-xl p-4 text-left transition group"
              >
                <span
                  class="material-icons text-green-400 text-3xl mb-2 group-hover:scale-110 transition"
                  >account_balance_wallet</span
                >
                <h4 class="font-semibold mb-1">Controle de Caixa</h4>
                <p class="text-xs text-gray-400">Gerenciar entradas e saídas</p>
              </button>

              <button
                onclick="openAdminModal('reports')"
                class="bg-dark-800 border border-dark-600 hover:border-primary rounded-xl p-4 text-left transition group"
              >
                <span
                  class="material-icons text-blue-400 text-3xl mb-2 group-hover:scale-110 transition"
                  >bar_chart</span
                >
                <h4 class="font-semibold mb-1">Relatório de Desempenho</h4>
                <p class="text-xs text-gray-400">Métricas por profissional</p>
              </button>
            </div>

            <div
              class="bg-dark-800 rounded-xl border border-dark-600 overflow-hidden mb-6"
            >
              <div
                class="px-6 py-4 bg-dark-700 flex items-center justify-between"
              >
                <h3 class="text-xl font-semibold">Agendamentos Recentes</h3>
                  <button
                    onclick="openAdminModal('appointments')"
                    class="text-primary hover:text-orange-400 text-sm font-medium cursor-pointer px-4 py-2"
                  >
                    Ver todos
                  </button>
              </div>
              <table class="w-full">
                <thead class="bg-dark-700 border-t border-dark-600">
                  <tr>
                    <th class="px-6 py-3 text-left text-sm font-semibold">
                      Data/Hora
                    </th>
                    <th class="px-6 py-3 text-left text-sm font-semibold">
                      Cliente
                    </th>
                    <th class="px-6 py-3 text-left text-sm font-semibold">
                      Profissional
                    </th>
                    <th class="px-6 py-3 text-left text-sm font-semibold">
                      Serviço
                    </th>
                    <th class="px-6 py-3 text-left text-sm font-semibold">
                      Status
                    </th>
                    <th class="px-6 py-3 text-left text-sm font-semibold">
                      Ações
                    </th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-dark-600">
                  <tr class="hover:bg-dark-700">
                    <td class="px-6 py-3 text-sm">19/10 09:00</td>
                    <td class="px-6 py-3 text-sm">João Pereira</td>
                    <td class="px-6 py-3 text-sm">Carlos Silva</td>
                    <td class="px-6 py-3 text-sm">Corte + Barba</td>
                    <td class="px-6 py-3">
                      <span
                        class="status-badge bg-primary/10 text-primary px-2 py-1 rounded-full text-xs"
                      >
                        <span class="material-icons text-xs">autorenew</span>
                        Em andamento
                      </span>
                    </td>
                    <td class="px-6 py-3">
                      <div class="flex gap-2">
                        <button
                          onclick="confirmAdminAppointment(1)"
                          class="bg-green-500/10 text-green-400 hover:bg-green-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition"
                        >
                          Confirmar
                        </button>
                        <button
                          onclick="cancelAdminAppointment(1)"
                          class="bg-red-500/10 text-red-400 hover:bg-red-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition"
                        >
                          Cancelar
                        </button>
                      </div>
                    </td>
                  </tr>
                  <tr class="hover:bg-dark-700">
                    <td class="px-6 py-3 text-sm">19/10 10:30</td>
                    <td class="px-6 py-3 text-sm">Lucas Lima</td>
                    <td class="px-6 py-3 text-sm">Pedro Souza</td>
                    <td class="px-6 py-3 text-sm">Corte Social</td>
                    <td class="px-6 py-3">
                      <span
                        class="status-badge bg-blue-500/10 text-blue-400 px-2 py-1 rounded-full text-xs">
                        <span class="material-icons text-xs">schedule</span>
                        Agendado
                      </span>
                    </td>
                    <td class="px-6 py-3">
                      <div class="flex gap-2">
                        <button
                          onclick="confirmAdminAppointment(2)"
                          class="bg-green-500/10 text-green-400 hover:bg-green-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition"
                        >
                          Confirmar
                        </button>
                        <button
                          onclick="cancelAdminAppointment(2)"
                          class="bg-red-500/10 text-red-400 hover:bg-red-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition"
                        >
                          Cancelar
                        </button>
                      </div>
                    </td>
                  </tr>
                  <tr class="hover:bg-dark-700">
                    <td class="px-6 py-3 text-sm">19/10 14:00</td>
                    <td class="px-6 py-3 text-sm">Rafael Costa</td>
                    <td class="px-6 py-3 text-sm">Carlos Silva</td>
                    <td class="px-6 py-3 text-sm">Barba Completa</td>
                    <td class="px-6 py-3">
                      <span
                        class="status-badge bg-blue-500/10 text-blue-400 px-2 py-1 rounded-full text-xs"
                      >
                        <span class="material-icons text-xs">check_circle</span>
                        Confirmado
                      </span>
                    </td>
                    <td class="px-6 py-3">
                      <div class="flex gap-2">
                        <button
                          onclick="confirmAdminAppointment(3)"
                          class="bg-green-500/10 text-green-400 hover:bg-green-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition"
                        >
                          Confirmar
                        </button>
                        <button
                          onclick="cancelAdminAppointment(3)"
                          class="bg-red-500/10 text-red-400 hover:bg-red-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition"
                        >
                          Cancelar
                        </button>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div
              class="bg-dark-800 rounded-xl border border-dark-600 overflow-hidden"
            >
              <div
                class="px-6 py-4 bg-dark-700 flex items-center justify-between"
              >
                <h3 class="text-xl font-semibold">Avaliações Recentes</h3>
                <button
                  onclick="openAdminModal('reviews')"
                  class="text-primary hover:text-orange-400 text-sm font-medium cursor-pointer"
                >
                  Ver todas
                </button>
              </div>
              <div class="p-6 space-y-4">
                <div
                  class="flex items-start gap-4 p-4 bg-dark-700 rounded-lg hover:bg-dark-600 transition"
                >
                  <div
                    class="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center"
                  >
                    <span class="material-icons text-primary">person</span>
                  </div>
                  <div class="flex-1">
                    <div class="flex items-center justify-between mb-2">
                      <div>
                        <h4 class="font-semibold">João Pereira</h4>
                        <p class="text-xs text-gray-400">
                          Avaliou Carlos Silva - Corte + Barba
                        </p>
                      </div>
                      <div class="flex gap-1">
                        <span class="material-icons text-yellow-400 text-sm"
                          >star</span
                        >
                        <span class="material-icons text-yellow-400 text-sm"
                          >star</span
                        >
                        <span class="material-icons text-yellow-400 text-sm"
                          >star</span
                        >
                        <span class="material-icons text-yellow-400 text-sm"
                          >star</span
                        >
                        <span class="material-icons text-yellow-400 text-sm"
                          >star</span
                        >
                      </div>
                    </div>
                    <p class="text-sm text-gray-300 mb-2">
                      "Excelente atendimento! Muito profissional e atencioso."
                    </p>
                    <button
                      onclick="openRespondReview(1)"
                      class="text-primary hover:text-orange-400 text-xs font-medium"
                    >
                      Responder avaliação
                    </button>
                  </div>
                </div>

                <div
                  class="flex items-start gap-4 p-4 bg-dark-700 rounded-lg hover:bg-dark-600 transition"
                >
                  <div
                    class="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center"
                  >
                    <span class="material-icons text-primary">person</span>
                  </div>
                  <div class="flex-1">
                    <div class="flex items-center justify-between mb-2">
                      <div>
                        <h4 class="font-semibold">Lucas Lima</h4>
                        <p class="text-xs text-gray-400">
                          Avaliou Pedro Souza - Corte Social
                        </p>
                      </div>
                      <div class="flex gap-1">
                        <span class="material-icons text-yellow-400 text-sm"
                          >star</span
                        >
                        <span class="material-icons text-yellow-400 text-sm"
                          >star</span
                        >
                        <span class="material-icons text-yellow-400 text-sm"
                          >star</span
                        >
                        <span class="material-icons text-yellow-400 text-sm"
                          >star</span
                        >
                        <span class="material-icons text-gray-600 text-sm"
                          >star</span
                        >
                      </div>
                    </div>
                    <p class="text-sm text-gray-300 mb-2">
                      "Muito bom, recomendo!"
                    </p>
                    <button
                      onclick="openRespondReview(2)"
                      class="text-primary hover:text-orange-400 text-xs font-medium"
                    >
                      Responder avaliação
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </main>
        </div>
      </div>
</template>
<script setup lang="ts">

</script>
<style lang="">

</style>
