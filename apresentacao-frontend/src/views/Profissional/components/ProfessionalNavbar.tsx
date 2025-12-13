interface ProfessionalNavbarProps {
  userName?: string;
  onLogout?: () => void;
}

export default function ProfessionalNavbar({ userName = 'Profissional', onLogout }: ProfessionalNavbarProps) {
  return (
    <header className="bg-dark-800 border-b border-dark-600 px-8 py-4">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <span className="material-icons text-primary text-4xl">
            content_cut
          </span>
          <h1 className="text-2xl font-bold">Sistema Barbearia</h1>
        </div>
                  <div className="flex items-center gap-6">
                    <div className="flex items-center gap-2 bg-primary/10 px-4 py-2 rounded-lg">
                      <span className="material-icons text-gray-400">person</span> {/* Placeholder for avatar */}
                      <span className="font-medium">Profissional</span>
                    </div>
                    <div className="flex items-center gap-3">
                      <span className="text-sm text-gray-400">{userName}</span>
                      <button
                        onClick={onLogout}
                        className="material-icons text-gray-400 hover:text-primary cursor-pointer transition"
                        title="Sair"
                      >
                        logout
                      </button>
                    </div>
                  </div>      </div>
    </header>
  );
}
