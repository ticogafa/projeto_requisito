import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import type { AxiosError, AxiosResponse } from 'axios';
import { toast } from 'react-toastify';
import type {
  CadastrarProdutoRequest,
  AtualizarProdutoRequest,
  AdicionarEstoqueRequest,
  RemoverEstoqueRequest,
  RegistrarVendaRequest,
  ProdutoResumo,
} from '@/interfaces/ProdutoInterface';

export function useCadastrarProduto() {
  const { setLoading } = useLoadingStore();

  const cadastrar = (
    data: CadastrarProdutoRequest,
    onSuccess?: (produto: ProdutoResumo) => void
  ) => {
    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = (response: AxiosResponse<ProdutoResumo>) => {
      toast.success('Produto cadastrado com sucesso!');
      onSuccess?.(response.data);
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || 'Erro ao cadastrar produto';
      toast.error(errorMessage);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.cadastrarProduto(data, successCallback, errorCallback, finallyCallback);
  };

  return { cadastrar };
}

export function useAtualizarProduto() {
  const { setLoading } = useLoadingStore();

  const atualizar = (
    id: number,
    data: AtualizarProdutoRequest,
    onSuccess?: (produto: ProdutoResumo) => void
  ) => {
    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = (response: AxiosResponse<ProdutoResumo>) => {
      toast.success('Produto atualizado com sucesso!');
      onSuccess?.(response.data);
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || 'Erro ao atualizar produto';
      toast.error(errorMessage);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.atualizarProduto(id, data, successCallback, errorCallback, finallyCallback);
  };

  return { atualizar };
}

export function useAdicionarEstoque() {
  const { setLoading } = useLoadingStore();

  const adicionar = (
    id: number,
    data: AdicionarEstoqueRequest,
    onSuccess?: () => void
  ) => {
    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = () => {
      toast.success('Estoque adicionado com sucesso!');
      onSuccess?.();
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || 'Erro ao adicionar estoque';
      toast.error(errorMessage);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.adicionarEstoque(id, data, successCallback, errorCallback, finallyCallback);
  };

  return { adicionar };
}

export function useRemoverEstoque() {
  const { setLoading } = useLoadingStore();

  const remover = (
    id: number,
    data: RemoverEstoqueRequest,
    onSuccess?: () => void
  ) => {
    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = () => {
      toast.success('Estoque removido com sucesso!');
      onSuccess?.();
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || 'Erro ao remover estoque';
      toast.error(errorMessage);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.removerEstoque(id, data, successCallback, errorCallback, finallyCallback);
  };

  return { remover };
}

export function useRegistrarVenda() {
  const { setLoading } = useLoadingStore();

  const registrar = (
    id: number,
    data: RegistrarVendaRequest,
    onSuccess?: () => void
  ) => {
    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = () => {
      toast.success('Venda registrada com sucesso!');
      onSuccess?.();
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || 'Erro ao registrar venda';
      toast.error(errorMessage);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.registrarVenda(id, data, successCallback, errorCallback, finallyCallback);
  };

  return { registrar };
}

