import React from 'react';
import CacheMonitor from '@/components/Administrador/Cache/CacheMonitor';

const CashControlView: React.FC = () => {
  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold text-white mb-6">Controle de Caixa</h1>
      <CacheMonitor />
    </div>
  );
};

export default CashControlView;
