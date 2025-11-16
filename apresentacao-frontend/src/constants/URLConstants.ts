export const URLS_PREFIX = {
  API: '/api',
  AUTHENTICATION: '/auth',
  SERVICO_OFERECIDO: '/servico-oferecido'
} as const;

export const SERVICO_OFERECIDO_URLS = {
  GET_ALL_SERVICOS_OFERECIDOS: `${URLS_PREFIX.SERVICO_OFERECIDO}/listar/`
};

export const AGENDAMENTO_URLS = {
  CRIAR: '/agendamentos/criar',
  EDITAR: '/agendamentos',
  CANCELAR: '/agendamentos',
  PROFISSIONAIS_DISPONIVEIS: '/agendamentos/profissionais-disponiveis',
  POR_CLIENTE: '/agendamentos/por-cliente'
};

export const AUTHENTICATION_URLS = {
  GET_TOKEN: `${URLS_PREFIX.AUTHENTICATION}/token/`,
  REFRESH_TOKEN: `${URLS_PREFIX.AUTHENTICATION}/token/refresh/`,
  VERIFY_TOKEN: `${URLS_PREFIX.AUTHENTICATION}/token/verify/`,
  REGISTER: `${URLS_PREFIX.AUTHENTICATION}/register/`,
  LOGIN: `${URLS_PREFIX.AUTHENTICATION}/login/`
};

export const URLS_TO_BE_IGNORED: string[] = [
  AUTHENTICATION_URLS.GET_TOKEN,
  AUTHENTICATION_URLS.REFRESH_TOKEN,
  AUTHENTICATION_URLS.VERIFY_TOKEN
];
