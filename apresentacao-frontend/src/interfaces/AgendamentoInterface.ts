export interface AgendamentoInterface {
  id: number;
  dataHora: string;
  profissionalNome: string;
  servicoNome: string;
  status: string;
  observacoes?: string;
}

export interface ProfissionalDisponivelInterface {
  id: number;
  nome: string;
  senioridade: string;
}

export interface CriarAgendamentoRequest {
  clienteId: number;
  servicoId: number;
  dataHora: string;
  profissionalId?: number;
  observacoes?: string;
}
