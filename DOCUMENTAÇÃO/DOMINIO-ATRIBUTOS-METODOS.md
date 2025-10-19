## Domínio: Atributos e Métodos por Agregado (pacote)

Este documento lista, por pacote (agregado), os atributos das Entidades e os métodos públicos expostos por Entidades, Serviços e Repositórios do módulo `barbearia-backend/dominio-principal`.

Observações gerais:
- Repositórios específicos estendem um repositório base genérico (`Repositorio<T, ID>`). Além dos métodos listados por cada repositório específico, assume-se a presença dos métodos herdados: `salvar(T)`, `buscarPorId(ID)`, `listarTodos()`, `remover(ID)` (conforme uso no código).
- Onde houver `@Getter` do Lombok, os getters são gerados implicitamente (não listados individualmente, a menos que apareçam no código-fonte).

---

### Pacote: agendamento

Entidade: `Agendamento`
- Atributos:
  - `AgendamentoId id`
  - `LocalDateTime dataHora`
  - `StatusAgendamento status`
  - `ClienteId clienteId`
  - `ProfissionalId profissionalId`
  - `ServicoOferecidoId servicoId`
  - `String observacoes`
- Métodos públicos (negócio e mutação):
  - `confirmar()`
  - `cancelar()`
  - `setId(AgendamentoId id)`
  - `setDataHora(LocalDateTime dataHora)`
  - `setStatus(StatusAgendamento status)`
  - `setCliente(ClienteId clienteId)`
  - `setProfissional(ProfissionalId profissionalId)`
  - `setServico(ServicoOferecidoId servicoId)`
  - `setObservacoes(String observacoes)`
  - Getters gerados por Lombok (`@Getter`).

Serviço: `AgendamentoServico`
- Métodos públicos:
  - `Agendamento criar(Agendamento agendamento, int duracaoServicoMinutos)`
  - `Agendamento buscarPorId(AgendamentoId id)`
  - `Agendamento confirmar(AgendamentoId id)`
  - `Agendamento cancelar(AgendamentoId id)`
  - `List<Agendamento> listarPorCliente(ClienteId clienteId)`
  - `List<Agendamento> listarPorProfissional(ProfissionalId profissionalId)`
  - `List<Agendamento> listarPorStatus(StatusAgendamento status)`
  - `List<Agendamento> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim)`
  - `List<Agendamento> listarTodos()`

Repositório: `AgendamentoRepositorio`
- Métodos públicos específicos:
  - `List<Agendamento> buscarPorCliente(ClienteId clienteId)`
  - `List<Agendamento> buscarPorProfissional(ProfissionalId profissionalId)`
  - `List<Agendamento> buscarPorStatus(StatusAgendamento status)`
  - `List<Agendamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim)`
  - `boolean existeAgendamentoNoPeriodo(ProfissionalId profissionalId, LocalDateTime dataHora, int duracaoMinutos)`

---

### Pacote: profissional

Entidade: `Profissional`
- Atributos:
  - `ProfissionalId id`
  - `String nome`
  - `Email email`
  - `Cpf cpf`
  - `Telefone telefone`
  - `Agenda agenda`
  - `Senioridade senioridade`
  - `boolean ativo`
  - `String motivoInatividade`
- Métodos públicos:
  - `setId(ProfissionalId id)`
  - `setNome(String nome)`
  - `setEmail(Email email)`
  - `setCpf(Cpf cpf)`
  - `setTelefone(Telefone telefone)`
  - `setSenioridade(Senioridade senioridade)`
  - `setAtivo(boolean ativo)`
  - `setMotivoInatividade(String motivoInatividade)`
  - `atualizarContato(Email novoEmail, Telefone novoTelefone)`
  - `desativar(String motivo)`
  - `ProfissionalId getId()`
  - `String getNome()`
  - `Email getEmail()`
  - `Cpf getCpf()`
  - `Telefone getTelefone()`
  - `Agenda getAgenda()`
  - `Senioridade getSenioridade()`
  - `boolean isAtivo()`
  - `String getMotivoInatividade()`

Serviço: `ProfissionalServico`
- Métodos públicos:
  - `Profissional registrarNovo(Profissional profissional)`
  - `Profissional registrarNovo(Profissional profissional, Senioridade senioridade)`
  - `Profissional buscarPorId(ProfissionalId id)`
  - `Profissional buscarPorCpf(Cpf cpf)`
  - `List<Profissional> listarTodos()`
  - `Profissional atualizar(Profissional profissional)`
  - `void remover(ProfissionalId id)`
  - `Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoServicoMinutos)`
  - `Profissional desativar(ProfissionalId id, String motivo)`

