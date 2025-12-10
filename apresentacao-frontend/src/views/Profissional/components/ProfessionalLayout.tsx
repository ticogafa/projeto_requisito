import { ReactNode } from 'react';
import ProfessionalNavbar from './ProfessionalNavbar';
import ProfessionalSidebar from './ProfessionalSidebar';

interface ProfessionalLayoutProps {
  children: ReactNode;
  userName?: string;
  activeMenuItem?: string;
  onLogout?: () => void;
}

export default function ProfessionalLayout({
  children,
  userName,
  activeMenuItem,
  onLogout
}: ProfessionalLayoutProps) {
  return (
    <div className="min-h-screen bg-dark-900 text-white">
      <ProfessionalNavbar userName={userName} onLogout={onLogout} />
      <div className="flex">
        <ProfessionalSidebar activeItem={activeMenuItem} />
        <main className="flex-1 p-8">
          {children}
        </main>
      </div>
    </div>
  );
}
