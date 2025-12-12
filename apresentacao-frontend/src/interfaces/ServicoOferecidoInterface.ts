export interface ServicoOferecido {
  id: { valor: number } | number;
  nome: string;
  preco: number;
  descricao: string;
  duracaoMinutos: number;
  ativo?: boolean;
  categoria?: string;
  servicoDependente?: boolean;
  destaque?: string;
}

export type ServicosOferecidosResponse = ServicoOferecido[];
export default ServicoOferecido;
