<script setup lang="ts">

</script>
<template>
 <div id="professionalPanel" class="min-h-screen">
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
                <span class="material-icons text-primary">cut</span>
                <span class="font-medium">Profissional</span>
              </div>
              <div class="flex items-center gap-3">
                <span class="text-sm text-gray-400">Carlos Silva</span>
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
                onclick="showProfessionalSection('agenda')"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg bg-primary/10 text-primary"
                id="profAgendaLink"
              >
                <span class="material-icons">calendar_today</span>
                <span class="font-medium">Agenda</span>
              </a>
              <a
                onclick="showProfessionalSection('reviews')"
                href="#"
                class="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-dark-700 text-gray-400 hover:text-gray-200"
                id="profReviewsLink"
              >
                <span class="material-icons">star</span>
                <span class="font-medium">Minhas Avaliações</span>
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
                <span class="material-icons">content_cut</span>
                <span class="font-medium">Serviços</span>
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
            <div id="professionalAgendaSection">
              <div class="mb-8">
                <h2 class="text-3xl font-bold mb-2">
                  Atendimentos de Carlos Silva
                </h2>
                <p class="text-gray-400" id="currentDate">
                  19 de Outubro de 2025
                </p>
              </div>

              <div
                class="bg-dark-800 rounded-xl border border-dark-600 overflow-hidden"
              >
                <table class="w-full">
                  <thead class="bg-dark-700">
                    <tr>
                      <th class="px-6 py-4 text-left text-sm font-semibold">
                        Hora
                      </th>
                      <th class="px-6 py-4 text-left text-sm font-semibold">
                        Cliente
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
                      <td class="px-6 py-4 font-medium">09:00</td>
                      <td class="px-6 py-4">João Pereira</td>
                      <td class="px-6 py-4">Corte de cabelo</td>
                      <td class="px-6 py-4">
                        <span
                          class="status-badge bg-blue-500/10 text-blue-400 px-3 py-1 rounded-full text-sm"
                        >
                          <span class="material-icons text-xs">schedule</span>
                          Agendado
                        </span>
                      </td>
                      <td class="px-6 py-4">
                        <div class="flex gap-2">
                          <button
                            onclick="startAppointment(1)"
                            class="bg-green-500/10 text-green-400 hover:bg-green-500/20 px-4 py-2 rounded-lg text-sm font-medium transition"
                          >
                            Iniciar Atendimento
                          </button>
                          <button
                            onclick="cancelProfessionalAppointment(1)"
                            class="bg-red-500/10 text-red-400 hover:bg-red-500/20 px-3 py-2 rounded-lg text-sm font-medium transition"
                          >
                            Cancelar
                          </button>
                        </div>
                      </td>
                    </tr>
                    <tr class="hover:bg-dark-700">
                      <td class="px-6 py-4 font-medium">10:30</td>
                      <td class="px-6 py-4">Lucas Lima</td>
                      <td class="px-6 py-4">Barba + Corte</td>
                      <td class="px-6 py-4">
                        <span
                          class="status-badge bg-primary/10 text-primary px-3 py-1 rounded-full text-sm"
                        >
                          <span class="material-icons text-xs">autorenew</span>
                          Em andamento
                        </span>
                      </td>
                      <td class="px-6 py-4">
                        <button
                          onclick="finishAppointment(2)"
                          class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg text-sm font-medium transition"
                        >
                          Finalizar Atendimento
                        </button>
                      </td>
                    </tr>
                    <tr class="hover:bg-dark-700 opacity-50">
                      <td class="px-6 py-4 font-medium">12:00</td>
                      <td class="px-6 py-4 text-gray-500">—</td>
                      <td class="px-6 py-4 text-gray-500">—</td>
                      <td class="px-6 py-4">
                        <span
                          class="status-badge bg-gray-500/10 text-gray-400 px-3 py-1 rounded-full text-sm"
                        >
                          <span class="material-icons text-xs"
                            >event_available</span
                          >
                          Horário livre
                        </span>
                      </td>
                      <td class="px-6 py-4 text-gray-500">—</td>
                    </tr>
                    <tr class="hover:bg-dark-700">
                      <td class="px-6 py-4 font-medium">14:00</td>
                      <td class="px-6 py-4">Pedro Santos</td>
                      <td class="px-6 py-4">Corte Social</td>
                      <td class="px-6 py-4">
                        <span
                          class="status-badge bg-blue-500/10 text-blue-400 px-3 py-1 rounded-full text-sm"
                        >
                          <span class="material-icons text-xs"
                            >check_circle</span
                          >
                          Confirmado
                        </span>
                      </td>
                      <td class="px-6 py-4">
                        <div class="flex gap-2">
                          <button
                            onclick="startAppointment(3)"
                            class="bg-green-500/10 text-green-400 hover:bg-green-500/20 px-4 py-2 rounded-lg text-sm font-medium transition"
                          >
                            Iniciar Atendimento
                          </button>
                          <button
                            onclick="cancelProfessionalAppointment(3)"
                            class="bg-red-500/10 text-red-400 hover:bg-red-500/20 px-3 py-2 rounded-lg text-sm font-medium transition"
                          >
                            Cancelar
                          </button>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <div id="professionalReviewsSection" class="hidden">
              <div class="mb-8">
                <h2 class="text-3xl font-bold mb-2">Minhas Avaliações</h2>
                <p class="text-gray-400">Feedback dos seus clientes</p>
              </div>

              <div class="grid grid-cols-4 gap-6 mb-8">
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
                  <h3 class="text-3xl font-bold mb-1">4.9</h3>
                  <p class="text-gray-400 text-sm">Avaliação Geral</p>
                </div>

                <div class="bg-dark-800 border border-dark-600 rounded-xl p-6">
                  <div class="flex items-center justify-between mb-4">
                    <span class="material-icons text-primary text-4xl"
                      >rate_review</span
                    >
                    <span
                      class="bg-primary/10 text-primary px-3 py-1 rounded-full text-sm font-semibold"
                      >Total</span
                    >
                  </div>
                  <h3 class="text-3xl font-bold mb-1">127</h3>
                  <p class="text-gray-400 text-sm">Total de Avaliações</p>
                </div>

                <div class="bg-dark-800 border border-dark-600 rounded-xl p-6">
                  <div class="flex items-center justify-between mb-4">
                    <span class="material-icons text-green-400 text-4xl"
                      >thumb_up</span
                    >
                    <span
                      class="bg-green-500/10 text-green-400 px-3 py-1 rounded-full text-sm font-semibold"
                      >5 Estrelas</span
                    >
                  </div>
                  <h3 class="text-3xl font-bold mb-1">98</h3>
                  <p class="text-gray-400 text-sm">Avaliações Máximas</p>
                </div>

                <div class="bg-dark-800 border border-dark-600 rounded-xl p-6">
                  <div class="flex items-center justify-between mb-4">
                    <span class="material-icons text-blue-400 text-4xl"
                      >trending_up</span
                    >
                    <span
                      class="bg-blue-500/10 text-blue-400 px-3 py-1 rounded-full text-sm font-semibold"
                      >Este Mês</span
                    >
                  </div>
                  <h3 class="text-3xl font-bold mb-1">18</h3>
                  <p class="text-gray-400 text-sm">Novas Avaliações</p>
                </div>
              </div>

              <div
                class="bg-dark-800 rounded-xl border border-dark-600 overflow-hidden"
              >
                <div
                  class="px-6 py-4 bg-dark-700 flex items-center justify-between"
                >
                  <h3 class="text-xl font-semibold">Avaliações Recentes</h3>
                  <div class="flex gap-2">
                    <select
                      class="bg-dark-600 border border-dark-500 rounded-lg px-3 py-2 text-sm focus:outline-none focus:border-primary"
                    >
                      <option>Todas as avaliações</option>
                      <option>5 estrelas</option>
                      <option>4 estrelas</option>
                      <option>3 estrelas</option>
                      <option>2 estrelas</option>
                      <option>1 estrela</option>
                    </select>
                  </div>
                </div>
                <div class="p-6 space-y-4">
                  <div
                    class="flex items-start gap-4 p-4 bg-dark-700 rounded-lg"
                  >
                    <div
                      class="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center flex-shrink-0"
                    >
                      <span class="material-icons text-primary">person</span>
                    </div>
                    <div class="flex-1">
                      <div class="flex items-center justify-between mb-2">
                        <div>
                          <h4 class="font-semibold">João Pereira</h4>
                          <p class="text-xs text-gray-400">
                            Corte + Barba • 18/10/2025
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
                      <p class="text-sm text-gray-300 mb-3">
                        "Excelente atendimento! Muito profissional e atencioso.
                        O corte ficou perfeito e o ambiente é muito agradável.
                        Com certeza voltarei!"
                      </p>
                      <div
                        class="flex items-center gap-2 text-xs text-gray-500 mb-3"
                      >
                        <span class="material-icons text-xs">verified</span>
                        <span>Atendimento verificado</span>
                      </div>
                      <button
                        onclick="openProfessionalResponseModal(1, 'João Pereira', 'Corte + Barba', 5)"
                        class="text-primary hover:text-orange-400 text-sm font-medium flex items-center gap-1 transition"
                      >
                        <span class="material-icons text-sm">reply</span>
                        Responder avaliação
                      </button>
                    </div>
                  </div>

                  <div
                    class="flex items-start gap-4 p-4 bg-dark-700 rounded-lg"
                  >
                    <div
                      class="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center flex-shrink-0"
                    >
                      <span class="material-icons text-primary">person</span>
                    </div>
                    <div class="flex-1">
                      <div class="flex items-center justify-between mb-2">
                        <div>
                          <h4 class="font-semibold">Lucas Lima</h4>
                          <p class="text-xs text-gray-400">
                            Corte Social • 17/10/2025
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
                      <p class="text-sm text-gray-300 mb-3">
                        "Muito bom, recomendo! Profissional dedicado e com ótimo
                        gosto."
                      </p>
                      <div
                        class="flex items-center gap-2 text-xs text-gray-500 mb-3"
                      >
                        <span class="material-icons text-xs">verified</span>
                        <span>Atendimento verificado</span>
                      </div>
                      <button
                        onclick="openProfessionalResponseModal(2, 'Lucas Lima', 'Corte Social', 4)"
                        class="text-primary hover:text-orange-400 text-sm font-medium flex items-center gap-1 transition"
                      >
                        <span class="material-icons text-sm">reply</span>
                        Responder avaliação
                      </button>
                    </div>
                  </div>

                  <div
                    class="flex items-start gap-4 p-4 bg-dark-700 rounded-lg"
                  >
                    <div
                      class="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center flex-shrink-0"
                    >
                      <span class="material-icons text-primary">person</span>
                    </div>
                    <div class="flex-1">
                      <div class="flex items-center justify-between mb-2">
                        <div>
                          <h4 class="font-semibold">Rafael Costa</h4>
                          <p class="text-xs text-gray-400">
                            Barba Completa • 15/10/2025
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
                      <p class="text-sm text-gray-300 mb-3">
                        "Perfeito! Melhor barbeiro da região. Sempre pontual e
                        caprichoso no trabalho."
                      </p>
                      <div
                        class="flex items-center gap-2 text-xs text-gray-500 mb-3"
                      >
                        <span class="material-icons text-xs">verified</span>
                        <span>Atendimento verificado</span>
                      </div>
                      <button
                        onclick="openProfessionalResponseModal(3, 'Rafael Costa', 'Barba Completa', 5)"
                        class="text-primary hover:text-orange-400 text-sm font-medium flex items-center gap-1 transition"
                      >
                        <span class="material-icons text-sm">reply</span>
                        Responder avaliação
                      </button>
                    </div>
                  </div>

                  <div
                    class="flex items-start gap-4 p-4 bg-dark-700 rounded-lg"
                  >
                    <div
                      class="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center flex-shrink-0"
                    >
                      <span class="material-icons text-primary">person</span>
                    </div>
                    <div class="flex-1">
                      <div class="flex items-center justify-between mb-2">
                        <div>
                          <h4 class="font-semibold">Marcos Oliveira</h4>
                          <p class="text-xs text-gray-400">
                            Corte + Barba • 14/10/2025
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
                      <p class="text-sm text-gray-300 mb-3">
                        "Excelente profissional! Sempre atento aos detalhes.
                        Ambiente limpo e organizado."
                      </p>
                      <div
                        class="flex items-center gap-2 text-xs text-gray-500 mb-3"
                      >
                        <span class="material-icons text-xs">verified</span>
                        <span>Atendimento verificado</span>
                      </div>
                      <button
                        onclick="openProfessionalResponseModal(4, 'Marcos Oliveira', 'Corte + Barba', 5)"
                        class="text-primary hover:text-orange-400 text-sm font-medium flex items-center gap-1 transition"
                      >
                        <span class="material-icons text-sm">reply</span>
                        Responder avaliação
                      </button>
                    </div>
                  </div>

                  <div
                    class="flex items-start gap-4 p-4 bg-dark-700 rounded-lg"
                  >
                    <div
                      class="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center flex-shrink-0"
                    >
                      <span class="material-icons text-primary">person</span>
                    </div>
                    <div class="flex-1">
                      <div class="flex items-center justify-between mb-2">
                        <div>
                          <h4 class="font-semibold">André Santos</h4>
                          <p class="text-xs text-gray-400">
                            Corte Infantil • 12/10/2025
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
                      <p class="text-sm text-gray-300 mb-3">
                        "Meu filho adora! Muito paciente com as crianças.
                        Parabéns pelo trabalho!"
                      </p>
                      <div
                        class="flex items-center gap-2 text-xs text-gray-500 mb-3"
                      >
                        <span class="material-icons text-xs">verified</span>
                        <span>Atendimento verificado</span>
                      </div>
                      <button
                        onclick="openProfessionalResponseModal(5, 'André Santos', 'Corte Infantil', 5)"
                        class="text-primary hover:text-orange-400 text-sm font-medium flex items-center gap-1 transition"
                      >
                        <span class="material-icons text-sm">reply</span>
                        Responder avaliação
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </main>
        </div>
      </div>
</template>
