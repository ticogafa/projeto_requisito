/**
 * Handles local storage authentication data,
 * with centralized serialization/deserialization.
 */
export default class AuthStorage {
  private static getItem<T>(key: string, fallback: T): T {
    const raw = localStorage.getItem(key);
    if (raw === null) return fallback;

    try {
      return JSON.parse(raw);
    } catch {
      // fallback para dados antigos não serializados
      return raw as unknown as T;
    }
  }

  private static setItem<T>(key: string, value: T): void {
    localStorage.setItem(key, JSON.stringify(value));
  }

  static clear(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('is_logged');
  }

  static getAccessToken(): string {
    return this.getItem('access_token', '');
  }

  static getRefreshToken(): string {
    return this.getItem('refresh_token', '');
  }

  static getIsLogged(): boolean {
    return this.getItem('is_logged', false);
  }

  static setAccessToken(token: string): void {
    this.setItem('access_token', token);
  }

  static setRefreshToken(token: string): void {
    this.setItem('refresh_token', token);
  }

  static setIsLogged(isLogged: boolean): void {
    this.setItem('is_logged', isLogged);
  }
}
