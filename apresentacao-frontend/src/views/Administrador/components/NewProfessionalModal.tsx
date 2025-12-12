import React, { useState, useEffect } from 'react';
import MainService from '@/services/MainService';
import { useServicosOferecidos } from '@/hooks/useServicosOferecidos';
import { useLoadingStore } from '@/store/useLoadingStore';
import { toast } from 'react-toastify';
import { AxiosError } from 'axios';
import { registerSecondaryUser, saveUserRole } from '@/auth';

interface NewProfessionalModalProps {
  visible: boolean;
  closeModal: () => void;
  onSuccess: () => void;
  profissionalParaEditar?: any;
}

export default function NewProfessionalModal({ visible, closeModal, onSuccess, profissionalParaEditar }: NewProfessionalModalProps) {
  const { data: servicos } = useServicosOferecidos();
  const { setLoading } = useLoadingStore();
  const mainService = MainService.getInstance();

  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [cpf, setCpf] = useState('');
  const [telefone, setTelefone] = useState('');
  const [senha, setSenha] = useState('');
  const [senioridade, setSenioridade] = useState('JUNIOR');
  const [inicioJornada, setInicioJornada] = useState('08:00:00');
  const [fimJornada, setFimJornada] = useState('18:00:00');

  const [servicosSelecionados, setServicosSelecionados] = useState<number[]>([]);

  useEffect(() => {
    if (visible) {
      if (profissionalParaEditar) {

        setNome(profissionalParaEditar.nome);

        const emailVal = typeof profissionalParaEditar.email === 'object' ? profissionalParaEditar.email.value : profissionalParaEditar.email;
        const cpfVal = typeof profissionalParaEditar.cpf === 'object' ? profissionalParaEditar.cpf.value : profissionalParaEditar.cpf;
        const telVal = typeof profissionalParaEditar.telefone === 'object' ? profissionalParaEditar.telefone.value : profissionalParaEditar.telefone;

        setEmail(emailVal || '');
        setCpf(cpfVal || '');
        setTelefone(telVal || '');

        setSenioridade(profissionalParaEditar.senioridade || 'JUNIOR');

        if (profissionalParaEditar.agenda) {
          setInicioJornada(profissionalParaEditar.agenda.inicioJornada || '08:00:00');
          setFimJornada(profissionalParaEditar.agenda.fimJornada || '18:00:00');
        }

        if (profissionalParaEditar.servicoOferecidoIds) {
          const ids = profissionalParaEditar.servicoOferecidoIds.map((s: any) => s.valor || s);
          setServicosSelecionados(ids);
        }
      } else {

        setNome('');
        setEmail('');
        setCpf('');
        setTelefone('');
        setSenha('');
        setSenioridade('JUNIOR');
        setInicioJornada('08:00:00');
        setFimJornada('18:00:00');
        setServicosSelecionados([]);
      }
    }
  }, [visible, profissionalParaEditar]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    const payload = {
      nome,
      email,
      cpf,
      telefone,
      senioridade,
      ativo: true,
      agenda: {
        inicioJornada: inicioJornada.length === 5 ? inicioJornada + ':00' : inicioJornada,
        fimJornada: fimJornada.length === 5 ? fimJornada + ':00' : fimJornada
      },
      servicoOferecidoIds: servicosSelecionados.map(id => ({ valor: id }))
    };

    const successAction = () => {
      toast.success(`Profissional ${profissionalParaEditar ? 'atualizado' : 'cadastrado'} com sucesso!`);
      onSuccess();
      closeModal();
    };

    const errorAction = (error: AxiosError | any) => {
      const errorData = error.response?.data as { message?: string } | undefined;
      toast.error(errorData?.message || error.message || 'Erro ao salvar profissional');
    };

    const doneAction = () => setLoading(false);

    if (profissionalParaEditar) {
      const id = profissionalParaEditar.id?.valor || profissionalParaEditar.id;
      mainService.atualizarProfissional(id, payload, successAction, errorAction, doneAction);
    } else {
      try {
        if (!senha) {
          throw new Error("Senha é obrigatória para novos profissionais");
        }
        // 1. Create user in Firebase
        const userCredential = await registerSecondaryUser(email, senha);
        
        // 2. Save role in Firestore
        await saveUserRole(userCredential.user.uid, email, 'profissional', nome, cpf, telefone);

        // 3. Create professional in Backend
        mainService.criarProfissional(payload, successAction, errorAction, doneAction);
      } catch (error: any) {
        setLoading(false);
        toast.error(error.message || "Erro ao criar usuário no Firebase");
      }
    }
  };

  const toggleServico = (id: number) => {
    setServicosSelecionados(prev =>
      prev.includes(id) ? prev.filter(s => s !== id) : [...prev, id]
    );
  };

  if (!visible) return null;

  return (
    <div className="fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4" onClick={closeModal}>
      <div className="bg-dark-800 rounded-2xl p-8 max-w-2xl w-full border border-dark-600 max-h-[90vh] overflow-y-auto" onClick={e => e.stopPropagation()}>
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-2xl font-bold flex items-center gap-2">
            <span className="material-icons text-primary">{profissionalParaEditar ? 'edit' : 'person_add'}</span>
            {profissionalParaEditar ? 'Editar Profissional' : 'Novo Profissional'}
          </h3>
          <button onClick={closeModal} className="text-gray-400 hover:text-white">
            <span className="material-icons">close</span>
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">Nome *</label>
              <input required type="text" value={nome} onChange={e => setNome(e.target.value)}
                className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" />
            </div>

            {/* --- CAMPO CPF --- */}
            <div>
              <label className="block text-sm font-medium mb-1">CPF *</label>
              <input
                required
                type="text"
                value={cpf}
                onChange={e => setCpf(e.target.value)}
                placeholder="000.000.000-00"

                disabled={!!profissionalParaEditar}

                className={`w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none ${profissionalParaEditar ? 'opacity-50 cursor-not-allowed text-gray-400' : ''}`}
              />
            </div>
            {/* ---------------- */}
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">Email *</label>
              <input required type="email" value={email} onChange={e => setEmail(e.target.value)}
                className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Telefone *</label>
              <input required type="tel" value={telefone} onChange={e => setTelefone(e.target.value)}
                className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" />
            </div>
          </div>

          {!profissionalParaEditar && (
            <div>
              <label className="block text-sm font-medium mb-1">Senha *</label>
              <input required type="password" value={senha} onChange={e => setSenha(e.target.value)}
                className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" />
            </div>
          )}

          <div>
            <label className="block text-sm font-medium mb-1">Senioridade *</label>
            <select value={senioridade} onChange={e => setSenioridade(e.target.value)}
              className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none">
              <option value="JUNIOR">Júnior</option>
              <option value="PLENO">Pleno</option>
              <option value="SENIOR">Sênior</option>
            </select>
          </div>

          <div className="grid grid-cols-2 gap-4 bg-dark-700 p-3 rounded-lg border border-dark-600">
            <div>
              <label className="block text-xs text-gray-400 mb-1">Início Jornada</label>
              <input type="time" value={inicioJornada} onChange={e => setInicioJornada(e.target.value)}
                className="bg-transparent text-white font-mono focus:outline-none w-full" />
            </div>
            <div>
              <label className="block text-xs text-gray-400 mb-1">Fim Jornada</label>
              <input type="time" value={fimJornada} onChange={e => setFimJornada(e.target.value)}
                className="bg-transparent text-white font-mono focus:outline-none w-full" />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium mb-2">Serviços que realiza:</label>
            <div className="grid grid-cols-2 gap-2 max-h-32 overflow-y-auto p-2 bg-dark-700 rounded-lg">
              {servicos.map((servico: any) => {
                const rawId = servico.id?.valor ?? servico.id;
                const servicoId = Number(rawId);

                return (
                  <label key={servicoId} className="flex items-center gap-2 cursor-pointer hover:bg-dark-600 p-1 rounded">
                    <input type="checkbox"
                      checked={servicosSelecionados.includes(servicoId)}
                      onChange={() => toggleServico(servicoId)}
                      className="accent-primary"
                    />
                    <span className="text-sm">{servico.nome}</span>
                  </label>
                );
              })}
            </div>
          </div>

          <button type="submit" className="w-full bg-primary hover:bg-orange-600 text-white font-bold py-3 rounded-lg mt-4 transition">
            {profissionalParaEditar ? 'Salvar Alterações' : 'Salvar Profissional'}
          </button>
        </form>
      </div>
    </div>
  );
}
