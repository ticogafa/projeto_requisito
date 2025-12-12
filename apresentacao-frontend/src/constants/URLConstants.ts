export const URLS_PREFIX = {
  API: '/api',
  AUTHENTICATION: '/auth',
  SERVICO_OFERECIDO: '/servico'
} as const;

export const SERVICO_OFERECIDO_URLS = {
  GET_ALL_SERVICOS_OFERECIDOS: `${URLS_PREFIX.SERVICO_OFERECIDO}`
};

export const AGENDAMENTO_URLS = {
  BASE: '/agendamentos',
  CRIAR: '/agendamentos/criar',
  EDITAR: '/agendamentos',
  CANCELAR: '/agendamentos',
  PROFISSIONAIS_DISPONIVEIS: '/agendamentos/profissionais-disponiveis',
  POR_CLIENTE: '/agendamentos/por-cliente',
  POR_PROFISSIONAL: '/agendamentos/por-profissional'
};

export const PRODUTO_URLS = {
  GET_ALL: '/produtos',
  GET_ESTOQUE_BAIXO: '/produtos/estoque-baixo',
  CADASTRAR: '/produtos',
  ATUALIZAR: '/produtos',
  ADICIONAR_ESTOQUE: '/produtos/:id/adicionar-estoque',
  REMOVER_ESTOQUE: '/produtos/:id/remover-estoque',
  REGISTRAR_VENDA: '/produtos/:id/registrar-venda',
  HISTORICO: '/produtos/:id/movimentacoes'
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
