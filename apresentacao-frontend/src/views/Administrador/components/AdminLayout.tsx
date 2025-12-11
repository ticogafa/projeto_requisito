import { ReactNode } from 'react';
import AdminNavbar from '@/views/Administrador/components/AdminNavbar';
import AdminSidebar from '@/views/Administrador/components/AdminSidebar';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';

interface AdminLayoutProps {
  children: ReactNode;
}

export default function AdminLayout({ children }: AdminLayoutProps) {
  const navigate = useNavigate();

  const handleLogout = () => {

    localStorage.removeItem('token');
    toast.success('Logout realizado');
    navigate('/');
  };

  return (
    <div className="min-h-screen bg-dark-900 text-gray-100">
      <AdminNavbar onLogout={handleLogout} />
      <div className="flex">
        <AdminSidebar />
        <main className="flex-1 p-8 bg-dark-900">
          {children}
        </main>
      </div>
    </div>
  );
}
