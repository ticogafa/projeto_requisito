import React, { useState, useEffect } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { Header } from '@/components/Header/Header';
import { ProfessionalSelection } from '@/components/ProfessionalSelection/ProfessionalSelection';
import { ServiceSelection } from '@/components/ServiceSelection/ServiceSelection';
import Calendar from '@/components/Calendar/Calendar';
import LoadingSpinner from '@/components/LoadingSpinner/LoadingSpinner';
import apiClient from '@/services/api';
import type { Agendamento, Profissional, Servico, CriarAgendamentoRequest } from '@/types';

interface AgendamentoDraft {
  servicoId: number; 
  profissionalId: number;
  clienteId: number;
  dataHora: string;
  observacoes: string;
  // Campos para montagem do dataHora
  data: string;
  horario: string;
}

const Dashboard: React.FC = () => {
  const { currentUser, logout } = useAuth();
  const [loading, setLoading] = useState(false);
  const [servicosLoading, setServicosLoading] = useState(true);
  const [servicos, setServicos] = useState<Servico[]>([]);
  const [profissionais, setProfissionais] = useState<Profissional[]>([]);
  const [formData, setFormData] = useState<AgendamentoDraft>({
    servicoId: 0,
    profissionalId: 0,
    clienteId: 1, // Cliente fixo para simplificar
    dataHora: '',
    observacoes: '',
    data: '',
    horario: '',
  });
  const [mensagem, setMensagem] = useState<string>('');
  const [horarios, setHorarios] = useState<string[]>([]);
  const [profissionaisDisponiveis, setProfissionaisDisponiveis] = useState<Profissional[]>([]);
  const [profissionaisDisponiveisLoading, setProfissionaisDisponiveisLoading] = useState(false);

  // Carregar serviços da API
  useEffect(() => {
    setServicosLoading(true);
    
    const successCallback = (servicos: Servico[]) => {        
      setServicos(servicos);
      if (servicos.length > 0) {
        setFormData(prev => ({ ...prev, servicoId: servicos[0].id }));
      }
      setServicosLoading(false);
    }

    const errorCallback = (error: string) => {
      console.error('Falha ao buscar serviços:', error);
      setServicos([]);
      setServicosLoading(false);
    }

    apiClient.getServices(successCallback, errorCallback);
  }, []);

  // Carregar profissionais da API
  useEffect(() => {
    setProfissionaisLoading(true);

    const successCallback = (response: Profissional[]) => {
      setProfissionais(response);
      setProfissionaisLoading(false);
    };

    const errorCallback = (error: string) => {
      console.error('Falha ao buscar profissionais:', error);
      setProfissionais([]);
      setProfissionaisLoading(false);
    };

    apiClient.getProfessionals(successCallback, errorCallback);
  }, []);

  // Buscar horários disponíveis quando a data ou serviço mudar
  useEffect(() => {
    if (!formData.data || !formData.servicoId) return;
    
    setHorarioLoading(true);
    setHorarios([]);
    
    apiClient.getAvailableTimeSlots(
      formData.data,
      formData.servicoId,
      (horarios) => {
        setHorarios(horarios);
        setHorarioLoading(false);
      },
      (error) => {
        console.error('Falha ao buscar horários disponíveis:', error);
        setHorarios([]);
        setHorarioLoading(false);
      }
    );
  }, [formData.data, formData.servicoId]);

  // Buscar profissionais disponíveis quando a data, horário ou serviço mudar
  useEffect(() => {
    if (!formData.data || !formData.horario || !formData.servicoId) return;
    
    setProfissionaisDisponiveisLoading(true);
    setProfissionaisDisponiveis([]);
    
    apiClient.getAvailableProfessionals(
      formData.data,
      formData.horario,
      formData.servicoId,
      (profissionais) => {
        setProfissionaisDisponiveis(profissionais);
        if (profissionais.length > 0) {
          // Seleciona o primeiro profissional disponível por padrão
          setFormData(prev => ({ ...prev, profissionalId: Number(profissionais[0].id) }));
        }
        setProfissionaisDisponiveisLoading(false);
      },
      (error) => {
        console.error('Falha ao buscar profissionais disponíveis:', error);
        setProfissionaisDisponiveis([]);
        setProfissionaisDisponiveisLoading(false);
      }
    );
  }, [formData.data, formData.horario, formData.servicoId]);

  const selectedServico = servicos.find(s => s.id === formData.servicoId);
  const selectedProfissional = profissionais.find(p => Number(p.id) === formData.profissionalId);

  const handleServicoSelect = (servicoId: number) => {
    setFormData(prev => ({ ...prev, servicoId, horario: '' }));
  };

  const handleProfissionalSelect = (profissionalId: string) => {
    setFormData(prev => ({ ...prev, profissionalId: parseInt(profissionalId) }));
  };

  const handleDateChange = (data: string) => {
    setFormData(prev => ({ ...prev, data, horario: '' }));
  };

  const handleTimeSelect = (horario: string) => {
    setFormData(prev => ({ ...prev, horario }));
  };

  const handleObservacoesChange = (observacoes: string) => {
    setFormData(prev => ({ ...prev, observacoes }));
  };

  const calculateEndTime = (startTime: string, duration: number): string => {
    const [hours, minutes] = startTime.split(':').map(Number);
    const totalMinutes = hours * 60 + minutes + duration;
    const endHours = Math.floor(totalMinutes / 60);
    const endMins = totalMinutes % 60;
    return `${endHours.toString().padStart(2, '0')}:${endMins.toString().padStart(2, '0')}`;
  };

  const isFormValid = (): boolean => {
    return !!(
      formData.servicoId && 
      formData.profissionalId && 
      formData.data && 
      formData.horario
    );
  }

  const handleAgendamento = () => {
    if (!isFormValid() || !selectedServico || !selectedProfissional) {
      setMensagem('Por favor, preencha todos os campos obrigatórios.');
      return;
    }

    setLoading(true);
    setMensagem('');
    
    // Formatar data e hora para o formato ISO
    const dataHora = `${formData.data}T${formData.horario}:00`;
    
    // Criar objeto de requisição para a API
    const agendamentoRequest: CriarAgendamentoRequest = {
      dataHora,
      clienteId: formData.clienteId,
      profissionalId: formData.profissionalId,
      servicoId: formData.servicoId,
      observacoes: formData.observacoes
    };
    
    // Chamar API para criar o agendamento
    const successCallback = (agendamento: Agendamento) => {
      setAgendamentos(prev => [...prev, agendamento]);
      
      // Exibir mensagem de sucesso
      setMensagem(`Agendamento criado com sucesso para ${new Date(agendamento.dataHora).toLocaleString('pt-BR')}`);
      
      // Resetar formulário
      setFormData({
        servicoId: selectedServico.id,
        profissionalId: 0,
        clienteId: 1,
        dataHora: '',
        observacoes: '',
        data: '',
        horario: '',
      });
      
      setLoading(false);
    };

    const errorCallback = (error: string) => {
      setMensagem(`Erro ao criar agendamento: ${error || 'Tente novamente mais tarde'}`);
      setLoading(false);
    };

    apiClient.createAppointment(agendamentoRequest, successCallback, errorCallback);
  };

  const handleClearForm = () => {
    if (confirm('Limpar todos os campos e seleções?')) {
      setFormData({
        servicoId: servicos.length > 0 ? servicos[0].id : 0,
        profissionalId: 0,
        clienteId: 1,
        dataHora: '',
        observacoes: '',
        data: '',
        horario: '',
      });
      setMensagem('');
    }
  };

  return (
    <div className="app">
      <Header
        userName={currentUser?.displayName || currentUser?.email || 'Usuário'} 
        onLogout={logout}
      />

      <main className="grid">
        <section className="card" aria-label="Formulário de agendamento">
          <div className="section">
            <h1>Agendar Serviço</h1>
            {mensagem && (
              <div className={mensagem.includes('sucesso') ? 'success-message' : 'error-message'}>
                {mensagem}
              </div>
            )}
          </div>

          <div className="section">
            <h2>1. Escolha um serviço</h2>
            {servicosLoading ? (
              <LoadingSpinner />
            ) : (
              <ServiceSelection
                services={servicos.map(s => ({ 
                  id: s.id, 
                  name: s.nome, 
                  duration: s.duracaoMinutos,
                  price: s.preco 
                }))}
                selectedService={formData.servicoId}
                onSelect={handleServicoSelect}
              />
            )}
          </div>

          <div className="section">
            <h2>2. Selecione a data e horário</h2>
            <div className="inputs">
              <Calendar
                selectedDate={formData.data}
                selectedTime={formData.horario}
                onDateChange={handleDateChange}
                onTimeSelect={handleTimeSelect}
                serviceDuration={selectedServico?.duracaoMinutos || 30}
                professionalId={'0'} // Passamos 0 pois vamos usar os horários da API
                bookedSlots={horarios.length > 0 ? horarios : []}
              />
            </div>
          </div>

          <div className="section">
            <h2>3. Escolha um profissional</h2>
            {profissionaisDisponiveisLoading ? (
              <LoadingSpinner />
            ) : profissionaisDisponiveis.length > 0 ? (
              <ProfessionalSelection
                professionals={profissionaisDisponiveis}
                selectedProfessional={formData.profissionalId.toString()}
                onSelect={handleProfissionalSelect}
              />
            ) : (
              <div className="empty-state">
                {formData.data && formData.horario 
                  ? "Nenhum profissional disponível neste horário. Por favor, escolha outro horário." 
                  : "Selecione uma data e horário para ver os profissionais disponíveis."}
              </div>
            )}
          </div>

          <div className="section">
            <h2>4. Observações (opcional)</h2>
            <div className="field">
              <textarea
                id="observacoes"
                placeholder="Informe detalhes adicionais se necessário"
                value={formData.observacoes}
                onChange={(e) => handleObservacoesChange(e.target.value)}
                rows={3}
              />
            </div>
          </div>

          <div className="section">
            <h2>Resumo do agendamento</h2>
            <div className="summary">
              <div>Serviço: {selectedServico?.nome || '—'} 
                {selectedServico && ` (${selectedServico.duracaoMinutos}min - R$ ${selectedServico.preco.toFixed(2)})`}
              </div>
              <div>Data: {formData.data 
                ? new Date(formData.data).toLocaleDateString('pt-BR', { 
                    weekday: 'long', 
                    year: 'numeric', 
                    month: 'long', 
                    day: 'numeric' 
                  }) 
                : '—'}
              </div>
              <div>Horário: {formData.horario 
                ? `${formData.horario} – ${calculateEndTime(formData.horario, selectedServico?.duracaoMinutos || 30)}` 
                : '—'}
              </div>
              <div>Profissional: {selectedProfissional?.nome || '—'}</div>
            </div>
            
            <div className="actions">
              <button 
                className="btn"
                disabled={!isFormValid() || loading}
                onClick={handleAgendamento}
              >
                {loading ? <LoadingSpinner /> : 'Confirmar agendamento'}
              </button>
              <button 
                className="btn secondary"
                onClick={handleClearForm}
                disabled={loading}
              >
                Limpar
              </button>
            </div>
          </div>
        </section>
      </main>

      <footer style={{ marginTop: '22px', color: 'var(--text-dim)', fontSize: '12px', textAlign: 'center' }}>
        Sistema de Barbearia • Versão 1.0 • {new Date().getFullYear()}
      </footer>
    </div>
  );
};

export default Dashboard;
