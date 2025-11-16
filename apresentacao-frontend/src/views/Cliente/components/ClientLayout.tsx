import { ReactNode } from 'react';
import ClientNavbar from '@/views/Cliente/components/ClientNavbar';
import ClientSidebar from '@/views/Cliente/components/ClientSidebar';

interface ClientLayoutProps {
  children: ReactNode;
  userName?: string;
  activeMenuItem?: string;
  onLogout?: () => void;
}

export default function ClientLayout({
  children,
  userName,
  activeMenuItem,
  onLogout
}: ClientLayoutProps) {
  return (
    <div className="min-h-screen">
      <ClientNavbar userName={userName} onLogout={onLogout} />
      <div className="flex">
        <ClientSidebar activeItem={activeMenuItem} />
        <main className="flex-1 p-8">
          {children}
        </main>
      </div>
    </div>
  );
}
