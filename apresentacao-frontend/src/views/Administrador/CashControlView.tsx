import React, { useState, useEffect } from 'react';
import { Caixa } from '@/interfaces/Caixa';
import MainService from '@/services/MainService';

const CashControlView: React.FC = () => {
    const [lancamentos, setLancamentos] = useState<Caixa[]>([]);
    const [descricao, setDescricao] = useState('');
    const [valor, setValor] = useState('');
    const [tipo, setTipo] = useState<'ENTRADA' | 'SAIDA'>('ENTRADA');
    const mainService = MainService.getInstance();

    useEffect(() => {
        loadLancamentos();
    }, []);

    const loadLancamentos = () => {
        mainService.getLancamentos(
            (response) => {
                setLancamentos(response.data);
            },
            (error) => {
                console.error('Erro ao buscar lançamentos:', error);
            },
            () => {}
        );
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        const novoLancamento = {
            descricao,
            valor: parseFloat(valor),
            tipo,
        };
        mainService.addLancamento(
            novoLancamento,
            () => {
                loadLancamentos();
                setDescricao('');
                setValor('');
            },
            (error) => {
                console.error('Erro ao adicionar lançamento:', error);
            }
        );
    };

    const totalEntradas = lancamentos
        .filter((l) => l.tipo === 'ENTRADA')
        .reduce((acc, l) => acc + l.valor, 0);

    const totalSaidas = lancamentos
        .filter((l) => l.tipo === 'SAIDA')
        .reduce((acc, s) => acc + s.valor, 0);

    const saldo = totalEntradas - totalSaidas;

    return (
        <div className="p-6">
            <h1 className="text-3xl font-bold text-white mb-6">Controle de Caixa</h1>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
                <div className="bg-gray-800 p-4 rounded-lg">
                    <h2 className="text-lg font-bold text-white">Entradas</h2>
                    <p className="text-2xl text-green-400">R$ {totalEntradas.toFixed(2)}</p>
                </div>
                <div className="bg-gray-800 p-4 rounded-lg">
                    <h2 className="text-lg font-bold text-white">Saídas</h2>
                    <p className="text-2xl text-red-400">R$ {totalSaidas.toFixed(2)}</p>
                </div>
                <div className="bg-gray-800 p-4 rounded-lg">
                    <h2 className="text-lg font-bold text-white">Saldo</h2>
                    <p className={`text-2xl ${saldo >= 0 ? 'text-green-400' : 'text-red-400'}`}>R$ {saldo.toFixed(2)}</p>
                </div>
            </div>
            <div className="bg-gray-800 p-6 rounded-lg">
                <h2 className="text-2xl font-bold text-white mb-4">Novo Lançamento</h2>
                <form onSubmit={handleSubmit}>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                        <input
                            type="text"
                            placeholder="Descrição"
                            value={descricao}
                            onChange={(e) => setDescricao(e.target.value)}
                            className="bg-gray-700 text-white p-2 rounded-lg"
                        />
                        <input
                            type="number"
                            placeholder="Valor"
                            value={valor}
                            onChange={(e) => setValor(e.target.value)}
                            className="bg-gray-700 text-white p-2 rounded-lg"
                        />
                        <select
                            value={tipo}
                            onChange={(e) => setTipo(e.target.value as 'ENTRADA' | 'SAIDA')}
                            className="bg-gray-700 text-white p-2 rounded-lg"
                        >
                            <option value="ENTRADA">Entrada</option>
                            <option value="SAIDA">Saída</option>
                        </select>
                    </div>
                    <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded-lg">Adicionar</button>
                </form>
            </div>
            <div className="mt-6">
                <h2 className="text-2xl font-bold text-white mb-4">Histórico de Lançamentos</h2>
                <table className="w-full text-white">
                    <thead>
                        <tr>
                            <th className="text-left p-2">Data</th>
                            <th className="text-left p-2">Descrição</th>
                            <th className="text-left p-2">Tipo</th>
                            <th className="text-left p-2">Valor</th>
                        </tr>
                    </thead>
                    <tbody>
                        {lancamentos.map((lancamento) => (
                            <tr key={lancamento.id}>
                                <td className="p-2">{new Date(lancamento.data).toLocaleDateString()}</td>
                                <td className="p-2">{lancamento.descricao}</td>
                                <td className={`p-2 ${lancamento.tipo === 'ENTRADA' ? 'text-green-400' : 'text-red-400'}`}>
                                    {lancamento.tipo}
                                </td>
                                <td className="p-2">R$ {lancamento.valor.toFixed(2)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default CashControlView;
