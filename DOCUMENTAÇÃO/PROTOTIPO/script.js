let professionalsData = [
{
    id: "p1",
    name: "Carlos Silva",
    level: "Sênior",
    jornada: "Seg-Sex (8h-18h)",
    rating: "4.9/5.0",
    status: "Ativo",
},
{
    id: "p2",
    name: "Pedro Souza",
    level: "Pleno",
    jornada: "Seg-Sáb (10h-20h)",
    rating: "4.7/5.0",
    status: "Ativo",
},
{
    id: "p3",
    name: "Lucas Lima",
    level: "Sênior",
    jornada: "Ter-Sáb (8h-18h)",
    rating: "4.8/5.0",
    status: "Ativo",
}, // Editado no teste anterior
{
    id: "p4",
    name: "Marcos Andrade",
    level: "Sênior",
    jornada: "N/A",
    rating: "4.5/5.0",
    status: "Inativo",
},
];

let servicesData = [
{
    id: "s1",
    name: "Corte Simples",
    category: "Cortes",
    duration: 30,
    price: 35.0,
    assignedProfessionals: "3/4",
    status: "Ativo",
    assignedProfIds: ["p1", "p2", "p3"],
},
{
    id: "s2",
    name: "Corte + Barba",
    category: "Pacotes",
    duration: 50,
    price: 60.0,
    assignedProfessionals: "2/4",
    status: "Ativo",
    assignedProfIds: ["p1", "p4"],
},
{
    id: "s3",
    name: "Barba Completa",
    category: "Barba",
    duration: 25,
    price: 30.0,
    assignedProfessionals: "4/4",
    status: "Ativo",
    assignedProfIds: ["p1", "p2", "p3", "p4"],
},
{
    id: "s4",
    name: "Corte Social",
    category: "Cortes",
    duration: 40,
    price: 45.0,
    assignedProfessionals: "1/4",
    status: "Inativo",
    assignedProfIds: ["p1"],
},
{
    id: "s5",
    name: "Corte Infantil",
    category: "Cortes",
    duration: 25,
    price: 30.0,
    assignedProfessionals: "2/4",
    status: "Ativo",
    assignedProfIds: ["p2", "p3"],
},
{
    id: "s6",
    name: "Barba Express",
    category: "Barba",
    duration: 15,
    price: 20.0,
    assignedProfessionals: "3/4",
    status: "Ativo",
    assignedProfIds: ["p1", "p3", "p4"],
},
];
// Variáveis para Controle de Caixa
let cashTransactions =
JSON.parse(localStorage.getItem("cashTransactions")) || [];
let cashBalance = parseFloat(localStorage.getItem("cashBalance")) || 0;

// Dados simulados dos profissionais e atendimentos
const professionalPerformanceData = {
todos: {
    atendimentos: 85,
    tempoMedio: 45.0,
    receita: 4250.0,
    avaliacaoMedia: 4.5,
},
carlos: {
    atendimentos: 32,
    tempoMedio: 40.0,
    receita: 1760.0,
    avaliacaoMedia: 4.8,
},
pedro: {
    atendimentos: 28,
    tempoMedio: 35.0,
    receita: 1540.0,
    avaliacaoMedia: 4.6,
},
lucas: {
    atendimentos: 25,
    tempoMedio: 45.0,
    receita: 1375.0,
    avaliacaoMedia: 4.9,
},
};

// Salvar relatórios gerados
let generatedReports =
JSON.parse(localStorage.getItem("generatedReports")) || [];
// Variáveis globais
let currentProfile = "";
let currentRating = 0;

// Função para selecionar perfil
function selectProfile(profile) {
currentProfile = profile;
document.getElementById("profileSelection").classList.add("hidden");
document.getElementById("loginScreen").classList.remove("hidden");

const subtitle = document.getElementById("loginSubtitle");
if (profile === "admin") {
    subtitle.textContent = "Entrando como Administrador";
} else if (profile === "professional") {
    subtitle.textContent = "Entrando como Profissional";
} else {
    subtitle.textContent = "Entrando como Cliente";
}
}

function login(event) {
event.preventDefault();
document.getElementById("loginScreen").classList.add("hidden");

if (currentProfile === "admin") {
    document.getElementById("adminPanel").classList.remove("hidden");
    renderProfessionalsTable();
    renderServicesTable();
} else if (currentProfile === "professional") {
    document
    .getElementById("professionalPanel")
    .classList.remove("hidden");
} else {
    document.getElementById("clientPanel").classList.remove("hidden");
}
}

// Função para voltar à seleção de perfil
function backToProfileSelection() {
document.getElementById("loginScreen").classList.add("hidden");
document.getElementById("profileSelection").classList.remove("hidden");
}

// Função de logout
function logout() {
document.getElementById("adminPanel").classList.add("hidden");
document.getElementById("professionalPanel").classList.add("hidden");
document.getElementById("clientPanel").classList.add("hidden");
document.getElementById("profileSelection").classList.remove("hidden");
}

