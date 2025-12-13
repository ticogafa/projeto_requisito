interface ClientHeaderProps {
  userName?: string;
  pontos?: number;
  onNewAppointment: () => void;
}

export default function ClientHeader({ userName = 'Cliente', pontos = 0, onNewAppointment }: ClientHeaderProps) {
  return (
    <div className="mb-8 flex items-center justify-between">
      <div>
        <h2 className="text-3xl font-bold mb-2">Olá, {userName}</h2>
        <p className="text-gray-400 flex items-center gap-3">
          <span>Gerencie seus agendamentos e avaliações</span>
          <span className="inline-flex items-center gap-1 px-3 py-1 rounded-full bg-primary/10 text-primary text-sm font-semibold">
            <span className="material-icons text-sm">loyalty</span>
            {pontos} pts
          </span>
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
