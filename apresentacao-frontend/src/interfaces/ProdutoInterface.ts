export interface ProdutoResumo {
  id: number;
  nome: string;
  estoque: number;
  preco: number;
  estoqueMinimo: number;
}

export interface ProdutoResumoExpandido extends ProdutoResumo {
  dataCadastro?: string;
  totalMovimentacoes?: number;
  ultimaMovimentacao?: string;
}

export interface MovimentacaoEstoqueResumo {
  id: number;
  produtoId: number;
  produtoNome: string;
  tipo: 'ENTRADA' | 'SAIDA' | 'VENDA' | 'AJUSTE' | 'ESTOQUE_INICIAL' | 'DESATIVACAO';
  quantidade: number;
  dataHora: string;
  estoqueAnterior: number;
  estoqueAtual: number;
  observacao?: string;
  usuarioResponsavel?: string;
}

export interface CadastrarProdutoRequest {
  nome: string;
  preco: number;
  estoqueInicial: number;
  estoqueMinimo: number;
  usuarioResponsavel: string;
}

export interface AtualizarProdutoRequest {
  nome: string;
  estoque: number;
  preco: number;
  estoqueMinimo: number;
  usuarioResponsavel: string;
}

export interface AdicionarEstoqueRequest {
  quantidade: number;
  observacao?: string;
  usuarioResponsavel: string;
}

export interface RemoverEstoqueRequest {
  quantidade: number;
  observacao?: string;
  usuarioResponsavel: string;
}

export interface RegistrarVendaRequest {
  quantidade: number;
  usuarioResponsavel: string;
}