Repositório: `ProfissionalRepositorio`
- Métodos públicos específicos:
  - `Profissional buscarPorCpf(Cpf cpf)`
  - `boolean existePorCpf(Cpf cpf)`
  - `Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoServicoMinutos)`

---

### Pacote: servico

Entidade: `ServicoOferecido`
- Atributos:
  - `Integer pontosFidelidade`
  - `ServicoOferecidoId id`
  - `ProfissionalId profissionalId`
  - `String nome`
  - `BigDecimal preco`
  - `String descricao`
  - `Integer duracaoMinutos`
  - `ServicoOferecidoId servicoPrincipalId`
  - `Integer intervaloLimpezaMinutos`
  - `boolean ativo`
  - `String motivoInatividade`
- Métodos públicos:
  - `setId(ServicoOferecidoId id)`
  - `setProfissionalId(ProfissionalId profissionalId)`
  - `setNome(String nome)`
  - `setPreco(BigDecimal preco)`
  - `setDescricao(String descricao)`
  - `setPontosFidelidade(Integer pontos)`
  - `setDuracaoMinutos(Integer duracaoMinutos)`
  - `setServicoPrincipalId(ServicoOferecidoId servicoPrincipalId)`
  - `setIntervaloLimpezaMinutos(Integer intervaloMinutos)`
  - `setAtivo(boolean ativo)`
  - `setMotivoInatividade(String motivoInatividade)`
  - `atualizarPreco(BigDecimal novoPreco)`
  - `definirComoAddonDe(ServicoOferecidoId principalId)`
  - `atualizarDuracao(Integer novaDuracao)`
  - `definirIntervaloLimpeza(Integer intervaloMinutos)`
  - `desativar(String motivo)`
  - `reativar()`
  - Getters: `getId()`, `getProfissionalId()`, `getNome()`, `getPreco()`, `getDescricao()`, `getDuracaoMinutos()`, `getServicoPrincipalId()`, `getIntervaloLimpezaMinutos()`, `isAtivo()`, `getMotivoInatividade()`

Serviço: `ServicoOferecidoServico`
- Métodos públicos:
  - `ServicoOferecido buscarPorId(Integer id)`
  - `ServicoOferecido registrar(ServicoOferecido servico)`
  - `void associarProfissional(String nomeServico, String nomeProfissional)`
  - `ServicoOferecido definirAddOn(ServicoOferecidoId addOnId, ServicoOferecidoId principalId)`
  - `ServicoOferecido definirIntervaloLimpeza(Integer id, int intervaloMinutos)`
  - `boolean podeSerAgendadoSozinho(ServicoOferecido servico)`
  - `List<ServicoOferecido> buscarPorProfissional(ProfissionalId profissionalId)`
  - `List<ServicoOferecido> listarTodos()`
  - `ServicoOferecido desativarServico(Integer id, String motivo)`
  - `List<ServicoOferecido> listarServicosAtivos()`
  - `ServicoOferecido atualizar(Integer id, ServicoOferecido servico)`
  - `ServicoOferecido atualizarPreco(Integer id, BigDecimal novoPreco)`
  - `ServicoOferecido atualizarDuracao(Integer id, Integer novaDuracao)`
  - `void remover(Integer id)`

Repositório: `ServicoOferecidoRepositorio`
- Métodos públicos específicos:
  - `List<ServicoOferecido> buscarPorProfissional(ProfissionalId profissionalId)`
  - `ServicoOferecido buscarPorNome(String nome)`
  - `List<ServicoOferecido> buscarAddOnDoServicoPrincipal(ServicoOferecidoId servicoPrincipalId)`
  - `void salvarAssociacao(String nomeServico, String nomeProfissional)`
  - `boolean estaQualificado(String nomeServico, String nomeProfissional)`

---

### Pacote: produto

Entidade: `Produto`
- Atributos:
  - `Integer id`
  - `String nome`
  - `int estoque`
  - `BigDecimal preco`
  - `int estoqueMinimo`
- Métodos públicos:
  - `setId(Integer id)`
  - `setNome(String nome)`
  - `setEstoque(int estoque)`
  - `setPreco(BigDecimal preco)`
  - `setEstoqueMinimo(int estoqueMinimo)`
  - Getters: `getId()`, `getNome()`, `getEstoque()`, `getPreco()`, `getEstoqueMinimo()`

