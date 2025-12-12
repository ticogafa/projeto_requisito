/* eslint-disable react-hooks/exhaustive-deps */
import type { ProfissionalDisponivelInterface } from '@/interfaces/AgendamentoInterface';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import { type AxiosResponse, AxiosError } from 'axios';
import { useEffect, useState } from 'react';

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
      // Garantir que sempre seja um array e com IDs únicos
      const profissionais = Array.isArray(response.data) ? response.data : [];
      // Filtrar profissionais duplicados baseado no ID
      const profissionaisUnicos = profissionais.filter((prof, index, self) => 
        index === self.findIndex((p) => p.id === prof.id)
      );
      setData(profissionaisUnicos);
    };

    const errorCallback = (err: AxiosError) => {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const errorMessage = (err.response?.data as any)?.message || err.message || 'Erro ao carregar profissionais';
      console.error('Erro ao buscar profissionais:', errorMessage);
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
