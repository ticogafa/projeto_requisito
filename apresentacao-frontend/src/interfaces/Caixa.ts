export interface Caixa {
    id: number;
    descricao: string;
    valor: number;
    tipo: 'ENTRADA' | 'SAIDA';
    data: string;
}
