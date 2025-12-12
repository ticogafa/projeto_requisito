import PerformanceDashboard from '@/components/Administrador/Report/PerformanceDashboard';

const PerformanceReportView: React.FC = () => {
  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold text-white mb-6">Relatório de Desempenho</h1>
      <PerformanceDashboard />
    </div>
  );
};

export default PerformanceReportView;
