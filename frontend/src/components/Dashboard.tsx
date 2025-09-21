import React, { useState, useEffect } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { Header } from '@/components/Header/Header';
import { ProfessionalSelection } from '@/components/ProfessionalSelection/ProfessionalSelection';
import { BookingList } from '@/components/BookingList/BookingList';
import { ServiceSelection } from '@/components/ServiceSelection/ServiceSelection';
import Calendar from '@/components/Calendar/Calendar';
import LoadingSpinner from '@/components/LoadingSpinner/LoadingSpinner';
import type { Service } from '@/components/ServiceSelection/ServiceSelection';
import type { Professional } from '@/components/ProfessionalSelection/ProfessionalSelection';
import type { Booking } from '@/components/BookingList/BookingList';
import type { ServicoResponse } from '@/types';
import apiClient from '@/services/api';

const PROFESSIONALS: Professional[] = [
  { id: '1', name: 'Ana', role: 'Especialista em degradê' },
  { id: '2', name: 'Bruno', role: 'Cortes clássicos' },
  { id: '3', name: 'Carlos', role: 'Mestre da barba' },
];

interface BookingFormData {
  service: number; // Changed from string to number
  professional: string;
  date: string;
  time: string;
  clientName: string;
  clientPhone: string;
  notes: string;
}

