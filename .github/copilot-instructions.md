# Copilot Instructions - Sistema de Barbearia

## 🏗️ Architecture Overview

This is a **Clean Architecture** barbershop management system with:
- **Backend**: Java 21 + Spring Boot (multi-module Maven project in `barbearia-backend/`)
- **Frontend**: React 19 + TypeScript + Vite + Tailwind CSS (in `apresentacao-frontend/`)
- **Database**: MySQL 8.0 (runs in Docker container `barbearia-container`)
- **Auth**: Firebase Authentication (frontend)

### Module Structure

The backend follows **DDD principles** with strict layer separation:

```
barbearia-backend/
  dominio-principal/        # Main executable module
    dominio/
      principal/            # Business logic (Cliente, Agendamento, Profissional, Produto, ServicoOferecido)
      compartilhado/        # Shared domain (ValueObjects, Events, Exceptions)
    aplicacao/              # Application Services (orchestration layer)
    infraestrutura/         # JPA entities, repositories, proxies
    apresentacao/           # REST controllers
    config/                 # Spring configuration
  pai/                      # Parent POM with dependencies
```

**Critical Rule**: Domain Services (`*Servico.java` in `dominio/`) **MUST NOT** use Spring annotations. They are wired as `@Bean` in [config/DomainServicesConfig.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/config/DomainServicesConfig.java) to keep domain framework-agnostic (inspired by `sgb-2025-01` project).

## 🔑 Key Conventions

### Domain Layer

- **Entities**: Immutable business objects (no setters). Use constructors or factory methods.
- **Value Objects**: Extend `ValueObjectId<T>` for type-safe IDs. Example: `ProfissionalId`, `AgendamentoId`.
- **Services**: Plain Java classes with constructor injection. Example pattern:
  ```java
  public class AgendamentoServico {
      private final AgendamentoRepositorio repositorio;
      
      public AgendamentoServico(AgendamentoRepositorio repositorio) {
          this.repositorio = repositorio;
      }
  }
  ```

### Infrastructure Layer

- **JPA Entities**: Suffixed with `Jpa` (e.g., `ProdutoJpa`, `AgendamentoJpa`). Located in `infraestrutura/persistencia/jpa/`.
- **Mappers**: `JpaMapeador` converts between domain entities and JPA entities. Use it in repositories.
- **Proxies**: Cache and virtual proxies implemented for performance (see Design Patterns section below).

### Presentation Layer

- **Controllers**: Suffixed with `Controlador` (e.g., `AgendamentoControlador`). Use `@RestController`.
- **DTOs**: Request/Response objects in `aplicacao/` package (e.g., `EditarAgendamentoRequest`).
- **Mapper**: `BackendMapeador` converts between DTOs and domain objects.

### Frontend Conventions

- **Services**: Singleton pattern via `MainService.getInstance()`. All API calls go through this service.
- **Constants**: API URLs defined in [constants/URLConstants.ts](apresentacao-frontend/src/constants/URLConstants.ts).
- **Hooks**: Custom hooks for data fetching (`use*` pattern). Examples: `useProdutos`, `useCriarAgendamento`.
- **Auth**: Firebase auth context in [auth/AuthContext.tsx](apresentacao-frontend/src/auth/AuthContext.tsx). Use `useAuth()` hook.
- **State**: Zustand for global state (e.g., `useLoadingStore`).

## 🎨 Design Patterns Implemented

### Proxy Pattern (Cache + Virtual Proxy)

The project extensively uses the Proxy pattern for performance optimization:

- **Cache Proxy**: [ProdutoRepositorioVirtualProxy.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/infraestrutura/proxy/ProdutoRepositorioVirtualProxy.java)
  - Uses `ConcurrentHashMap` for thread-safe caching
  - Tracks hit/miss statistics accessible via `/api/cache/statistics`
  - Invalidates cache on write operations
  - Marked with `@Primary` for automatic DI

**Pattern Structure**: Interface (`ProdutoRepositorio`) → Real Subject (`ProdutoRepositorioJpa`) ← Proxy (`ProdutoRepositorioCacheProxy`)

See [padroes.md](padroes.md) and [README_PROXY.md](barbearia-backend/dominio-principal/README_PROXY.md) for complete documentation.

