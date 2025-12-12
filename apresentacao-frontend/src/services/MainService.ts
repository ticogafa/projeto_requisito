import { AGENDAMENTO_URLS, AUTHENTICATION_URLS, PRODUTO_URLS, SERVICO_OFERECIDO_URLS, URLS_PREFIX } from '@/constants/URLConstants';
import { JornadaDto } from '@/interfaces/JornadaInterface';
import { ProfissionaisResponse } from '@/interfaces/ProfissionaisInterfaces';
import HttpClient from '@/services/httpClient';
import type { AxiosError, AxiosResponse } from 'axios';
import { ServicoOferecido } from '@/interfaces/ServicoOferecidoInterface';

export default class MainService {
  client: HttpClient;
  static instance: MainService;

  private constructor() {
    this.client = new HttpClient(URLS_PREFIX.API);
  }

  public static getInstance(): MainService {
    if (!this.instance) {
      this.instance = new MainService();
    }
    return this.instance;
  }

  public getJornada(
    profissionalId: number,
    successCallback: (data: JornadaDto[]) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    console.warn('getJornada method not yet implemented in MainService.');
    // Placeholder for actual implementation
    this.client.get(
      `/jornada/${profissionalId}`,
      {},
      {},
      (response) => successCallback(response.data),
      errorCallback,
      finallyCallback
    );
  }

  public atualizarJornada(
    profissionalId: number,
    data: JornadaDto[],
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    console.warn('atualizarJornada method not yet implemented in MainService.');
    // Placeholder for actual implementation
    this.client.put(
      `/jornada/${profissionalId}`,
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  public listarServicos(
    successCallback: (data: ServicoOferecido[]) => void,
    errorCallback: (error: AxiosError) => void
  ): void {
    this.client.get(
      SERVICO_OFERECIDO_URLS.GET_ALL_SERVICOS_OFERECIDOS || '/servico',
      {},
      {},
      (response) => successCallback(response.data),
      errorCallback,
      () => {}
    );
  }

  public criarServico(
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ) {
    this.client.post('/servico', data, {}, successCallback, errorCallback, finallyCallback);
  }

  public atualizarServico(
    id: number,
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ) {
    this.client.put(`/servico/${id}`, data, {}, successCallback, errorCallback, finallyCallback);
  }

  public deletarServico(
    id: number,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ) {
    this.client.delete(`/servico/${id}`, {}, {}, successCallback, errorCallback, finallyCallback);
  }

  getServicosOferecidos(
    params: object,
    header: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.get(
      SERVICO_OFERECIDO_URLS.GET_ALL_SERVICOS_OFERECIDOS,
      params,
      header,
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  public criarCliente(
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ) {
    this.client.post(
      '/clientes',
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  public getClientePorEmail(
    email: string,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ) {
    this.client.get(
      '/clientes/buscar',
      { email },
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  public getProfissionais(
    params: object,
    headers: object,
    successCallback: (response: AxiosResponse<ProfissionaisResponse>) => void,
    errorCallback: (err: AxiosError) => void,
    finallyCallback: () => void
  ) {
    this.client.get(
      '/profissional',
      params,
      headers,
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  public criarProfissional(
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ) {
    this.client.post(
      '/profissional',
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  public atualizarProfissional(
    id: number,
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ) {
    this.client.put(
      `/profissional/${id}`,
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  public desativarProfissional(
    id: number,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ) {
    this.client.delete(
      `/profissional/${id}`,
      {},
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  /**
   * Gets professionals available.
   */
  getProfissionaisDisponiveis(
    params: object,
    header: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.get(
      AGENDAMENTO_URLS.PROFISSIONAIS_DISPONIVEIS,
      params,
      header,
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  /**
   * Creates a new appointment.
   */
  criarAgendamento(
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      AGENDAMENTO_URLS.CRIAR,
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  /**
   * Gets appointments by client ID.
   */
  getAgendamentosPorCliente(
    params: object,
    header: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.get(
      AGENDAMENTO_URLS.POR_CLIENTE,
      params,
      header,
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  /**
   * Gets appointments by professional ID.
   */
  getAgendamentosPorProfissional(
    params: object,
    header: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.get(
      AGENDAMENTO_URLS.POR_PROFISSIONAL,
      params,
      header,
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  /**
   * Edits an existing appointment.
   */
  editarAgendamento(
    agendamentoId: number,
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.put(
      `${AGENDAMENTO_URLS.EDITAR}/${agendamentoId}`,
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  cancelarAgendamento(
    agendamentoId: number,
    clienteId: number,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.delete(
      `${AGENDAMENTO_URLS.CANCELAR}/${agendamentoId}?clienteId=${clienteId}`,
      {},
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  cancelarAgendamentoPorProfissional(
    agendamentoId: number,
    profissionalId: number,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.delete(
      `${AGENDAMENTO_URLS.CANCELAR}/${agendamentoId}?profissionalId=${profissionalId}`,
      {},
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  registerUser(
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      AUTHENTICATION_URLS.REGISTER,
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  loginUser(
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      AUTHENTICATION_URLS.LOGIN,
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  getProdutos(
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.get(
      PRODUTO_URLS.GET_ALL,
      {},
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  getProdutosEstoqueBaixo(
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.get(
      PRODUTO_URLS.GET_ESTOQUE_BAIXO,
      {},
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  cadastrarProduto(
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      PRODUTO_URLS.CADASTRAR,
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  atualizarProduto(
    id: number,
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.put(
      `${PRODUTO_URLS.ATUALIZAR}/${id}`,
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  adicionarEstoque(
    id: number,
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      PRODUTO_URLS.ADICIONAR_ESTOQUE.replace(':id', id.toString()),
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  removerEstoque(
    id: number,
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      PRODUTO_URLS.REMOVER_ESTOQUE.replace(':id', id.toString()),
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  registrarVenda(
    id: number,
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      PRODUTO_URLS.REGISTRAR_VENDA.replace(':id', id.toString()),
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  getHistoricoMovimentacoes(
    id: number,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.get(
      PRODUTO_URLS.HISTORICO.replace(':id', id.toString()),
      {},
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  /**
   * Resets test data for appointments.
   */
  public resetarDadosTeste(
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      '/dev/seed-agendamentos',
      {},
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  public registrarAtendimento(
    data: { profissionalId: number; valor: number; inicio: string; fim: string },
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      '/atendimento/registrar',
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }

  public registrarAvaliacao(
    data: { profissionalId: number; nota: number },
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      '/avaliacao',
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }
}
