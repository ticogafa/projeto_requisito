import { create } from 'zustand';
import { Caixa } from '@/interfaces/Caixa';
import MainService from '@/services/MainService';
import { toast } from 'react-toastify';

interface CaixaStore {
  lancamentos: Caixa[];
  saldo: number;
  fetchLancamentos: () => void;
  addLancamento: (data: { descricao: string; valor: number; tipo: 'ENTRADA' | 'SAIDA' }) => void;
}

const mainService = MainService.getInstance();

export const useCaixaStore = create<CaixaStore>((set, get) => ({
  lancamentos: [],
  saldo: 0,
  fetchLancamentos: () => {
    mainService.getLancamentos(
      (response) => {
        const lancamentos = response.data;
        const totalEntradas = lancamentos
          .filter((l) => l.tipo === 'ENTRADA')
          .reduce((acc, l) => acc + l.valor, 0);
        const totalSaidas = lancamentos
          .filter((l) => l.tipo === 'SAIDA')
          .reduce((acc, s) => acc + s.valor, 0);
        const saldo = totalEntradas - totalSaidas;
        set({ lancamentos, saldo });
      },
      (error) => {
        console.error('Erro ao buscar lançamentos:', error);
        toast.error('Erro ao buscar lançamentos.');
      },
      () => {}
    );
  },
  addLancamento: (data) => {
    mainService.addLancamento(
      data,
      () => {
        toast.success('Lançamento adicionado com sucesso!');
        get().fetchLancamentos(); // Refetch after adding
      },
      (error) => {
        console.error('Erro ao adicionar lançamento:', error);
        toast.error('Erro ao adicionar lançamento.');
      },
      () => {} // finallyCallback adicionado aqui
    );
  },
}));
