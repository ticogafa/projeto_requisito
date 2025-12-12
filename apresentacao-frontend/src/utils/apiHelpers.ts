// Helper para normalizar IDs que vêm como objetos {valor: X} do backend
export const normalizeId = (id: any): number => {
  if (typeof id === 'object' && id !== null && 'valor' in id) {
    return id.valor;
  }
  return typeof id === 'number' ? id : parseInt(id, 10);
};

// Helper para normalizar array de objetos com IDs
export const normalizeIds = <T extends Record<string, any>>(items: T[]): T[] => {
  return items.map(item => {
    const normalized: T = { ...item };
    (Object.keys(normalized) as Array<keyof T>).forEach(key => {
      if (key === 'id' || (typeof key === 'string' && key.endsWith('Id'))) {
        (normalized[key] as any) = normalizeId(normalized[key]);
      }
    });
    return normalized;
  });
};
