import { createBrowserRouter } from 'react-router-dom';
import App from '../App';
import ClientView from '../views/Cliente/ClientView';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        path: '/',
        element: <ClientView />,
      },
    ],
  },
]);

export default router;