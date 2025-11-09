import type { AxiosError, AxiosResponse } from "axios";
import { SERVICO_OFERECIDO_URLS, URLS_PREFIX } from "../constants/URLConstants";
import HttpClient from "./httpClient";


export default class MainService {
  client: HttpClient;

  constructor() {
    this.client = new HttpClient(URLS_PREFIX.API);
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
}