Serviço: `ProdutoServico`
- Métodos públicos:
  - `Produto salvar(Produto produto)`
  - `Produto buscarPorId(ProdutoId id)`
  - `List<Produto> listarTodos()`
  - `List<Produto> listarComEstoqueBaixo()`
  - `Produto atualizar(ProdutoId id, Produto produto)`
  - `Produto aumentarEstoque(ProdutoId produtoId, int quantidade)`
  - `Produto baixaEstoque(ProdutoId produtoId, int quantidade)`
  - `void remover(ProdutoId id)`

Repositório: `ProdutoRepositorio`
- Métodos públicos específicos:
  - `List<Produto> findProdutosComEstoqueBaixo()`
  - `List<Produto> listarProdutosComEstoqueBaixo()`

---

### Pacote: itemvenda

Entidade: `ItemVenda`
- Atributos:
  - `ItemVendaId id`
  - `VendaId venda`
  - `ProdutoId produto` (opcional)
  - `ServicoOferecidoId servicoId` (opcional)
  - `String descricao`
  - `int quantidade`
  - `BigDecimal precoUnitario`
  - `BigDecimal precoTotal`
  - `TipoVenda tipo`
- Métodos públicos:
  - `setId(ItemVendaId id)`
  - `setVenda(VendaId venda)`
  - `setProdutoId(ProdutoId produto)`
  - `setServicoId(ServicoOferecidoId servicoId)`
  - `setDescricao(String descricao)`
  - `setQuantidade(int quantidade)` (recalcula `precoTotal`)
  - `setPrecoUnitario(BigDecimal precoUnitario)` (recalcula `precoTotal`)
  - `setTipo(TipoVenda tipo)`
  - Getters: `getId()`, `getVenda()`, `getProdutoId()`, `getServicoId()`, `getDescricao()`, `getQuantidade()`, `getPrecoUnitario()`, `getPrecoTotal()`, `getTipo()`

Serviço: `ItemVendaServico`
- Métodos públicos:
  - `ItemVenda registrar(ItemVenda itemVenda)`
  - `ItemVenda buscarPorId(ItemVendaId id)`
  - `List<ItemVenda> listarTodos()`
  - `ItemVenda atualizar(ItemVendaId id, ItemVenda itemVenda)`
  - `BigDecimal calcularTotalItens(List<ItemVenda> itens)`
  - `void remover(ItemVendaId id)`

Repositório: `ItemVendaRepositorio`
- Métodos públicos específicos: (nenhum adicional além dos herdados)

---

### Pacote: venda

Entidade: `Venda`
- Atributos:
  - `VendaId id`
  - `ClienteId clienteId`
  - `LocalDateTime dataVenda`
  - `BigDecimal valorTotal`
  - `List<ItemVenda> itens`
  - `Voucher voucher`
  - `String observacoes`
- Métodos públicos:
  - `setId(VendaId id)`
  - `setClienteId(ClienteId clienteId)`
  - `setDataVenda(LocalDateTime dataVenda)`
  - `setValorTotal(BigDecimal valorTotal)`
  - `setItens(List<ItemVenda> itens)` (recalcula `valorTotal`)
  - `setVoucher(Voucher voucher)` (recalcula `valorTotal`)
  - `setObservacoes(String observacoes)`
  - Getters: `getId()`, `getClienteId()`, `getDataVenda()`, `getValorTotal()`, `getItens()`, `getVoucher()`, `getObservacoes()`

Serviço: `VendaServico`
- Métodos públicos:
  - `Venda registrarVendaPDV(Venda venda)`
  - `Venda buscarPorId(VendaId id)`
  - `List<Venda> listarTodas()`
  - `void remover(VendaId id)`

Repositório: `VendaRepositorio`
- Métodos públicos específicos: (nenhum adicional além dos herdados)

---

### Pacote: pagamento

Entidade: `Pagamento`
- Atributos:
  - `PagamentoId id`
  - `MeioPagamento meioPagamento`
- Métodos públicos:
  - `setId(PagamentoId id)`
  - `setMeioPagamento(MeioPagamento meioPagamento)`
  - Getters: `getId()`, `getMeioPagamento()`

Serviço: `PagamentoServico`
- Métodos públicos:
  - `Pagamento registrar(Pagamento pagamento)`
  - `Pagamento salvar(Pagamento pagamento)`
  - `Pagamento buscarPorId(PagamentoId id)`
  - `List<Pagamento> listarTodos()`
  - `Pagamento atualizar(PagamentoId id, Pagamento pagamento)`
  - `void remover(PagamentoId id)`

