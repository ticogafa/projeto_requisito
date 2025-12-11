import { createBrowserRouter } from 'react-router-dom';
import App from '@/App';
import ProtectedRoute from '@/components/common/ProtectedRoute';

import LoginView from '@/views/Auth/LoginView';
import ProfileSelection from '@/views/Auth/ProfileSelection';
import RegisterView from '@/views/Auth/RegisterView';

import ClientView from '@/views/Cliente/ClientView';

import AdminDashboardView from '@/views/Administrador/AdminDashboardView';
import { AdminLayout } from '@/views/Administrador/components';
import ProfissionaisView from '@/views/ProfissioinalView';
import EstoqueView from '@/views/Administrador/EstoqueView';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [

      {
        path: '/',
        element: <ProfileSelection />
      },
      {
        path: '/login',
        element: <LoginView />
      },
      {
        path: '/register',
        element: <RegisterView />
      },

      {
        path: '/cliente',
        element: (
          <ProtectedRoute>
            <ClientView />
          </ProtectedRoute>
        )
      },

      {
        path: '/admin',
        element: (
          <ProtectedRoute>
            <AdminDashboardView />
          </ProtectedRoute>
        )
      },
      {
        path: '/admin/profissionais',
        element: (
          <ProtectedRoute>
            {/* Reaproveita a view de profissionais DENTRO do layout do admin */}
            <AdminLayout>
              <ProfissionaisView />
            </AdminLayout>
          </ProtectedRoute>
        )
      },

      {
        path: '/admin/agendamentos',
        element: (
          <ProtectedRoute>
            <AdminDashboardView />
          </ProtectedRoute>
        )
      },
      {
        path: '/admin/servicos',
        element: (
          <ProtectedRoute>
            <AdminDashboardView />
          </ProtectedRoute>
        )
      },
      {
        path: '/admin/estoque',
        element: (
          <ProtectedRoute>
            <EstoqueView />
          </ProtectedRoute>
        )
      },
      {
        path: '/admin/financeiro',
        element: (
          <ProtectedRoute>
            <AdminDashboardView />
          </ProtectedRoute>
        )
      },
      {
        path: '/admin/relatorios',
        element: (
          <ProtectedRoute>
            <AdminDashboardView />
          </ProtectedRoute>
        )
      }
    ]
  }
]);

export default router;