### Decorator Pattern

- **ValidadorSaldoDecorator**: Wraps `IGestaoCaixa` to add balance validation logic.

### Observer Pattern

- **Event Listeners**: Domain events (e.g., `ServicoOferecidoEvent`) published via Spring's `ApplicationEventPublisher`.
- Example: [NotificacaoProfissionalListener.java](barbearia-backend/dominio-principal/src/main/java/com/cesarschool/barbearia/aplicacao/profissional/listeners/NotificacaoProfissionalListener.java)

## 🚀 Development Workflows

### Starting the Project

Use [start_project.sh](start_project.sh) to start all services at once:
```bash
./start_project.sh
```

Or start manually:

1. **Database** (must be first):
   ```bash
   docker run --name barbearia-container -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=barbearia_db -p 3306:3306 -d mysql:8.0
   ```

2. **Backend** (from project root):
   ```bash
   cd barbearia-backend
   ./mvnw spring-boot:run -pl dominio-principal -DskipTests
   ```

3. **Frontend** (from project root):
   ```bash
   cd apresentacao-frontend
   npm install  # First time only
   npm run dev
   ```

### Testing

- **BDD Tests**: Cucumber features in `barbearia-backend/dominio-principal/src/test/resources/features/`
  ```bash
  cd barbearia-backend/dominio-principal
  mvn test
  ```

- **Demo Mode**: Run proxy pattern demonstration:
  ```bash
  mvn spring-boot:run -Dspring-boot.run.profiles=demo
  ```

### Building

```bash
# Backend (from barbearia-backend/)
./mvnw clean package -DskipTests

# Frontend (from apresentacao-frontend/)
npm run build
```

## 🔗 Integration Points

### Frontend ↔ Backend

- **Base URL**: Frontend uses `/api` prefix (proxied by Vite to `http://localhost:8080`)
- **Auth Flow**: Firebase token → sent in requests → validated by backend
- **Error Handling**: Axios interceptors in [AxiosInterceptor.ts](apresentacao-frontend/src/services/AxiosInterceptor.ts)

### Backend API Structure

Controllers expose RESTful endpoints:
- `/api/agendamentos/*` - Scheduling operations
- `/api/produtos/*` - Product/inventory management
- `/api/servico/*` - Services offered
- `/api/profissionais/*` - Professional management
- `/api/cache/*` - Cache monitoring (Proxy pattern metrics)

### Database Schema

Schema is auto-generated by JPA from entities in `infraestrutura/persistencia/jpa/`. Tables follow snake_case convention.

## 📝 Context Mapper

The domain model is documented using Context Mapper DSL in [DOCUMENTAÇÃO/CONTEXT MAPPER/CONTEXT.cml](DOCUMENTAÇÃO/CONTEXT MAPPER/CONTEXT.cml). This defines:
- Bounded contexts
- Aggregates (Cliente, Agendamento, Profissional, Produto, ServicoOferecido)
- Value Objects
- Repository interfaces
- Domain Services

**Important**: When adding new domain concepts, update the `.cml` file to maintain architectural documentation.

## 💡 Common Tasks

### Adding a new Domain Entity

1. Create entity in `dominio/principal/[aggregate-name]/`
2. Create JPA entity in `infraestrutura/persistencia/jpa/` (suffix: `Jpa`)
3. Add mapping logic to `JpaMapeador`
4. Create repository interface in domain layer
5. Implement repository in `infraestrutura/persistencia/`
6. Wire repository in `DomainServicesConfig` if needed
7. Update `CONTEXT.cml`

### Adding a new REST Endpoint

1. Create Request/Response DTOs in `aplicacao/[feature]/`
2. Add endpoint to Controller in `apresentacao/[feature]/` 
3. Add mapping logic to `BackendMapeador`
4. Frontend: Add URL constant to [URLConstants.ts](apresentacao-frontend/src/constants/URLConstants.ts)
5. Frontend: Add method to [MainService.ts](apresentacao-frontend/src/services/MainService.ts)
6. Frontend: Create hook in `hooks/` for the operation

### Implementing a new Design Pattern

Document it in [padroes.md](padroes.md) following the existing format:
- Description & objective
- Classes created/modified
- UML structure diagram
- Code examples
- How to test/demonstrate it
