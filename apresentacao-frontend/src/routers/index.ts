import { createBrowserRouter } from 'react-router-dom';
import ClientView from '../views/Cliente/ClientView';

// Export a router to be mounted by the main entrypoint. Do not call createRoot here;
// mounting the router from a single place prevents double-rendering and keeps
// the app entry predictable.
export const router = createBrowserRouter([
  {
    path: '/',
    Component: ClientView,
  },
]);

export default router;