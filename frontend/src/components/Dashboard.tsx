import React from 'react';
import { useAuth } from '@contexts/AuthContext';

const Dashboard: React.FC = () => {
  const { currentUser, logout } = useAuth();

  const handleLogout = async () => {
    try {
      await logout();
    } catch (error) {
      console.error('Error logging out:', error);
    }
  };

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>🪒 Sistema Barbearia</h1>
        <div className="user-info">
          <span>Bem-vindo, {currentUser?.email}</span>
          <button onClick={handleLogout} className="logout-button">
            Sair
          </button>
        </div>
      </header>
      
      <main className="dashboard-content">
        <div className="welcome-card">
          <h2>Dashboard</h2>
          <p>Bem-vindo ao sistema de gerenciamento da barbearia!</p>
          <p><strong>Usuário:</strong> {currentUser?.email}</p>
          <p><strong>ID:</strong> {currentUser?.uid}</p>
        </div>
        
      </main>
    </div>
  );
};

export default Dashboard;
