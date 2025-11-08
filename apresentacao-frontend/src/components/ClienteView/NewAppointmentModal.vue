<template>
     <div
        id="newAppointmentModal"
        onclick="closeModalOnBackdrop(event, 'newAppointmentModal')"
        class="hidden fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4"
      >
        <div
          class="bg-dark-800 rounded-2xl p-8 max-w-2xl w-full border border-dark-600 max-h-[90vh] overflow-y-auto"
          onclick="event.stopPropagation()"
        >
          <div class="flex items-center justify-between mb-6">
            <div class="flex items-center gap-3">
              <span class="material-icons text-primary text-4xl"
                >event_available</span
              >
              <h3 class="text-2xl font-bold">Novo Agendamento</h3>
            </div>
            <button
              onclick="closeModal('newAppointmentModal')"
              class="material-icons text-gray-400 hover:text-primary cursor-pointer"
            >
              close
            </button>
          </div>

          <form onsubmit="submitNewAppointment(event)" class="space-y-6">
            <div>
              <label class="block text-sm font-medium mb-2">Serviço *</label>
              <select
                id="appointmentService"
                onchange="updateServiceInfo()"
                required
                class="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
              >
                <option value="">Selecione um serviço</option>
                <option
                  value="corte_simples"
                  data-duration="30"
                  data-price="35.00"
                >
                  Corte Simples - R$ 35,00 (30 min)
                </option>
                <option
                  value="corte_barba"
                  data-duration="50"
                  data-price="60.00"
                >
                  Corte + Barba - R$ 60,00 (50 min)
                </option>
                <option value="barba" data-duration="25" data-price="30.00">
                  Barba Completa - R$ 30,00 (25 min)
                </option>
                <option
                  value="corte_social"
                  data-duration="40"
                  data-price="45.00"
                >
                  Corte Social - R$ 45,00 (40 min)
                </option>
                <option
                  value="corte_infantil"
                  data-duration="25"
                  data-price="30.00"
                >
                  Corte Infantil - R$ 30,00 (25 min)
                </option>
                <option
                  value="barba_express"
                  data-duration="15"
                  data-price="20.00"
                >
                  Barba Express - R$ 20,00 (15 min)
                </option>
              </select>
              <div id="serviceInfo" class="mt-2 text-sm text-gray-400 hidden">
                <div class="flex items-center gap-2">
                  <span class="material-icons text-xs">schedule</span>
                  <span
                    >Duração: <span id="serviceDuration"></span> minutos</span
                  >
                  <span class="mx-2">•</span>
                  <span class="material-icons text-xs">payments</span>
                  <span>Valor: R$ <span id="servicePrice"></span></span>
                </div>
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium mb-2">Data *</label>
              <input
                type="date"
                id="appointmentDate"
                onchange="loadAvailableSlots()"
                required
                min=""
                class="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
              />
              <p class="mt-2 text-xs text-gray-400">
                <span class="material-icons text-xs align-middle">info</span>
                Horário de funcionamento: 8h às 18h
              </p>
            </div>

            <div>
              <label
                class="block text-sm font-medium mb-2 flex items-center gap-2"
              >
                Profissional
                <span class="text-xs text-gray-400 font-normal"
                  >(opcional - sistema escolherá automaticamente)</span
                >
              </label>
              <select
                id="appointmentProfessional"
                onchange="loadAvailableSlots()"
                class="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
              >
                <option value="">
                  Sistema escolherá o primeiro disponível
                </option>
                <option value="carlos">Carlos Silva</option>
                <option value="pedro">Pedro Souza</option>
                <option value="lucas">Lucas Lima</option>
                <option value="joao">João Pereira</option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium mb-3"
                >Horários Disponíveis *</label
              >
              <div id="availableSlots" class="grid grid-cols-4 gap-2">
                <div class="text-center text-gray-400 text-sm col-span-4 py-8">
                  Selecione um serviço e uma data para ver os horários
                  disponíveis
                </div>
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium mb-2">Observações</label>
              <textarea
                id="appointmentNotes"
                rows="3"
                placeholder="Adicione informações adicionais (opcional)"
                class="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none resize-none"
              ></textarea>
            </div>

            <div
              id="appointmentSummary"
              class="hidden bg-dark-700 rounded-lg p-4 space-y-2"
            >
              <h4 class="font-semibold text-primary mb-3">
                Resumo do Agendamento
              </h4>
              <div class="space-y-2 text-sm">
                <div class="flex justify-between">
                  <span class="text-gray-400">Serviço:</span>
                  <span class="font-medium" id="summaryService">-</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-400">Data:</span>
                  <span class="font-medium" id="summaryDate">-</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-400">Horário:</span>
                  <span class="font-medium" id="summaryTime">-</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-400">Duração:</span>
                  <span class="font-medium" id="summaryDuration">-</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-400">Profissional:</span>
                  <span class="font-medium" id="summaryProfessional">-</span>
                </div>
                <div
                  class="flex justify-between border-t border-dark-600 pt-2 mt-2"
                >
                  <span class="text-gray-400">Valor:</span>
                  <span
                    class="font-semibold text-primary text-lg"
                    id="summaryPrice"
                    >-</span
                  >
                </div>
              </div>
            </div>

            <div class="flex gap-4">
              <button
                type="button"
                onclick="closeModal('newAppointmentModal')"
                class="flex-1 bg-dark-700 hover:bg-dark-600 text-gray-300 py-3 rounded-lg font-medium transition"
              >
                Cancelar
              </button>
              <button
                type="submit"
                class="flex-1 bg-primary hover:bg-primary/90 text-white py-3 rounded-lg font-medium transition"
              >
                Confirmar Agendamento
              </button>
            </div>
          </form>
        </div>
      </div>
   <div
        id="newAppointmentModal"
        onclick="closeModalOnBackdrop(event, 'newAppointmentModal')"
        class="hidden fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4"
      >
        <div
          class="bg-dark-800 rounded-2xl p-8 max-w-2xl w-full border border-dark-600 max-h-[90vh] overflow-y-auto"
          onclick="event.stopPropagation()"
        >
          <div class="flex items-center justify-between mb-6">
            <div class="flex items-center gap-3">
              <span class="material-icons text-primary text-4xl"
                >event_available</span
              >
              <h3 class="text-2xl font-bold">Novo Agendamento</h3>
            </div>
            <button
              onclick="closeModal('newAppointmentModal')"
              class="material-icons text-gray-400 hover:text-primary cursor-pointer"
            >
              close
            </button>
          </div>

          <form onsubmit="submitNewAppointment(event)" class="space-y-6">
            <div>
              <label class="block text-sm font-medium mb-2">Serviço *</label>
              <select
                id="appointmentService"
                onchange="updateServiceInfo()"
                required
                class="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
              >
                <option value="">Selecione um serviço</option>
                <option
                  value="corte_simples"
                  data-duration="30"
                  data-price="35.00"
                >
                  Corte Simples - R$ 35,00 (30 min)
                </option>
                <option
                  value="corte_barba"
                  data-duration="50"
                  data-price="60.00"
                >
                  Corte + Barba - R$ 60,00 (50 min)
                </option>
                <option value="barba" data-duration="25" data-price="30.00">
                  Barba Completa - R$ 30,00 (25 min)
                </option>
                <option
                  value="corte_social"
                  data-duration="40"
                  data-price="45.00"
                >
                  Corte Social - R$ 45,00 (40 min)
                </option>
                <option
                  value="corte_infantil"
                  data-duration="25"
                  data-price="30.00"
                >
                  Corte Infantil - R$ 30,00 (25 min)
                </option>
                <option
                  value="barba_express"
                  data-duration="15"
                  data-price="20.00"
                >
                  Barba Express - R$ 20,00 (15 min)
                </option>
              </select>
              <div id="serviceInfo" class="mt-2 text-sm text-gray-400 hidden">
                <div class="flex items-center gap-2">
                  <span class="material-icons text-xs">schedule</span>
                  <span
                    >Duração: <span id="serviceDuration"></span> minutos</span
                  >
                  <span class="mx-2">•</span>
                  <span class="material-icons text-xs">payments</span>
                  <span>Valor: R$ <span id="servicePrice"></span></span>
                </div>
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium mb-2">Data *</label>
              <input
                type="date"
                id="appointmentDate"
                onchange="loadAvailableSlots()"
                required
                min=""
                class="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
              />
              <p class="mt-2 text-xs text-gray-400">
                <span class="material-icons text-xs align-middle">info</span>
                Horário de funcionamento: 8h às 18h
              </p>
            </div>

            <div>
              <label
                class="block text-sm font-medium mb-2 flex items-center gap-2"
              >
                Profissional
                <span class="text-xs text-gray-400 font-normal"
                  >(opcional - sistema escolherá automaticamente)</span
                >
              </label>
              <select
                id="appointmentProfessional"
                onchange="loadAvailableSlots()"
                class="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
              >
                <option value="">
                  Sistema escolherá o primeiro disponível
                </option>
                <option value="carlos">Carlos Silva</option>
                <option value="pedro">Pedro Souza</option>
                <option value="lucas">Lucas Lima</option>
                <option value="joao">João Pereira</option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium mb-3"
                >Horários Disponíveis *</label
              >
              <div id="availableSlots" class="grid grid-cols-4 gap-2">
                <div class="text-center text-gray-400 text-sm col-span-4 py-8">
                  Selecione um serviço e uma data para ver os horários
                  disponíveis
                </div>
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium mb-2">Observações</label>
              <textarea
                id="appointmentNotes"
                rows="3"
                placeholder="Adicione informações adicionais (opcional)"
                class="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none resize-none"
              ></textarea>
            </div>

            <div
              id="appointmentSummary"
              class="hidden bg-dark-700 rounded-lg p-4 space-y-2"
            >
              <h4 class="font-semibold text-primary mb-3">
                Resumo do Agendamento
              </h4>
              <div class="space-y-2 text-sm">
                <div class="flex justify-between">
                  <span class="text-gray-400">Serviço:</span>
                  <span class="font-medium" id="summaryService">-</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-400">Data:</span>
                  <span class="font-medium" id="summaryDate">-</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-400">Horário:</span>
                  <span class="font-medium" id="summaryTime">-</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-400">Duração:</span>
                  <span class="font-medium" id="summaryDuration">-</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-gray-400">Profissional:</span>
                  <span class="font-medium" id="summaryProfessional">-</span>
                </div>
                <div
                  class="flex justify-between border-t border-dark-600 pt-2 mt-2"
                >
                  <span class="text-gray-400">Valor:</span>
                  <span
                    class="font-semibold text-primary text-lg"
                    id="summaryPrice"
                    >-</span
                  >
                </div>
              </div>
            </div>

            <div class="flex gap-4">
              <button
                type="button"
                onclick="closeModal('newAppointmentModal')"
                class="flex-1 bg-dark-700 hover:bg-dark-600 text-gray-300 py-3 rounded-lg font-medium transition"
              >
                Cancelar
              </button>
              <button
                type="submit"
                class="flex-1 bg-primary hover:bg-primary/90 text-white py-3 rounded-lg font-medium transition"
              >
                Confirmar Agendamento
              </button>
            </div>
          </form>
        </div>
      </div>

</template>
