import React from 'react';
import Logo from '../Logo/Logo';

interface HeaderProps {
  userName?: string;
  onLogout?: () => void;
}

export const Header: React.FC<HeaderProps> = ({ userName, onLogout }) => {
  const today = new Date();
  const todayLabel = today.toLocaleDateString(undefined, { 
    weekday: 'short', 
    year: 'numeric', 
    month: 'short', 
    day: 'numeric' 
  });

  return (
    <header className="header">
      <div className="brand">
        <Logo />
        <div>
          <h1>Barber Booker</h1>
          <small>Sistema de Agendamento • Firebase Auth</small>
        </div>
      </div>
      <div className="header-info">
        <div className="subtle">Hoje: <span>{todayLabel}</span></div>
        {userName && (
          <div className="user-section">
            <span className="subtle">Olá, {userName}</span>
            {onLogout && (
              <button 
                onClick={onLogout}
                className="btn ghost"
                style={{ padding: '6px 12px', fontSize: '12px' }}
              >
                Sair
              </button>
            )}
          </div>
        )}
      </div>
    </header>
  );
};
