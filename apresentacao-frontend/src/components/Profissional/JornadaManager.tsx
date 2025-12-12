import { DIAS_SEMANA, type JornadaDto } from '@/interfaces/JornadaInterface';
import MainService from '@/services/MainService';
import type { AxiosError } from 'axios';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';

interface JornadaManagerProps {
  profissionalId: number;
}

export default function JornadaManager({ profissionalId }: JornadaManagerProps) {
  const [jornadas, setJornadas] = useState<JornadaDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  useEffect(() => {
    loadJornada();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [profissionalId]);

  const loadJornada = () => {
    setLoading(true);
    MainService.getInstance().getJornada(
      profissionalId,
      (data: JornadaDto[]) => {
        // Merge with defaults to ensure all days are present
        const merged = DIAS_SEMANA.map(dia => {
          const existing = data.find(j => j.diaSemana === dia.value);
          if (existing) {
             return {
                 ...existing,
                 horaInicio: existing.horaInicio?.slice(0, 5) || '',
                 horaFim: existing.horaFim?.slice(0, 5) || '',
                 intervaloInicio: existing.intervaloInicio?.slice(0, 5) || '',
                 intervaloFim: existing.intervaloFim?.slice(0, 5) || ''
             };
          }
          return {
            diaSemana: dia.value,
            horaInicio: '08:00',
            horaFim: '18:00',
            intervaloInicio: '',
            intervaloFim: '',
            ativo: true // Default to active
          };
        });
        setJornadas(merged);
      },
      (error: AxiosError) => {
        console.error('Erro ao carregar jornada', error);
        toast.error('Erro ao carregar jornada de trabalho');
      },
      () => setLoading(false)
    );
  };

  const validateJornada = (currentJornadas: JornadaDto[]) => {
    const newErrors: { [key: string]: string } = {};
  
    currentJornadas.forEach((jornada, _index) => {
      if (jornada.ativo && (!jornada.horaInicio || !jornada.horaFim)) {
        newErrors[jornada.diaSemana] = 'Horário de início e fim são obrigatórios para dias ativos.';
      } else if (jornada.ativo && jornada.horaInicio >= jornada.horaFim) {
        newErrors[jornada.diaSemana] = 'Horário de fim deve ser depois do horário de início.';
      }
  
      if (jornada.ativo && jornada.intervaloInicio && jornada.intervaloFim) {
        if (jornada.intervaloInicio >= jornada.intervaloFim) {
          newErrors[jornada.diaSemana] = 'Início do intervalo deve ser antes do fim do intervalo.';
        }
        if (jornada.intervaloInicio < jornada.horaInicio || jornada.intervaloFim > jornada.horaFim) {
          newErrors[jornada.diaSemana] = 'Intervalo deve estar dentro do horário de trabalho.';
        }
      } else if (jornada.ativo && (jornada.intervaloInicio || jornada.intervaloFim)) {
        newErrors[jornada.diaSemana] = 'Ambos, início e fim do intervalo, devem ser preenchidos.';
      }
    });
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };
  
  const handleTimeChange = (index: number, field: keyof JornadaDto, value: string | boolean) => {
    const newJornadas = [...jornadas];
    newJornadas[index] = { ...newJornadas[index], [field]: value };
    setJornadas(newJornadas);
  
    // Validate immediately after change
    validateJornada(newJornadas);
  };

  const handleSave = () => {
    if (!validateJornada(jornadas)) {
      toast.error('Corrija os erros no formulário antes de salvar.');
      return;
    }

    setSaving(true);
    
    // Filter out inactive entries or format if needed
    const payload = jornadas.filter(j => j.ativo); // Only send active days for now, or adapt backend to handle inactive

    MainService.getInstance().atualizarJornada(
      profissionalId,
      payload,
      () => {
        toast.success('Jornada atualizada com sucesso!');
      },
      (error: AxiosError) => {
        console.error('Erro ao salvar jornada', error);
        toast.error('Erro ao atualizar jornada: ' + (error.response?.data as any)?.message || error.message);
      },
      () => setSaving(false)
    );
  };

  if (loading) {
    return <div className="p-8 text-center text-gray-400">Carregando jornada...</div>;
  }

  return (
    <div className="bg-dark-800 rounded-xl p-6 border border-dark-600 shadow-lg">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-bold text-white flex items-center gap-2">
          <span className="material-icons text-primary">schedule</span>
          Gerenciar Jornada de Trabalho
        </h2>
        <button
          onClick={handleSave}
          disabled={saving}
          className="bg-primary hover:bg-primary/90 text-white px-4 py-2 rounded-lg font-medium flex items-center gap-2 transition disabled:opacity-50"
        >
          {saving ? (
            <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
          ) : (
            <span className="material-icons">save</span>
          )}
          Salvar Alterações
        </button>
      </div>

      <div className="space-y-4">
        {jornadas.map((jornada, index) => {
            const diaLabel = DIAS_SEMANA.find(d => d.value === jornada.diaSemana)?.label || jornada.diaSemana;
            const hasError = errors[jornada.diaSemana];
            
            return (
                <div key={jornada.diaSemana} className={`grid grid-cols-1 md:grid-cols-12 gap-4 items-center bg-dark-700/50 p-4 rounded-lg border ${hasError ? 'border-red-500' : 'border-dark-600'} hover:border-primary/50 transition`}>
                    <div className="md:col-span-2 flex items-center gap-2">
                        <input
                            type="checkbox"
                            checked={jornada.ativo}
                            onChange={(e) => handleTimeChange(index, 'ativo', e.target.checked)}
                            className="form-checkbox h-5 w-5 text-primary rounded border-gray-500 bg-dark-600 focus:ring-primary"
                        />
                        <label className="font-medium text-white">{diaLabel}</label>
                    </div>
                    
                    <div className="md:col-span-4 flex items-center gap-2">
                        <div className="flex-1">
                            <label className="text-xs text-gray-400 block mb-1">Início</label>
                            <input
                                type="time"
                                value={jornada.horaInicio}
                                onChange={(e) => handleTimeChange(index, 'horaInicio', e.target.value)}
                                disabled={!jornada.ativo}
                                className="w-full bg-dark-800 border border-dark-600 rounded px-2 py-1 text-white text-sm focus:border-primary focus:outline-none disabled:opacity-50 disabled:cursor-not-allowed"
                            />
                        </div>
                        <span className="text-gray-500 mt-4">-</span>
                        <div className="flex-1">
                            <label className="text-xs text-gray-400 block mb-1">Fim</label>
                            <input
                                type="time"
                                value={jornada.horaFim}
                                onChange={(e) => handleTimeChange(index, 'horaFim', e.target.value)}
                                disabled={!jornada.ativo}
                                className="w-full bg-dark-800 border border-dark-600 rounded px-2 py-1 text-white text-sm focus:border-primary focus:outline-none disabled:opacity-50 disabled:cursor-not-allowed"
                            />
                        </div>
                    </div>

                    <div className="md:col-span-4 flex items-center gap-2">
                         <div className="flex-1">
                            <label className="text-xs text-gray-400 block mb-1">Intervalo Início</label>
                            <input
                                type="time"
                                value={jornada.intervaloInicio || ''}
                                onChange={(e) => handleTimeChange(index, 'intervaloInicio', e.target.value)}
                                disabled={!jornada.ativo}
                                className="w-full bg-dark-800 border border-dark-600 rounded px-2 py-1 text-white text-sm focus:border-primary focus:outline-none disabled:opacity-50 disabled:cursor-not-allowed"
                            />
                        </div>
                        <span className="text-gray-500 mt-4">-</span>
                        <div className="flex-1">
                            <label className="text-xs text-gray-400 block mb-1">Intervalo Fim</label>
                            <input
                                type="time"
                                value={jornada.intervaloFim || ''}
                                onChange={(e) => handleTimeChange(index, 'intervaloFim', e.target.value)}
                                disabled={!jornada.ativo}
                                className="w-full bg-dark-800 border border-dark-600 rounded px-2 py-1 text-white text-sm focus:border-primary focus:outline-none disabled:opacity-50 disabled:cursor-not-allowed"
                            />
                        </div>
                    </div>
                    
                    <div className="md:col-span-2 text-right">
                        {hasError && <p className="text-red-400 text-xs">{hasError}</p>}
                    </div>
                </div>
            );
        })}
      </div>
    </div>
  );
}
