import { useState, useEffect } from 'react';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import type { ProdutoResumo } from '@/interfaces/ProdutoInterface';
import { toast } from 'react-toastify';
import type { AxiosError, AxiosResponse } from 'axios';

export function useProdutos() {
  const [produtos, setProdutos] = useState<ProdutoResumo[]>([]);
  const { setLoading } = useLoadingStore();

  const fetchProdutos = () => {
    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = (response: AxiosResponse<ProdutoResumo[]>) => {
      setProdutos(response.data);
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || 'Erro ao buscar produtos';
      toast.error(errorMessage);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.getProdutos(successCallback, errorCallback, finallyCallback);
  };

  useEffect(() => {
    fetchProdutos();
  }, []);

  return { produtos, refetch: fetchProdutos };
}

export function useProdutosEstoqueBaixo() {
  const [produtos, setProdutos] = useState<ProdutoResumo[]>([]);
  const { setLoading } = useLoadingStore();

  const fetchProdutos = () => {
    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = (response: AxiosResponse<ProdutoResumo[]>) => {
      setProdutos(response.data);
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || 'Erro ao buscar produtos com estoque baixo';
      toast.error(errorMessage);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.getProdutosEstoqueBaixo(successCallback, errorCallback, finallyCallback);
  };

  useEffect(() => {
    fetchProdutos();
  }, []);

  return { produtos, refetch: fetchProdutos };
}

