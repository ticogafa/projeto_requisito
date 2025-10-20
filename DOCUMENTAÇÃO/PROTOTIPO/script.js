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

      // Variáveis para Gestão de Estoque
      let stockProducts = JSON.parse(localStorage.getItem("stockProducts")) || [
        { id: "st1", name: "Shampoo Premium", stock: 45, minStock: 10, price: 35.00, active: true },
        { id: "st2", name: "Gel Fixador", stock: 50, minStock: 15, price: 25.00, active: true },
        { id: "st3", name: "Pomada Modeladora", stock: 25, minStock: 8, price: 40.00, active: true },
        { id: "st4", name: "Cera Modeladora", stock: 18, minStock: 10, price: 38.00, active: true },
        { id: "st5", name: "Óleo para Barba", stock: 5, minStock: 5, price: 45.00, active: true }, // Estoque baixo
        { id: "st6", name: "Pente Profissional", stock: 30, minStock: 10, price: 15.00, active: true },
        { id: "st7", name: "Toalha Premium", stock: 2, minStock: 10, price: 50.00, active: true }, // Estoque baixo
      ];

      let stockHistory = JSON.parse(localStorage.getItem("stockHistory")) || [];

      // Variáveis para Gestão de Agendamentos
      let appointments = JSON.parse(localStorage.getItem("appointments")) || [
        {
          id: "apt1",
          clientId: "c1",
          clientName: "Maria Silva",
          professionalId: "p1",
          professionalName: "Carlos Silva",
          serviceId: "s1",
          serviceName: "Corte Simples",
          date: new Date().toISOString().split('T')[0],
          time: "09:00",
          status: "confirmado",
          duration: 30,
          price: 35.00,
          observations: ""
        },
        {
          id: "apt2",
          clientId: "c2",
          clientName: "João Santos",
          professionalId: "p2",
          professionalName: "Pedro Souza",
          serviceId: "s2",
          serviceName: "Corte + Barba",
          date: new Date().toISOString().split('T')[0],
          time: "10:00",
          status: "pendente",
          duration: 50,
          price: 60.00,
          observations: ""
        },
        {
          id: "apt3",
          clientId: "c3",
          clientName: "Ana Costa",
          professionalId: "p3",
          professionalName: "Lucas Lima",
          serviceId: "s1",
          serviceName: "Corte Simples",
          date: new Date().toISOString().split('T')[0],
          time: "14:00",
          status: "confirmado",
          duration: 30,
          price: 35.00,
          observations: "Cliente preferencial"
        }
      ];

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
          
          // Carregar respostas salvas quando visualizar avaliações
          setTimeout(() => loadSavedResponses(), 100);
        }

        return false; // Prevenir navegação padrão do link
      }

      // ==================== FUNÇÕES DE RESPOSTA DO PROFISSIONAL ====================
      
      let currentReviewId = null;
      let professionalResponses = JSON.parse(localStorage.getItem('professionalResponses')) || {};

      function openProfessionalResponseModal(id, clientName, service, stars) {
        currentReviewId = id;
        
        // Preencher informações da avaliação
        document.getElementById('responseReviewId').value = id;
        document.getElementById('responseClientName').textContent = clientName;
        document.getElementById('responseService').textContent = service;
        
        // Gerar estrelas
        let starsHTML = '';
        for (let i = 0; i < 5; i++) {
          starsHTML += `<span class="material-icons ${i < stars ? 'text-yellow-400' : 'text-gray-600'} text-sm">star</span>`;
        }
        document.getElementById('responseStars').innerHTML = starsHTML;
        
        // Limpar campo de texto
        document.getElementById('professionalResponseText').value = '';
        document.getElementById('charCount').textContent = '0';
        
        // Abrir modal
        document.getElementById('professionalResponseModal').classList.remove('hidden');
      }

      function submitProfessionalResponse(event) {
        event.preventDefault();
        
        const reviewId = document.getElementById('responseReviewId').value;
        const responseText = document.getElementById('professionalResponseText').value.trim();
        
        // Validação
        if (!responseText) {
          showToast('Atenção', 'Por favor, escreva uma resposta antes de enviar.', 'warning', 'yellow');
          return;
        }
        
        if (responseText.length > 500) {
          showToast('Atenção', 'A resposta não pode ter mais de 500 caracteres.', 'warning', 'yellow');
          return;
        }
        
        // Salvar resposta
        professionalResponses[reviewId] = {
          text: responseText,
          date: new Date().toISOString()
        };
        localStorage.setItem('professionalResponses', JSON.stringify(professionalResponses));
        
        // Fechar modal
        closeModal('professionalResponseModal');
        
        // Exibir toast de sucesso
        showToast('Resposta Enviada!', 'Sua resposta foi publicada e o cliente receberá uma notificação.', 'check_circle');
        
        // Em produção, aqui seria feita a chamada ao backend
        console.log('Resposta do profissional:', {
          reviewId: reviewId,
          response: responseText,
          timestamp: new Date().toISOString()
        });
        
        // Atualizar UI para mostrar a resposta
        addResponseToReviewCard(reviewId, responseText);
      }

      function addResponseToReviewCard(reviewId, responseText) {
        // Encontrar o card de avaliação correspondente
        const reviewCards = document.querySelectorAll('#professionalReviewsSection .bg-dark-700');
        
        // Mapeamento de IDs para índices (começando em 0)
        const reviewIndex = parseInt(reviewId) - 1;
        
        if (reviewCards[reviewIndex]) {
          const card = reviewCards[reviewIndex];
          
          // Verificar se já existe uma resposta
          const existingResponse = card.querySelector('.professional-response');
          if (existingResponse) {
            existingResponse.remove();
          }
          
          // Criar elemento de resposta
          const responseDiv = document.createElement('div');
          responseDiv.className = 'professional-response mt-4 pt-4 border-t border-dark-600';
          responseDiv.innerHTML = `
            <div class="flex items-start gap-3">
              <div class="w-8 h-8 bg-primary/10 rounded-full flex items-center justify-center flex-shrink-0">
                <span class="material-icons text-primary text-sm">badge</span>
              </div>
              <div class="flex-1">
                <div class="flex items-center gap-2 mb-2">
                  <span class="text-sm font-semibold text-primary">Resposta do Profissional</span>
                  <span class="text-xs text-gray-500">${new Date().toLocaleDateString('pt-BR')}</span>
                </div>
                <p class="text-sm text-gray-300 leading-relaxed">${escapeHtml(responseText)}</p>
              </div>
            </div>
          `;
          
          // Adicionar ao card, antes do botão "Responder avaliação"
          const replyButton = card.querySelector('button[onclick^="openProfessionalResponseModal"]');
          if (replyButton) {
            // Esconder o botão após responder
            replyButton.classList.add('hidden');
            // Adicionar resposta antes do botão
            replyButton.parentElement.insertBefore(responseDiv, replyButton);
          }
        }
      }

      // Função para escapar HTML e prevenir XSS
      function escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
      }

      // Carregar respostas salvas ao mostrar as avaliações
      function loadSavedResponses() {
        const savedResponses = JSON.parse(localStorage.getItem('professionalResponses')) || {};
        Object.keys(savedResponses).forEach(reviewId => {
          addResponseToReviewCard(reviewId, savedResponses[reviewId].text);
        });
      }

      // Adicionar contador de caracteres ao carregar a página
      document.addEventListener('DOMContentLoaded', function() {
        const responseTextarea = document.getElementById('professionalResponseText');
        if (responseTextarea) {
          responseTextarea.addEventListener('input', function() {
            const count = this.value.length;
            document.getElementById('charCount').textContent = count;
            
            // Alertar quando próximo do limite
            const charCountElement = document.getElementById('charCount');
            if (count > 450) {
              charCountElement.classList.add('text-red-400', 'font-semibold');
            } else {
              charCountElement.classList.remove('text-red-400', 'font-semibold');
            }
          });
        }
      });

      // ==================== FIM DAS FUNÇÕES DE RESPOSTA DO PROFISSIONAL ====================

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

      // ==================== FUNÇÕES DE GESTÃO DE ESTOQUE ====================

      // Função para renderizar a tabela de estoque
      function renderStockTable() {
        const tbody = document.getElementById("stockTableBody");
        tbody.innerHTML = "";

        if (stockProducts.length === 0) {
          tbody.innerHTML = `
            <tr>
              <td colspan="6" class="text-center py-8 text-gray-400">
                Nenhum produto cadastrado. Clique em "Novo Produto" para começar.
              </td>
            </tr>
          `;
          return;
        }

        stockProducts.forEach((product) => {
          const isLowStock = product.stock <= product.minStock;
          const statusBadge = product.active
            ? '<span class="inline-flex items-center gap-1 bg-green-500/10 text-green-400 px-3 py-1 rounded-full text-sm"><span class="material-icons text-sm">check_circle</span> Ativo</span>'
            : '<span class="inline-flex items-center gap-1 bg-red-500/10 text-red-400 px-3 py-1 rounded-full text-sm"><span class="material-icons text-sm">cancel</span> Inativo</span>';

          const stockDisplay = isLowStock
            ? `<span class="text-yellow-400 font-semibold">${product.stock}</span>`
            : `<span>${product.stock}</span>`;

          const row = `
            <tr class="border-b border-dark-600 hover:bg-dark-600/50">
              <td class="px-6 py-4">
                <div class="flex items-center gap-3">
                  <span class="material-icons text-primary">inventory</span>
                  <span class="font-medium">${product.name}</span>
                  ${isLowStock ? '<span class="material-icons text-yellow-400 text-sm" title="Estoque baixo">warning</span>' : ''}
                </div>
              </td>
              <td class="text-center px-6 py-4">${stockDisplay}</td>
              <td class="text-center px-6 py-4 text-gray-400">${product.minStock}</td>
              <td class="text-center px-6 py-4">R$ ${product.price.toFixed(2)}</td>
              <td class="text-center px-6 py-4">${statusBadge}</td>
              <td class="text-center px-6 py-4">
                <div class="flex items-center justify-center gap-2">
                  <button
                    onclick="openUpdateStockModal('${product.id}')"
                    class="material-icons text-blue-400 hover:text-blue-300 cursor-pointer"
                    title="Adicionar estoque"
                  >
                    add_box
                  </button>
                  <button
                    onclick="openEditProductModal('${product.id}')"
                    class="material-icons text-primary hover:text-orange-400 cursor-pointer"
                    title="Editar produto"
                  >
                    edit
                  </button>
                  <button
                    onclick="toggleProductStatus('${product.id}')"
                    class="material-icons ${product.active ? 'text-red-400 hover:text-red-300' : 'text-green-400 hover:text-green-300'} cursor-pointer"
                    title="${product.active ? 'Desativar' : 'Ativar'}"
                  >
                    ${product.active ? 'block' : 'check_circle'}
                  </button>
                </div>
              </td>
            </tr>
          `;
          tbody.innerHTML += row;
        });

        updateStockSummary();
      }

      // Função para atualizar resumo de estoque
      function updateStockSummary() {
        const totalProducts = stockProducts.length;
        const activeProducts = stockProducts.filter(p => p.active).length;
        const lowStockProducts = stockProducts.filter(p => p.stock <= p.minStock).length;

        document.getElementById("totalProducts").textContent = totalProducts;
        document.getElementById("activeProducts").textContent = activeProducts;
        document.getElementById("lowStockProducts").textContent = lowStockProducts;
      }

      // Função para abrir modal de novo produto
      function openNewProductModal() {
        document.getElementById("productModalTitle").textContent = "Novo Produto";
        document.getElementById("productForm").reset();
        document.getElementById("productId").value = "";
        document.getElementById("productModal").classList.remove("hidden");
      }

      // Função para abrir modal de edição de produto
      function openEditProductModal(productId) {
        const product = stockProducts.find(p => p.id === productId);
        if (!product) return;

        document.getElementById("productModalTitle").textContent = "Editar Produto";
        document.getElementById("productId").value = product.id;
        document.getElementById("productName").value = product.name;
        document.getElementById("productStock").value = product.stock;
        document.getElementById("productMinStock").value = product.minStock;
        document.getElementById("productPrice").value = product.price;

        document.getElementById("productModal").classList.remove("hidden");
      }

      // Função para submeter formulário de produto
      function submitProductForm(event) {
        event.preventDefault();

        const productId = document.getElementById("productId").value;
        const name = document.getElementById("productName").value.trim();
        const stock = parseInt(document.getElementById("productStock").value);
        const minStock = parseInt(document.getElementById("productMinStock").value);
        const price = parseFloat(document.getElementById("productPrice").value);

        // Validação: nome duplicado (cenário negativo do teste)
        const existingProduct = stockProducts.find(p => 
          p.name.toLowerCase() === name.toLowerCase() && p.id !== productId
        );

        if (existingProduct) {
          showToast(
            "Erro no Cadastro",
            "Já existe um produto com este nome. Escolha outro nome.",
            "error",
            "error"
          );
          return;
        }

        if (productId) {
          // Editar produto existente
          const index = stockProducts.findIndex(p => p.id === productId);
          if (index !== -1) {
            stockProducts[index] = {
              ...stockProducts[index],
              name,
              stock,
              minStock,
              price
            };

            addStockHistory(productId, "ATUALIZAÇÃO", 0, "Produto atualizado");
            showToast("Produto Atualizado!", "As alterações foram salvas com sucesso.", "check_circle");
          }
        } else {
          // Criar novo produto (cenário positivo do teste)
          const newProduct = {
            id: "st" + Date.now(),
            name,
            stock,
            minStock,
            price,
            active: true
          };

          stockProducts.push(newProduct);
          addStockHistory(newProduct.id, "CADASTRO", stock, "Estoque inicial");
          showToast("Produto Cadastrado!", `${name} foi adicionado ao estoque.`, "check_circle");
        }

        saveStockData();
        renderStockTable();
        closeModal("productModal");
      }

      // Função para abrir modal de atualização de estoque
      function openUpdateStockModal(productId) {
        const product = stockProducts.find(p => p.id === productId);
        if (!product) return;

        document.getElementById("updateStockProductId").value = product.id;
        document.getElementById("updateStockProductName").value = product.name;
        document.getElementById("updateStockCurrent").value = product.stock;
        document.getElementById("updateStockQuantity").value = "";

        document.getElementById("updateStockModal").classList.remove("hidden");
      }

      // Função para submeter atualização de estoque (cenário positivo do teste)
      function submitStockUpdate(event) {
        event.preventDefault();

        const productId = document.getElementById("updateStockProductId").value;
        const quantity = parseInt(document.getElementById("updateStockQuantity").value);

        if (quantity < 0) {
          showToast("Erro", "A quantidade deve ser positiva.", "error", "error");
          return;
        }

        const product = stockProducts.find(p => p.id === productId);
        if (!product) return;

        const oldStock = product.stock;
        product.stock += quantity;

        addStockHistory(productId, "ENTRADA", quantity, "Atualização manual de estoque");
        saveStockData();
        renderStockTable();

        showToast(
          "Estoque Atualizado!",
          `${product.name}: ${oldStock} → ${product.stock} unidades`,
          "check_circle"
        );

        closeModal("updateStockModal");
      }

      // Função para alternar status do produto
      function toggleProductStatus(productId) {
        const product = stockProducts.find(p => p.id === productId);
        if (!product) return;

        product.active = !product.active;
        const status = product.active ? "ativado" : "desativado";

        addStockHistory(
          productId,
          product.active ? "ATIVAÇÃO" : "DESATIVAÇÃO",
          0,
          `Produto ${status}`
        );

        saveStockData();
        renderStockTable();

        showToast(
          `Produto ${status.charAt(0).toUpperCase() + status.slice(1)}!`,
          `${product.name} foi ${status} com sucesso.`,
          "check_circle"
        );
      }

      // Função para abrir modal PDV
      function openPdvModal() {
        const select = document.getElementById("pdvProduct");
        select.innerHTML = '<option value="">Selecione um produto</option>';

        // Apenas produtos ativos com estoque
        const availableProducts = stockProducts.filter(p => p.active && p.stock > 0);

        if (availableProducts.length === 0) {
          showToast(
            "Sem Produtos Disponíveis",
            "Não há produtos ativos com estoque para venda.",
            "info",
            "info"
          );
          return;
        }

        availableProducts.forEach(product => {
          const option = document.createElement("option");
          option.value = product.id;
          option.textContent = `${product.name} (${product.stock} un.)`;
          select.appendChild(option);
        });

        document.getElementById("pdvQuantity").value = 1;
        updatePdvSummary();
        document.getElementById("pdvModal").classList.remove("hidden");
      }

      // Função para atualizar resumo do PDV
      function updatePdvSummary() {
        const productId = document.getElementById("pdvProduct").value;
        const quantity = parseInt(document.getElementById("pdvQuantity").value) || 1;

        if (!productId) {
          document.getElementById("pdvAvailableStock").textContent = "-";
          document.getElementById("pdvUnitPrice").textContent = "R$ 0,00";
          document.getElementById("pdvTotal").textContent = "R$ 0,00";
          return;
        }

        const product = stockProducts.find(p => p.id === productId);
        if (!product) return;

        const total = product.price * quantity;

        document.getElementById("pdvAvailableStock").textContent = product.stock;
        document.getElementById("pdvUnitPrice").textContent = `R$ ${product.price.toFixed(2)}`;
        document.getElementById("pdvTotal").textContent = `R$ ${total.toFixed(2)}`;
      }

      // Função para submeter venda PDV (cenários de venda do teste)
      function submitPdvSale(event) {
        event.preventDefault();

        const productId = document.getElementById("pdvProduct").value;
        const quantity = parseInt(document.getElementById("pdvQuantity").value);

        const product = stockProducts.find(p => p.id === productId);
        if (!product) return;

        // Cenário negativo: estoque insuficiente
        if (product.stock < quantity) {
          showToast(
            "Estoque Insuficiente",
            `Apenas ${product.stock} unidades disponíveis de ${product.name}.`,
            "error",
            "error"
          );
          return;
        }

        // Cenário positivo: venda bem-sucedida
        const oldStock = product.stock;
        product.stock -= quantity;
        const total = product.price * quantity;

        addStockHistory(productId, "VENDA", quantity, `Venda PDV - ${quantity}x ${product.name}`);
        saveStockData();
        renderStockTable();

        showToast(
          "Venda Registrada!",
          `${quantity}x ${product.name} vendido(s). Total: R$ ${total.toFixed(2)}`,
          "check_circle"
        );

        closeModal("pdvModal");
      }

      // Função para filtrar estoque
      function filterStock() {
        const searchTerm = document.getElementById("searchStock").value.toLowerCase();
        const rows = document.querySelectorAll("#stockTableBody tr");

        rows.forEach(row => {
          const text = row.textContent.toLowerCase();
          row.style.display = text.includes(searchTerm) ? "" : "none";
        });
      }

      // Função para adicionar histórico de movimentação
      function addStockHistory(productId, type, quantity, observation) {
        const product = stockProducts.find(p => p.id === productId);
        if (!product) return;

        const entry = {
          id: "hist" + Date.now(),
          productId,
          productName: product.name,
          type,
          quantity,
          observation,
          timestamp: new Date().toISOString()
        };

        stockHistory.push(entry);
        localStorage.setItem("stockHistory", JSON.stringify(stockHistory));
      }

      // Função para salvar dados de estoque
      function saveStockData() {
        localStorage.setItem("stockProducts", JSON.stringify(stockProducts));
      }

      // ==================== FIM DAS FUNÇÕES DE ESTOQUE ====================

      // ==================== FUNÇÕES DE GESTÃO DE AGENDAMENTOS ====================

      // Função para renderizar tabela de agendamentos
      function renderAppointmentsTable() {
        const tbody = document.getElementById("appointmentsTableBody");
        tbody.innerHTML = "";

        if (appointments.length === 0) {
          tbody.innerHTML = `
            <tr>
              <td colspan="6" class="text-center py-8 text-gray-400">
                Nenhum agendamento encontrado. Clique em "Novo Agendamento" para começar.
              </td>
            </tr>
          `;
          return;
        }

        // Ordenar por data e hora
        const sortedAppointments = [...appointments].sort((a, b) => {
          const dateA = new Date(`${a.date}T${a.time}`);
          const dateB = new Date(`${b.date}T${b.time}`);
          return dateA - dateB;
        });

        sortedAppointments.forEach((apt) => {
          const statusBadge = getStatusBadge(apt.status);
          const dateFormatted = formatDate(apt.date);
          
          const row = `
            <tr class="border-b border-dark-600 hover:bg-dark-600/50">
              <td class="px-6 py-4">
                <div class="flex flex-col">
                  <span class="font-medium">${dateFormatted}</span>
                  <span class="text-sm text-gray-400">${apt.time}</span>
                </div>
              </td>
              <td class="px-6 py-4">
                <div class="flex items-center gap-2">
                  <span class="material-icons text-primary text-sm">person</span>
                  <span>${apt.clientName}</span>
                </div>
              </td>
              <td class="px-6 py-4">
                <div class="flex items-center gap-2">
                  <span class="material-icons text-blue-400 text-sm">badge</span>
                  <span>${apt.professionalName}</span>
                </div>
              </td>
              <td class="px-6 py-4">
                <div class="flex flex-col">
                  <span class="font-medium">${apt.serviceName}</span>
                  <span class="text-sm text-gray-400">${apt.duration} min - R$ ${apt.price.toFixed(2)}</span>
                </div>
              </td>
              <td class="text-center px-6 py-4">${statusBadge}</td>
              <td class="text-center px-6 py-4">
                <div class="flex items-center justify-center gap-2">
                  ${apt.status === 'pendente' ? `
                    <button
                      onclick="confirmAppointmentAdmin('${apt.id}')"
                      class="material-icons text-green-400 hover:text-green-300 cursor-pointer"
                      title="Confirmar"
                    >
                      check_circle
                    </button>
                  ` : ''}
                  ${apt.status !== 'cancelado' && apt.status !== 'concluido' ? `
                    <button
                      onclick="openEditAppointmentModal('${apt.id}')"
                      class="material-icons text-blue-400 hover:text-blue-300 cursor-pointer"
                      title="Editar"
                    >
                      edit
                    </button>
                    <button
                      onclick="cancelAppointmentAdmin('${apt.id}')"
                      class="material-icons text-red-400 hover:text-red-300 cursor-pointer"
                      title="Cancelar"
                    >
                      cancel
                    </button>
                  ` : ''}
                  <button
                    onclick="viewAppointmentDetails('${apt.id}')"
                    class="material-icons text-primary hover:text-orange-400 cursor-pointer"
                    title="Ver detalhes"
                  >
                    visibility
                  </button>
                </div>
              </td>
            </tr>
          `;
          tbody.innerHTML += row;
        });

        updateAppointmentsSummary();
      }

      // Função para obter badge de status
      function getStatusBadge(status) {
        const badges = {
          pendente: '<span class="inline-flex items-center gap-1 bg-yellow-500/10 text-yellow-400 px-3 py-1 rounded-full text-sm"><span class="material-icons text-sm">pending</span> Pendente</span>',
          confirmado: '<span class="inline-flex items-center gap-1 bg-green-500/10 text-green-400 px-3 py-1 rounded-full text-sm"><span class="material-icons text-sm">check_circle</span> Confirmado</span>',
          cancelado: '<span class="inline-flex items-center gap-1 bg-red-500/10 text-red-400 px-3 py-1 rounded-full text-sm"><span class="material-icons text-sm">cancel</span> Cancelado</span>',
          concluido: '<span class="inline-flex items-center gap-1 bg-blue-500/10 text-blue-400 px-3 py-1 rounded-full text-sm"><span class="material-icons text-sm">done_all</span> Concluído</span>'
        };
        return badges[status] || badges.pendente;
      }

      // Função para formatar data
      function formatDate(dateString) {
        const date = new Date(dateString + 'T00:00:00');
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        
        const tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);
        
        if (date.toDateString() === today.toDateString()) {
          return "Hoje";
        } else if (date.toDateString() === tomorrow.toDateString()) {
          return "Amanhã";
        } else {
          return date.toLocaleDateString('pt-BR');
        }
      }

      // Função para atualizar resumo de agendamentos
      function updateAppointmentsSummary() {
        const today = new Date().toISOString().split('T')[0];
        const todayAppointments = appointments.filter(apt => apt.date === today);
        
        document.getElementById("totalAppointmentsToday").textContent = todayAppointments.length;
        document.getElementById("confirmedAppointments").textContent = 
          appointments.filter(apt => apt.status === 'confirmado').length;
        document.getElementById("pendingAppointments").textContent = 
          appointments.filter(apt => apt.status === 'pendente').length;
        document.getElementById("canceledAppointments").textContent = 
          appointments.filter(apt => apt.status === 'cancelado').length;
      }

      // Função para abrir modal de novo agendamento (admin)
      function openNewAppointmentAdminModal() {
        // Resetar formulário
        document.getElementById("appointmentAdminClient").value = "";
        document.getElementById("appointmentAdminService").value = "";
        document.getElementById("appointmentAdminProfessional").value = "";
        document.getElementById("appointmentAdminDate").value = "";
        document.getElementById("appointmentAdminTime").value = "";
        document.getElementById("appointmentAdminObservations").value = "";
        
        // Ocultar alertas
        document.getElementById("qualificationWarning").classList.add("hidden");
        document.getElementById("conflictWarning").classList.add("hidden");
        document.getElementById("appointmentAdminSummary").classList.add("hidden");

        // Preencher serviços ativos
        const serviceSelect = document.getElementById("appointmentAdminService");
        serviceSelect.innerHTML = '<option value="">Selecione um serviço</option>';
        
        servicesData.filter(s => s.status === "Ativo").forEach(service => {
          const option = document.createElement("option");
          option.value = service.id;
          option.textContent = `${service.name} - ${service.duration}min - R$ ${service.price.toFixed(2)}`;
          option.dataset.duration = service.duration;
          option.dataset.price = service.price;
          option.dataset.assignedProfs = JSON.stringify(service.assignedProfIds);
          serviceSelect.appendChild(option);
        });

        // Preencher profissionais ativos
        const profSelect = document.getElementById("appointmentAdminProfessional");
        profSelect.innerHTML = '<option value="">Selecione um profissional</option>';
        
        professionalsData.filter(p => p.status === "Ativo").forEach(prof => {
          const option = document.createElement("option");
          option.value = prof.id;
          option.textContent = `${prof.name} (${prof.level})`;
          profSelect.appendChild(option);
        });

        // Definir data mínima como hoje
        const today = new Date().toISOString().split('T')[0];
        document.getElementById("appointmentAdminDate").min = today;

        document.getElementById("newAppointmentAdminModal").classList.remove("hidden");
      }

      // Função para atualizar informações do serviço selecionado
      function updateAdminServiceInfo() {
        const select = document.getElementById("appointmentAdminService");
        const option = select.options[select.selectedIndex];

        if (option.value) {
          const duration = option.dataset.duration;
          const price = option.dataset.price;

          document.getElementById("summaryService").textContent = option.textContent.split(' - ')[0];
          document.getElementById("summaryDuration").textContent = `${duration} minutos`;
          document.getElementById("summaryPrice").textContent = `R$ ${parseFloat(price).toFixed(2)}`;
          document.getElementById("appointmentAdminSummary").classList.remove("hidden");

          // Revalidar qualificação
          checkAdminProfessionalQualification();
        } else {
          document.getElementById("appointmentAdminSummary").classList.add("hidden");
        }
      }

      // Função para verificar qualificação do profissional (cenário negativo do teste)
      function checkAdminProfessionalQualification() {
        const serviceSelect = document.getElementById("appointmentAdminService");
        const profSelect = document.getElementById("appointmentAdminProfessional");
        
        if (!serviceSelect.value || !profSelect.value) {
          document.getElementById("qualificationWarning").classList.add("hidden");
          document.getElementById("submitAppointmentBtn").disabled = false;
          return;
        }

        const selectedOption = serviceSelect.options[serviceSelect.selectedIndex];
        const assignedProfIds = JSON.parse(selectedOption.dataset.assignedProfs || '[]');
        const selectedProfId = profSelect.value;

        // Verificar se profissional está qualificado para o serviço
        if (!assignedProfIds.includes(selectedProfId)) {
          document.getElementById("qualificationWarning").classList.remove("hidden");
          document.getElementById("submitAppointmentBtn").disabled = true;
        } else {
          document.getElementById("qualificationWarning").classList.add("hidden");
          document.getElementById("submitAppointmentBtn").disabled = false;
          checkAdminTimeSlotAvailability();
        }
      }

      // Função para verificar disponibilidade de horário (cenário de conflito do teste)
      function checkAdminTimeSlotAvailability() {
        const profId = document.getElementById("appointmentAdminProfessional").value;
        const date = document.getElementById("appointmentAdminDate").value;
        const time = document.getElementById("appointmentAdminTime").value;

        if (!profId || !date || !time) {
          document.getElementById("conflictWarning").classList.add("hidden");
          return;
        }

        // Verificar conflitos de horário
        const hasConflict = appointments.some(apt => 
          apt.professionalId === profId &&
          apt.date === date &&
          apt.time === time &&
          apt.status !== 'cancelado'
        );

        if (hasConflict) {
          document.getElementById("conflictWarning").classList.remove("hidden");
          document.getElementById("submitAppointmentBtn").disabled = true;
        } else {
          document.getElementById("conflictWarning").classList.add("hidden");
          // Só habilitar se não houver problema de qualificação
          const qualWarning = document.getElementById("qualificationWarning");
          if (qualWarning.classList.contains("hidden")) {
            document.getElementById("submitAppointmentBtn").disabled = false;
          }
        }
      }

      // Função para submeter novo agendamento (admin) - cenário positivo do teste
      function submitNewAppointmentAdmin(event) {
        event.preventDefault();

        const clientId = document.getElementById("appointmentAdminClient").value;
        const clientName = document.getElementById("appointmentAdminClient").options[
          document.getElementById("appointmentAdminClient").selectedIndex
        ].textContent;

        const serviceSelect = document.getElementById("appointmentAdminService");
        const serviceOption = serviceSelect.options[serviceSelect.selectedIndex];
        const serviceId = serviceSelect.value;
        const serviceName = serviceOption.textContent.split(' - ')[0];
        const duration = parseInt(serviceOption.dataset.duration);
        const price = parseFloat(serviceOption.dataset.price);

        const profSelect = document.getElementById("appointmentAdminProfessional");
        const profId = profSelect.value;
        const profName = profSelect.options[profSelect.selectedIndex].textContent.split(' (')[0];

        const date = document.getElementById("appointmentAdminDate").value;
        const time = document.getElementById("appointmentAdminTime").value;
        const observations = document.getElementById("appointmentAdminObservations").value;

        // Criar novo agendamento
        const newAppointment = {
          id: "apt" + Date.now(),
          clientId,
          clientName,
          professionalId: profId,
          professionalName: profName,
          serviceId,
          serviceName,
          date,
          time,
          status: "confirmado",
          duration,
          price,
          observations
        };

        appointments.push(newAppointment);
        saveAppointments();
        renderAppointmentsTable();

        showToast(
          "Agendamento Criado!",
          `Agendamento para ${clientName} em ${formatDate(date)} às ${time}`,
          "check_circle"
        );

        closeModal("newAppointmentAdminModal");
      }

      // Função para confirmar agendamento
      function confirmAppointmentAdmin(appointmentId) {
        const appointment = appointments.find(apt => apt.id === appointmentId);
        if (!appointment) return;

        appointment.status = "confirmado";
        saveAppointments();
        renderAppointmentsTable();

        showToast(
          "Agendamento Confirmado!",
          `Agendamento de ${appointment.clientName} foi confirmado.`,
          "check_circle"
        );
      }

      // Função para cancelar agendamento (cenário de cancelamento do teste)
      function cancelAppointmentAdmin(appointmentId) {
        const appointment = appointments.find(apt => apt.id === appointmentId);
        if (!appointment) return;

        if (confirm(`Deseja realmente cancelar o agendamento de ${appointment.clientName}?`)) {
          appointment.status = "cancelado";
          saveAppointments();
          renderAppointmentsTable();

          showToast(
            "Agendamento Cancelado",
            `O horário ${appointment.time} de ${formatDate(appointment.date)} está disponível novamente.`,
            "check_circle"
          );
        }
      }

      // Função para visualizar detalhes do agendamento
      function viewAppointmentDetails(appointmentId) {
        const appointment = appointments.find(apt => apt.id === appointmentId);
        if (!appointment) return;

        const details = `
          <strong>Cliente:</strong> ${appointment.clientName}<br>
          <strong>Profissional:</strong> ${appointment.professionalName}<br>
          <strong>Serviço:</strong> ${appointment.serviceName}<br>
          <strong>Data/Hora:</strong> ${formatDate(appointment.date)} às ${appointment.time}<br>
          <strong>Duração:</strong> ${appointment.duration} minutos<br>
          <strong>Valor:</strong> R$ ${appointment.price.toFixed(2)}<br>
          <strong>Status:</strong> ${appointment.status}<br>
          ${appointment.observations ? `<strong>Observações:</strong> ${appointment.observations}` : ''}
        `;

        // Criar modal simples ou usar alert
        alert(details.replace(/<br>/g, '\n').replace(/<strong>|<\/strong>/g, ''));
      }

      // Função para filtrar agendamentos por data
      function filterAppointmentsByDate(period) {
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        let startDate, endDate;

        if (period === 'today') {
          startDate = endDate = today.toISOString().split('T')[0];
        } else if (period === 'week') {
          startDate = today.toISOString().split('T')[0];
          const weekEnd = new Date(today);
          weekEnd.setDate(weekEnd.getDate() + 7);
          endDate = weekEnd.toISOString().split('T')[0];
        }

        // Filtrar appointments
        const filtered = appointments.filter(apt => {
          return apt.date >= startDate && apt.date <= endDate;
        });

        // Renderizar apenas filtrados (simplificado)
        renderAppointmentsTable();
      }

      // Função para filtrar por data customizada
      function filterAppointmentsByCustomDate() {
        const selectedDate = document.getElementById("appointmentFilterDate").value;
        if (selectedDate) {
          // Filtrar appointments pela data selecionada
          renderAppointmentsTable();
        }
      }

      // Função para filtrar agendamentos por busca
      function filterAppointments() {
        const searchTerm = document.getElementById("searchAppointment").value.toLowerCase();
        const rows = document.querySelectorAll("#appointmentsTableBody tr");

        rows.forEach(row => {
          const text = row.textContent.toLowerCase();
          row.style.display = text.includes(searchTerm) ? "" : "none";
        });
      }

      // Função para salvar agendamentos
      function saveAppointments() {
        localStorage.setItem("appointments", JSON.stringify(appointments));
      }

      // Variáveis globais para edição de agendamento
      let editAppointmentId = null;
      let editSelectedTimeSlot = null;
      let editOriginalData = {};

      // Função para abrir modal de edição de agendamento
      function openEditAppointmentModal(appointmentId) {
        const appointment = appointments.find(apt => apt.id === appointmentId);
        
        if (!appointment) {
          showToast("Agendamento não encontrado!", "error", "red");
          return;
        }

        // Verificar se o agendamento pode ser editado
        if (appointment.status === 'cancelado' || appointment.status === 'concluido') {
          showToast("Não é possível editar agendamentos cancelados ou concluídos!", "error", "red");
          return;
        }

        // Armazenar ID e dados originais
        editAppointmentId = appointmentId;
        editSelectedTimeSlot = null;
        editOriginalData = {
          date: appointment.date,
          time: appointment.time,
          observations: appointment.observations || ''
        };

        // Preencher informações atuais
        const dateObj = new Date(appointment.date + 'T00:00:00');
        const formattedDate = dateObj.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' });
        document.getElementById('editCurrentDateTime').textContent = `${formattedDate} ${appointment.time}`;
        document.getElementById('editCurrentProfessional').textContent = appointment.professionalName;
        document.getElementById('editCurrentService').textContent = appointment.serviceName;
        
        const statusLabels = {
          'pendente': 'Pendente',
          'confirmado': 'Confirmado',
          'concluido': 'Concluído',
          'cancelado': 'Cancelado'
        };
        document.getElementById('editCurrentStatus').textContent = statusLabels[appointment.status] || appointment.status;

        // Preencher observações atuais
        document.getElementById('editAppointmentNotes').value = appointment.observations || '';

        // Configurar data mínima (hoje)
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('editAppointmentDate').setAttribute('min', today);
        document.getElementById('editAppointmentDate').value = '';

        // Limpar seleção de horários
        document.getElementById('editAvailableSlots').innerHTML = 
          '<div class="text-center text-gray-400 text-sm col-span-4 py-8">Selecione uma data para ver os horários disponíveis</div>';

        // Ocultar resumo
        document.getElementById('editSummary').classList.add('hidden');

        // Abrir modal
        document.getElementById("editAppointmentModal").classList.remove("hidden");
      }

      // Função para carregar horários disponíveis para edição
      function loadEditAvailableSlots() {
        const date = document.getElementById('editAppointmentDate').value;
        
        if (!date) {
          document.getElementById('editAvailableSlots').innerHTML = 
            '<div class="text-center text-gray-400 text-sm col-span-4 py-8">Selecione uma data para ver os horários disponíveis</div>';
          return;
        }

        // Gerar horários disponíveis (8h às 18h)
        const slots = generateTimeSlots(null);
        
        let slotsHTML = '';
        slots.forEach(slot => {
          slotsHTML += `
            <button type="button" onclick="selectEditTimeSlot(this, '${slot.time}', ${slot.available})" 
              class="edit-time-slot ${slot.available ? 'bg-dark-700 hover:bg-dark-600 hover:border-primary' : 'bg-dark-700/50 cursor-not-allowed'} 
              border border-dark-600 rounded-lg py-3 text-sm font-medium transition ${!slot.available ? 'text-gray-600' : ''}"
              ${!slot.available ? 'disabled' : ''}>
              ${slot.time}
            </button>
          `;
        });
        
        document.getElementById('editAvailableSlots').innerHTML = slotsHTML;
      }

      // Função para selecionar horário de edição
      function selectEditTimeSlot(element, time, available) {
        if (!available) return;
        
        // Remover seleção anterior
        document.querySelectorAll('.edit-time-slot').forEach(slot => {
          slot.classList.remove('bg-primary', 'border-primary', 'text-white');
          slot.classList.add('border-dark-600');
        });
        
        // Adicionar seleção ao slot clicado
        element.classList.remove('border-dark-600');
        element.classList.add('bg-primary', 'border-primary', 'text-white');
        
        editSelectedTimeSlot = time;
        updateEditSummary();
      }

      // Função para atualizar resumo de edição
      function updateEditSummary() {
        const date = document.getElementById('editAppointmentDate').value;
        const notes = document.getElementById('editAppointmentNotes').value.trim();
        
        const hasDateChange = date && editSelectedTimeSlot;
        const hasNotesChange = notes !== editOriginalData.observations;
        
        if (!hasDateChange && !hasNotesChange) {
          document.getElementById('editSummary').classList.add('hidden');
          return;
        }
        
        // Mostrar resumo
        document.getElementById('editSummary').classList.remove('hidden');
        
        // Atualizar alteração de data/hora
        if (hasDateChange) {
          const dateObj = new Date(date + 'T00:00:00');
          const formattedDate = dateObj.toLocaleDateString('pt-BR', { 
            day: '2-digit', 
            month: '2-digit'
          });
          
          document.getElementById('editNewDateTime').textContent = `${formattedDate} ${editSelectedTimeSlot}`;
          document.getElementById('editSummaryDateTime').classList.remove('hidden');
        } else {
          document.getElementById('editSummaryDateTime').classList.add('hidden');
        }
        
        // Atualizar alteração de observações
        if (hasNotesChange) {
          document.getElementById('editNewNotes').textContent = notes || '(Observações removidas)';
          document.getElementById('editSummaryNotes').classList.remove('hidden');
        } else {
          document.getElementById('editSummaryNotes').classList.add('hidden');
        }
      }

      // Adicionar listener para observações
      if (!window.editNotesListenerAdded) {
        window.addEventListener('DOMContentLoaded', function() {
          const editNotesField = document.getElementById('editAppointmentNotes');
          if (editNotesField) {
            editNotesField.addEventListener('input', updateEditSummary);
          }
        });
        window.editNotesListenerAdded = true;
      }

      // Função para atualizar informações do serviço no modal de edição
      function updateEditServiceInfo() {
        const serviceSelect = document.getElementById("editAppointmentService");
        const selectedOption = serviceSelect.options[serviceSelect.selectedIndex];
        const infoCard = document.getElementById("editServiceInfoCard");

        if (selectedOption.value) {
          const duration = selectedOption.dataset.duration;
          const price = parseFloat(selectedOption.dataset.price);

          document.getElementById("editServiceDuration").textContent = `${duration} minutos`;
          document.getElementById("editServicePrice").textContent = `R$ ${price.toFixed(2)}`;
          infoCard.classList.remove("hidden");

          // Verificar qualificação do profissional
          checkEditProfessionalQualification();
        } else {
          infoCard.classList.add("hidden");
        }
      }

      // Função para verificar qualificação do profissional no modal de edição
      function checkEditProfessionalQualification() {
        const serviceSelect = document.getElementById("editAppointmentService");
        const profSelect = document.getElementById("editAppointmentProfessional");
        const warning = document.getElementById("editQualificationWarning");

        if (!serviceSelect.value || !profSelect.value) {
          warning.classList.add("hidden");
          return;
        }

        const selectedServiceOption = serviceSelect.options[serviceSelect.selectedIndex];
        const serviceProfId = selectedServiceOption.dataset.professionalId;
        const selectedProfId = profSelect.value;

        if (serviceProfId !== selectedProfId) {
          warning.classList.remove("hidden");
        } else {
          warning.classList.add("hidden");
        }
      }

      // Função para submeter edição de agendamento
      function submitEditAppointment(event) {
        event.preventDefault();

        const date = document.getElementById('editAppointmentDate').value;
        const notes = document.getElementById('editAppointmentNotes').value.trim();
        
        const hasDateChange = date && editSelectedTimeSlot;
        const hasNotesChange = notes !== editOriginalData.observations;
        
        if (!hasDateChange && !hasNotesChange) {
          showToast('Nenhuma alteração foi realizada.', 'warning', 'yellow');
          return;
        }
        
        // Validar antecedência de 2 horas se houver mudança de horário
        if (hasDateChange) {
          const appointmentDateTime = new Date(date + 'T' + editSelectedTimeSlot);
          const now = new Date();
          const hoursDifference = (appointmentDateTime - now) / (1000 * 60 * 60);
          
          if (hoursDifference < 2) {
            showToast('Alterações de horário devem ser feitas com pelo menos 2 horas de antecedência.', 'warning', 'yellow');
            return;
          }

          // Verificar conflito de horário
          const appointment = appointments.find(apt => apt.id === editAppointmentId);
          if (appointment) {
            const conflict = appointments.find(apt => {
              if (apt.id === editAppointmentId) return false;
              if (apt.status === 'cancelado') return false;
              if (apt.professionalId !== appointment.professionalId) return false;
              if (apt.date !== date) return false;

              // Verificar sobreposição de horários
              const aptTime = apt.time;
              const selectedTime = editSelectedTimeSlot;
              
              const aptStart = new Date(date + 'T' + aptTime);
              const aptEnd = new Date(aptStart.getTime() + apt.duration * 60000);
              const selectedStart = new Date(date + 'T' + selectedTime);
              const selectedEnd = new Date(selectedStart.getTime() + appointment.duration * 60000);

              return (
                (selectedStart >= aptStart && selectedStart < aptEnd) ||
                (selectedEnd > aptStart && selectedEnd <= aptEnd) ||
                (selectedStart <= aptStart && selectedEnd >= aptEnd)
              );
            });

            if (conflict) {
              showToast('O profissional já possui agendamento neste horário.', 'error', 'red');
              return;
            }
          }
        }
        
        // Atualizar agendamento
        const appointmentIndex = appointments.findIndex(apt => apt.id === editAppointmentId);
        if (appointmentIndex !== -1) {
          if (hasDateChange) {
            appointments[appointmentIndex].date = date;
            appointments[appointmentIndex].time = editSelectedTimeSlot;
          }
          if (hasNotesChange) {
            appointments[appointmentIndex].observations = notes;
          }

          saveAppointments();
          renderAppointmentsTable();

          // Processar mensagem de alterações
          const changes = [];
          if (hasDateChange) {
            const dateObj = new Date(date + 'T00:00:00');
            const formattedDate = dateObj.toLocaleDateString('pt-BR', { 
              day: '2-digit', 
              month: '2-digit'
            });
            changes.push(`horário alterado para ${formattedDate} às ${editSelectedTimeSlot}`);
          }
          if (hasNotesChange) {
            changes.push('observações atualizadas');
          }
          
          const message = `Agendamento atualizado com sucesso! ${changes.join(' e ')}.`;
          
          closeModal('editAppointmentModal');
          showToast(message, 'check_circle', 'green');
        } else {
          showToast('Agendamento não encontrado.', 'error', 'red');
        }
      }


      // ==================== FIM DAS FUNÇÕES DE AGENDAMENTOS ====================

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
        } else if (modalType === "stockManagement") {
          document
            .getElementById("stockManagementModal")
            .classList.remove("hidden");
          // Renderizar tabela de estoque
          renderStockTable();
        } else if (modalType === "appointmentManagement") {
          document
            .getElementById("appointmentManagementModal")
            .classList.remove("hidden");
          // Renderizar tabela de agendamentos
          renderAppointmentsTable();
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
