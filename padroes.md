# 📐 Padrões de Projeto Adotados

Este documento lista todos os padrões de projeto (Design Patterns) implementados no projeto Barbearia Backend, detalhando as classes criadas e/ou modificadas para cada padrão.

---

## 🔷 1. Padrão PROXY (Estrutural)

### 📋 Descrição
O padrão **Proxy** fornece um substituto ou placeholder para outro objeto, controlando o acesso ao objeto original. No projeto, implementamos um **Virtual Proxy com Lazy Loading** para otimizar a performance das operações de repositório, adiando o carregamento de dados do banco de dados até que sejam realmente necessários.

### 🎯 Objetivo
Implementar Lazy Loading transparente entre o cliente e o repositório real, economizando recursos ao carregar dados SOB DEMANDA sem modificar o código cliente.

### 📦 Classes Criadas

#### 1. `ProdutoRepositorioVirtualProxy.java`
- **Pacote:** `com.cesarschool.barbearia.infraestrutura.proxy`
- **Tipo:** Virtual Proxy com Lazy Loading
- **Responsabilidade:** Adia o carregamento de dados até que sejam realmente necessários (lazy loading)
- **Características:**
  - Implementa a interface `ProdutoRepositorio` (mesma interface do Real Subject)
  - Usa composição: contém uma referência ao Real Subject (`ProdutoRepositorioJpa`)
  - **Lazy Initialization:** Carrega dados SOB DEMANDA
  - Cache thread-safe com `ConcurrentHashMap` para dados já carregados
  - Invalidação seletiva em operações de escrita (preserva outros produtos carregados)
  - Rastreia estatísticas: Lazy Loads vs Reuso
  - Anotado com `@Primary` para injeção de dependência automática
- **Linhas de código:** ~336 linhas
- **Métodos principais:**
  - `buscarPorId()`: Lazy loading com verificação de carregamento prévio
  - `buscarTodos()`: Lazy loading de lista completa
  - `buscarProdutosComEstoqueBaixo()`: Lazy loading de lista filtrada
  - `salvar()`: Delega e invalida seletivamente
  - `excluir()`: Delega e invalida seletivamente
  - `limparDadosCarregados()`: Limpa todos os dados carregados
  - `getEstatisticas()`: Retorna métricas de lazy loading

#### 2. `DemonstradorProxy.java`
- **Pacote:** `com.cesarschool.barbearia`
- **Tipo:** Demonstrador / Cliente do Proxy
- **Responsabilidade:** Demonstra o funcionamento do Virtual Proxy através de cenários práticos
- **Características:**
  - Implementa `CommandLineRunner` para execução automática
  - Perfil Spring `@Profile("demo")` para execução isolada
  - 8 cenários de teste demonstrando Lazy Loading vs Reuso
  - Logs visuais com emojis (🟣 Virtual Proxy, 🔵 Real Subject)
  - Pausas interativas entre testes
  - Exibe estatísticas finais (lazy loads vs reuso)
- **Linhas de código:** ~330 linhas
- **Cenários de teste:**
  1. Cadastrar produto (invalidação seletiva)
  2. Primeira busca por ID (LAZY LOAD - acessa BD)
  3. Segunda busca por ID (REUSO - já carregado)
  4. Terceira busca por ID (REUSO - já carregado)
  5. Listar todos os produtos (LAZY LOAD - acessa BD)
  6. Listar todos novamente (REUSO - já carregado)
  7. Atualizar produto (invalidação seletiva)
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
┌────────────────────┐  ┌──────────────────────────────┐  ┌────────────────────┐
│ ProdutoRepositorioJpa│  │ProdutoRepositorioVirtualProxy│  │  (outros proxies) │
│   (Real Subject)   │  │    (Virtual Proxy)           │  │    possíveis       │
│                    │  │                              │  └────────────────────┘
│ - Acessa BD        │  │ - Lazy Loading               │
│ - JPA/Hibernate    │  │ - Dados sob demanda          │
│                    │◄─┤ - Invalidação seletiva       │
└────────────────────┘  │ - Rastreamento (reuso/loads) │
                        │ - @Primary                   │
                        └──────────────────────────────┘
