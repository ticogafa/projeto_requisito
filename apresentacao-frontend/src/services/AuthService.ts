import { loginWithEmail as firebaseLogin, logout as firebaseLogout, registerWithEmail as firebaseRegister, getFirebaseErrorMessage, getUserData, saveUserRole } from '@/auth';
import type { UserRole } from '@/interfaces/UserInterface';
import AuthStorage from '@/services/AuthStorage';
import MainService from '@/services/MainService';
import type { UserCredential } from 'firebase/auth';

/**
 * Service responsible for authentication operations.
 * Encapsulates Firebase authentication logic following Single Responsibility Principle.
 */
export default class AuthService {
  /**
   * Performs user login with email and password.
   * Authenticates in Firebase and retrieves user role from Firestore.
   *
   * @param email - User's email
   * @param password - User's password
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
   */
  static async login(
    email: string,
    password: string,
    successCallback: (userCredential: UserCredential) => void,
    errorCallback: (error: string) => void,
    finallyCallback: () => void
  ): Promise<void> {
    try {
      // 1. Autenticar no Firebase
      const userCredential = await firebaseLogin(email, password);

      // 2. Buscar dados do usuário no Firestore
      const userData = await getUserData(userCredential.user.uid);

      if (userData) {
        // 3. Sincronizar com Backend (Get or Create)
        try {
           const mainService = MainService.getInstance();
           
           if (userData.role === 'cliente') {
              await new Promise<void>((resolve, reject) => {
                 mainService.getClientePorEmail(
                    userData.email,
                    () => resolve(), // Encontrou, tudo certo
                    (_error) => {
                       // Se não encontrou (provavel 404), tenta criar
                       if (userData.name && userData.cpf && userData.phone) {
                          console.log('Cliente não encontrado no backend. Tentando criar...');
                          mainService.criarCliente(
                             { 
                               nome: userData.name, 
                               email: userData.email, 
                               cpf: userData.cpf, 
                               telefone: userData.phone 
                             },
                             () => {
                                console.log('Cliente recriado no backend com sucesso.');
                                resolve();
                             },
                             (createError) => {
                                console.error('Erro ao recriar cliente:', createError);
                                reject(createError);
                             },
                             () => {}
                          );
                       } else {
                          console.warn('Dados incompletos no Firestore para recriar cliente.');
                          resolve(); // Deixa passar, mas backend vai falhar nas buscas
                       }
                    },
                    () => {}
                 );
              });
           } else if (userData.role === 'profissional') {
               // Lógica similar para profissional se necessário
               // Por enquanto foca no cliente como solicitado
           }

        } catch (syncError) {
           console.error('Erro na sincronização com backend durante login:', syncError);
           // Não bloqueia login, mas avisa console
        }

        // Salvar dados do usuário no localStorage
        AuthStorage.setUserData({
          id: userCredential.user.uid,
          email: userData.email,
          role: userData.role
        });
        AuthStorage.setIsLogged(true);
      }

      successCallback(userCredential);
    } catch (error: unknown) {
      const errorMessage = getFirebaseErrorMessage(error);
      console.error('Erro no login:', errorMessage);
      errorCallback(errorMessage);
    } finally {
      finallyCallback();
    }
  }

