import App from '@/App';
import ProtectedRoute from '@/components/common/ProtectedRoute';
import LoginView from '@/views/Auth/LoginView';
import ProfileSelection from '@/views/Auth/ProfileSelection';
import RegisterView from '@/views/Auth/RegisterView';
import ClientView from '@/views/Cliente/ClientView';
import { createBrowserRouter } from 'react-router-dom';

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
      }
    ]
  }
]);

export default router;
