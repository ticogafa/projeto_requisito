// Main app components  
export { default as LoginForm } from '@/components/LoginForm';
export { default as Dashboard } from '@/components/Dashboard';
export { default as ProtectedRoute } from '@/components/ProtectedRoute';

// Component library
export { default as LoadingSpinner } from '@/components/LoadingSpinner';
export { default as Logo } from '@/components/Logo';
export { default as NavigationTabs } from '@/components/NavigationTabs';
export { default as ServiceSelection } from '@/components/ServiceSelection';
export { default as Calendar } from '@/components/Calendar';

// New prototype-based components
export { Header } from '@/components/Header';
export { ProfessionalSelection } from '@/components/ProfessionalSelection';
export { BookingList } from '@/components/BookingList';

// Type Exports
export type { Service } from '@/components/ServiceSelection';
export type { TimeSlot } from '@/components/Calendar';
export type { Booking } from '@/components/BookingList';
