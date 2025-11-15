/* eslint-disable react-hooks/exhaustive-deps */
import type {
  AgendamentoInterface,
  CriarAgendamentoRequest,
  ProfissionalDisponivelInterface
} from '@/interfaces/AgendamentoInterface';
import type { ServicosOferecidosResponse } from '@/interfaces/ServicoOferecidoInterface';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import type { AxiosResponse } from 'axios';
import { AxiosError } from 'axios';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';

export function useServicosOferecidos(params: object = {}, headers: object = {}) {
  const [data, setData] = useState<ServicosOferecidosResponse>([]);
  const { setLoading } = useLoadingStore();

  const paramsStr = JSON.stringify(params);
  const headersStr = JSON.stringify(headers);

  useEffect(() => {
    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = (response: AxiosResponse<ServicosOferecidosResponse>) => {
      setData(response.data);
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || err.message || 'Erro ao carregar serviços';
      toast.error(errorMessage);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.getServicosOferecidos(
      params,
      headers,
      successCallback,
      errorCallback,
      finallyCallback
    );
  }, [headersStr, paramsStr, setLoading]);

  return { data };
}

export function useAgendamentosPorCliente(clienteId: number, params: object = {}, headers: object = {}) {
  const [data, setData] = useState<AgendamentoInterface[]>([]);
  const { setLoading } = useLoadingStore();

  const paramsStr = JSON.stringify(params);
  const headersStr = JSON.stringify(headers);

  useEffect(() => {
    if (!clienteId) return;

    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = (response: AxiosResponse<AgendamentoInterface[]>) => {
      setData(response.data);
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || err.message || 'Erro ao carregar agendamentos';
      toast.error(errorMessage);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.getAgendamentosPorCliente(
      { ...params, clienteId },
      headers,
      successCallback,
      errorCallback,
      finallyCallback
    );
  }, [clienteId, headersStr, paramsStr, setLoading]);

  return { data, setData };
}

export function useProfissionaisDisponiveis(
  servicoId: number | null,
  dataHora: string | null,
  params: object = {},
  headers: object = {}
) {
  const [data, setData] = useState<ProfissionalDisponivelInterface[]>([]);
  const { setLoading } = useLoadingStore();

  const paramsStr = JSON.stringify(params);
  const headersStr = JSON.stringify(headers);

  useEffect(() => {
    if (!servicoId || !dataHora) {
      setData([]);
      return;
    }

    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = (response: AxiosResponse<ProfissionalDisponivelInterface[]>) => {
      setData(response.data);
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || err.message || 'Erro ao carregar profissionais';
      toast.error(errorMessage);
      setData([]);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.getProfissionaisDisponiveis(
      { ...params, servicoId, dataHora },
      headers,
      successCallback,
      errorCallback,
      finallyCallback
    );
  }, [servicoId, dataHora, headersStr, paramsStr, setLoading]);

  return { data };
}

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
