// hooks/useServicosOferecidos.ts
import type { AxiosResponse } from 'axios';
import { AxiosError } from 'axios';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import MainService from '../services/MainService';
import { useLoadingStore } from '../store/useLoadingStore';

export function useServicosOferecidos(params: object = {}, headers: object = {}) {
  const [data, setData] = useState<AxiosResponse | null>(null);
  const { setLoading } = useLoadingStore();

  useEffect(() => {
    const service = new MainService();
    setLoading(true);

    const successCallback = (response: AxiosResponse) => {
      setData(response);
    };

    const errorCallback = (err: AxiosError) => {
      toast.error(err.message);
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
  }, [headers, params]);

  return { data };
}
