# Relatório de Implementação: Auto Gerenciamento de Profissionais (Jornada)

## 1. Visão Geral
Este documento detalha o plano de implementação da funcionalidade de **Auto Gerenciamento**, com foco específico na **Atualização de Jornada de Trabalho** pelo próprio profissional. A arquitetura seguirá estritamente os padrões do projeto de referência `@sgb-2025-01`, utilizando DDD (Domain-Driven Design) e separação clara de camadas (Domínio, Aplicação, Apresentação).

## 2. Arquitetura Backend (`barbearia-backend`)

A estrutura será dividida em camadas, conforme o padrão SGB.

### 2.1 Camada de Domínio (`dominio-principal`)
Responsável pelas regras de negócio e entidades.

*   **Entidade `Agenda` (Refatoração):**
    *   Atualmente simples, será evoluída para suportar dias da semana.
    *   Novo Value Object: `JornadaDiaria` (DiaSemana, Inicio, Fim, IntervaloInicio, IntervaloFim).
    *   A entidade `Profissional` passará a ter uma lista/mapa de `JornadaDiaria`.

*   **Serviço de Domínio (`ProfissionalServico.java`):**
    *   Método `atualizarJornada(ProfissionalId id, List<JornadaDiaria> novaJornada)`.
    *   Validações: Horário de fim deve ser maior que início, intervalos devem estar dentro da jornada.

*   **Repositório (`ProfissionalRepositorio.java`):**
    *   Contrato para persistir a nova estrutura de jornada.

### 2.2 Camada de Aplicação (`aplicacao`)
Orquestra o fluxo e converte DTOs para Domínio.

*   **DTOs (`dev.sauloaraujo.sgb.aplicacao...` style):**
    *   `AtualizarJornadaComando`: DTO recebido do controlador com os dados brutos.
    *   `JornadaResumo`: DTO para leitura da jornada atual.

*   **Serviço de Aplicação (`ProfissionalServicoAplicacao.java`):**
    *   Ponte entre o Controlador e o Domínio.
    *   Método `atualizarJornada(AtualizarJornadaComando comando)`.
    *   Busca o profissional, converte o comando para objetos de valor, invoca o serviço de domínio e salva.

### 2.3 Camada de Apresentação (`apresentacao-backend`)
Controladores REST e Mapeadores.

*   **Controlador (`ProfissionalJornadaControlador.java`):**
    *   Endpoint `GET /api/profissional/me/jornada`: Retorna a jornada atual do usuário logado.
    *   Endpoint `PUT /api/profissional/me/jornada`: Recebe o JSON de atualização.
    *   Uso de `BackendMapeador` (ModelMapper) para converter DTOs <-> Entidades, similar ao SGB.

*   **Segurança:**
    *   Extrair o ID do profissional do token JWT (Contexto de Segurança) para garantir que ele só altere a PRÓPRIA jornada.

## 3. Banco de Dados

*   **Tabela `jornada_trabalho`:**
    *   `id` (PK)
    *   `profissional_id` (FK)
    *   `dia_semana` (ENUM: SEGUNDA, TERCA...)
    *   `inicio` (TIME)
    *   `fim` (TIME)
    *   `intervalo_inicio` (TIME, nullable)
    *   `intervalo_fim` (TIME, nullable)

## 4. Frontend (`apresentacao-frontend`)

### 4.1 Serviços (`ProfissionalService.ts`)
*   Método `getMinhaJornada()`: GET `/api/profissional/me/jornada`
*   Método `atualizarMinhaJornada(jornada)`: PUT `/api/profissional/me/jornada`

### 4.2 Componentes React
*   **`JornadaManager.tsx`:**
    *   Grid com os 7 dias da semana.
    *   Para cada dia, inputs de "Início" e "Fim" e checkbox "Folga".
    *   Validação visual (ex: erro se tentar salvar fim < início).
    *   Botão "Salvar Alterações".

*   **Integração:**
    *   Uso de `useEffect` para carregar dados iniciais.
    *   Feedback com `react-toastify` (Sucesso/Erro).

## 5. Exemplo de Código (Baseado em SGB)

### Backend - Controlador
```java
@RestController
@RequestMapping("backend/profissional/jornada")
class ProfissionalJornadaControlador {
    @Autowired private ProfissionalServicoAplicacao servicoAplicacao;
    @Autowired private BackendMapeador mapeador;

    @RequestMapping(method = GET)
    JornadaFormulario obter() {
        // Lógica para pegar ID do usuário logado
        var profissionalId = obterIdLogado(); 
        return servicoAplicacao.obterJornada(profissionalId);
    }

    @RequestMapping(method = POST, path = "salvar")
    void salvar(@RequestBody JornadaDto dto) {
        var comando = mapeador.map(dto, AtualizarJornadaComando.class);
        servicoAplicacao.atualizarJornada(comando);
    }
}
```

### Backend - Aplicação
```java
public class ProfissionalServicoAplicacao {
    private final ProfissionalRepositorio repositorio;
    private final ProfissionalServico servicoDominio;

    public void atualizarJornada(AtualizarJornadaComando comando) {
        Profissional p = repositorio.buscarPorId(comando.getProfissionalId());
        // Conversão e lógica de domínio
        servicoDominio.atualizarJornada(p, comando.getNovasJornadas());
    }
}
```

## 6. Próximos Passos
1.  Criar a tabela `jornada_trabalho` via migration/script.
2.  Atualizar a entidade `Profissional` para mapear `@OneToMany` com `JornadaTrabalho`.
3.  Implementar os DTOs e Serviços de Aplicação seguindo o padrão SGB.
4.  Criar o Frontend para consumo.
