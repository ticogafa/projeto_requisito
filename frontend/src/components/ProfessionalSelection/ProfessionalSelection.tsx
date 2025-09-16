import React from 'react';

export interface Professional {
  id: string;
  name: string;
  role: string;
  avatar?: string;
}

interface ProfessionalSelectionProps {
  professionals: Professional[];
  selectedProfessional?: string;
  onSelect: (professionalId: string) => void;
}

export const ProfessionalSelection: React.FC<ProfessionalSelectionProps> = ({
  professionals,
  selectedProfessional,
  onSelect,
}) => {
  return (
    <div className="pro-list" role="radiogroup" aria-label="Professional">
      {professionals.map((pro) => (
        <label 
          key={pro.id} 
          className={`pro ${selectedProfessional === pro.id ? 'selected' : ''}`}
        >
          <input
            type="radio"
            name="professional"
            value={pro.id}
            checked={selectedProfessional === pro.id}
            onChange={() => onSelect(pro.id)}
            className="sr-only"
          />
          <div className="avatar">
            {pro.avatar || pro.name.charAt(0).toUpperCase()}
          </div>
          <div className="meta">
            <span className="name">{pro.name}</span>
            <span className="role">{pro.role}</span>
          </div>
        </label>
      ))}
    </div>
  );
};
