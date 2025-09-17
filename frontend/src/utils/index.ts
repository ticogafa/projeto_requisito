/**
 * Environment utility functions
 */
export const getAppName = (): string => {
  return import.meta.env.VITE_APP_NAME || 'Sistema Barbearia';
};

export const getAppVersion = (): string => {
  return import.meta.env.VITE_APP_VERSION || '1.0.0';
};

export const isDevelopment = (): boolean => {
  return import.meta.env.DEV;
};

export const isProduction = (): boolean => {
  return import.meta.env.PROD;
};

/**
 * Error handling utilities
 */
export const getErrorMessage = (error: unknown): string => {
  if (error instanceof Error) {
    return error.message;
  }
  if (typeof error === 'string') {
    return error;
  }
  return 'An unexpected error occurred';
};

/**
 * Firebase error handling
 */
export const getFirebaseErrorMessage = (error: unknown): string => {
  const message = getErrorMessage(error);
  
  // Common Firebase Auth error translations
  const errorMap: Record<string, string> = {
    'auth/user-not-found': 'Usuário não encontrado',
    'auth/wrong-password': 'Senha incorreta',
    'auth/email-already-in-use': 'Email já está em uso',
    'auth/weak-password': 'Senha muito fraca',
    'auth/invalid-email': 'Email inválido',
    'auth/user-disabled': 'Usuário desabilitado',
    'auth/too-many-requests': 'Muitas tentativas. Tente novamente mais tarde',
  };
  
  for (const [code, translation] of Object.entries(errorMap)) {
    if (message.includes(code)) {
      return translation;
    }
  }
  
  return message;
};
