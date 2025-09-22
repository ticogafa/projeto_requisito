import React, { useState, useEffect } from 'react';
import LoadingSpinner from '@/components/LoadingSpinner/LoadingSpinner';

export interface Booking {
  id: string;
  serviceName: string;
  professionalName: string;
  date: string;
  startTime: string;
  endTime: string;
  duration: number;
  clientName: string;
  clientPhone: string;
  notes?: string;
  createdAt: string;
}

interface BookingListProps {
  bookings?: Booking[];
  onCancel: (bookingId: string) => void;
  onExportCalendar?: (booking: Booking) => void;
  clientId?: number;
}

export const BookingList: React.FC<BookingListProps> = ({
  bookings,
  onCancel,
  onExportCalendar,
  clientId,
}) => {
  const [apiBookings, setApiBookings] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(false);
  const [error] = useState<string | null>(null);

  // Placeholder for API implementation
  useEffect(() => {
    if (bookings) return; // Use prop bookings if provided
    
    setLoading(true);
    // Mock data for now
    setTimeout(() => {
      setApiBookings([]);
      setLoading(false);
    }, 500);
  }, [bookings, clientId]);

  const displayBookings = bookings || apiBookings;

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return <div className="subtle error">{error}</div>;
  }

  if (displayBookings.length === 0) {
    return <div className="subtle">Nenhum agendamento ainda.</div>;
  }

  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('pt-BR', {
      weekday: 'short',
      month: 'short',
      day: 'numeric'
    });
  };

  // Sort bookings by date and time
  const sortedBookings = [...displayBookings].sort((a, b) => {
    const dateCompare = a.date.localeCompare(b.date);
    if (dateCompare !== 0) return dateCompare;
    return a.startTime.localeCompare(b.startTime);
  });

  return (
    <div className="list">
      {sortedBookings.map((booking) => (
        <div key={booking.id} className="booking">
          <div>
            <div className="title">
              {booking.serviceName} com {booking.professionalName}
            </div>
            <div className="meta">
              {formatDate(booking.date)} • {booking.startTime}–{booking.endTime} • {booking.duration}m • {booking.clientName} ({booking.clientPhone})
              {booking.notes && ` • ${booking.notes}`}
            </div>
          </div>
          <div className="ops">
            {onExportCalendar && (
              <button 
                className="btn ghost"
                onClick={() => onExportCalendar(booking)}
                title="Adicionar ao calendário"
              >
                Calendário
              </button>
            )}
            <button 
              className="btn danger"
              onClick={() => onCancel(booking.id)}
              title="Cancelar agendamento"
            >
              Cancelar
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default BookingList;
