/**
 * Service for managing authentication tokens in localStorage.
 */
export default class AuthStorage {
  private static readonly ACCESS_TOKEN_KEY = 'access_token';
  private static readonly REFRESH_TOKEN_KEY = 'refresh_token';
  private static readonly USER_KEY = 'user_data';
  private static readonly IS_LOGGED_KEY = 'is_logged';

  /**
   * Sets the access token in localStorage.
   */
  static setAccessToken(token: string): void {
    localStorage.setItem(this.ACCESS_TOKEN_KEY, token);
  }

  /**
   * Gets the access token from localStorage.
   */
  static getAccessToken(): string | null {
    return localStorage.getItem(this.ACCESS_TOKEN_KEY);
  }

  /**
   * Sets the refresh token in localStorage.
   */
  static setRefreshToken(token: string): void {
    localStorage.setItem(this.REFRESH_TOKEN_KEY, token);
  }

  /**
   * Gets the refresh token from localStorage.
   */
  static getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  /**
   * Sets user data in localStorage.
   */
  static setUserData(user: object): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  /**
   * Gets user data from localStorage.
   */
  static getUserData(): object | null {
    const data = localStorage.getItem(this.USER_KEY);
    return data ? JSON.parse(data) : null;
  }

  /**
   * Sets the logged-in status.
   */
  static setIsLogged(isLogged: boolean): void {
    localStorage.setItem(this.IS_LOGGED_KEY, String(isLogged));
  }

  /**
   * Gets the logged-in status.
   */
  static getIsLogged(): boolean {
    return localStorage.getItem(this.IS_LOGGED_KEY) === 'true';
  }

  /**
   * Clears all authentication data from localStorage.
   */
  static clearAll(): void {
    localStorage.removeItem(this.ACCESS_TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.IS_LOGGED_KEY);
  }
}
