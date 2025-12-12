import HttpClient from './httpClient';
import { URLS_PREFIX } from '@/constants/URLConstants';
import { AxiosError, AxiosResponse } from 'axios';

export interface RelatorioDesempenho {
  tempoServico: number;
  receitaGerada: number;
  numeroClientesAtendidos: number;
  avaliacaoFuncionario: number;
}

export default class PerformanceService {
  client: HttpClient;
  static instance: PerformanceService;

  private constructor() {
    this.client = new HttpClient(URLS_PREFIX.API);
  }

  public static getInstance(): PerformanceService {
    if (!this.instance) {
      this.instance = new PerformanceService();
    }
    return this.instance;
  }

  public getRelatorioDesempenho(
    profissionalId: number,
    date: string | undefined,
    successCallback: (data: RelatorioDesempenho) => void,
    errorCallback: (error: AxiosError) => void,
    finallyCallback: () => void
  ): void {
    const params: any = {};
    if (date) {
      params.data = date;
    }

    this.client.get(
      `/relatorios/${profissionalId}`,
      params,
      {},
      (response: AxiosResponse) => successCallback(response.data as RelatorioDesempenho),
      errorCallback,
      finallyCallback
    );
  }
}
