import type { ServicosOferecidosResponse } from '@/interfaces/ServicoOferecidoInterface';

interface NewAppointmentModalProps {
  visible: boolean;
  servicos: ServicosOferecidosResponse;
  closeModal: () => void;
}

// Informações que precisam vir do backend
//Agendamento:
// - Serviços disponíveis (nome, duração, preço)
// - Profissionais disponíveis (nome, id)
// - Horários disponíveis (baseado em serviço, data, profissional)

export default function NewAppointmentModal(props: NewAppointmentModalProps) {
  function submitNewAppointment(event: React.FormEvent) {
    event.preventDefault();
    // Lógica para submissão do novo agendamento
  }
  function updateServiceInfo() {
    // Lógica para atualizar informações do serviço selecionado
  }
  function loadAvailableSlots() {
    // Lógica para carregar horários disponíveis com base na data e profissional selecionados
  }

  if (!props.visible) return null;

  return (
    <div
      id="newAppointmentModal"
      onClick={props.closeModal}
      className="fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4"
    >
      <div
        className="bg-dark-800 rounded-2xl p-8 max-w-2xl w-full border border-dark-600 max-h-[90vh] overflow-y-auto"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-3">
            <span className="material-icons text-primary text-4xl"
            >event_available</span
            >
            <h3 className="text-2xl font-bold">Novo Agendamento</h3>
          </div>
          <button
            onClick={props.closeModal}
            className="material-icons text-gray-400 hover:text-primary cursor-pointer"
          >
            close
          </button>
        </div>

        <form onSubmit={submitNewAppointment} className="space-y-6">
          <div>
            <label className="block text-sm font-medium mb-2">Serviço *</label>
            <select
              id="appointmentService"
              onChange={updateServiceInfo}
              required
              className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
            >
              <option value="">Selecione um serviço</option>
              {props.servicos.map((servico) => (
                <option
                  key={servico.id}
                  value={servico.id}
                  data-duration={servico.duracaoMinutos}
                  data-price={servico.preco.toFixed(2)}
                  data-nome={servico.nome}
                >
                  {servico.nome} - R$ {servico.preco.toFixed(2)} ({servico.duracaoMinutos} min)
                </option>
              ))}
            </select>
            <div id="serviceInfo" className="mt-2 text-sm text-gray-400 hidden">
              <div className="flex items-center gap-2">
                <span className="material-icons text-xs">schedule</span>
                <span>
                  Duração: <span id="serviceDuration"></span> minutos
                </span>
                <span className="mx-2">•</span>
                <span className="material-icons text-xs">payments</span>
                <span>Valor: R$ <span id="servicePrice"></span></span>
              </div>
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium mb-2">Data *</label>
            <input
              type="date"
              id="appointmentDate"
              onChange={loadAvailableSlots}
              required
              min=""
              className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
            />
            <p className="mt-2 text-xs text-gray-400">
              <span className="material-icons text-xs align-middle">info</span>
              Horário de funcionamento: 8h às 18h
            </p>
          </div>

          <div>
            <label className="flex text-sm font-medium mb-2 items-center gap-2">
              Profissional
              <span className="text-xs text-gray-400 font-normal">
                (opcional - sistema escolherá automaticamente)
              </span>
            </label>
            <select
              id="appointmentProfessional"
              onChange={loadAvailableSlots}
              className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none"
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
            <label className="block text-sm font-medium mb-3"
            >Horários Disponíveis *</label
            >
            <div id="availableSlots" className="grid grid-cols-4 gap-2">
              <div className="text-center text-gray-400 text-sm col-span-4 py-8">
                Selecione um serviço e uma data para ver os horários
                disponíveis
              </div>
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium mb-2">Observações</label>
            <textarea
              id="appointmentNotes"
              rows={3}
              placeholder="Adicione informações adicionais (opcional)"
              className="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 text-white focus:border-primary focus:outline-none resize-none"
            ></textarea>
          </div>

          <div
            id="appointmentSummary"
            className="hidden bg-dark-700 rounded-lg p-4 space-y-2"
          >
            <h4 className="font-semibold text-primary mb-3">
              Resumo do Agendamento
            </h4>
            <div className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-gray-400">Serviço:</span>
                <span className="font-medium" id="summaryService">-</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-400">Data:</span>
                <span className="font-medium" id="summaryDate">-</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-400">Horário:</span>
                <span className="font-medium" id="summaryTime">-</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-400">Duração:</span>
                <span className="font-medium" id="summaryDuration">-</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-400">Profissional:</span>
                <span className="font-medium" id="summaryProfessional">-</span>
              </div>
              <div
                className="flex justify-between border-t border-dark-600 pt-2 mt-2"
              >
                <span className="text-gray-400">Valor:</span>
                <span
                  className="font-semibold text-primary text-lg"
                  id="summaryPrice"
                >-</span
                >
              </div>
            </div>
          </div>

          <div className="flex gap-4">
            <button
              type="button"
              onClick={props.closeModal}
              className="flex-1 bg-dark-700 hover:bg-dark-600 text-gray-300 py-3 rounded-lg font-medium transition"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 bg-primary hover:bg-primary/90 text-white py-3 rounded-lg font-medium transition"
            >
              Confirmar Agendamento
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
