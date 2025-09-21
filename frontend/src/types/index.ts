// Authentication types
export interface User {
  id: string;
  name: string;
  email: string;
  phone?: string;
  points: number;
  role: 'client' | 'professional';
  createdAt: Date;
}

// Service types (based on ServicoOferecido and ServicoDTOs)
export interface Servico {
  id: number;
  nome: string;
  preco: number;
  duracaoMinutos: number;
  descricao: string;
}

export interface ServicoResponse {
  id: number;
  nome: string;
  preco: number;
  duracaoMinutos: number;
  descricao: string;
}

export interface CriarServicoRequest {
  nome: string;
  preco: number;
  duracaoMinutos: number;
  descricao: string;
}

// Professional types (based on Profissional and ProfissionalDTOs)
export interface Profissional {
  id: number;
  nome: string;
  email: string;
  cpf: string;
  telefone: string;
}

export interface ProfissionalResponse {
  id: number;
  nome: string;
  email: string;
  cpf: string;
  telefone: string;
}

export interface CriarProfissionalRequest {
  nome: string;
  email: string;
  cpf: string;
  telefone: string;
}

// Client types (based on Cliente model)
export interface Cliente {
  id: number;
  nome: string;
  email: string;
  cpf: string;
  telefone: string;
  pontos: number;
}

// Appointment types (based on Agendamento and AgendamentoDTOs)
export interface Agendamento {
  id: number;
  dataHora: string; // ISO date string
  status: StatusAgendamento;
  observacoes?: string;
  cliente: Cliente;
  profissional: Profissional;
  servico: Servico;
}

export interface AgendamentoResponse {
  id: number;
  dataHora: string; // ISO date string
  status: StatusAgendamento;
  observacoes?: string;
  // Client data
  clienteId: number;
  clienteNome: string;
  clienteEmail: string;
  clienteTelefone: string;
  // Professional data
  profissionalId: number;
  profissionalNome: string;
  profissionalEmail: string;
  // Service data
  servicoId: number;
  servicoNome: string;
  servicoPreco: number;
  servicoDuracaoMinutos: number;
}

export interface CriarAgendamentoRequest {
  dataHora: string; // ISO date string
  clienteId: number;
  profissionalId: number;
  servicoId: number;
  observacoes?: string;
}

export type StatusAgendamento = 'AGENDADO' | 'CONFIRMADO' | 'CANCELADO' | 'CONCLUIDO';

// Schedule types for legacy compatibility
export interface Schedule {
  [dayOfWeek: string]: TimeSlot[];
}

export interface TimeSlot {
  time: string; // "09:00"
  status: 'available' | 'booked' | 'unavailable';
  appointmentId?: string;
}

// Legacy types for backward compatibility with existing components
export interface Professional {
  id: string;
  name: string;
  services: string[]; // service IDs
  avatar?: string;
  rating?: number;
  schedule: Schedule;
}

export interface Appointment {
  id: string;
  clientId: string;
  professionalId: string;
  serviceId: string;
  date: string; // ISO date string
  time: string; // "09:00"
  status: 'pending' | 'confirmed' | 'completed' | 'cancelled';
  notes?: string;
  createdAt: Date;
  updatedAt: Date;
}

// Product types
export interface Product {
  id: number;
  nome: string;
  preco: number;
  estoque: number;
  estoqueMinimo: number;
  descricao?: string;
  categoria?: string;
}

// Loyalty types
export interface LoyaltyProgram {
  pointsBalance: number;
  vouchers: Voucher[];
  tier: 'bronze' | 'silver' | 'gold';
}

export interface Voucher {
  id: string;
  title: string;
  discount: number; // percentage
  expiresAt: Date;
  isUsed: boolean;
}

// API Response types
export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: string;
  message?: string;
}

// Common types
export type LoadingState = 'idle' | 'loading' | 'success' | 'error';

export interface PaginatedResponse<T> {
  items: T[];
  total: number;
  page: number;
  limit: number;
  hasNext: boolean;
  hasPrevious: boolean;
}
