// Main app components  
export { default as LoginForm } from '@/components/LoginForm';
export { default as Dashboard } from '@/components/Dashboard';
export { default as ProtectedRoute } from '@/components/ProtectedRoute';

// Component library
export { default as LoadingSpinner } from '@/components/LoadingSpinner/LoadingSpinner';
export { default as Logo } from '@/components/Logo/Logo';
export { default as NavigationTabs } from '@/components/Navigation/NavigationTabs';
export { default as ServiceSelection } from '@/components/ServiceSelection/ServiceSelection';
export { default as Calendar } from '@/components/Calendar/Calendar';

// New prototype-based components
export { Header } from '@/components/Header/Header';
export { ProfessionalSelection } from '@/components/ProfessionalSelection/ProfessionalSelection';
export { BookingList } from '@/components/BookingList/BookingList';

// Type Exports
export type { Service } from '@/components/ServiceSelection/ServiceSelection';
export type { Professional } from '@/components/ProfessionalSelection/ProfessionalSelection';
export type { TimeSlot } from '@/components/Calendar/Calendar';
export type { Booking } from '@/components/BookingList/BookingList';
