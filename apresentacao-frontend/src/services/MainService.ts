import type { AxiosError, AxiosResponse } from 'axios';
import { AUTHENTICATION_URLS, SERVICO_OFERECIDO_URLS, URLS_PREFIX } from '@/constants/URLConstants';
import HttpClient from '@/services/httpClient';

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
  //TODO IMplementar esse metodo
  /**
   * Signs in a user.
   *
   * @param data - The user's data
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
   */
  signInUser(
    data: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      AUTHENTICATION_URLS.GET_TOKEN,
      data,
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }
}
