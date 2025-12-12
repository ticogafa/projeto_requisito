# Proposta de Funcionalidade: Auto Gerenciamento de Profissionais

## 1. Visão Geral
A funcionalidade de **Auto Gerenciamento** visa empoderar os barbeiros/cabeleireiros, permitindo que eles tenham controle sobre seus próprios dados, horários e métricas de desempenho. Isso reduz a carga administrativa do gerente e aumenta a autonomia da equipe.

## 2. Funcionalidades Sugeridas (Foco no Usuário)

### A. Gestão de Perfil (O Básico)
Permitir que o profissional mantenha seus dados atualizados para que os clientes vejam informações precisas.
*   **Avatar/Foto:** Upload de foto de perfil (ajuda o cliente a identificar).
*   **Biografia/Especialidade:** Pequeno texto onde o profissional descreve seus pontos fortes (ex: "Especialista em cortes degradê e barboterapia").
*   **Dados de Contato:** Atualização de telefone/WhatsApp visível para o cliente (opcional).

### B. Gestão de Jornada e Disponibilidade (O Core)
Atualmente, a jornada parece ser fixa no cadastro. O ideal é torná-la flexível.
*   **Horário de Trabalho Padrão:** O profissional define que nas terças trabalha das 10h às 19h, e nas sextas das 08h às 17h.
*   **Gestão de Pausas/Almoço:** Bloqueio automático de horário de almoço (ex: 12h às 13h) para que ninguém agende nesse período.
*   **Bloqueios de Exceção:** "Vou ao médico na próxima quinta-feira às 14h". O profissional cria um bloqueio na agenda sem precisar pedir ao administrador.
*   **Modo "Ausente/Férias":** Botão para desativar a agenda por um período específico.

### C. Dashboard de Desempenho (Gamificação e Controle)
Dar visibilidade sobre o trabalho realizado.
*   **Meus Agendamentos:** Visualização de lista ou calendário ("Minha Agenda do Dia").
*   **Histórico:** "Quantos cortes fiz este mês?".
*   **Comissão Estimada:** Baseado no `%` de comissão já cadastrado no banco, mostrar quanto ele já faturou no mês corrente.
*   **Feedback:** Se houver sistema de avaliação, mostrar a nota média dele dada pelos clientes.

### D. Gestão de Serviços Habilitados
Embora a barbearia defina o preço, o profissional pode escolher o que faz.
*   **Toggle de Habilidade:** Uma lista de todos os serviços da barbearia onde ele pode marcar/desmarcar o que realiza.
    *   *Exemplo:* O "Carlos" decide que não fará mais "Platinado" temporariamente pois está sem o produto ou lesionado. Ele desmarca e o sistema não permite mais agendar esse serviço com ele.

### E. Portfólio (Visual)
*   **Galeria de Trabalhos:** O profissional pode subir fotos de cortes realizados ("Antes e Depois") que ficam vinculados ao perfil dele na hora do agendamento.

---

## 3. Implicações Técnicas e Backend

### 3.1 Melhorias na Entidade e Serviço (`Profissional` e `ProfissionalServico`)

Além das funcionalidades básicas, podemos expandir o domínio para suportar regras de negócio mais ricas diretamente no backend:

#### **A. Agenda Flexível e Bloqueios (Refinamento da `Agenda`)**
Atualmente `Agenda` tem apenas `inicioJornada` e `fimJornada`.
*   **Ideia:** Transformar `Agenda` em uma entidade ou ValueObject mais complexo que suporte dias da semana.
*   **Backend (`Profissional.java` / `Agenda.java`):**
    *   Adicionar `Map<DiaSemana, IntervaloJornada> jornadaSemanal`.
    *   Criar entidade `BloqueioAgenda` (OneToMany em Profissional) para exceções (férias, médico).
*   **Serviço (`ProfissionalServico.java`):**
    *   Novo método: `adicionarBloqueio(ProfissionalId id, Bloqueio bloqueio)`.
    *   Novo método: `definirJornadaDia(ProfissionalId id, DiaSemana dia, LocalTime inicio, LocalTime fim)`.
    *   Atualizar `buscarDisponiveisNaDataHora` para respeitar esses novos bloqueios.

#### **B. Gestão Dinâmica de Habilidades (Serviços Oferecidos)**
*   **Ideia:** Permitir que o profissional ative/desative serviços sem remover a "qualificação". Ele sabe fazer, mas não quer fazer no momento.
*   **Backend:**
    *   A relação ManyToMany `profissional_servico` poderia ter uma coluna extra `ativo` (status da habilitação).
*   **Serviço (`ProfissionalServico.java`):**
    *   Novo método: `alternarDisponibilidadeServico(ProfissionalId id, ServicoOferecidoId servicoId, boolean ativo)`.
    *   Isso difere de `adicionarQualificacao` (que implica saber fazer) vs `disponibilizarServico` (querer fazer agora).

#### **C. Métricas e Comissões (Domínio Analítico)**
*   **Ideia:** Calcular performance.
*   **Backend (`ProfissionalServico.java`):**
    *   Método `calcularComissaoPeriodo(ProfissionalId id, LocalDate inicio, LocalDate fim)`: Itera sobre agendamentos CONCLUIDOS, soma valores e aplica o percentual de comissão do profissional.
    *   Método `obterMetricas(ProfissionalId id)`: Retorna DTO com total atendimentos, nota média, faturamento.

#### **D. Auditoria e Logs de Alteração**
*   **Ideia:** Saber quem alterou a agenda ou dados do profissional.
*   **Backend:**
    *   Uso de eventos de domínio já existentes (`ProfissionalEvent`), mas criando um Listener para persistir um histórico (`LogAuditoriaProfissional`).
    *   No `ProfissionalControlador`, garantir que as alterações de "Auto Gerenciamento" passem o ID do usuário logado para validar se ele está alterando o PRÓPRIO perfil (segurança).

### 3.2 Novos Endpoints (`ProfissionalControlador`)

*   `PATCH /api/profissional/{id}/agenda`: Atualizar horários específicos.
*   `POST /api/profissional/{id}/bloqueios`: Adicionar "ausência".
*   `GET /api/profissional/{id}/metricas`: Retornar dashboard financeiro/produtividade.
*   `PUT /api/profissional/{id}/servicos/{servicoId}/status`: Ativar/Desativar oferta de serviço.

---

## 4. Sugestão de MVP (Mínimo Produto Viável)
Para a entrega da funcionalidade "Auto Gerenciamento", foque em:
1.  **Login do Profissional.**
2.  **Visualizar "Minha Agenda"** (Lista de agendamentos futuros filtrada pelo ID do usuário logado).
3.  **Editar Jornada de Trabalho** (Alterar hora de início e fim no banco).