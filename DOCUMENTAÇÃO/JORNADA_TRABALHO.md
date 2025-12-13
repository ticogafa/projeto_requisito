# Sistema de Jornada de Trabalho - Documentação Técnica

## 📋 Sumário

- [Visão Geral](#visão-geral)
- [Arquitetura](#arquitetura)
- [Camada de Apresentação (Frontend)](#camada-de-apresentação-frontend)
- [Camada de Serviços HTTP](#camada-de-serviços-http)
- [Camada REST (Backend)](#camada-rest-backend)
- [Camada de Aplicação](#camada-de-aplicação)
- [Camada de Domínio](#camada-de-domínio)
- [Camada de Persistência](#camada-de-persistência)
- [Fluxo de Dados](#fluxo-de-dados)
- [Problemas Conhecidos](#problemas-conhecidos)
- [Próximos Passos](#próximos-passos)

---

## 🎯 Visão Geral

O sistema de **Jornada de Trabalho** permite que profissionais da barbearia configurem seus horários de trabalho semanais, incluindo:

- Horário de início e fim para cada dia da semana
- Intervalos/pausas durante o expediente
- Ativação/desativação de dias específicos
- Validação de horários e intervalos

### Objetivos

- ✅ Permitir gestão flexível de horários por profissional
- ✅ Validar conflitos de horários e intervalos
- ✅ Suportar dias inativos (folgas)
- ✅ Fornecer interface intuitiva para configuração

---

## 🏗️ Arquitetura

O sistema segue a **Clean Architecture** com separação clara de responsabilidades:

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (React)                          │
│  JornadaManager.tsx → MainService → Axios                   │
└─────────────────────────┬───────────────────────────────────┘
                          │ HTTP (GET/PUT)
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                 BACKEND (Spring Boot)                        │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │ REST: ProfissionalJornadaControlador               │    │
│  └──────────────────┬─────────────────────────────────┘    │
│                     │                                       │
│  ┌──────────────────▼─────────────────────────────────┐    │
│  │ APPLICATION: ProfissionalServicoAplicacao          │    │
│  │   - obterJornada()                                 │    │
│  │   - atualizarJornada()                             │    │
│  │   - validações de negócio                          │    │
│  └──────────────────┬─────────────────────────────────┘    │
│                     │                                       │
│  ┌──────────────────▼─────────────────────────────────┐    │
│  │ REPOSITORY: ProfissionalRepositorio                │    │
│  │   - listarJornadas()  ⚠️ VAZIO                     │    │
│  │   - atualizarJornadas() ⚠️ VAZIO                   │    │
│  └──────────────────┬─────────────────────────────────┘    │
│                     │                                       │
│  ┌──────────────────▼─────────────────────────────────┐    │
│  │ DOMAIN: HorarioTrabalho                            │    │
│  │   - Entity com validações                          │    │
│  │   - DiaSemana enum                                 │    │
│  └────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────┘
```

---

## 🎨 Camada de Apresentação (Frontend)

### Componente: `JornadaManager.tsx`

**Localização:** `apresentacao-frontend/src/components/Profissional/JornadaManager.tsx`

#### Responsabilidades

1. **Renderização da UI**: Grid com 7 dias da semana
2. **Gerenciamento de Estado**: Controla dados da jornada em `useState`
3. **Validação Imediata**: Valida campos a cada alteração
4. **Comunicação com API**: Carrega e salva jornadas via `MainService`

#### Estrutura de Estado

```typescript
interface JornadaDto {
  diaSemana: string;        // "SEGUNDA", "TERCA", etc.
  horaInicio: string;       // "08:00"
  horaFim: string;          // "18:00"
  intervaloInicio?: string; // "12:00" (opcional)
  intervaloFim?: string;    // "13:00" (opcional)
  ativo: boolean;           // true/false
}

const [jornadas, setJornadas] = useState<JornadaDto[]>([]);
const [errors, setErrors] = useState<{ [key: string]: string }>({});
```

#### Fluxo de Inicialização

```typescript
useEffect(() => {
  loadJornada(); // Carrega ao montar ou quando profissionalId muda
}, [profissionalId]);

const loadJornada = () => {
  MainService.getInstance().getJornada(
    profissionalId,
    (data: JornadaDto[]) => {
      // Merge com defaults para garantir todos os 7 dias
      const merged = DIAS_SEMANA.map(dia => {
        const existing = data.find(j => j.diaSemana === dia.value);
        return existing || { /* defaults */ };
      });
      setJornadas(merged);
    },
    // ... error handling
  );
};
```

#### Validações Implementadas

A validação ocorre **imediatamente** após cada alteração:

```typescript
const validateJornada = (currentJornadas: JornadaDto[]) => {
  const newErrors: { [key: string]: string } = {};
  
  currentJornadas.forEach((jornada) => {
    if (jornada.ativo) {
      // 1. Horários obrigatórios
      if (!jornada.horaInicio || !jornada.horaFim) {
        newErrors[jornada.diaSemana] = 'Horário de início e fim são obrigatórios';
      }
      
      // 2. Fim após início
      if (jornada.horaInicio >= jornada.horaFim) {
        newErrors[jornada.diaSemana] = 'Horário de fim deve ser depois do início';
      }
      
      // 3. Intervalo consistente
      if (jornada.intervaloInicio && jornada.intervaloFim) {
        if (jornada.intervaloInicio >= jornada.intervaloFim) {
          newErrors[jornada.diaSemana] = 'Início do intervalo antes do fim';
        }
        if (jornada.intervaloInicio < jornada.horaInicio || 
            jornada.intervaloFim > jornada.horaFim) {
          newErrors[jornada.diaSemana] = 'Intervalo dentro do horário de trabalho';
        }
      }
      
      // 4. Intervalo completo ou vazio
      if ((jornada.intervaloInicio || jornada.intervaloFim) && 
          !(jornada.intervaloInicio && jornada.intervaloFim)) {
        newErrors[jornada.diaSemana] = 'Ambos início e fim devem ser preenchidos';
      }
    }
  });
  
  setErrors(newErrors);
  return Object.keys(newErrors).length === 0;
};
```

#### Fluxo de Salvamento

```typescript
const handleSave = () => {
  if (!validateJornada(jornadas)) {
    toast.error('Corrija os erros antes de salvar');
    return;
  }

  const payload = jornadas.filter(j => j.ativo); // Apenas dias ativos

  MainService.getInstance().atualizarJornada(
    profissionalId,
    payload,
    () => toast.success('Jornada atualizada!'),
    (error) => toast.error('Erro ao atualizar'),
  );
};
```

#### UI Components

- **Checkbox**: Ativar/desativar dia
- **Time Inputs**: Horário início, fim, intervalo início, intervalo fim
- **Validation Feedback**: Mensagens de erro em vermelho
- **Save Button**: Com estado de loading

---

## 🌐 Camada de Serviços HTTP

### Service: `MainService`

**Localização:** `apresentacao-frontend/src/services/MainService.ts`

#### Singleton Pattern

```typescript
class MainService {
  private static instance: MainService;
  
  static getInstance(): MainService {
    if (!MainService.instance) {
      MainService.instance = new MainService();
    }
    return MainService.instance;
  }
}
```

#### Métodos de Jornada

##### 1. `getJornada()`

```typescript
public getJornada(
  profissionalId: number,
  onSuccess: (data: JornadaDto[]) => void,
  onError: (error: AxiosError) => void,
  onFinally?: () => void
): void {
  axios.get(`/jornada/${profissionalId}`)
    .then(response => onSuccess(response.data))
    .catch(error => onError(error))
    .finally(() => onFinally?.());
}
```

**⚠️ PROBLEMA:** Endpoint incorreto!
- Frontend chama: `/jornada/${profissionalId}`
- Backend espera: `/api/profissional/${profissionalId}/jornada`

##### 2. `atualizarJornada()`

```typescript
public atualizarJornada(
  profissionalId: number,
  jornadas: JornadaDto[],
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
  onFinally?: () => void
): void {
  axios.put(`/jornada/${profissionalId}`, jornadas)
    .then(() => onSuccess())
    .catch(error => onError(error))
    .finally(() => onFinally?.());
}
```

**⚠️ PROBLEMA:** Mesmo erro de endpoint!

#### Interceptors

Os requests passam por `AxiosInterceptor.ts` que:
- Adiciona token de autenticação
- Trata erros globalmente
- Faz proxy para `http://localhost:8080` (configurado no Vite)

---

## 🔌 Camada REST (Backend)

### Controller: `ProfissionalJornadaControlador`

**Localização:** `barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/apresentacao/profissional/ProfissionalJornadaControlador.java`

#### Endpoints

##### GET `/api/profissional/{id}/jornada`

```java
@GetMapping("/{id}/jornada")
public ResponseEntity<List<JornadaResumo>> obterJornada(@PathVariable Integer id) {
    try {
        List<JornadaResumo> jornadas = servicoAplicacao.obterJornada(id);
        return ResponseEntity.ok(jornadas);
    } catch (Exception e) {
        return exceptionHandler.handleException(e);
    }
}
```

**Função:** Retorna lista de jornadas de um profissional

##### PUT `/api/profissional/{id}/jornada`

```java
@PutMapping("/{id}/jornada")
public ResponseEntity<Void> atualizarJornada(
    @PathVariable Integer id,
    @RequestBody List<JornadaResumo> novasJornadas
) {
    try {
        AtualizarJornadaComando comando = new AtualizarJornadaComando(id, novasJornadas);
        servicoAplicacao.atualizarJornada(comando);
        return ResponseEntity.noContent().build();
    } catch (Exception e) {
        return exceptionHandler.handleException(e);
    }
}
```

**Função:** Atualiza jornadas de um profissional

#### Exception Handling (Strategy Pattern)

```java
@Autowired
private ExceptionHandler exceptionHandler;
```

O `ExceptionHandler` centraliza tratamento de exceções:
- `EntidadeNaoEncontradaException` → 404
- `ValidacaoException` → 400
- `Exception` genérica → 500

---

## ⚙️ Camada de Aplicação

### Service: `ProfissionalServicoAplicacao`

**Localização:** `barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/aplicacao/profissional/ProfissionalServicoAplicacao.java`

#### Método: `obterJornada()`

```java
public List<JornadaResumo> obterJornada(Integer profissionalId) {
    validarIdNaoNulo(profissionalId);
    return repositorio.listarJornadas(profissionalId);
}
```

**Delega diretamente ao repositório** (sem lógica adicional).

#### Método: `atualizarJornada()`

```java
public void atualizarJornada(AtualizarJornadaComando comando) {
    validarComandoNaoNulo(comando);
    validarIdNaoNulo(comando.getProfissionalId());
    validarJornadasNaoVazias(comando.getNovasJornadas());
    
    // Validação individual de cada jornada
    for (JornadaResumo jornada : comando.getNovasJornadas()) {
        validarHorarioTrabalho(jornada);
        if (jornada.getIntervaloInicio() != null || jornada.getIntervaloFim() != null) {
            validarIntervalo(jornada);
        }
    }
    
    repositorio.atualizarJornadas(
        comando.getProfissionalId(), 
        comando.getNovasJornadas()
    );
}
```

#### Validações de Negócio

##### 1. `validarHorarioTrabalho()`

```java
private void validarHorarioTrabalho(JornadaResumo jornada) {
    if (jornada.getHoraInicio() == null || jornada.getHoraFim() == null) {
        throw new ValidacaoException(
            "Horário de início e fim são obrigatórios"
        );
    }
    
    if (!jornada.getHoraInicio().isBefore(jornada.getHoraFim())) {
        throw new ValidacaoException(
            "Horário de fim deve ser posterior ao de início"
        );
    }
}
```

##### 2. `validarIntervalo()`

```java
private void validarIntervalo(JornadaResumo jornada) {
    LocalTime inicio = jornada.getIntervaloInicio();
    LocalTime fim = jornada.getIntervaloFim();
    
    if (inicio == null || fim == null) {
        throw new ValidacaoException(
            "Intervalo deve ter início e fim"
        );
    }
    
    if (!inicio.isBefore(fim)) {
        throw new ValidacaoException(
            "Início do intervalo deve ser antes do fim"
        );
    }
    
    if (inicio.isBefore(jornada.getHoraInicio()) || 
        fim.isAfter(jornada.getHoraFim())) {
        throw new ValidacaoException(
            "Intervalo deve estar dentro do horário de trabalho"
        );
    }
}
```

### DTOs e Commands

#### `JornadaResumo`

**Localização:** `aplicacao/profissional/dto/JornadaResumo.java`

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JornadaResumo {
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private LocalTime intervaloInicio;
    private LocalTime intervaloFim;
    private boolean ativo;
}
```

**Função:** DTO para transferência de dados entre camadas.

#### `AtualizarJornadaComando`

**Localização:** `aplicacao/profissional/commands/AtualizarJornadaComando.java`

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarJornadaComando {
    private Integer profissionalId;
    private List<JornadaResumo> novasJornadas;
}
```

**Função:** Command object para encapsular requisição de atualização.

---

## 🏛️ Camada de Domínio

### Entity: `HorarioTrabalho`

**Localização:** `dominio/principal/horariotrabalho/HorarioTrabalho.java`

#### Estrutura

```java
public class HorarioTrabalho {
    private final HorarioTrabalhoId id;
    private final ProfissionalId profissionalId;
    private final DiaSemana diaSemana;
    private final LocalTime horaInicio;
    private final LocalTime horaFim;
    private LocalTime inicioPausa;
    private LocalTime fimPausa;
    
    // Construtor obrigatório (entidade imutável)
    public HorarioTrabalho(
        ProfissionalId profissionalId,
        DiaSemana diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim
    ) {
        validarParametros(profissionalId, diaSemana, horaInicio, horaFim);
        this.id = new HorarioTrabalhoId(gerarNovoId());
        this.profissionalId = profissionalId;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }
}
```

**Princípios:**
- ✅ **Imutável**: Sem setters, apenas getters
- ✅ **Value Objects**: Usa `HorarioTrabalhoId`, `ProfissionalId`
- ✅ **Validação no Construtor**: Garantia de estado válido

#### Métodos de Pausa

```java
public void definirPausa(LocalTime inicio, LocalTime fim) {
    validarPausa(inicio, fim);
    this.inicioPausa = inicio;
    this.fimPausa = fim;
}

public void removerPausa() {
    this.inicioPausa = null;
    this.fimPausa = null;
}

private void validarPausa(LocalTime inicio, LocalTime fim) {
    if (inicio == null || fim == null) {
        throw new ValidacaoException("Pausa deve ter início e fim");
    }
    if (!inicio.isBefore(fim)) {
        throw new ValidacaoException("Início da pausa antes do fim");
    }
    if (inicio.isBefore(horaInicio) || fim.isAfter(horaFim)) {
        throw new ValidacaoException("Pausa dentro do horário de trabalho");
    }
}
```

### Enum: `DiaSemana`

**Localização:** `dominio/compartilhado/DiaSemana.java`

```java
public enum DiaSemana {
    SEGUNDA(DayOfWeek.MONDAY),
    TERCA(DayOfWeek.TUESDAY),
    QUARTA(DayOfWeek.WEDNESDAY),
    QUINTA(DayOfWeek.THURSDAY),
    SEXTA(DayOfWeek.FRIDAY),
    SABADO(DayOfWeek.SATURDAY),
    DOMINGO(DayOfWeek.SUNDAY);
    
    private final DayOfWeek dayOfWeek;
    
    DiaSemana(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    public static DiaSemana fromDayOfWeek(DayOfWeek dayOfWeek) {
        for (DiaSemana dia : values()) {
            if (dia.dayOfWeek == dayOfWeek) {
                return dia;
            }
        }
        throw new IllegalArgumentException("DayOfWeek inválido: " + dayOfWeek);
    }
    
    public DayOfWeek toDayOfWeek() {
        return dayOfWeek;
    }
}
```

**Função:** Wrapper para `java.time.DayOfWeek` mantendo nomenclatura em português.

---

## 💾 Camada de Persistência

### Interface: `ProfissionalRepositorio`

**Localização:** `dominio/principal/profissional/ProfissionalRepositorio.java`

```java
public interface ProfissionalRepositorio {
    // ... outros métodos
    
    List<JornadaResumo> listarJornadas(Integer profissionalId);
    void atualizarJornadas(Integer profissionalId, List<JornadaResumo> jornadas);
}
```

### Implementação: `ProfissionalJpaRepositorioImpl`

**Localização:** `infraestrutura/persistencia/ProfissionalJpa.java` (inner class)

#### ⚠️ PROBLEMA CRÍTICO: Implementação Vazia

```java
@Override
public List<JornadaResumo> listarJornadas(Integer profissionalId) {
    return new ArrayList<>();  // ❌ RETORNA VAZIO!
}

@Override
public void atualizarJornadas(Integer profissionalId, List<JornadaResumo> jornadas) {
    // ❌ NÃO FAZ NADA!
}
```

**Linha no código:** 258-264

**Impacto:**
- ❌ GET sempre retorna lista vazia
- ❌ PUT não persiste alterações
- ❌ Feature não funcional apesar da UI completa

### Interface: `HorarioTrabalhoRepositorio`

**Localização:** `dominio/principal/horariotrabalho/HorarioTrabalhoRepositorio.java`

```java
public interface HorarioTrabalhoRepositorio {
    List<HorarioTrabalho> buscarPorProfissional(ProfissionalId profissionalId);
    Optional<HorarioTrabalho> buscarPorProfissionalEDia(
        ProfissionalId profissionalId, 
        DiaSemana dia
    );
    List<HorarioTrabalho> listarAtivos(ProfissionalId profissionalId);
}
```

**⚠️ PROBLEMA:** Bean não registrado no Spring!
- DevController tenta `@Autowired` deste repositório
- Spring não encontra implementação
- Backend não inicia

---

## 🔄 Fluxo de Dados

### Fluxo Completo: Carregar Jornada (GET)

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. FRONTEND                                                     │
│    JornadaManager.useEffect() → loadJornada()                  │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 2. HTTP SERVICE                                                 │
│    MainService.getJornada(profissionalId)                       │
│    → axios.get(`/jornada/${profissionalId}`)                    │
└────────────────────────┬────────────────────────────────────────┘
                         │ ❌ Endpoint errado!
                         │ (deveria ser /api/profissional/{id}/jornada)
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 3. REST CONTROLLER                                              │
│    ProfissionalJornadaControlador.obterJornada(id)              │
│    → servicoAplicacao.obterJornada(id)                          │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 4. APPLICATION SERVICE                                          │
│    ProfissionalServicoAplicacao.obterJornada(id)                │
│    → repositorio.listarJornadas(id)                             │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 5. REPOSITORY                                                   │
│    ProfissionalJpaRepositorioImpl.listarJornadas(id)            │
│    → return new ArrayList<>();  ❌ VAZIO!                       │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 6. RETORNO                                                      │
│    [] (lista vazia) → Frontend cria defaults                   │
└─────────────────────────────────────────────────────────────────┘
```

### Fluxo Completo: Salvar Jornada (PUT)

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. FRONTEND                                                     │
│    JornadaManager.handleSave()                                  │
│    → validateJornada() ✅                                       │
│    → filter(j => j.ativo)                                       │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 2. HTTP SERVICE                                                 │
│    MainService.atualizarJornada(id, jornadas)                   │
│    → axios.put(`/jornada/${id}`, jornadas)                      │
└────────────────────────┬────────────────────────────────────────┘
                         │ ❌ Endpoint errado!
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 3. REST CONTROLLER                                              │
│    ProfissionalJornadaControlador.atualizarJornada(id, jornadas)│
│    → new AtualizarJornadaComando(id, jornadas)                  │
│    → servicoAplicacao.atualizarJornada(comando)                 │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 4. APPLICATION SERVICE                                          │
│    ProfissionalServicoAplicacao.atualizarJornada(comando)       │
│    → validarComandoNaoNulo() ✅                                 │
│    → validarIdNaoNulo() ✅                                      │
│    → validarJornadasNaoVazias() ✅                              │
│    → for each jornada:                                          │
│        - validarHorarioTrabalho() ✅                            │
│        - validarIntervalo() ✅                                  │
│    → repositorio.atualizarJornadas(id, jornadas)                │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 5. REPOSITORY                                                   │
│    ProfissionalJpaRepositorioImpl.atualizarJornadas(id, jornadas)│
│    → { /* VAZIO */ }  ❌ NÃO PERSISTE!                          │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│ 6. RETORNO                                                      │
│    204 No Content → Frontend mostra sucesso                    │
│    (mas nada foi salvo no banco!)                              │
└─────────────────────────────────────────────────────────────────┘
```

---

## ⚠️ Problemas Conhecidos

### 1. Backend Não Inicia

**Erro:**
```
Field horarioTrabalhoRepositorio in DevController required a bean 
of type 'HorarioTrabalhoRepositorio' that could not be found.
```

**Causa:** 
- `DevController` usa `@Autowired` em `HorarioTrabalhoRepositorio`
- Não existe implementação JPA registrada como `@Repository`

**Solução:**
- Criar `HorarioTrabalhoRepositorioJpa` em `infraestrutura/persistencia/`
- Ou remover dependência do `DevController` (seed data não precisa disso)

### 2. Endpoints Incompatíveis

**Frontend chama:**
- GET `/jornada/{profissionalId}`
- PUT `/jornada/{profissionalId}`

**Backend expõe:**
- GET `/api/profissional/{id}/jornada`
- PUT `/api/profissional/{id}/jornada`

**Solução:**
Atualizar `MainService.ts`:
```typescript
axios.get(`/api/profissional/${profissionalId}/jornada`)
axios.put(`/api/profissional/${profissionalId}/jornada`, jornadas)
```

### 3. Implementação do Repositório Vazia

**Localização:** `ProfissionalJpa.java` linhas 258-264

**Problema:**
```java
public List<JornadaResumo> listarJornadas(Integer profissionalId) {
    return new ArrayList<>();  // ❌
}

public void atualizarJornadas(Integer profissionalId, List<JornadaResumo> jornadas) {
    // ❌ Vazio
}
```

**Solução:** Implementar lógica de persistência usando JPA.

### 4. Falta Entidade JPA para HorarioTrabalho

**Problema:** Não existe `HorarioTrabalhoJpa` mapeada

**Solução:** Criar entidade JPA e mapeador:

```java
// infraestrutura/persistencia/jpa/HorarioTrabalhoJpa.java
@Entity
@Table(name = "horario_trabalho")
public class HorarioTrabalhoJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private ProfissionalJpa profissional;
    
    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;
    
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private LocalTime inicioPausa;
    private LocalTime fimPausa;
    private Boolean ativo;
    
    // getters/setters
}
```

---

## 📝 Próximos Passos

### Prioridade Alta

1. **Corrigir Startup do Backend**
   - [ ] Remover `@Autowired HorarioTrabalhoRepositorio` do `DevController`
   - [ ] Ou criar implementação JPA deste repositório

2. **Corrigir Endpoints no Frontend**
   - [ ] Atualizar `MainService.ts` com paths corretos
   - [ ] Atualizar `URLConstants.ts` se necessário

3. **Implementar Persistência**
   - [ ] Criar `HorarioTrabalhoJpa` entity
   - [ ] Criar `HorarioTrabalhoRepositorioJpa` implementation
   - [ ] Implementar `listarJornadas()` em `ProfissionalJpaRepositorioImpl`
   - [ ] Implementar `atualizarJornadas()` em `ProfissionalJpaRepositorioImpl`

### Prioridade Média

4. **Testes**
   - [ ] Testes unitários para validações
   - [ ] Testes de integração para repositório
   - [ ] Testes E2E para fluxo completo

5. **Melhorias**
   - [ ] Cache de jornadas (Proxy Pattern já implementado para outras entidades)
   - [ ] Histórico de alterações
   - [ ] Notificações quando profissional altera jornada

### Prioridade Baixa

6. **Otimizações**
   - [ ] Lazy loading de jornadas
   - [ ] Validação assíncrona no frontend
   - [ ] Debounce em campos de tempo

---

## 🔧 Exemplo de Implementação do Repositório

### `ProfissionalJpaRepositorioImpl.listarJornadas()`

```java
@Override
public List<JornadaResumo> listarJornadas(Integer profissionalId) {
    ProfissionalJpa profissional = profissionalRepository
        .findById(profissionalId)
        .orElseThrow(() -> new EntidadeNaoEncontradaException(
            "Profissional não encontrado"
        ));
    
    // Assumindo que HorarioTrabalhoJpa existe e tem relacionamento
    List<HorarioTrabalhoJpa> horarios = horarioTrabalhoRepository
        .findByProfissionalId(profissionalId);
    
    return horarios.stream()
        .map(h -> new JornadaResumo(
            h.getDiaSemana(),
            h.getHoraInicio(),
            h.getHoraFim(),
            h.getInicioPausa(),
            h.getFimPausa(),
            h.getAtivo()
        ))
        .collect(Collectors.toList());
}
```

### `ProfissionalJpaRepositorioImpl.atualizarJornadas()`

```java
@Override
public void atualizarJornadas(Integer profissionalId, List<JornadaResumo> jornadas) {
    ProfissionalJpa profissional = profissionalRepository
        .findById(profissionalId)
        .orElseThrow(() -> new EntidadeNaoEncontradaException(
            "Profissional não encontrado"
        ));
    
    // 1. Remover horários antigos
    horarioTrabalhoRepository.deleteByProfissionalId(profissionalId);
    
    // 2. Criar novos horários
    List<HorarioTrabalhoJpa> novosHorarios = jornadas.stream()
        .map(j -> {
            HorarioTrabalhoJpa h = new HorarioTrabalhoJpa();
            h.setProfissional(profissional);
            h.setDiaSemana(j.getDiaSemana());
            h.setHoraInicio(j.getHoraInicio());
            h.setHoraFim(j.getHoraFim());
            h.setInicioPausa(j.getIntervaloInicio());
            h.setFimPausa(j.getIntervaloFim());
            h.setAtivo(j.isAtivo());
            return h;
        })
        .collect(Collectors.toList());
    
    // 3. Salvar todos
    horarioTrabalhoRepository.saveAll(novosHorarios);
}
```

---

## 📚 Referências

### Arquivos Relevantes

**Frontend:**
- `apresentacao-frontend/src/components/Profissional/JornadaManager.tsx`
- `apresentacao-frontend/src/services/MainService.ts`
- `apresentacao-frontend/src/interfaces/JornadaInterface.ts`
- `apresentacao-frontend/src/constants/URLConstants.ts`

**Backend - Apresentação:**
- `barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/apresentacao/profissional/ProfissionalJornadaControlador.java`

**Backend - Aplicação:**
- `barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/aplicacao/profissional/ProfissionalServicoAplicacao.java`
- `barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/aplicacao/profissional/dto/JornadaResumo.java`
- `barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/aplicacao/profissional/commands/AtualizarJornadaComando.java`

**Backend - Domínio:**
- `barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/dominio/principal/horariotrabalho/HorarioTrabalho.java`
- `barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/dominio/principal/horariotrabalho/HorarioTrabalhoRepositorio.java`
- `barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/dominio/compartilhado/DiaSemana.java`

**Backend - Infraestrutura:**
- `barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/infraestrutura/persistencia/ProfissionalJpa.java` (linhas 258-264)

### Padrões Utilizados

- **Clean Architecture**: Separação estrita de camadas
- **Repository Pattern**: Abstração de persistência
- **DTO Pattern**: Transferência de dados entre camadas
- **Command Pattern**: Encapsulamento de requisições (AtualizarJornadaComando)
- **Strategy Pattern**: Tratamento de exceções (ExceptionHandler)
- **Singleton Pattern**: MainService no frontend
- **Value Object Pattern**: DiaSemana, HorarioTrabalhoId, ProfissionalId

---

## ✅ Checklist de Funcionalidades

### Implementadas

- ✅ UI completa para gerenciamento de jornada (7 dias)
- ✅ Validação frontend (imediata e pré-envio)
- ✅ Validação backend (regras de negócio)
- ✅ Endpoints REST definidos
- ✅ DTOs e Commands criados
- ✅ Entidade de domínio HorarioTrabalho
- ✅ Enum DiaSemana com conversão

### Não Implementadas

- ❌ Persistência JPA de jornadas
- ❌ Entidade JPA HorarioTrabalhoJpa
- ❌ Repository implementation funcional
- ❌ Bean HorarioTrabalhoRepositorio
- ❌ Endpoints frontend/backend sincronizados
- ❌ Testes automatizados

---

**Documento gerado em:** 2025-12-12  
**Versão:** 1.0  
**Autor:** Sistema de Documentação Automática
