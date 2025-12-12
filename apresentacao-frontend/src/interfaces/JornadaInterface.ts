export interface JornadaDto {
    diaSemana: string;
    horaInicio: string;
    horaFim: string;
    intervaloInicio?: string;
    intervaloFim?: string;
    ativo: boolean;
}

export const DIAS_SEMANA = [
    { value: 'SEGUNDA', label: 'Segunda-feira' },
    { value: 'TERCA', label: 'Terça-feira' },
    { value: 'QUARTA', label: 'Quarta-feira' },
    { value: 'QUINTA', label: 'Quinta-feira' },
    { value: 'SEXTA', label: 'Sexta-feira' },
    { value: 'SABADO', label: 'Sábado' },
    { value: 'DOMINGO', label: 'Domingo' }
];
