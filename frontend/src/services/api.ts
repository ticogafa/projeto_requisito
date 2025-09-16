import type { 
  User, 
  Service, 
  Professional, 
  Appointment, 
  Product, 
  LoyaltyProgram,
  ApiResponse
} from '../types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api';

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

  // Services
  async getServices(): Promise<ApiResponse<Service[]>> {
    return this.request<Service[]>('/services');
  }

  async getService(id: string): Promise<ApiResponse<Service>> {
    return this.request<Service>(`/services/${id}`);
  }

  // Professionals
  async getProfessionals(): Promise<ApiResponse<Professional[]>> {
    return this.request<Professional[]>('/professionals');
  }

  async getProfessional(id: string): Promise<ApiResponse<Professional>> {
    return this.request<Professional>(`/professionals/${id}`);
  }

  async getProfessionalAvailability(
    professionalId: string, 
    date: string
  ): Promise<ApiResponse<string[]>> {
    return this.request<string[]>(`/professionals/${professionalId}/availability?date=${date}`);
  }

  // Appointments
  async createAppointment(appointment: Partial<Appointment>): Promise<ApiResponse<Appointment>> {
    return this.request<Appointment>('/appointments', {
      method: 'POST',
      body: JSON.stringify(appointment),
    });
  }

  async getUserAppointments(userId: string): Promise<ApiResponse<Appointment[]>> {
    return this.request<Appointment[]>(`/appointments/user/${userId}`);
  }

  async updateAppointment(
    id: string, 
    updates: Partial<Appointment>
  ): Promise<ApiResponse<Appointment>> {
    return this.request<Appointment>(`/appointments/${id}`, {
      method: 'PATCH',
      body: JSON.stringify(updates),
    });
  }

  async cancelAppointment(id: string): Promise<ApiResponse<void>> {
    return this.request<void>(`/appointments/${id}`, {
      method: 'DELETE',
    });
  }

  // Products
  async getProducts(): Promise<ApiResponse<Product[]>> {
    return this.request<Product[]>('/products');
  }

  async updateProductStock(id: string, quantity: number): Promise<ApiResponse<Product>> {
    return this.request<Product>(`/products/${id}/stock`, {
      method: 'PATCH',
      body: JSON.stringify({ quantity }),
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
