import React, { useState, useEffect } from 'react';
import apiClient from '@/services/api';
import LoadingSpinner from '@/components/LoadingSpinner';

export interface TimeSlot {
  time: string;
  available: boolean;
  minutes: number; // Minutes from midnight for easier calculations
}

interface CalendarProps {
  selectedDate: string;
  selectedTime?: string;
  onDateChange: (date: string) => void;
  onTimeSelect: (time: string) => void;
  serviceDuration: number;
  professionalId: string;
  bookedSlots?: string[]; // Array of booked times for the selected date
}

const Calendar: React.FC<CalendarProps> = ({
  selectedDate,
  selectedTime,
  onDateChange,
  onTimeSelect,
  serviceDuration,
  professionalId,
  bookedSlots = [],
}) => {
  const [availableSlots, setAvailableSlots] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);
  
  // Business hours configuration
  const BUSINESS_START = 9 * 60; // 9:00 AM in minutes
  const BUSINESS_END = 19 * 60; // 7:00 PM in minutes  
  const SLOT_DURATION = 30; // 30 minutes per slot

  // Fetch available time slots from API when date or professional changes
  useEffect(() => {
    if (!selectedDate || !professionalId) return;
    
    setLoading(true);
    
    apiClient.getAvailableTimeSlots(
      selectedDate,
      parseInt(professionalId),
      (slots) => {
        setAvailableSlots(slots);
        setLoading(false);
      },
      (error) => {
        console.error('Failed to fetch available slots:', error);
        setAvailableSlots([]);
        setLoading(false);
      }
    );
  }, [selectedDate, professionalId]);

  // Generate time slots
  const generateTimeSlots = (): TimeSlot[] => {
    const slots: TimeSlot[] = [];
    const today = new Date();
    const selectedDateObj = new Date(selectedDate);
    const isToday = selectedDateObj.toDateString() === today.toDateString();
    const nowMinutes = isToday ? today.getHours() * 60 + today.getMinutes() : 0;
    
    for (let minutes = BUSINESS_START; minutes < BUSINESS_END; minutes += SLOT_DURATION) {
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;
      const timeString = `${hours.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}`;
      
      // Check if this slot is available
      let available = true;
      
      // Check if it's in the past (for today only)
      if (isToday && minutes <= nowMinutes + SLOT_DURATION) {
        available = false;
      }
      
      // If using API data, check if the slot is in the availableSlots array
      if (availableSlots.length > 0) {
        available = availableSlots.includes(timeString);
      } else {
        // Fallback to checking against bookedSlots
        if (bookedSlots.includes(timeString)) {
          available = false;
        }
        
        // Don't show slots that would extend beyond business hours
        if (minutes + serviceDuration > BUSINESS_END) {
          available = false;
        }
      }
      
      slots.push({
        time: timeString,
        available,
        minutes,
      });
    }
    
    return slots;
  };

  const timeSlots = generateTimeSlots();

  // Check if selected date is a Sunday (closed)
  const selectedDateObj = new Date(selectedDate);
  const isSunday = selectedDateObj.getDay() === 0;

  // Set up date input constraints
  const today = new Date();
  const maxDate = new Date(today.getTime() + 30 * 24 * 60 * 60 * 1000); // 30 days from now
  const minDateString = today.toISOString().split('T')[0];
  const maxDateString = maxDate.toISOString().split('T')[0];

  // Render time slots based on conditions
  const renderTimeSlots = () => {
    if (loading) {
      return <LoadingSpinner />;
    }
    
    if (isSunday) {
      return (
        <div className="closed">
          Fechado neste dia. Por favor, escolha uma segunda a sábado.
        </div>
      );
    }
    
    return (
      <div className="slots" aria-label="Available time slots">
        {timeSlots.map((slot) => (
          <button
            key={slot.time}
            className={`slot ${selectedTime === slot.time ? 'selected' : ''}`}
            disabled={!slot.available}
            onClick={() => slot.available && onTimeSelect(slot.time)}
            aria-pressed={selectedTime === slot.time}
            title={slot.available ? `Selecionar horário ${slot.time}` : 'Horário indisponível'}
          >
            {slot.time}
          </button>
        ))}
      </div>
    );
  };

  return (
    <div className="calendar">
      <div className="field">
        <label htmlFor="date">Dia</label>
        <input
          id="date"
          type="date"
          value={selectedDate}
          onChange={(e) => onDateChange(e.target.value)}
          min={minDateString}
          max={maxDateString}
        />
        <div className="subtle">Fechado aos domingos</div>
      </div>
      
      <div className="time-slots">
        <h3>Horários disponíveis</h3>
        {renderTimeSlots()}
      </div>
    </div>
  );
};

export default Calendar;
