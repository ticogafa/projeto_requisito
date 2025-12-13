# Padrão Proxy - Virtual Proxy com Lazy Loading

## Resumo
O projeto usa um Virtual Proxy para `ProdutoRepositorio`, focado em lazy loading (adiamento de leitura) para operações de estoque. O proxy mantém o contrato do repositório, carrega dados apenas quando acessados e invalida seletivamente após escritas.

## Estrutura
```
Cliente (GestaoEstoqueServico, controllers, DemonstradorProxy)
          |
          v
ProdutoRepositorio (Subject)
    |                    
    |-- ProdutoRepositorioVirtualProxy (Virtual Proxy, @Primary)
    |       - Lazy load (produtos individuais, lista completa, lista estoque baixo)
    |       - Cache em ConcurrentHashMap
    |       - Invalidação seletiva e métricas
    |
    └-- ProdutoRepositorioJpa (Real Subject)
            - Acesso direto ao banco via Spring Data JPA
```

## Componentes
- Subject: `dominio/principal/produto/ProdutoRepositorio.java` (contrato comum; JavaDoc atualizado para citar o Virtual Proxy).
- Real Subject: `infraestrutura/persistencia/jpa/ProdutoRepositorioJpa` (acessa BD, logs de acesso).
- Virtual Proxy: `infraestrutura/proxy/ProdutoRepositorioVirtualProxy` (lazy loading, invalidação seletiva, métricas, `@Primary`).
- Cliente principal: `dominio/principal/produto/estoque/GestaoEstoqueServico` (operações de estoque usando o contrato do repositório; se beneficia do lazy load).
- Observabilidade: `apresentacao/proxy/ProxyMetricasControlador` (estatísticas e limpeza de cache via HTTP).
- Demonstração: `com.cesarschool.barbearia.DemonstradorProxy` (profile `demo`).

## Fluxo do Virtual Proxy
1. Cliente usa `ProdutoRepositorio` sem conhecer o proxy.
2. Proxy verifica se o dado já está carregado; se não, delega ao Real Subject e armazena (lazy load).
3. Operações de escrita (`salvar`, `remover`) invalidam apenas o produto alterado e as listas dependentes.
4. Métricas de reuso vs lazy load ficam disponíveis para depuração/monitoramento.

## Endpoints de observabilidade
- GET `/api/proxy/statistics` → métricas em JSON.
- GET `/api/proxy/statistics/text` → métricas em texto legível.
- DELETE `/api/proxy/cache` → limpa dados lazy-loaded.
- DELETE `/api/proxy/statistics` → reseta contadores.

## Demonstração
```
cd barbearia-backend/dominio-principal
mvn spring-boot:run -Dspring-boot.run.profiles=demo -Dmaven.test.skip=true
```

## Por que trocar o cache proxy anterior?
- O antigo `ProdutoRepositorioCacheProxy` era focado em cache preemptivo; foi removido da documentação porque a implementação real agora é um Virtual Proxy voltado a lazy loading e invalidação seletiva.
- A interface e os clientes permanecem inalterados; apenas o comportamento interno mudou para priorizar economia de I/O.

## Checklist rápido
- [x] Subject documentado
- [x] Real Subject com `@Repository("produtoRepositorioJpa")`
- [x] Virtual Proxy com `@Primary`, lazy loading e métricas
- [x] Endpoints de métricas disponíveis
- [x] Cliente de estoque funcionando sobre o proxy
Versão 3.0 - Implementação do Padrão Proxy com Cache

---

**✅ Implementação completa e funcional!**

Para dúvidas, consulte:
- `PROXY_GUIDE_TIAGO.md` - Guia completo
- `RESUMO_PROXY_TIAGO.md` - Resumo executivo
