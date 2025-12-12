import HttpClient from './httpClient';
import { URLS_PREFIX } from '@/constants/URLConstants';
import { AxiosError, AxiosResponse } from 'axios';

interface CacheStats {
  hits: number;
  misses: number;
  size: number;
}

export default class CacheService {
  client: HttpClient;
  static instance: CacheService;

  private constructor() {
    this.client = new HttpClient(URLS_PREFIX.API);
  }

  public static getInstance(): CacheService {
    if (!this.instance) {
      this.instance = new CacheService();
    }
    return this.instance;
  }

  public getCacheStats(
    successCallback: (data: CacheStats) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.get(
      '/cache/metricas',
      {},
      {},
      (response: AxiosResponse) => {
        const data = response.data;
        const stats: CacheStats = {
          hits: data.reuso || 0,
          misses: data.lazyLoads || 0,
          size: data.produtosCarregados || 0
        };
        successCallback(stats);
      },
      errorCallback,
      finallyCallback
    );
  }

  public clearCache(
    successCallback: (response: AxiosResponse) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    this.client.post(
      '/cache/limpar',
      {},
      {},
      successCallback,
      errorCallback,
      finallyCallback
    );
  }
}
