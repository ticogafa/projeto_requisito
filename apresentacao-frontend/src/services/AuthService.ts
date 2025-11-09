import type { AxiosError, AxiosResponse } from 'axios';
import { AUTHENTICATION_URLS, URLS_PREFIX } from '../constants/URLConstants';
import HttpClient from './httpClient';


/**
 * An access layer to the Authentication Service.
 */
export default class AuthService {
  client: HttpClient;

  constructor() {
    this.client = new HttpClient(URLS_PREFIX.API);
  }

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