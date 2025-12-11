import { useNavigate, useLocation } from 'react-router-dom';

export default function AdminSidebar() {
  const navigate = useNavigate();
  const location = useLocation();

  const menuItems = [
    { icon: 'dashboard', label: 'Dashboard', path: '/admin' },
    { icon: 'event', label: 'Agendamentos', path: '/admin/agendamentos' },
    { icon: 'people', label: 'Profissionais', path: '/admin/profissionais' },
    { icon: 'content_cut', label: 'Serviços', path: '/admin/servicos' },
    { icon: 'inventory_2', label: 'Estoque', path: '/admin/estoque' },
    { icon: 'attach_money', label: 'Financeiro', path: '/admin/financeiro' },
    { icon: 'bar_chart', label: 'Relatórios', path: '/admin/relatorios' }
  ];

  return (
    <aside className="w-64 bg-dark-800 min-h-screen border-r border-dark-600 p-6">
      <nav className="space-y-2">
        {menuItems.map((item) => {
          const isActive = location.pathname === item.path;
          return (
            <button
              key={item.path}
              onClick={() => navigate(item.path)}
              className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg transition ${
                isActive
                  ? 'bg-primary/10 text-primary'
                  : 'hover:bg-dark-700 text-gray-400 hover:text-gray-200'
              }`}
            >
              <span className="material-icons">{item.icon}</span>
              <span className="font-medium">{item.label}</span>
            </button>
          );
        })}
      </nav>
    </aside>
  );
}
