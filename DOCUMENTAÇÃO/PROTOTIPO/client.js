/* Client-specific logic extracted from script.js
   Moved functions that are only used by the Client UI: booking flow, cancel, rating.
   Shared utilities (showToast, closeModal, generateTimeSlots) remain in script.js.
*/

let selectedTimeSlot = null;
let appointmentData = {};

function openNewAppointmentModal() {
  document.getElementById("newAppointmentModal").classList.remove("hidden");
  setMinDate();
  resetAppointmentForm();
}

function setMinDate() {
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);
  const dateString = tomorrow.toISOString().split("T")[0];
  document.getElementById("appointmentDate").setAttribute("min", dateString);
}

function resetAppointmentForm() {
  document.getElementById("appointmentService").value = "";
  document.getElementById("appointmentDate").value = "";
  document.getElementById("appointmentProfessional").value = "";
  document.getElementById("appointmentNotes").value = "";
  document.getElementById("serviceInfo").classList.add("hidden");
  document.getElementById("appointmentSummary").classList.add("hidden");
  document.getElementById("availableSlots").innerHTML =
    '<div class="text-center text-gray-400 text-sm col-span-4 py-8">Selecione um serviço e uma data para ver os horários disponíveis</div>';
  selectedTimeSlot = null;
  appointmentData = {};
}

function updateServiceInfo() {
  const select = document.getElementById("appointmentService");
  const option = select.options[select.selectedIndex];

  if (option.value) {
    const duration = option.getAttribute("data-duration");
    const price = option.getAttribute("data-price");

    document.getElementById("serviceDuration").textContent = duration;
    document.getElementById("servicePrice").textContent = price;
    document.getElementById("serviceInfo").classList.remove("hidden");

    appointmentData.service = option.text.split(" - ")[0];
    appointmentData.duration = duration;
    appointmentData.price = price;

    loadAvailableSlots();
  } else {
    document.getElementById("serviceInfo").classList.add("hidden");
  }
}

function loadAvailableSlots() {
  const service = document.getElementById("appointmentService").value;
  const date = document.getElementById("appointmentDate").value;
  const professional = document.getElementById("appointmentProfessional").value;

  if (!service || !date) {
    document.getElementById("availableSlots").innerHTML =
      '<div class="text-center text-gray-400 text-sm col-span-4 py-8">Selecione um serviço e uma data para ver os horários disponíveis</div>';
    return;
  }

  // Simular carregamento de horários disponíveis
  const slots = generateTimeSlots(professional);

  let slotsHTML = "";
  slots.forEach((slot) => {
    slotsHTML += `
              <button type="button" onclick="selectTimeSlot(this, '${slot.time}', ${slot.available})" 
                  class="time-slot ${slot.available ? "bg-dark-700 hover:bg-primary/20 hover:border-primary" : "bg-dark-700/50 cursor-not-allowed"} 
                  border border-dark-600 rounded-lg py-3 text-sm font-medium transition ${!slot.available ? "text-gray-600" : ""}"
                  ${!slot.available ? "disabled" : ""}>
                  ${slot.time}
              </button>
          `;
  });

  document.getElementById("availableSlots").innerHTML = slotsHTML;
}

function selectTimeSlot(element, time, available) {
  if (!available) return;

  document.querySelectorAll(".time-slot").forEach((slot) => {
    slot.classList.remove("bg-primary", "border-primary", "text-white");
  });

  element.classList.add("bg-primary", "border-primary", "text-white");

  selectedTimeSlot = time;
  appointmentData.time = time;

  updateAppointmentSummary();
}

function updateAppointmentSummary() {
  if (!selectedTimeSlot) return;

  const service = document.getElementById("appointmentService");
  const date = document.getElementById("appointmentDate").value;
  const professional = document.getElementById("appointmentProfessional");

  const dateObj = new Date(date + "T00:00:00");
  const formattedDate = dateObj.toLocaleDateString("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });

  const [hours, minutes] = selectedTimeSlot.split(":");
  const endTime = new Date();
  endTime.setHours(
    parseInt(hours),
    parseInt(minutes) + parseInt(appointmentData.duration),
  );
  const endTimeStr = `${endTime.getHours().toString().padStart(2, "0")}:${endTime.getMinutes().toString().padStart(2, "0")}`;

  document.getElementById("summaryService").textContent = appointmentData.service;
  document.getElementById("summaryDate").textContent = formattedDate;
  document.getElementById("summaryTime").textContent = `${selectedTimeSlot} - ${endTimeStr}`;
  document.getElementById("summaryDuration").textContent = `${appointmentData.duration} minutos`;
  document.getElementById("summaryProfessional").textContent =
    professional.value ? professional.options[professional.selectedIndex].text : "Sistema escolherá automaticamente";
  document.getElementById("summaryPrice").textContent = `R$ ${parseFloat(appointmentData.price).toFixed(2)}`;

  document.getElementById("appointmentSummary").classList.remove("hidden");
}

function submitNewAppointment(event) {
  event.preventDefault();

  if (!selectedTimeSlot) {
    showToast("Atenção", "Por favor, selecione um horário disponível.", "warning", "yellow");
    return;
  }

  const date = document.getElementById("appointmentDate").value;

  const appointmentDateTime = new Date(date + "T" + selectedTimeSlot);
  const now = new Date();
  const hoursDifference = (appointmentDateTime - now) / (1000 * 60 * 60);

  if (hoursDifference < 2) {
    showToast("Atenção", "Agendamentos devem ser feitos com pelo menos 2 horas de antecedência.", "warning", "yellow");
    return;
  }

  appointmentData.status = "Pendente";
  appointmentData.date = date;
  appointmentData.notes = document.getElementById("appointmentNotes").value;

  closeModal("newAppointmentModal");
  showToast("Agendamento Criado!", `Seu agendamento para ${appointmentData.date} às ${selectedTimeSlot} foi criado. Status: Pendente.`, "check_circle");
  resetAppointmentForm();
}

function cancelAppointment(id) {
  document.getElementById("cancelModal").classList.remove("hidden");
}

function confirmCancel() {
  closeModal("cancelModal");
  showToast("Agendamento Cancelado", "Seu agendamento foi cancelado com sucesso.", "cancel", "red");
}

function openRatingModal(professional, service) {
  document.getElementById("ratingProfessionalName").textContent = professional;
  document.getElementById("ratingService").textContent = service;
  currentRating = 0;
  resetStars();
  document.getElementById("ratingModal").classList.remove("hidden");
}

function setRating(stars) {
  currentRating = stars;
  const starElements = document.querySelectorAll("#starRating .material-icons");
  starElements.forEach((star, index) => {
    star.style.color = index < stars ? "#FF8C00" : "#4a5568";
  });
}

function resetStars() {
  const starElements = document.querySelectorAll("#starRating .material-icons");
  starElements.forEach((star) => {
    star.style.color = "#4a5568";
  });
}

function submitRating() {
  if (currentRating === 0) {
    alert("Por favor, selecione uma avaliação!");
    return;
  }
  closeModal("ratingModal");
  showToast("Avaliação Enviada", "Obrigado pelo seu feedback!", "star", "yellow");
}
