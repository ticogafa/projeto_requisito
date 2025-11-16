import type { AgendamentoInterface, CriarAgendamentoRequest } from '@/interfaces/AgendamentoInterface';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import { type AxiosResponse, AxiosError } from 'axios';
import { toast } from 'react-toastify';

export function useCriarAgendamento() {
  const { setLoading } = useLoadingStore();

  const criar = (
    data: CriarAgendamentoRequest,
    onSuccess?: (agendamento: AgendamentoInterface) => void
  ) => {
    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = (response: AxiosResponse<AgendamentoInterface>) => {
      toast.success('Agendamento criado com sucesso!');
      onSuccess?.(response.data);
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || err.message || 'Erro ao criar agendamento';
      toast.error(errorMessage);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.criarAgendamento(
      data,
      successCallback,
      errorCallback,
      finallyCallback
    );
  };

  return { criar };
}
