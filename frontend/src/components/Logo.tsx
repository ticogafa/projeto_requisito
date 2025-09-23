import React from 'react';

interface LogoProps {
  size?: 'small' | 'medium' | 'large';
  showText?: boolean;
}

const Logo: React.FC<LogoProps> = ({ size = 'medium', showText = true }) => {
  return (
    <div className={`logo ${size}`}>
      <div className="logo-icon">
        🪒
      </div>
      {showText && (
        <div className="logo-text">
          BARBERSHOP
        </div>
      )}
    </div>
  );
};

export default Logo;
