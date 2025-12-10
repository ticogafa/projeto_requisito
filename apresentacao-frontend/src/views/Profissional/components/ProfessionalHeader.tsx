interface ProfessionalHeaderProps {
  userName?: string;
}

export default function ProfessionalHeader({ userName = 'Carlos Silva' }: ProfessionalHeaderProps) {
  const currentDate = new Date().toLocaleDateString('pt-BR', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  });

  return (
    <div className="mb-8">
      <h2 className="text-3xl font-bold mb-2">
        Atendimentos de {userName}
      </h2>
      <p className="text-gray-400">
        {currentDate}
      </p>
    </div>
  );
}
