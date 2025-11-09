import type { AxiosError, AxiosResponse } from "axios";
import AuthService from "./AuthService";
import AuthStorage from "./AuthStorage";


const _AUTH_SERVICE: AuthService = new AuthService();

/**
 * A class for manipulating the login.
 */
export default class AuthController {
  /**
   * Performs the login.
   *
   * @param user - User data
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
   */
  static login(
    user: object,
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    const authSuccessCallback = (response: AxiosResponse): void => {
      if (successCallback){
        successCallback(response);
        AuthStorage.setAccessToken(response.data.access);
        AuthStorage.setRefreshToken(response.data.refresh);
        AuthStorage.setIsLogged(true);
      } 
    };
    _AUTH_SERVICE.signInUser(user, authSuccessCallback, errorCallback, finallyCallback);
  }
}