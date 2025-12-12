// Import the functions you need from the SDKs you need
import type { UserRole } from '@/interfaces/UserInterface';
import { FirebaseError } from '@firebase/util';
import { getAnalytics } from 'firebase/analytics';
import { initializeApp } from 'firebase/app';
import {
  createUserWithEmailAndPassword,
  getAuth,
  signInWithEmailAndPassword,
  signOut,
  User
} from 'firebase/auth';
import { doc, getDoc, getFirestore, setDoc } from 'firebase/firestore';
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: 'AIzaSyDWlljzNEliOiIOadK_iaZyWpP8nsb3gNU',
  authDomain: 'barbearia-cesar.firebaseapp.com',
  projectId: 'barbearia-cesar',
  storageBucket: 'barbearia-cesar.firebasestorage.app',
  messagingSenderId: '553433565891',
  appId: '1:553433565891:web:fbfb491c6139c5576fb076',
  measurementId: 'G-EHW775Y6BX'
};

// Initialize Firebase
export const app = initializeApp(firebaseConfig);
export const analytics = getAnalytics(app);
export const auth = getAuth(app);
export const db = getFirestore(app);

// Firestore helper functions for user data
export const saveUserRole = async (
  userId: string, 
  email: string, 
  role: UserRole,
  name?: string,
  cpf?: string,
  phone?: string
): Promise<void> => {
  await setDoc(doc(db, 'users', userId), {
    email,
    role,
    name: name || '',
    cpf: cpf || '',
    phone: phone || '',
    createdAt: new Date().toISOString()
  });
};

export const getUserRole = async (userId: string): Promise<UserRole | null> => {
  const userDoc = await getDoc(doc(db, 'users', userId));
  if (userDoc.exists()) {
    return userDoc.data().role as UserRole;
  }
  return null;
};

export const getUserData = async (userId: string): Promise<{ 
  email: string; 
  role: UserRole; 
  createdAt: string;
  name?: string;
  cpf?: string;
  phone?: string;
} | null> => {
  const userDoc = await getDoc(doc(db, 'users', userId));
  if (userDoc.exists()) {
    return userDoc.data() as { 
      email: string; 
      role: UserRole; 
      createdAt: string;
      name?: string;
      cpf?: string;
      phone?: string;
    };
  }
  return null;
};

// Auth helper functions
export const loginWithEmail = async (email: string, password: string) => {
  return await signInWithEmailAndPassword(auth, email, password);
};

export const registerWithEmail = async (email: string, password: string) => {
  return await createUserWithEmailAndPassword(auth, email, password);
};

export const logout = async () => {
  return await signOut(auth);
};

const FIREBASE_ERROR_MESSAGES: Record<string, string> = {
  // Erros de autenticação - Credenciais
  'auth/invalid-credential': 'Email ou senha incorretos',
  'auth/wrong-password': 'Email ou senha incorretos',
  'auth/user-not-found': 'Email ou senha incorretos',

  // Erros de validação
  'auth/invalid-email': 'Email inválido',
  'auth/weak-password': 'A senha deve ter no mínimo 6 caracteres',

  // Erros de conta
  'auth/user-disabled': 'Esta conta foi desativada',
  'auth/email-already-in-use': 'Este email já está em uso',

  // Erros de segurança
  'auth/too-many-requests': 'Muitas tentativas. Tente novamente mais tarde',
  'auth/requires-recent-login': 'Por segurança, faça login novamente',

  // Erros de sistema
  'auth/network-request-failed': 'Erro de conexão. Verifique sua internet',
  'auth/operation-not-allowed': 'Operação não permitida'
};

// Função para traduzir códigos de erro do Firebase
export const getFirebaseErrorMessage = (error: unknown): string => {
  if (error instanceof FirebaseError) {
    const errorCode = error.code;
    return FIREBASE_ERROR_MESSAGES[errorCode] || `Erro: ${errorCode}`;
  }

  if (error instanceof Error) {
    return error.message;
  }

  return 'Erro desconhecido. Tente novamente';
};

export type { User };
