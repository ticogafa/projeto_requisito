import { Loader2 } from 'lucide-react';

export function TheLoading() {
  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/40 backdrop-blur-sm z-9999">
      <div className="flex flex-col items-center space-y-3 bg-white/90 p-6 rounded-2xl shadow-xl">
        <Loader2 className="h-10 w-10 text-indigo-600 animate-spin" />
        <span className="text-gray-800 text-sm font-medium">Carregando...</span>
      </div>
    </div>
  );
}
