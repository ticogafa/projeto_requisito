import React from 'react';

export interface Service {
  id: string;
  name: string;
  duration: number;
  price?: number;
}

interface ServiceSelectionProps {
  services: Service[];
  selectedService?: string;
  onSelect: (serviceId: string) => void;
}

export const ServiceSelection: React.FC<ServiceSelectionProps> = ({
  services,
  selectedService,
  onSelect,
}) => {
  return (
    <div className="seg" role="radiogroup" aria-label="Service">
      {services.map((service) => (
        <label key={service.id} aria-label={`${service.name} - ${service.duration} minutos`}>
          <input
            type="radio"
            name="service"
            value={service.id}
            checked={selectedService === service.id}
            onChange={() => onSelect(service.id)}
            className="sr-only"
          />
          <div 
            className={`chip ${selectedService === service.id ? 'selected' : ''}`}
            tabIndex={0}
            onKeyDown={(e) => {
              if (e.key === ' ' || e.key === 'Enter') {
                e.preventDefault();
                onSelect(service.id);
              }
            }}
          >
            <span>{service.name}</span>
            <span className="dur">{service.duration}m</span>
          </div>
        </label>
      ))}
    </div>
  );
};

export default ServiceSelection;