  /**
   * Performs user registration with email and password.
   * Registers in Firebase and saves user role in Firestore.
   *
   * @param email - User's email
   * @param password - User's password
   * @param role - User's role (cliente, profissional, admin)
   * @param name - User's full name
   * @param cpf - User's CPF
   * @param phone - User's phone number
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
   */
  static async register(
    email: string,
    password: string,
    role: UserRole,
    name: string,
    cpf: string,
    phone: string,
    successCallback: (userCredential: UserCredential) => void,
    errorCallback: (error: string) => void,
    finallyCallback: () => void
  ): Promise<void> {
    try {
      // 1. Registrar no Firebase
      const userCredential = await firebaseRegister(email, password);

      // 2. Salvar role e dados do usuário no Firestore
      await saveUserRole(userCredential.user.uid, email, role, name, cpf, phone);

      // 3. Criar entidade no Backend (Java/Spring)
      // Isso garante que o usuário tenha um ID numérico no sistema legado/backend
      try {
        if (role === 'cliente') {
          await new Promise<void>((resolve, reject) => {
             MainService.getInstance().criarCliente(
               { nome: name, email, cpf, telefone: phone },
               (response) => {
                  console.log('Cliente criado no backend com sucesso:', response.data);
                  resolve();
               },
               (error) => {
                  console.error('Erro ao criar cliente no backend:', error);
                  // Não impedimos o registro no Firebase por enquanto, mas logamos
                  reject(error);
               },
               () => {}
             );
          });
        } else if (role === 'profissional') {
           // Nota: Profissional pode requerer mais campos no futuro
           await new Promise<void>((resolve, reject) => {
             MainService.getInstance().criarProfissional(
               { nome: name, email, cpf, telefone: phone },
               (response) => {
                  console.log('Profissional criado no backend com sucesso:', response.data);
                  resolve();
               },
               (error) => {
                  console.error('Erro ao criar profissional no backend:', error);
                  reject(error);
               },
               () => {}
             );
           });
        }
      } catch (backendError) {
        console.warn('Atenção: Usuário criado no Firebase, mas falha ao sincronizar com backend:', backendError);
        // Opcional: toast.warning("Conta criada, mas houve um erro de sincronização. Contate o suporte.");
      }

      // 4. Salvar dados no localStorage
      AuthStorage.setUserData({
        id: userCredential.user.uid,
        email,
        role
      });
      AuthStorage.setIsLogged(true);

      successCallback(userCredential);
    } catch (error: unknown) {
      const errorMessage = getFirebaseErrorMessage(error);
      console.error('Erro no registro:', errorMessage);
      errorCallback(errorMessage);
    } finally {
      finallyCallback();
    }
  }

  /**
   * Performs user logout.
   * Clears Firebase session and local storage.
   *
   * @param successCallback - Success callback function
   * @param errorCallback - Error callback function
   * @param finallyCallback - Finally callback function
   */
  static async logout(
    successCallback: () => void,
    errorCallback: (error: string) => void,
    finallyCallback: () => void = () => { }
  ): Promise<void> {
    try {
      await firebaseLogout();

      // Limpar tokens e dados do localStorage
      AuthStorage.clearAll();

      successCallback();
    } catch (error: unknown) {
      const errorMessage = getFirebaseErrorMessage(error);
      console.error('Erro no logout:', errorMessage);
      errorCallback(errorMessage);
    } finally {
      finallyCallback();
    }
  }

  /**
   * Validates email format.
   *
   * @param email - Email to validate
   * @returns True if email is valid
   */
  static validateEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  /**
   * Validates password strength.
   *
   * @param password - Password to validate
   * @returns Object with validation result and message
   */
  static validatePassword(password: string): { valid: boolean; message?: string } {
    if (password.length < 6) {
      return {
        valid: false,
        message: 'A senha deve ter no mínimo 6 caracteres'
      };
    }
    return { valid: true };
  }

  /**
   * Validates login form data.
   *
   * @param email - User's email
   * @param password - User's password
   * @returns Object with validation result and error message if any
   */
  static validateLoginData(email: string, password: string): { valid: boolean; error?: string } {
    if (!email || !password) {
      return {
        valid: false,
        error: 'Preencha todos os campos'
      };
    }

    if (!this.validateEmail(email)) {
      return {
        valid: false,
        error: 'Email inválido'
      };
    }

    const passwordValidation = this.validatePassword(password);
    if (!passwordValidation.valid) {
      return {
        valid: false,
        error: passwordValidation.message
      };
    }

    return { valid: true };
  }

  /**
   * Gets the current user's role from localStorage.
   *
   * @returns User's role or null if not logged in
   */
  static getCurrentUserRole(): UserRole | null {
    const userData = AuthStorage.getUserData() as { role?: UserRole } | null;
    return userData?.role || null;
  }

  /**
   * Gets the current user's data from localStorage.
   *
   * @returns User data or null if not logged in
   */
  static getCurrentUserData(): { id: string; email: string; role: UserRole } | null {
    return AuthStorage.getUserData() as { id: string; email: string; role: UserRole } | null;
  }
}
