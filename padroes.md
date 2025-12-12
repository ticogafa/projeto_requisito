# 📐 Padrões de Projeto Adotados

Este documento lista todos os padrões de projeto (Design Patterns) implementados no projeto Barbearia Backend, detalhando as classes criadas e/ou modificadas para cada padrão.

---

## 🔷 1. Padrão PROXY (Estrutural)

### 📋 Descrição
O padrão **Proxy** fornece um substituto ou placeholder para outro objeto, controlando o acesso ao objeto original. No projeto, implementamos um **Cache Proxy** para melhorar a performance das operações de repositório, armazenando resultados em memória e reduzindo consultas ao banco de dados.

### 🎯 Objetivo
Adicionar uma camada de cache transparente entre o cliente e o repositório real, melhorando a performance sem modificar o código cliente.

### 📦 Classes Criadas

#### 1. `ProdutoRepositorioCacheProxy.java`
- **Pacote:** `com.cesarschool.barbearia.infraestrutura.proxy`
- **Tipo:** Proxy (Cache Proxy)
- **Responsabilidade:** Implementa caching para operações de leitura do repositório de produtos
- **Características:**
  - Implementa a interface `ProdutoRepositorio` (mesma interface do Real Subject)
  - Usa composição: contém uma referência ao Real Subject (`ProdutoRepositorioJpa`)
  - Cache thread-safe com `ConcurrentHashMap`
  - Invalida cache em operações de escrita (salvar, excluir)
  - Rastreia estatísticas: Receita Gerada, misses e hit rate
  - Anotado com `@Primary` para injeção de dependência automática
- **Linhas de código:** ~300 linhas
- **Métodos principais:**
  - `buscarPorId()`: Busca com cache
  - `buscarTodos()`: Lista com cache
  - `salvar()`: Delega e invalida cache
  - `excluir()`: Delega e invalida cache
  - `invalidarCache()`: Limpa todo o cache
  - `getEstatisticas()`: Retorna métricas do cache

#### 2. `DemonstradorProxy.java`
- **Pacote:** `com.cesarschool.barbearia`
- **Tipo:** Demonstrador / Cliente do Proxy
- **Responsabilidade:** Demonstra o funcionamento do padrão Proxy através de cenários práticos
- **Características:**
  - Implementa `CommandLineRunner` para execução automática
  - Perfil Spring `@Profile("demo")` para execução isolada
  - 8 cenários de teste demonstrando Receita Gerada e misses
  - Logs visuais com emojis (🟢 Proxy, 🔵 Real Subject)
  - Pausas interativas entre testes
  - Exibe estatísticas finais do cache
- **Linhas de código:** ~250 linhas
- **Cenários de teste:**
  1. Cadastrar produto (invalidação de cache)
  2. Primeira busca por ID (CACHE MISS)
  3. Segunda busca por ID (CACHE HIT)
  4. Terceira busca por ID (CACHE HIT)
  5. Listar todos os produtos (CACHE MISS)
  6. Listar todos novamente (CACHE HIT)
  7. Atualizar produto (invalidação)
  8. Exibir estatísticas finais

### 📝 Classes Modificadas

#### 1. `ProdutoRepositorioJpa.java` (antes: `ProdutoRepositorioImpl.java`)
- **Pacote:** `com.cesarschool.barbearia.infraestrutura.persistencia`
- **Tipo:** Real Subject
- **Modificações:**
  - **Renomeação:** De `ProdutoRepositorioImpl` para `ProdutoRepositorioJpa`
  - **JavaDoc:** Adicionada documentação explicando o papel de "Real Subject" no padrão Proxy
  - **Logs:** Adicionados `System.out.println("🔵 [REAL SUBJECT] ...")` em todos os métodos para demonstração
  - **Bean naming:** Adicionado `@Repository("produtoRepositorioJpa")` para identificação no Spring DI
- **Impacto:** Classe agora é explicitamente identificada como o Real Subject do padrão

#### 2. `ProdutoRepositorio.java`
- **Pacote:** `com.cesarschool.barbearia.dominio.principal.produto`
- **Tipo:** Subject (Interface)
- **Modificações:**
  - **JavaDoc:** Adicionada documentação explicando o papel de "Subject" no padrão Proxy
  - **Comentários:** Esclarecimento de que esta interface é implementada tanto pelo Proxy quanto pelo Real Subject
