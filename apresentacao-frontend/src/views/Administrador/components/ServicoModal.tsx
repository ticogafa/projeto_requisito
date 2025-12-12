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

  const [categoria, setCategoria] = useState('');
  const [destaque, setDestaque] = useState('');
  const [servicoDependente, setServicoDependente] = useState(false);

  useEffect(() => {
    if (visible) {
      if (servicoParaEditar) {
        setNome(servicoParaEditar.nome);
        setPreco(servicoParaEditar.preco.toString());
        setDuracao(servicoParaEditar.duracaoMinutos.toString());
        setDescricao(servicoParaEditar.descricao || '');

        setCategoria(servicoParaEditar.categoria || '');
        setDestaque(servicoParaEditar.destaque || '');
        setServicoDependente(servicoParaEditar.servicoDependente || false);
      } else {

        setNome('');
        setPreco('');
        setDuracao('');
        setDescricao('');
        setCategoria('');
        setDestaque('');
        setServicoDependente(false);
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
      ativo: true,
      categoria,
      destaque,
      servicoDependente
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

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">Categoria</label>
              <input type="text" list="categorias" value={categoria} onChange={e => setCategoria(e.target.value)} className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" placeholder="Ex: Cabelo" />
              <datalist id="categorias">
                <option value="Cabelo" />
                <option value="Barba" />
                <option value="Tratamento" />
                <option value="Estética" />
              </datalist>
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Destaque</label>
              <select value={destaque} onChange={e => setDestaque(e.target.value)} className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none">
                <option value="">Nenhum</option>
                <option value="POPULAR">Popular</option>
                <option value="NOVO">Novo</option>
              </select>
            </div>
          </div>

          <div>
            <label className="flex items-center gap-3 cursor-pointer bg-dark-700 p-3 rounded-lg border border-dark-600 hover:bg-dark-600 transition">
              <input type="checkbox" checked={servicoDependente} onChange={e => setServicoDependente(e.target.checked)} className="accent-primary w-5 h-5" />
              <div className="flex flex-col">
                <span className="text-sm font-bold text-white">Serviço Adicional (Add-on)</span>
                <span className="text-xs text-gray-400">Marque se este serviço só pode ser agendado junto com outro.</span>
              </div>
            </label>
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Descrição</label>
            <input type="text" value={descricao} onChange={e => setDescricao(e.target.value)} className="w-full bg-dark-700 border border-dark-600 rounded-lg px-3 py-2 text-white focus:border-primary focus:outline-none" placeholder="Detalhes do serviço" />
          </div>

          <button type="submit" className="w-full bg-primary hover:bg-orange-600 text-white font-bold py-3 rounded-lg mt-4 transition">
            Salvar
          </button>
        </form>
      </div>
    </div>
  );
}
