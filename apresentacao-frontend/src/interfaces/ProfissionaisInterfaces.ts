// ProfissionalInterface.ts
// import { ServicoOferecidoId } from "./ServicoOferecidoInterface"; // Você pode ter que criar ServicoOferecidoId mais tarde

// Definição da Agenda (Jornada de Trabalho)
export interface AgendaInterface {
  inicioJornada: string; // Ex: "09:00:00"
  fimJornada: string;   // Ex: "18:00:00"
}

// O Objeto Completo do Profissional
export interface ProfissionalInterface {
  id: {
    valor: number;
  };
  nome: string;
  email: {
    value: string;
  };
  cpf: {
    value: string;
  };
  telefone: {
    value: string;
  };
  senioridade: 'JUNIOR' | 'PLENO' | 'SENIOR';
  ativo: boolean;
  motivoInatividade?: string;
  agenda: AgendaInterface;
  servicoOferecidoIds: {
    valor: number;
  }[]; // Lista de IDs de serviços que ele oferece
}

export type ProfissionaisResponse = ProfissionalInterface[];