# Sistema Barbearia - Frontend Architecture

## 🎨 Design System

### Color Palette
- **Primary Dark**: `#1a1a2e` - Main background
- **Card Background**: `#23234a` - Component backgrounds  
- **Accent Blue**: `#007bff` - Primary actions and highlights
- **Accent Red**: `#dc3545` - Destructive actions and errors
- **Accent Green**: `#28a745` - Success states and confirmations
- **Text Light**: `#fff` - Primary text color
- **Text Muted**: `#cccccc` - Secondary text

### Typography
- **Font Family**: Inter (Google Fonts)
- **Font Sizes**: xs(12px) → sm(14px) → base(16px) → lg(18px) → xl(20px) → 2xl(24px) → 3xl(32px)
- **Font Weights**: normal(400), semibold(600), bold(700)

### Spacing System
- **xs**: 4px, **sm**: 8px, **md**: 12px, **lg**: 18px, **xl**: 24px, **2xl**: 32px

### Components
- **Border Radius**: 18px (main), 8px (small), 24px (large)
- **Shadows**: Subtle depth with `rgba(0,0,0,0.18)` opacity
- **Animations**: Smooth transitions (0.2s ease, 0.4s ease-slow)

## 🏗️ Architecture

### Directory Structure
```
src/
├── components/           # Reusable UI components
│   ├── Logo/            # Brand logo component
│   ├── LoadingSpinner/  # Loading state component
│   ├── Navigation/      # Tab navigation component
│   ├── ServiceSelection/# Service picker component
│   ├── Calendar/        # Date/time picker component
│   └── index.ts         # Component exports
├── contexts/            # React contexts (Auth, etc.)
├── services/            # API client and external services
├── styles/              # Global CSS and design system
├── types/               # TypeScript type definitions
├── utils/               # Helper functions and utilities
└── config/              # Configuration files (Firebase, etc.)
```

### Design Principles
1. **Component Composition**: Small, focused components that compose into larger features
2. **Separation of Concerns**: Clear boundaries between UI, business logic, and data
3. **Type Safety**: Comprehensive TypeScript coverage for better DX
4. **Responsive Design**: Mobile-first approach with flexible layouts
5. **Accessibility**: Semantic HTML and ARIA support

## 🔧 Component API

### Logo Component
```tsx
<Logo size="small|medium|large" showText={boolean} />
```

### NavigationTabs Component
```tsx
<NavigationTabs 
  tabs={Tab[]}
  activeTab={string}
  onTabChange={(tabId) => void}
/>
```

### ServiceSelection Component
```tsx
<ServiceSelection
  services={Service[]}
  selectedServiceId={string}
  onServiceSelect={(serviceId) => void}
/>
```

### Calendar Component
```tsx
<Calendar
  selectedDate={string}
  onDateSelect={(date) => void}
  availableTimeSlots={string[]}
  selectedTime={string}
  onTimeSelect={(time) => void}
  bookedTimes={string[]}
/>
```

## 🎯 Features Implemented

### ✅ Authentication System
- Firebase Authentication integration
- Email/password login and registration
- Protected routes with automatic redirects
- User session persistence

### ✅ Design System
- CSS custom properties for consistent theming
- Utility classes for rapid development
- Responsive grid system
- Component-scoped styling

### ✅ Service Booking Flow
- Service selection with visual cards
- Professional selection dropdown
- Calendar with date/time picking
- Real-time availability display

### ✅ Navigation System
- Tab-based navigation
- Multiple app sections (Booking, Loyalty, Products, Profile)
- Smooth transitions between sections

### ✅ API Integration Ready
- Type-safe API client with error handling
- Environment-based configuration
- Token-based authentication support
- Extensible for backend integration

## 🔄 State Management

### Current Implementation
- React Context for authentication state
- Local component state for UI interactions
- Custom hooks for reusable logic

### Recommended Scaling
- Consider Zustand for complex global state
- React Query for server state management
- Separate stores for different domains

## 🚀 Performance Optimizations

### Implemented
- Code splitting with React.lazy (ready for implementation)
- CSS custom properties for efficient re-theming
- Optimized bundle size with ES modules
- Tree-shaking friendly component exports

### Future Enhancements
- Image optimization and lazy loading
- Service worker for offline functionality
- Bundle analysis and optimization
- CDN integration for static assets

## 📱 Responsive Design

### Breakpoints
- **Mobile**: < 640px (single column, compact spacing)
- **Tablet**: 640px - 1024px (adapted grid layouts)  
- **Desktop**: > 1024px (full feature layouts)

### Mobile Optimizations
- Touch-friendly button sizing (min 44px)
- Optimized typography scale
- Simplified navigation on small screens
- Gesture-friendly interactions

## 🧪 Testing Strategy

### Unit Testing (Recommended)
- Component testing with React Testing Library
- Hook testing with @testing-library/react-hooks
- Utility function testing with Jest

### Integration Testing
- API integration tests
- Authentication flow testing
- User journey testing with Cypress

### Visual Testing
- Storybook for component documentation
- Visual regression testing
- Accessibility testing with axe-core

## 🔮 Future Enhancements

### Phase 2 Features
- Real-time notifications
- Payment integration
- Multi-language support
- Advanced booking management

### Technical Improvements
- GraphQL integration
- Progressive Web App features
- Advanced caching strategies
- Performance monitoring

### UX Enhancements
- Skeleton loading states
- Optimistic UI updates
- Advanced animations
- Voice interaction support

## 🛠️ Development Workflow

### Getting Started
```bash
npm install          # Install dependencies
npm run dev         # Start development server
npm run build       # Build for production
npm run preview     # Preview production build
```

### Code Standards
- ESLint + Prettier for code formatting
- Conventional Commits for git messages
- TypeScript strict mode enabled
- Component composition over inheritance

### Environment Configuration
- `.env` for local development
- `.env.example` for team reference
- Environment-specific builds
- Secure credential management
