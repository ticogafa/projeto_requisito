import { AGENDAMENTO_URLS, AUTHENTICATION_URLS, SERVICO_OFERECIDO_URLS, URLS_PREFIX } from '@/constants/URLConstants';
import HttpClient from '@/services/httpClient';
import type { AxiosError, AxiosResponse } from 'axios';

export default class MainService {
  client: HttpClient;
  static instance: MainService;

  private constructor() {
    this.client = new HttpClient(URLS_PREFIX.API);
  }

  public static getInstance(): MainService {
    return !this.instance ? new MainService() : this.instance;
  }

  /**
  * Gets all works.
  *
  * @param params - Requests params object
  * @param header - Axios header object
  * @param successCallback - Success callback function
  * @param errorCallback - Error callback function
  * @param finallyCallback - Finally callback function
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
   * Gets professionals available for a service at a specific date/time.
   *
   * @param params - Request params object with servicoId and dataHora
   * @param header - Axios header object
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
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
   *
   * @param data - The appointment data
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
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
   *
   * @param params - Request params object with clienteId
   * @param header - Axios header object
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
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
   * Edits an existing appointment.
   *
   * @param agendamentoId - The appointment ID
   * @param data - The updated appointment data
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
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

  /**
   * Cancels an appointment.
   *
   * @param agendamentoId - The appointment ID
   * @param clienteId - The client ID requesting cancellation
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
   */
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

  /**
   * Registers a new user in the backend.
   *
   * @param data - User registration data (email, password, role)
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
   */
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

  /**
   * Authenticates user and gets JWT token.
   *
   * @param data - Login data (email, password)
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
   */
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

}
