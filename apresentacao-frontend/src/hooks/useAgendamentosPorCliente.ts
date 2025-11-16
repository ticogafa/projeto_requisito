/* eslint-disable react-hooks/exhaustive-deps */
import type { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import { type AxiosResponse, AxiosError } from 'axios';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';

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
