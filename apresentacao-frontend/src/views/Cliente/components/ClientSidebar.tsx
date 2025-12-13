import { toast } from 'react-toastify';

interface MenuItem {
  icon: string;
  label: string;
  active?: boolean;
  onClick?: () => void;
}

interface ClientSidebarProps {
  activeItem?: string;
}

export default function ClientSidebar({ activeItem = 'appointments' }: ClientSidebarProps) {
  const menuItems: MenuItem[] = [
    {
      icon: 'event',
      label: 'Meus Agendamentos',
      active: activeItem === 'appointments',
      onClick: () => {
        // navigateToAppointments()
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
