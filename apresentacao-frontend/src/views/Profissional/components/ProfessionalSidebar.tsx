import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';

interface MenuItem {
  icon: string;
  label: string;
  active?: boolean;
  onClick?: () => void;
}

interface ProfessionalSidebarProps {
  activeItem?: string;
}

export default function ProfessionalSidebar({ activeItem = 'agenda' }: ProfessionalSidebarProps) {
  const navigate = useNavigate();

  const menuItems: MenuItem[] = [
    {
      icon: 'calendar_today',
      label: 'Agenda',
      active: activeItem === 'agenda',
      onClick: () => {
        navigate('/profissional');
      }
    },
    {
      icon: 'schedule',
      label: 'Jornada',
      active: activeItem === 'jornada',
      onClick: () => {
        navigate('/profissional/jornada');
      }
    },
    {
      icon: 'star',
      label: 'Minhas Avaliações',
      onClick: () => {
        toast.info('Esta funcionalidade estará disponível em breve.');
      }
    },
    {
      icon: 'history',
      label: 'Histórico',
      onClick: () => {
        toast.info('Esta funcionalidade estará disponível em breve.');
      }
    },
    {
      icon: 'content_cut',
      label: 'Serviços',
      onClick: () => {
        toast.info('Esta funcionalidade estará disponível em breve.');
      }
    },
    {
      icon: 'person',
      label: 'Perfil',
      onClick: () => {
        toast.info('Esta funcionalidade estará disponível em breve.');
      }
    }
  ];

  return (
    <aside className="w-64 bg-dark-800 min-h-screen border-r border-dark-600 p-6">
      <nav className="space-y-2">
        {menuItems.map((item) => (
          <a
            key={item.label}
            onClick={(e) => {
              e.preventDefault();
              item.onClick?.();
            }}
            href="#"
            className={`flex items-center gap-3 px-4 py-3 rounded-lg transition ${
              item.active
                ? 'bg-primary/10 text-primary'
                : 'hover:bg-dark-700 text-gray-400 hover:text-gray-200'
            }`}
          >
            <span className="material-icons">{item.icon}</span>
            <span className="font-medium">{item.label}</span>
          </a>
        ))}
      </nav>
    </aside>
  );
}