// Função para alternar seções do Profissional
function showProfessionalSection(section) {
// Esconder todas as seções
document
    .getElementById("professionalAgendaSection")
    .classList.add("hidden");
document
    .getElementById("professionalReviewsSection")
    .classList.add("hidden");

// Remover classe ativa de todos os links
const links = document.querySelectorAll("#professionalPanel nav a");
links.forEach((link) => {
    link.classList.remove("bg-primary/10", "text-primary");
    link.classList.add("text-gray-400", "hover:text-gray-200");
});

// Mostrar seção selecionada e ativar link correspondente
if (section === "agenda") {
    document
    .getElementById("professionalAgendaSection")
    .classList.remove("hidden");
    const link = document.getElementById("profAgendaLink");
    link.classList.add("bg-primary/10", "text-primary");
    link.classList.remove("text-gray-400", "hover:text-gray-200");
} else if (section === "reviews") {
    document
    .getElementById("professionalReviewsSection")
    .classList.remove("hidden");
    const link = document.getElementById("profReviewsLink");
    link.classList.add("bg-primary/10", "text-primary");
    link.classList.remove("text-gray-400", "hover:text-gray-200");
}

return false; // Prevenir navegação padrão do link
}

// Funções do Profissional
function startAppointment(id) {
const names = ["João Pereira", "Pedro Santos"];
document.getElementById("startClientName").textContent =
    names[id - 1] || "Cliente";
document.getElementById("startModal").classList.remove("hidden");
}

function confirmStart() {
closeModal("startModal");
showToast(
    "Atendimento Iniciado",
    "O cliente foi notificado do início do atendimento.",
    "play_circle",
);
}

function finishAppointment(id) {
document.getElementById("finishModal").classList.remove("hidden");
}

function confirmFinish() {
closeModal("finishModal");
showToast(
    "Atendimento Concluído",
    "O cliente receberá uma notificação para avaliar o atendimento.",
    "check_circle",
);
}

// Função para profissional cancelar agendamento
function cancelProfessionalAppointment(id) {
if (confirm("Tem certeza que deseja cancelar este agendamento?")) {
    showToast(
    "Agendamento Cancelado",
    "O cliente será notificado sobre o cancelamento.",
    "cancel",
    "red",
    );
    // Aqui você removeria a linha da tabela no sistema real
}
}

// Funções do Admin
function confirmAdminAppointment(id) {
showToast(
    "Agendamento Confirmado",
    "O cliente e o profissional foram notificados da confirmação.",
    "check_circle",
);
}

function cancelAdminAppointment(id) {
if (confirm("Tem certeza que deseja cancelar este agendamento?")) {
    showToast(
    "Agendamento Cancelado",
    "O cliente e o profissional foram notificados do cancelamento.",
    "cancel",
    "red",
    );
    // Aqui você removeria a linha da tabela no sistema real
}
}

function renderProfessionalsTable() {
const tableBody = document.getElementById("professionalsTableBody");
if (!tableBody) return;

tableBody.innerHTML = "";

professionalsData.forEach((prof) => {
    const isInactive = prof.status === "Inativo";

    const actionButtons = isInactive
    ? `<button onclick="toggleProfessionalStatus('${prof.id}')" class="bg-green-500/10 text-green-400 hover:bg-green-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">Reativar</button>`
    : `
                <button onclick="openJornadaModal('${prof.id}')" class="bg-blue-500/10 text-blue-400 hover:bg-blue-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">Jornada</button>
                <button onclick="openEditProfessionalModal('${prof.id}')" class="bg-yellow-500/10 text-yellow-400 hover:bg-yellow-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">Editar</button>
                <button onclick="toggleProfessionalStatus('${prof.id}')" class="bg-red-500/10 text-red-400 hover:bg-red-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">Desativar</button>
            `;

    const row = `
            <tr class="hover:bg-dark-800 ${isInactive ? "opacity-60" : ""}">
                <td class="px-6 py-4 font-medium">${prof.name}</td>
                <td class="px-6 py-4">${prof.level}</td>
                <td class="px-6 py-4">${prof.jornada}</td>
                <td class="px-6 py-4">${prof.rating}</td>
                <td class="px-6 py-4">
                    <span class="${isInactive ? "bg-red-500/10 text-red-400" : "bg-green-500/10 text-green-400"} px-3 py-1 rounded-full text-sm">${prof.status}</span>
                </td>
                <td class="px-6 py-4">
                    <div class="flex gap-2">${actionButtons}</div>
                </td>
            </tr>
        `;
    tableBody.innerHTML += row;
});
}

function openNewProfessionalModal() {
document.getElementById("professionalFormId").value = "";
document.getElementById("professionalFormName").value = "";
document.getElementById("professionalFormLevel").value = "Júnior";
document.getElementById("professionalFormJornada").value =
    "Seg-Sex (8h-18h)";
document.getElementById("professionalFormRating").value = "5.0/5.0";

document.getElementById("professionalFormTitle").textContent =
    "Cadastrar Novo Profissional";
document.getElementById("professionalFormIcon").textContent =
    "person_add";

document
    .getElementById("professionalFormModal")
    .classList.remove("hidden");
}

