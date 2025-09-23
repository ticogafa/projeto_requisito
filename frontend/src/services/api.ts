import axios from 'axios';
import type { AxiosInstance, AxiosRequestConfig, AxiosError } from 'axios';
import type { Agendamento, CriarAgendamentoRequest, CriarProfissionalRequest, CriarServicoRequest, Produto, Profissional, Servico } from '@/types';
import type { User } from 'firebase/auth';

// Define callback types
export type SuccessCallback<T> = (data: T) => void;
export type ErrorCallback = (error: string) => void;

const API_BASE_URL = 'http://localhost:8080/api';

class ApiClient {
  private readonly axiosInstance: AxiosInstance;

  constructor() {
    this.axiosInstance = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    
    // Add response interceptor for global error handling
    this.axiosInstance.interceptors.response.use(
      (response) => response,
      (error: AxiosError) => {
        console.error('API Request failed:', error);
        return Promise.reject(error);
      }
    );
  }

  private async request<T>(
    endpoint: string,
    options: AxiosRequestConfig = {},
    onSuccess?: SuccessCallback<T>,
    onError?: ErrorCallback
  ): Promise<void> {
    try {
      const response = await this.axiosInstance({
        url: endpoint,
        ...options,
      });
      
      if (onSuccess) {
        onSuccess(response.data);
      }
    } catch (error) {
      const errorMessage = this.handleError(error);
      
      if (onError) {
        onError(errorMessage);
      }
    }
  }
  
  private handleError(error: unknown): string {
    if (axios.isAxiosError(error)) {
      const axiosError = error as AxiosError;
      if (axiosError.response) {
        // The server responded with a status code outside the 2xx range
        const data = axiosError.response.data as Record<string, unknown>;
        return (data.message as string) || `HTTP error! status: ${axiosError.response.status}`;
      } else if (axiosError.request) {
        // The request was made but no response was received
        return 'No response received from the server';
      } else {
        // Something happened in setting up the request
        return axiosError.message || 'An error occurred during the request setup';
      }
    }
    
    return error instanceof Error ? error.message : 'Unknown error occurred';
  }

  // Auth methods
  loginUser(
    email: string, 
    password: string, 
    onSuccess: SuccessCallback<User>, 
    onError: ErrorCallback
  ): void {
    this.request<User>(
      '/auth/login',
      {
        method: 'POST',
        data: { email, password },
      },
      onSuccess,
      onError
    );
  }

  registerUser(
    userData: Partial<User>, 
    onSuccess: SuccessCallback<User>, 
    onError: ErrorCallback
  ): void {
    this.request<User>(
      '/auth/register',
      {
        method: 'POST',
        data: userData,
      },
      onSuccess,
      onError
    );
  }

  // Services
  getServices(
    onSuccess: SuccessCallback<Servico[]>, 
    onError: ErrorCallback
  ): void {
    this.request<Servico[]>(
      '/servicos',
      {},
      onSuccess,
      onError
    );
  }

  getService(
    id: number, 
    onSuccess: SuccessCallback<Servico>, 
    onError: ErrorCallback
  ): void {
    this.request<Servico>(
      `/servicos/${id}`,
      {},
      onSuccess,
      onError
    );
  }

  createService(
    service: CriarServicoRequest, 
    onSuccess: SuccessCallback<Servico>, 
    onError: ErrorCallback
  ): void {
    this.request<Servico>(
      '/servicos',
      {
        method: 'POST',
        data: service,
      },
      onSuccess,
      onError
    );
  }

  updateService(
    id: number, 
    service: Partial<CriarServicoRequest>, 
    onSuccess: SuccessCallback<Servico>, 
    onError: ErrorCallback
  ): void {
    this.request<Servico>(
      `/servicos/${id}`,
      {
        method: 'PUT',
        data: { id, ...service },
      },
      onSuccess,
      onError
    );
  }

  deleteService(
    id: number, 
    onSuccess: SuccessCallback<void>, 
    onError: ErrorCallback
  ): void {
    this.request<void>(
      `/servicos/${id}`,
      {
        method: 'DELETE',
      },
      onSuccess,
      onError
    );
  }

  // Professionals
  getProfessionals(
    onSuccess: SuccessCallback<Profissional[]>, 
    onError: ErrorCallback
  ): void {
    this.request<Profissional[]>(
      '/profissionais',
      {},
      onSuccess,
      onError
    );
  }