```

### ✅ Benefícios Obtidos

1. **Performance e Economia de Recursos:**
   - ⚡ Inicialização rápida (não carrega tudo de uma vez)
   - 💾 Economia de memória (só carrega o que é usado)
   - 🔄 Dados já carregados são reutilizados instantaneamente
   - 📉 Redução significativa de operações I/O no banco de dados

2. **Lazy Loading Efetivo:**
   - Dados carregados SOB DEMANDA (apenas quando necessário)
   - Primeira chamada: LAZY LOAD (acessa BD)
   - Chamadas subsequentes: REUSO (não acessa BD)
   - Invalidação seletiva preserva outros dados carregados

3. **Transparência:**
   - Cliente não precisa saber que está usando Virtual Proxy
   - Spring DI injeta automaticamente o Proxy via `@Primary`
   - Mesma interface para Proxy e Real Subject
   - Comportamento lazy transparente para o usuário

4. **Thread Safety:**
   - Uso de `ConcurrentHashMap` para acesso concorrente
   - Seguro para uso em ambiente multi-thread
   - Sincronização automática de dados carregados

5. **Observabilidade:**
   - Logs detalhados de Lazy Loads vs Reuso
   - Estatísticas em tempo real (lazy load count vs reuso count)
   - Fácil depuração e monitoramento do padrão
   - Métricas acessíveis via API REST

### 🚀 Como Executar a Demonstração

```bash
# Navegar até o diretório do projeto
cd barbearia-backend/dominio-principal

# Executar com perfil demo
mvn spring-boot:run -Dspring-boot.run.profiles=demo -Dmaven.test.skip=true
```Virtual Proxy Statistics:
   Lazy Loads: 3 | Reuso: 4 | Total: 7
   Reuso Rate: 57,14%
   Dados Carregados: 1 produto + 1 lista

📈 ANÁLISE:
   • Primeira busca = LAZY LOAD (carrega do BD)
   • Buscas subsequentes = REUSO (não acessa BD)
   • Reuso rate > 50% = lazy loading efetivo
   • Operações de escrita invalidam seletivamente
   • Dados preservados são reutilizados automaticamente
   • Economia de recursos: só carrega o necessário
📈 ANÁLISE:
   • Múltiplas buscas ao mesmo produto = Receita Gerada
   • Hit rate > 50% = cache está funcionando bem
   • Operações de escrita invalidam cache (garantem consistência)
   • PVirtual Proxy:** Substituto com Lazy Loading (`ProdutoRepositorioVirtualProxy`)
- ✅ **Composição:** Proxy HAS-A Real Subject (não usa herança)
- ✅ **Delegação:** Proxy delega para Real Subject APENAS quando necessário (lazy)
- ✅ **Lazy Initialization:** Dados carregados SOB DEMANDA
- ✅ **Controle:** Proxy adiciona lazy loading, invalidação seletiva e estatísticas
- ✅ **Transparência:** Cliente desconhece existência do Proxy
- ✅ **Economia de Recursos:** Evita carregar dados desnecessários
- ✅ **Subject:** Interface comum (`ProdutoRepositorio`)
- ✅ **Real Subject:** Implementação real (`ProdutoRepositorioJpa`)
- ✅ **Proxy:** Substituto com comportamento adicional (`ProdutoRepositorioCacheProxy`)
- ✅ **Composição:** Proxy HAS-A Real Subject (não usa herança)
- ✅ **Delegação:** Proxy delega para Real Subject quando necessário
- ✅ **Controle:** Proxy adiciona cache, invalidação e estatísticas
- ✅ Variante Implementada:** Virtual Proxy (Lazy Loading)
- **Outras Variantes:** Cache Proxy, Protection Proxy, Remote Proxy, Smart Reference

---

## 📝 Observações

- Este documento será atualizado conforme novos padrões de projeto forem implementados no sistema
- Data da última atualização: 12/12/2025
- Responsável pela implementação do Virtual Proxy: Tiago Gurgel
- **Nota Importante:** O proxy implementado é um **Virtual Proxy** (adiamento de carregamento), não um Cache Proxy tradicional. A distinção é importante pois Virtual Proxy foca em **lazy initialization** enquanto Cache Proxy foca em **reutilização de resultados já computados**.
- **Aplicabilidade:** Cache, Lazy Loading, Access Control, Logging, Remote Proxy

---

## 📝 Observações

- Este documento será atualizado conforme novos padrões de projeto forem implementados no sistema
- Data da última atualização: 10/12/2025
- Responsável pela implementação do Proxy: Tiago Gurgel
