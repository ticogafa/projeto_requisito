import { AGENDAMENTO_URLS, AUTHENTICATION_URLS, SERVICO_OFERECIDO_URLS, URLS_PREFIX } from '@/constants/URLConstants';
import HttpClient from '@/services/httpClient';
import { ProfissionaisResponse } from '@/interfaces/ProfissionaisInterfaces';
import type { AxiosError, AxiosResponse } from 'axios';

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

  /**
  * Gets all works.
  */
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
}