  getProfessional(
    id: number, 
    onSuccess: SuccessCallback<Profissional>, 
    onError: ErrorCallback
  ): void {
    this.request<Profissional>(
      `/profissionais/${id}`,
      {},
      onSuccess,
      onError
    );
  }

  createProfessional(
    professional: CriarProfissionalRequest, 
    onSuccess: SuccessCallback<Profissional>, 
    onError: ErrorCallback
  ): void {
    this.request<Profissional>(
      '/profissionais',
      {
        method: 'POST',
        data: professional,
      },
      onSuccess,
      onError
    );
  }

  // Appointments
  createAppointment(
    appointment: CriarAgendamentoRequest, 
    onSuccess: SuccessCallback<Agendamento>, 
    onError: ErrorCallback
  ): void {
    this.request<Agendamento>(
      '/agendamentos/criar-agendamento',
      {
        method: 'POST',
        data: appointment,
      },
      onSuccess,
      onError
    );
  }

  getAppointment(
    id: number, 
    onSuccess: SuccessCallback<Agendamento>, 
    onError: ErrorCallback
  ): void {
    this.request<Agendamento>(
      `/agendamentos/${id}`,
      {},
      onSuccess,
      onError
    );
  }

  confirmAppointment(
    id: number, 
    onSuccess: SuccessCallback<Agendamento>, 
    onError: ErrorCallback
  ): void {
    this.request<Agendamento>(
      `/agendamentos/confirmar-agendamento/${id}`,
      {
        method: 'PATCH',
      },
      onSuccess,
      onError
    );
  }

  cancelAppointment(
    id: number, 
    onSuccess: SuccessCallback<Agendamento>, 
    onError: ErrorCallback
  ): void {
    this.request<Agendamento>(
      `/agendamentos/cancelar-agendamento/${id}`,
      {
        method: 'PATCH',
      },
      onSuccess,
      onError
    );
  }

  // Scheduling availability
  getAvailableTimeSlots(
    data: string, 
    servicoOferecidoId: number, 
    onSuccess: SuccessCallback<string[]>, 
    onError: ErrorCallback
  ): void {
    this.request<string[]>(
      `/agendamentos/horarios-disponiveis?data=${data}&servicoId=${servicoOferecidoId}`,
      {},
      onSuccess,
      onError
    );
  }

  getAvailableProfessionals(
    data: string, 
    horario: string, 
    servicoOferecidoId: number, 
    onSuccess: SuccessCallback<Profissional[]>, 
    onError: ErrorCallback
  ): void {
    this.request<Profissional[]>(
      `/agendamentos/profissionais-disponiveis?data=${data}&horario=${horario}&servicoId=${servicoOferecidoId}`,
      {},
      onSuccess,
      onError
    );
  }

  // Products
  getProducts(
    onSuccess: SuccessCallback<Produto[]>, 
    onError: ErrorCallback
  ): void {
    this.request<Produto[]>(
      '/produtos',
      {},
      onSuccess,
      onError
    );
  }

  getProductsLowStock(
    onSuccess: SuccessCallback<Produto[]>, 
    onError: ErrorCallback
  ): void {
    this.request<Produto[]>(
      '/produtos/estoque-baixo',
      {},
      onSuccess,
      onError
    );
  }

  createProduct(
    product: {
      nome: string;
      preco: number;
      estoque: number;
      estoqueMinimo: number;
    }, 
    onSuccess: SuccessCallback<Produto[]>, 
    onError: ErrorCallback
  ): void {
    this.request<Produto[]>(
      '/produtos/cadastrar',
      {
        method: 'POST',
        data: product,
      },
      onSuccess,
      onError
    );
  }

  updateProductStock(
    id: number, 
    quantidade: number, 
    onSuccess: SuccessCallback<Produto[]>, 
    onError: ErrorCallback
  ): void {
    this.request<Produto[]>(
      `/produtos/${id}/baixa?quantidade=${quantidade}`,
      {
        method: 'POST',
      },
      onSuccess,
      onError
    );
  }



  // Authentication token management
  setAuthToken(token: string): void {
    this.axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  }

  clearAuthToken(): void {
    delete this.axiosInstance.defaults.headers.common['Authorization'];
  }
}

export const apiClient = new ApiClient();
export default apiClient;
