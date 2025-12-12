import { useState, useEffect } from 'react';

interface ServiceTimerProps {
  startTime: Date;
}

export function ServiceTimer({ startTime }: ServiceTimerProps) {
  const [elapsed, setElapsed] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setElapsed(Math.floor((new Date().getTime() - startTime.getTime()) / 1000));
    }, 1000);
    return () => clearInterval(interval);
  }, [startTime]);

  const formatTime = (seconds: number) => {
    const h = Math.floor(seconds / 3600);
    const m = Math.floor((seconds % 3600) / 60);
    const s = seconds % 60;
    return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  };

  return (
    <div className="bg-dark-700 p-4 rounded-lg border border-primary/50 flex items-center justify-between mb-4 shadow-lg shadow-primary/10">
      <div className="flex items-center gap-3">
        <span className="material-icons text-primary animate-spin-slow">hourglass_top</span>
        <div>
          <h3 className="font-bold text-white">Atendimento em Andamento</h3>
          <p className="text-sm text-gray-400">Registrando tempo de serviço...</p>
        </div>
      </div>
      <div className="text-3xl font-mono font-bold text-primary tracking-wider">
        {formatTime(elapsed)}
      </div>
    </div>
  );
}
