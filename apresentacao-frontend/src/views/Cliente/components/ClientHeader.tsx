interface ClientHeaderProps {
  userName?: string;
  onNewAppointment: () => void;
}

export default function ClientHeader({ userName = 'Cliente', onNewAppointment }: ClientHeaderProps) {
  return (
    <div className="mb-8 flex items-center justify-between">
      <div>
        <h2 className="text-3xl font-bold mb-2">Olá, {userName}</h2>
        <p className="text-gray-400">
          Gerencie seus agendamentos e avaliações
        </p>
      </div>
      <button
        onClick={onNewAppointment}
        className="bg-primary hover:bg-primary/90 text-white px-6 py-3 rounded-lg font-medium transition flex items-center gap-2"
      >
        <span className="material-icons">add</span>
        Novo Agendamento
      </button>
    </div>
  );
}
