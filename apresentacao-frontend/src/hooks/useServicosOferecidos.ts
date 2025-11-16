/* eslint-disable react-hooks/exhaustive-deps */
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