function openEditProfessionalModal(id) {
const prof = professionalsData.find((p) => p.id === id);
if (!prof) return;

document.getElementById("professionalFormId").value = prof.id;
document.getElementById("professionalFormName").value = prof.name;
document.getElementById("professionalFormLevel").value = prof.level;
document.getElementById("professionalFormJornada").value = prof.jornada;
document.getElementById("professionalFormRating").value = prof.rating;

document.getElementById("professionalFormTitle").textContent =
    "Editar Profissional";
document.getElementById("professionalFormIcon").textContent = "edit";

document
    .getElementById("professionalFormModal")
    .classList.remove("hidden");
}

function submitProfessionalForm(event) {
event.preventDefault();

const id = document.getElementById("professionalFormId").value;
const name = document.getElementById("professionalFormName").value;
const level = document.getElementById("professionalFormLevel").value;
const jornada = document.getElementById(
    "professionalFormJornada",
).value;
const rating = document.getElementById("professionalFormRating").value;

if (id) {
    const index = professionalsData.findIndex((p) => p.id === id);
    if (index !== -1) {
    professionalsData[index] = {
        ...professionalsData[index],
        name,
        level,
        jornada,
        rating,
    };
    }
    showToast(
    "Sucesso!",
    "Profissional atualizado com sucesso.",
    "check_circle",
    );
} else {
    const newProf = {
    id: "p" + new Date().getTime(),
    name,
    level,
    jornada,
    rating: rating || "N/A",
    status: "Ativo",
    };
    professionalsData.push(newProf);
    showToast(
    "Sucesso!",
    "Profissional cadastrado com sucesso.",
    "check_circle",
    );
}

closeModal("professionalFormModal");
renderProfessionalsTable();
}

function toggleProfessionalStatus(id) {
const index = professionalsData.findIndex((p) => p.id === id);
if (index === -1) return;

const prof = professionalsData[index];
const isActivating = prof.status === "Inativo";

const confirmationText = isActivating
    ? `Deseja realmente REATIVAR ${prof.name}?`
    : `Deseja realmente DESATIVAR ${prof.name}? Isso irá ocultá-lo de novos agendamentos.`;

if (confirm(confirmationText)) {
    prof.status = isActivating ? "Ativo" : "Inativo";

    const toastMessage = isActivating
    ? "Profissional reativado."
    : "Profissional desativado.";

    showToast("Status Alterado", toastMessage, "info", "info");
    renderProfessionalsTable();
    renderServicesTable();
}
}

function openJornadaModal(id) {
const prof = professionalsData.find((p) => p.id === id);
if (!prof) return;

document.getElementById("jornadaProfessionalId").value = prof.id;
document.getElementById("jornadaProfessionalName").textContent =
    prof.name;

document.getElementById("jornadaModal").classList.remove("hidden");
}

function submitJornada() {
const id = document.getElementById("jornadaProfessionalId").value;
const prof = professionalsData.find((p) => p.id === id);

closeModal("jornadaModal");
showToast(
    "Jornada Salva!",
    `A jornada de ${prof.name} foi atualizada.`,
    "check_circle",
);
}

function renderServicesTable() {
const tableBody = document.getElementById("servicesTableBody");
if (!tableBody) return;

tableBody.innerHTML = "";
const totalActiveProfessionals = professionalsData.filter(
    (p) => p.status === "Ativo",
).length;

servicesData.forEach((service) => {
    const isInactive = service.status === "Inativo";

    const assignedActiveCount = service.assignedProfIds.filter(
    (profId) => {
        const prof = professionalsData.find((p) => p.id === profId);
        return prof && prof.status === "Ativo";
    },
    ).length;
    const assignedProfessionalsText = `${assignedActiveCount}/${totalActiveProfessionals}`;
    service.assignedProfessionals = assignedProfessionalsText;

    const actionButtons = isInactive
    ? `<button onclick="toggleServiceStatus('${service.id}')" class="bg-green-500/10 text-green-400 hover:bg-green-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">Reativar</button>`
    : `
                <button onclick="openAssociateProfessionalModal('${service.id}')" class="bg-blue-500/10 text-blue-400 hover:bg-blue-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">Associar</button>
                <button onclick="openEditServiceModal('${service.id}')" class="bg-yellow-500/10 text-yellow-400 hover:bg-yellow-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">Editar</button>
                <button onclick="toggleServiceStatus('${service.id}')" class="bg-red-500/10 text-red-400 hover:bg-red-500/20 px-3 py-1.5 rounded-lg text-sm font-medium transition">Desativar</button>
            `;

    const row = `
            <tr class="hover:bg-dark-800 ${isInactive ? "opacity-60" : ""}">
                <td class="px-6 py-4 font-medium">${service.name}</td>
                <td class="px-6 py-4">${service.category}</td>
                <td class="px-6 py-4">${service.duration} min</td>
                <td class="px-6 py-4">R$ ${service.price.toFixed(2).replace(".", ",")}</td>
                <td class="px-6 py-4">${assignedProfessionalsText}</td>
                    <td class="px-6 py-4">
                    <span class="${isInactive ? "bg-red-500/10 text-red-400" : "bg-green-500/10 text-green-400"} px-3 py-1 rounded-full text-sm">${service.status}</span>
                </td>
                <td class="px-6 py-4">
                    <div class="flex gap-2">${actionButtons}</div>
                </td>
            </tr>
        `;
    tableBody.innerHTML += row;
});
}

