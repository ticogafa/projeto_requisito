export type UserRole = 'cliente' | 'profissional' | 'admin';

export interface RegisterUserData {
  email: string;
  password: string;
  role: UserRole;
  nome?: string;
}

export interface LoginUserData {
  email: string;
  password: string;
}

export interface AuthTokenResponse {
  access: string;
  refresh: string;
  user: {
    id: number;
    email: string;
    role: UserRole;
    nome?: string;
  };
}
