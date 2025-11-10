import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import axios, { HttpStatusCode } from 'axios';
import { AUTHENTICATION_URLS, URLS_PREFIX, URLS_TO_BE_IGNORED } from '@/constants/URLConstants';
import AuthStorage from '@/services/AuthStorage';

/**
 * Handles token expiration by refreshing them and manages request's header.
 */
export default class AxiosInterceptor {
  static hasDoneLogout: boolean = false;
  static refreshToken: null | Promise<AxiosResponse> = null;

  /**
   * Adds an interceptor to run before the request is made.
   */
  static addInterceptionBeforeRequest(axiosInstance: AxiosInstance): void {
    axiosInstance.interceptors.request.use(async request => {
      if (request.url && !URLS_TO_BE_IGNORED.includes(request.url)) {
        this._addAuthenticationHeaderOnRequest(request);
      }
      return request;
    }, error => {
      return Promise.reject(error);
    }
    );
  }

  /**
   * Adds an interceptor to run before the response is made.
   */
  static addInterceptionBeforeResponse(axiosInstance: AxiosInstance): void {
    axiosInstance.interceptors.response.use(response => {
      return response;
    }, async error => {
      let value: Promise<never> = Promise.reject(error);
      const config = error.config;

      if (!URLS_TO_BE_IGNORED.includes(config.url)) {
        if (error.response.status === HttpStatusCode.Unauthorized && !config._retry) {
          config._retry = true;
          try {
            await this._tryRefreshToken();
            value = axiosInstance(config); // Retries the connection
          } catch {
            if (!this.hasDoneLogout) AuthStorage.clear();
          } finally {
            this.refreshToken = null;
          }
        }
      }
      return value;
    }
    );
  }

  /**
   * Makes the refresh token request and returns the request promise.
   */
  private static async _refreshAccessToken() {
    const token: string = AuthStorage.getRefreshToken();
    const tokenData = { refresh: token };
    return axios.post(`${URLS_PREFIX.API}${AUTHENTICATION_URLS.REFRESH_TOKEN}`, tokenData);
  }

  /**
   * Inserts access token into request header.
   *
   * @param request - Request from axios
   */
  private static _addAuthenticationHeaderOnRequest(request: AxiosRequestConfig): void {
    const accessToken: string = AuthStorage.getAccessToken();
    if (request.headers) request.headers.Authorization = `Bearer ${accessToken}`;
  }

  /**
   * Tries to refresh the token and assignee it to the AuthController if succeed to refresh.
   */
  private static async _tryRefreshToken(): Promise<void> {
    this.refreshToken = this.refreshToken || this._refreshAccessToken();
    const refreshResponse: AxiosResponse = await this.refreshToken;
    if (refreshResponse.data.access) {
      AuthStorage.setAccessToken(refreshResponse.data.access);
    }
  }
}
