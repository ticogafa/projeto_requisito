import React from 'react';

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

export const Calendar: React.FC<CalendarProps> = ({
  selectedDate,
  selectedTime,
  onDateChange,
  onTimeSelect,
  serviceDuration,
  bookedSlots = [],
}) => {
  // Business hours configuration
  const BUSINESS_START = 9 * 60; // 9:00 AM in minutes
  const BUSINESS_END = 19 * 60; // 7:00 PM in minutes  
  const SLOT_DURATION = 30; // 30 minutes per slot

  // Generate time slots
  const generateTimeSlots = (): TimeSlot[] => {
    const slots: TimeSlot[] = [];
    const slotsNeeded = Math.ceil(serviceDuration / SLOT_DURATION);
    const today = new Date();
    const selectedDateObj = new Date(selectedDate);
    const isToday = selectedDateObj.toDateString() === today.toDateString();
    const nowMinutes = isToday ? today.getHours() * 60 + today.getMinutes() : 0;
    
    for (let minutes = BUSINESS_START; minutes < BUSINESS_END; minutes += SLOT_DURATION) {
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;
      const timeString = `${hours.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}`;
      
      // Check if this slot and required following slots are available
      let available = true;
      
      // Check if it's in the past (for today only)
      if (isToday && minutes <= nowMinutes + SLOT_DURATION) {
        available = false;
      }
      
      // Check if this slot or any required following slots are booked
      for (let i = 0; i < slotsNeeded; i++) {
        const checkMinutes = minutes + (i * SLOT_DURATION);
        const checkHours = Math.floor(checkMinutes / 60);
        const checkMins = checkMinutes % 60;
        const checkTimeString = `${checkHours.toString().padStart(2, '0')}:${checkMins.toString().padStart(2, '0')}`;
        
        if (bookedSlots.includes(checkTimeString) || checkMinutes >= BUSINESS_END) {
          available = false;
          break;
        }
      }
      
      // Don't show slots that would extend beyond business hours
      if (minutes + (serviceDuration) > BUSINESS_END) {
        available = false;
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
        {isSunday ? (
          <div className="closed">
            Fechado neste dia. Por favor, escolha uma segunda a sábado.
          </div>
        ) : (
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
        )}
      </div>
    </div>
  );
};

export default Calendar;