export interface AgendamentoInterface {
  id: number;
  dataHora: string;
  profissionalId?: number;
  profissionalNome: string;
  servicoId: number;
  servicoNome: string;
  servicoPreco?: number;
  status: string;
  observacoes?: string;
  clienteNome?: string;
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
