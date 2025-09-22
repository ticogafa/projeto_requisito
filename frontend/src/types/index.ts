export type Union<T, K> = T | K;

export interface User {
  id: string;
  name: string;
  email: string;
}

export interface Servico {
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

export interface Profissional {
  id: number | string;
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

export interface CriarAgendamentoRequest {
  dataHora: string; // ISO date string
  clienteId: number;
  profissionalId: number;
  servicoId: number;
  observacoes?: string;
}

export type StatusAgendamento = 'AGENDADO' | 'CONFIRMADO' | 'CANCELADO' | 'CONCLUIDO';


export interface TimeSlot {
  time: string; // "09:00"
  status: 'available' | 'booked' | 'unavailable';
  appointmentId?: string;
}

export interface Profissional {
  id: number | string;
  nome: string;
  email: string;
  cpf: string;
  telefone: string;
  servicos?: []; // service IDs
}


// Product types
export interface Produto {
  id: number;
  nome: string;
  preco: number;
  estoque: number;
  estoqueMinimo: number;
  descricao?: string;
  categoria?: string;
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