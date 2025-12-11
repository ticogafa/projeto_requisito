import { ServicosOferecidosResponse } from '@/interfaces/ServicoOferecidoInterface';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import { type AxiosResponse, AxiosError } from 'axios';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';

export function useServicos() {
  const [data, setData] = useState<ServicosOferecidosResponse>([]);
  const { setLoading } = useLoadingStore();

  useEffect(() => {
    const service = MainService.getInstance();
    setLoading(true);

    service.getServicosOferecidos(
      {},
      {},
      (response: AxiosResponse<ServicosOferecidosResponse>) => {
        setData(response.data);
      },
      (err: AxiosError) => {
        console.error(err);
        toast.error('Erro ao carregar serviços.');
      },
      () => setLoading(false)
    );
  }, []);

  return { data };
}
