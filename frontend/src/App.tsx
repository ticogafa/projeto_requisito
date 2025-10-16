import Dashboard from '@/components/Dashboard';
import LoginForm from '@/components/LoginForm';
import ProtectedRoute from '@components/ProtectedRoute';
import { AuthProvider, useAuth } from '@contexts/AuthContext';
import React from 'react';
import { Navigate, Route, BrowserRouter as Router, Routes } from 'react-router-dom';

const AppRoutes: React.FC = () => {
  const { currentUser } = useAuth();

  return (
    <Routes>
      <Route 
        path="/login" 
        element={currentUser ? <Navigate to="/dashboard" /> : <LoginForm />} 
      />
      <Route 
        path="/dashboard" 
        element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        }
      />
      <Route 
        path="/" 
        element={<Navigate to={currentUser ? "/dashboard" : "/login"} />} 
      />
    </Routes>
  );
};

const App: React.FC = () => {
  return (
    <Router>
      <AuthProvider>
        <div className="App">
          <AppRoutes />
        </div>
      </AuthProvider>
    </Router>
  );
};

export default App;
