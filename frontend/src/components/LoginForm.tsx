import { useState, type FC, type FormEvent } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import LoadingSpinner from '@/components/LoadingSpinner/LoadingSpinner';
import Logo from '@/components/Logo/Logo';
import { getFirebaseErrorMessage } from '@/utils';

const LoginForm: FC = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const { login, signup } = useAuth();

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    
    if (!email || !password) {
      setError('Por favor, preencha todos os campos');
      return;
    }

    if (password.length < 6) {
      setError('A senha deve ter pelo menos 6 caracteres');
      return;
    }

    setLoading(true);
    setError('');

    try {
      if (isLogin) {
        await login(email, password);
      } else {
        await signup(email, password);
      }
    } catch (error: unknown) {
      console.error('Erro de autenticação:', error);
      
      setError(
        getFirebaseErrorMessage((error as { code?: string }).code)
      );
    } finally {
      setLoading(false);
    }
  };

  const toggleMode = () => {
    setIsLogin(!isLogin);
    setError('');
    setEmail('');
    setPassword('');
  };

  return (
    <div className="app">
      <div className="login-container">
        <div className="logo-section">
          <Logo />
          <div>
            <h1>Barber Booker</h1>
            <small>Sistema de Agendamento • Firebase Auth</small>
          </div>
        </div>

        <form onSubmit={handleSubmit}>
          <h2 style={{ textAlign: 'center', marginBottom: 'var(--space-lg)', color: 'var(--text-primary)' }}>
            {isLogin ? 'Entrar' : 'Criar Conta'}
          </h2>

          {error && (
            <div className="error-message">
              {error}
            </div>
          )}

          <div className="form-group">
            <label htmlFor="email" className="form-label">
              Email
            </label>
            <input
              id="email"
              type="email"
              className="form-input"
              placeholder="seu@email.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              disabled={loading}
              autoComplete="email"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password" className="form-label">
              Senha
            </label>
            <input
              id="password"
              type="password"
              className="form-input"
              placeholder="Sua senha"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={loading}
              autoComplete={isLogin ? 'current-password' : 'new-password'}
              required
            />
          </div>

          <button
            type="submit"
            className="btn"
            disabled={loading}
            style={{ width: '100%', marginTop: 'var(--space-md)' }}
          >
          {loading ? (
            <>
              <LoadingSpinner />
              <span>{isLogin ? 'Entrando...' : 'Criando conta...'}</span>
            </>
          ) : (
            <>
              {isLogin ? 'Entrar' : 'Criar Conta'}
            </>
          )}
          </button>

          <div style={{ textAlign: 'center', marginTop: 'var(--space-lg)' }}>
            <button
              type="button"
              className="btn ghost"
              onClick={toggleMode}
              disabled={loading}
            >
              {isLogin ? 'Não tem uma conta? Criar conta' : 'Já tem uma conta? Entrar'}
            </button>
          </div>
        </form>

        <footer style={{ marginTop: 'var(--space-xl)', textAlign: 'center', color: 'var(--text-dim)', fontSize: '12px' }}>
          Autenticação segura com Firebase • Sistema protegido
        </footer>
      </div>
    </div>
  );
};

export default LoginForm;
