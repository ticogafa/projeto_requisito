import { AuthProvider } from '@/auth/AuthContext';
import '@/index.css';
import router from '@/routers';
import React from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';

export const root = document.getElementById('root');

createRoot(root!).render(
  <React.StrictMode>
    <AuthProvider>
      <RouterProvider router={router}/>
    </AuthProvider>
  </React.StrictMode>
);
