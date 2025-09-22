import type { Professional } from '@/types';
import React from 'react';


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
  if (professionals.length === 0) {
    return <div className="no-professionals">Nenhum profissional disponível</div>;
  }
  
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
          </div>
        </label>
      ))}
    </div>
  );
};

export default ProfessionalSelection;