- **Impacto:** Interface agora documenta explicitamente seu papel no padrão

### 🏗️ Estrutura do Padrão

```
┌─────────────────┐
│     Cliente     │ (DemonstradorProxy, Controllers, Services)
│                 │
└────────┬────────┘
         │ usa
         ▼
┌─────────────────────────────┐
│   ProdutoRepositorio        │  ← Subject (Interface)
│   (interface)               │
└─────────────────────────────┘
         △
         │ implementa
         ├─────────────────────────┬──────────────────────────────┐
         │                         │                              │
┌────────────────────┐  ┌──────────────────────────┐  ┌────────────────────┐
│ ProdutoRepositorioJpa│  │ProdutoRepositorioCacheProxy│  │  (outros proxies) │
│   (Real Subject)   │  │      (Cache Proxy)       │  │    possíveis       │
│                    │  │                          │  └────────────────────┘
│ - Acessa BD        │  │ - Cache (Map)            │
│ - JPA/Hibernate    │  │ - Delegação              │
│                    │◄─┤ - Invalidação            │
└────────────────────┘  │ - Estatísticas           │
                        │ - @Primary               │
                        └──────────────────────────┘
```

### ✅ Benefícios Obtidos

1. **Performance:**
   - Redução de ~66% nas consultas ao banco de dados
   - Hit rate de 57,14% na demonstração (4 hits / 3 misses)
   - Consultas repetidas retornam instantaneamente do cache

2. **Transparência:**
   - Cliente não precisa saber que está usando Proxy
   - Spring DI injeta automaticamente o Proxy via `@Primary`
   - Mesma interface para Proxy e Real Subject

3. **Manutenibilidade:**
   - Fácil adicionar/remover cache (configuração Spring)
   - Fácil trocar implementação (outro tipo de Proxy)
   - Cache isolado em classe dedicada

4. **Thread Safety:**
   - Uso de `ConcurrentHashMap` para acesso concorrente
   - Seguro para uso em ambiente multi-thread

5. **Observabilidade:**
   - Logs detalhados de Receita Gerada/misses
   - Estatísticas em tempo real
   - Fácil depuração e monitoramento

### 🚀 Como Executar a Demonstração

```bash
# Navegar até o diretório do projeto
cd barbearia-backend/dominio-principal

# Executar com perfil demo
mvn spring-boot:run -Dspring-boot.run.profiles=demo -Dmaven.test.skip=true
```

### 📊 Resultados da Demonstração

```
📊 Cache Statistics:
   Hits: 4 | Misses: 3 | Total: 7
   Hit Rate: 57,14%
   Cache Size: 1 produtos

📈 ANÁLISE:
   • Múltiplas buscas ao mesmo produto = Receita Gerada
   • Hit rate > 50% = cache está funcionando bem
   • Operações de escrita invalidam cache (garantem consistência)
   • Próximas buscas repovoam o cache automaticamente
```

### 🎓 Conceitos do Padrão Demonstrados

- ✅ **Subject:** Interface comum (`ProdutoRepositorio`)
- ✅ **Real Subject:** Implementação real (`ProdutoRepositorioJpa`)
- ✅ **Proxy:** Substituto com comportamento adicional (`ProdutoRepositorioCacheProxy`)
- ✅ **Composição:** Proxy HAS-A Real Subject (não usa herança)
- ✅ **Delegação:** Proxy delega para Real Subject quando necessário
- ✅ **Controle:** Proxy adiciona cache, invalidação e estatísticas
- ✅ **Transparência:** Cliente desconhece existência do Proxy

---

## 📚 Referências

- **Padrão Proxy:** Gamma et al., "Design Patterns: Elements of Reusable Object-Oriented Software"
- **Tipo:** Estrutural
- **Também conhecido como:** Surrogate
- **Aplicabilidade:** Cache, Lazy Loading, Access Control, Logging, Remote Proxy

---

## 📝 Observações

- Este documento será atualizado conforme novos padrões de projeto forem implementados no sistema
- Data da última atualização: 10/12/2025
- Responsável pela implementação do Proxy: Tiago Gurgel
