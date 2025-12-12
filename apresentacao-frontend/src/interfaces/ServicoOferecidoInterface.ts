export interface ServicoOferecido {
  id: { valor: number };
  nome: string;
  preco: number;
  descricao: string;
  duracaoMinutos: number;
  motivoInatividade?: string | null;
  ativo?: boolean;
}

export type ServicosOferecidosResponse = ServicoOferecido[];
export default ServicoOferecido;
