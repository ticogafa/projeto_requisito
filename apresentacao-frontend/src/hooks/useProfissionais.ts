
import type { ProfissionaisResponse } from '@/interfaces/ProfissionaisInterfaces';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import { type AxiosResponse, AxiosError } from 'axios';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';

export function useProfissionais(
  params: object = {},
  headers: object = {}
) {

  const [data, setData] = useState<ProfissionaisResponse>([]);
  const { setLoading } = useLoadingStore();

  const paramsStr = JSON.stringify(params);
  const headersStr = JSON.stringify(headers);

  useEffect(() => {
    const service = MainService.getInstance();
    setLoading(true);

    const successCallback = (response: AxiosResponse<ProfissionaisResponse>) => {
      setData(response.data);
    };

    const errorCallback = (err: AxiosError) => {

      const errorMessage = (err.response?.data as any)?.message || err.message || 'Erro ao carregar lista de profissionais';
      toast.error(errorMessage);
      setData([]);
    };

    const finallyCallback = () => {
      setLoading(false);
    };

    service.getProfissionais(
      params,
      headers,
      successCallback,
      errorCallback,
      finallyCallback
    );
  }, [headersStr, paramsStr, setLoading]);

  return { data };
}
