import type { AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse, ResponseType } from 'axios';
import axios from 'axios';
// import AxiosInterceptor from './AxiosInterceptor';

export const HttpVerb = {
  POST: 'POST',
  GET: 'GET',
  PUT: 'PUT',
  DELETE: 'DELETE'
};

export default class HttpClient {
  client: AxiosInstance;

  /**
   * A common bridge to out-side services.
   *
   * @param baseURL - Base url used in all requests
   */
  constructor(baseURL: string) {
    this.client = axios.create({ baseURL });
    // AxiosInterceptor.addInterceptionBeforeRequest(this.client);
    // AxiosInterceptor.addInterceptionBeforeResponse(this.client);
  }

  /**
   * Post Method.
   *
   * @param path - Path of the request
   * @param data - The content of the request
   * @param headers - Header of the request
   * @param successCallback - Success response
   * @param errorCallback - Error response
   * @param finallyCallback - Final response
   */
  async post(
    path: string,
    data: object,
    headers: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): Promise<void> {
    const config: AxiosRequestConfig = {
      url: path,
      data,
      headers,
      method: HttpVerb.POST
    };
    await this.httpClientCallbacks(config, successCallback, errorCallback, finallyCallback);
  }

  /**
   * Get Method.
   *
   * @param path - Path of the request
   * @param params - Requests params object
   * @param headers - Header of the request
   * @param successCallback - Success response
   * @param errorCallback - Error response
   * @param finallyCallback - Final response
   * @param responseType - Response Type of the request
   */
  async get(
    path: string,
    params: object,
    headers: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void,
    responseType?: ResponseType | undefined
  ): Promise<void> {
    const config: AxiosRequestConfig = {
      url: path,
      params,
      headers,
      responseType,
      method: HttpVerb.GET
    };
    await this.httpClientCallbacks(config, successCallback, errorCallback, finallyCallback);
  }

  /**
   * Put Method.
   *
   * @param path - Path of the request
   * @param data - The content of the request
   * @param headers - Header of the request
   * @param successCallback - Success response
   * @param errorCallback - Error response
   * @param finallyCallback - Final response
   */
  async put(
    path: string,
    data: object,
    headers: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): Promise<void> {
    const config: AxiosRequestConfig = {
      url: path,
      data,
      headers,
      method: HttpVerb.PUT
    };
    await this.httpClientCallbacks(config, successCallback, errorCallback, finallyCallback);
  }

  /**
   * Delete Method.
   *
   * @param path - Path of the request
   * @param data - The content of the request
   * @param headers - Header of the request
   * @param successCallback - Success response
   * @param errorCallback - Error response
   * @param finallyCallback - Final response
   */
  async delete(
    path: string,
    data: object,
    headers: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): Promise<void> {
    const config: AxiosRequestConfig = {
      url: path,
      data,
      headers,
      method: HttpVerb.DELETE
    };
    await this.httpClientCallbacks(config, successCallback, errorCallback, finallyCallback);
  }

  async httpClientCallbacks(
    methodArgs: AxiosRequestConfig,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): Promise<void> {
    try {
      const response: AxiosResponse = await this.client(methodArgs);
      successCallback(response);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        errorCallback(error);
      }
    } finally {
      finallyCallback();
    }
  }

}
