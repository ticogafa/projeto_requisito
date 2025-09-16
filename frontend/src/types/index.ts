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

// Service types
export interface Service {
  id: string;
  name: string;
  duration: number; // in minutes
  price: number;
  icon: string;
  description?: string;
}

// Professional types
export interface Professional {
  id: string;
  name: string;
  services: string[]; // service IDs
  avatar?: string;
  rating?: number;
  schedule: Schedule;
}

// Schedule types
export interface Schedule {
  [dayOfWeek: string]: TimeSlot[];
}

export interface TimeSlot {
  time: string; // "09:00"
  status: 'available' | 'booked' | 'unavailable';
  appointmentId?: string;
}

// Appointment types
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
  id: string;
  name: string;
  price: number;
  stock: number;
  description?: string;
  image?: string;
  category: string;
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
