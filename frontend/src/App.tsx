import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from '@contexts/AuthContext';
import LoginForm from '@components/LoginForm';
import Dashboard from '@components/Dashboard';
import ProtectedRoute from '@components/ProtectedRoute';
import '@styles/globals.css';
import './App.css';

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
