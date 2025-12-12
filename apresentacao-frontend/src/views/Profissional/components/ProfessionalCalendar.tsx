import { useState, useMemo } from 'react';
import { AgendamentoInterface } from '@/interfaces/AgendamentoInterface';

interface ProfessionalCalendarProps {
  agendamentos: AgendamentoInterface[];
  onStart: (id: number) => void;
  onFinish: (id: number) => void;
  onCancel: (id: number) => void;
  onAppointmentClick: (appointment: AgendamentoInterface) => void; // New prop
}

type ViewMode = 'day' | 'week' | 'month';

const HOURS = Array.from({ length: 13 }, (_, i) => i + 8); // 08:00 to 20:00
const DAYS_OF_WEEK = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];
const MONTHS = [
  'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
  'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
];

export default function ProfessionalCalendar({ agendamentos, onAppointmentClick }: ProfessionalCalendarProps) {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [viewMode, setViewMode] = useState<ViewMode>('week');

  // Helpers
  const getStartOfWeek = (date: Date) => {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day; // Adjust when day is Sunday
    return new Date(d.setDate(diff));
  };

  const addDays = (date: Date, days: number) => {
    const result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
  };

  const isSameDay = (d1: Date, d2: Date) => {
    return (
      d1.getDate() === d2.getDate() &&
      d1.getMonth() === d2.getMonth() &&
      d1.getFullYear() === d2.getFullYear()
    );
  };

  // Generate days for the current view
  const viewDays = useMemo(() => {
    if (viewMode === 'day') return [currentDate];
    
    if (viewMode === 'week') {
      const start = getStartOfWeek(currentDate);
      return Array.from({ length: 7 }, (_, i) => addDays(start, i));
    }

    // Month view logic (simplified for grid)
    if (viewMode === 'month') {
      const year = currentDate.getFullYear();
      const month = currentDate.getMonth();
      const firstDay = new Date(year, month, 1);
      const lastDay = new Date(year, month + 1, 0);
      
      const days = [];
      // Pad start
      let startDayOffset = firstDay.getDay();
      // Ensure weeks start on Sunday (0)
      for (let i = 0; i < startDayOffset; i++) {
        days.unshift(addDays(firstDay, -(i + 1)));
      }
      // Actual days
      for (let i = 1; i <= lastDay.getDate(); i++) {
        days.push(new Date(year, month, i));
      }
      // Pad end to fill up to 6 weeks (42 days)
      while (days.length < 42) {
        days.push(addDays(days[days.length - 1], 1));
      }

      return days;
    }
    return [];
  }, [currentDate, viewMode]);

  // Filter agendamentos for visible days
  const getAgendamentosForDay = (date: Date) => {
    return agendamentos.filter(a => {
      const aDate = new Date(a.dataHora);
      return isSameDay(aDate, date);
    });
  };

  const handlePrev = () => {
    const newDate = new Date(currentDate);
    if (viewMode === 'day') newDate.setDate(newDate.getDate() - 1);
    if (viewMode === 'week') newDate.setDate(newDate.getDate() - 7);
    if (viewMode === 'month') newDate.setMonth(newDate.getMonth() - 1);
    setCurrentDate(newDate);
  };

  const handleNext = () => {
    const newDate = new Date(currentDate);
    if (viewMode === 'day') newDate.setDate(newDate.getDate() + 1);
    if (viewMode === 'week') newDate.setDate(newDate.getDate() + 7);
    if (viewMode === 'month') newDate.setMonth(newDate.getMonth() + 1);
    setCurrentDate(newDate);
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'CONFIRMADO': return 'bg-blue-600 border-blue-700';
      case 'PENDENTE': return 'bg-yellow-600 border-yellow-700';
      case 'EM_ANDAMENTO': return 'bg-purple-600 border-purple-700';
      case 'CONCLUIDO': return 'bg-green-600 border-green-700';
      case 'CANCELADO': return 'bg-red-600 border-red-700';
      default: return 'bg-gray-600 border-gray-700';
    }
  };

  // Renderers
  const renderHeader = () => (
    <div className="flex flex-col md:flex-row justify-between items-center mb-6 gap-4">
      <div className="flex items-center gap-4">
        <h2 className="text-2xl font-bold capitalize">
          {MONTHS[currentDate.getMonth()]} {currentDate.getFullYear()}
        </h2>
        <div className="flex bg-dark-700 rounded-lg p-1">
          <button onClick={handlePrev} className="p-1 hover:bg-dark-600 rounded">
            <span className="material-icons">chevron_left</span>
          </button>
          <button onClick={() => setCurrentDate(new Date())} className="px-3 text-sm font-medium hover:bg-dark-600 rounded">
            Hoje
          </button>
          <button onClick={handleNext} className="p-1 hover:bg-dark-600 rounded">
            <span className="material-icons">chevron_right</span>
          </button>
        </div>
      </div>

      <div className="flex bg-dark-700 rounded-lg p-1">
        {(['day', 'week', 'month'] as ViewMode[]).map((mode) => (
          <button
            key={mode}
            onClick={() => setViewMode(mode)}
            className={`px-4 py-2 rounded-md text-sm font-medium transition ${
              viewMode === mode ? 'bg-primary text-white shadow-sm' : 'text-gray-400 hover:text-white'
            }`}
          >
            {mode === 'day' ? 'Dia' : mode === 'week' ? 'Semana' : 'Mês'}
          </button>
        ))}
      </div>
    </div>
  );

  const renderEventBlock = (agendamento: AgendamentoInterface) => {
    const date = new Date(agendamento.dataHora);
    let startHour = date.getHours();
    let startMin = date.getMinutes();
    const firstVisibleHour = HOURS[0]; // Assuming HOURS[0] is 8

    let top = ((startHour - firstVisibleHour) * 70) + startMin;
    let height = 70; // Assuming 1 hour duration for now, using 70px per hour

    // Adjust if event starts before the visible time range (e.g., 7:30 AM when view starts at 8 AM)
    if (startHour < firstVisibleHour) {
      const minutesBeforeVisible = ((firstVisibleHour - startHour) * 60) - startMin;
      // Calculate how many pixels the event would extend above the visible area
      const pixelsPerMinute = 70 / 60; // 70px per hour, so 70px / 60 minutes
      const pixelsAboveVisible = minutesBeforeVisible * pixelsPerMinute;
      
      top = 0; // The event should visually start at the top of the visible area
      height = Math.max(0, height - pixelsAboveVisible); // Reduce height by the amount clipped
    }

    return (
      <div
        key={agendamento.id}
        className={`absolute left-1 right-1 p-1 flex flex-col gap-0.5 rounded text-xs border-l-4 overflow-hidden cursor-pointer hover:z-50 hover:!h-auto hover:shadow-xl transition-all duration-200 ${getStatusColor(agendamento.status)}`}
        style={{ top: `${top}px`, height: `${height}px` }}
        title={`${agendamento.servicoNome} - ${agendamento.clienteNome || 'Cliente'}`}
        onClick={() => onAppointmentClick(agendamento)}
      >
        <div className="font-bold leading-tight truncate">{new Date(agendamento.dataHora).toLocaleTimeString('pt-BR', {hour: '2-digit', minute:'2-digit'})}</div>
        <div className="font-semibold leading-tight truncate">{agendamento.clienteNome || 'Cliente'}</div>
        <div className="truncate opacity-75 leading-tight">{agendamento.servicoNome}</div>
      </div>
    );
  };

  const renderWeekView = () => (
    <div className="overflow-x-auto"> {/* Keep this for horizontal scroll on small screens */}
      <div className="min-w-[700px] lg:min-w-full"> {/* Adjust min-width or remove for full flexibility */}
        {/* Header Row */}
        <div className="grid grid-cols-[60px_repeat(7,minmax(0,1fr))] border-b border-dark-600">
          <div className="p-2 border-r border-dark-600 text-gray-400 font-medium text-center text-sm sticky left-0 bg-dark-800 z-10">Hora</div>
          {viewDays.map((day, i) => (
            <div key={i} className={`p-2 border-r border-dark-600 text-center ${isSameDay(day, new Date()) ? 'bg-primary/10' : ''}`}>
              <div className="text-xs text-gray-400 uppercase">{DAYS_OF_WEEK[day.getDay()]}</div>
              <div className={`text-lg font-bold ${isSameDay(day, new Date()) ? 'text-primary' : ''}`}>
                {day.getDate()}
              </div>
            </div>
          ))}
        </div>

        {/* Body */}
        <div className="relative grid grid-cols-[60px_repeat(7,minmax(0,1fr))] h-[910px] overflow-y-auto"> {/* 13 hours * 70px */}
          {/* Time Column */}
          <div className="border-r border-dark-600 sticky left-0 bg-dark-800 z-10">
            {HOURS.map(hour => (
              <div key={hour} className="h-[70px] border-b border-dark-700 text-xs text-gray-500 p-2 text-right">
                {hour}:00
              </div>
            ))}
          </div>

          {/* Day Columns */}
          {viewDays.map((day, i) => (
            <div key={i} className="relative border-r border-dark-600 bg-dark-800/50">
               {/* Grid lines */}
               {HOURS.map(hour => (
                <div key={hour} className="h-[70px] border-b border-dark-700/50"></div>
              ))}
              
              {/* Events */}
              {getAgendamentosForDay(day).map(renderEventBlock)}
            </div>
          ))}
        </div>
      </div>
    </div>
  );

  const renderMonthView = () => {
    // Basic month grid implementation
    return (
        <div className="grid grid-cols-7 gap-px bg-dark-600 rounded-lg overflow-hidden">
             {DAYS_OF_WEEK.map(d => (
                 <div key={d} className="bg-dark-700 p-2 text-center text-sm font-semibold text-gray-400">
                     {d}
                 </div>
             ))}
             {/* We need to properly generate days for the grid including padding */}
             {(() => {
                 const year = currentDate.getFullYear();
                 const month = currentDate.getMonth();
                 const firstDay = new Date(year, month, 1);
                 
                 const days = [];
                 let startDate = getStartOfWeek(firstDay); // Adjust to start of week for the first day of the month
                 // Generate 6 weeks to cover all possibilities for month view
                 for(let i=0; i<42; i++) {
                     days.push(addDays(startDate, i));
                 }
                 
                 return days.map((day, idx) => {
                     const isCurrentMonth = day.getMonth() === month;
                     const dayAgendamentos = getAgendamentosForDay(day);
                     
                     return (
                         <div key={idx} className={`min-h-[60px] bg-dark-800 p-1 md:p-2 ${!isCurrentMonth ? 'opacity-50' : ''} hover:bg-dark-700 transition`}>
                             <div className={`text-right mb-1 text-xs md:text-sm ${isSameDay(day, new Date()) ? 'text-primary font-bold' : 'text-gray-400'}`}>
                                 {day.getDate()}
                             </div>
                             <div className="space-y-0.5">
                                 {dayAgendamentos.slice(0, 2).map(a => ( // Show less on month view
                                     <div key={a.id} 
                                          className={`text-xs p-1 rounded truncate ${getStatusColor(a.status)} text-white cursor-pointer`}
                                          onClick={(e) => { e.stopPropagation(); onAppointmentClick(a); }} // Stop propagation for day click
                                     >
                                         {new Date(a.dataHora).toLocaleTimeString('pt-BR', {hour:'2-digit', minute:'2-digit'})} {a.servicoNome}
                                     </div>
                                 ))}
                                 {dayAgendamentos.length > 2 && ( // Changed from 3 to 2 for smaller cells
                                     <div className="text-xs text-center text-gray-500">
                                         + {dayAgendamentos.length - 2} mais
                                     </div>
                                 )}
                             </div>
                         </div>
                     );
                 });
             })()}
        </div>
    );
  }

  return (
    <div className="bg-dark-800 rounded-xl border border-dark-600 p-6">
      {renderHeader()}
      {viewMode === 'month' ? renderMonthView() : renderWeekView()}
    </div>
  );
}
