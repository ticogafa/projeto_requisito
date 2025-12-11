/**
 * Interface representing a single service offered by the barbershop.
 * Matches the ServicoOferecidoResumo from the backend.
 */
export interface ServicoOferecido {
  id: number;
  nome: string;
  preco: number;
  descricao: string;
  duracaoMinutos: number;
  ativo?: boolean;
  motivoInatividade?: string | null;
}

/**
 * Type for the array of services returned by the backend.
 */
export type ServicosOferecidosResponse = ServicoOferecido[];

export default ServicoOferecido;
