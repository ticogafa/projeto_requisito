import App from '@/App';
import ProtectedRoute from '@/components/common/ProtectedRoute';
import LoginView from '@/views/Auth/LoginView';
import ProfileSelection from '@/views/Auth/ProfileSelection';
import RegisterView from '@/views/Auth/RegisterView';
import { createBrowserRouter } from 'react-router-dom';

import ClientView from '@/views/Cliente/ClientView';
import ProfessionalView from '@/views/Profissional/ProfessionalView';

import AdminDashboardView from '@/views/Administrador/AdminDashboardView';
import AgendamentoView from '@/views/Administrador/AgendamentoView';
import CashControlView from '@/views/Administrador/CashControlView';
import PerformanceReportView from '@/views/Administrador/PerformanceReportView';
import { AdminLayout } from '@/views/Administrador/components';
import EstoqueView from '@/views/Administrador/EstoqueView';
import ProfissionaisView from '@/views/ProfissioinalView';
import ServicosView from '@/views/ServicoView';

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
        path: '/profissional',
        element: (
          <ProtectedRoute>
            <ProfessionalView />
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
            <AdminLayout>
              <ProfissionaisView />
            </AdminLayout>
          </ProtectedRoute>
        )
      },

      {
        path: '/admin/servicos',
        element: (
          <ProtectedRoute>
            <AdminLayout>
              <ServicosView />
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
        path: '/admin/agendamentos',
        element: (
          <ProtectedRoute>
            <AgendamentoView />
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
      },
      {
        path: '/admin/controle-caixa',
        element: (
          <ProtectedRoute>
            <AdminLayout>
              <CashControlView />
            </AdminLayout>
          </ProtectedRoute>
        )
      },
      {
        path: '/admin/relatorio-desempenho',
        element: (
          <ProtectedRoute>
            <AdminLayout>
              <PerformanceReportView />
            </AdminLayout>
          </ProtectedRoute>
        )
      }
    ]
  }
]);

export default router;