const Dashboard: React.FC = () => {
  const { currentUser, logout } = useAuth();
  const [loading, setLoading] = useState(false);
  const [servicesLoading, setServicesLoading] = useState(true);
  const [services, setServices] = useState<Service[]>([]);
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [formData, setFormData] = useState<BookingFormData>({
    service: 0, // Changed from empty string to 0
    professional: PROFESSIONALS[0].id,
    date: new Date().toISOString().split('T')[0],
    time: '',
    clientName: '',
    clientPhone: '',
    notes: '',
  });

  // Load services from API
  useEffect(() => {
    const fetchServices = async () => {
      try {
        setServicesLoading(true);
        const response = await apiClient.getServices();
        if (response.success && response.data) {
          // Map backend Servico to frontend Service format
          const mappedServices: Service[] = response.data.map(servico => ({
            id: servico.id,
            name: servico.nome,
            duration: servico.duracaoMinutos,
            price: servico.preco
          }));
          
          setServices(mappedServices);
          // Set default service after loading
          if (mappedServices.length > 0) {
            setFormData(prev => ({ ...prev, service: mappedServices[0].id }));
          }
        } else {
          console.error('Failed to fetch services:', response.error);
          // Fallback to mock data or show error
          setServices([]);
        }
      } catch (error) {
        console.error('Error fetching services:', error);
        setServices([]);
      } finally {
        setServicesLoading(false);
      }
    };

    fetchServices();
  }, []);

  // Load bookings from localStorage on mount
  useEffect(() => {
    const savedBookings = localStorage.getItem('barbeariaBookings');
    if (savedBookings) {
      try {
        setBookings(JSON.parse(savedBookings));
      } catch (error) {
        console.error('Error loading bookings:', error);
      }
    }
  }, []);

  // Save bookings to localStorage whenever bookings change
  useEffect(() => {
    localStorage.setItem('barbeariaBookings', JSON.stringify(bookings));
  }, [bookings]);

  const selectedService = services.find(s => s.id === formData.service);
  const selectedProfessional = PROFESSIONALS.find(p => p.id === formData.professional);

  const handleServiceSelect = (serviceId: number) => {
    setFormData(prev => ({ ...prev, service: serviceId, time: '' }));
  };

  const handleProfessionalSelect = (professionalId: string) => {
    setFormData(prev => ({ ...prev, professional: professionalId, time: '' }));
  };

  const handleDateChange = (date: string) => {
    setFormData(prev => ({ ...prev, date, time: '' }));
  };

  const handleTimeSelect = (time: string) => {
    setFormData(prev => ({ ...prev, time }));
  };

  const handleInputChange = (field: keyof BookingFormData, value: string) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  // Get booked time slots for the selected date and professional
  const getBookedSlots = (): string[] => {
    return bookings
      .filter(booking => 
        booking.date === formData.date && 
        booking.professionalName === selectedProfessional?.name
      )
      .map(booking => booking.startTime);
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
      formData.service &&
      formData.professional &&
      formData.date &&
      formData.time &&
      formData.clientName.trim() &&
      formData.clientPhone.trim()
    );
  };

  const handleBooking = async () => {
    if (!isFormValid() || !selectedService || !selectedProfessional) return;

    setLoading(true);
    
    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      const newBooking: Booking = {
        id: Math.random().toString(36).substr(2, 9),
        serviceName: selectedService.name,
        professionalName: selectedProfessional.name,
        date: formData.date,
        startTime: formData.time,
        endTime: calculateEndTime(formData.time, selectedService.duration),
        duration: selectedService.duration,
        clientName: formData.clientName,
        clientPhone: formData.clientPhone,
        notes: formData.notes,
        createdAt: new Date().toISOString(),
      };

      setBookings(prev => [...prev, newBooking]);
      
      // Reset form
      setFormData(prev => ({
        ...prev,
        time: '',
        clientName: '',
        clientPhone: '',
        notes: '',
      }));

      // Generate and download ICS file
      generateICSFile(newBooking);
      
    } catch (error) {
      console.error('Error creating booking:', error);
      alert('Erro ao criar agendamento. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  const handleCancelBooking = (bookingId: string) => {
    if (confirm('Cancelar este agendamento?')) {
      setBookings(prev => prev.filter(booking => booking.id !== bookingId));
    }
  };

  const generateICSFile = (booking: Booking) => {
    const startDateTime = new Date(`${booking.date}T${booking.startTime}:00`);
    const endDateTime = new Date(`${booking.date}T${booking.endTime}:00`);
    
    const formatICSDate = (date: Date): string => {
      return date.toISOString().replace(/[-:]/g, '').replace(/\.\d{3}/, '');
    };

    const icsContent = [
      'BEGIN:VCALENDAR',
      'VERSION:2.0',
      'PRODID:-//Barber Booker//EN',
      'CALSCALE:GREGORIAN',
      'METHOD:PUBLISH',
      'BEGIN:VEVENT',
      `UID:${booking.id}@barberbooker`,
      `DTSTAMP:${formatICSDate(new Date())}`,
      `DTSTART:${formatICSDate(startDateTime)}`,
      `DTEND:${formatICSDate(endDateTime)}`,
      `SUMMARY:${booking.serviceName} com ${booking.professionalName}`,
      `DESCRIPTION:Cliente: ${booking.clientName} (${booking.clientPhone})${booking.notes ? '\\nObservações: ' + booking.notes : ''}`,
      'END:VEVENT',
      'END:VCALENDAR'
    ].join('\r\n');

    const blob = new Blob([icsContent], { type: 'text/calendar' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `agendamento-${booking.date}-${booking.startTime.replace(':', '')}.ics`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  };

  const handleClearForm = () => {
    if (confirm('Limpar todos os campos e seleções?')) {
      setFormData(prev => ({
        ...prev,
        time: '',
        clientName: '',
        clientPhone: '',
        notes: '',
      }));
    }
  };

  return (
    <div className="app">
      <Header
        userName={currentUser?.displayName || currentUser?.email || 'Usuário'} 
        onLogout={logout}
      />

      <main className="grid">
        {/* Left: Booking Form */}
        <section className="card" aria-label="Formulário de agendamento">
          <div className="section">
            <h2>Escolha um serviço</h2>
            {servicesLoading ? (
              <LoadingSpinner />
            ) : (
              <ServiceSelection
                services={services}
                selectedService={formData.service}
                onSelect={handleServiceSelect}
              />
            )}
          </div>

          <div className="section">
            <h2>Escolha um profissional</h2>
            <ProfessionalSelection
              professionals={PROFESSIONALS}
              selectedProfessional={formData.professional}
              onSelect={handleProfessionalSelect}
            />
          </div>

          <div className="section">
            <h2>Data e suas informações</h2>
            <div className="inputs">
              <Calendar
                selectedDate={formData.date}
                selectedTime={formData.time}
                onDateChange={handleDateChange}
                onTimeSelect={handleTimeSelect}
                serviceDuration={selectedService?.duration || 30}
                professionalId={formData.professional}
                bookedSlots={getBookedSlots()}
              />
              
              <div className="field">
                <label htmlFor="clientName">Seu nome</label>
                <input
                  id="clientName"
                  type="text"
                  placeholder="ex. João Silva"
                  value={formData.clientName}
                  onChange={(e) => handleInputChange('clientName', e.target.value)}
                  autoComplete="name"
                />
              </div>
              
              <div className="field">
                <label htmlFor="clientPhone">Telefone</label>
                <input
                  id="clientPhone"
                  type="tel"
                  placeholder="ex. (11) 99999-9999"
                  value={formData.clientPhone}
                  onChange={(e) => handleInputChange('clientPhone', e.target.value)}
                  autoComplete="tel"
                />
              </div>
              
              <div className="field">
                <label htmlFor="notes">Observações</label>
                <input
                  id="notes"
                  type="text"
                  placeholder="Opcional: estilo desejado, etc."
                  value={formData.notes}
                  onChange={(e) => handleInputChange('notes', e.target.value)}
                />
              </div>
            </div>
          </div>

          <div className="section">
            <div className="summary">
              <div>Serviço: {selectedService?.name} ({selectedService?.duration}m)</div>
              <div>Profissional: {selectedProfessional?.name}</div>
              <div>Data: {new Date(formData.date).toLocaleDateString('pt-BR', { 
                weekday: 'short', 
                year: 'numeric', 
                month: 'short', 
                day: 'numeric' 
              })}</div>
              <div>Horário: {formData.time ? `${formData.time} – ${calculateEndTime(formData.time, selectedService?.duration || 30)}` : '—'}</div>
            </div>
            
            <div className="actions">
              <button 
                className="btn"
                disabled={!isFormValid() || loading}
                onClick={handleBooking}
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

        {/* Right: Booking List */}
        <section className="card" aria-label="Seus agendamentos">
          <div className="section">
            <h2>Seus agendamentos</h2>
            <div className="subtle">Salvos localmente. Você pode cancelar ou adicionar ao calendário.</div>
          </div>
          <div className="section">
            <BookingList
              bookings={bookings}
              onCancel={handleCancelBooking}
              onExportCalendar={generateICSFile}
            />
          </div>
        </section>
      </main>

      <footer style={{ marginTop: '22px', color: 'var(--text-dim)', fontSize: '12px', textAlign: 'center' }}>
        Sistema de agendamento com Firebase Auth • Dados persistem no navegador
      </footer>
    </div>
  );
};

export default Dashboard;
