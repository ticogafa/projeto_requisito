export interface AgendaInterface {
  inicioJornada: string;
  fimJornada: string;
}

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
  }[];
}

export type ProfissionaisResponse = ProfissionalInterface[];
