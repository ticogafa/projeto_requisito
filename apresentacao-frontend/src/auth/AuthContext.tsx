import { User } from 'firebase/auth';
import { createContext, useContext, useEffect, useState } from 'react';
import { useAuthState } from 'react-firebase-hooks/auth';
import { auth } from '@/auth/index';

interface AuthContextType {
  user: User | null;
  loading: boolean;
  error: Error | undefined;
}

const AuthContext = createContext<AuthContextType>({
  user: null,
  loading: true,
  error: undefined
});

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => useContext(AuthContext);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, loading, error] = useAuthState(auth);
  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    if (!loading) {
      setIsInitialized(true);
    }
  }, [loading]);

  return (
    <AuthContext.Provider value={{ user: user || null, loading: !isInitialized, error }}>
      {children}
    </AuthContext.Provider>
  );
}
