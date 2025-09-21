import type { 
  User, 
  ServicoResponse, 
  CriarServicoRequest,
  ProfissionalResponse, 
  CriarProfissionalRequest,
  AgendamentoResponse, 
  CriarAgendamentoRequest,
  Product, 
  LoyaltyProgram,
  ApiResponse
} from '@/types';


const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

class ApiClient {
  private baseURL: string;
  private headers: HeadersInit;

  constructor() {
    this.baseURL = API_BASE_URL;
    this.headers = {
      'Content-Type': 'application/json',
    };
  }

  private async request<T>(
    endpoint: string, 
    options: RequestInit = {}
  ): Promise<ApiResponse<T>> {
    try {
      const url = `${this.baseURL}${endpoint}`;
      const response = await fetch(url, {
        ...options,
        headers: {
          ...this.headers,
          ...options.headers,
        },
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || `HTTP error! status: ${response.status}`);
      }

      return {
        success: true,
        data,
      };
    } catch (error) {
      console.error('API Request failed:', error);
      return {
        success: false,
        error: error instanceof Error ? error.message : 'Unknown error occurred',
      };
    }
  }

  // Auth methods
  async loginUser(email: string, password: string): Promise<ApiResponse<User>> {
    return this.request<User>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  }

  async registerUser(userData: Partial<User>): Promise<ApiResponse<User>> {
    return this.request<User>('/auth/register', {
      method: 'POST',
      body: JSON.stringify(userData),
    });
  }

  // Services - updated to match backend endpoints
  async getServices(): Promise<ApiResponse<ServicoResponse[]>> {
    return this.request<ServicoResponse[]>('/servicos');
  }

  async getService(id: number): Promise<ApiResponse<ServicoResponse>> {
    return this.request<ServicoResponse>(`/servicos/${id}`);
  }

  async createService(service: CriarServicoRequest): Promise<ApiResponse<ServicoResponse>> {
    return this.request<ServicoResponse>('/servicos', {
      method: 'POST',
      body: JSON.stringify(service),
    });
  }

  async updateService(id: number, service: Partial<CriarServicoRequest>): Promise<ApiResponse<ServicoResponse>> {
    return this.request<ServicoResponse>(`/servicos/${id}`, {
      method: 'PUT',
      body: JSON.stringify({ id, ...service }),
    });
  }

  async deleteService(id: number): Promise<ApiResponse<void>> {
    return this.request<void>(`/servicos/${id}`, {
      method: 'DELETE',
    });
  }

  // Professionals - updated to match backend endpoints
  async getProfessionals(): Promise<ApiResponse<ProfissionalResponse[]>> {
    return this.request<ProfissionalResponse[]>('/profissionais');
  }

  async getProfessional(id: number): Promise<ApiResponse<ProfissionalResponse>> {
    return this.request<ProfissionalResponse>(`/profissionais/${id}`);
  }

  async createProfessional(professional: CriarProfissionalRequest): Promise<ApiResponse<ProfissionalResponse>> {
    return this.request<ProfissionalResponse>('/profissionais', {
      method: 'POST',
      body: JSON.stringify(professional),
    });
  }

  // Appointments - updated to match backend endpoints
  async createAppointment(appointment: CriarAgendamentoRequest): Promise<ApiResponse<AgendamentoResponse>> {
    return this.request<AgendamentoResponse>('/agendamentos/criar-agendamento', {
      method: 'POST',
      body: JSON.stringify(appointment),
    });
  }

  async getAppointment(id: number): Promise<ApiResponse<AgendamentoResponse>> {
    return this.request<AgendamentoResponse>(`/agendamentos/${id}`);
  }

  async confirmAppointment(id: number): Promise<ApiResponse<AgendamentoResponse>> {
    return this.request<AgendamentoResponse>(`/agendamentos/confirmar-agendamento/${id}`, {
      method: 'PATCH',
    });
  }

  async cancelAppointment(id: number): Promise<ApiResponse<AgendamentoResponse>> {
    return this.request<AgendamentoResponse>(`/agendamentos/cancelar-agendamento/${id}`, {
      method: 'PATCH',
    });
  }

  // Scheduling availability endpoints
  async getAvailableTimeSlots(
    data: string, 
    servicoOferecidoId: number
  ): Promise<ApiResponse<string[]>> {
    return this.request<string[]>(`/agendamentos/horarios-disponiveis?data=${data}&servicoOferecidoId=${servicoOferecidoId}`);
  }

  async getAvailableProfessionals(
    data: string, 
    horario: string, 
    servicoOferecidoId: number
  ): Promise<ApiResponse<ProfissionalResponse[]>> {
    return this.request<ProfissionalResponse[]>(`/agendamentos/profissionais-disponiveis?data=${data}&horario=${horario}&servicoOferecidoId=${servicoOferecidoId}`);
  }

  // Products
  async getProducts(): Promise<ApiResponse<Product[]>> {
    return this.request<Product[]>('/produtos');
  }

  async getProductsLowStock(): Promise<ApiResponse<Product[]>> {
    return this.request<Product[]>('/produtos/estoque-baixo');
  }

  async createProduct(product: {
    nome: string;
    preco: number;
    estoque: number;
    estoqueMinimo: number;
  }): Promise<ApiResponse<Product>> {
    return this.request<Product>('/produtos/cadastrar', {
      method: 'POST',
      body: JSON.stringify(product),
    });
  }

  async updateProductStock(id: number, quantidade: number): Promise<ApiResponse<Product>> {
    return this.request<Product>(`/produtos/${id}/baixa?quantidade=${quantidade}`, {
      method: 'POST',
    });
  }

  // Loyalty
  async getUserLoyalty(userId: string): Promise<ApiResponse<LoyaltyProgram>> {
    return this.request<LoyaltyProgram>(`/loyalty/${userId}`);
  }

  async addLoyaltyPoints(userId: string, points: number): Promise<ApiResponse<LoyaltyProgram>> {
    return this.request<LoyaltyProgram>(`/loyalty/${userId}/points`, {
      method: 'POST',
      body: JSON.stringify({ points }),
    });
  }

  // Set auth token for authenticated requests
  setAuthToken(token: string) {
    this.headers = {
      ...this.headers,
      'Authorization': `Bearer ${token}`,
    };
  }

  // Remove auth token
  clearAuthToken() {
    const headersWithoutAuth = { ...this.headers };
    delete (headersWithoutAuth as Record<string, string>).Authorization;
    this.headers = headersWithoutAuth;
  }
}

export const apiClient = new ApiClient();
export default apiClient;