function openNewServiceModal() {
document.getElementById("serviceFormId").value = "";
document.getElementById("serviceFormName").value = "";
document.getElementById("serviceFormCategory").value = "";
document.getElementById("serviceFormDuration").value = "";
document.getElementById("serviceFormPrice").value = "";

document.getElementById("serviceFormTitle").textContent =
    "Cadastrar Novo Serviço";
document.getElementById("serviceFormIcon").textContent = "add_circle";

document.getElementById("serviceFormModal").classList.remove("hidden");
}

function openEditServiceModal(id) {
const service = servicesData.find((s) => s.id === id);
if (!service) return;

document.getElementById("serviceFormId").value = service.id;
document.getElementById("serviceFormName").value = service.name;
document.getElementById("serviceFormCategory").value = service.category;
document.getElementById("serviceFormDuration").value = service.duration;
document.getElementById("serviceFormPrice").value =
    service.price.toFixed(2);

document.getElementById("serviceFormTitle").textContent =
    "Editar Serviço";
document.getElementById("serviceFormIcon").textContent = "edit";

document.getElementById("serviceFormModal").classList.remove("hidden"); //A
}

function submitServiceForm(event) {
event.preventDefault();

const id = document.getElementById("serviceFormId").value;
const name = document.getElementById("serviceFormName").value;
const category = document.getElementById("serviceFormCategory").value;
const duration = parseInt(
    document.getElementById("serviceFormDuration").value,
);
const price = parseFloat(
    document.getElementById("serviceFormPrice").value,
);

if (isNaN(duration) || isNaN(price) || duration <= 0 || price < 0) {
    showToast(
    "Erro",
    "Duração e Preço devem ser números válidos.",
    "error",
    "red",
    );
    return;
}

if (id) {
    const index = servicesData.findIndex((s) => s.id === id);
    if (index !== -1) {
    servicesData[index] = {
        ...servicesData[index],
        name,
        category,
        duration,
        price,
    };
    }
    showToast(
    "Sucesso!",
    "Serviço atualizado com sucesso.",
    "check_circle",
    );
} else {
    const newService = {
    id: "s" + new Date().getTime(),
    name,
    category,
    duration,
    price,
    assignedProfessionals: `0/${professionalsData.filter((p) => p.status === "Ativo").length}`,
    status: "Ativo",
    assignedProfIds: [],
    };
    servicesData.push(newService);
    showToast(
    "Sucesso!",
    "Serviço cadastrado com sucesso.",
    "check_circle",
    );
}

closeModal("serviceFormModal");
renderServicesTable();
}

function toggleServiceStatus(id) {
const index = servicesData.findIndex((s) => s.id === id);
if (index === -1) return;

const service = servicesData[index];
const isActivating = service.status === "Inativo";

const confirmationText = isActivating
    ? `Deseja realmente REATIVAR o serviço "${service.name}"?`
    : `Deseja realmente DESATIVAR o serviço "${service.name}"? Clientes não poderão mais agendá-lo.`;

if (confirm(confirmationText)) {
    service.status = isActivating ? "Ativo" : "Inativo";

    const toastMessage = isActivating
    ? "Serviço reativado."
    : "Serviço desativado.";

    showToast("Status Alterado", toastMessage, "info", "info");
    renderServicesTable();
}
}

function openAssociateProfessionalModal(serviceId) {
const service = servicesData.find((s) => s.id === serviceId);
if (!service) return;

document.getElementById("associateServiceId").value = serviceId;
document.getElementById("associateServiceName").textContent =
    service.name;

const listContainer = document.getElementById(
    "associateProfessionalList",
);
listContainer.innerHTML = "";

const activeProfessionals = professionalsData.filter(
    (p) => p.status === "Ativo",
);

if (activeProfessionals.length === 0) {
    listContainer.innerHTML =
    '<p class="text-sm text-gray-400">Nenhum profissional ativo encontrado.</p>';
} else {
    activeProfessionals.forEach((prof) => {
    const isChecked = service.assignedProfIds.includes(prof.id);

    const checkboxHTML = `
                <label class="flex items-center gap-2 bg-dark-700 p-2 rounded hover:bg-dark-600 cursor-pointer">
                    <input type="checkbox" value="${prof.id}" ${isChecked ? "checked" : ""} class="form-checkbox professional-association-checkbox bg-dark-600 border-dark-500 text-primary focus:ring-primary">
                    <span>${prof.name} (${prof.level})</span>
                </label>
            `;
    listContainer.innerHTML += checkboxHTML;
    });
}

document
    .getElementById("associateProfessionalModal")
    .classList.remove("hidden");
}

