import type { CriarAgendamentoRequest, ProfissionalDisponivelInterface } from '@/interfaces/AgendamentoInterface';
import type { ServicoOferecido } from '@/interfaces/ServicoOferecidoInterface';
import MainService from '@/services/MainService';
import { normalizeIds } from '@/utils/apiHelpers';
import { type AxiosError, type AxiosResponse } from 'axios';
import { useEffect, useState } from 'react';

interface Cliente {
  id: number;
  nome: string;
  email: string;
  telefone: string;
}

interface NewAgendamentoModalProps {
  onClose: () => void;
  onSuccess: () => void;
  clienteId?: number; // Opcional - se não fornecido, mostra select para escolher
}

export default function NewAgendamentoModal({ onClose, onSuccess, clienteId }: NewAgendamentoModalProps) {
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [servicos, setServicos] = useState<ServicoOferecido[]>([]);
  const [profissionaisDisponiveis, setProfissionaisDisponiveis] = useState<ProfissionalDisponivelInterface[]>([]);
  
  const [isNewClientMode, setIsNewClientMode] = useState(false);

  const [formData, setFormData] = useState({
    clienteId: clienteId?.toString() || '',
    servicoId: '',
    dataHora: '',
    profissionalId: '',
    observacoes: '',
    // Novos campos
    emailCliente: '',
    nomeCliente: '',
    cpfCliente: '',
    telefoneCliente: ''
  });
  
  const [loading, setLoading] = useState(false);
  const [loadingServicos, setLoadingServicos] = useState(true);
  const [loadingClientes, setLoadingClientes] = useState(!clienteId); // Só carrega se não foi fornecido
  const [loadingProfissionais, setLoadingProfissionais] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    loadServicos();
    if (!clienteId) {
      loadClientes();
    }
  }, []);

  useEffect(() => {
    if (formData.servicoId && formData.dataHora) {
      loadProfissionaisDisponiveis();
    } else {
      setProfissionaisDisponiveis([]);
      setFormData((prev) => ({ ...prev, profissionalId: '' }));
    }
  }, [formData.servicoId, formData.dataHora]);

  const loadClientes = async () => {
    setLoadingClientes(true);
    try {
      const response = await fetch('http://localhost:8080/api/clientes');
      if (response.ok) {
        const data = await response.json();
        setClientes(Array.isArray(data) ? data : []);
      }
    } catch (error) {
      console.error('Erro ao carregar clientes:', error);
      setClientes([]);
    } finally {
      setLoadingClientes(false);
    }
  };

  const loadServicos = () => {
    setLoadingServicos(true);
    MainService.getInstance().getServicosOferecidos(
      {},
      {},
      (response: AxiosResponse) => {
        const data = response.data;
        const normalized = normalizeIds(data) as ServicoOferecido[];
        const servicosAtivos = normalized.filter((s) => s.ativo === true || s.ativo === undefined);
        setServicos(servicosAtivos);
      },
      (error: AxiosError) => {
        console.error('Erro ao carregar serviços:', error);
        setError('Erro ao carregar serviços. Verifique a conexão com o servidor.');
      },
      () => {
        setLoadingServicos(false);
      }
    );
  };

  const loadProfissionaisDisponiveis = () => {
    if (!formData.servicoId || !formData.dataHora) {
      setProfissionaisDisponiveis([]);
      return;
    }

    setLoadingProfissionais(true);
    
    const servicoIdNum = parseInt(formData.servicoId);
    if (isNaN(servicoIdNum) || servicoIdNum <= 0) {
      setProfissionaisDisponiveis([]);
      setLoadingProfissionais(false);
      return;
    }

    MainService.getInstance().getProfissionaisDisponiveis(
      {
        servicoId: servicoIdNum,
        dataHora: formData.dataHora
      },
      {},
      (response: AxiosResponse) => {
        const data = response.data;
        setProfissionaisDisponiveis(data);
      },
      (error: AxiosError) => {
        console.error('Erro ao carregar profissionais disponíveis:', error);
        setProfissionaisDisponiveis([]);
      },
      () => {
        setLoadingProfissionais(false);
      }
    );
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    // Validações básicas
    if (!formData.servicoId || !formData.dataHora) {
      setError('Serviço e data/hora são obrigatórios');
      setLoading(false);
      return;
    }

    let requestData: CriarAgendamentoRequest;
    
    // Converter servico e data
    const servicoIdNum = parseInt(formData.servicoId);
    if (isNaN(servicoIdNum) || servicoIdNum <= 0) {
      setError('Serviço inválido selecionado');
      setLoading(false);
      return;
    }
    const dataHoraISO = `${formData.dataHora}:00`;

    if (isNewClientMode) {
        if (!formData.emailCliente) {
            setError('Email do cliente é obrigatório');
            setLoading(false);
            return;
        }
        // Se estiver criando novo, precisa dos outros dados caso não exista
        // Mas a lógica do backend é: se não achar email, tenta criar.
        // Então devemos enviar nome, cpf, telefone se o usuário preencheu.
        // Vamos exigir preenchimento se for novo cliente esperado
        if (!formData.nomeCliente || !formData.cpfCliente || !formData.telefoneCliente) {
             setError('Preencha Nome, CPF e Telefone para garantir o cadastro caso o cliente não exista.');
             setLoading(false);
             return;
        }

        console.log('=== DADOS DO NOVO CLIENTE ===');
        console.log('Nome:', formData.nomeCliente);
        console.log('Email:', formData.emailCliente);
        console.log('CPF:', formData.cpfCliente);
        console.log('Telefone:', formData.telefoneCliente);

        requestData = {
          servicoId: servicoIdNum,
          dataHora: dataHoraISO,
          profissionalId: formData.profissionalId ? parseInt(formData.profissionalId) : undefined,
          observacoes: formData.observacoes || undefined,
          emailCliente: formData.emailCliente,
          nomeCliente: formData.nomeCliente,
          cpfCliente: formData.cpfCliente,
          telefoneCliente: formData.telefoneCliente
        };

        console.log('=== REQUEST DATA ===', requestData);
    } else {
        if (!formData.clienteId) {
            setError('Selecione um cliente ou mude para busca por email');
            setLoading(false);
            return;
        }
        const clienteIdNum = parseInt(formData.clienteId);
        if (isNaN(clienteIdNum) || clienteIdNum <= 0) {
            setError('Cliente inválido selecionado');
            setLoading(false);
            return;
        }
        
        requestData = {
          clienteId: clienteIdNum,
          servicoId: servicoIdNum,
          dataHora: dataHoraISO,
          profissionalId: formData.profissionalId ? parseInt(formData.profissionalId) : undefined,
          observacoes: formData.observacoes || undefined,
        };
    }

    console.log('Enviando requisição de criação:', requestData);

    MainService.getInstance().criarAgendamento(
      requestData,
      (_response: AxiosResponse) => {
        onSuccess();
      },
      (error: AxiosError) => {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const message = (error.response?.data as any)?.message || 'Erro ao criar agendamento';
        setError(message);
        console.error('Erro ao criar agendamento:', error);
      },
      () => {
        setLoading(false);
      }
    );
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    console.log(`Campo alterado: ${name} = ${value}`);
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  // Gera data/hora mínima (agora + 2 horas)
  const getMinDateTime = () => {
    const now = new Date();
    now.setHours(now.getHours() + 2);
    return now.toISOString().slice(0, 16);
  };

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-dark-800 rounded-2xl border border-dark-600 shadow-2xl w-full max-w-2xl transform transition-all max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-dark-600 sticky top-0 bg-dark-800 z-10">
          <div className="flex items-center gap-3">
            <div className="bg-primary/10 p-2 rounded-lg">
              <span className="material-icons text-primary text-2xl">event</span>
            </div>
            <h2 className="text-2xl font-bold text-white">Novo Agendamento</h2>
          </div>
          <button
            onClick={onClose}
            className="p-2 hover:bg-dark-700 rounded-lg transition-colors group"
          >
            <span className="material-icons text-gray-400 group-hover:text-white">close</span>
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-5">
          {error && (
            <div className="bg-red-500/10 border border-red-500/30 rounded-lg p-4 flex items-center gap-2">
              <span className="material-icons text-red-400">error</span>
              <p className="text-red-400 text-sm">{error}</p>
            </div>
          )}

          {/* Seletor de Modo de Cliente (apenas para admin e se clienteId não foi passado) */}
          {!clienteId && (
            <div className="flex gap-4 mb-4">
                <button
                    type="button"
                    onClick={() => setIsNewClientMode(false)}
                    className={`flex-1 py-2 px-4 rounded-lg border transition-colors ${!isNewClientMode ? 'bg-primary text-white border-primary' : 'bg-dark-700 text-gray-400 border-dark-600 hover:bg-dark-600'}`}
                >
                    Selecionar Existente
                </button>
                <button
                    type="button"
                    onClick={() => setIsNewClientMode(true)}
                    className={`flex-1 py-2 px-4 rounded-lg border transition-colors ${isNewClientMode ? 'bg-primary text-white border-primary' : 'bg-dark-700 text-gray-400 border-dark-600 hover:bg-dark-600'}`}
                >
                    Buscar/Criar por Email
                </button>
            </div>
          )}

          {/* Cliente (Modo Seleção) */}
          {!clienteId && !isNewClientMode && (
            <div>
              <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
                <span className="material-icons text-lg">person</span>
                Cliente *
              </label>
              {loadingClientes ? (
                <div className="flex items-center gap-2 px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg">
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-primary"></div>
                  <span className="text-gray-400 text-sm">Carregando clientes...</span>
                </div>
              ) : (
                <>
                  <select
                    name="clienteId"
                    value={formData.clienteId}
                    onChange={handleChange}
                    required
                    disabled={clientes.length === 0}
                    className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    <option value="">{clientes.length === 0 ? 'Nenhum cliente cadastrado' : 'Selecione um cliente'}</option>
                    {clientes.map((cliente) => (
                      <option key={cliente.id} value={cliente.id}>
                        {cliente.nome} - {cliente.telefone}
                      </option>
                    ))}
                  </select>
                </>
              )}
            </div>
          )}
          
          {/* Cliente (Modo Email/Criação) */}
          {!clienteId && isNewClientMode && (
            <div className="space-y-4 border border-dark-600 p-4 rounded-lg bg-dark-700/30">
                <div>
                    <label className="block text-sm font-semibold text-gray-300 mb-2">Email do Cliente *</label>
                    <input
                        type="email"
                        name="emailCliente"
                        value={formData.emailCliente}
                        onChange={handleChange}
                        required
                        autoComplete="off"
                        className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                        placeholder="cliente@email.com"
                    />
                    <p className="text-xs text-gray-400 mt-1">Se o email não existir, um novo cliente será criado com os dados abaixo.</p>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-semibold text-gray-300 mb-2">Nome Completo *</label>
                        <input
                            type="text"
                            name="nomeCliente"
                            value={formData.nomeCliente}
                            onChange={handleChange}
                            required
                            autoComplete="off"
                            className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                            placeholder="Nome do Cliente"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-semibold text-gray-300 mb-2">CPF *</label>
                        <input
                            type="text"
                            name="cpfCliente"
                            value={formData.cpfCliente}
                            onChange={handleChange}
                            required
                            autoComplete="off"
                            className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                            placeholder="000.000.000-00"
                        />
                    </div>
                </div>
                <div>
                    <label className="block text-sm font-semibold text-gray-300 mb-2">Telefone *</label>
                    <input
                        type="text"
                        name="telefoneCliente"
                        value={formData.telefoneCliente}
                        onChange={handleChange}
                        autoComplete="off"
                        required
                        className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                        placeholder="(00) 00000-0000"
                    />
                </div>
            </div>
          )}

          {/* Serviço */}
          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">content_cut</span>
              Serviço *
            </label>
            {loadingServicos ? (
              <div className="flex items-center gap-2 px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg">
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-primary"></div>
                <span className="text-gray-400 text-sm">Carregando serviços...</span>
              </div>
            ) : (
              <>
                <select
                  name="servicoId"
                  value={formData.servicoId}
                  onChange={handleChange}
                  required
                  disabled={servicos.length === 0}
                  className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <option value="">{servicos.length === 0 ? 'Nenhum serviço disponível' : 'Selecione um serviço'}</option>
                  {servicos.map((servico, index) => {
                    const servicoId = typeof servico.id === 'object' ? servico.id.valor : servico.id;
                    return (
                      <option key={servicoId + '-' + index} value={servicoId}>
                        {servico.nome} - R$ {servico.preco.toFixed(2)} ({servico.duracaoMinutos} min)
                      </option>
                    );
                  })}
                </select>
                {servicos.length === 0 && !loadingServicos && (
                  <p className="text-xs text-yellow-400 mt-1 flex items-center gap-1">
                    <span className="material-icons text-sm">warning</span>
                    Nenhum serviço cadastrado. Cadastre serviços antes de criar agendamentos.
                  </p>
                )}
              </>
            )}
          </div>

          {/* Data e Hora */}
          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">schedule</span>
              Data e Hora *
            </label>
            <input
              type="datetime-local"
              name="dataHora"
              value={formData.dataHora}
              onChange={handleChange}
              min={getMinDateTime()}
              required
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
            />
            <p className="text-xs text-gray-500 mt-1">
              Agendamentos devem ser feitos com pelo menos 2 horas de antecedência
            </p>
          </div>

          {/* Profissional Disponível */}
          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">person</span>
              Profissional (opcional)
            </label>
            {loadingProfissionais ? (
              <div className="flex items-center gap-2 px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg">
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-primary"></div>
                <span className="text-gray-400 text-sm">Carregando profissionais disponíveis...</span>
              </div>
            ) : (
              <>
                <select
                  name="profissionalId"
                  value={formData.profissionalId}
                  onChange={handleChange}
                  disabled={!formData.servicoId || !formData.dataHora || profissionaisDisponiveis.length === 0}
                  className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <option value="">Sistema escolherá automaticamente</option>
                  {profissionaisDisponiveis.map((prof, index) => (
                    <option key={`prof-${prof.id}-${index}`} value={prof.id}>
                      {prof.nome} ({prof.senioridade})
                    </option>
                  ))}
                </select>
                {formData.servicoId && formData.dataHora && profissionaisDisponiveis.length === 0 && !loadingProfissionais && (
                  <p className="text-xs text-yellow-400 mt-1 flex items-center gap-1">
                    <span className="material-icons text-sm">warning</span>
                    Nenhum profissional disponível neste horário. O sistema alocará quando possível.
                  </p>
                )}
              </>
            )}
          </div>

          {/* Observações */}
          <div>
            <label className="block text-sm font-semibold text-gray-300 mb-2 flex items-center gap-2">
              <span className="material-icons text-lg">notes</span>
              Observações
            </label>
            <textarea
              name="observacoes"
              value={formData.observacoes}
              onChange={handleChange}
              rows={3}
              maxLength={500}
              className="w-full px-4 py-3 bg-dark-700 border border-dark-600 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent resize-none"
              placeholder="Informações adicionais sobre o agendamento..."
            />
            <p className="text-xs text-gray-500 mt-1 text-right">
              {formData.observacoes.length}/500 caracteres
            </p>
          </div>

          {/* Botões */}
          <div className="flex gap-3 pt-4 border-t border-dark-600">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-6 py-3 bg-dark-700 hover:bg-dark-600 text-white rounded-lg transition-colors font-medium"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 px-6 py-3 bg-gradient-to-r from-primary to-secondary text-white rounded-lg hover:shadow-lg hover:shadow-primary/50 transition-all font-semibold disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
            >
              {loading ? (
                <>
                  <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                  <span>Criando...</span>
                </>
              ) : (
                <>
                  <span className="material-icons">check</span>
                  <span>Criar Agendamento</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}