Repositório: `PagamentoRepositorio`
- Métodos públicos específicos: (nenhum adicional além dos herdados)

---

### Pacote: cliente

Entidade: `Cliente`
- Atributos:
  - `ClienteId id`
  - `String nome`
  - `Email email`
  - `Cpf cpf`
  - `Telefone telefone`
  - `int pontos`
- Métodos públicos:
  - `adicionarPontos(int pontosGanhos)`
  - `usarPontos(int pontosGastos)`
  - `possuiPontosSuficientes(int pontosNecessarios)`
  - `setNome(String nome)`
  - `setEmail(Email email)`
  - `setCpf(Cpf cpf)`
  - `setId(ClienteId id)`
  - `setTelefone(Telefone telefone)`
  - `setPontos(int pontos)`
  - Getters: `getNome()`, `getEmail()`, `getCpf()`, `getTelefone()`, `getPontos()`, `getId()`

Serviço: `ClienteServico`
- Métodos públicos:
  - `Cliente criarCliente(Cliente request)`
  - `Cliente buscarPorId(ClienteId id)`
  - `Cliente atualizarCliente(ClienteId id, Cliente request)`
  - `List<Cliente> listarClientes()`
  - `Cliente adicionarPontos(ClienteId id, int pontos)`
  - `Cliente usarPontos(ClienteId id, int pontos)`
  - `void deletarCliente(ClienteId id)`

Repositório: `ClienteRepositorio`
- Métodos públicos específicos:
  - `Optional<Cliente> buscarPorNome(String nome)`
  - `Optional<Cliente> buscarPorEmail(String email)`
  - `Optional<Cliente> buscarPorTelefone(String telefone)`
  - `Optional<Cliente> buscarPorCpf(String cpf)`

---

### Pacote: cliente.caixa

Entidade: `Lancamento`
- Atributos:
  - `LancamentoId id`
  - `ClienteId clienteId` (opcional para dívidas)
  - `StatusLancamento status`
  - `String descricao`
  - `double valor`
  - `LocalDateTime quando`
- Métodos públicos:
  - `static Lancamento novaEntrada(String descricao, double valor)`
  - `static Lancamento novoGasto(String descricao, double valor)`
  - `static Lancamento novaDivida(ClienteId clienteId, String descricao, double valor)`
  - `void quitar()`
  - Getters: `getId()`, `getClienteId()`, `getStatus()`, `getDescricao()`, `getValor()`, `getQuando()`

Serviço: `GestaoCaixaServico`
- Métodos públicos:
  - `void registrarEntrada(String descricao, double valor)`
  - `void registrarSaida(String descricao, double valor)`
  - `void registrarDivida(ClienteId clienteId, String descricao, double valor)`
  - `void pagarPrimeiraDivida(ClienteId clienteId, double valorPago)`
  - `double saldoAtual()`

Repositório: `LancamentoRepositorio`
- Métodos públicos específicos:
  - `void salvar(Lancamento lancamento)`
  - `Optional<Lancamento> buscarPorId(LancamentoId id)`
  - `List<Lancamento> buscarTodos()`
  - `List<Lancamento> buscarPendentesPorCliente(ClienteId clienteId)`

---

### Pacote: voucher

Entidade: `Voucher`
- Atributos:
  - `Integer id`
  - `ClienteId cliente`
  - `BigDecimal valorDesconto`
  - `StatusVoucher status`
  - `LocalDateTime expiraEm`
- Métodos públicos:
  - `setId(Integer id)`
  - `setCliente(ClienteId cliente)`
  - `setValorDesconto(BigDecimal valorDesconto)`
  - `setStatus(StatusVoucher status)`
  - `setExpiraEm(LocalDateTime expiraEm)`
  - Getters: `getId()`, `getCliente()`, `getValorDesconto()`, `getStatus()`, `getExpiraEm()`

Repositório: `VoucherRepositorio`
- Métodos públicos específicos: (nenhum adicional além dos herdados)

---

### Pacote: fidelidade

Entidade: `Pontuacao` — sem atributos e métodos públicos definidos no código atual.

Serviço: `FidelidadeServico` — sem métodos públicos definidos no código atual.

Repositório: `PontuacaoRepositorio` — sem métodos públicos definidos no código atual.
