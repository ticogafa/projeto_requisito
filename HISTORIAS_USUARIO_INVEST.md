# Histórias de Usuário - Auto Gerenciamento de Jornada (Princípio INVEST)

Este documento descreve as histórias de usuário implementadas para a funcionalidade de Auto Gerenciamento de Jornada, estruturadas segundo os critérios INVEST (Independent, Negotiable, Valuable, Estimable, Small, Testable).

---

## História 1: Configuração de Horário de Trabalho Diário

**Como** profissional da barbearia (Barbeiro/Cabeleireiro),
**Quero** definir meus horários de início e fim de expediente para cada dia da semana,
**Para que** o sistema só permita agendamentos quando eu estiver realmente trabalhando.

### Critérios INVEST
*   **Independent (Independente):** Pode ser implementada sem depender de outras funcionalidades complexas (como bloqueios de férias).
*   **Negotiable (Negociável):** A interface (tabela vs calendário) pôde ser discutida, mas o núcleo (definir horas) manteve-se.
*   **Valuable (Valiosa):** Evita que clientes agendem em horários que o profissional não está na loja, reduzindo frustração e cancelamentos.
*   **Estimable (Estimável):** O esforço foi claro: criar tabela no banco, endpoint CRUD e interface de inputs de hora.
*   **Small (Pequena):** Focada apenas na jornada padrão recorrente (Seg-Dom), sem exceções complexas.
*   **Testable (Testável):**
    *   *Cenário:* Profissional define Seg 09:00-18:00. Sistema salva.
    *   *Cenário:* Profissional tenta definir fim (08:00) menor que início (09:00). Sistema rejeita.

---

## História 2: Definição de Intervalos de Pausa

**Como** profissional da barbearia,
**Quero** configurar meu horário de almoço ou intervalo para dias específicos,
**Para que** eu tenha tempo garantido para descanso sem risco de agendamentos conflitantes.

### Critérios INVEST
*   **Independent:** Pode ser feita após a configuração básica do dia, ou junto, mas não depende de *outros* módulos.
*   **Negotiable:** Poderia ser fixo (1h) ou flexível. Decidiu-se por flexível (início/fim).
*   **Valuable:** Garante qualidade de vida ao profissional e evita atendimento apressado/atrasado.
*   **Estimable:** Adição de dois campos na tabela e validação lógica simples.
*   **Small:** Funcionalidade atômica dentro da gestão de jornada.
*   **Testable:**
    *   *Cenário:* Intervalo definido das 12:00 às 13:00.
    *   *Cenário:* Tentar definir intervalo (19:00-20:00) fora do expediente (09:00-18:00). Sistema rejeita.

---

## História 3: Ativação e Desativação de Dias de Trabalho

**Como** profissional,
**Quero** marcar rapidamente quais dias da semana eu não trabalho (folgas),
**Para que** minha agenda fique completamente bloqueada nesses dias sem precisar apagar os horários configurados.

### Critérios INVEST
*   **Independent:** O estado "ativo/inativo" independe dos valores de horário.
*   **Negotiable:** Poderia ser feito deletando os horários, mas um "toggle" (checkbox) é mais usável.
*   **Valuable:** Facilita a gestão de escalas rotativas ou folgas fixas.
*   **Estimable:** Adição de uma coluna booleana e um checkbox no frontend.
*   **Small:** Mudança mínima no modelo de dados e UI.
*   **Testable:**
    *   *Cenário:* Desmarcar "Domingo". Tentar agendar para domingo -> Sistema não mostra disponibilidade.

---

## Critérios de Aceitação Gerais (Validadores)

Para considerar essas histórias "Prontas" (Done), os seguintes critérios técnicos foram atendidos:
1.  **Persistência:** Os dados são salvos em banco relacional (`jornada_trabalho`) e recuperados corretamente.
2.  **Validação de Negócio:**
    *   `Hora Fim` > `Hora Início`.
    *   `Intervalo` deve estar contido entre `Hora Início` e `Hora Fim`.
    *   `Intervalo Fim` > `Intervalo Início`.
3.  **Interface:** O profissional visualiza feedback claro (Toasts) em caso de sucesso ou erro.
