import React, { useState, useEffect } from 'react';
import MainService from '@/services/MainService';
import { useLoadingStore } from '@/store/useLoadingStore';
import { toast } from 'react-toastify';
import { ServicoOferecido } from '@/interfaces/ServicoOferecidoInterface';
import { AxiosError } from 'axios';

interface ServicoModalProps {
  visible: boolean;
  servicoParaEditar: ServicoOferecido | null;
  closeModal: () => void;
  onSuccess: () => void;
}

export default function ServicoModal({ visible, servicoParaEditar, closeModal, onSuccess }: ServicoModalProps) {
  const { setLoading } = useLoadingStore();
  const mainService = MainService.getInstance();

  const [nome, setNome] = useState('');
  const [preco, setPreco] = useState('');
  const [duracao, setDuracao] = useState('');
  const [descricao, setDescricao] = useState('');

  useEffect(() => {
    if (visible) {
      if (servicoParaEditar) {
        setNome(servicoParaEditar.nome);
        setPreco(servicoParaEditar.preco.toString());
        setDuracao(servicoParaEditar.duracaoMinutos.toString());
        setDescricao(servicoParaEditar.descricao || '');
      } else {
        setNome('');
        setPreco('');
        setDuracao('');
        setDescricao('');
      }
    }
  }, [visible, servicoParaEditar]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    const precoNum = parseFloat(preco.replace(',', '.'));
    const duracaoNum = parseInt(duracao);

    const payload: any = {
      nome,
      preco: precoNum,
      duracaoMinutos: duracaoNum,
      descricao,
      ativo: true
    };

    const successCallback = () => {
      toast.success(`Serviço ${servicoParaEditar ? 'atualizado' : 'cadastrado'} com sucesso!`);
      onSuccess();
      closeModal();
    };

    const errorCallback = (error: AxiosError) => {
      const data = error.response?.data as { message?: string } | undefined;
      toast.error(data?.message || 'Erro ao salvar serviço. Verifique os dados.');
    };

    const doneCallback = () => setLoading(false);

    if (servicoParaEditar) {
      const idNumerico = typeof servicoParaEditar.id === 'object' ? (servicoParaEditar.id as any).valor : servicoParaEditar.id;

      payload.id = { valor: idNumerico };

      mainService.atualizarServico(idNumerico, payload, successCallback, errorCallback, doneCallback);
    } else {
      mainService.criarServico(payload, successCallback, errorCallback, doneCallback);
    }
  };

  if (!visible) return null;

  return (
    <div className="fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4" onClick={closeModal}>
      <div className="bg-dark-800 rounded-2xl p-8 max-w-md w-full border border-dark-600" onClick={e => e.stopPropagation()}>
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-2xl font-bold flex items-center gap-2">
            <span className="material-icons text-primary">{servicoParaEditar ? 'edit' : 'add_circle'}</span>
            {servicoParaEditar ? 'Editar Serviço' : 'Novo Serviço'}
          </h3>
          <button onClick={closeModal} className="text-gray-400 hover:text-white"><span className="material-icons">close</span></button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-1">Nome *</label>
            <input required type="text" value={nome} onChange={e => setNome(e.target.value)} className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" placeholder="Ex: Corte Degrade" />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">Preço (R$) *</label>
              <input required type="number" step="0.01" value={preco} onChange={e => setPreco(e.target.value)} className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" placeholder="0.00" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Duração (min) *</label>
              <input required type="number" value={duracao} onChange={e => setDuracao(e.target.value)} className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" placeholder="30" />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Descrição</label>
            <input type="text" value={descricao} onChange={e => setDescricao(e.target.value)} className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" placeholder="Ex: Categoria Cortes" />
          </div>

          <button type="submit" className="w-full bg-primary hover:bg-orange-600 text-white font-bold py-3 rounded-lg mt-4 transition">
            Salvar
          </button>
        </form>
      </div>
    </div>
  );
}