function submitAssociation() {
const serviceId = document.getElementById("associateServiceId").value;
const index = servicesData.findIndex((s) => s.id === serviceId);
if (index === -1) return;

const selectedCheckboxes = document.querySelectorAll(
    ".professional-association-checkbox:checked",
);
const selectedProfIds = Array.from(selectedCheckboxes).map(
    (cb) => cb.value,
);

servicesData[index].assignedProfIds = selectedProfIds;

const totalActiveProfessionals = professionalsData.filter(
    (p) => p.status === "Ativo",
).length;
servicesData[index].assignedProfessionals =
    `${selectedProfIds.length}/${totalActiveProfessionals}`;

closeModal("associateProfessionalModal");
renderServicesTable();
showToast(
    "Associações Salvas!",
    "Os profissionais foram associados ao serviço.",
    "check_circle",
);
}
// Funções do Cliente
let selectedTimeSlot = null;
let appointmentData = {};

function openNewAppointmentModal() {
document
    .getElementById("newAppointmentModal")
    .classList.remove("hidden");
setMinDate();
resetAppointmentForm();
}

function setMinDate() {
const tomorrow = new Date();
tomorrow.setDate(tomorrow.getDate() + 1);
const dateString = tomorrow.toISOString().split("T")[0];
document
    .getElementById("appointmentDate")
    .setAttribute("min", dateString);
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
const professional = document.getElementById(
    "appointmentProfessional",
).value;

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

function generateTimeSlots(professional) {
const slots = [];
const startHour = 8;
const endHour = 18;

for (let hour = startHour; hour < endHour; hour++) {
    for (let minute = 0; minute < 60; minute += 30) {
    const time = `${hour.toString().padStart(2, "0")}:${minute.toString().padStart(2, "0")}`;
    const random = Math.random();
    let available = random > 0.3;

    if (!professional) {
        available = random > 0.2;
    }

    slots.push({ time, available });
    }
}

return slots;
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

document.getElementById("summaryService").textContent =
    appointmentData.service;
document.getElementById("summaryDate").textContent = formattedDate;
document.getElementById("summaryTime").textContent =
    `${selectedTimeSlot} - ${endTimeStr}`;
document.getElementById("summaryDuration").textContent =
    `${appointmentData.duration} minutos`;
document.getElementById("summaryProfessional").textContent =
    professional.value
    ? professional.options[professional.selectedIndex].text
    : "Sistema escolherá automaticamente";
document.getElementById("summaryPrice").textContent =
    `R$ ${parseFloat(appointmentData.price).toFixed(2)}`;

document
    .getElementById("appointmentSummary")
    .classList.remove("hidden");
}

function submitNewAppointment(event) {
event.preventDefault();

if (!selectedTimeSlot) {
    showToast(
    "Atenção",
    "Por favor, selecione um horário disponível.",
    "warning",
    "yellow",
    );
    return;
}

const date = document.getElementById("appointmentDate").value;

const appointmentDateTime = new Date(date + "T" + selectedTimeSlot);
const now = new Date();
const hoursDifference = (appointmentDateTime - now) / (1000 * 60 * 60);

if (hoursDifference < 2) {
    showToast(
    "Atenção",
    "Agendamentos devem ser feitos com pelo menos 2 horas de antecedência.",
    "warning",
    "yellow",
    );
    return;
}

appointmentData.status = "Pendente";
appointmentData.date = date;
appointmentData.notes =
    document.getElementById("appointmentNotes").value;

closeModal("newAppointmentModal");
showToast(
    "Agendamento Criado!",
    `Seu agendamento para ${appointmentData.date} às ${selectedTimeSlot} foi criado. Status: Pendente.`,
    "check_circle",
);
resetAppointmentForm();
}

function cancelAppointment(id) {
document.getElementById("cancelModal").classList.remove("hidden");
}

function confirmCancel() {
closeModal("cancelModal");
showToast(
    "Agendamento Cancelado",
    "Seu agendamento foi cancelado com sucesso.",
    "cancel",
    "red",
);
}

function openRatingModal(professional, service) {
document.getElementById("ratingProfessionalName").textContent =
    professional;
document.getElementById("ratingService").textContent = service;
currentRating = 0;
resetStars();
document.getElementById("ratingModal").classList.remove("hidden");
}

function setRating(stars) {
currentRating = stars;
const starElements = document.querySelectorAll(
    "#starRating .material-icons",
);
starElements.forEach((star, index) => {
    star.style.color = index < stars ? "#FF8C00" : "#4a5568";
});
}

function resetStars() {
const starElements = document.querySelectorAll(
    "#starRating .material-icons",
);
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
showToast(
    "Avaliação Enviada",
    "Obrigado pelo seu feedback!",
    "star",
    "yellow",
);
}

function closeModal(modalId) {
document.getElementById(modalId).classList.add("hidden");
}

function closeModalOnBackdrop(event, modalId) {
if (event.target.id === modalId) {
    closeModal(modalId);
}
}

function showToast(
title,
message,
icon = "check_circle",
color = "primary",
) {
const toast = document.getElementById("toast");
const toastIcon = document.getElementById("toastIcon");
const toastTitle = document.getElementById("toastTitle");
const toastMessage = document.getElementById("toastMessage");

toastTitle.textContent = title;
toastMessage.textContent = message;
toastIcon.textContent = icon;

// Reset classes
toast.className =
    "hidden fixed top-8 right-8 bg-dark-800 border rounded-lg p-4 shadow-2xl z-50 max-w-sm";
toastIcon.className = "material-icons";

if (color === "red") {
    toastIcon.classList.add("text-red-400");
    toast.classList.add("border-red-400");
} else if (color === "yellow") {
    toastIcon.classList.add("text-yellow-400");
    toast.classList.add("border-yellow-400");
} else if (color === "info") {
    toastIcon.classList.add("text-blue-400");
    toast.classList.add("border-blue-400");
} else {
    toastIcon.classList.add("text-primary");
    toast.classList.add("border-primary");
}

toast.classList.remove("hidden");
toast.classList.add("toast");

setTimeout(() => {
    toast.classList.add("hidden");
}, 4000);
}

function openAdminModal(modalType) {
if (modalType === "cashControl") {
    document
    .getElementById("cashControlModal")
    .classList.remove("hidden");
    // Inicializar interface de caixa
    updateCashUI();
    filterCashTransactions("today"); // Mostrar transações de hoje por padrão
} else if (modalType === "reports") {
    document.getElementById("reportsModal").classList.remove("hidden");
    // Definir data de hoje como padrão
    document.getElementById("reportDate").valueAsDate = new Date();
    // Atualizar histórico de relatórios
    updateReportHistory();
} else if (modalType === "professionalManagement") {
    document
    .getElementById("professionalManagementModal")
    .classList.remove("hidden");
} else if (modalType === "serviceManagement") {
    document
    .getElementById("serviceManagementModal")
    .classList.remove("hidden");
} else {
    showToast(
    "Em breve",
    "Esta funcionalidade estará disponível em breve.",
    "info",
    "info",
    );
}
}
// Função para registrar uma transação no caixa
function registerCashTransaction(type) {
const description = document.getElementById("cashDescription").value;
const valueInput = document.getElementById("cashValue");
const value = parseFloat(valueInput.value);
const payment = document.getElementById("cashPayment").value;

if (!description) {
    showToast(
    "Erro",
    "Informe uma descrição para o lançamento.",
    "error",
    "red",
    );
    return;
}

if (isNaN(value) || value <= 0) {
    showToast(
    "Erro",
    "Informe um valor válido maior que zero.",
    "error",
    "red",
    );
    return;
}

// Criar objeto de transação
const transaction = {
    id: Date.now(),
    date: new Date().toISOString(),
    description: description,
    value: type === "ENTRADA" ? value : -value,
    payment: payment,
    status: "Concluído",
};

// Adicionar à lista e salvar
cashTransactions.push(transaction);
cashBalance += transaction.value;

// Persistir no localStorage
localStorage.setItem(
    "cashTransactions",
    JSON.stringify(cashTransactions),
);
localStorage.setItem("cashBalance", cashBalance.toString());

// Atualizar UI
updateCashUI();

// Limpar campos
document.getElementById("cashDescription").value = "";
valueInput.value = "";

showToast(
    type === "ENTRADA" ? "Entrada Registrada" : "Saída Registrada",
    `${type === "ENTRADA" ? "Crédito" : "Débito"} de R$ ${value.toFixed(2)} registrado com sucesso.`,
    type === "ENTRADA" ? "add_circle" : "remove_circle",
    type === "ENTRADA" ? "green" : "red",
);
}

// Função para atualizar interface de caixa
function updateCashUI() {
// Atualizar saldo
document.getElementById("cashBalanceValue").textContent =
    `R$ ${cashBalance.toFixed(2).replace(".", ",")}`;

// Calcular entradas e saídas do dia
const today = new Date().toISOString().split("T")[0];
const todaysTransactions = cashTransactions.filter((t) =>
    t.date.startsWith(today),
);

const todaysIncome = todaysTransactions
    .filter((t) => t.value > 0)
    .reduce((sum, t) => sum + t.value, 0);

const todaysExpenses = todaysTransactions
    .filter((t) => t.value < 0)
    .reduce((sum, t) => sum + Math.abs(t.value), 0);

document.getElementById("todayIncome").textContent =
    `R$ ${todaysIncome.toFixed(2).replace(".", ",")}`;

document.getElementById("todayExpenses").textContent =
    `R$ ${todaysExpenses.toFixed(2).replace(".", ",")}`;

document.getElementById("incomeCount").textContent =
    `${todaysTransactions.filter((t) => t.value > 0).length} transações`;

document.getElementById("expenseCount").textContent =
    `${todaysTransactions.filter((t) => t.value < 0).length} transações`;

// Atualizar tabela de transações
const tableBody = document.getElementById("cashTransactionsTable");
tableBody.innerHTML = "";

// Ordenar transações da mais recente para a mais antiga
const sortedTransactions = [...cashTransactions].sort(
    (a, b) => new Date(b.date) - new Date(a.date),
);

sortedTransactions.forEach((transaction) => {
    const date = new Date(transaction.date);
    const formattedDate = `${date.toLocaleDateString("pt-BR")} ${date.getHours()}:${date.getMinutes().toString().padStart(2, "0")}`;

    const row = document.createElement("tr");
    row.className = "border-t border-dark-600";
    row.innerHTML = `
    <td class="py-2">${formattedDate}</td>
    <td class="py-2">${transaction.description}</td>
    <td class="py-2 ${transaction.value > 0 ? "text-green-500" : "text-red-500"}">
        ${transaction.value > 0 ? "+ " : "- "}R$ ${Math.abs(transaction.value).toFixed(2)}
    </td>
    <td class="py-2">${transaction.payment}</td>
    <td class="py-2">
        <span class="bg-green-500/10 text-green-400 px-2 py-1 rounded-full text-xs">${transaction.status}</span>
    </td>
    <td class="py-2">
        <button onclick="deleteCashTransaction(${transaction.id})" 
            class="text-red-400 hover:text-red-300">
            <span class="material-icons text-sm">delete</span>
        </button>
    </td>
`;

    tableBody.appendChild(row);
});

// Exibir mensagem se não houver transações
if (cashTransactions.length === 0) {
    const row = document.createElement("tr");
    row.innerHTML = `
    <td colspan="6" class="py-4 text-center text-gray-400">
        Nenhuma transação registrada
    </td>
`;
    tableBody.appendChild(row);
}
}

// Função para deletar uma transação
function deleteCashTransaction(id) {
if (!confirm("Tem certeza que deseja excluir esta transação?")) {
    return;
}

const index = cashTransactions.findIndex((t) => t.id === id);
if (index === -1) return;

// Atualizar saldo
cashBalance -= cashTransactions[index].value;

// Remover transação
cashTransactions.splice(index, 1);

// Persistir no localStorage
localStorage.setItem(
    "cashTransactions",
    JSON.stringify(cashTransactions),
);
localStorage.setItem("cashBalance", cashBalance.toString());

// Atualizar UI
updateCashUI();

showToast(
    "Transação Removida",
    "O lançamento foi removido com sucesso.",
    "delete",
    "red",
);
}

// Função para filtrar transações
function filterCashTransactions(period) {
const tableBody = document.getElementById("cashTransactionsTable");
tableBody.innerHTML = "";

// Determinar data de início para o filtro
let startDate = new Date();
if (period === "week") {
    startDate.setDate(startDate.getDate() - 7);
} else if (period === "month") {
    startDate.setMonth(startDate.getMonth() - 1);
} else if (period === "all") {
    startDate = new Date(0); // início dos tempos
}

const filteredTransactions = cashTransactions
    .filter((t) => new Date(t.date) >= startDate)
    .sort((a, b) => new Date(b.date) - new Date(a.date));

if (filteredTransactions.length === 0) {
    const row = document.createElement("tr");
    row.innerHTML = `
    <td colspan="6" class="py-4 text-center text-gray-400">
        Nenhuma transação encontrada no período selecionado
    </td>
`;
    tableBody.appendChild(row);
    return;
}

filteredTransactions.forEach((transaction) => {
    const date = new Date(transaction.date);
    const formattedDate = `${date.toLocaleDateString("pt-BR")} ${date.getHours()}:${date.getMinutes().toString().padStart(2, "0")}`;

    const row = document.createElement("tr");
    row.className = "border-t border-dark-600";
    row.innerHTML = `
    <td class="py-2">${formattedDate}</td>
    <td class="py-2">${transaction.description}</td>
    <td class="py-2 ${transaction.value > 0 ? "text-green-500" : "text-red-500"}">
        ${transaction.value > 0 ? "+ " : "- "}R$ ${Math.abs(transaction.value).toFixed(2)}
    </td>
    <td class="py-2">${transaction.payment}</td>
    <td class="py-2">
        <span class="bg-green-500/10 text-green-400 px-2 py-1 rounded-full text-xs">${transaction.status}</span>
    </td>
    <td class="py-2">
        <button onclick="deleteCashTransaction(${transaction.id})" 
            class="text-red-400 hover:text-red-300">
            <span class="material-icons text-sm">delete</span>
        </button>
    </td>
`;

    tableBody.appendChild(row);
});
}
// Função para gerar relatório de desempenho
function generatePerformanceReport() {
const professional =
    document.getElementById("reportProfessional").value;
const date = document.getElementById("reportDate").value;

if (!date) {
    showToast(
    "Erro",
    "Selecione uma data para gerar o relatório.",
    "error",
    "red",
    );
    return;
}

// Obter dados do profissional
const data =
    professionalPerformanceData[professional] ||
    professionalPerformanceData["todos"];

// Aplicar alguma variação baseada na data para simular dados diferentes
const dateObj = new Date(date);
const dateFactor = dateObj.getDate() / 30; // Fator entre 0.03 e 1

// Aplicar variação de +/- 10% baseada na data
const variation = 0.9 + dateFactor * 0.2;

const reportData = {
    id: Date.now(),
    date: date,
    professional: professional,
    professionalName: getProfessionalName(professional),
    tempoServico: Math.round(data.tempoMedio * variation * 10) / 10,
    receita: Math.round(data.receita * variation * 100) / 100,
    atendimentos: Math.round(data.atendimentos * variation),
    avaliacaoMedia: Math.min(
    5,
    Math.round(data.avaliacaoMedia * variation * 10) / 10,
    ),
};

// Salvar relatório
generatedReports.push(reportData);
localStorage.setItem(
    "generatedReports",
    JSON.stringify(generatedReports),
);

// Atualizar UI
updateReportUI(reportData);

showToast(
    "Relatório Gerado",
    "O relatório de desempenho foi gerado com sucesso.",
    "analytics",
);
}

// Função para atualizar UI do relatório
function updateReportUI(report) {
// Exibir resultados
document.getElementById("reportResult").classList.remove("hidden");

// Preencher valores
document.getElementById("r_tempo").textContent =
    report.tempoServico.toFixed(1);
document.getElementById("r_receita").textContent =
    `R$ ${report.receita.toFixed(2)}`;
document.getElementById("r_atend").textContent = report.atendimentos;
document.getElementById("r_media").textContent =
    report.avaliacaoMedia.toFixed(1);

// Exibir nome do profissional no título do relatório
const reportTitle = document.getElementById("reportTitle");
reportTitle.textContent = `Relatório de ${report.professionalName}`;

// Exibir data formatada
const dateObj = new Date(report.date);
const formattedDate = dateObj.toLocaleDateString("pt-BR");
document.getElementById("reportDate").value = report.date;
document.getElementById("reportDateDisplay").textContent =
    formattedDate;

// Atualizar histórico de relatórios
updateReportHistory();
}

// Função para atualizar histórico de relatórios
function updateReportHistory() {
const historyContainer = document.getElementById("reportHistory");
if (!historyContainer) return;

historyContainer.innerHTML = "";

// Ordenar relatórios do mais recente para o mais antigo
const sortedReports = [...generatedReports].sort(
    (a, b) => new Date(b.date) - new Date(a.date),
);

// Limitar a 5 relatórios mais recentes
const recentReports = sortedReports.slice(0, 5);

recentReports.forEach((report) => {
    const dateObj = new Date(report.date);
    const formattedDate = dateObj.toLocaleDateString("pt-BR");

    const reportCard = document.createElement("div");
    reportCard.className =
    "bg-dark-700 p-3 rounded-lg hover:bg-dark-600 cursor-pointer";
    reportCard.onclick = () => updateReportUI(report);

    reportCard.innerHTML = `
    <div class="flex justify-between items-center">
        <div>
            <h5 class="font-medium">${report.professionalName}</h5>
            <p class="text-xs text-gray-400">${formattedDate}</p>
        </div>
        <div>
            <span class="text-primary font-medium">
                ${report.avaliacaoMedia.toFixed(1)}★
            </span>
        </div>
    </div>
`;

    historyContainer.appendChild(reportCard);
});

// Exibir mensagem se não houver relatórios
if (recentReports.length === 0) {
    historyContainer.innerHTML = `
    <p class="text-center text-gray-400 py-4">
        Nenhum relatório gerado recentemente
    </p>
`;
}
}

// Função auxiliar para obter nome do profissional
function getProfessionalName(professional) {
const professionalNames = {
    todos: "Todos os Profissionais",
    carlos: "Carlos Silva",
    pedro: "Pedro Souza",
    lucas: "Lucas Lima",
};

return professionalNames[professional] || "Profissional";
}

// Função para exportar relatório como CSV
function exportReport() {
// Verificar se há relatório gerado
if (
    document.getElementById("reportResult").classList.contains("hidden")
) {
    showToast(
    "Erro",
    "Gere um relatório antes de exportar.",
    "error",
    "red",
    );
    return;
}

// Obter dados do relatório atual
const professional = document
    .getElementById("reportTitle")
    .textContent.replace("Relatório de ", "");
const date = document.getElementById("reportDateDisplay").textContent;
const tempo = document.getElementById("r_tempo").textContent;
const receita = document.getElementById("r_receita").textContent;
const atendimentos = document.getElementById("r_atend").textContent;
const avaliacao = document.getElementById("r_media").textContent;

// Criar conteúdo CSV
const csvContent = [
    "Profissional,Data,Tempo de Serviço (min),Receita,Atendimentos,Avaliação Média",
    `"${professional}","${date}",${tempo},${receita.replace("R$ ", "")},${atendimentos},${avaliacao}`,
].join("\n");

// Criar blob e link para download
const blob = new Blob([csvContent], {
    type: "text/csv;charset=utf-8;",
});
const url = URL.createObjectURL(blob);
const link = document.createElement("a");

link.setAttribute("href", url);
link.setAttribute(
    "download",
    `relatorio_${date.replace(/\//g, "-")}.csv`,
);
link.style.visibility = "hidden";

document.body.appendChild(link);
link.click();
document.body.removeChild(link);

showToast(
    "Exportação Concluída",
    "O relatório foi exportado no formato CSV.",
    "file_download",
);
}
